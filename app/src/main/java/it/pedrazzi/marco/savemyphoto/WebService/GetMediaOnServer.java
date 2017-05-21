package it.pedrazzi.marco.savemyphoto.WebService;

import android.os.AsyncTask;
import it.pedrazzi.marco.savemyphoto.WebService.Autogenerate.RRCArrayOfString;
import it.pedrazzi.marco.savemyphoto.WebService.Autogenerate.RRCWSsaveMyPhotoSoap;

/**
 * Created by Elmer on 21/05/2017.
 */

public class GetMediaOnServer extends AsyncTask <String[],Void,String[]> {

    String [] risultato;
    String nomeUtente;
    Integer idDispositivo;

    public GetMediaOnServer(String nomeUtente,Integer idDispositivo)
    {
        this.nomeUtente=nomeUtente;
        this.idDispositivo=idDispositivo;
    }

    @Override
    protected String[] doInBackground(String[]... strings)
    {
        RRCWSsaveMyPhotoSoap service=new RRCWSsaveMyPhotoSoap();
        service.enableLogging=true;
        try
        {
            //recupero i percorsi delle foto dal server
            RRCArrayOfString rrcArrayOfString=service.CheckMediaOnServer(nomeUtente,idDispositivo);

            if(rrcArrayOfString.getPropertyCount()>0)
            {
                this.risultato=new String[rrcArrayOfString.getPropertyCount()];

                for (int i = 0; i < risultato.length; i++)
                {
                    risultato[i] = rrcArrayOfString.get(i);
                }
            }
            return risultato;

        } catch (Exception e)
        {
            e.printStackTrace();
            return risultato;
        }
    }

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);


    }
}
