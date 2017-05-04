package it.pedrazzi.marco.savemyphoto.WebService;

//classe di supporto per incapsulare i dati da inviare all'asyncTask
public class NuovoUtente {

    String nomeUtente;
    String mail;
    String password;
    String marca;
    String modello;
    String versioneAndroid;
    Integer spazioLibero;

    public NuovoUtente(String nomeUtente, String mail, String password, String marca, String modello, String versioneAndroid, Integer spazioLibero)
    {
        this.nomeUtente=nomeUtente;
        this.mail=mail;
        this.password=password;
        this.marca=marca;
        this.modello=modello;
        this.versioneAndroid=versioneAndroid;
        this.spazioLibero=spazioLibero;
    }

}
