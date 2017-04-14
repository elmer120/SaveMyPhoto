package it.pedrazzi.marco.savemyphoto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/**
 * Created by Elmer on 14/04/2017.
 */

public class DBgestione {
    private DBddl dbddl;

    public DBgestione(Context ctx)
    {
        dbddl=new DBddl(ctx);
    }

    public boolean RegistraUtente(String nomeUtente, String dataNascita, String mail,String password) throws SQLiteException
    {
        //scrittura lettura su db
        SQLiteDatabase db=dbddl.getWritableDatabase();

        //se la registrazione è avvenuta
        if(!RicercaUtente(nomeUtente,password,true)) {
            ContentValues cv = new ContentValues();
            cv.put(this.dbddl.nomeUtente, nomeUtente);
            cv.put(this.dbddl.dataNascita, dataNascita);
            cv.put(this.dbddl.mail, mail);
            cv.put(this.dbddl.password, password);

            long risultato=db.insert(this.dbddl.tbUtentiNome, null, cv);

            return true;
        }
        return false;

    }

    public boolean RicercaUtente(String nomeUtente,String password,boolean registrazione)
    {
        //lettura su db
        SQLiteDatabase db=dbddl.getReadableDatabase();

        Cursor cursor=null;

        //se non è una registrazione controllo anche la password
        if (!registrazione) {
            cursor = db.rawQuery("SELECT id FROM " + this.dbddl.tbUtentiNome + " WHERE nomeUtente = ? AND password = ?", new String[]{nomeUtente,password});
        }
        else {
            cursor = db.rawQuery("SELECT id FROM " + this.dbddl.tbUtentiNome + " WHERE nomeUtente = ?",new String[]{nomeUtente});
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

    public boolean delete(long id)
    {
        SQLiteDatabase db=dbddl.getWritableDatabase();
        try
        {
            if (db.delete(this.dbddl.tbUtentiNome, this.dbddl.idU+"=?", new String[]{Long.toString(id)})>0)
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
            crs=db.query(this.dbddl.tbUtentiNome, null, null, null, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        return crs;
    }

}
