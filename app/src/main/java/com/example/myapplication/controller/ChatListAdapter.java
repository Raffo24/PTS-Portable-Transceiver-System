package com.example.myapplication.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.CircularTextView;
import com.example.myapplication.R;
import com.example.myapplication.model.DatiChat;
import com.example.myapplication.model.DatiContact;
import com.example.myapplication.model.SortedArrayList;
import com.example.myapplication.model.Message;

import java.util.Random;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.chatListViewHolder> {
    Context context;
    SortedArrayList<Message> lastMsgs;
    View view;
    public static final String path = "/drawable/rounded_textview.xml";

    public ChatListAdapter(SortedArrayList<Message> lastMsgs, Context context) {
        this.lastMsgs = lastMsgs;
        this.context = context;
    }
    @NonNull @Override
    public ChatListAdapter.chatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == R.layout.contact_active_layout) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_active_layout,parent,false);
        } else if (viewType == R.layout.contact_disabled_layout) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_disabled_layout,parent,false);
        }        ChatListAdapter.chatListViewHolder holder = new ChatListAdapter.chatListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.chatListViewHolder holder, int position) {
        Message m = lastMsgs.get(position);
        if(m != null){
            holder.contactName.setText(m.getIdInterlocutore());
            holder.lastMessage.setText(m.getMessage());
            holder.time.setText(String.format("%s %s", m.getHour(), m.getMin()));
            holder.contactImage.setText(DatiContact.getNameByIdOrNumber(m.getIdInterlocutore()).substring(0, 1));
            holder.contactImage.setStrokeWidth(1);
            holder.contactImage.setStrokeColor("#ffffff");
            holder.contactImage.setSolidColor(CircularTextView.getRandColor());
        }
    }

    @Override public int getItemCount() {
        return lastMsgs.size();
    }

   @Override public int getItemViewType(final int position) {
        return DatiChat.getChatById(lastMsgs.get(position).getIdInterlocutore()).isActive() ? R.layout.contact_active_layout : R.layout.contact_disabled_layout;
    }

    public class chatListViewHolder extends RecyclerView.ViewHolder {
        private CircularTextView contactImage;
        private TextView contactName;
        private TextView lastMessage;
        private TextView time;

        public chatListViewHolder(@NonNull View itemView) {
            super(itemView);
            contactImage = itemView.findViewById(R.id.contactImage);
            contactName = itemView.findViewById(R.id.contactNameChat);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            time = itemView.findViewById(R.id.time);
        }
    }

}
