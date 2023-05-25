package com.example.myapplication.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyberbros.PTS.PTSRadio.internals.PTSEvent;
import com.cyberbros.PTS.PTSRadio.internals.PTSListener;
import com.cyberbros.PTS.PTSRadio.service.PTSCall;
import com.example.myapplication.controller.DatabaseAdapter;
import com.example.myapplication.controller.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.DatiCall;

public class IncomingCall extends Fragment {
    private View view;
    private TextView nomeInterlocutore;
    private ImageButton risposta;
    private ImageButton chiudi;
    private String idInterlocutore;
    private PTSCall callService;
    private DatabaseAdapter DB = DatabaseAdapter.getInstance(MainActivity.getInstance());

    public IncomingCall(String idInterlocutore,PTSCall callService){
        this.idInterlocutore = idInterlocutore;
        this.callService = callService;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_incoming_call, container, false);
        risposta = view.findViewById(R.id.risposta);
        chiudi = view.findViewById(R.id.chiudi);
        nomeInterlocutore = view.findViewById(R.id.nome_interlocutore_prechiamata);
        nomeInterlocutore.setText(idInterlocutore);

        risposta.setOnClickListener(view -> {
            Call call = new Call(idInterlocutore, false);
            MainActivity.getInstance().replaceFragment(call);
            call.accettaChiamata(callService);
        });
        chiudi.setOnClickListener(view -> {
            callService.refuse();
            DB.insertCall(new DatiCall(idInterlocutore,false, 0));
            MainActivity.getInstance().onBackPressed();
        });
        final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.scale);
        myAnim.setDuration(500);

        risposta.startAnimation(myAnim);
        chiudi.startAnimation(myAnim);

        return view;
    }

}
