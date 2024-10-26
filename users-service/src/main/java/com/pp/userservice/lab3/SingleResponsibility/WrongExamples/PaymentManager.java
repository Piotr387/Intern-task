package com.pp.userservice.lab3.SingleResponsibility.WrongExamples;

import com.pp.userservice.lab3.SingleResponsibility.CorectedExamples.ReceiptSender;

public class PaymentManager {
    private final ReceiptSender receiptSender;

    public PaymentManager(ReceiptSender receiptSender) {
        this.receiptSender = receiptSender;
    }

    public void processPaymentAndSendReceipt(double amount, String userEmail) {
        System.out.println("Processing payment of: $" + amount);
        receiptSender.sendReceipt(userEmail, "Receipt for payment of $" + amount);
    }
}
