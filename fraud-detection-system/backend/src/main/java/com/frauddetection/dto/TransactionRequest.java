package com.frauddetection.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class TransactionRequest {

    @NotBlank(message = "Card number is required")
    private String cardNumber;

    @NotBlank(message = "Card holder name is required")
    private String cardHolderName;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank(message = "Merchant is required")
    private String merchant;

    @NotBlank(message = "Merchant category is required")
    private String merchantCategory;

    @NotBlank(message = "Transaction country is required")
    @Size(min = 2, max = 2, message = "Country must be a 2-letter code (e.g. IN)")
    private String country;

    @NotBlank(message = "Home country is required")
    @Size(min = 2, max = 2, message = "Home country must be a 2-letter code (e.g. IN)")
    private String homeCountry;

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
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
}
