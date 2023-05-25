package com.example.myapplication.view;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.controller.RecyclerItemClickListener;
import com.example.myapplication.model.DatiChat;
import com.example.myapplication.model.SortedArrayList;
import com.example.myapplication.controller.ContactsAdapter;
import com.example.myapplication.controller.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.DatiContact;

public class ContactList extends Fragment {
    private View view;
    private Button addContactBtn;
    private SharedPreferences sp;
    private RecyclerView rv;
    private RecyclerView.Adapter mAdapter;
    private MenuItem chiamaBtn;
    private MenuItem messaggiaBtn;
    /*private MenuItem chiamaBtn;*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        SortedArrayList<DatiContact> contatti = DatiContact.getContacts();
        rv = (RecyclerView) view.findViewById(R.id.contacts_view);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.getInstance());
        rv.setLayoutManager(manager);
        rv.setHasFixedSize(true);
        mAdapter = new ContactsAdapter(contatti, MainActivity.getInstance());
        rv.setAdapter(mAdapter);
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.getInstance(), rv ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        MainActivity.getInstance().replaceFragment(new ContactInfo(contatti.get(position)));
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        addContactBtn = (Button) view.findViewById(R.id.addContact);
        addContactBtn.setOnClickListener(view -> MainActivity.getInstance().replaceFragment(new ContactAdder()));
        chiamaBtn = view.findViewById(R.id.chiama_btn);
        messaggiaBtn = view.findViewById(R.id.messaggia_btn);

        return view;
    }
    /*@Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            item.
            case R.id.chiama_btn:
                Call call = new Call(cc.getId(), true);
                call.startChiamata();
                MainActivity.getInstance().replaceFragment(call);
                break;
            case R.id.messaggia_btn:
                DatiChat cm = DatiChat.getChatById(cc.getId());
                MainActivity.getInstance().replaceFragment(Chat.getInstance(cm));
                break;
            case R.id.modifica_contatto_btn:
                MainActivity.getInstance().replaceFragment(new ContactInfo(DatiContact.getById(cc.getId())));
                break;
            case R.id.mostra_contatto:
                MainActivity.getInstance().replaceFragment(new ContactInfo(DatiContact.getById(cc.getId())));
                break;
        }
        return true;
    }*/
}