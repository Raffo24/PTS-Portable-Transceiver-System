package com.example.myapplication.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Message> chat;
    Context context;
    View view;
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    private String imageurl="default";
    private static int[] colors = {R.color.blue,R.color.purple_700, R.color.red, R.color.rossochiaro};
    public MessageAdapter(List<Message> chat, Context context) {
        this.chat = chat; this.context = context;
    }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_LEFT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_other, parent, false);
            MessageAdapter.MessageLeftViewHolder holder = new MessageAdapter.MessageLeftViewHolder(view);
            return holder;
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_user, parent, false);
            MessageAdapter.MessageRightViewHolder holder = new MessageAdapter.MessageRightViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       switch(holder.getItemViewType()){
           case MSG_TYPE_LEFT:
               MessageLeftViewHolder holderleft = (MessageLeftViewHolder) holder;
               holderleft.nickMessage.setText(chat.get(position).getIdInterlocutore());
               holderleft.nickMessage.setTextColor(colorTextById(chat.get(position).getIdInterlocutore()));
               holderleft.ora.setText(chat.get(position).getHour());
               holderleft.message.setText(chat.get(position).getMessage());
               break;
           case MSG_TYPE_RIGHT:
               MessageRightViewHolder holderright = (MessageRightViewHolder) holder;
               holderright.ora.setText(chat.get(position).getHour());
               holderright.message.setText(chat.get(position).getMessage());
               break;
       }
    }
    public static int colorTextById(String id){
        int sum = 0;
        for (char c : id.toCharArray()){
            sum += (int) c;
        }
        return  colors[sum%colors.length];
    }


    @Override
    public int getItemCount() {
        return chat.size();
    }

    public class MessageLeftViewHolder extends RecyclerView.ViewHolder {
        private TextView message;
        private TextView ora;
        private  TextView nickMessage;
        public MessageLeftViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            ora = itemView.findViewById(R.id.ora);
            nickMessage = itemView.findViewById(R.id.nick_message);
        }
    }
    public class MessageRightViewHolder extends RecyclerView.ViewHolder {
        private TextView message;
        private TextView ora;

        public MessageRightViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            ora = itemView.findViewById(R.id.ora);
        }
    }
    public int getItemViewType(int position){
        if (chat.get(position).isSender()){ return MSG_TYPE_RIGHT; }
        return MSG_TYPE_LEFT;
    }
}
