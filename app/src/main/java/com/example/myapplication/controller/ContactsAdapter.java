package com.example.myapplication.controller;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.CircularTextView;
import com.example.myapplication.view.ContactInfo;
import com.example.myapplication.R;
import com.example.myapplication.model.SortedArrayList;
import com.example.myapplication.model.DatiContact;

import java.util.Random;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    SortedArrayList<DatiContact> datiContacts;
    Context context;
    View view;
    public ContactsAdapter(SortedArrayList<DatiContact> datiContacts, Context context) {
        this.datiContacts = datiContacts; this.context = context;
    }

    @NonNull @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_contact,parent,false);
        ContactsViewHolder holder = new ContactsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        DatiContact c = datiContacts.get(position);
        String name_surname = c.getName() + " " + c.getSurname();
        holder.contactName.setText(name_surname);
        holder.contactName.setOnClickListener(view ->
                MainActivity.getInstance().replaceFragment(new ContactInfo(c))
        );
        holder.contactImage.setText(String.format("%s%s", name_surname, c.getId()).substring(0, 1));
        holder.contactImage.setStrokeWidth(1);
        holder.contactImage.setStrokeColor("#ffffff");
        holder.contactImage.setSolidColor(CircularTextView.getRandColor());
    }
    @Override public int getItemCount() {
        return datiContacts.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        private CircularTextView contactImage;
        private TextView contactName;
        private ImageButton contactOption;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            contactImage = itemView.findViewById(R.id.contactImage);
            contactName = itemView.findViewById(R.id.contactName);
            contactOption = itemView.findViewById(R.id.contactOption);
            MainActivity.getInstance().registerForContextMenu(contactOption);
        }
    }
}
