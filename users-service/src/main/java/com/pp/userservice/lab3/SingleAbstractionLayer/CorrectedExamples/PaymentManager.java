package com.pp.userservice.lab3.SingleAbstractionLayer.CorrectedExamples;

import com.pp.userservice.lab3.SingleResponsibility.CorectedExamples.ReceiptSender;

//l3z7
public class PaymentManager {
    private final ReceiptSender receiptSender;

    public PaymentManager(ReceiptSender receiptSender) {
        this.receiptSender = receiptSender;
    }

    public void processTransaction(double amount, String userEmail) {
        double finalAmount = calculateFinalAmount(amount);
        processPayment(finalAmount);
        issueReceipt(userEmail, finalAmount);
    }

    private double calculateFinalAmount(double amount) {
        return amount - calculateTransactionFee(amount);
    }

    private double calculateTransactionFee(double amount) {
        return amount * 0.02;
    }

    private void processPayment(double finalAmount) {
        System.out.println("Processing payment of $" + finalAmount + " in the system.");
    }

    private void issueReceipt(String userEmail, double amount) {
        receiptSender.sendReceipt(userEmail, "Receipt for payment of $" + amount);
    }
}
