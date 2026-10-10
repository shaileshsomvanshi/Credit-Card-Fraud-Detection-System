package com.frauddetection.service;

import com.frauddetection.dto.TransactionRequest;
import com.frauddetection.model.Transaction;
import com.frauddetection.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Rule-based fraud scoring engine.
 * Every rule adds points to a risk score (0-100). The final score decides the status:
 *   score >= block threshold -> BLOCKED
 *   score >= flag threshold  -> FLAGGED (needs manual review)
 *   otherwise                -> APPROVED
 */
@Service
public class FraudDetectionService {

    private static final Set<String> HIGH_RISK_CATEGORIES =
            Set.of("CRYPTO", "GAMBLING", "WIRE_TRANSFER", "GIFT_CARDS", "ADULT");

    private final TransactionRepository repository;
    private final int flagThreshold;
    private final int blockThreshold;

    public FraudDetectionService(TransactionRepository repository,
                                 @Value("${fraud.threshold.flag}") int flagThreshold,
                                 @Value("${fraud.threshold.block}") int blockThreshold) {
        this.repository = repository;
        this.flagThreshold = flagThreshold;
        this.blockThreshold = blockThreshold;
    }

    @Transactional
    public Transaction process(TransactionRequest req) {
        String digits = req.getCardNumber().replaceAll("[\\s-]", "");
        if (!digits.matches("\\d{13,19}") || !passesLuhn(digits)) {
            throw new IllegalArgumentException("Invalid card number (failed Luhn check)");
        }

        Transaction t = new Transaction();
        t.setCardHash(sha256(digits));
        t.setCardLast4(digits.substring(digits.length() - 4));
        t.setCardHolderName(req.getCardHolderName().trim());
        t.setAmount(req.getAmount().setScale(2, RoundingMode.HALF_UP));
        t.setMerchant(req.getMerchant().trim());
        t.setMerchantCategory(req.getMerchantCategory().trim().toUpperCase());
        t.setCountry(req.getCountry().trim().toUpperCase());
        t.setHomeCountry(req.getHomeCountry().trim().toUpperCase());
        t.setTransactionTime(LocalDateTime.now());

        List<String> reasons = new ArrayList<>();
        int score = evaluate(t, reasons);
        score = Math.min(score, 100);

        t.setRiskScore(score);
        t.setStatus(score >= blockThreshold ? "BLOCKED" : score >= flagThreshold ? "FLAGGED" : "APPROVED");
        t.setFraudReasons(reasons.isEmpty() ? "No suspicious signals" : String.join("; ", reasons));
        return repository.save(t);
    }

    private int evaluate(Transaction t, List<String> reasons) {
        int score = 0;
        double amount = t.getAmount().doubleValue();

        // Rule 1: large amount
        if (amount >= 5000) {
            score += 40; reasons.add("Very high amount (>= 5000)");
        } else if (amount >= 2000) {
            score += 20; reasons.add("High amount (>= 2000)");
        }

        // Rule 2: transaction outside the cardholder's home country
        if (!t.getCountry().equals(t.getHomeCountry())) {
            score += 20; reasons.add("Foreign transaction (" + t.getHomeCountry() + " -> " + t.getCountry() + ")");
        }

        // Rule 3: high-risk merchant category
        if (HIGH_RISK_CATEGORIES.contains(t.getMerchantCategory())) {
            score += 15; reasons.add("High-risk merchant category (" + t.getMerchantCategory() + ")");
        }

        // Rule 4: unusual hour (midnight - 5 AM)
        int hour = t.getTransactionTime().getHour();
        if (hour < 5) {
            score += 10; reasons.add("Unusual transaction hour (" + hour + ":00)");
        }

        // Rule 5: velocity - many transactions in a short window
        long recent = repository.countByCardHashAndTransactionTimeAfter(
                t.getCardHash(), t.getTransactionTime().minusMinutes(10));
        if (recent >= 3) {
            score += 35; reasons.add("Card used " + (recent + 1) + " times within 10 minutes");
        } else if (recent == 2) {
            score += 15; reasons.add("Card used 3 times within 10 minutes");
        }

        // Rule 6: amount far above this card's normal spending
        long history = repository.countByCardHash(t.getCardHash());
        BigDecimal avg = repository.averageApprovedAmount(t.getCardHash());
        if (history >= 3 && avg != null && avg.compareTo(BigDecimal.ZERO) > 0
                && amount > avg.doubleValue() * 5) {
            score += 25; reasons.add("Amount is more than 5x the card's average (" + avg.setScale(2, RoundingMode.HALF_UP) + ")");
        }

        // Rule 7: location change within 1 hour (possible impossible travel)
        Transaction last = repository.findFirstByCardHashOrderByTransactionTimeDesc(t.getCardHash());
        if (last != null && !last.getCountry().equals(t.getCountry())
                && Duration.between(last.getTransactionTime(), t.getTransactionTime()).toMinutes() < 60) {
            score += 30; reasons.add("Country changed from " + last.getCountry() + " to " + t.getCountry() + " within 1 hour");
        }

        // Rule 8: new card with a big purchase
        if (history == 0 && amount >= 1000) {
            score += 10; reasons.add("First transaction on this card is large");
        }

        return score;
    }

    /** Luhn checksum - the standard validity check for card numbers. */
    private boolean passesLuhn(String number) {
        int sum = 0;
        boolean alternate = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = number.charAt(i) - '0';
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }

    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    public List<Transaction> findAll() {
        return repository.findAllByOrderByTransactionTimeDesc();
    }

    public List<Transaction> findByStatus(String status) {
        return repository.findByStatusOrderByTransactionTimeDesc(status.toUpperCase());
    }

    @Transactional
    public Transaction review(Long id, String decision) {
        String d = decision.toUpperCase();
        if (!d.equals("APPROVED") && !d.equals("BLOCKED")) {
            throw new IllegalArgumentException("Decision must be APPROVED or BLOCKED");
        }
        Transaction t = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Transaction " + id + " not found"));
        t.setStatus(d);
        t.setReviewed(true);
        return repository.save(t);
    }

    public Map<String, Object> stats() {
        long approved = repository.countByStatus("APPROVED");
        long flagged = repository.countByStatus("FLAGGED");
        long blocked = repository.countByStatus("BLOCKED");
        long total = approved + flagged + blocked;
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("total", total);
        m.put("approved", approved);
        m.put("flagged", flagged);
        m.put("blocked", blocked);
        BigDecimal blockedAmount = repository.sumAmountByStatus("BLOCKED");
        m.put("blockedAmount", blockedAmount == null ? BigDecimal.ZERO : blockedAmount);
        m.put("fraudRate", total == 0 ? 0 : Math.round((flagged + blocked) * 1000.0 / total) / 10.0);
        return m;
    }
}
