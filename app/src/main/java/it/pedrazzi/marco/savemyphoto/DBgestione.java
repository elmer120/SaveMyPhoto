package it.pedrazzi.marco.savemyphoto;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

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
