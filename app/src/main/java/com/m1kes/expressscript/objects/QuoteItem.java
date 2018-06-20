package com.m1kes.expressscript.objects;

public class QuoteItem {

    private String desc;
    private String price;

    public QuoteItem() {
    }

    public QuoteItem(String desc, String price) {
        this.desc = desc;
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
