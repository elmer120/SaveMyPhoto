package it.pedrazzi.marco.savemyphoto.Media;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.security.Timestamp;
import java.util.Date;

/**
 * Created by elmer on 29/11/16.
 */
public class FileMedia implements Parcelable,Comparable<FileMedia>
{


    public FileMedia() { }

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

    public void setSelezionata(boolean selezionata) {
        this.selezionata = selezionata;
    }

    public Date getDataAcquisizione()
    {return dataAcquisizione;}

    public Integer getAltezza() {
        return altezza;
    }

    public Integer getLarghezza() {
        return larghezza;
    }

    public String getOrientamento() {
        return orientamento;
    }

    public Double getLatitudine() {
        return latitudine;
    }

    public Double getLongitudine() {
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
    private Double latitudine;
    private Double longitudine;
    private Integer dimensione;
    private Boolean suServer;
    private Boolean suDispositivo;
    private boolean selezionata=false;


    public FileMedia(Date dataAcquisizione, String path, String nome, String bucket, String mimeType,
                     Integer dimensione, Integer altezza, Integer larghezza, String orientamento,
                     Double latitudine, Double longitudine, Boolean suServer, Boolean suDispositivo){
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
    @Override
    public int compareTo(@NonNull FileMedia o) {
        if (o!=null)
        {
            FileMedia objParametro = (FileMedia)o;
            //ordino per timestamp
            if(this.dataAcquisizione.getTime()>objParametro.dataAcquisizione.getTime())
            {
                return -1;
            }
            else if(this.dataAcquisizione.getTime()==objParametro.dataAcquisizione.getTime())
            {
                return 0;
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
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(this.dataAcquisizione.getTime());
        dest.writeString(this.getNome());
        dest.writeString(this.getPath());
        dest.writeString(this.getBucket());
        dest.writeString(this.getMimeType());
        dest.writeInt(this.getAltezza());
        dest.writeInt(this.getLarghezza());
        dest.writeString(this.getOrientamento());
        dest.writeDouble(this.getLatitudine());
        dest.writeDouble(this.getLongitudine());
        dest.writeInt(this.getDimensione());
        dest.writeByte((byte) (this.getSuServer() ? 1 : 0));
        dest.writeByte((byte) (this.getSuDispositivo() ? 1 : 0));
        dest.writeByte((byte) (this.getSelezionata() ? 1 : 0));
    }

    public FileMedia(Parcel in)
    {
        this.dataAcquisizione = new Date(in.readLong());
        this.nome = in.readString();
        this.path=in.readString();
        this.bucket = in.readString();
        this.mimeType = in.readString();
        this.altezza=in.readInt();
        this.larghezza=in.readInt();
        this.orientamento=in.readString();
        this.latitudine=in.readDouble();
        this.longitudine=in.readDouble();
        this.dimensione=in.readInt();
        this.suServer=in.readByte()!=0;
        this.suDispositivo=in.readByte()!=0;
        this.selezionata=in.readByte()!=0;
    }


    public static final Creator<FileMedia> CREATOR = new Creator<FileMedia>() {
        @Override
        public FileMedia createFromParcel(Parcel in) {
            return new FileMedia(in);
        }

        @Override
        public FileMedia[] newArray(int size) {
            return new FileMedia[size];
        }
    };
}

