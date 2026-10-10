package com.frauddetection.controller;

import com.frauddetection.dto.TransactionRequest;
import com.frauddetection.model.Transaction;
import com.frauddetection.service.FraudDetectionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class TransactionController {

    private final FraudDetectionService service;

    public TransactionController(FraudDetectionService service) {
        this.service = service;
    }

    /** Submit a transaction - it is scored immediately. */
    @PostMapping
    public ResponseEntity<Transaction> create(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.process(request));
    }

    /** List all transactions, optionally filtered by ?status=APPROVED|FLAGGED|BLOCKED */
    @GetMapping
    public List<Transaction> list(@RequestParam(required = false) String status) {
        return (status == null || status.isBlank()) ? service.findAll() : service.findByStatus(status);
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return service.stats();
    }

    /** Analyst decision on a flagged transaction: ?decision=APPROVED or BLOCKED */
    @PutMapping("/{id}/review")
    public Transaction review(@PathVariable Long id, @RequestParam String decision) {
        return service.review(id, decision);
    }
}
