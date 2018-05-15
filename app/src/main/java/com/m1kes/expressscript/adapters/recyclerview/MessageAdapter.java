package com.m1kes.expressscript.adapters.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m1kes.expressscript.R;
import com.m1kes.expressscript.objects.Message;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.RecyclerViewHolder> {

    private List<Message> data;
    private Context context;
    private MessageAdapter.RecyclerViewHolder viewHolder;

    public MessageAdapter(Context context, List<Message> data) {
        if(data == null || data.isEmpty() )data = new ArrayList<>();
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_item,parent,false);
        viewHolder = new RecyclerViewHolder(view,context);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        Message item = data.get(position);

        if (item != null) {
            holder.msgContent.setText(item.getContent());

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.txtMessageContent) TextView msgContent;
        private Context context;

        RecyclerViewHolder(View view,Context context){
            super(view);
            ButterKnife.bind(this, itemView);
            this.context = itemView.getContext();
        }
    }

}
