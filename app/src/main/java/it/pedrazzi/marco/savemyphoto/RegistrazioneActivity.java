package it.pedrazzi.marco.savemyphoto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

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
    private String UtenteBundle;
    private DBgestione dBgestione;
    private String macAddr;

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

            //richiedo il wifi attivo per recuperare il mac-addr e accedere al db remoto
            if (WifiCheck())
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
                            if(Registrazione())
                            {
                                Toast.makeText(this, "Registrazione di " + getEditTextUtente().getText().toString() + " avvenuta con successo!!", Toast.LENGTH_SHORT).show();
                                AvvioActivitySuccessiva(getEditTextUtente().getText().toString());
                                //rimuovo dallo stack l'activity precedente e la corrente
                                this.getParent().finish();
                                finish();
                                return;
                            }
                            else
                            {
                                Toast.makeText(this, "Utente o dispositivo già presente!!", Toast.LENGTH_SHORT).show();
                            }
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

    //Controllo se il wifi è attivo cosi posso recuperare il mac-address
    private boolean WifiCheck()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo retiAttive = connectivityManager.getActiveNetworkInfo();

        //se ci sono reti attive faccio il check sul tipo di connessione
        if (retiAttive != null && retiAttive.getType() == ConnectivityManager.TYPE_WIFI)
        {
            if(retiAttive.isConnected()) {
                final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                this.macAddr = wifiManager.getConnectionInfo().getMacAddress();
                Log.i(retiAttive.getTypeName(), "Mac-Address: " + macAddr);
                return true;
            }
        }
        Log.i("WifiCheck: ","Nessuna connessione rilevata");
        Toast.makeText(this, "Impossibile procedere!\n Alla registrazione o al primo accesso su un nuovo dispositivo è richiesta l'attivazione del wifi.", Toast.LENGTH_LONG).show();
        return false;
    }

    //registra nuovo utente nel db remoto e aggiungi le credenziali nel db locale
    private boolean Registrazione()
    {
        //recupero dati utente e del dispositivo
        String nomeUtente=getEditTextUtente().getText().toString();
        String mail=getEditTextMail().getText().toString();
        String password=getEditTextPassword().getText().toString();
        String macAddr=this.macAddr;
        String marca=   Build.MANUFACTURER;
        String modello= Build.MODEL;
        String versioneAndroid= null;
        try
        {
            versioneAndroid = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            versioneAndroid="Non trovata";
            e.printStackTrace();
        }

        int spazioLibero=10;

        //chiamo il Web service remoto
        NuovoUtente nuovoUtente=new NuovoUtente(nomeUtente,mail,password,macAddr,marca,modello,versioneAndroid,spazioLibero);
        RegistrazioneUtenteAsync registrazioneUtenteAsync=new RegistrazioneUtenteAsync();
        registrazioneUtenteAsync.execute(nuovoUtente);

        try
        {
            //se la registrazione sul db remoto è andata a buon fine
            if(registrazioneUtenteAsync.get())
            {
                //registro anche in locale
                DBgestione dBgestione=new DBgestione(this);
                if(dBgestione.RegistrazioneDbLocale(nomeUtente,mail,password,macAddr,marca,modello,versioneAndroid,spazioLibero))
                {
                    return true;
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    //trova lo spazio libero rimasto sul filesystem
    public void GetSpazioLibero()
    {


        Double freeBytesExternal = (new File(getExternalFilesDir(null).toString()).getFreeSpace())/1073741824.0;

        //TODO arrotondare il double a 2 cifre decimali
    }

    //Avvio activity successiva
    private void AvvioActivitySuccessiva(String nomeUtente)
    {
        // mando nome utente
        Intent intent = new Intent(this, SearchView.class);
        Bundle bundle = new Bundle();
        //TODO Mandare utente mi serve??
        bundle.putString("utente",nomeUtente);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
