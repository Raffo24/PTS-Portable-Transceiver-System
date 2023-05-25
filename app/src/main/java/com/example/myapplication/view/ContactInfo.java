package com.example.myapplication.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.controller.MainActivity;
import com.example.myapplication.model.DatiCall;
import com.example.myapplication.model.DatiContact;
import com.example.myapplication.view.Call;
import com.example.myapplication.view.Dialpad;
import com.google.android.material.button.MaterialButton;


public class ContactInfo extends Fragment {
    private View view;
    private TextView nomeContact;
    private MaterialButton chatBtn;
    private MaterialButton callBtn;
    private TextView idView;
    private DatiContact contatto;
    public ContactInfo(DatiContact contatto){
        this.contatto = contatto;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_contact_info, container, false);
        nomeContact = view.findViewById(R.id.nome_info_contact);
        chatBtn = view.findViewById(R.id.chat_info_contact);
        callBtn = view.findViewById(R.id.call_info_contact);
        idView = view.findViewById(R.id.id_info_contact);
        nomeContact.setText(contatto.getName());
        idView.setText(contatto.getId());
        callBtn.setOnClickListener(view -> {
            Call call = new Call(contatto.getId(), true);
            MainActivity.getInstance().replaceFragment(call);
            call.startChiamata();
        });
        return view;
    }
}