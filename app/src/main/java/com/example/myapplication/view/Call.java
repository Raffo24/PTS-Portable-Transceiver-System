package com.example.myapplication.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyberbros.PTS.PTSRadio.internals.PTSListener;
import com.cyberbros.PTS.PTSRadio.service.PTSCall;
import com.example.myapplication.controller.DatabaseAdapter;
import com.example.myapplication.controller.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.DatiCall;

public class Call extends Fragment {
    private View view;
    private ImageButton mute;
    private ImageButton vivavoce;
    private ImageButton startTalk;
    private TextView displayTime;
    private TextView nomeInterlocutoreView;
    private String nomeInterlocutore;
    private  ImageButton chiudiChiamata;
    private PTSCall callService;
    private DatabaseAdapter DB = DatabaseAdapter.getInstance(MainActivity.getInstance());
    private boolean isSender;
    boolean iAmTalking = false;
    private PTSListener callListener = callListener = (PTSListener) event -> {
        String action = event.getAction();
        MainActivity.mkToast(action);
        switch (action) {
            case PTSCall.CALL_ACCEPTED:
                callService.listen();
                break;
            case PTSCall.CALL_CLOSED:
                salvaChiamata();
                break;
            case PTSCall.CALL_REFUSED:
                DB.insertCall(new DatiCall(nomeInterlocutore,false, 0));
                break;
            case PTSCall.CALL_NO_CHANNEL:
                break;
            case PTSCall.CALL_ERROR:
                break;
            case PTSCall.CALL_VOICE_BUSY:
                break;
            case PTSCall.CALL_VOICE_FREE:
                break;
            case PTSCall.CALL_REQUEST_TIMEOUT:
                DB.insertCall(new DatiCall(nomeInterlocutore,true, 0));
                break;
        }
    };;
    public Call(String nomeInterlocutore, boolean isSender){
        this.nomeInterlocutore = nomeInterlocutore;
        this.isSender = isSender;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_call, container, false);
        displayTime = view.findViewById(R.id.tempo);
        displayTime.setText("in chiamata...");
        startTalk = view.findViewById(R.id.startTalk);
        mute = view.findViewById(R.id.mute);
        vivavoce = view.findViewById(R.id.viva_voce);
        nomeInterlocutoreView = view.findViewById(R.id.nome_interlocutore_chiamata);
        nomeInterlocutoreView.setText(nomeInterlocutore);
        chiudiChiamata = view.findViewById(R.id.avvia_chiamata);
        chiudiChiamata.setOnClickListener(view -> {
            if (callService.isOpen()){
                callService.quit();
            }
            salvaChiamata();
            MainActivity.getInstance().onBackPressed();
        });
        startTalk.setOnClickListener(view -> {
            if(!iAmTalking) {
                iAmTalking = true;
                startTalk.setImageResource(R.drawable.stop_circle_60);
                callService.talk();
            } else{
                iAmTalking = false;
                startTalk.setImageResource(R.drawable.play_circle_filled_60);
                callService.listen();
            }
        });
        mute.setOnClickListener(new View.OnClickListener() {
            boolean flag = false;
            @Override public void onClick(View view) {

                if (!flag) {
                    flag = true;
                    mute.setBackgroundResource(R.color.transparent);
                } else {
                    flag = false;
                    mute.setBackgroundResource(R.drawable.clicked);
                }
            }
        });
        vivavoce.setOnClickListener(new View.OnClickListener() {
            boolean flag = false;
            @Override public void onClick(View view) {
                if(!flag) {
                    flag = true;
                    vivavoce.setBackgroundResource(R.color.transparent);
                }
                else{
                    flag = false;
                    vivavoce.setBackgroundResource(R.drawable.clicked);
                }
            }
        });
        return view;
    }

    public void accettaChiamata(PTSCall callService){
        this.callService = callService;
        callService.setListener(callListener);
        callService.accept();
    }
    public void startChiamata(){
        this.callService = new PTSCall(nomeInterlocutore);
        callService.setListener(callListener);
        MainActivity.getInstance().getRadio().startService(callService);
    }

    public void salvaChiamata(){
        DatabaseAdapter.getInstance(MainActivity.getInstance()).insertCall(new DatiCall(nomeInterlocutore, isSender, 0));
        MainActivity.getInstance().onBackPressed();
        android.app.Fragment f = getActivity().getFragmentManager().findFragmentByTag("IncomingCall");
        if(f != null && f.isVisible()){
            MainActivity.getInstance().onBackPressed();
        }
    }
}