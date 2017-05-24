package it.pedrazzi.marco.savemyphoto.Galleria;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.Media.FileMedia;
import it.pedrazzi.marco.savemyphoto.Media.FileMediaWeb;
import it.pedrazzi.marco.savemyphoto.R;

public class ImageAdapter extends BaseAdapter  {

    private Bitmap placeholder;

    private Context ctx;

    private MemoryCachePhoto cachePhoto=new MemoryCachePhoto(); //istanzio classe cache per velocizzare caricamento foto

    private ArrayList<FileMedia> listMedia; //recupero img da caricare

    private Bitmap iconaSuDispositivo;
    private Bitmap iconaSuServer;

    public ImageAdapter(Context context,ArrayList<FileMedia> lista,Bitmap placeholder)
    {
        ctx = context;
        this.listMedia =lista;
        this.placeholder=placeholder;
    }

    public int getCount()
    {
       return listMedia.size();
    }

    public Object getItem(int position)
    {
        return null;
    }

    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer)
    {
        super.registerDataSetObserver(observer);
    }

    // crea un imageview per ogni view della gridview
    public View getView(int position, View convertView, ViewGroup parent)
    {
        FileMedia media=listMedia.get(position);
        ImageViewOverlay imageViewOverlay;

        //controllo se esiste già la view e posso riutilizzarla
        if (convertView != null)
        {
            imageViewOverlay = (ImageViewOverlay) convertView;
            //reimposto gli attributi corretti
            imageViewOverlay.suServer=media.getSuServer();
            imageViewOverlay.suDipositivo=media.getSuDispositivo();
        }
        else
        {
            imageViewOverlay = new ImageViewOverlay(ctx,media.getSuServer(),media.getSuDispositivo());
        }

        //immagine già presente in cache?
        Bitmap bitmap = cachePhoto.get((long) position);

        if (bitmap != null)
        {
            imageViewOverlay.setImageBitmap(cachePhoto.get((long) position));
            Log.d("Load image: ", "Cache");
        }
        else  //se non presente in cache la carico in modo asincrono
        {
                try
                {

                   CaricaImmagine(this.ctx,media,imageViewOverlay,position);
                    Log.d("Load image: ", "Async");

                }
                catch (Exception e)
                {
                    Log.d(this.getClass().getSimpleName(),e.getMessage());
                }
        }

        //Log.w("Immagine caricata: ","H: "+bitmap.getHeight()+"W: "+bitmap.getWidth());



    //l'immagine è selezionata
       if(media.isSelezionata())
       {
           return SelezionaImmagine(imageViewOverlay,true);
       }
        else
       {
           return SelezionaImmagine(imageViewOverlay,false);
       }


    }

    //selezione immagini
    public ImageView SelezionaImmagine(ImageViewOverlay imageViewOverlay,Boolean seleziona)
    {
        if(seleziona)
        {
            imageViewOverlay.setCropToPadding(true);
            imageViewOverlay.setPadding(2,2,2,2);
            imageViewOverlay.setBackgroundColor(Color.WHITE);
            imageViewOverlay.setColorFilter(Color.BLUE, PorterDuff.Mode.LIGHTEN);
        }
            else
            {
                imageViewOverlay.clearColorFilter();
                imageViewOverlay.setPadding(0,0,0,0);
                imageViewOverlay.setBackgroundColor(Color.BLACK);
            }

        return imageViewOverlay;
    }


    //da guida google
    public void CaricaImmagine(Context ctx,FileMedia fileMedia, ImageViewOverlay imageView,int position)
    {
        if (LoadPhotoBackground.NeccessarioNuovoThread(fileMedia.hashCode(), imageView)) {
            final LoadPhotoBackground task = new LoadPhotoBackground(ctx,imageView,cachePhoto,position);
            final LoadPhotoBackground.AsyncDrawable asyncDrawable = new LoadPhotoBackground.AsyncDrawable(ctx.getResources(),this.placeholder,task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(fileMedia);
        }
    }



    /*  //TODO cancellare stycky grid view
    @Override //quanti elementi per ogni intestazione "i" parametro è l'intestazione ennesima
    public int getCountForHeader(int i) {
        //return  listFileMedia.nElementiPerIntestazione.get(i);
        return 0;
    }

    @Override //totale delle intestazioni
    public int getNumHeaders() {
        //return listFileMedia.countIntestazioni;
        return 0;
    }

    @Override  //tipo di intestazione
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {

        return view;
    }
*/
}
