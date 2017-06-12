package it.pedrazzi.marco.savemyphoto.Http;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.DbLocale.DBgestione;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;


    //classe asincrona per invia i media al server
    public class HttpDownloadAsync extends AsyncTask<String,Void,byte[]> {

        private Context ctx;
        private DBgestione dBgestione;
        private Http http;
        private ImageView imageView;


        @Override
        protected void onProgressUpdate(Void... values)
        {
            super.onProgressUpdate(values);
        }

        public HttpDownloadAsync(Context ctx)
        {
            this.ctx=ctx;
            this.dBgestione=new DBgestione(ctx);
        }
        public HttpDownloadAsync(Context ctx, ImageView imageView)
        {
            this.ctx=ctx;
            this.dBgestione=new DBgestione(ctx);
            this.imageView=imageView;
        }

        @Override
        protected byte[] doInBackground(String... strings) {

            //TODO per implementare i progressi del upload usare publicProgress
            http =new Http(this.dBgestione);

            // richiesta get
            return http.Ricevi(strings[0]);


        }

        @Override
        protected void onPostExecute(byte[] arrayByte)
        {
            super.onPostExecute(arrayByte);
            if(arrayByte!=null)
            {
                Toast.makeText(this.ctx,"Download media terminato con successo!",Toast.LENGTH_SHORT).show();
                if(this.imageView!=null) //usata solo in presentazione
                {
                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(arrayByte, 0, arrayByte.length));
                }
            }
            else
            {
                Toast.makeText(this.ctx,"Download media fallito!",Toast.LENGTH_SHORT).show();
            }
        }

    }





