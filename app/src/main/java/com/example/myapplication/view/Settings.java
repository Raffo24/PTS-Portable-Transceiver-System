package com.example.myapplication.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;

public class Settings extends Fragment {
    private View view;
    private String myId;
    private TextView myIdView;
    private TextView versione;

    public Settings(String myId){
        this.myId = myId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        myIdView = view.findViewById(R.id.ID);
        myIdView.setText(myId);
        versione = view.findViewById(R.id.versione);
        //TODO getter versione
        versione.setText("232323");
        return view;
    }
}