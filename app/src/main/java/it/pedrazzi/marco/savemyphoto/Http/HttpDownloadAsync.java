package it.pedrazzi.marco.savemyphoto.Http;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.DbLocale.DBgestione;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;


    //classe asincrona per invia i media al server
    public class HttpDownloadAsync extends AsyncTask<String,Void,InputStream> {

        //private String nomeUtente;
        private Context ctx;
        private DBgestione dBgestione;
        private Http http;
        //private int idDispositivo;
        //private ArrayList<FileMedia> listMedia;


        @Override
        protected void onProgressUpdate(Void... values)
        {
            super.onProgressUpdate(values);
        }

        public HttpDownloadAsync(Context ctx)
        {
            this.ctx=ctx;
            //this.nomeUtente=nomeUtente;
            this.dBgestione=new DBgestione(ctx);
            //this.idDispositivo=idDispositivo;
            //this.listMedia=listMedia;
        }


        @Override
        protected InputStream doInBackground(String... strings) {

            //TODO per implementare i progressi del upload usare publicProgress
            http =new Http(this.dBgestione);

            // richiesta get
            return http.Ricevi(strings[0]);


        }

        @Override
        protected void onPostExecute(InputStream inputStream)
        {
            super.onPostExecute(inputStream);
            if(inputStream!=null)
            {
                Toast.makeText(this.ctx,"Download media terminato con successo!",Toast.LENGTH_SHORT);
            }
            else
            {
                Toast.makeText(this.ctx,"Download media fallito!",Toast.LENGTH_SHORT);
            }
        }



    }




