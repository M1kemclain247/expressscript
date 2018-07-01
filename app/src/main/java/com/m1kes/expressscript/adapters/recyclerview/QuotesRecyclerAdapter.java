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
import com.m1kes.expressscript.objects.Quote;
import com.m1kes.expressscript.objects.QuoteItem;

import java.io.File;
import java.util.List;

public class QuotesRecyclerAdapter extends RecyclerView.Adapter<QuotesRecyclerAdapter.RecyclerViewHolder> {

    private List<Quote> quotes;
    private Context context;
    private RecyclerViewHolder viewHolder;

    public QuotesRecyclerAdapter(Context context, List<Quote> quotes) {
        this.quotes = quotes;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_row,parent,false);
        viewHolder = new QuotesRecyclerAdapter.RecyclerViewHolder(view,context);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        Quote quote = quotes.get(position);

        if(quote !=null){
            holder.txtOrderNo.setText("#" + quote.getId());


            File file = new File(quote.getQuotationDetails());
            if(file.exists()){
                System.out.println("File Exists!");
                System.out.println("Loading image");
                Glide.with(context)
                        .load(file)
                        .into(holder.quoteThumbnail);
            }

            holder.txtQuoteDetails.setText(quote.getQuotationDetails());



            if(quote.isSynced()){
                holder.txtQuoteStatus.setText("Complete");
            }else{
                holder.txtQuoteStatus.setText("Processing...");
            }


        }

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
                        Quote quote =  quotes.get(getAdapterPosition());
                        if(quote == null)return;

                        System.out.println("sending accross this quote : " + quote);

                        Intent i = new Intent(context, QuoteDetails.class);
                        i.putExtra("quote",quote);
                        context.startActivity(i);
                    }
                });
            }


        }
    }


}
