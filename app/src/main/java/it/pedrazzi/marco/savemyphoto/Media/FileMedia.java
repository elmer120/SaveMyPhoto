package it.pedrazzi.marco.savemyphoto.Media;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by elmer on 29/11/16.
 */
public class FileMedia implements Parcelable{


    public FileMedia() {

    }

    public String getPath() {return path;}

    public String getNome() {
        return nome;
    }

    public String getBucket() {
        return bucket;
    }

    public String getMimeType() { return mimeType; }

    public Integer getDimensione() {return dimensione;}

    public void setDimensione(Integer dimensione) {this.dimensione = dimensione;}

    public boolean getSelezionata() {
        return selezionata;
    }

    public void setSelezionata() {this.selezionata=selezionata;}

    public Date getDataAcquisizione() {
        return dataAcquisizione;
    }

    public Integer getAltezza() {
        return altezza;
    }

    public Integer getLarghezza() {
        return larghezza;
    }

    public String getOrientamento() {
        return orientamento;
    }

    public Integer getLatitudine() {
        return latitudine;
    }

    public Integer getLongitudine() {
        return longitudine;
    }

    public Boolean getSuServer() {
        return suServer;
    }

    public void setSuServer(Boolean suServer) {
        this.suServer = suServer;
    }

    public Boolean getSuDispositivo() {
        return suDispositivo;
    }

    public void setSuDispositivo(Boolean suDispositivo) {
        this.suDispositivo = suDispositivo;
    }

    private Date dataAcquisizione;
    private String nome;
    private String path;
    private String bucket;
    private String mimeType;
    private Integer altezza;
    private Integer larghezza;
    private String orientamento;
    private Integer latitudine;
    private Integer longitudine;
    private Integer dimensione;
    private Boolean suServer;
    private Boolean suDispositivo;

    private boolean selezionata=false;


    public FileMedia(Date dataAcquisizione,String path,String nome,String bucket,String mimeType,
                     Integer dimensione,Integer altezza,Integer larghezza,String orientamento,
                     Integer latitudine,Integer longitudine,Boolean suServer,Boolean suDispositivo){
        super();

        this.path=path;
        this.bucket=bucket;
        this.mimeType=mimeType;
        this.dataAcquisizione=dataAcquisizione;
        this.nome=nome;
        this.dimensione=dimensione;
        this.altezza=altezza;
        this.larghezza=larghezza;
        this.orientamento=orientamento;
        this.latitudine=latitudine;
        this.longitudine=longitudine;
        this.suServer=suServer;
        this.suDispositivo=suDispositivo;
    }

    //metodo per comparare gli oggetti
    public int compareTo(Object o)
    {
        if (o!=null){
            FileMedia objParametro = (FileMedia)o;

            if (this.dataAcquisizione.getYear()>objParametro.dataAcquisizione.getYear()){
                return -1;
            }else{
                if (this.dataAcquisizione.getYear()==objParametro.dataAcquisizione.getYear()){ //se anno è uguale

                    if (this.dataAcquisizione.getMonth()>this.dataAcquisizione.getMonth())
                    {
                        return -1;
                    }
                    else {
                            if(this.dataAcquisizione.getMonth()==objParametro.dataAcquisizione.getMonth()) //se mese e anno sono uguali
                            {
                                return 0;
                            }
                    }
                }
            }
        }
        return 1;
    }

//Parcelable per trasferire l'oggetto tra activity

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.nome);
        dest.writeString(this.bucket);
        dest.writeString(this.mimeType);

    }

    @SuppressWarnings("unused")
    protected FileMedia(Parcel in) {
        this();
        readFromParcel(in);

    }

    private void readFromParcel(Parcel in) {
        this.path = in.readString();
        this.nome = in.readString();
        this.bucket = in.readString();
        this.mimeType = in.readString();

    }

    public static final Creator<FileMedia> CREATOR = new Creator<FileMedia>() {
        @Override
        public FileMedia createFromParcel(Parcel source) {
            return new FileMedia(source);
        }

        @Override
        public FileMedia[] newArray(int size) {
            return new FileMedia[size];
        }
    };

    public boolean isSelezionata() {
        return selezionata;
    }

    public void setSelezionata(boolean selezionata) {
        this.selezionata = selezionata;
    }
}

