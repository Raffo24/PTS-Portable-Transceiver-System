package com.example.myapplication.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.cyberbros.PTS.PTSRadio.PTSRadio;
import com.cyberbros.PTS.PTSRadio.internals.PTSEvent;
import com.cyberbros.PTS.PTSRadio.internals.PTSListener;
import com.cyberbros.PTS.PTSRadio.io.PTSSerialSimulator;
import com.cyberbros.PTS.PTSRadio.service.PTSCall;
import com.cyberbros.PTS.PTSRadio.service.PTSChat;
import com.example.myapplication.CircularTextView;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.model.DatiCall;
import com.example.myapplication.model.DatiChat;
import com.example.myapplication.view.Call;
import com.example.myapplication.view.Chat;
import com.example.myapplication.view.ChatList;
import com.example.myapplication.view.ContactList;
import com.example.myapplication.view.Dialpad;
import com.example.myapplication.view.IncomingCall;
import com.example.myapplication.view.RecentCalls;
import com.example.myapplication.view.Settings;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static MainActivity instance;

    private ActivityMainBinding binding;
    private PTSChat chatService; //not delete
    private PTSCall callService;
    private PTSRadio radio;
    private static String myID;
    private ImageButton impostazioni;
    private ImageButton backBtn;
    private FragmentManager fm = getSupportFragmentManager();

    public static String getMyId() {
        return myID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.main_color));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavigationView.setOnItemSelectedListener(Item->{
            switch(Item.getItemId()){
                case R.id.contactBtn:
                    replaceFragment(new ContactList());
                    break;
                case R.id.recentCallBtn:
                    replaceFragment(new RecentCalls());
                    break;
                case R.id.dialpadBtn:
                    replaceFragment(new Dialpad());
                    break;
                case R.id.chatListBtn:
                    replaceFragment(new ChatList());
                    break;
            }
            return true;
        });
        backBtn = findViewById(R.id.indietro);
        // da cambiare se no l'animazione non va
        backBtn.setOnClickListener(view -> onBackPressed());

        /**
         * instanza da utilizzare da un altro fragment, per alcuni metodi che necessitano di una activity
         */
        instance = this;
        /**
         * bottoni della toolbar
         */
        PTSListener radioListener = event -> {
            String action = event.getAction();
            mkToast(action);
            if (action.equals(PTSRadio.CONNECTED)) {
                myID = radio.getRadioID();
            } else if (action.equals(PTSRadio.DISCONNECTED)) {
                // Radio disconnessa, funzionità non piu accessibili
                // Notifica disconnessione
            } else if (action.equals(PTSRadio.ERROR_USB)) {
                // Errore durante procedure di inizializzazione USB
                Exception ex = (Exception) event.getPayloadElement(0);
                // Notifica eccezione
            } else if (action.equals(PTSRadio.REQUEST_CHAT)) {
                // Ricevuta richiesta chat
                this.chatService = (PTSChat) event.getPayloadElement(0);
                gestisciRichiestaChat();
            }
            else if(action.equals(PTSRadio.REQUEST_CALL)){
                this.callService = (PTSCall) event.getPayloadElement(0);
                replaceFragment(new IncomingCall(callService.getMemberID(),callService), "IncomingCall");
            }
            else if(action.equals(PTSRadio.REQUEST_GROUP)){
                //TODO come vogliamo si svolga la dinamica? si deve accettare la richiesta o come su zozzap vieni aggiunto e bha?
                //replaceFragment();
            }
            else if(action.equals(PTSRadio.AUDIO_ATTACHED)){

            }
            else if(action.equals(PTSRadio.ERROR_AUDIO)){

            }
            else if(action.equals(PTSRadio.MISSING_AUDIO_PERMISSION)){

            }
            else if(action.equals(PTSRadio.AUDIO_DETACHED)){

            }
            else if(action.equals(PTSRadio.MISSING_USB_PERMISSION)){

            }
            else if(action.equals(PTSRadio.USER_OFFLINE)){
                //TODO gestire user_offline
            }
            else if(action.equals(PTSRadio.USER_ONLINE)){
                //TODO gestire user_online
            }

            // Esistono altri eventi gestibili, lista completa nella sezione "Classi"
        };

        radio = new PTSRadio(this);
        radio.setRadioListener(radioListener);
        /**
         * simulazione
         */
        //radio.startSimulation(PTSSerialSimulator.FLAG_CHAT_REQUEST_SEND);
        /**
         * effettivo
         */
        radio.setRadioListener(radioListener);
        if (!radio.start()) {
            // Device USB assente o no trovato
            mkToast("Device USB assente o non trovato");
        } else {
            mkToast("Device USB trovato");
            myID = radio.getRadioID();
        }
        /** scommentare le righe precedenti per attivare o disattivare la modalità simulazione */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        impostazioni = findViewById(R.id.impostazioni);
        impostazioni.setOnClickListener(view -> {
            replaceFragment(new Settings(myID));
        });
        //replaceFragment(new ContactList());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);
            return;
        }
    }
    /**
     * aggiunge il numero digitato sulla tastiera al numero da comporre
     * @param v
     */
    public void addNumber(View v) {
        TextInputEditText text = findViewById(R.id.numero);
        TextInputLayout textLayout = findViewById(R.id.layoutNumero);
        //textLayout.setEndIconVisible(true);
        Button btn = (Button) findViewById(v.getId());
        text.append(btn.getText());
        if (text.getText().length() != 5) {
            textLayout.setError("Id has 5 numbers");
        } else { textLayout.setError(null); }
    }

    /**
     * metodo di utilità per cambiare frammento, non può essere chiamato dai frammenti stessi
     * perchè getSupportFragmentManager è un metodo della classe activity
     * @param fragment
     */
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        ft.replace(R.id.framePrincipale, fragment).addToBackStack(fragment.getTag()).commit();
    }
    public void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        ft.replace(R.id.framePrincipale, fragment, tag).addToBackStack(fragment.getTag()).commit();

    }
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != PackageManager.PERMISSION_GRANTED) return;

        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            int res = grantResults[i];

            if (perm.equals(Manifest.permission.RECORD_AUDIO) && res != PackageManager.PERMISSION_GRANTED) {
                //TODO FAI ROBA
                Toast.makeText(this, "Permessi non garantiti", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        switch (v.getId()){
            case R.id.menu:
                getMenuInflater().inflate(R.menu.flow_menu, menu);
                break;
            case R.id.contactOption:
                getMenuInflater().inflate(R.menu.contact_menu, menu);
                break;
            case R.id.callback_option:
                getMenuInflater().inflate(R.menu.contact_menu, menu);
                break;
        }
    }

    public static void mkToast(String message) {
        MainActivity.getInstance().runOnUiThread(() ->Toast.makeText(MainActivity.getInstance(), message, Toast.LENGTH_LONG).show());
    }
    public PTSRadio getRadio() {
        return radio;
    }

    @Override
    protected void onStop() {
        super.onStop();
        radio.close();
    }
    public void gestisciRichiestaChat(){
        String idInterlocutore = chatService.getMemberID();
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.accetta_chat_popup, null);
        TextView t = popupView.findViewById(R.id.popup_testo);
        t.setText(idInterlocutore + " vuole aprire una chat. Vuoi accettare?");
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupView.findViewById(R.id.si_popup).setOnClickListener(view -> {
            popupWindow.dismiss();
            DatiChat cc = DatiChat.getChatById(idInterlocutore);
            Chat chatView = Chat.getInstance(cc);
            replaceFragment(chatView);
            chatView.accettaRichiesta(chatService);
        });
        popupView.findViewById(R.id.no_popup).setOnClickListener(view -> {
            popupWindow.dismiss();
            mkToast("Chat rifiutata");
        });
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        findViewById(R.id.MainFrame).post(
                () -> popupWindow.showAtLocation(findViewById(R.id.MainFrame),
                        Gravity.CENTER, 0, 0));

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}