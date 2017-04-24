package it.pedrazzi.marco.savemyphoto.WebService;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Elmer on 24/04/2017.
 */

public class AssociaNuovoDispositivo extends AsyncTask <NuovoUtente,Void,Boolean>{

    public AssociaNuovoDispositivo() {
        super();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(NuovoUtente... nuovoUtentes) {
        IBNWSsaveMyphotoSoap12 service=new IBNWSsaveMyphotoSoap12();
        service.enableLogging=true;
        try {
            //ritorna la risp del WS
            boolean risposta= service.AssociaNuovoDispositivo(
                    nuovoUtentes[0].macAddr,
                    nuovoUtentes[0].marca,
                    nuovoUtentes[0].modello,
                    nuovoUtentes[0].versioneAndroid,
                    nuovoUtentes[0].spazioLibero,
                    nuovoUtentes[0].nomeUtente
                    );
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