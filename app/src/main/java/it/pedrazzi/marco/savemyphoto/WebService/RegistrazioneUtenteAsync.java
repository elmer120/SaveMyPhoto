package it.pedrazzi.marco.savemyphoto.WebService;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.reflect.Array;

/**
 * Created by Elmer on 19/04/2017.
 */

public class RegistrazioneUtenteAsync extends AsyncTask <NuovoUtente,Void,Boolean>{
    public RegistrazioneUtenteAsync() {
        super();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(NuovoUtente... nuovoUtentes) {
        FKCWSsaveMyphotoSoap12 service=new FKCWSsaveMyphotoSoap12();
        service.enableLogging=true;
        try {
            //ritorna la risp del WS
            boolean risposta= service.RegistrazioneUtente(nuovoUtentes[0].nomeUtente,
                                        nuovoUtentes[0].mail,
                                        nuovoUtentes[0].password,
                                        nuovoUtentes[0].macAddr,
                                        nuovoUtentes[0].marca,
                                        nuovoUtentes[0].modello,
                                        nuovoUtentes[0].versioneAndroid,
                                        nuovoUtentes[0].spazioLibero);
            Log.i("Ws Reg Utente", ""+risposta);
            return risposta;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }



}

