package it.pedrazzi.marco.savemyphoto;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.media.browse.MediaBrowser;
import android.mtp.MtpConstants;
import android.os.AsyncTask;
import android.provider.Contacts;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.Signature;

/**
 * Created by elmer on 25/11/16.
 */
//OnPreExecute e OnPostExecute vengono eseguiti sul thread principale

//1° parametro input doInBackgroud -- 3°parametro output doInBackground
public class LoadPhotoBackgroud extends AsyncTask<FileMedia,Void,Bitmap> {
    private int TARGET_IMAGE_VIEW_WIDTH = 200;
    private int TARGET_IMAGE_VIEW_HEIGHT = 200;
    WeakReference<ImageView> imageViewReferences; //riferimento debole dell'imageView per l'adapter, server per evitare memory leak
    private int hashCode = 0;
    private MemoryCachePhoto cachePhoto;
    private int posizione;

    public LoadPhotoBackgroud(ImageView imageView,MemoryCachePhoto cachePhoto,int posizione) {

        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReferences = new WeakReference<ImageView>(imageView);
        this.cachePhoto=cachePhoto;
        this.posizione=posizione;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.w("OnPreExecute", "");
    }

    //metodo eseguito in un thread separato
    @Override
    protected Bitmap doInBackground(FileMedia... params) {

        String tipoFile = params[0].getMimeType();
        String percorsoFile = params[0].getPath();
        Bitmap anteprima = Bitmap.createBitmap(200, 200, Bitmap.Config.ALPHA_8);
        BitmapFactory.Options opzioniBitmap = null;
        opzioniBitmap = new BitmapFactory.Options();
        opzioniBitmap.inPreferredConfig = Bitmap.Config.RGB_565; //TODO verificare codifica foto
        String log="";

        try {
            switch (tipoFile) {
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
                    if (exifInterface.hasThumbnail()) {
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

                        opzioniBitmap.inJustDecodeBounds = true; //decodifico solo le misure
                        anteprima = BitmapFactory.decodeFile(percorsoFile, opzioniBitmap);

                        //Calcolo il corretto fattore di ridimensionamento
                        opzioniBitmap.inSampleSize = calculateInSampleSize(opzioniBitmap);

                        opzioniBitmap.inJustDecodeBounds = false; //ora decodifico
                        anteprima = BitmapFactory.decodeFile(percorsoFile, opzioniBitmap);
                        log+="NoDatiExif";
                    }
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("Immagine scalata: ", "H: " + anteprima.getHeight() + " W: " + anteprima.getWidth()+" "+log);

        return anteprima;
    }

    @Override    //da guida google
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReferences != null && bitmap != null) {
            final ImageView imageView = imageViewReferences.get();
            final LoadPhotoBackgroud bitmapWorkerTask = getThreadAssociato(imageView);

            if (this == bitmapWorkerTask && imageView != null) {
                cachePhoto.put((long)posizione,bitmap);
                imageView.setImageBitmap(bitmap);
            }
        }
    }


    //calcola il fattore di scala ottimale per l'immagine
//TODO foto panoramiche non vengono scalate
    private int calculateInSampleSize(BitmapFactory.Options bmOptions) {
        Log.d("immagine originale: ", "H: " + bmOptions.outHeight + " W: " + bmOptions.outWidth);
        final int photoWidth = bmOptions.outWidth;
        final int photoHeight = bmOptions.outHeight;
        int scaleFactor = 1;
        if (photoWidth > TARGET_IMAGE_VIEW_WIDTH || photoHeight > TARGET_IMAGE_VIEW_HEIGHT) {
            final int halfPhotoWidth = photoWidth / 2;
            final int halfPhotoHeight = photoHeight / 2;
            while (halfPhotoWidth / scaleFactor > TARGET_IMAGE_VIEW_WIDTH
                    || halfPhotoHeight / scaleFactor > TARGET_IMAGE_VIEW_HEIGHT) {
                scaleFactor *= 2;
            }
        }
        return scaleFactor;
    }

//da guida google
    //classe statica che mantiene un riferimento al thread che decodifica l'immagine
    static class AsyncDrawable extends BitmapDrawable {

        public LoadPhotoBackgroud getRiferimentoAlThread() {
            return riferimentoAlThread.get();
        }

        private WeakReference<LoadPhotoBackgroud> riferimentoAlThread;


        public AsyncDrawable(Resources res, Bitmap bitmap, LoadPhotoBackgroud riferimentoAlThread) {
            super(res, bitmap);
            this.riferimentoAlThread=new WeakReference<LoadPhotoBackgroud>(riferimentoAlThread);
        }


    }

    //da guida google
    //data una view ritorna il thread associato!?!?
    private static LoadPhotoBackgroud getThreadAssociato(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();

                if (drawable instanceof AsyncDrawable) {
                    final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                    return asyncDrawable.getRiferimentoAlThread();
                }

        }
        return null;
    }
//da guida google
    public static boolean NeccessarioNuovoThread(int hashCode, ImageView imageView) {
        final LoadPhotoBackgroud threadAssociato = getThreadAssociato(imageView);

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
