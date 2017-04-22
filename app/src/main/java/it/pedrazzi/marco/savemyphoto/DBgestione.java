package it.pedrazzi.marco.savemyphoto;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import it.pedrazzi.marco.savemyphoto.WebService.FKCWSsaveMyphotoSoap12;

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
    public boolean RegistrazioneDbLocale(String nomeUtente,String mail,String password,String macAddr,String marca,String modello,String versioneAndroid,int spazioLibero) throws SQLiteException {

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
        cv.put(DbString.tbDispositivi.ID, macAddr);
        cv.put(DbString.tbDispositivi.Marca, marca);
        cv.put(DbString.tbDispositivi.Modello, modello);
        cv.put(DbString.tbDispositivi.VersioneAndroid, versioneAndroid);
        cv.put(DbString.tbDispositivi.SpazioLibero, spazioLibero);
        cv.put(DbString.tbDispositivi.FKUtenti, idUtente);

        long idDispositivo = db.insert(DbString.tbDispositivi.tbNome, null, cv);

        if (idUtente != -1 && idDispositivo != -1)
        {
            Log.i("DbLocale","Nuova registrazione inserita! corretamente");
            return true;
        }
        return false;
    }

    public boolean AccessoDaNuovoDispositivo(String nomeUtente,String password)
    {
        //TODO Prima del inserimento in locale controllare che l'utente esista nel database del server

        SQLiteDatabase db=dbddl.getWritableDatabase();
        if (UtenteCheckDbRemoto(nomeUtente,password,false))
        {
            ContentValues cv = new ContentValues();
            cv.put(DbString.tbUtenti.NomeUtente, nomeUtente);
            cv.put(DbString.tbUtenti.Password, password);
            long risultato = db.insert(DbString.tbUtenti.tbNome, null, cv);
            return true;
        }
        return false;
    }

    public boolean UtenteCheckDbLocale(String nomeUtente,String password,boolean registrazione)
    {
        //lettura su db

        SQLiteDatabase db=dbddl.getReadableDatabase();

        Cursor cursor=null;

        //se non è una registrazione controllo anche la password
        if (!registrazione) {
            cursor = db.rawQuery("SELECT id FROM " + DbString.tbUtenti.tbNome + " WHERE nomeUtente = ? AND password = ?", new String[]{nomeUtente,password});
        }
        else {
            cursor = db.rawQuery("SELECT id FROM " + DbString.tbUtenti.tbNome + " WHERE nomeUtente = ?",new String[]{nomeUtente});
        }
            if (cursor.getCount() > 0) //se esiste l'utente

            {
                cursor.close();
                return true;
            } else {
                cursor.close();
                return false;
            }
    }

    public boolean UtenteCheckDbRemoto(String nomeUtente,String password,boolean registrazione) {


        FKCWSsaveMyphotoSoap12 service=new FKCWSsaveMyphotoSoap12();

        try {

            //service.UtenteCheck(nomeUtente,password);
            //Log.i("CheckUtente",service.UtenteCheck(nomeUtente,password));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
return false;
    }

    public boolean RegistrazioneDbRemoto(String nomeUtente,String password,boolean registrazione)
    {
        return true;
    }

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

    public Cursor query()
    {
        Cursor crs=null;
        try
        {
            SQLiteDatabase db=this.dbddl.getReadableDatabase();
            crs=db.query(DbString.tbUtenti.tbNome, null, null, null, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        return crs;
    }

}
