package com.example.myapplication.model;
import com.example.myapplication.controller.DatabaseAdapter;
import com.example.myapplication.controller.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class DatiChat implements Comparable<DatiChat>{
    private String idInterlocutore;
    private boolean active = false;
    private List<Message> messaggi;
    private static SortedArrayList<DatiChat> chats;
    private static DatabaseAdapter DB = DatabaseAdapter.getInstance(MainActivity.getInstance());

    private DatiChat(String idIntelocutore, List<Message> messaggi) {
        this.messaggi = messaggi;
        this.idInterlocutore = idIntelocutore;
    }
    public static DatiChat getInstance(String idIntelocutore){
        if (chats == null || chats.isEmpty()) {
            chats = DB.getAllChats();
        }
        for (DatiChat c : chats){
            if (c.getId().equals(idIntelocutore)){
                return c;
            }
        }
        return new DatiChat(idIntelocutore,new ArrayList<>());
    }
    public static DatiChat getInstance(String idIntelocutore, List<Message> messaggi){
        return new DatiChat(idIntelocutore,messaggi);
    }

    public static SortedArrayList<DatiChat> getChats() {
        if (chats == null || chats.isEmpty()){
            chats = DB.getAllChats();
        }
        return chats;
    }

    public String getId() {
        return idInterlocutore;
    }

    public List<Message> getMessaggi() {
        return messaggi;
    }

    public static void updateDB(){
        chats = DB.getAllChats();
    }

    public void addMsg(String content, String idInterlocutore, boolean isSender){
        DB.insertMessage(content, idInterlocutore, isSender);
        messaggi.add(DB.getLastMsg());
    }
    public static void delMsgOfId(String id){
        DatabaseAdapter.getInstance(MainActivity.getInstance()).delMsgOfId(id);
        updateDB();
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active){
        this.active=active;
    }

    public static DatiChat getChatById(String id){
        if (chats == null || chats.isEmpty()){
            chats = DB.getAllChats();
        }
        for (DatiChat c: chats){
            if (c.getId().equals(id)) {
                return c;
            }
        }
        DatiChat x = new DatiChat(id, new ArrayList<>());
        chats.add(x);
        return x;
    }


    @Override
    public int compareTo(DatiChat t2) {
        return this.messaggi.get(0).compareTo(t2.messaggi.get(0));
    }
}
