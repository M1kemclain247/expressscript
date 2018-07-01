package com.m1kes.expressscript.objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.m1kes.expressscript.utils.CoreUtils;

import java.util.List;

public class Quote implements Parcelable{

    private int id;
    private String content;
    private String quotation_details;
    private boolean isSynced;
    private List<QuoteItem> items;

    public Quote() {
    }

    public Quote(int id) {
        this.id = id;
    }

    public Quote(int id, String content) {
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

    public List<QuoteItem> getItems() {
        return items;
    }

    public void setItems(List<QuoteItem> items) {
        this.items = items;
    }

    public Quote(Parcel in) {
        this.id = in.readInt();
        this.content = in.readString();
        this.quotation_details = in.readString();
        this.isSynced = CoreUtils.toBoolean(in.readInt());
        this.items = in.createTypedArrayList(QuoteItem.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(content);
        dest.writeString(quotation_details);
        dest.writeInt(CoreUtils.toInt(isSynced));
        dest.writeTypedList(items);
    }

    public static final Parcelable.Creator<Quote> CREATOR = new Parcelable.Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };


    @Override
    public String toString() {
        return "Quote{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", quotation_details='" + quotation_details + '\'' +
                ", isSynced=" + isSynced +
                ", items=" + items +
                '}';
    }
}
