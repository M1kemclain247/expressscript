package com.m1kes.expressscript.adapters.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m1kes.expressscript.R;
import com.m1kes.expressscript.objects.QuoteItem;

import java.text.DecimalFormat;
import java.util.List;

public class QuoteItemsRecyclerAdapter extends RecyclerView.Adapter<QuoteItemsRecyclerAdapter.RecyclerViewHolder> {

    private List<QuoteItem> orders;
    private Context context;
    private RecyclerViewHolder viewHolder;

    public QuoteItemsRecyclerAdapter(Context context, List<QuoteItem> orders) {
        this.orders = orders;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_quote_item,parent,false);
        viewHolder = new QuoteItemsRecyclerAdapter.RecyclerViewHolder(view,context);
        return viewHolder;
    }
    private DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        QuoteItem order = orders.get(position);

        if(order!=null){
            holder.txtItemDesc.setText(order.getDescription());
            holder.txtItemPrice.setText("$" + df.format(order.getTotal()) );
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView txtItemDesc,txtItemPrice;

        RecyclerViewHolder(View view,Context context){
            super(view);

            txtItemDesc = view.findViewById(R.id.txtItemDesc);
            txtItemPrice = view.findViewById(R.id.txtItemPrice);
        }
    }
}