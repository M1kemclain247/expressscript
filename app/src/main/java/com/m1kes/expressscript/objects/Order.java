package com.m1kes.expressscript.objects;

public class Order {

    private int id;
    private String content;
    private String quotation_details;
    private boolean isSynced;

    public Order() {
    }

    public Order(int id) {
        this.id = id;
    }

    public Order(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public String getQuotationDetails() {
        return quotation_details;
    }

    public void setQuotationDetails(String quotation_details) {
        this.quotation_details = quotation_details;
    }


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", quotation_details='" + quotation_details + '\'' +
                ", isSynced=" + isSynced +
                '}';
    }
}
