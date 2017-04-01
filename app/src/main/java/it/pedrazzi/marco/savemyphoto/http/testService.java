package it.pedrazzi.marco.savemyphoto.http;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Elmer on 26/03/2017.
 */

public class testService extends IntentService {

    public testService(){
        super("testService");
    }

    @Override //1° avvio   ---richiamato solo una volta anche con molti avvi del service
    public void onCreate() {
        super.onCreate();
        Log.i(this.getClass().getSimpleName(), "On create");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.i(this.getClass().getSimpleName(), "OnStartCommand");
        return super.onStartCommand(intent, flags, startId);
        //return START_STICKY; //ricarica il servizio in caso di una sua "uccisione improvvisa"
    }

    @Override
    protected void onHandleIntent(Intent i)
    {
        String path=i.getExtras().getString("upload");
        Log.i(this.getClass().getSimpleName(),i.getExtras().getString("upload"));
        new HttpMultipart().Invia(path);
            try {
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            { }

    }

    @Override //2° stop
    public void onDestroy()
    {
        Log.i(this.getClass().getSimpleName(), "OnDestroy");
        super.onDestroy();
    }



}
