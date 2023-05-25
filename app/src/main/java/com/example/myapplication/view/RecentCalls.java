package com.example.myapplication.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.model.DatiChat;
import com.example.myapplication.model.DatiContact;
import com.example.myapplication.model.SortedArrayList;
import com.example.myapplication.controller.DatabaseAdapter;
import com.example.myapplication.controller.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.controller.CallbacksAdapter;
import com.example.myapplication.model.DatiCall;

public class RecentCalls extends Fragment {
    private View view;
    static SortedArrayList<DatiCall> rec_calls;
    private RecyclerView rv;
    private RecyclerView.Adapter mAdapter;
    private DatabaseAdapter DB = DatabaseAdapter.getInstance(MainActivity.getInstance());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recent_calls, container, false);
        rec_calls = DB.getAllCalls();
        rv = (RecyclerView) view.findViewById(R.id.recent_call_rv);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.getInstance());
        rv.setLayoutManager(manager);
        rv.setHasFixedSize(true);
        mAdapter = new CallbacksAdapter(rec_calls, MainActivity.getInstance());
        rv.setAdapter(mAdapter);
        return view;
    }
    /*@Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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