package com.m1kes.expressscript.adapters.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.m1kes.expressscript.QuoteDetails;
import com.m1kes.expressscript.R;
import com.m1kes.expressscript.objects.OrderWrapper;
import com.m1kes.expressscript.objects.Quote;
import com.m1kes.expressscript.objects.QuoteItem;

import java.io.File;
import java.util.List;

public class OrdersRecyclerAdapter extends RecyclerView.Adapter<OrdersRecyclerAdapter.RecyclerViewHolder> {

    private List<OrderWrapper> quotes;
    private Context context;
    private RecyclerViewHolder viewHolder;

    public OrdersRecyclerAdapter(Context context, List<OrderWrapper> quotes) {
        this.quotes = quotes;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_row,parent,false);
        viewHolder = new OrdersRecyclerAdapter.RecyclerViewHolder(view,context);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        OrderWrapper wrapper = quotes.get(position);
        holder.txtOrderNo.setText("#" + wrapper.getId());
        holder.txtQuoteStatus.setText(wrapper.getStatus() == null  || wrapper.getStatus().equalsIgnoreCase("") ? "Processing..." : wrapper.getStatus() );

    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView txtOrderNo,txtQuoteDetails,txtQuoteStatus;
        ImageView quoteThumbnail;

        RecyclerViewHolder(View view, final Context context){
            super(view);

            txtOrderNo = view.findViewById(R.id.txtOrderNo);
            txtQuoteDetails = view.findViewById(R.id.txtQuoteDetails);
            txtQuoteStatus = view.findViewById(R.id.txtQuoteStatus);
            quoteThumbnail = view.findViewById(R.id.quoteThumbnail);

            if(context != null){
                final Context ctx = context;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderWrapper wrapper =  quotes.get(getAdapterPosition());
                        if(wrapper == null)return;

                    }
                });
            }


        }
    }


}