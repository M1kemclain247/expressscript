package com.m1kes.expressscript.adapters.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m1kes.expressscript.QuoteDetails;
import com.m1kes.expressscript.R;
import com.m1kes.expressscript.objects.Order;

import java.util.List;

public class OrdersRecyclerAdapter extends RecyclerView.Adapter<OrdersRecyclerAdapter.RecyclerViewHolder> {

    private List<Order> orders;
    private Context context;
    private RecyclerViewHolder viewHolder;

    public OrdersRecyclerAdapter(Context context, List<Order> orders) {
        this.orders = orders;
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

        Order order = orders.get(position);

        if(order!=null){
            holder.txtOrderNo.setText("#" + order.getId());
            holder.txtQuoteDetails.setText(order.getQuotationDetails());

            if(order.isSynced()){
                holder.txtQuoteStatus.setText("Complete");
            }else{
                holder.txtQuoteStatus.setText("Processing...");
            }
        }

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView txtOrderNo,txtQuoteDetails,txtQuoteStatus;

        RecyclerViewHolder(View view, final Context context){
            super(view);

            txtOrderNo = view.findViewById(R.id.txtOrderNo);
            txtQuoteDetails = view.findViewById(R.id.txtQuoteDetails);
            txtQuoteStatus = view.findViewById(R.id.txtQuoteStatus);

            if(context != null){
                final Context ctx = context;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, QuoteDetails.class));
                    }
                });
            }


        }
    }


}
