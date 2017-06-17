package it.pedrazzi.marco.savemyphoto.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.File;

import it.pedrazzi.marco.savemyphoto.DbLocale.DBgestione;

/**
 * Created by Elmer on 04/06/2017.
 */

public class SupportoActivity {

    //Avvio activity successiva
    public void AvvioActivity(Context ctx,String nomeUtente,int idDispositivo,Class activitySucc)
    {
        //preparo il bundle da inviare
        Bundle bundle = new Bundle();
        bundle.putString("nomeUtente",nomeUtente);
        bundle.putInt("idDispositivo",idDispositivo);

        //metto il bundle nel intent ed avvio l'activity
        Intent intent = new Intent(ctx, activitySucc);
        intent.putExtras(bundle);
        ((Activity) ctx).startActivity(intent);
        //rimuovo dallo stack l'activity  corrente
        ((Activity) ctx).finish();
        return;
    }




}
