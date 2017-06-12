package it.pedrazzi.marco.savemyphoto.Http;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.DbLocale.DBgestione;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;
import it.pedrazzi.marco.savemyphoto.WebService.Autogenerate.RVKWSsaveMyPhotoSoap;


/**
 * Created by Elmer on 19/03/2017.
 */

public class Http {

    //SERVER DI TEST UTILIZZATI

    //Richieste post
    //http://posttestserver.com/post.php

    //Richieste multipart
    //http://posttestserver.com/post.php?dir=example
    private String fineRiga = "\r\n";
    private String dueLinee = "--";
    private String separatore = "*****";
    private String campoUno = "nomeUtente";
    private String campoDue = "idDispositivo";
    private String campoTre = "album";
    //public String urlServer="http://muletto.elmer.it"; //server casalingo
    //public String urlServer="http://192.168.1.20:51262/WFupload.aspx"; //server locale
    //public String urlServer="http://posttestserver.com/post.php?dir=example"; //server dubug http
    public String urlServer ="http://savemyphoto.gear.host/WFupload.aspx"; //server hosting di test
    public int qualitaJpeg=80;
    private DBgestione dBgestione;

    public Http(DBgestione dBgestione)
    {
        this.dBgestione=dBgestione;
    }
    public Http(){}

    public boolean Invia(String nomeUtente, Integer idDispositivo, ArrayList<FileMedia> listMedia)
    {
            //Creo l'oggetto url
            URL url = null;
            try
            {
                url = new URL(urlServer);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
                Log.i(this.getClass().getSimpleName(), "Url nn corretto!");
            }
            //Istanzio la connessione/client
            HttpURLConnection client = null;
            try
            {
                client = (HttpURLConnection) url.openConnection();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Log.i(this.getClass().getSimpleName(), "Problemi nel contattare il server!");
            }

            //imposto che la connessione è in uscita(abilita la scrittura sul oggetto) e in entrata
            client.setDoOutput(true);
            client.setDoInput(true);
            client.setRequestProperty("charset", "UTF-8");

            //non utilizzo il meccanismo di caching
            client.setUseCaches(false);

            //imposto il tipo di richiesta utilizzato
            try
            {
                client.setRequestMethod("POST");

            }
            catch (ProtocolException e)
            {
                e.printStackTrace();
                Log.i(this.getClass().getSimpleName(), "Metodo http impostato è errato!");
            }

            //imposto che in caso di disconnessione ritenti
            client.setRequestProperty("Connection", "Keep-Alive");

            //imposto il timeout della connessione ad infinito
            client.setConnectTimeout(0);

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
                Log.i(this.getClass().getSimpleName(),"Connessione aperta...");

                //STREAM
                //Faccio lo stream di quello che voglio inviare sulla connessione

                //Comincio a scrivere sulla connessione
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());

                //Parametri post standard
                dos.writeBytes(CreaMsgPost(campoUno, nomeUtente));
                dos.writeBytes(CreaMsgPost(campoDue, idDispositivo.toString()));
                //fine parametri

                //invio i media
                for (FileMedia media:listMedia)
                {
                    Log.i(this.getClass().getSimpleName(), "---\nUpload di "+media.getNome()+"\n DimensioneOriginale: "+media.getDimensione());
                    dos.writeBytes(CreaHeadMsgMedia(media.getNome(), media.getMimeType()));

                    //comprimo il media
                    byte [] tmp=CreaBodyMsgMedia(media.getPath(),qualitaJpeg);
                    //setto la nuova dimensione per inviarla al server in quanto la compressione fa perdere i metadati
                    media.setDimensione(tmp.length);

                    dos.write(tmp);
                    dos.writeBytes(CreaTailMsgMedia());
                }

                //chiudo la richiesta
                dos.writeBytes(FineRichiesta());
                Log.i(this.getClass().getSimpleName(), "Richiesta http conclusa.");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(this.getClass().getSimpleName(), "Errore durante l'upload");
            }

        //Leggo la risposta dal server
            switch (LetturaRispostaServer(client))
            {

                case 200:
                    Log.i(this.getClass().getSimpleName(), "Upload ok! Risposta server: 200 OK!");
                    //aggiungo i record sul db remoto
                    if(AggiungiMediaDbRemoto(listMedia,nomeUtente,idDispositivo))
                    {
                        //aggiungo il media nel db locale
                        if(this.dBgestione.SyncListMedia(listMedia))
                        {
                            //imposto che il media è sul server
                            for (FileMedia media:listMedia)
                            {
                                media.setSuServer(true);
                            }
                            //DISCONNESSIONE
                            client.disconnect();
                            return true;
                        }
                    }
                    break;
            }

            //DISCONNESSIONE
            client.disconnect();

        return false;
    }

    public byte[] Ricevi(String linkFoto)
    {
        //Creo l'oggetto url
        URL url = null;
        //Istanzio la connessione/client
        HttpURLConnection client = null;

        try
        {
            url = new URL("http://savemyphoto.gear.host/"+linkFoto);
            //istanzio il client
            client = (HttpURLConnection) url.openConnection();
            //imposto il metodo http da utilizzare
            client.setRequestMethod("GET");
            //user agent fake
            client.setRequestProperty("User-Agent", "CodeJava Agent");
            client.setRequestProperty("charset", "UTF-8");
            //non utilizzo il meccanismo di caching
            client.setUseCaches(false);
            //apro la connessione (da qui non posso più impostare nessuna proprietà di connessione)
            client.connect();
            Log.i(this.getClass().getSimpleName(),"Connessione aperta...");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            Log.i(this.getClass().getSimpleName(), "Url nn corretto!");
        } catch (IOException e)
        {
            e.printStackTrace();
            Log.i(this.getClass().getSimpleName(), "Problemi nel contattare il server!");
        }




        //imposto che in caso di disconnessione ritenti
        //client.setRequestProperty("Connection", "Keep-Alive");
        //imposto il timeout della connessione ad infinito
        //client.setConnectTimeout(0);
        //imposto il content type multipart + il delimitatore/separatore della form
        //client.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + separatore);
        //ALTRE PROPRIETA' X RICHIESTA
        //Tipo contenuto
        //client.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //Dimensione del contenuto
        //client.setRequestProperty("Content-Length", Integer.toString(nome.getBytes().length));
        //Codifica della richiesta
        //client.setRequestProperty("charset", "UTF-8");
        //meccanismo di caching
        //client.setUseCaches(false);


        //Leggo la risposta dal server
        switch (LetturaRispostaServerDownload(client))
        {

            case 200:
            {
                Log.i(this.getClass().getSimpleName(), "Download ok! Risposta server: 200 OK!");
                try
                {
                    InputStream inputStream=client.getInputStream();
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int nRead;
                    byte[] data = new byte[1024];
                    while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    client.disconnect();
                    return buffer.toByteArray();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    client.disconnect();
                }


            }
        }

        //DISCONNESSIONE
        client.disconnect();

        return null;

    }


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

    //Contenuto sezione InvioFile img to byte (compressa)
    private byte[] CreaBodyMsgMedia(String absolutePath,int quality)
    {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap img= BitmapFactory.decodeFile(absolutePath);
        img.compress(Bitmap.CompressFormat.JPEG, quality, stream); //con la compressione si perdono i metadati exif
        Log.i(this.getClass().getSimpleName(),"Dimensione compressa: "+stream.size());
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


    private int LetturaRispostaServerDownload(HttpURLConnection client)
    {
        Log.i(this.getClass().getSimpleName(), "Attendo risposta dal server..");
        //Leggo la RISPOSTA dallo stream

        int httpCodiceRisposta=0;

        try
        {
            httpCodiceRisposta=client.getResponseCode();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.i(this.getClass().getSimpleName(),"Errore! Risposta server: "+httpCodiceRisposta);
            return -1;
        }

        return httpCodiceRisposta;
    }
//TODO metodi da rivedere
    private int LetturaRispostaServer(HttpURLConnection client)
    {
        Log.i(this.getClass().getSimpleName(), "Attendo risposta dal server..");
        //Leggo la RISPOSTA dallo stream
        int i;
        String buffer="-";
        int httpCodiceRisposta=0;
        InputStream inputStream = null;

        try
        {
            httpCodiceRisposta=client.getResponseCode();
            inputStream = client.getInputStream();

        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.i(this.getClass().getSimpleName(),"Errore! Risposta server: "+httpCodiceRisposta);
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
            Log.i(this.getClass().getSimpleName(),"Errore nella lettura della risposta del server!");
            return -1;
        }

        Log.i(this.getClass().getSimpleName(), "Risposta Http server: "+buffer);

        return httpCodiceRisposta;
    }

    private boolean AggiungiMediaDbRemoto(ArrayList<FileMedia> listmedia,String nomeUtente,Integer idDispositivo)
    {
    for (FileMedia media:listmedia)
    {
        RVKWSsaveMyPhotoSoap service=new RVKWSsaveMyPhotoSoap();
        service.enableLogging=true;
        boolean risultato= false;
        try {
            risultato = service.AggiungiMedia(
                                                        media.getNome(),
                                                        media.getBucket(),
                                                        media.getDataAcquisizione(),
                                                        media.getDimensione(),
                                                        nomeUtente,
                                                        media.getAltezza(),
                                                        media.getLarghezza(),
                                                        media.getMimeType(),
                                                        media.getOrientamento(),
                                                        media.getLatitudine(),
                                                        media.getLongitudine(),
                                                        idDispositivo);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (!risultato)
        {
            Log.i(this.getClass().getSimpleName(),"Errore nel aggiunta dei media al Db remoto!");
            return false;
        }

    }
    Log.i(this.getClass().getSimpleName(),"Media aggiunti correttamente al Db remoto!");
    return true;
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
