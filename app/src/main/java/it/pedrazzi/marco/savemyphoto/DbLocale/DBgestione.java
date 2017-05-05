package it.pedrazzi.marco.savemyphoto.DbLocale;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.FileMedia;

/**
 * Created by Elmer on 14/04/2017.
 */

public class DBgestione {
    private DBddl dbddl;

    public DBgestione(Context ctx)
    {
        dbddl=new DBddl(ctx);
    }

    //registra nuovo utente e dispositivo in locale
    public boolean RegistrazioneDbLocale(String nomeUtente,String mail,String password,Integer idDispostivo,String marca,String modello,String versioneAndroid,int spazioLibero) throws SQLiteException {

        //scrittura lettura su db
        SQLiteDatabase db = dbddl.getWritableDatabase();

        //inserisco l'utente
        ContentValues cv = new ContentValues();
        cv.put(DbString.tbUtenti.NomeUtente, nomeUtente);
        cv.put(DbString.tbUtenti.Mail, mail);
        cv.put(DbString.tbUtenti.Password, password);
        //TODO datanascita mancante!!!!
        //cv.put(DbString.tbUtenti.DataNascita, );

        long idUtente = db.insert(DbString.tbUtenti.tbNome, null, cv);

        //inserisco il dispositivo
        cv.clear();
        cv.put(DbString.tbDispositivi.ID, idDispostivo);
        cv.put(DbString.tbDispositivi.Marca, marca);
        cv.put(DbString.tbDispositivi.Modello, modello);
        cv.put(DbString.tbDispositivi.VersioneAndroid, versioneAndroid);
        cv.put(DbString.tbDispositivi.SpazioLibero, spazioLibero);
        cv.put(DbString.tbDispositivi.FKUtenti, idUtente);

        long idDispositivo = db.insert(DbString.tbDispositivi.tbNome, null, cv);

        if (idUtente != -1 && idDispositivo != -1)
        {
            Log.i("DbLocale","Nuova registrazione inserita corretamente!");
            return true;
        }
        return false;
    }

    //controlla le credenziali in locale
    public boolean UtenteCheckDbLocale(String nomeUtente,String password)
    {
        //lettura su db
        SQLiteDatabase db=dbddl.getReadableDatabase();
        Cursor cursor=null;

        //
        cursor = db.rawQuery("SELECT id FROM " + DbString.tbUtenti.tbNome + " WHERE nomeUtente = ? AND password = ?", new String[]{nomeUtente,password});
            if (cursor.getCount() > 0) //se esiste l'utente
            {
                cursor.close();
                return true;
            } else {
                cursor.close();
                return false;
            }
    }


    public int getIdDispositivo(String nomeUtente)
    {
        //lettura su db
        SQLiteDatabase db=dbddl.getReadableDatabase();
        Cursor cursor=null;

        //recupero l'id del utente
        cursor = db.rawQuery("SELECT "+DbString.tbUtenti.ID+" FROM " + DbString.tbUtenti.tbNome + " WHERE nomeUtente = ?",new String[]{nomeUtente});
        if (cursor.getCount() > 0) //se esiste l'utente
        {
            cursor.moveToFirst();
            int columnID=cursor.getColumnIndex(DbString.tbUtenti.ID);
            String idUtente=cursor.getString(columnID);

            cursor = db.rawQuery("SELECT "+DbString.tbDispositivi.ID+" FROM " + DbString.tbDispositivi.tbNome + " WHERE "+DbString.tbDispositivi.FKUtenti+" = ?",new String[]{idUtente});

            if (cursor.getCount() > 0) //se esiste il dispositivo
            {
                cursor.moveToFirst();
                columnID=cursor.getColumnIndex(DbString.tbDispositivi.ID);
                int idDispositivo=cursor.getInt(columnID);
                cursor.close();
                return idDispositivo;
            }

        }
        return -1;
    }

    public boolean SyncListMedia(ArrayList<FileMedia> listMedia, String macAddr)
    {
        //scrittura lettura su db
        SQLiteDatabase db = dbddl.getWritableDatabase();

        ContentValues cv = new ContentValues();

        //scorro la lista e inserisco i media nel db
        for (FileMedia media:listMedia)
        {
            cv.put(DbString.tbMedia.Nome, media.getNome());
            cv.put(DbString.tbMedia.Album, media.getBucket());
            cv.put(DbString.tbMedia.DataAcquisizione, media.getDataAcquisizione().toString());
            cv.put(DbString.tbMedia.Dimensione, media.getDimensione());
            cv.put(DbString.tbMedia.Altezza, media.getAltezza());
            cv.put(DbString.tbMedia.Larghezza, media.getLarghezza());
            cv.put(DbString.tbMedia.Formato, media.getMimeType());
            cv.put(DbString.tbMedia.Orientamento, media.getOrientamento());
            cv.put(DbString.tbMedia.GpsLat, media.getLatitudine());
            cv.put(DbString.tbMedia.GpsLong,media.getLongitudine());
            cv.put(DbString.tbMedia.Server,0);
            cv.put(DbString.tbMedia.FKDispositivo, macAddr);

            //TODO se il record esiste già?

            long idMedia = db.insert(DbString.tbMedia.tbNome, null, cv);
            if (idMedia != -1)
            {
                Log.i("DbLocale", "Nuovo media inserito nel db locale!");
            }
            else
            {
                Log.i("DbLocale", "Errore nel inserimento del media nel db locale!");
                return false;
            }
        }
        return true;
    }

    //svuota il db
    public boolean delete(long id)
    {
        SQLiteDatabase db=dbddl.getWritableDatabase();
        try
        {
            if (db.delete(DbString.tbUtenti.tbNome, DbString.tbUtenti.ID+"=?", new String[]{Long.toString(id)})>0)
                return true;
            return false;
        }
        catch (SQLiteException sqle)
        {
            return false;
        }

    }

}
