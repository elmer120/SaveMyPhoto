package it.pedrazzi.marco.savemyphoto.WebService;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import it.pedrazzi.marco.savemyphoto.Galleria.ImageAdapter;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;
import it.pedrazzi.marco.savemyphoto.WebService.Autogenerate.RVKArrayOfString;
import it.pedrazzi.marco.savemyphoto.WebService.Autogenerate.RVKWSsaveMyPhotoSoap;


public class GetMediaOnServerAsync extends AsyncTask <Void,Void,Boolean> {

    String nomeUtente;
    Integer idDispositivo;
    Context ctx;
    ArrayList<FileMedia> listMedia;
    ImageAdapter imageAdapter;
    int numeroMedia;

    public GetMediaOnServerAsync(Context ctx, String nomeUtente, Integer idDispositivo, ArrayList<FileMedia>listMedia, ImageAdapter imageAdapter)
    {
        this.nomeUtente=nomeUtente;
        this.idDispositivo=idDispositivo;
        this.ctx=ctx;
        this.listMedia=listMedia;
        this.imageAdapter=imageAdapter;
    }

    @Override
    protected Boolean doInBackground(Void... voids)
    {
        RVKWSsaveMyPhotoSoap service=new RVKWSsaveMyPhotoSoap();
        service.enableLogging=true;

        try
        {
            //recupero i metadati delle foto dal server
            RVKArrayOfString rrcArrayOfString=service.CheckMediaOnServer(nomeUtente,idDispositivo);

            if(rrcArrayOfString.getPropertyCount()>0)
            {
                //calcolo il numero dei media ritornati
                numeroMedia = rrcArrayOfString.getPropertyCount() / 10;
                //per ogni media
                for (int i = 1; i <= numeroMedia; i++)
                {
                    //calcolo che parte del array estrarre
                    int count = i * 10;

                    ArrayList<String> metaDati = new ArrayList<String>();

                    for (int j = count - 10; j < count; j++) {
                        //aggiungo i metadati alla lista
                        metaDati.add(rrcArrayOfString.get(j));
                    }

                    //creo l'oggetto per formattare string in date
                    SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");

                    //creo il media
                    FileMedia mediaTmp =new FileMedia
                                                (
                                                        parser.parse(metaDati.get(0)),//dataAcquisizione
                                                        metaDati.get(1),//path
                                                        metaDati.get(2),//nome
                                                        "cloud",//bucket
                                                        "image/web",//mimetype
                                                        Integer.parseInt(metaDati.get(4)),//dimensione
                                                        Integer.parseInt(metaDati.get(5)),//altezza
                                                        Integer.parseInt(metaDati.get(6)),//larghezza
                                                        metaDati.get(7),//orientamento
                                                        Double.parseDouble(metaDati.get(8)),//latitudine
                                                        Double.parseDouble(metaDati.get(9)),//longitudine
                                                        true,//su server
                                                        false//su dispositivo
                                                );
                    Boolean presente=false;
                    //controllo se è già in lista
                    for (FileMedia media:listMedia)
                    {
                        //se è uguale nn lo aggiungo alla lista
                        if(media.compareTo(mediaTmp)==0)
                        {
                            Log.i(this.getClass().getSimpleName(),"1 media già scaricato");
                            presente=true;

                        }
                    }
                    if(!presente)
                    {
                        listMedia.add(mediaTmp);
                    }

                }
                return true;
            }
            else
            {
                return false;
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        super.onPostExecute(bool);

        //se ci sono media da visualizzare li aggiungo al listmedia
        if(bool)
        {
            //ordino
            Collections.sort(this.listMedia);
            //notifico all'adapter il cambio della base di dati
            this.imageAdapter.notifyDataSetChanged();
            //libero la cache
            this.imageAdapter.getCachePhoto().Clear();
            Log.i(this.getClass().getSimpleName(),"Presenti "+numeroMedia+" media su server");
        }
        else
        {
            Log.i(this.getClass().getSimpleName(),"Nessun media su server!");
        }
    }

}
