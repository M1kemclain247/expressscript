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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private List<Message> data;
    private Context context;

    public MessageAdapter(Context context, List<Message> data) {
        if(data == null || data.isEmpty() )data = new ArrayList<>();
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view,context);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_recieved, parent, false);
            return new ReceivedMessageHolder(view,context);
        }

        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Message msg = data.get(position);

        if (msg != null) {

            switch (holder.getItemViewType()) {
                case VIEW_TYPE_MESSAGE_SENT:
                    ((SentMessageHolder) holder).bind(msg);
                    break;
                case VIEW_TYPE_MESSAGE_RECEIVED:
                    ((ReceivedMessageHolder) holder).bind(msg);
            }

        }
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = (Message) data.get(position);

        if (message.getSender().equals("Me")) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class SentMessageHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.text_message_body) TextView content;
        @BindView(R.id.text_message_time) TextView time;
        private Context context;

        SentMessageHolder(View view,Context context){
            super(view);
            ButterKnife.bind(this, itemView);
            this.context = itemView.getContext();
        }

        void bind(Message message) {
            content.setText(message.getContent());
            time.setText(message.getCreationTime());
        }
    }

    class ReceivedMessageHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.text_message_body) TextView content;
        @BindView(R.id.text_message_time) TextView time;
        @BindView(R.id.txtSenderName)TextView sender;
        private Context context;

        ReceivedMessageHolder(View view,Context context){
            super(view);
            ButterKnife.bind(this, itemView);
            this.context = itemView.getContext();
        }

        void bind(Message message) {
            content.setText(message.getContent());
            time.setText(message.getDate().getShortTime());
            sender.setText(message.getSender());
        }
    }

}
