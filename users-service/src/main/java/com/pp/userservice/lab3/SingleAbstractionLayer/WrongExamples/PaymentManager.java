package com.pp.userservice.lab3.SingleAbstractionLayer.WrongExamples;

import com.pp.userservice.lab3.SingleResponsibility.CorectedExamples.ReceiptSender;

public class PaymentManager {
    private final ReceiptSender receiptSender;

    public PaymentManager(ReceiptSender receiptSender) {
        this.receiptSender = receiptSender;
    }

    public void processTransaction(double amount, String userEmail) {
        double finalAmount = amount - calculateTransactionFee(amount);
        processPaymentInSystem(finalAmount);
        sendReceipt(userEmail, finalAmount);
    }

    private double calculateTransactionFee(double amount) {
        return amount * 0.02;
    }

    private void processPaymentInSystem(double finalAmount) {
        System.out.println("Processing payment of $" + finalAmount + " in the system.");
    }

    private void sendReceipt(String userEmail, double amount) {
        receiptSender.sendReceipt(userEmail, "Receipt for payment of $" + amount);
    }
}
