package it.pedrazzi.marco.savemyphoto.WebService;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import it.pedrazzi.marco.savemyphoto.Galleria.ImageAdapter;
import it.pedrazzi.marco.savemyphoto.Http.HttpDownloadAsync;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;
import it.pedrazzi.marco.savemyphoto.WebService.Autogenerate.RRCArrayOfString;
import it.pedrazzi.marco.savemyphoto.WebService.Autogenerate.RRCWSsaveMyPhotoSoap;

/**
 * Created by Elmer on 21/05/2017.
 */

public class GetMediaOnServer extends AsyncTask <String[],Void,String[]> {

    String [] links;
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
    protected String[] doInBackground(String[]... strings)
    {
        RRCWSsaveMyPhotoSoap service=new RRCWSsaveMyPhotoSoap();
        service.enableLogging=true;
        try
        {
            //recupero i link delle foto dal server
            RRCArrayOfString rrcArrayOfString=service.CheckMediaOnServer(nomeUtente,idDispositivo);

            if(rrcArrayOfString.getPropertyCount()>0)
            {
                this.links =new String[rrcArrayOfString.getPropertyCount()];

                for (int i = 0; i < links.length; i++)
                {
                    //aggiungo i media alla base dati del adapter
                    FileMedia tmp=new FileMedia(null,rrcArrayOfString.get(i),"test","test","image/web",1,1,1,"0",1,1,true,false);
                    this.listMedia.add(tmp);
                }
            }
            return links;

        } catch (Exception e)
        {
            e.printStackTrace();
            return links;
        }
    }

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);

        //se ci sono media da visualizzare li aggiungo al listmedia
        if(strings.length>0)
        {
            //notifico all'adapter il cambio della base di dati
            this.imageAdapter.notifyDataSetChanged();

        }
        else
        {

        }
    }
}
