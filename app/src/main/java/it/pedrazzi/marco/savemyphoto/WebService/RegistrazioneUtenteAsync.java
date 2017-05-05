package it.pedrazzi.marco.savemyphoto.WebService;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import it.pedrazzi.marco.savemyphoto.DbLocale.DBgestione;
import it.pedrazzi.marco.savemyphoto.R;
import it.pedrazzi.marco.savemyphoto.Activity.RegistrazioneActivity;
import it.pedrazzi.marco.savemyphoto.WebService.Autogenerate.TSHWSsaveMyPhotoSoap12;

/**
 * Created by Elmer on 19/04/2017.
 */

public class RegistrazioneUtenteAsync extends AsyncTask <NuovoUtente,Void,Integer>{

    RegistrazioneActivity mRegistrazioneActivity; //riferimento all'activity
    Context ctx;

    ProgressBar progressBarReg;
    String nomeUtente;

    public RegistrazioneUtenteAsync(Context ctx, RegistrazioneActivity mRegistrazioneActivity) {
        super();
        this.mRegistrazioneActivity=mRegistrazioneActivity;
        this.ctx=ctx;
        this.progressBarReg=(ProgressBar)mRegistrazioneActivity.findViewById(R.id.progressBarReg);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBarReg.setVisibility(View.VISIBLE);
    }

    @Override
    protected Integer doInBackground(NuovoUtente... nuovoUtentes) {
        TSHWSsaveMyPhotoSoap12 service=new TSHWSsaveMyPhotoSoap12();
        service.enableLogging=true;
        try {
            //ritorna la risp del WS
            int risposta= service.RegistrazioneNuovoUtente(
                                        nuovoUtentes[0].nomeUtente,
                                        nuovoUtentes[0].mail,
                                        nuovoUtentes[0].password,
                                        nuovoUtentes[0].marca,
                                        nuovoUtentes[0].modello,
                                        nuovoUtentes[0].versioneAndroid,
                                        nuovoUtentes[0].spazioLibero);
            Log.i("Ws Reg Utente", ""+risposta);
            this.nomeUtente=nuovoUtentes[0].nomeUtente;
            return risposta;
        } catch (Exception e) {

            e.printStackTrace();
            Log.i("Server irraggiungibile", e.getMessage());

        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer risposta) {
        super.onPostExecute(risposta);
        //se la registrazione sul db remoto è andata a buon fine
        if(risposta!=-1)
        {
            Toast.makeText(ctx, "Registrazione remota di " + this.nomeUtente+ " avvenuta con successo!!", Toast.LENGTH_SHORT).show();
            //registro anche in locale
            DBgestione dBgestione=new DBgestione(ctx);
            Boolean regLocDb=dBgestione.RegistrazioneDbLocale(  mRegistrazioneActivity.nomeUtente,
                                                                mRegistrazioneActivity.mail,
                                                                mRegistrazioneActivity.password,
                                                                risposta,
                                                                mRegistrazioneActivity.marca,
                                                                mRegistrazioneActivity.modello,
                                                                mRegistrazioneActivity.versioneAndroid,
                                                                mRegistrazioneActivity.spazioLibero);
            //se tutto è andato bene avvio activity successiva
            if(regLocDb)
            {
                Toast.makeText(ctx, "Registrazione locale di " + this.nomeUtente+ " avvenuta con successo!!", Toast.LENGTH_SHORT).show();
                mRegistrazioneActivity.AvvioActivitySuccessiva(this.nomeUtente);
                progressBarReg.setVisibility(View.INVISIBLE);
            }
            else
            {
                Toast.makeText(ctx, "Qualcosa è andato storto nella registrazione nel db locale !", Toast.LENGTH_SHORT).show();
                progressBarReg.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            Toast.makeText(ctx, "Utente già presente!!", Toast.LENGTH_SHORT).show();
            progressBarReg.setVisibility(View.INVISIBLE);
        }
    }



}

