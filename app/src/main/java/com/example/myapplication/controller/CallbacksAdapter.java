package com.example.myapplication.controller;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.CircularTextView;
import com.example.myapplication.R;
import com.example.myapplication.model.SortedArrayList;
import com.example.myapplication.model.DatiCall;
import com.example.myapplication.model.DatiContact;

import java.util.Random;

public class CallbacksAdapter extends RecyclerView.Adapter<CallbacksAdapter.CallbacksViewHolder> {
    SortedArrayList<DatiCall> callbacks;
    Context context;
    View view;

    public CallbacksAdapter(SortedArrayList<DatiCall> callbacks, Context context) {
        this.callbacks = callbacks;
        this.context = context;
    }

    @NonNull @Override
    public CallbacksAdapter.CallbacksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.callbacknew_layout,parent,false);
        CallbacksAdapter.CallbacksViewHolder holder = new CallbacksAdapter.CallbacksViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CallbacksAdapter.CallbacksViewHolder holder, int position) {
        String idInterlocutore = callbacks.get(position).getIdInterlocutore();
        String name = DatiContact.getNameByIdIfPresent(idInterlocutore);
        holder.contactName.setText(name.equals("") ? idInterlocutore : name);
        DatiCall c = callbacks.get(position);
        holder.callDate.setText(c.getDate().toString());
        holder.contactImage.setText(DatiContact.getNameByIdOrNumber(c.getIdInterlocutore()).substring(0,1));
        holder.contactImage.setStrokeWidth(1);
        holder.contactImage.setStrokeColor("#ffffff");
        holder.contactImage.setSolidColor(CircularTextView.getRandColor());

        if(c.isSender()){
            if(c.getDurata() > 0){
                holder.callbackMode.setImageResource(R.drawable.call_received_24_green);
            } else {
                holder.callbackMode.setImageResource(R.drawable.call_missed_24_red);
            }
        } else {
            if(c.getDurata() > 0){
                holder.callbackMode.setImageResource(R.drawable.call_received_24_green);
            }
            else{
                holder.callbackMode.setImageResource(R.drawable.block_24);
            }
        }
    }

    @Override
    public int getItemCount() {
        return callbacks.size();
    }

    public class CallbacksViewHolder extends RecyclerView.ViewHolder {
        private TextView contactName, callDate;
        private CircularTextView contactImage;
        private ImageView callbackMode;
        private ImageButton callbackOption;


        public CallbacksViewHolder(@NonNull View itemView) {
            super(itemView);
            contactImage = itemView.findViewById(R.id.contactImage);
            contactName = itemView.findViewById(R.id.contactNameCallback);
            callbackMode = itemView.findViewById(R.id.callback_mode);
            callbackOption = itemView.findViewById(R.id.callback_option);
            callbackOption.setLongClickable(false);
            callDate = itemView.findViewById(R.id.date_call);
        }
    }
}