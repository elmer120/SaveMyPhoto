package it.pedrazzi.marco.savemyphoto.WebService;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import it.pedrazzi.marco.savemyphoto.Activity.AccediActivity;
import it.pedrazzi.marco.savemyphoto.DbLocale.DBgestione;
import it.pedrazzi.marco.savemyphoto.R;
import it.pedrazzi.marco.savemyphoto.WebService.Autogenerate.RVKWSsaveMyPhotoSoap;

/**
 * Created by Elmer on 24/04/2017.
 */

public class AssociaNuovoDispositivoAsync extends AsyncTask <NuovoUtente,Void,Integer>{

    AccediActivity mAccediActivity;
    Context ctx;

    ProgressBar progressBarAcc;

    public AssociaNuovoDispositivoAsync(Context ctx, AccediActivity mAccediActivity)
    {
        super();
        this.ctx=ctx;
        this.mAccediActivity=mAccediActivity;
        this.progressBarAcc=(ProgressBar)mAccediActivity.findViewById(R.id.progressBarAcc);
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        this.progressBarAcc.setVisibility(View.VISIBLE);
    }

    @Override
    protected Integer doInBackground(NuovoUtente... nuovoUtentes) {
        RVKWSsaveMyPhotoSoap service=new RVKWSsaveMyPhotoSoap();
        service.enableLogging=true;
        try
        {
            //ritorna la risp del WS
            Integer risposta= service.AssociaNuovoDispositivo(
                                                                nuovoUtentes[0].marca,
                                                                nuovoUtentes[0].modello,
                                                                nuovoUtentes[0].versioneAndroid,
                                                                nuovoUtentes[0].spazioLibero,
                                                                nuovoUtentes[0].nomeUtente
                                                                );
            Log.i("Ws Reg Utente", ""+risposta);
            return risposta;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer risposta)
    {
        super.onPostExecute(risposta);
        //se l'inserimento del nuovo dispositivo sul db remoto è andata a buon fine
        if (risposta!=-1)
        {
            //registro anche in locale
            DBgestione dBgestione = new DBgestione(ctx);
            Boolean regLocDb=dBgestione.RegistrazioneDbLocale(  mAccediActivity.nomeUtente,"",
                                                                mAccediActivity.password,
                                                                risposta,
                                                                mAccediActivity.marca,
                                                                mAccediActivity.modello,
                                                                mAccediActivity.versioneAndroid,
                                                                mAccediActivity.spazioLibero);
            //se tutto è andato bene passo all'activity successiva
            if (regLocDb)
            {
                Toast.makeText(ctx, "Nuovo dispositivo associato correttamente all'account di "+mAccediActivity.nomeUtente+" !", Toast.LENGTH_SHORT).show();
                this.progressBarAcc.setVisibility(View.INVISIBLE);
                mAccediActivity.AvvioActivitySuccessiva(mAccediActivity.nomeUtente);
            }
            else
            {
                    Toast.makeText(ctx, "Qualcosa è andato storto nell'associazione locale del nuovo dispositivo!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
                Toast.makeText(ctx, "Qualcosa è andato storto nell'associazione remota del nuovo dispositivo!\n Hai già un account associato a questo nome utente?", Toast.LENGTH_SHORT).show();
                this.progressBarAcc.setVisibility(View.INVISIBLE);
        }
    }



}