package it.pedrazzi.marco.savemyphoto.Media;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import it.pedrazzi.marco.savemyphoto.DbLocale.DBgestione;

/**
 * Created by elmer on 29/12/16.
 */

public class ContentProviderScanner {


    private ArrayList<FileMedia> listMedia;
    final private String camera="bucket_display_name='100MEDIA' OR bucket_display_name='Camera' OR bucket_display_name='100ANDRO'";
    final private String whatsApp ="bucket_display_name='WhatsApp Images'";
    final private String all=null;

    private Context ctx;
    private DBgestione dBgestione;

    public ContentProviderScanner(Context mContext)
    {
        this.ctx=mContext;
        this.dBgestione=new DBgestione(mContext);
    }


    public ArrayList<FileMedia> getListMedia(Album album,boolean video)
    {
        this.listMedia=null;
        this.listMedia=new ArrayList<FileMedia>();
        FileMedia fileMedia=null;
        String where=null;
        Cursor cursorVideo=null;

        //controllo che album si vuole estrarre
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

        //recupero il cursore per le immagini
        Cursor cursorImage=searchImage(where);

        //se la ricerca comprende anche i video
        if(video)
        {
            //recupero il cursore per i video
            cursorVideo=searchVideo(where);
        }
        else
        {
                Log.i("Video: ", "ricerca non richiesta");
        }

        //recupero indice colonne
        int pathColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media.DATA);
        int dateColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
        int nameColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
        int dirColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        int mimeColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
        int sizeColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media.SIZE);
        int heightColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media.HEIGHT);
        int widthColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media.WIDTH);
        int orientationColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
        int latitudeColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media.LATITUDE);
        int longitudeColumnIndex = cursorImage.getColumnIndex(MediaStore.Images.Media.LONGITUDE);

        //recupero il numero di righe ritornate
        int countRow = cursorImage.getCount();

        //ciclo per ogni riga
            for (int i = 0; i < countRow; i++)
            {
                fileMedia=null;
                //sposto il cursore sulla ennesima riga
                cursorImage.moveToPosition(i);

                //estraggo il persorso assoluto del media
                /*String path = (pathColumnIndex != -1) ? cursorImage.getString(pathColumnIndex):null;*/
                String path = cursorImage.getString(pathColumnIndex);
                File file=new File(path);

                    //se il file esiste
                    if(file.exists())
                    {
                        ExifInterface ex= null;
                        try
                        {
                            ex = new ExifInterface(path);
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                            Log.i("Exif interface","non trovata");
                        }
/*
                        //estraggo le informazioni del media
                        //operatore ternario
                        //se colonna esiste assegno il suo valore alla variabile altrimenti assegno alla variabile il valore dell'interfaccia exif o null
                        String bucket = (dirColumnIndex != -1) ? cursorImage.getString(dirColumnIndex) : null;
                        final String nome   = (nameColumnIndex!= -1) ? cursorImage.getString(nameColumnIndex): null;
                        String mimeType= (mimeColumnIndex!=-1) ? cursorImage.getString(mimeColumnIndex): null;
                        Integer dimensione = (sizeColumnIndex!=-1) ?  cursorImage.getInt(sizeColumnIndex):ex.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH,0);
                        Integer altezza    = (heightColumnIndex!=-1)? cursorImage.getInt(heightColumnIndex):null;
                        Integer larghezza  = (widthColumnIndex!=-1) ? cursorImage.getInt(widthColumnIndex):ex.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH,0);
                        String orientamento= (orientationColumnIndex!=-1) ? cursorImage.getString(orientationColumnIndex):ex.getAttribute(ExifInterface.TAG_ORIENTATION);
                        Double latitudine= (latitudeColumnIndex!=-1) ? cursorImage.getDouble(latitudeColumnIndex):ex.getAttributeDouble(ExifInterface.TAG_GPS_LATITUDE,0);
                        Double longitudine= (longitudeColumnIndex!=-1) ? cursorImage.getDouble(longitudeColumnIndex): ex.getAttributeDouble(ExifInterface.TAG_GPS_LONGITUDE,0);
*/

                        String bucket = cursorImage.getString(dirColumnIndex);
                        String nome   = cursorImage.getString(nameColumnIndex);
                        String mimeType= cursorImage.getString(mimeColumnIndex);
                        Integer dimensione = cursorImage.getInt(sizeColumnIndex);
                        Integer altezza    = cursorImage.getInt(heightColumnIndex);
                        Integer larghezza  = cursorImage.getInt(widthColumnIndex);
                        String orientamento= cursorImage.getString(orientationColumnIndex);
                        Double latitudine=  cursorImage.getDouble(latitudeColumnIndex);
                        Double longitudine= cursorImage.getDouble(longitudeColumnIndex);


                        Long timestamp = cursorImage.getLong(dateColumnIndex);
                        Date dataAquisizione=new Date();
                        dataAquisizione.setTime(timestamp);


                        Boolean suServer=false;
                        Boolean suDispositivo=true;


                        //se il media è già presente nel db locale
                        if(dBgestione.CheckMedia(nome))
                        {
                            //significa che è già sul server
                            suServer=true;
                        }

                        //aggiungo il media alla lista
                        fileMedia = new FileMedia(dataAquisizione,path,nome,bucket, mimeType,dimensione,altezza,larghezza,orientamento,latitudine,longitudine,suServer,suDispositivo);


                        this.listMedia.add(fileMedia);

                      /*  Log.i("---", "____________");
                        Log.i("Bucket", bucket);
                        Log.i("MimeType", mimeType);
                        Log.i("DATA", timestamp.toString());
                        Log.i("PATH", path);*/
                    }
        }
        //chiudo il cursore
        cursorImage.close();

        //se sono compresi anche i video
        if (video)
        {
            //recupero indice colonne
            pathColumnIndex = cursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
            dateColumnIndex = cursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.DATE_TAKEN);
            nameColumnIndex = cursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME);
            dirColumnIndex = cursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME);
            mimeColumnIndex = cursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.MIME_TYPE);
            sizeColumnIndex = cursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.SIZE);
            heightColumnIndex = cursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.HEIGHT);
            widthColumnIndex = cursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.WIDTH);
            latitudeColumnIndex = cursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.LATITUDE);
            longitudeColumnIndex = cursorVideo.getColumnIndex(MediaStore.Video.VideoColumns.LONGITUDE);



            //recupero il numero di righe ritornate
            countRow = cursorVideo.getCount();

                //ciclo per ogni riga
                for (int i = 0; i < countRow; i++)
                {
                    fileMedia=null;
                    cursorVideo.moveToPosition(i);

                    //estraggo il path del media
                    //String path = (pathColumnIndex!=-1)?cursorVideo.getString(pathColumnIndex):null;
                    String path = cursorVideo.getString(pathColumnIndex);
                    File file=new File(path);

                        //il file esiste?
                        if(file.exists())
                        {

                            ExifInterface ex= null;
                            try
                            {
                                ex = new ExifInterface(path);
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                                Log.i("Exif interface","non trovata");
                            }

                            /*
                            //estraggo le informazioni del media
                            //operatore ternario
                            //se colonna esiste assegno il suo valore alla variabile altrimenti assegno alla variabile il valore dell'interfaccia exif o null
                            String bucket = (dirColumnIndex != -1) ? cursorVideo.getString(dirColumnIndex) : null;
                            String nome   = (nameColumnIndex!= -1) ? cursorVideo.getString(nameColumnIndex): null;
                            String mimeType= (mimeColumnIndex!=-1) ? cursorVideo.getString(mimeColumnIndex): null;
                            Integer dimensione = (sizeColumnIndex!=-1) ?  cursorVideo.getInt(sizeColumnIndex):ex.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH,0);
                            Integer altezza    = (heightColumnIndex!=-1)? cursorVideo.getInt(heightColumnIndex):null;
                            Integer larghezza  = (widthColumnIndex!=-1) ? cursorVideo.getInt(widthColumnIndex):ex.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH,0);
                            Double latitudine= (latitudeColumnIndex!=-1) ? cursorVideo.getDouble(latitudeColumnIndex):ex.getAttributeDouble(ExifInterface.TAG_GPS_LATITUDE,0);
                            Double longitudine= (longitudeColumnIndex!=-1) ? cursorVideo.getDouble(longitudeColumnIndex): ex.getAttributeDouble(ExifInterface.TAG_GPS_LONGITUDE,0);

                            */

                            String bucket = cursorVideo.getString(dirColumnIndex);
                            String nome   = cursorVideo.getString(nameColumnIndex);
                            String mimeType= cursorVideo.getString(mimeColumnIndex);
                            Integer dimensione = cursorVideo.getInt(sizeColumnIndex);
                            Integer altezza    = cursorVideo.getInt(heightColumnIndex);
                            Integer larghezza  = cursorVideo.getInt(widthColumnIndex);
                            String orientamento= cursorVideo.getString(orientationColumnIndex);
                            Double latitudine=  cursorVideo.getDouble(latitudeColumnIndex);
                            Double longitudine= cursorVideo.getDouble(longitudeColumnIndex);

                            Long timestamp = cursorVideo.getLong(dateColumnIndex);
                            Date dataAquisizione=new Date();
                            dataAquisizione.setTime(timestamp);

                            Boolean suServer=false;
                            Boolean suDispositivo=true;


                            //se il media è già presente nel db locale
                            if(dBgestione.CheckMedia(nome))
                            {
                                //significa che è già sul server
                                suServer=true;
                            }

                            //aggiungo il media alla lista
                            fileMedia = new FileMedia(dataAquisizione,path,nome,bucket, mimeType,dimensione,altezza,larghezza,"",latitudine,longitudine,suServer,suDispositivo);

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

        Collections.sort(listMedia);
        return this.listMedia;
    }

    private Cursor searchImage(String where){
        //istanzio il content resolver neccessario per interrogare il content provider (mediaStore)
        ContentResolver contentResolver = this.ctx.getContentResolver();

        //definisco le colonne che voglio estrarre
        final String[] selezione = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.LATITUDE,
                MediaStore.Images.Media.LONGITUDE};

        //definisco l'ordine del interrogazione
        final String orderBy = MediaStore.Images.Media.BUCKET_DISPLAY_NAME+" and "+MediaStore.Images.Media.DATE_TAKEN+ " DESC";

        //eseguo query sul content provider
        Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, //URI identificatore risorsa da interrogare
                selezione, //selezione colonne da estrarre
                where,     //selezione (WHERE in SQL)
                null,
                orderBy);  //selezione (ORDER BY in SQL)

        //esempio query generata
        //SELECT _data, datetaken, bucket_display_name, mime_type
        // FROM images WHERE (bucket_display_name='100MEDIA')
        // ORDER BY bucket_display_name and datetaken DESC

        //Totale record
        Log.i("Tot immagini "+where+": ", ""+cursor.getCount());
        return cursor;
    }

    private Cursor searchVideo(String where)
    {
        //istanzio il content resolver neccessario per interrogare il content provider (mediaStore)
        ContentResolver contentResolver = this.ctx.getContentResolver();

        //definisco le colonne che voglio estrarre
        final String[] selezione = {MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DATE_TAKEN,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Video.VideoColumns.MIME_TYPE,
                MediaStore.Video.VideoColumns.SIZE,
                MediaStore.Video.VideoColumns.HEIGHT,
                MediaStore.Video.VideoColumns.WIDTH,
                MediaStore.Video.VideoColumns.LATITUDE,
                MediaStore.Video.VideoColumns.LONGITUDE};

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


   /* private ArrayList<FileMedia> OrderList(ArrayList<FileMedia> list)
    {
        Collections.sort(list, new Comparator<FileMedia>() {
            @Override
            public int compare(FileMedia fileMedia1, FileMedia fileMedia2)
            {
                Log.i("compare",fileMedia1.compareTo(fileMedia2)+"");
                return fileMedia1.compareTo(fileMedia2);
            }
        });
        return list;

    }*/



}