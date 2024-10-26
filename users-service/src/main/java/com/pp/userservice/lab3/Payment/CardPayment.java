package com.pp.userservice.lab3.Payment;

public class CardPayment extends AbstractPayment {
    public CardPayment(String paymentId) {
        super(paymentId);
    }

    @Override
    public String processPayment(double amount) {
        return "Processing card payment of " + amount + " with ID: " + paymentId;
    }
}
