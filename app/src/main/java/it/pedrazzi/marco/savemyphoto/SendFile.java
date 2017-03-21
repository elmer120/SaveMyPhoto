package it.pedrazzi.marco.savemyphoto;


import android.graphics.Bitmap;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * Created by Elmer on 19/03/2017.
 */

public class SendFile {

    //SERVER DI TEST UTILIZZATI

    //Richieste post
    //http://posttestserver.com/post.php

    //Richieste multipart
    //http://posttestserver.com/post.php?dir=example

    public void Send()
    {

        try {

            Bitmap img = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
            //Url server
            String urlServer="http://posttestserver.com/post.php?dir=example";

            //definisco un boundary
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";


            String parametro = "Valore";
            //Creo l'oggetto url
            URL url = new URL(urlServer);
            //Istanzio la connessione/client
            HttpURLConnection client = (HttpURLConnection) url.openConnection();

            //imposto che la connessione è in uscita(abilita la scrittura sul oggetto) e in entrata
            client.setDoOutput(true);
            client.setDoInput(true);

            //non utilizzo il meccanismo di caching
            client.setUseCaches(false);

            //imposto Metodo http utilizzato
            client.setRequestMethod("POST");

            //impostp che in caso di disconnessione ritenta
            client.setRequestProperty("Connection", "Keep-Alive");

            //imposto il content type multipart + il delimitatore/boundary
            client.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);

            //ALTRE PROPRIETA'RICHIESTA
            //Tipo contenuto
            //client.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //Dimensione del contenuto
            //client.setRequestProperty("Content-Length", Integer.toString(nome.getBytes().length));
            //Codifica della richiesta
            //client.setRequestProperty("charset", "UTF-8");
            //meccanismo di caching
            //client.setUseCaches(false);

            //apro la connessione (da qui non posso più impostare nessuna proprietà di connessione)
            client.connect();

            //STREAM
            //Faccio lo stream di quello che voglio inviare sulla connessione

            //Apro lo streaming verso la servlet
            DataOutputStream dos = new DataOutputStream( client.getOutputStream() );

            //Scrivo la prima riga
            dos.writeBytes(twoHyphens + boundary + lineEnd); //--+delimitatore+\r\n
            //dos.writeBytes("Content-Disposition: form-data; name=\"upload\";" + " filename=\"" + img+"\"" + lineEnd);
            dos.writeBytes(lineEnd);



            client.getOutputStream().write(("parametro="+parametro).getBytes());







//Invio il boundary per delimitare la fine del file
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);




            //Leggo la RISPOSTA dallo stream
            byte[] rawlettura = new byte[1024];
            int i;
            String buffer=null;

            InputStream inputStream = client.getInputStream();

            while ((i=inputStream.read())!=-1) //mentre c'è stream
            {
                //converto il byte ricevuto da intero a char
                buffer+=(char)i;
            }
            //CODICE RISPOSTA
            String httpCodiceRisposta= ""+client.getResponseCode();

            Log.i("Codice http risposta", httpCodiceRisposta);
            Log.i("Risposta da server", buffer);

            //DISCONNESSIONE
            client.disconnect();
        }
        catch (ProtocolException e) {
            Log.i("protocol",e.getMessage());
            e.printStackTrace();
        } catch (MalformedURLException e) {

            Log.i("URL",e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {

            Log.i("IO",e.getMessage());
            e.printStackTrace();
        }
    }


    public void SendPostHttp()
    {

        //server di test
        //Richieste post
        //http://posttestserver.com/post.php
        //Richieste multipart
        //http://posttestserver.com/post.php?dir=example

        try {
            String parametro = "Valore";
            //L'URL a cui fare la POST
            URL url = new URL("http://posttestserver.com/post.php?dir=example");
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            //imposto che la connessione è in uscita(abilita la scrittura sul oggetto)
            client.setDoInput(true);
            client.setDoOutput(true);
            //Metodo http utilizzato
            client.setRequestMethod("POST");


            client.connect();

            //STREAM
            //Faccio lo stream di quello che voglio inviare sulla connessione
            client.getOutputStream().write(("parametro="+parametro).getBytes());

            //Leggo la RISPOSTA dallo stream
            byte[] rawlettura = new byte[1024];
            int i;
            String buffer=null;

            InputStream inputStream = client.getInputStream();

                while ((i=inputStream.read())!=-1) //mentre c'è stream
                {
                    //converto il byte ricevuto da intero a char
                        buffer+=(char)i;
                }
            //CODICE RISPOSTA
            String httpCodiceRisposta= ""+client.getResponseCode();

            Log.i("Codice http risposta", httpCodiceRisposta);
            Log.i("Risposta da server", buffer);

            //DISCONNESSIONE
            client.disconnect();
        }
        catch (ProtocolException e) {
            Log.i("protocol",e.getMessage());
            e.printStackTrace();
        } catch (MalformedURLException e) {

            Log.i("URL",e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {

            Log.i("IO",e.getMessage());
            e.printStackTrace();
        }
    }



}
