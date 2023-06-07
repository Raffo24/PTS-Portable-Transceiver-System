# PTS Portable Transceiver System
#Attenzione ! per ora qui viene caricato solo il front-end, tra qualche mese aggiungerò tutte le parti del progetto esattamente nello stesso stato con cui sono state esposte alla maker faire 2022
## Obiettivo
ll progetto si propone di sviluppare un sistema di comunicazione aggiuntivo interfacciabile con il cellulare al fine di consentire comunicazioni in zone non provviste di copertura e\o mezzi di comunicazione “classici”: come wi-fi, 3G, 4G e linea telefonica.
Questo è reso possibile dallo sviluppo di appositi moduli in grado di trasmettere e ricevere voce e testo, che si interfacciano con il cellulare attraverso un'applicazione, dotata di un'interfaccia grafica intuitiva e di facile utilizzo.
Il sistema progettato risolve problemi di scarsa copertura, ad esempio in zone di mare aperto, campagna e montagna.
# Obiettivi e ambienti di sviluppo
Il prototipo finale deve avere requisiti fondamentali:
1) Basso consumo energetico;
2) Dimensione ridotte;
3) Elevata semplicità di utilizzo;
4) Basso costo;
5) Ampio raggio di azione.

Abbiamo utilizzato per l’hardware un microprocessore ATMEGA328P e quindi la famiglia di Hardware Arduino.

Quindi è stato necessario sviluppare due tipi di software:
1. Codice per programmare l’hardware Arduino (linguaggio basato su C++)
2. App in grado di far interagire l’hardware Arduino con quello dei dispositivi Android.

# Scelta della tecnologia e sviluppo dell’Arduino
Abbiamo optato per appoggiarci ad una tecnologia relativamente nuova dell’IOT, ovvero Medium-long range 2.4 gigahertz che anche se permette di trasmettere e ricevere su distanze minori (ovvero con il giusto hardware, in confronto ai di base del LoRa) ha una banda di trasmissione molto più ampia e quindi permette l’invio non solo di piccoli pacchetti di testo, ma anche di grandi pacchetti testuali, audio e video (anche se estremamente compresso).

Per usare questa banda di comunicazione abbiamo scelto di sviluppare il nostro sistema di comunicazione a partire dal nRF24L01+ PNA/LNA transceiver module ideato e sviluppato da Nordic Semiconductor.

Abbiamo scelto questo modulo principalmente per due motivi:

1) Ha un costo inferiore a 5 dollari, rendendo lo sviluppo di PTS molto economico;
2) È molto piccolo ed energicamente efficiente;
3) Con poche e relativamente semplici modifiche può trasmettere entro 5 Km di distanza.

Un ulteriore vantaggio nella scelta del modulo nRF24L01+ PNA/LNA è la presenza di un gran numero di librerie già realizzate che ci hanno permesso di configurare l’invio di messaggi testuali e la comunicazione audio senza progettare un sistema da zero.

In particolare, il nostro progetto si basa sulle due librerie:

- RF24.h (utilizzata nella sua versione di base)
- nRF24audio.h (modificata dal team per adeguarla alle esigenze)

L’hardware è stato scelto e ottimizzato in funzione dei requisiti delle librerie mediante l’aggiunta di un amplificatore audio basato sull’integrato LM386.

# Sviluppo App Android

L’App utilizza un’unica libreria basata su API Android

- UsbSerial di felHR85.

Lo sviluppo dell’app è stato diviso in 2 parti, la prima che si occupa dell’interazione con l’hardware Arduino e la seconda che si occupa dei dati e dell’interazione con l’utente.

La prima parte di sviluppo si compone di 3 classi principali:

- PTSRadio, che controlla la scheda Arduino durante il suo ciclo di vita;

- PTSChat, che controlla il servizio di messaggistica istantanea;

- PTSCall, che controlla il servizio di chiamate vocali.

La seconda parte di sviluppo segue i classici criteri di sviluppo delle app e fa uso del pattern architetturale MVC (Model View Control).

- Il Model fornisce i metodi per accedere alle strutture dati utili.

- La View si occupa dell’interazione con l’utente e della realizzazione della GUI (“graphical user interface”)

- Il Controller fa da ponte tra la view e il model e fornisce le funzioni per rispondere alle richieste dell’utente

L’app è aggiornata al target API level 31 ed è retrocompatibile fino alle Android 8 (Oreo).

# Assemblaggio hardware arduino

# Compilazione
Il progetto può essere compilato manualmente tramite Android Studio:
1. Aprire la cartella del progetto, dal menù "File" -> "open"
2. Dal menù "Build" -> "Build" -> "Build Bundle(s)/APK(s)" -> "Build APK(s)"
# Esecuzione
- Collegare i dispositivi all'arduino con un cavo seriale usb-c
- Installare l'apk nei dispositivi android (abilitando la sorgente sconosciuta).
- Associare il dispositivo con cui si vuole comunicare
- mandare un messaggio
