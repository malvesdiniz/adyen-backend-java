package com.mosselsouper.adyenbackend.model;

public class PaymentRequest {

    private String currency;
    private Integer amount;
    private String reference;

    // Getters e setters
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
}
