package com.example.myapplication.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;

import com.example.myapplication.R;
import com.example.myapplication.controller.MainActivity;
import com.example.myapplication.model.DatiCall;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Dialpad extends Fragment {
    private View dialpad;
    private TextInputEditText text;
    private TextInputLayout textLayout;
    private GridView numbersGrid;
    private ImageButton call;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dialpad = inflater.inflate(R.layout.fragment_dialpad, container, false);
        text = dialpad.findViewById(R.id.numero);
        textLayout = dialpad.findViewById(R.id.layoutNumero);
        text.setShowSoftInputOnFocus(false);
        numbersGrid = dialpad.findViewById(R.id.numbersGrid);
        call = dialpad.findViewById(R.id.avvia_chiamata);
        call.setOnClickListener(view -> {
            Call call = new Call(text.getText().toString(), true);
            MainActivity.getInstance().replaceFragment(call);
            call.startChiamata();
        });
        return dialpad;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListAdapter adapter = new ArrayAdapter<String>(getContext(), R.layout.grid_numbers_item, R.id.button3, new String[]{"1", "2", "3","4", "5", "6","7", "8", "9","*", "0", "#"});
        numbersGrid.setAdapter(adapter);
    }
}