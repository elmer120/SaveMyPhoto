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
import java.lang.reflect.Array;
import java.util.concurrent.ExecutionException;

import it.pedrazzi.marco.savemyphoto.WebService.AssociaNuovoDispositivo;
import it.pedrazzi.marco.savemyphoto.WebService.CredenzialiCheckAsync;
import it.pedrazzi.marco.savemyphoto.WebService.NuovoUtente;

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
                    //se il database locale non c'è, significa che è una nuovo dispositivo e bisogna associarlo all'account esistente
                    if (!DatabaseCheck(this, DbString.nomeDB))
                    {

                        //allora richiedo il wifi attivo per recuperare il mac-addr e accedere al db remoto
                        if (WifiCheck())
                        {
                            //controllo che l'utente esista e che la password sia corretta
                            String[] arrayString={getEditTextUtente().getText().toString(),getEditTextPassword().getText().toString()};
                            CredenzialiCheckAsync credenzialiCheckAsync=new CredenzialiCheckAsync();
                            credenzialiCheckAsync.execute(arrayString);

                            try {
                                if (credenzialiCheckAsync.get())
                                {
                                    //Associo il nuovo dispositivo nel db remoto e popolo il db locale
                                    if(AssociaNuovoDispositivo())
                                    {
                                        Toast.makeText(this, "Nuovo dispositivo associato correttamente all'account di "+getEditTextUtente().getText().toString()+" !", Toast.LENGTH_SHORT).show();
                                        AvvioActivitySuccessiva(getEditTextUtente().getText().toString());
                                        //rimuovo dallo stack l'activity precedente e la corrente
                                        this.getParent().finish();
                                        finish();
                                        return;
                                    }

                                    Toast.makeText(this, "Ops qualcosa è andato storto!!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(this, "Utente o password errati!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else //se c'è posso far fare il login anche offline controllando che le credenziali siano corrette nel db locale
                    {
                        //controllo utente e password
                        if(CredenzialiCheckDbLocale())
                        {

                            Toast.makeText(this, "Bentornato!! "+ getEditTextUtente().getText().toString(), Toast.LENGTH_SHORT).show();
                            //avvio activity
                            AvvioActivitySuccessiva(getEditTextUtente().getText().toString());
                            finish();
                        }
                        else
                        {
                            Toast.makeText(this, "Utente o password errati!!", Toast.LENGTH_SHORT).show();
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

    //Controlla credenziali
    private boolean CredenzialiCheckDbLocale() {

        String utenteInserito = getEditTextUtente().getText().toString();
        String passwordInserita = getEditTextPassword().getText().toString();

        return dBgestione.UtenteCheckDbLocale(utenteInserito, passwordInserita);
    }

    //Associa nuovo dispositivo e registra credenziali nel db locale
    private boolean AssociaNuovoDispositivo()
    {
        String nomeUtente=getEditTextUtente().getText().toString();
        String password=getEditTextPassword().getText().toString();
        String macAddr=this.macAddr;
        String marca="HTC";
        String modello="10";
        String versioneAndroid="7";
        Integer spazioLibero=10;

        NuovoUtente nuovoUtente=new NuovoUtente(nomeUtente,"",password,macAddr,marca,modello,versioneAndroid,spazioLibero);
        AssociaNuovoDispositivo associaNuovoDispositivo=new AssociaNuovoDispositivo();
        associaNuovoDispositivo.execute(nuovoUtente);
        try {

            //se l'inserimento del nuovo dispositivo sul db remoto è andata a buon fine
            if(associaNuovoDispositivo.get()) {

                //registro anche in locale
                DBgestione dBgestione = new DBgestione(this);
                if (dBgestione.RegistrazioneDbLocale(nomeUtente,"", password, macAddr, marca, modello, versioneAndroid, spazioLibero)) {
                    return true;
                }
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
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


