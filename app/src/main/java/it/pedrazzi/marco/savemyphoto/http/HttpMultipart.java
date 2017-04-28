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
import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.FileMedia;
import it.pedrazzi.marco.savemyphoto.old.ListFileMedia;


/**
 * Created by Elmer on 19/03/2017.
 */

public class HttpMultipart {

    //SERVER DI TEST UTILIZZATI

    //Richieste post
    //http://posttestserver.com/post.php

    //Richieste multipart
    //http://posttestserver.com/post.php?dir=example
    public String fineRiga = "\r\n";
    public String dueLinee = "--";
    public String separatore = "*****";
    public String campoUno = "nomeUtente";
    public String campoDue = "macAddr";
    //public String urlServer="http://10.0.0.85:51262/WFUpload.aspx"; //server locale
    public String urlServer="http://posttestserver.com/post.php?dir=example"; //server dubug http
    //public String urlServer="http://savemyphoto.gear.host/WFUpload.aspx"; //server hosting di test
    public int qualitaJpeg=50;

    public void Invia(String nomeUtente, String macAddr, ArrayList<FileMedia> listMedia)
    {



            //VARIABILI DI SUPPORTO

            //DEBUG
            //String percorsoFoto="/storage/emulated/0/DCIM/100MEDIA/IMAG0487.jpg";
            //Bitmap img= BitmapFactory.decodeFile(percorsoFoto);

            //Creo l'oggetto url
            URL url = null;
            try {
                url = new URL(urlServer);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.i("Url", "Errato");
            }
            //Istanzio la connessione/client
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("Connesione", "problemi nel contattare il server");
            }

            //imposto che la connessione è in uscita(abilita la scrittura sul oggetto) e in entrata
            client.setDoOutput(true);
            client.setDoInput(true);
            client.setRequestProperty("charset", "UTF-8");

            //non utilizzo il meccanismo di caching
            client.setUseCaches(false);

            //imposto il tipo di richiesta utilizzato
            try {
                client.setRequestMethod("POST");

            } catch (ProtocolException e) {
                e.printStackTrace();
                Log.i("Metodo http", "errato");
            }

            //imposto che in caso di disconnessione ritenti
            client.setRequestProperty("Connection", "Keep-Alive");

            //imposto il content type multipart + il delimitatore/separatore della form
            client.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + separatore);

            //user agent fake
            client.setRequestProperty("User-Agent", "CodeJava Agent");

            //ALTRE PROPRIETA' X RICHIESTA
            //Tipo contenuto
            //client.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //Dimensione del contenuto
            //client.setRequestProperty("Content-Length", Integer.toString(nome.getBytes().length));
            //Codifica della richiesta
            //client.setRequestProperty("charset", "UTF-8");
            //meccanismo di caching
            //client.setUseCaches(false);


            //apro la connessione (da qui non posso più impostare nessuna proprietà di connessione)
            try {
                client.connect();

                //STREAM
                //Faccio lo stream di quello che voglio inviare sulla connessione

                //Comincio a scrivere sulla connessione
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());


                //Parametri post standard
                dos.writeBytes(CreaMsgPost(campoUno, "elmer"));
                dos.writeBytes(CreaMsgPost(campoDue, "20:6E:9C:DC:F9:F5"));
                //fine parametri

                /*invio i media
                for (FileMedia media : listMedia) {
                    dos.writeBytes(CreaHeadMsgMedia("img", media.getNome(), media.getMimeType()));
                    Log.i("Upload:", media.getNome() + "..in corso...");
                    dos.write(CreaBodyMsgMedia(media.getPath()));
                    dos.writeBytes(CreaTailMsgMedia());
                }
*/
                for (int i = 0; i < 1; i++) {
                    FileMedia media=listMedia.get(i);
                    dos.writeBytes(CreaHeadMsgMedia(media.getNome(), media.getMimeType()));
                    Log.i("Upload:", media.getNome() + "..in corso...");
                    dos.write(CreaBodyMsgMedia(media.getPath(),qualitaJpeg));
                    dos.writeBytes(CreaTailMsgMedia());
                }
                //chiudo la richiesta
                dos.writeBytes(FineRichiesta());
                Log.i("Upload: ", "Fine richiesta Content-Lenght "+client.getContentLength());
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("Connessione", "error");
            }


            //Leggo la risposta dal server
            switch (LetturaRispostaServer(client)) {
                case 200:
                    Log.i("Risposta server: ", "Richiesta andata a buon fine");
                    break;
            }

            //DISCONNESSIONE
            client.disconnect();
    }
            /*//CONTENUTO

            //Leggo l'immagine e invio il contenuto

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            //dos.write(byteArray);


            for (int i = 0; i <2 ; i++) {


                //name == nome parametro usato dal server per individuare il file
                dos.writeBytes(dueLinee + separatore + fineRiga);
                dos.writeBytes("Content-Disposition: form-data; name=\"immagine\";" + " filename=\"" + "test"+i+".jpg" + "\"" + fineRiga);
                dos.writeBytes("Content-type: image/jpeg" + fineRiga);
                dos.writeBytes(fineRiga);

                //CONTENUTO

                //Leggo l'immagine e invio il contenuto

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                dos.write(byteArray);

                //FINE CONTENUTO

                //Invio il separatore per delimitare la fine del file
                dos.writeBytes(fineRiga);
                dos.writeBytes(dueLinee + separatore + fineRiga);
            }

            //Invio il separatore per delimitare la fine della richiesta
            dos.writeBytes(fineRiga);
            dos.writeBytes(dueLinee + separatore + dueLinee + fineRiga);


                Log.i("Upload:", img.getConfig().name()+" in corso...");
                //Thread.sleep(500);

*/








    //Sezione Parametro Post
    private String CreaMsgPost(String nomeCampo,String valore)
    {
        //se i parametri non sono vuoti
        if(!nomeCampo.isEmpty() && !valore.isEmpty())
        {

            return dueLinee + separatore + fineRiga+
                        "Content-Disposition: form-data; name=\""+nomeCampo+"\" "+fineRiga+
                        "Content-Type: text/plain; charset=UTF-8"+fineRiga+
                        fineRiga+
                        valore+ fineRiga+
                        dueLinee + separatore + fineRiga+
                        fineRiga;

        }
        return null;
    }

    //Apertura sezione InvioFile
    private String CreaHeadMsgMedia(String nomeFile,String contentType)
    {
        if (!nomeFile.isEmpty() && !contentType.isEmpty())
        {
            //name == nome parametro usato dal server per individuare il file
            return  dueLinee + separatore + fineRiga +
                    "Content-Disposition: form-data; name=\"" + nomeFile + "\";" + " filename=\"" + nomeFile + "\"" + fineRiga +
                    "Content-type: " + contentType + fineRiga +
                    fineRiga;

        }
        return null;
    }

    //Contenuto sezione InvioFile img to byte
    private byte[] CreaBodyMsgMedia(String absolutePath,int quality)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Bitmap img= BitmapFactory.decodeFile(absolutePath);
        img.compress(Bitmap.CompressFormat.JPEG, quality, stream);

        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    //Chiusura sezione InvioFile
    private String CreaTailMsgMedia()
    {
        //FINE CONTENUTO Img
        return  fineRiga+
                dueLinee + separatore + fineRiga;
    }

    //Chiusura richiesta http
    private String FineRichiesta()
    {
        //Invio il separatore per delimitare la fine della richiesta
        return  fineRiga+
                dueLinee + separatore + dueLinee + fineRiga;
    }


    private int LetturaRispostaServer(HttpURLConnection client)
    {
        //Leggo la RISPOSTA dallo stream
        byte[] rawlettura = new byte[1024];
        int i;
        String buffer="-";
        int httpCodiceRisposta=0;
        InputStream inputStream = null;

        try
        {
            inputStream = client.getInputStream();
            httpCodiceRisposta=client.getResponseCode();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.i("Risposta Server:","Problemi nella connessione?!?!");
            return -1;
        }

        try
        {
            while ((i=inputStream.read())!=-1) //mentre c'è stream
            {
                //converto il byte ricevuto da intero a char
                buffer+=(char)i;
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.i("Risposta Server:","Errore nella conversione!");
            return -1;
        }

        Log.i("Risposta Server", "Cod.Http: "+httpCodiceRisposta);
        Log.i("Risposta Server Http", buffer);

        return httpCodiceRisposta;
    }
    //Msg post test
    /*dos.writeBytes(dueLinee + separatore + fineRiga); //--*****\r\n
                dos.writeBytes("Content-Disposition: form-data; name=\"nomeUtente\" "+fineRiga);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8"+fineRiga);
                dos.writeBytes(fineRiga);
                dos.writeBytes("elmer"+ fineRiga);
                dos.writeBytes(dueLinee + separatore + fineRiga);
                dos.writeBytes(fineRiga);


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
