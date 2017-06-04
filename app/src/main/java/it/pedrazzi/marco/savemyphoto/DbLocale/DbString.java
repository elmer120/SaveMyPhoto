package it.pedrazzi.marco.savemyphoto.DbLocale;

/**
 * Created by Elmer on 15/04/2017.
 */

//TODO servono tutte le tabelle??
public final class DbString {

        public static final String nomeDB="SaveMyPhotoTestProva51";

        //TABELLE

        public class tbUtenti{
        public static final String tbNome="Utenti";
        public static final String ID="ID";
        public static final String NomeUtente="NomeUtente";
        public static final String DataNascita="DataNascita";
        public static final String Mail="Mail";
        public static final String Password="Password";
        }

        public class tbDispositivi{
            public static final String tbNome="Dispositivi";
            public static final String ID="ID";
            public static final String Marca="marca";
            public static final String Modello="Modello";
            public static final String VersioneAndroid="VersioneAndroid";
            public static final String SpazioLibero="SpazioLibero";
            public static final String FKUtenti="FKUtenti";
        }

        public class tbMedia{
            public static final String tbNome="Media";
            public static final String ID="ID";
            public static final String Nome="Nome";
            public static final String Album="Album";
            public static final String DataAcquisizione="DataAcquisizione";
            public static final String Dimensione="Dimensione";
            public static final String Altezza="Altezza";
            public static final String Larghezza="Larghezza";
            public static final String Formato="Formato";
            public static final String Orientamento="Orientamento";
            public static final String GpsLat="GpsLat";
            public static final String GpsLong="GpsLong";
            public static final String Server="Server";
            public static final String FKDispositivo="FKDispositivo";
        }

        public class tbFoto{
            public static final String tbNome="Foto";
            public static final String ID="ID";
            public static final String Flash="Flash";
        }


        public class tbVideo{
            public static final String tbNome="Video";
            public static final String ID="ID";
            public static final String Durata="Durata";
        }


}
