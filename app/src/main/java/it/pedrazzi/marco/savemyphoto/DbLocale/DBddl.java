package it.pedrazzi.marco.savemyphoto.DbLocale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBddl extends SQLiteOpenHelper {

    public DBddl(Context context) {
        //contesto,nomeDb,cursor,versione db
        super(context, DbString.nomeDB, null,1);
    }

    /*viene invocato nel momento in cui non si trova nello spazio
    dell’applicazione un database con nome come indicato nel costruttore*/
    @Override
    public void onCreate(SQLiteDatabase db)
    {

        /*TIPI di dati in sqlite
        * integer, cioè numero intero
        real, cioè numero reale
        text, cioè testo
        blob, cioè testo in forma binaria
        cd null, cioè sconosciuto.

        per compatibilità accetta anche la dichiarazione del tipo come in sqlserver(es nvarchar),
        ma li convertirà (nvarchar--->TEXT) non ha controllo sul tipo inserito
        */
        String qU="CREATE TABLE "+DbString.tbUtenti.tbNome+
                " ("+DbString.tbUtenti.ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                DbString.tbUtenti.NomeUtente +" NVARCHAR(30) NOT NULL," +
                DbString.tbUtenti.DataNascita +" DATETIME," +
                DbString.tbUtenti.Mail+" NVARCHAR(256) NOT NULL," +
                DbString.tbUtenti.Password+" NVARCHAR(128) NOT NULL)";

        String qD="CREATE TABLE "+DbString.tbDispositivi.tbNome+
                " ("+DbString.tbDispositivi.ID+" INTEGER PRIMARY KEY NOT NULL," +
                DbString.tbDispositivi.Marca+" NVARCHAR(30)," +
                DbString.tbDispositivi.Modello+" NVARCHAR(30)," +
                DbString.tbDispositivi.VersioneAndroid+" NVARCHAR(15) NOT NULL," +
                DbString.tbDispositivi.SpazioLibero+" INT NOT NULL," +
                DbString.tbDispositivi.FKUtenti+" INT NOT NULL)";

        String qM="CREATE TABLE "+DbString.tbMedia.tbNome+
                " ("+DbString.tbMedia.ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                DbString.tbMedia.Nome+" NVARCHAR(30) NOT NULL,"+
                DbString.tbMedia.Album+" NVARCHAR(30)NULL," +
                DbString.tbMedia.DataAcquisizione+" DATATIME NOT NULL," +
                DbString.tbMedia.Dimensione+" INT NULL," +
                DbString.tbMedia.Altezza+" INT NULL," +
                DbString.tbMedia.Larghezza+" INT NULL," +
                DbString.tbMedia.Formato+" NVARCHAR(15) NOT NULL," +
                DbString.tbMedia.Orientamento+" INT NULL," +
                DbString.tbMedia.GpsLat+" REAL NULL," +
                DbString.tbMedia.GpsLong+" REAL NULL," +
                DbString.tbMedia.Server+" INT NOT NULL," +
                DbString.tbMedia.FKDispositivo+" INT NOT NULL)";

        String qF="CREATE TABLE "+DbString.tbFoto.tbNome+
                " ("+DbString.tbFoto.ID+" INTEGER NOT NULL," +
                DbString.tbFoto.Flash+" INT)";
        String qV="CREATE TABLE "+DbString.tbVideo.tbNome+
                " ("+DbString.tbVideo.ID+" INTEGER NOT NULL," +
                DbString.tbVideo.Durata+" INT)";

        //effettua il Create Database + le DDL
        db.execSQL(qU);
        db.execSQL(qD);
        db.execSQL(qM);
        db.execSQL(qF);
        db.execSQL(qV);
        Log.i("DbLocale: "+this.getDatabaseName(),"Creato!");
    }

    /*viene invocato nel momento in cui di richiede una altra versione del db*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {  }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.i("DbLocale: "+this.getDatabaseName(),"Aperto Correttamente!");
        super.onOpen(db);
    }

}
