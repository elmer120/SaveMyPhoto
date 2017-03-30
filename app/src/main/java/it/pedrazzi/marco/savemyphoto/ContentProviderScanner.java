package it.pedrazzi.marco.savemyphoto;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by elmer on 29/12/16.
 */

public class ContentProviderScanner {

    Context mContext;
    ArrayList<FileMedia> listMedia;
    final private String camera="bucket_display_name='100MEDIA' OR bucket_display_name='Camera' OR bucket_display_name='100ANDRO'";
    final private String whatsApp ="bucket_display_name='WhatsApp Images'";
    final private String all=null;

    public ContentProviderScanner(Context mContext){
        this.mContext=mContext;
    }


    public ArrayList<FileMedia> getListMedia(Album album,boolean video) {
        this.listMedia=null;
        this.listMedia=new ArrayList<FileMedia>();
        FileMedia fileMedia=null;
        String where=null;
        Cursor cursorVideo=null;

        switch (album)
        {
            case Camera:
                where=this.camera;
                break;
            case WhatsApp:
                where=this.whatsApp;
                break;
            case All:
                where=this.all;
                break;
        }
        Cursor cursorImage=searchImage(where);

        if(video)
        {
            cursorVideo=searchVideo(where);
        }
            else {
                Log.i("Video: ", "ricerca non richiesta");
            }
        //recupero indice colonne
        int pathColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media.DATA);
        int dateColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
        int nameColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
        int dirColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        int mimeColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);


        int count = cursorImage.getCount();
            for (int i = 0; i < count; i++) {
            fileMedia=null;
            cursorImage.moveToPosition(i);

            //estraggo il path del media
            String path = cursorImage.getString(pathColumnIndex);
            File file=new File(path);
            //il file esiste?

            if(file.exists()) {
                //estraggo le informazioni del media
                String bucket = cursorImage.getString(dirColumnIndex);
                String nome = cursorImage.getString(nameColumnIndex);
                String mimeType = cursorImage.getString(mimeColumnIndex);
                Long timestamp = cursorImage.getLong(dateColumnIndex);


                Calendar calendario = Calendar.getInstance();
                calendario.setTimeInMillis(timestamp);
                int giorno=calendario.get(Calendar.DAY_OF_MONTH);
                int mese=calendario.get(Calendar.MONTH);
                int anno=calendario.get(Calendar.YEAR);

                //aggiungo il media alla lista
                fileMedia = new FileMedia(giorno,mese,anno,path,nome,bucket, mimeType);

                this.listMedia.add(fileMedia);

              /*  Log.i("---", "____________");
                Log.i("Bucket", bucket);
                Log.i("MimeType", mimeType);
                Log.i("DATA", timestamp.toString());
                Log.i("PATH", path);*/
            }
        }

        cursorImage.close();

        if (video)
        {
            //recupero indice colonne
            pathColumnIndex = cursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
            dateColumnIndex = cursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.DATE_TAKEN);
            nameColumnIndex = cursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME);
            dirColumnIndex = cursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME);
            mimeColumnIndex = cursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.MIME_TYPE);

            count = cursorVideo.getCount();
            for (int i = 0; i < count; i++) {
                fileMedia=null;
                cursorVideo.moveToPosition(i);

                //estraggo il path del media
                String path = cursorVideo.getString(pathColumnIndex);
                File file=new File(path);
                //il file esiste?

                if(file.exists()) {
                    //estraggo le informazioni del media
                    String bucket = cursorVideo.getString(dirColumnIndex);
                    String nome = cursorVideo.getString(nameColumnIndex);
                    String mimeType = cursorVideo.getString(mimeColumnIndex);
                    Long timestamp = cursorVideo.getLong(dateColumnIndex);

                    Calendar calendario = Calendar.getInstance();
                    calendario.setTimeInMillis(timestamp);
                    int giorno=calendario.get(Calendar.DAY_OF_MONTH);
                    int mese=calendario.get(Calendar.MONTH);
                    int anno=calendario.get(Calendar.YEAR);

                    //aggiungo il media alla lista
                    fileMedia = new FileMedia(giorno,mese,anno,path,nome,bucket, mimeType);

                    this.listMedia.add(fileMedia);

                 /*   Log.i("---", "____________");
                    Log.i("Bucket", bucket);
                    Log.i("MimeType", mimeType);
                    Log.i("DATA", timestamp.toString());
                    Log.i("PATH", path);*/
                }
            }
            cursorVideo.close();
        }

        OrderList(listMedia);
        return this.listMedia;
    }

    private Cursor searchImage(String where){
        //istanzio il content resolver neccessario per interrogare il content provider (mediaStore)
        ContentResolver contentResolver = this.mContext.getContentResolver();

        //definisco le colonne che voglio estrarre
        final String[] selezione = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.MIME_TYPE};

        //definisco l'ordine del interrogazione
        final String orderBy = MediaStore.Images.Media.BUCKET_DISPLAY_NAME+" and "+MediaStore.Images.Media.DATE_TAKEN+ " DESC";

        //eseguo query sul content provider
        Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, //identificatore risorsa da interrogare
                selezione, //selezione colonne da estrarre
                where,     //selezione (tipo WHERE SQL)
                null,
                orderBy);  //selezione (tipo ORDER BY SQL)

        //esempio query generata
        //SELECT _data, datetaken, bucket_display_name, mime_type FROM images WHERE (bucket_display_name='100MEDIA') ORDER BY bucket_display_name and datetaken DESC

        //Totale record
        Log.i("Tot immagini "+where+": ", ""+cursor.getCount());
        return cursor;
    }



    private Cursor searchVideo(String where)
    {
        //istanzio il content resolver neccessario per interrogare il content provider (mediaStore)
        ContentResolver contentResolver = this.mContext.getContentResolver();

        //definisco le colonne che voglio estrarre
        final String[] selezione = {MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DATE_TAKEN,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Video.VideoColumns.MIME_TYPE};

        //definisco l'ordine del interrogazione
        final String orderBy = MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME+" and "+MediaStore.Video.VideoColumns.DATE_TAKEN+ " DESC";

        //eseguo query sul content provider
        Cursor cursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, //identificatore risorsa da interrogare
                selezione, //selezione colonne da estrarre
                where,     //selezione (tipo WHERE SQL)
                null,
                orderBy);  //selezione (tipo ORDER BY SQL)

        //Totale record
        cursor.getCount();
        Log.i("Tot video "+where+": ", ""+cursor.getCount());
        return cursor;
    }

    private ArrayList<FileMedia> OrderList(ArrayList<FileMedia> list)
    {
        Collections.sort(list, new Comparator<FileMedia>() {
            @Override
            public int compare(FileMedia fileMedia1, FileMedia fileMedia2)
            {
                return  fileMedia1.compareTo(fileMedia2);
            }
        });
        return list;

    }



}