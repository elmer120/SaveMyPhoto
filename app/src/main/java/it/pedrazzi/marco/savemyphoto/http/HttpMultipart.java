package it.pedrazzi.marco.savemyphoto.http;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
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

public class HttpMultipart {

    //SERVER DI TEST UTILIZZATI

    //Richieste post
    //http://posttestserver.com/post.php

    //Richieste multipart
    //http://posttestserver.com/post.php?dir=example


    public HttpMultipart(){
    //costruttore default
    }
    public void Invia(String path)  {

        try {

            //VARIABILI DI SUPPORTO

            //DEBUG
            String percorsoFoto="/storage/emulated/0/WhatsApp/Media/WhatsApp Images/test0.jpg";
            Bitmap img= BitmapFactory.decodeFile(percorsoFoto);

            //Muletto PHP
            // String urlServer="http://192.168.1.9";
            //Locale Asp.net
            //Foto creata run-time
            //Bitmap img = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);

            //Url server
            //String urlServer="http://posttestserver.com/post.php?dir=example";

            String urlServer="http://192.168.1.20:23591/index.aspx";

            //definisco un boundary
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            //Creo l'oggetto url
            URL url = new URL(urlServer);
            //Istanzio la connessione/client
            HttpURLConnection client = (HttpURLConnection) url.openConnection();

            //imposto che la connessione è in uscita(abilita la scrittura sul oggetto) e in entrata
            client.setDoOutput(true);
            client.setDoInput(true);
            client.setRequestProperty("charset","UTF-8");

            //non utilizzo il meccanismo di caching
            client.setUseCaches(false);

            //imposto il tipo di richiesta utilizzato
            client.setRequestMethod("POST");

            //imposto che in caso di disconnessione ritenti
            client.setRequestProperty("Connection", "Keep-Alive");

            //imposto il content type multipart + il delimitatore/boundary della form
            client.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);

            //user agent fake
            client.setRequestProperty("User-Agent", "CodeJava Agent");

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

            //Comincio a scrivere sulla connessione
            DataOutputStream dos = new DataOutputStream( client.getOutputStream() );


                //Parametri post standard
                //Scrivo la prima riga

                dos.writeBytes(twoHyphens + boundary + lineEnd); //--+delimitatore+\r\n
                dos.writeBytes("Content-Disposition: form-data; name=\"campo1"+"\""+lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8"+lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes("elmer"+ lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes(lineEnd);

                //fine parametri
            for (int i = 0; i <2 ; i++) {


                //name == nome parametro usato dal server per individuare il file
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"immagine\";" + " filename=\"" + "test"+i+".jpg" + "\"" + lineEnd);
                dos.writeBytes("Content-type: image/jpeg" + lineEnd);
                dos.writeBytes(lineEnd);

                //CONTENUTO

                //Leggo l'immagino e invio il contenuto

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                dos.write(byteArray);

                //FINE CONTENUTO

                //Invio il boundary per delimitare la fine del file
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
            }

            //Invio il boundary per delimitare la fine della richiesta
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                Log.i("Upload:", img.getConfig().name()+" in corso...");
                //Thread.sleep(500);




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

/* TEST
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
*/


}
