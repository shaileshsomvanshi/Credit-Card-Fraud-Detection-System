package com.frauddetection.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** SHA-256 of the card number - groups a card's history without storing the number. */
    @Column(nullable = false, length = 64)
    private String cardHash;

    @Column(nullable = false, length = 4)
    private String cardLast4;

    @Column(nullable = false)
    private String cardHolderName;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String merchant;

    @Column(nullable = false)
    private String merchantCategory;

    @Column(nullable = false, length = 2)
    private String country;

    @Column(nullable = false, length = 2)
    private String homeCountry;

    @Column(nullable = false)
    private LocalDateTime transactionTime;

    /** APPROVED, FLAGGED or BLOCKED */
    @Column(nullable = false, length = 10)
    private String status;

    private int riskScore;

    @Column(length = 600)
    private String fraudReasons;

    private boolean reviewed;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCardHash() { return cardHash; }
    public void setCardHash(String cardHash) { this.cardHash = cardHash; }
    public String getCardLast4() { return cardLast4; }
    public void setCardLast4(String cardLast4) { this.cardLast4 = cardLast4; }
    public String getCardHolderName() { return cardHolderName; }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getMerchant() { return merchant; }
    public void setMerchant(String merchant) { this.merchant = merchant; }
    public String getMerchantCategory() { return merchantCategory; }
    public void setMerchantCategory(String merchantCategory) { this.merchantCategory = merchantCategory; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getHomeCountry() { return homeCountry; }
    public void setHomeCountry(String homeCountry) { this.homeCountry = homeCountry; }
    public LocalDateTime getTransactionTime() { return transactionTime; }
    public void setTransactionTime(LocalDateTime transactionTime) { this.transactionTime = transactionTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getRiskScore() { return riskScore; }
    public void setRiskScore(int riskScore) { this.riskScore = riskScore; }
    public String getFraudReasons() { return fraudReasons; }
    public void setFraudReasons(String fraudReasons) { this.fraudReasons = fraudReasons; }
    public boolean isReviewed() { return reviewed; }
    public void setReviewed(boolean reviewed) { this.reviewed = reviewed; }
}
