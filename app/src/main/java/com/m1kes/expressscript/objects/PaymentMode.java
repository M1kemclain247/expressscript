package com.m1kes.expressscript.objects;

public enum PaymentMode {

    CASH(1),
    ECO_CASH(2),
    BANK_TRANSFER(3),
    EFT_SWIPE(4);

    final int paymentId;

    PaymentMode(final int paymentId) {
        this.paymentId = paymentId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    @Override
    public String toString() {
        switch (paymentId){
            case 1: return "CASH";
            case 2: return "ECO_CASH";
            case 3: return "BANK_TRANSFER";
            case 4: return "EFT_SWIPE";
            default: return null;
        }
    }
}
