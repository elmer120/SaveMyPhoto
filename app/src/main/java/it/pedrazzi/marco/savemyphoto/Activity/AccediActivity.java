package it.pedrazzi.marco.savemyphoto.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.io.File;

import it.pedrazzi.marco.savemyphoto.DbLocale.DBgestione;
import it.pedrazzi.marco.savemyphoto.DbLocale.DbString;
import it.pedrazzi.marco.savemyphoto.R;
import it.pedrazzi.marco.savemyphoto.WebService.AssociaNuovoDispositivoAsync;
import it.pedrazzi.marco.savemyphoto.WebService.CredenzialiCheckAsync;
import it.pedrazzi.marco.savemyphoto.WebService.NuovoUtente;

public class AccediActivity extends AppCompatActivity implements View.OnClickListener //imposto l'interfaccia per l'evento on click
{


    public EditText getEditTextUtente() {
        editTextUtente=(EditText) findViewById(R.id.txtUtente);
        return editTextUtente;
    }
    public EditText getEditTextPassword() {
        editTextPassword=(EditText) findViewById(R.id.txtPassword);
        return editTextPassword;
    }

    public CheckBox getCbMantieniAccesso() {
        return cbMantieniAccesso;
    }

    public void setCbMantieniAccesso(CheckBox cbMantieniAccesso) {
        this.cbMantieniAccesso = cbMantieniAccesso;
    }

    private CheckBox cbMantieniAccesso;
    private EditText editTextUtente;
    private EditText editTextPassword;

    public String nomeUtente;
    public String password;
    public Integer idDispositivo;
    public String marca;
    public String modello;
    public String versioneAndroid;
    public Integer spazioLibero;

    public DBgestione dBgestione=null;

    ProgressBar progressBarAccOffline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accedi);

        //recupero il pulsante e setto l'evento on click
        Button btnAvanti=(Button)findViewById(R.id.btnAvanti);
        btnAvanti.setOnClickListener(this);

        this.cbMantieniAccesso=(CheckBox)findViewById(R.id.cBMantieniAccesso);

        this.progressBarAccOffline=(ProgressBar) findViewById(R.id.progressBarAcc);

        // creo db istanzio la classe che consente le operazioni sul db locale
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

                        //richiedo una connesione dati per accedere al db remoto
                        if (ConnectionCheck())
                        {
                            //Avvio task asincrono per controllare che l'utente esista e che la password sia corretta
                            String[] arrayString={getEditTextUtente().getText().toString(),getEditTextPassword().getText().toString()};
                            CredenzialiCheckAsync credenzialiCheckAsync=new CredenzialiCheckAsync(this,this);
                            credenzialiCheckAsync.execute(arrayString);
                        }

                    }
                    else //se c'è posso far fare il login anche offline controllando che le credenziali siano corrette nel db locale
                    {
                        //controllo utente e password
                        if(CredenzialiCheckDbLocale())
                        {

                            Toast.makeText(this, "Bentornato!! "+ getEditTextUtente().getText().toString(), Toast.LENGTH_SHORT).show();
                            this.progressBarAccOffline.setVisibility(View.INVISIBLE);
                            //avvio activity

                            this.idDispositivo=this.dBgestione.getIdDispositivo(nomeUtente);
                            SupportoActivity supportoActivity=new SupportoActivity();
                            supportoActivity.AvvioActivity(this,this.nomeUtente,this.idDispositivo, SearchViewActivity.class);
                            finish();
                        }
                        else
                        {
                            this.progressBarAccOffline.setVisibility(View.INVISIBLE);
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

    //Controlla credenziali db locale
    private boolean CredenzialiCheckDbLocale() {

        this.progressBarAccOffline.setVisibility(View.VISIBLE);
        this.nomeUtente = getEditTextUtente().getText().toString();
        String passwordInserita = getEditTextPassword().getText().toString();
        return dBgestione.UtenteCheckDbLocale(this.nomeUtente, passwordInserita,this.getCbMantieniAccesso().isChecked());
    }

    //Associa nuovo dispositivo e registra credenziali nel db locale
    public void AssociaNuovoDispositivo()
    {
        this.nomeUtente=getEditTextUtente().getText().toString();
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
        this.spazioLibero=10;


        //avvia task asincrono per associare il nuovo dispositivo nel db remoto
        NuovoUtente nuovoUtente=new NuovoUtente(nomeUtente,null,password,marca,modello,versioneAndroid,spazioLibero);
        AssociaNuovoDispositivoAsync associaNuovoDispositivo=new AssociaNuovoDispositivoAsync(this,this);
        associaNuovoDispositivo.execute(nuovoUtente);

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


    //Controllo che il database esista in caso contrario faccio il check su accesso e registrazione
    private static boolean DatabaseCheck(Context context, String dbName)
    {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}


