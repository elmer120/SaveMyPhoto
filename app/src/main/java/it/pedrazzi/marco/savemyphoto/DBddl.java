package it.pedrazzi.marco.savemyphoto;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Elmer on 14/04/2017.
 */

public class DBddl extends SQLiteOpenHelper {

    //COSTANTI PER IL DATABASE
    public static final String nomeDB="BILLBOOK";

    //TABELLE
    //Utenti
    public static final String tbUtentiNome="Utenti";

    public static final String idU="id";
    public static final String nomeUtente="nomeUtente";
    public static final String dataNascita="dataNascita";
    public static final String mail="mail";
    public static final String password="password";



    public DBddl(Context context) {
        //contesto,nomeDb,cursor,versione db
        super(context, nomeDB, null,1);
    }

    /*viene invocato nel momento in cui non si trova nello spazio
    dell’applicazione un database con nome indicato nel costruttore*/
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //TODO NON NULL!!
        String q="CREATE TABLE "+this.tbUtentiNome+
                " ("+idU+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                this.nomeUtente+" TEXT," +
                this.dataNascita+" TEXT," +
                this.password+" TEXT," +
                this.mail+" TEXT)";
        //effettuo il CREATE DATABASE + LA DDL
        db.execSQL(q);
    }

    /*viene invocato nel momento in cui di richiede una altra versione del db*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {  }

}
