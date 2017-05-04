package it.pedrazzi.marco.savemyphoto.AAold;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by elmer on 24/11/16.
 */
public class FileScanner {


    public FileScanner() throws FileNotFoundException {

        this.primaryStorage=FindAndCheckPrimaryStorage();
        this.secondaryStorage=FindAndCheckSecondaryStorage();
    }

    public File getPrimaryStorage() {
        return primaryStorage;
    }

    public File getSecondaryStorage() {
        return secondaryStorage;
    }

    private File primaryStorage; //memoria built-in
    private File secondaryStorage; //sd-card

    private File directoryDcmi=null;

    private Context mContext;//riferimento al activity




   //Cerca media nella memoria principale e ritorna il percorso corretto
    private File FindAndCheckPrimaryStorage() throws FileNotFoundException{


        String statoMemoriaPrimaria=Environment.getExternalStorageState();
        Log.d("Stato memoriaPrimaria: ",statoMemoriaPrimaria);

        //se correttamente montata controllo la presenza della cartella dcmi
        if(statoMemoriaPrimaria.equals(Environment.MEDIA_MOUNTED)) {

                directoryDcmi = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

               //controllo le sottocartelle presenti e seleziono la directory della fotocamera
                if (directoryDcmi.exists())
                {
                    File[] fileContenuti=directoryDcmi.listFiles();
                    for (File file:fileContenuti) {
                        if (file.getName().equals("Camera")|| file.getName().equals("100MEDIA")|| file.getName().equals("100ANDRO"))
                        {
                            return file.getAbsoluteFile(); //ritorno percorso corretto
                        }
                    }
                    throw new FileNotFoundException("SubDirectory Dcmi not Found!");

                } else //nessuna directory dcmi trovata
                {
                    throw new FileNotFoundException("Directory Dcmi not found!");
                }
        }

        throw new FileNotFoundException("Memoria primaria non montata correttamente");

    }


    //Cerca media nella memoria secondaria("sd-card") e ritorna il percorso corretto
    public File FindAndCheckSecondaryStorage() throws FileNotFoundException{

        if (System.getenv("SECONDARY_STORAGE")!=null) //c'è sd
        {
            directoryDcmi=new File(System.getenv("SECONDARY_STORAGE")+"/DCIM");

            //controllo le sottocartelle presenti e seleziono la directory della fotocamera
            if(directoryDcmi.exists()) //se DCMI esiste
            {
                File[] fileContenuti=directoryDcmi.listFiles();
                for (File file:fileContenuti) {
                    if (file.getName().equals("Camera")|| file.getName().equals("100MEDIA")|| file.getName().equals("100ANDRO"))
                    {
                        return file.getAbsoluteFile(); //ritorno percorso corretto
                    }
                }
                throw new FileNotFoundException("SubDirectory Dcmi not Found!");

            }
            else{
                return directoryDcmi;
            }
        }
        Log.i("Memoria sd: ","Assente");
        return directoryDcmi;

    }

    /*
            case Environment.MEDIA_MOUNTED_READ_ONLY:
                Log.d("Stato memoriaPrimaria: ",statoMemoriaPrimaria);
                break;
            case Environment.MEDIA_CHECKING:
                Log.d("Stato memoriaPrimaria: ",statoMemoriaPrimaria);
                break;
            case Environment.MEDIA_EJECTING:
                Log.d("Stato memoriaPrimaria: ",statoMemoriaPrimaria);
                break;
            case Environment.MEDIA_BAD_REMOVAL:
                Log.d("Stato memoriaPrimaria: ",statoMemoriaPrimaria);
                break;
            case Environment.MEDIA_REMOVED:
                Log.d("Stato memoriaPrimaria: ",statoMemoriaPrimaria);
                break;
            case Environment.MEDIA_SHARED:
                Log.d("Stato memoriaPrimaria: ",statoMemoriaPrimaria);
                break;
            case Environment.MEDIA_UNKNOWN:
                Log.d("Stato memoriaPrimaria: ",statoMemoriaPrimaria);
                break;
            case Environment.MEDIA_UNMOUNTABLE:
                Log.d("Stato memoriaPrimaria: ",statoMemoriaPrimaria);
                break;
            case Environment.MEDIA_UNMOUNTED:
                Log.d("Stato memoriaPrimaria: ",statoMemoriaPrimaria);
                break;
*/
}