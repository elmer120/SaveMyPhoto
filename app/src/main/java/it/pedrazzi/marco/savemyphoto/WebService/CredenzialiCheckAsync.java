package it.pedrazzi.marco.savemyphoto.WebService;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import it.pedrazzi.marco.savemyphoto.Activity.AccediActivity;
import it.pedrazzi.marco.savemyphoto.R;
import it.pedrazzi.marco.savemyphoto.WebService.Autogenerate.RRCWSsaveMyPhotoSoap;

/**
 * Created by Elmer on 23/04/2017.
 */

public class CredenzialiCheckAsync extends AsyncTask <String[],Void,Boolean>{



    AccediActivity mAccediActivity;
    Context ctx;

    ProgressBar progressBarAcc;
    String nomeUtente=null;

    public CredenzialiCheckAsync(Context ctx,AccediActivity mAccediActivity)
    {
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
    protected Boolean doInBackground(String[]... strings) {
        RRCWSsaveMyPhotoSoap service=new RRCWSsaveMyPhotoSoap();
        service.enableLogging=true;
        try {
            this.nomeUtente=strings[0][0];
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
        if (aBoolean)
        {
            Toast.makeText(ctx, "Credenziali di "+this.nomeUtente+" corrette!",Toast.LENGTH_SHORT).show();
            this.mAccediActivity.AssociaNuovoDispositivo();
        }
        else
        {
            Toast.makeText(ctx, "Credenziali errate!! riprovare!", Toast.LENGTH_SHORT).show();
            this.progressBarAcc.setVisibility(View.INVISIBLE);
        }
    }



}
