package it.pedrazzi.marco.savemyphoto;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.DbLocale.DBgestione;

/**
 * Created by Elmer on 27/04/2017.
 */

public class DbSyncListMedia extends AsyncTask<ArrayList<FileMedia>,Void,Boolean> {

    private String macAddr;
    private Context ctx;
    private DBgestione dBgestione;

    public DbSyncListMedia(Context ctx, String macAddr)
    {
        this.ctx=ctx;
        this.macAddr=macAddr;
        this.dBgestione=new DBgestione(ctx);
    }

    @Override
    protected Boolean doInBackground(ArrayList<FileMedia>... arrayLists)
    {

        //scrittura lettura su db
        return dBgestione.SyncListMedia(arrayLists[0],this.macAddr);
    }
}
