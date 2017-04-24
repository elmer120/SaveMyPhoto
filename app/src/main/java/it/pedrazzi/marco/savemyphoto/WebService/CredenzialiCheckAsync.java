package it.pedrazzi.marco.savemyphoto.WebService;

import android.os.AsyncTask;
import android.util.Log;
/**
 * Created by Elmer on 23/04/2017.
 */

public class CredenzialiCheckAsync extends AsyncTask <String[],Void,Boolean>{


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String[]... strings) {
        IBNWSsaveMyphotoSoap12 service=new IBNWSsaveMyphotoSoap12();
        service.enableLogging=true;
        try {
            String nomeUtente=strings[0][0];
            String password=strings[0][1];
            //ritorna la risp del WS
            boolean risposta= service.CredenzialiCheck(nomeUtente,password);
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
