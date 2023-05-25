package com.example.myapplication.model;

import android.util.Log;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class Message implements Chattabile, Comparable<Message> {
    private final String idInterlocutore;
    private LocalDateTime date;
    private String message;
    private boolean isSender;

    public Message(String message, String idInterlocutore, boolean isSender, String date) {
        this.idInterlocutore = idInterlocutore;
        this.message = message;
        this.isSender = isSender;
        if (date != null && date.length() == 19) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.date = LocalDateTime.parse(date, formatter);
        } else {
            Log.e("Message:", "data non accettata");
        }
    }

    public Message(String message, String idInterlocutore, boolean isSender) {
        this.idInterlocutore = idInterlocutore;
        this.message = message;
        this.isSender = isSender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSender() {
        return isSender;
    }

    public String getIdInterlocutore() {
        return idInterlocutore;
    }

    // GETTER PER DATI TEMPORALI //

    public String getMin() {
        if (date != null) {
            return String.valueOf(date.getMinute());
        }
        return "error";
    }
    public String getHour(){
        int hour = date.getHour();
        int minutes = date.getMinute();
        String ora = hour>9? Integer.toString(hour) : ("0"+hour);
        String minuti = (minutes>9? Integer.toString(minutes) : ("0"+ minutes));
        return ora+":"+minuti;
    }
    public static String getMoment(LocalDateTime dt){
        LocalDateTime now = LocalDateTime.now();
        int y = dt.getYear();
        Month m = dt.getMonth();
        int d = dt.getDayOfMonth();
        if(now.getYear()!=y){ return Integer.toString(y); }
        if(!now.getMonth().equals(m)){ m.toString(); }
        return dt.getHour()+":"+dt.getMinute();
    }

    @Override
    public int compareTo(Message m) {
        return this.date.compareTo(m.date);
    }
}