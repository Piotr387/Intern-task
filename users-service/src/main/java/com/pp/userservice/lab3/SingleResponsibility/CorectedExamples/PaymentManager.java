package com.pp.userservice.lab3.SingleResponsibility.CorectedExamples;

public class PaymentManager {
    private final ReceiptSender receiptSender;

    public PaymentManager(ReceiptSender receiptSender) {
        this.receiptSender = receiptSender;
    }

    public void processPayment(double amount) {
        System.out.println("Processing payment of: $" + amount);
    }

public void sendPaymentReceipt(String userEmail, double amount) {
    receiptSender.sendReceipt(userEmail, "Receipt for payment of $" + amount);
}

public void completePaymentProcess(double amount, String userEmail) {
    processPayment(amount);
    sendPaymentReceipt(userEmail, amount);
}
}
