package com.example.myapplication.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cyberbros.PTS.PTSRadio.exception.PTSChatIllegalStateException;
import com.cyberbros.PTS.PTSRadio.internals.PTSEvent;
import com.cyberbros.PTS.PTSRadio.internals.PTSListener;
import com.cyberbros.PTS.PTSRadio.service.PTSChat;
import com.example.myapplication.R;
import com.example.myapplication.controller.DatabaseAdapter;
import com.example.myapplication.controller.MainActivity;
import com.example.myapplication.controller.MessageAdapter;
import com.example.myapplication.model.DatiContact;
import com.example.myapplication.model.Message;
import com.example.myapplication.model.DatiChat;

import java.time.LocalDateTime;
import java.util.HashMap;


public class Chat extends Fragment {
    ///STATIC///
    private static HashMap<String,Chat> chats = new HashMap<>();
    ///NOT STATIC///
    private MainActivity mainActivity;
    private View view;
    private PTSChat chatService;
    private SharedPreferences sp;
    private RecyclerView rv;
    private RecyclerView.Adapter mAdapter;
    private DatiChat cc;

    private EditText messagebar;
    private ImageButton send;
    private Switch chatAttivaSwitch;
    private ImageButton menu;
    private TextView nomeC;
    private PTSListener chatListener;

    private Chat(DatiChat cc) {
        super();
        this.cc = cc;
        mainActivity = MainActivity.getInstance();
    }
    public static Chat getInstance(DatiChat cc){
        chats.putIfAbsent(cc.getId(),new Chat(cc));
        return chats.get(cc.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        nomeC = view.findViewById(R.id.nomeC);
        send = view.findViewById(R.id.send);

        startListener();
        String nome = DatiContact.getNameByIdIfPresent(cc.getId());
        if (nome == "") {
            nomeC.setText(cc.getId());
        } else {
            nomeC.setText(nome);
        }
        menu = view.findViewById(R.id.menu);
        messagebar = view.findViewById(R.id.mesagebar);
        chatAttivaSwitch = view.findViewById(R.id.chat_attiva);
        if (cc.isActive()) {
            chatAttivaSwitch.setChecked(true);
        } else {
            chatAttivaSwitch.setChecked(false);
        }
        sp = MainActivity.getInstance().getSharedPreferences("callbacks", Context.MODE_PRIVATE);
        rv = (RecyclerView) view.findViewById(R.id.chat_rv);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.getInstance());
        rv.setLayoutManager(manager);
        rv.setHasFixedSize(true);
        mAdapter = new MessageAdapter(cc.getMessaggi(), MainActivity.getInstance());
        rv.setAdapter(mAdapter);
        /**  bottoni della toolbar */

        MainActivity.getInstance().registerForContextMenu(menu);
        menu.setLongClickable(false);
        chatAttivaSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                cc.setActive(false);
                try{
                    chatService.quit();
                }
                catch (PTSChatIllegalStateException e){
                    // TODO gestire chiusura inaspettata
                    Log.e("Chat chatAttivaSwitch", "TODO: gestire chiusura inaspettata");
                }
                chatService = new PTSChat(cc.getId());
                Log.d("Chat:","Disattivata");
            } else {
                for (DatiChat c : DatiChat.getChats()) {
                    if (c.isActive()) {
                        chatAttivaSwitch.setChecked(false);
                        Toast.makeText(MainActivity.getInstance(), "Un'altra chat è già attiva", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                cc.setActive(true);
                startChat();
                Log.e("ID:", cc.getId());
            }
        });
        send.setOnClickListener(view -> {
            if (chatService != null) {
                cc.addMsg(messagebar.getText().toString(), cc.getId(), true);
                MainActivity.getInstance().runOnUiThread(() -> mAdapter.notifyDataSetChanged());
                Log.e("Chat empty:", messagebar.getText().toString());
                chatService.send(messagebar.getText().toString());
            }
            messagebar.setText("");
        });
        return view;
    }

    public void accettaRichiesta(PTSChat chatService) {
        this.chatService = chatService;
        startListener();
        chatService.setListener(chatListener);
        chatService.accept();
    }

    public void startChat() {
        chatService = new PTSChat(cc.getId());
        chatService.setListener(chatListener);
        MainActivity.getInstance().getRadio().startService(chatService);
    }
    public void startListener(){
        this.chatListener = event -> {
            String action = event.getAction();
            switch (action) {
                case PTSChat.CHAT_ACCEPTED:
                    cc.setActive(true);
                    MainActivity.getInstance().runOnUiThread(() -> chatAttivaSwitch.setChecked(true));
                    break;
                case PTSChat.CHAT_REFUSED:
                    MainActivity.getInstance().runOnUiThread(() -> {
                        cc.setActive(false);
                        MainActivity.getInstance().runOnUiThread(() -> chatAttivaSwitch.setChecked(false));
                    });
                    MainActivity.mkToast("Richiesta di conversazione rifiutata");
                    break;
                case PTSChat.CHAT_MESSAGE:
                    String msg = (String) event.getPayloadElement(0);
                    Log.d("MSG: ", msg);
                    cc.addMsg(msg, cc.getId(), false);
                    MainActivity.getInstance().runOnUiThread(() ->  mAdapter.notifyDataSetChanged());
                    break;
                case PTSChat.CHAT_CLOSED:
                    cc.setActive(false);
                    MainActivity.getInstance().runOnUiThread(() -> {chatAttivaSwitch.setChecked(false);});
                    MainActivity.mkToast("Chat chiusa");
                    break;
                default:
                    MainActivity.mkToast(action);
            }
        };
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chiama_btn:
                Call call = new Call(cc.getId(), false);
                MainActivity.getInstance().replaceFragment(call);
                call.startChiamata();
                break;
            case R.id.modifica_contatto_btn:
                MainActivity.getInstance().replaceFragment(new ContactInfo(DatiContact.getById(cc.getId())));
                break;
            case R.id.mostra_contatto:
                MainActivity.getInstance().replaceFragment(new ContactInfo(DatiContact.getById(cc.getId())));
                break;
            case R.id.del_msg_btn:
                DatiChat.delMsgOfId(cc.getId());
                break;
        }
        return true;
    }
}