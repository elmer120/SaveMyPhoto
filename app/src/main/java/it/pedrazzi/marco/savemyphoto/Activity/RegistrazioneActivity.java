package it.pedrazzi.marco.savemyphoto.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import it.pedrazzi.marco.savemyphoto.DbLocale.DBgestione;
import it.pedrazzi.marco.savemyphoto.R;
import it.pedrazzi.marco.savemyphoto.WebService.NuovoUtente;
import it.pedrazzi.marco.savemyphoto.WebService.RegistrazioneUtenteAsync;

public class RegistrazioneActivity extends Activity implements View.OnClickListener {

    public TextView getTextViewTitolo() {
        textViewTitolo=(TextView) findViewById(R.id.txtTitolo);
        return textViewTitolo;
    }
    public EditText getEditTextUtente() {
        editTextUtente=(EditText) findViewById(R.id.txtUtente);
        return editTextUtente;
    }
    public EditText getEditTextPassword() {
        editTextPassword=(EditText) findViewById(R.id.txtPassword);
        return editTextPassword;
    }
    public EditText getEditTextMail() {
        editTextMail=(EditText) findViewById(R.id.txtMail);
        return editTextMail;
    }
    public EditText getEditTextDataNascita() {
        editTextDataNascita=(EditText) findViewById(R.id.txtDataNascita);
        return editTextDataNascita;
    }

    private TextView textViewTitolo;
    private EditText editTextUtente;
    private EditText editTextPassword;
    private EditText editTextMail;
    private EditText editTextDataNascita;


    public String nomeUtente;
    public String mail;
    public String password;
    public Integer idDispositivo;
    public String marca;
    public String modello;
    public String versioneAndroid;
    public int spazioLibero;

    public DBgestione dBgestione=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        //recupero il pulsante e setto l'evento on click
        Button btnAvanti=(Button)findViewById(R.id.btnAvanti);
        btnAvanti.setOnClickListener(this);

        // creo db istanzio la classe che consente le operazioni sul db
        this.dBgestione=new DBgestione(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btnAvanti)
        {
            //richiedo una connesione dati per accedere al db remoto
            if (ConnectionCheck())
            {
                //se utente è compilato
                if (!isEmpty(getEditTextUtente()))
                {
                    //se password è compilato
                    if (!isEmpty(getEditTextPassword()))
                    {
                        //se mail è compilato
                        if (!isEmpty(getEditTextMail()))
                        {
                            //Effettuo la registrazione
                            Registrazione();
                        }

                    }
                }
            }
        }
    }


    //controllo la textbox
    private boolean isEmpty(EditText editText) {

        if(editText.getText().toString().trim().length() == 0)
        {
            Toast.makeText(this, "Non hai inserito "+editText.getHint(), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    //Controllo se c'è una connessione attiva
    private boolean ConnectionCheck()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo retiAttive = connectivityManager.getActiveNetworkInfo();

        //se ci sono reti attive
        if (retiAttive != null)
        {
                //controllo se c'è connessione
                if (retiAttive.isConnected())
                {
                    return true;
                }

        }
        Log.i("ConnectionCheck: ", "Nessuna connessione rilevata!");
        Toast.makeText(this, "Impossibile procedere!\n Alla registrazione o al primo accesso su un nuovo dispositivo è richiesta una connessione.", Toast.LENGTH_LONG).show();
        return false;
    }

    //registra nuovo utente nel db remoto e aggiunge le credenziali nel db locale
    private void Registrazione()
    {
        //recupero dati utente e del dispositivo
        this.nomeUtente=getEditTextUtente().getText().toString();
        this.mail=getEditTextMail().getText().toString();
        this.password=getEditTextPassword().getText().toString();
        this.marca=   Build.MANUFACTURER;
        this.modello= Build.MODEL;
        this.versioneAndroid= null;
        try
        {
            this.versioneAndroid = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            this.versioneAndroid="Non trovata";
            e.printStackTrace();
        }
        //TODO mancante spazio libero
        this.spazioLibero=10;

        //chiamo il Web service remoto
        NuovoUtente nuovoUtente=new NuovoUtente(nomeUtente,mail,password,marca,modello,versioneAndroid,spazioLibero);
        RegistrazioneUtenteAsync registrazioneUtenteAsync=new RegistrazioneUtenteAsync(this,this);
        registrazioneUtenteAsync.execute(nuovoUtente);
    }

    //trova lo spazio libero rimasto sul filesystem
    public void GetSpazioLibero()
    {
        Double freeBytesExternal = (new File(getExternalFilesDir(null).toString()).getFreeSpace())/1073741824.0;

        //TODO arrotondare il double a 2 cifre decimali
    }

}
