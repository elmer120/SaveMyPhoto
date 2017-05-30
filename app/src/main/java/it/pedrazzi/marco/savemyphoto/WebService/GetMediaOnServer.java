package it.pedrazzi.marco.savemyphoto.WebService;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.Galleria.ImageAdapter;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;
import it.pedrazzi.marco.savemyphoto.WebService.Autogenerate.RVKArrayOfString;
import it.pedrazzi.marco.savemyphoto.WebService.Autogenerate.RVKWSsaveMyPhotoSoap;


public class GetMediaOnServer extends AsyncTask <Void,Void,Boolean> {

    String nomeUtente;
    Integer idDispositivo;
    Context ctx;
    ArrayList<FileMedia> listMedia;
    ImageAdapter imageAdapter;

    public GetMediaOnServer(Context ctx, String nomeUtente, Integer idDispositivo, ArrayList<FileMedia>listMedia,ImageAdapter imageAdapter)
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
                int numeroMedia = rrcArrayOfString.getPropertyCount() / 10;
                //per ogni media
                for (int i = 1; i <= numeroMedia; i++)
                {
                    //calcolo che parte del array estrarre
                    int count = i * 10;

                    ArrayList<String> media = new ArrayList<String>();

                    for (int j = count - 10; j < count; j++) {
                        //aggiungo i metadati alla lista
                        media.add(rrcArrayOfString.get(j));
                    }

                    //creo l'oggetto per formattare string in date
                    SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");

                    //creo il media
                    FileMedia media2 =new FileMedia
                                                (
                                                        parser.parse(media.get(0)),//dataAcquisizione
                                                        media.get(1),//path
                                                        media.get(2),//nome
                                                        "cloud",//bucket
                                                        "image/web",//mimetype
                                                        Integer.parseInt(media.get(4)),//dimensione
                                                        Integer.parseInt(media.get(5)),//altezza
                                                        Integer.parseInt(media.get(6)),//larghezza
                                                        media.get(7),//orientamento
                                                        Double.parseDouble(media.get(8)),//latitudine
                                                        Double.parseDouble(media.get(9)),//longitudine
                                                        true,//su server
                                                        false//su dispositivo
                                                );

                    listMedia.add(media2);

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
            //notifico all'adapter il cambio della base di dati
            this.imageAdapter.notifyDataSetChanged();
            Log.i(this.getClass().getSimpleName(),"Presenti dei media da scaricare");
        }
        else
        {
            Log.i(this.getClass().getSimpleName(),"Nessun media da scaricare!");
        }
    }

}
