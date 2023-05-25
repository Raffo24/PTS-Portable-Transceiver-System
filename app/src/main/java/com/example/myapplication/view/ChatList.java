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
import android.widget.Button;

import com.example.myapplication.model.SortedArrayList;
import com.example.myapplication.controller.DatabaseAdapter;
import com.example.myapplication.controller.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.controller.RecyclerItemClickListener;
import com.example.myapplication.controller.ChatListAdapter;
import com.example.myapplication.model.DatiChat;
import com.example.myapplication.model.Message;

public class ChatList extends Fragment {
    private View view;
    private SharedPreferences sp;  // a che serve??
    private RecyclerView rv;
    private RecyclerView.Adapter mAdapter;
    private Button newChatBtn;
    private DatabaseAdapter DB = DatabaseAdapter.getInstance(MainActivity.getInstance());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        sp = MainActivity.getInstance().getSharedPreferences("chat_list", Context.MODE_PRIVATE);
        rv = (RecyclerView) view.findViewById(R.id.chat_list);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.getInstance());
        rv.setLayoutManager(manager);
        rv.setHasFixedSize(true);
        newChatBtn = (Button) view.findViewById(R.id.newChatBtn);
        SortedArrayList<Message> lastMsgs = DB.getLastMessages();
        mAdapter = new ChatListAdapter(lastMsgs,MainActivity.getInstance());
        rv.setAdapter(mAdapter);
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.getInstance(), rv ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        MainActivity.getInstance().replaceFragment(Chat.getInstance(DatiChat.getChatById(lastMsgs.get(position).getIdInterlocutore())));
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        newChatBtn.setOnClickListener(view -> {
            MainActivity.getInstance().replaceFragment(new ChatInitializer());
        });
        return view;
    }

}