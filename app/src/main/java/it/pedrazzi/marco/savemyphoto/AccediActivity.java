package it.pedrazzi.marco.savemyphoto;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.concurrent.ExecutionException;

import it.pedrazzi.marco.savemyphoto.WebService.FKCWSsaveMyphotoSoap12;
import it.pedrazzi.marco.savemyphoto.WebService.NuovoUtente;
import it.pedrazzi.marco.savemyphoto.WebService.RegistrazioneUtenteAsync;

public class AccediActivity extends AppCompatActivity implements View.OnClickListener //imposto l'interfaccia per l'evento on click
{
    public DBgestione dBgestione=null;
    public String macAddr=null;


    public EditText getEditTextUtente() {
        editTextUtente=(EditText) findViewById(R.id.txtUtente);
        return editTextUtente;
    }
    public EditText getEditTextPassword() {
        editTextPassword=(EditText) findViewById(R.id.txtPassword);
        return editTextPassword;
    }


    private EditText editTextUtente;
    private EditText editTextPassword;
    private String UtenteBundle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accedi);

        //recupero il pulsante e setto l'evento on click
        Button btnAvanti=(Button)findViewById(R.id.btnAvanti);
        btnAvanti.setOnClickListener(this);

        // creo db istanzio la classe che consente le operazioni sul db
        this.dBgestione=new DBgestione(this);

    }

    @Override
    public void onClick(View v)
    {
        //intercetto il click
        if(v.getId()==R.id.btnAvanti)
        {
            //se utente è compilato
            if(!isEmpty(getEditTextUtente()))
            {
                //se password è compilato
                if (!isEmpty(getEditTextPassword()))
                {
                    //se il database locale non c'è significa che c'è da associare un nuovo dispositivo all'account
                    if (!DatabaseCheck(this, DbString.nomeDB))
                    {

                        //allora richiedo il wifi attivo per recuperare il mac-addr e accedere al db remoto
                        if (WifiCheck())
                        {
                            //controllo che l'utente esista e che la password sia corretta
                            if (true)
                            {
                                //inserisco il nuovo dispositivo nel db remoto e popolo il db locale

                                Toast.makeText(this, "Nuovo dispositivo associato correttamente a !!", Toast.LENGTH_SHORT).show();

                            }
                            Toast.makeText(this, "Utente o password errati!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else //se c'è posso far fare il login anche offline controllando che le credenziali siano corrette nel db locale
                    {
                        if(true)
                        {

                            Toast.makeText(this, "Bentornato !!", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(this, "Utente o password errati!!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
                FKCWSsaveMyphotoSoap12 service=new FKCWSsaveMyphotoSoap12();
                service.enableLogging=true;
                try {
                    service.MacAddrCheck("5454");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*
                //verifico che non sia un nuovo dispositivo
                if(!DatabaseCheck(this,DbString.nomeDB))
                {
                    if (WifiCheck())
                    {
                        AccessoDaNuovoDispositivo();
                    }
                }
                //altrimenti controllo i campi inseriti e faccio check sul Utente
                else if ((getEditTextPassword().getText().toString().trim().length()>0 && getEditTextUtente().getText().toString().trim().length()>0))
                {
                    if (UtenteCheckDbLocale()) {

                        AvvioActivitySuccessiva(getEditTextUtente().getText().toString());

                    } else {
                        Toast.makeText(this, "Utente o password errati!!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "Nome utente e/o password non presenti!!", Toast.LENGTH_SHORT).show();
                }*/

/*
                if(WifiCheck())
                {
                    //se i campi non sono vuoti
                    if (!isEmpty(getEditTextUtente()) && !isEmpty(getEditTextPassword()) && !isEmpty(getEditTextMail()))
                    {
                        if (Registrazione()) {
                            Toast.makeText(this, "Registrazione di " + getEditTextUtente().getText().toString() + " avvenuta con successo!!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Utente già presente!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }*/
        }

    }

    //controllo la textbox
    private boolean isEmpty(EditText editText) {

        if(editText.getText().toString().trim().length() == 0)
        {
            Toast.makeText(this, "Non hai inserito "+editTextUtente.getHint(), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


//TODO Controllare il testo immesso in quanto sqlite non controlla i tipi di dati
    //Controlla credenziali
    private boolean UtenteCheckDbLocale() {

        String utenteInserito = getEditTextUtente().getText().toString();
        String passwordInserita = getEditTextPassword().getText().toString();

        return dBgestione.UtenteCheckDbLocale(utenteInserito, passwordInserita,false);
    }

    //registra nuovo utente
    private boolean Registrazione()
    {
        //fare activity precedente a questa con la scelta registrati o accedi (su accedi bisogna controllare se il dispositivo è già associato ad un account)


        String nomeUtente=getEditTextUtente().getText().toString();

       /* String mail=getEditTextMail().getText().toString();
        String password=getEditTextPassword().getText().toString();
        String macAddr=this.macAddr;
        String marca="HTC";
        String modello="10";
        String versioneAndroid="7";
        Integer spazioLibero=10;

        NuovoUtente nuovoUtente=new NuovoUtente(nomeUtente,mail,password,macAddr,marca,modello,versioneAndroid,spazioLibero);
        RegistrazioneUtenteAsync registrazioneUtenteAsync=new RegistrazioneUtenteAsync();
        registrazioneUtenteAsync.execute(nuovoUtente);
        try {
            return registrazioneUtenteAsync.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        return false;
        //return dBgestione.RegistraUtente(utenteInserito,"data","prova@mail.com",passwordInserita);
    }

    //Accesso da nuovo dispositivo
    private boolean AccessoDaNuovoDispositivo() {
        String utenteInserito = getEditTextUtente().getText().toString();
        String passwordInserita = getEditTextPassword().getText().toString();
        if(dBgestione.AccessoDaNuovoDispositivo(utenteInserito,passwordInserita))
        {return true;}
        Toast.makeText(this, "Impossibile procedere!\n Utente e/o password non sono presenti sul server!!.", Toast.LENGTH_SHORT).show();
        return false;
    }

    //Avvio activity successiva
    private void AvvioActivitySuccessiva(String nomeUtente)
    {
        //stampo msg e mando nome utente
        Toast.makeText(this, "Benvenuto " + nomeUtente, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SearchView.class);
        Bundle bundle = new Bundle();
        //TODO Mandare utente mi server??
        bundle.putString("utente","te");
        intent.putExtras(bundle);
        startActivity(intent);
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

    //Controllo  c'è connessione
    private boolean ConnessioneCheck(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo retiAttive = connectivityManager.getActiveNetworkInfo();

        //se ci sono reti attive faccio il check sul tipo di connessione
        if (retiAttive != null)
        {
            if(retiAttive.getType()==connectivityManager.TYPE_WIFI)
            {
                if(retiAttive.isConnected())
                {
                    Log.i(retiAttive.getTypeName(), retiAttive.getState().name());
                    return true;
                }
            }
            else if(retiAttive.getType()==connectivityManager.TYPE_MOBILE)
            {
                Log.i(retiAttive.getTypeName(), retiAttive.getState().name());
                return true;
            }
        }

        Toast.makeText(this, "Impossibile procedere!\n Nessuna connessione rilevata!!!", Toast.LENGTH_LONG).show();
        return false;
    }


    //Controllo che il database esista in caso contrario faccio il wifi check su accesso e registrazione
    private static boolean DatabaseCheck(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}


