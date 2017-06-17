package it.pedrazzi.marco.savemyphoto.Http;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.DbLocale.DBgestione;
import it.pedrazzi.marco.savemyphoto.Galleria.ImageAdapter;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;
import it.pedrazzi.marco.savemyphoto.R;


//classe asincrona per invia i media al server
public class HttpUploadAsync extends AsyncTask<ArrayList<FileMedia>,Void,Boolean> {

    private String nomeUtente;
    private Context ctx;
    private DBgestione dBgestione;
    private Http http;
    private int idDispositivo;
    ImageAdapter imageAdapter;
    ProgressBar progressBar;

    public HttpUploadAsync(Context ctx, String nomeUtente, int idDispositivo, ImageAdapter imageAdapter, ProgressBar progressBar) {
        this.ctx = ctx;
        this.nomeUtente = nomeUtente;
        this.dBgestione = new DBgestione(ctx);
        this.idDispositivo = idDispositivo;
        this.imageAdapter = imageAdapter;
        this.progressBar=progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //avvio progressBar
        this.progressBar.setVisibility(View.VISIBLE);
        this.progressBar.setIndeterminate(true);
    }

    @Override
    protected Boolean doInBackground(ArrayList<FileMedia>... arrayLists) {

        //TODO per implementare i progressi del upload usare publicProgress
        http = new Http(this.dBgestione);
        return http.Invia(this.nomeUtente, this.idDispositivo, arrayLists[0]);

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean) {
            this.imageAdapter.notifyDataSetChanged();
            this.progressBar.setVisibility(View.GONE);
            this.progressBar.setIndeterminate(false);
            Toast.makeText(this.ctx, "Upload media terminato con successo!", Toast.LENGTH_SHORT).show();
        } else {

            this.progressBar.setVisibility(View.GONE);
            this.progressBar.setIndeterminate(false);
            Toast.makeText(this.ctx, "Upload media fallito!", Toast.LENGTH_SHORT).show();
        }
    }
}


