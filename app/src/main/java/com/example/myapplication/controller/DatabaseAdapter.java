package com.example.myapplication.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myapplication.model.SortedArrayList;
import com.example.myapplication.model.DatiCall;
import com.example.myapplication.model.DatiContact;
import com.example.myapplication.model.Message;
import com.example.myapplication.model.DatiChat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseAdapter extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "messages_db";
    private static final String MSG_TABLE = "msg";
    private static final String CONTACTS_TABLE = "contacts";
    private static final String CALLS_TABLE = "calls";
    private static DatabaseAdapter instance;

    public static DatabaseAdapter getInstance(Context context){
        if(instance==null) instance = new DatabaseAdapter(context);
        return instance;
    }

    private DatabaseAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + MSG_TABLE +
                "(id integer PRIMARY KEY, number text NOT NULL, content text NOT NULL, isSender boolean NOT NULL, datetime default current_timestamp)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + CONTACTS_TABLE +
                "(id integer PRIMARY KEY, number text NOT NULL, name text, surname text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + CALLS_TABLE +
                "(id integer PRIMARY KEY, number text NOT NULL, durata integer NOT NULL, isSender boolean NOT NULL, datetime default current_timestamp)");
    }
    /* return -1 if an error occurred */
    public long insertMessage(String content, String idInterlocutore, boolean isSender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", idInterlocutore );
        values.put("content", content);
        values.put("isSender", isSender);
        long id = db.insert(MSG_TABLE, null, values);
        db.close();
        return id;
    }
    public Message getLastMsg(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + MSG_TABLE + " WHERE datetime = (SELECT max(datetime) FROM " + MSG_TABLE + ")", null);
        cursor.moveToFirst();
        String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
        String number = cursor.getString(cursor.getColumnIndexOrThrow("number"));
        String date = cursor.getString(cursor.getColumnIndexOrThrow("datetime"));
        boolean isSender = cursor.getInt(cursor.getColumnIndexOrThrow("isSender")) == 1;
        db.close();
        return new Message(content, number, isSender, date);
    }
    public DatiCall getLastCall(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CALLS_TABLE + " WHERE datetime = (SELECT max(datetime) FROM " + CALLS_TABLE + ")", null);
        cursor.moveToFirst();
        int durata = cursor.getInt(cursor.getColumnIndexOrThrow("durata"));
        String number = cursor.getString(cursor.getColumnIndexOrThrow("number"));
        String date = cursor.getString(cursor.getColumnIndexOrThrow("datetime"));
        boolean isSender = cursor.getInt(cursor.getColumnIndexOrThrow("isSender"))==1;
        db.close();
        return new DatiCall(number, isSender, durata, date);
    }
    /* return -1 if an error occurred */
    public long insertContact(DatiContact c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", c.getId());
        values.put("name", c.getName());
        values.put("surname", c.getSurname());
        long id = db.insert(CONTACTS_TABLE, null, values);
        db.close();
        return id;
    }
    /* return -1 if an error occurred */
    public long insertCall(DatiCall c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", c.getIdInterlocutore());
        values.put("durata", c.getDurata());
        values.put("isSender", c.isSender());
        long id = db.insert(CALLS_TABLE, null, values);
        db.close();
        return id;
    }

    public boolean delMsgOfId(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MSG_TABLE, "number = " + id, null) > 0;
    }

    public SortedArrayList<Message> getAllMessagesById(String number) {
        SortedArrayList<Message> messages = new SortedArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + MSG_TABLE + " WHERE number = " + number + " ORDER BY datetime DESC;", null);
        if (cursor.moveToFirst()) {
            do {
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("datetime"));
                int isSender = cursor.getInt(cursor.getColumnIndexOrThrow("isSender"));
                messages.add(new Message(content, number, (isSender==1), date));
            } while (cursor.moveToNext());
        }
        db.close();
        return messages;
    }

    public SortedArrayList<DatiContact> getAllContacts() {
        SortedArrayList<DatiContact> datiContacts = new SortedArrayList<>();
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT  * FROM " + CONTACTS_TABLE + " ORDER BY name;", null);
        if(cursor.moveToFirst())

            {
                do {
                    String surname = cursor.getString(cursor.getColumnIndexOrThrow("surname"));
                    String number = cursor.getString(cursor.getColumnIndexOrThrow("number"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    datiContacts.add(new DatiContact(name, surname, number));
                } while (cursor.moveToNext());
            }
        db.close();
        return datiContacts;
        }

    public SortedArrayList<Message> getLastMessages() {
        SortedArrayList<Message> lastMessages = new SortedArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + MSG_TABLE +
                " GROUP BY number HAVING datetime = max(datetime);", null);
        if (cursor.moveToFirst()) {
            do {
                String number = cursor.getString(cursor.getColumnIndexOrThrow("number"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("datetime"));
                int isSender = cursor.getInt(cursor.getColumnIndexOrThrow("isSender"));
                lastMessages.add(new Message(content, number, (isSender == 1), date));
            } while (cursor.moveToNext());
        }
        db.close();
        return lastMessages;
    }

    public SortedArrayList<DatiCall> getAllCalls() {
        SortedArrayList<DatiCall> calls = new SortedArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + CALLS_TABLE + " ORDER BY datetime DESC;", null);
        if (cursor.moveToFirst()) {
            do {
                String number = cursor.getString(cursor.getColumnIndexOrThrow("number"));
                int durata = cursor.getInt(cursor.getColumnIndexOrThrow("durata"));
                boolean isSender = cursor.getInt(cursor.getColumnIndexOrThrow("isSender"))==1;
                String date = cursor.getString(cursor.getColumnIndexOrThrow("datetime"));
                DatiCall call = new DatiCall(number,isSender,durata, date);
                calls.add(call);
            } while (cursor.moveToNext());
        }
        db.close();
        return calls;
    }

    public SortedArrayList<DatiChat> getAllChats(){
        SortedArrayList<DatiChat> chats = new SortedArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + MSG_TABLE + " ORDER BY datetime DESC;", null);
        HashMap<String, List<Message>> map = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                String number = cursor.getString(cursor.getColumnIndexOrThrow("number"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("datetime"));
                int isSender = cursor.getInt(cursor.getColumnIndexOrThrow("isSender"));
                if (map.containsKey(number)){
                    map.get(number).add(new Message(content, number, (isSender==1), date));
                } else{
                    map.put(number, new ArrayList<>(List.of(new Message(content, number, (isSender==1), date))));
                }
            } while (cursor.moveToNext());
        }
        System.out.println(map);
        for (String k : map.keySet()){
            chats.add(DatiChat.getInstance(k, map.get(k)));
        }
        db.close();
        return chats;
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + MSG_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CALLS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE);
        // Create tables again
        onCreate(db);
    }

    public static void loadIntoRam(){
        DatiChat.updateDB();
        DatiCall.updateDB();
        DatiContact.updateDB();
    }
}