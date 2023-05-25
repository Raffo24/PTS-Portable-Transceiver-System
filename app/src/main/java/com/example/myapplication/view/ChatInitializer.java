package com.example.myapplication.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.controller.ContactsAdapter;
import com.example.myapplication.controller.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.controller.RecyclerItemClickListener;
import com.example.myapplication.model.DatiContact;
import com.example.myapplication.model.DatiChat;

import java.util.ArrayList;
/*
* VIEW CHE SERVE A SCEGLIERE CON QUALE CONTATTO INIZIARE UNA CHAT
*/

public class ChatInitializer extends Fragment {
    // STATIC //
    private static ChatInitializer instance;
    // NOT STATIC //
    private View view;
    private SharedPreferences sp;
    private RecyclerView rv;
    private RecyclerView.Adapter mAdapter;
    /** ON CREATE */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contacts_chat, container, false);
        sp = MainActivity.getInstance().getSharedPreferences("contacts", Context.MODE_PRIVATE);
        rv = (RecyclerView) view.findViewById(R.id.contacts_view_chat);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.getInstance());
        rv.setLayoutManager(manager);
        rv.setHasFixedSize(true);
        mAdapter = new ContactsAdapter(
                DatiContact.getContacts(),
                MainActivity.getInstance()
        );
        rv.setAdapter(mAdapter);

        /** ON CLICK CONTATTO */
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.getInstance(), rv ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String id = DatiContact.getContacts().get(position).getId();
                        DatiChat cm = DatiChat.getChatById(id);
                        MainActivity.getInstance().replaceFragment(Chat.getInstance(cm));
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        return view;
    }
}