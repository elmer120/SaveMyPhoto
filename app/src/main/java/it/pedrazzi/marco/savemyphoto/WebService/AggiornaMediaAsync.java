package it.pedrazzi.marco.savemyphoto.WebService;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;

import it.pedrazzi.marco.savemyphoto.Galleria.ImageAdapter;
import it.pedrazzi.marco.savemyphoto.WebService.Autogenerate.RVKWSsaveMyPhotoSoap;

/**
 * Created by Elmer on 01/06/2017.
 */

//Aggiorna il record del media nel dbRemoto
public class AggiornaMediaAsync extends AsyncTask<String[],Void,Boolean>
{
    String mediaPath;
    Context ctx;
    ImageAdapter imageAdapter;

    public AggiornaMediaAsync(Context ctx, String mediaPath, ImageAdapter imageAdapter)
    {
        super();
        this.mediaPath=mediaPath;
        this.ctx=ctx;
        this.imageAdapter=imageAdapter;
    }

    @Override
    protected Boolean doInBackground(String[]... params)
    {
        Boolean risultato=false;
        //aggiorno il media nel db remoto mettendo dispositivo a 0
        RVKWSsaveMyPhotoSoap service=new RVKWSsaveMyPhotoSoap();
        try
        {
            service.enableLogging=true;
            risultato=service.AggiornaMedia(Integer.parseInt(params[0][0]),params[0][1]);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return risultato;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean)
    {
        super.onPostExecute(aBoolean);

        if(aBoolean)
        {
            if(new File(mediaPath).delete())
            {
                Toast.makeText(this.ctx, "Media rimosso correttamente", Toast.LENGTH_SHORT).show();
                this.imageAdapter.notifyDataSetChanged();
            }
        }
        else
        {
            Toast.makeText(this.ctx,"Errore nel eliminazione del media: "+mediaPath.toString(),Toast.LENGTH_SHORT).show();
        }


    }
}
