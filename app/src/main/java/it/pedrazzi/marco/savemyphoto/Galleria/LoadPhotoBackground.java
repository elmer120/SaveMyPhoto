package it.pedrazzi.marco.savemyphoto.Galleria;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;

import it.pedrazzi.marco.savemyphoto.Http.Http;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;

/**
 * Created by elmer on 25/11/16.
 */
//OnPreExecute e OnPostExecute vengono eseguiti sul thread principale

//1° parametro input doInBackgroud -- 3°parametro output doInBackground
public class LoadPhotoBackground extends AsyncTask<FileMedia,Void,Bitmap> {
    private int larghezzaMaxMedia = 100;
    private int altezzaMaxMedia = 100;
    WeakReference<ImageViewOverlay> imageViewReferences;
    private int hashCode = 0;
    private MemoryCachePhoto cachePhoto;
    private int posizione;
    private Context ctx;
    FileMedia media;


    public LoadPhotoBackground(Context ctx, ImageViewOverlay imageViewOverlay, MemoryCachePhoto cachePhoto, int posizione)
    {
        //riferimento debole dell'imageView per l'adapter, serve per consentire al garbage l'eliminazione
        imageViewReferences = new WeakReference<ImageViewOverlay>(imageViewOverlay);
        this.cachePhoto=cachePhoto;
        this.posizione=posizione;
        this.ctx=ctx;
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
//TODO migliorabile implementando cache disk android developer
        media=params[0];
        String mimeType = media.getMimeType();
        String percorsoFile = media.getPath();
        Bitmap anteprima = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
        BitmapFactory.Options opzioniBitmap = new BitmapFactory.Options();
        //codifica più leggera ogni pixel 2 byte
        opzioniBitmap.inPreferredConfig = Bitmap.Config.RGB_565;
        String log="";

        try {
            switch (mimeType)
            {
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
                    Log.i(this.getClass().getSimpleName(),exifInterface.toString());
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
                case "image/web":

                    Http http =new Http();
                    // richiesta get
                    byte[] arrayByte=http.Ricevi(media.getPath());
                    Log.i(this.getClass().getSimpleName(),""+arrayByte.length);

                    if(arrayByte!=null)
                    {
                        opzioniBitmap.inJustDecodeBounds = true;
                        //estraggo solamente le dimensioni
                        anteprima = BitmapFactory.decodeByteArray(arrayByte, 0, arrayByte.length,opzioniBitmap);
                        //Calcolo il corretto fattore di ridimensionamento
                        opzioniBitmap.inSampleSize = calculateInSampleSize(opzioniBitmap);
                        opzioniBitmap.inJustDecodeBounds = false;
                        //ora decodifico
                        anteprima = BitmapFactory.decodeByteArray(arrayByte, 0, arrayByte.length,opzioniBitmap);

                    }
                        break;
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //Log.i("Immagine scalata: ", "H: " + anteprima.getHeight() + " W: " + anteprima.getWidth()+" "+log);
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
                cachePhoto.put((long)this.posizione,bitmap);
                imageViewOverlay.setImageBitmap(bitmap);
            }

        }
    }


    //calcola il fattore di scala ottimale per il media
    private int calculateInSampleSize(BitmapFactory.Options bmOptions)
    {
        Log.d("immagine originale: ", "H: " + bmOptions.outHeight + " W: " + bmOptions.outWidth);
        final int photoWidth = bmOptions.outWidth;
        final int photoHeight = bmOptions.outHeight;
        int scaleFactor = 1;
        if (photoWidth > larghezzaMaxMedia || photoHeight > altezzaMaxMedia) {
            final int halfPhotoWidth = photoWidth / 2;
            final int halfPhotoHeight = photoHeight / 2;
            while (halfPhotoWidth / scaleFactor > larghezzaMaxMedia || halfPhotoHeight / scaleFactor > altezzaMaxMedia)
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
    //data una view ritorna il thread associato
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
