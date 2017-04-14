package it.pedrazzi.marco.savemyphoto;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;


import java.io.File;
import java.util.ArrayList;

import static android.R.attr.activatedBackgroundIndicator;
import static it.pedrazzi.marco.savemyphoto.LoadPhotoBackgroud.NeccessarioNuovoThread;

public class ImageAdapter extends BaseAdapter implements StickyGridHeadersBaseAdapter {

    private Bitmap placeholder;

    private Context mContext;

    private MemoryCachePhoto cachePhoto=new MemoryCachePhoto(); //istanzio classe cache per velocizzare caricamento foto

    private ArrayList<FileMedia> listMedia; //recupero img da caricare

    public ImageAdapter(Context c,ArrayList<FileMedia> lista,Bitmap placeholder) {
        mContext = c;
        this.listMedia =lista;
        this.placeholder=placeholder;
    }
    public int getCount() {
       return listMedia.size();

    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    // crea un imageview per ogni view della gridview
    public View getView(int position, View convertView, ViewGroup parent)
    {

            ImageView imageView;
            if (convertView == null) //controllo se esiste già la view e posso riutilizzarla
            {
                imageView = new ImageView(mContext);
            } else {
                imageView = (ImageView) convertView;
            }

            //immagine già presente in cache?
            Bitmap bitmap = cachePhoto.get((long) position);

            if (bitmap != null) {
                imageView.setImageBitmap(cachePhoto.get((long) position));
                Log.d("Load image: ", "Cache");

            } else { //se non presente in cache la carico in modo asincrono
                try {

                   CaricaImmagine(listMedia.get(position),imageView,position);
                    Log.d("Load image: ", "Async");

                } catch (Exception e)
                {}
            }

        //Log.w("Immagine caricata: ","H: "+bitmap.getHeight()+"W: "+bitmap.getWidth());

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); //scalo l'img centrata

        //l'immagine è selezionata
       if(listMedia.get(position).isSelezionata())
       {
           return SelezionaImmagine(imageView,true);
       }
        else
       {
           return SelezionaImmagine(imageView,false);
       }


    }

    //selezione immagini
    public ImageView SelezionaImmagine(ImageView imageView,Boolean b){
        if(b)
        {   imageView.setCropToPadding(true);
            imageView.setPadding(10,10,10,10);
            imageView.setBackgroundColor(Color.rgb(27,213,215));
        }else
            {
                imageView.setPadding(0,0,0,0);
                imageView.setBackgroundColor(Color.BLACK);
            }
        return imageView;
    }


    //da guida google
    public void CaricaImmagine(FileMedia fileMedia, ImageView imageView,int position) {
        if (NeccessarioNuovoThread(fileMedia.hashCode(), imageView)) {
            final LoadPhotoBackgroud task = new LoadPhotoBackgroud(imageView,cachePhoto,position);
            final LoadPhotoBackgroud.AsyncDrawable asyncDrawable = new LoadPhotoBackgroud.AsyncDrawable(mContext.getResources(),this.placeholder,task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(fileMedia);
        }
    }


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

}
