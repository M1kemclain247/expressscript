package com.m1kes.expressscript.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class QuoteItem implements Parcelable{

    private int quoteId;
    private int productID;
    private String description;
    private double quantity;
    private double unitPrice;
    private double total;

    public QuoteItem() {
    }

    public QuoteItem(int quoteId, int productID, String description, double quantity, double unitPrice, double total) {
        this.quoteId = quoteId;
        this.productID = productID;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = total;
    }

    public int getProductID() {
        return productID;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotal() {
        return total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public QuoteItem(Parcel in) {
        this.quoteId = in.readInt();
        this.productID = in.readInt();
        this.description = in.readString();
        this.quantity = in.readDouble();
        this.unitPrice = in.readDouble();
        this.total = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quoteId);
        dest.writeInt(productID);
        dest.writeString(description);
        dest.writeDouble(quantity);
        dest.writeDouble(unitPrice);
        dest.writeDouble(total);
    }

    public static final Parcelable.Creator<QuoteItem> CREATOR = new Parcelable.Creator<QuoteItem>() {
        @Override
        public QuoteItem createFromParcel(Parcel in) {
            return new QuoteItem(in);
        }

        @Override
        public QuoteItem[] newArray(int size) {
            return new QuoteItem[size];
        }
    };

    @Override
    public String toString() {
        return "QuoteItem{" +
                "quoteId=" + quoteId +
                ", productID=" + productID +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", total=" + total +
                '}';
    }
}
