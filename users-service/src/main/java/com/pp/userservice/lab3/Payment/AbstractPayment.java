package com.pp.userservice.lab3.Payment;

abstract class AbstractPayment implements PaymentProcessor {
    protected String paymentId;

    public AbstractPayment(String paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public abstract String processPayment(double amount);
}
