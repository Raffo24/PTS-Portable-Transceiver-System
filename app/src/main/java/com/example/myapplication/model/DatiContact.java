package com.example.myapplication.model;

import com.example.myapplication.controller.DatabaseAdapter;
import com.example.myapplication.controller.MainActivity;

import java.io.Serializable;

public class DatiContact implements Serializable, Comparable<DatiContact> {
    private String name;
    private String surname;
    private String id;
    private static DatabaseAdapter DB = DatabaseAdapter.getInstance(MainActivity.getInstance());
    private static SortedArrayList<DatiContact> contacts;

    public DatiContact(String name, String surname, String id){
        this.name = name;
        this.surname = surname;
        this.id = id;
    }
    // getters
    public String getId(){ return id; }
    public String getName(){ return name; }
    public String getSurname(){ return surname; }
    public static String getNameByIdIfPresent(String id){
        if (contacts == null){
            contacts = DB.getAllContacts();
        }
        for (DatiContact c : contacts){
            if (c.getId().equals(id)) {
                if (c.getName() != null) {
                    return c.getName();
                } else{
                    return c.getId();
                }
            }
        }
        return "";
    }
    public static String getNameByIdOrNumber(String id){
        String name = getNameByIdIfPresent(id);
        if (name == "") {
            return id;
        }
        return name;
    }
    public static DatiContact getById(String id){
        for (DatiContact c : contacts){
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return new DatiContact("","",id);
    }
    public static void addContact(DatiContact contact){
        contacts.add(contact);
        DB.insertContact(contact);
    }
    public static SortedArrayList<DatiContact> getContacts() {
        if (contacts == null){
            contacts = DB.getAllContacts();
        }
        return contacts;
    }
    public static void updateDB(){
        contacts = DB.getAllContacts();
    }

    public static void createContacts(){
        contacts = DB.getAllContacts();
    }


    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public int compareTo(DatiContact c2) {
        return this.getId().trim().compareTo(c2.getId().trim());
    }
}
