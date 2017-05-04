package it.pedrazzi.marco.savemyphoto.AAold;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.FileMedia;


public class ListFileMedia extends ArrayList<FileMedia> implements Parcelable{

    int countIntestazioni=0;


    ArrayList<Integer> indexIntestazioni= new ArrayList<Integer>();
    ArrayList<Integer> nElementiPerIntestazione= new ArrayList<Integer>();

   /*public void setPrimaryStorage(File primaryStorage) {
        this.primaryStorage = primaryStorage;
    }

    public void setSecondaryStorage(File secondaryStorage) {this.secondaryStorage = secondaryStorage;}

   // private File primaryStorage=null;

   // private File secondaryStorage=null;

    //Costruttore in caso che non funzioni la ricerca nel MediaStoreProvider*/

  /*  public ListFileMedia(File primaryStorage,File secondaryStorage) throws FileNotFoundException {
        this.primaryStorage=primaryStorage; //mem tel
        this.secondaryStorage=secondaryStorage; //sd

    }*/

   //popola la lista di media
    //TODO -- Controllare date sembra che dicembre sia conteggiato come mese 11!! (prob. 0-11)
    /*public void AggiornaLista() throws FileNotFoundException {

        File[] allImagesFile=this.primaryStorage.listFiles();

        File[] pImagesFile=this.primaryStorage.listFiles();
        File[] sImagesFile=this.secondaryStorage.listFiles();

        if (sImagesFile!=null && pImagesFile!=null) //controllo se ci sono file nel sd esterna
        {
            allImagesFile=null;
            allImagesFile=new File[pImagesFile.length+sImagesFile.length];
            //array sorgente, index sorgente,array destinazione,index destinazione,index count destinazione
            System.arraycopy(pImagesFile,0,allImagesFile,0,pImagesFile.length);
            System.arraycopy(sImagesFile,0,allImagesFile,pImagesFile.length,sImagesFile.length);
        }

        //dichiarazioni variabili
        ExifInterface metadati =null; //interfaccia exif
        DateFormat format = new SimpleDateFormat("yyyy:MM:dd ", Locale.ENGLISH); //formato data
        String dataString; //data foto
        Calendar calendario =Calendar.getInstance();
        if (allImagesFile.length!=0) {
            for (int i = 0; i < allImagesFile.length; i++) {
                try {

                    File File = allImagesFile[i];

                    metadati = new ExifInterface(File.getAbsolutePath());
                    dataString = metadati.getAttribute("DateTime");
//dataString=null;
                    //se c'è la data di acquisizione nel exif
                    if (dataString != null && File.getName().endsWith(".jpg") || File.getName().endsWith(".png")) {

                        //converto in calendar
                        //esempio 2015:05:10 10:32:05

                        calendario.setTime(format.parse(dataString));
                        int mese = calendario.get(Calendar.MONTH);
                        int anno = calendario.get(Calendar.YEAR);
                        int giorno = calendario.get(Calendar.DAY_OF_MONTH);


                        this.add(new FileMedia(0,0,0, File,"",""));
                        Log.d("Exif date: ", File.getName() + " ");
                    } else //imposto data ultima modifica del File
                    {
                        calendario.setTimeInMillis(File.lastModified());
                        int mese = calendario.get(Calendar.MONTH);
                        int anno = calendario.get(Calendar.YEAR);
                        int giorno = calendario.get(Calendar.DAY_OF_MONTH);

                        this.add(new FileMedia(calendario, File,"",""));

                        if (File.getName().endsWith(".jpg") || File.getName().endsWith(".png")) {
                            Log.w("Exif date null: ", File.getName() + " Use last modified! " + calendario.toString());
                        } else {
                            Log.d("Video: ", File.getName() + " Use last modified! " + calendario.toString());
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.w("error", "i");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            //ordino la lista
            Collections.sort(this);

            this.CountHeaders();
            this.GetElementsForHeader();
        }else
        {

                throw new FileNotFoundException("Risultato scansione: Nessun video/foto trovato sul dispositivo!");

        }

    }
*/

    //Ritorna il numero totale di intestazioni e la loro posizione
    public void CountHeaders() {

        if (this.size() < 2) { //se c'è un solo file
            indexIntestazioni.add(0);
            this.countIntestazioni = indexIntestazioni.size();
            nElementiPerIntestazione.add(1);
        } else {

            for (int i = 0; i < this.size() - 1; i++) {
                if (this.get(i).compareTo(this.get(i + 1)) == -1) {
                    indexIntestazioni.add(i + 1);
                }
            }
            this.countIntestazioni = indexIntestazioni.size() - 1;
        }
    }
    //Calcola il numero di elementi per ogni intestazione
    public void GetElementsForHeader(){
        for (int i = 0; i <indexIntestazioni.size()-1; i++) {


            nElementiPerIntestazione.add((indexIntestazioni.get(i + 1) - indexIntestazioni.get(i)));

        }
    }



    public ListFileMedia(){};


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.countIntestazioni);
        dest.writeList(this.indexIntestazioni);
        dest.writeList(this.nElementiPerIntestazione);
        //dest.writeSerializable(this.primaryStorage);
        //dest.writeSerializable(this.secondaryStorage);
        int size = this.size();


        dest.writeInt(size);

        for (int i = 0; i < size; i++) {
            FileMedia r = this.get(i);

            dest.writeString(r.getPath());
            dest.writeString(r.getBucket());
            dest.writeString(r.getMimeType());

            dest.writeInt(r.getGiorno());
            dest.writeInt(r.getMese());
            dest.writeInt(r.getAnno());

        }
    }
    private void readFromParcel(Parcel in) {
        this.clear();
        // First we have to read the list size
        int size = in.readInt();

        for (int i = 0; i < size; i++) {
            FileMedia r = new FileMedia(in.readInt(),in.readInt(),in.readInt(),in.readString(),in.readString(), in.readString(), in.readString(),in.readInt(),
                    in.readInt(),in.readInt(),in.readString(),in.readInt(),in.readInt());
            this.add(r);
        }
    }



    @SuppressWarnings("unused")
    protected ListFileMedia(Parcel in) {
        this();
        readFromParcel(in);
        this.countIntestazioni = in.readInt();
        this.indexIntestazioni = new ArrayList<Integer>();
        in.readList(this.indexIntestazioni, Integer.class.getClassLoader());
        this.nElementiPerIntestazione = new ArrayList<Integer>();
        in.readList(this.nElementiPerIntestazione, Integer.class.getClassLoader());
        //this.primaryStorage = (File) in.readSerializable();
        //this.secondaryStorage = (File) in.readSerializable();
    }

    public static final Creator<ListFileMedia> CREATOR = new Creator<ListFileMedia>() {
        @Override
        public ListFileMedia createFromParcel(Parcel source) {
            return new ListFileMedia(source);
        }

        @Override
        public ListFileMedia[] newArray(int size) {
            return new ListFileMedia[size];
        }
    };
}
