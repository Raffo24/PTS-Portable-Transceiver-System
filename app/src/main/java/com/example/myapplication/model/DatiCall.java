package com.example.myapplication.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.controller.DatabaseAdapter;
import com.example.myapplication.controller.MainActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatiCall implements Comparable<DatiCall>{

    private String idInterlocutore;
    private boolean isSender;
    private int durata; // in secondi
    private static SortedArrayList<DatiCall> calls;
    private static DatabaseAdapter DB = DatabaseAdapter.getInstance(MainActivity.getInstance());
    private LocalDateTime date;

    public DatiCall(String idInterlocutore, boolean isSender, int durata, String date) {
        this.idInterlocutore = idInterlocutore;
        this.isSender = isSender;
        this.durata = durata;
        if (date != null && date.length() == 19) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.date = LocalDateTime.parse(date, formatter);
        } else {
            Log.e("Call:", "data non accettata");
        }
    }
    public DatiCall(String idInterlocutore, boolean isSender, int durata) {
        this.idInterlocutore = idInterlocutore;
        this.isSender = isSender;
        this.durata = durata;
    }
    public String getIdInterlocutore() {
        return idInterlocutore;
    }

    public static void addCall(DatiCall call){
        DB.insertCall(call);
        DB.getLastCall();
    }
    public boolean isSender() {
        return isSender;
    }

    public int getDurata() {
        return durata;
    }

    public static SortedArrayList<DatiCall> getCalls() {
        if (calls == null){
            calls = DB.getAllCalls();
        }
        return calls;
    }
    public static void updateDB(){
        calls = DB.getAllCalls();
    }
    @Override public String toString() { return "Call{" + "number='" + idInterlocutore + '\'' + ", durata='" + durata + '\'' + '}'; }

    @Override
    public int compareTo(DatiCall o) {
        if (date == null){
            return -1;
        }
        return date.compareTo(o.getDate());
    }
    public LocalDateTime getDate(){return date;}
}


