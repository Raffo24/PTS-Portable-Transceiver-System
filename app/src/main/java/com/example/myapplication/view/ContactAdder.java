package com.example.myapplication.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.controller.DatabaseAdapter;
import com.example.myapplication.controller.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.DatiContact;
import com.google.android.material.textfield.TextInputEditText;

public class ContactAdder extends Fragment {
    private View view;
    private TextInputEditText nomeET;
    private TextInputEditText cognomeET;
    private TextInputEditText numeroET;
    private Button aggiungiBtn;
    private Button annullaBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** tengo l'instanza della view perchè un fragment non ha il metodo findViewById */
        view = inflater.inflate(R.layout.fragment_add_contact, container, false);

        /** bottoni */
        aggiungiBtn = (Button) view.findViewById(R.id.btn_aggiungi);
        annullaBtn = (Button) view.findViewById(R.id.btn_annulla);
        nomeET = (TextInputEditText) view.findViewById(R.id.nomeText);
        cognomeET = (TextInputEditText) view.findViewById(R.id.cognomeText);
        numeroET = (TextInputEditText) view.findViewById(R.id.telefonoText);

        /** ON CLICK AGGIUNGI */
        aggiungiBtn.setOnClickListener(view -> {
            if (nomeET.getText().toString().equals("") || cognomeET.getText().toString().equals("") || numeroET.getText().toString().equals("")) {
                MainActivity.mkToast("TUTTI I CAMPI SONO OBBLIGATORI");
            } else if (numeroET.getText().toString().length() != 5) {
                MainActivity.mkToast("L'identificativo deve essere di 5 caratteri");
            } else {
                DatiContact c = new DatiContact(nomeET.getText().toString(), cognomeET.getText().toString(), numeroET.getText().toString());
                DatiContact.addContact(c);
                MainActivity.getInstance().replaceFragment(new ContactList());
            }
        });

        /**ON CLICK ANNULLA*/
        annullaBtn.setOnClickListener(view ->
                MainActivity.getInstance().replaceFragment(new ContactList())
        );
        return view;
    }
}