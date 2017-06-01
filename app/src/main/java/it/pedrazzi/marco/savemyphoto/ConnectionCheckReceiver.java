package it.pedrazzi.marco.savemyphoto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Elmer on 19/05/2017.
 */
//broadcast receiver in ascolto per cambio/assenza connessione
public class ConnectionCheckReceiver extends BroadcastReceiver {
    public ConnectionCheckReceiver()
    {
        super();
    }

    //all'evento di sistema
    @Override
    public void onReceive(Context ctx, Intent intent)
    {

        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo retiAttive = connectivityManager.getActiveNetworkInfo();

        //se ci sono reti attive
        if (retiAttive != null)
        {
            //controllo se è in corso la connessione
            if (retiAttive.isConnectedOrConnecting())
            {
                Log.i(this.getClass().getSimpleName(),"Connessione in corso...");

                //controllo se è connesso
                if(retiAttive.isConnected())
                {
                    Log.i(this.getClass().getSimpleName(),"Connessione attiva!");
                }
            }
        }
        else
        {
            Log.i("ConnectionCheck: ", "Nessuna connessione rilevata!");
            Toast.makeText(ctx,"Attenzione connessione assente!",Toast.LENGTH_LONG).show();
            Log.i("ctx",ctx.getPackageCodePath());

        }




    }
}
