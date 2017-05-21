package it.pedrazzi.marco.savemyphoto.Galleria;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;

import it.pedrazzi.marco.savemyphoto.Media.FileMedia;
import it.pedrazzi.marco.savemyphoto.R;

/**
 * Created by elmer on 25/11/16.
 */
//OnPreExecute e OnPostExecute vengono eseguiti sul thread principale

//1° parametro input doInBackgroud -- 3°parametro output doInBackground
public class LoadPhotoBackground extends AsyncTask<FileMedia,Void,Bitmap> {
    private int TARGET_IMAGE_VIEW_WIDTH = 200;
    private int TARGET_IMAGE_VIEW_HEIGHT = 200;
    WeakReference<ImageViewOverlay> imageViewReferences; //riferimento debole dell'imageView per l'adapter, server per evitare memory leak
    private int hashCode = 0;
    private MemoryCachePhoto cachePhoto;
    private int posizione;
    private Context ctx;
    private Bitmap iconaSuDispositivo;
    private Bitmap iconaSuServer;


    public LoadPhotoBackground(Context ctx, ImageViewOverlay imageViewOverlay, MemoryCachePhoto cachePhoto, int posizione)
    {

        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReferences = new WeakReference<ImageViewOverlay>(imageViewOverlay);
        this.cachePhoto=cachePhoto;
        this.posizione=posizione;
        this.ctx=ctx;
        BitmapFactory.Options opzioniIcona=new BitmapFactory.Options();
        opzioniIcona.inSampleSize=3;
        iconaSuDispositivo = BitmapFactory.decodeResource(this.ctx.getResources(), R.drawable.ic_dispositivo,opzioniIcona);
        iconaSuServer = BitmapFactory.decodeResource(this.ctx.getResources(), R.drawable.ic_server,opzioniIcona);

    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        Log.w("OnPreExecute", "");
    }

    //metodo eseguito in un thread separato
    @Override
    protected Bitmap doInBackground(FileMedia... params) {

        FileMedia media=params[0];
        String mimeType = media.getMimeType();
        String percorsoFile = media.getPath();
        Bitmap anteprima = Bitmap.createBitmap(200, 200, Bitmap.Config.ALPHA_8);
        BitmapFactory.Options opzioniBitmap = null;
        opzioniBitmap = new BitmapFactory.Options();
        opzioniBitmap.inPreferredConfig = Bitmap.Config.RGB_565; //TODO verificare codifica foto
        String log="";

        try {
            switch (mimeType) {
                case "video/mp4":
                    anteprima = ThumbnailUtils.createVideoThumbnail(percorsoFile, MediaStore.Video.Thumbnails.MICRO_KIND); //96x96 dp
                    log+="Video";
                    Log.d("Video: ", "H: " + anteprima.getHeight() + " W: " + anteprima.getWidth()+" "+log);
                    break;
                case "image/jpg":
                case "image/jpeg":
                case "image/png":
                    //cerco se esiste già un anteprima nei metadati exif
                    ExifInterface exifInterface = new ExifInterface(percorsoFile);
                    if (exifInterface.hasThumbnail())
                    {
                        byte[] thumbnail = exifInterface.getThumbnail();

                        opzioniBitmap.inJustDecodeBounds = true;
                        anteprima = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length, opzioniBitmap);

                        //Calcolo il corretto fattore di ridimensionamento
                        opzioniBitmap.inSampleSize = calculateInSampleSize(opzioniBitmap);
                        //decodifico
                        opzioniBitmap.inJustDecodeBounds = false;
                        anteprima = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length, opzioniBitmap);
                        log+="DatiExif";

                    }
                    else { //Altrimenti la creà scalando l'immagine

                        opzioniBitmap.inJustDecodeBounds = true;
                        //estraggo solamente le dimensioni
                        anteprima = BitmapFactory.decodeFile(percorsoFile, opzioniBitmap);
                        //Calcolo il corretto fattore di ridimensionamento
                        opzioniBitmap.inSampleSize = calculateInSampleSize(opzioniBitmap);
                        opzioniBitmap.inJustDecodeBounds = false;
                        //ora decodifico
                        anteprima = BitmapFactory.decodeFile(percorsoFile, opzioniBitmap);
                        log+="NoDatiExif";
                    }
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("Immagine scalata: ", "H: " + anteprima.getHeight() + " W: " + anteprima.getWidth()+" "+log);


        //se il media è su server aggiungo l'icona
        if(media.getSuServer())
        {

           // float top=anteprima.getHeight()-(iconaSuServer.getHeight());
           // float left=48;
           // anteprima=disegnaIcone(anteprima,iconaSuServer,left,top);

        }
        if(media.getSuDispositivo())
        {
            //float top=anteprima.getHeight()-(iconaSuServer.getHeight()*4);
            //float left=10;
            //anteprima=disegnaIcone(anteprima,iconaSuDispositivo,left,top);
        }
        return anteprima;
    }

    //disegna le icone sull'anteprima
    private Bitmap disegnaIcone(Bitmap anteprima,Bitmap icona,float left,float top) {

        //rendo l'anteprima un bitmap mutabile
        anteprima=anteprima.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(anteprima);
        Paint paint=new Paint();
        canvas.drawBitmap(icona, left,top, null);

        return anteprima;
    }



    @Override    //da guida google
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled())
        {
            bitmap = null;
        }

        if (imageViewReferences != null && bitmap != null)
        {
            final ImageViewOverlay imageViewOverlay = imageViewReferences.get();
            final LoadPhotoBackground bitmapWorkerTask = getThreadAssociato(imageViewOverlay);

            if (this == bitmapWorkerTask && imageViewOverlay != null)
            {
                cachePhoto.put((long)posizione,bitmap);
                imageViewOverlay.setImageBitmap(bitmap);
            }

        }
    }


    //calcola il fattore di scala ottimale per l'immagine
    //TODO foto panoramiche non vengono scalate
    private int calculateInSampleSize(BitmapFactory.Options bmOptions)
    {
        Log.d("immagine originale: ", "H: " + bmOptions.outHeight + " W: " + bmOptions.outWidth);
        final int photoWidth = bmOptions.outWidth;
        final int photoHeight = bmOptions.outHeight;
        int scaleFactor = 1;
        if (photoWidth > TARGET_IMAGE_VIEW_WIDTH || photoHeight > TARGET_IMAGE_VIEW_HEIGHT) {
            final int halfPhotoWidth = photoWidth / 2;
            final int halfPhotoHeight = photoHeight / 2;
            while (halfPhotoWidth / scaleFactor > TARGET_IMAGE_VIEW_WIDTH || halfPhotoHeight / scaleFactor > TARGET_IMAGE_VIEW_HEIGHT)
            {
                scaleFactor *= 2;
            }
        }
        return scaleFactor;
    }

    //da guida google
    //classe statica che mantiene un riferimento al thread che decodifica l'immagine
    static class AsyncDrawable extends BitmapDrawable {

        public LoadPhotoBackground getRiferimentoAlThread() {
            return riferimentoAlThread.get();
        }

        private WeakReference<LoadPhotoBackground> riferimentoAlThread;


        public AsyncDrawable(Resources res, Bitmap bitmap, LoadPhotoBackground riferimentoAlThread) {
            super(res, bitmap);
            this.riferimentoAlThread=new WeakReference<LoadPhotoBackground>(riferimentoAlThread);
        }


    }

    //da guida google
    //data una view ritorna il thread associato!?!?
    private static LoadPhotoBackground getThreadAssociato(ImageViewOverlay imageViewOverlay) {
        if (imageViewOverlay != null) {
            final Drawable drawable = imageViewOverlay.getDrawable();

            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getRiferimentoAlThread();
            }

        }
        return null;
    }
    //da guida google
    public static boolean NeccessarioNuovoThread(int hashCode, ImageViewOverlay imageViewOverlay) {
        final LoadPhotoBackground threadAssociato = getThreadAssociato(imageViewOverlay);

        if (threadAssociato != null) {
            final int bitmapData = threadAssociato.hashCode;
            // If bitmapData is not yet set or it differs from the new data

            if (bitmapData == 0 || bitmapData != hashCode) {
                // Cancel previous task
                threadAssociato.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }


}
