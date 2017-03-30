package it.pedrazzi.marco.savemyphoto.http;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Elmer on 26/03/2017.
 */

public class testService extends IntentService {

    public testService(){
        super("testService");
    }

    @Override
    protected void onHandleIntent(Intent i)
    {
        int n=0;
        while(true)
        {
            Log.i("PROVA SERVICE", "Evento n."+n++);
            try {
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            { }
        }
    }

    @Override
    public void onDestroy()
    {
        Log.i("PROVA SERVICE", "Distruzione Service");
    }

}
