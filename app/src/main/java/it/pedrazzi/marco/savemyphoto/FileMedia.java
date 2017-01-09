package it.pedrazzi.marco.savemyphoto;

import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by elmer on 29/11/16.
 */
public class FileMedia implements Comparable,Parcelable{


    public FileMedia() {

    }

    public String getPath() {return path;}

    public String getBucket() {
        return bucket;
    }

    public String getMimeType() { return mimeType; }
    public int getGiorno() {return giorno;}

    public int getMese() {
        return mese;
    }

    public int getAnno() {
        return anno;
    }

    private String path;
    private String bucket;
    private String mimeType;
    private int giorno;
    private int mese;
    private int anno;

    public FileMedia(int giorno,int mese,int anno, String path,String bucket,String mimeType){
        super();
        this.path=path;
        this.bucket=bucket;
        this.mimeType=mimeType;
        this.giorno=giorno;
        this.mese=mese;
        this.anno=anno;
    }

    public int compareTo(Object o) {
        if (o!=null){
            FileMedia objParametro = (FileMedia)o;

            if (this.anno>objParametro.anno){
                return -1;
            }else{
                if (this.anno==objParametro.anno){ //se anno è uguale

                    if (this.mese>objParametro.mese)
                    {
                        return -1;
                    }
                    else {
                            if(this.mese==objParametro.mese) //se mese e anno sono uguali
                            {
                                return 0;
                            }
                    }
                }
            }
        }
        return 1;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.bucket);
        dest.writeString(this.mimeType);
        dest.writeInt(this.giorno);
        dest.writeInt(this.mese);
        dest.writeInt(this.anno);
    }

    @SuppressWarnings("unused")
    protected FileMedia(Parcel in) {
        this();
        readFromParcel(in);

    }

    private void readFromParcel(Parcel in) {
        this.path = in.readString();
        this.bucket = in.readString();
        this.mimeType = in.readString();
        this.giorno = in.readInt();
        this.mese = in.readInt();
        this.anno = in.readInt();
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
}

