package it.pedrazzi.marco.savemyphoto.Http;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.DbLocale.DBgestione;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;


//classe asincrona per invia i media al server
public class HttpUploadAsync extends AsyncTask<ArrayList<FileMedia>,Void,Boolean> {

    private String nomeUtente;
    private Context ctx;
    private DBgestione dBgestione;
    private Http http;
    private int idDispositivo;

    public HttpUploadAsync(Context ctx, String nomeUtente, int idDispositivo)
    {
        this.ctx=ctx;
        this.nomeUtente=nomeUtente;
        this.dBgestione=new DBgestione(ctx);
        this.idDispositivo=idDispositivo;
    }

    @Override
    protected Boolean doInBackground(ArrayList<FileMedia>... arrayLists)
    {

        //TODO per implementare i progressi del upload usare publicProgress
        http =new Http(this.dBgestione);
        return http.Invia(this.nomeUtente,this.idDispositivo,arrayLists[0]);

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean)
        {
            Toast.makeText(this.ctx,"Upload media terminato con successo!",Toast.LENGTH_SHORT);
        }
        else
        {
            Toast.makeText(this.ctx,"Upload media fallito!",Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
