package it.pedrazzi.marco.savemyphoto;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener //imposto l'interfaccia per l'evento on click
{
    DBgestione dBgestione=null;

    public EditText getEditTextUtente() {
        editTextUtente=(EditText) findViewById(R.id.TfUtente);
        return editTextUtente;
    }

    public EditText getEditTextPassword() {
        editTextPassword=(EditText) findViewById(R.id.TfPassword);
        return editTextPassword;
    }
    public String getUtenteBundle() {
        return UtenteBundle;
    }

    public void setUtenteBundle(String utenteBundle) {
        UtenteBundle = utenteBundle;
    }



    public EditText editTextUtente;
    public EditText editTextPassword;


    public String UtenteBundle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //se non c'è creo il database, cmq istanzio la classe che consente le operazioni sul db
        this.dBgestione=new DBgestione(this);
        //recupero i widget e setto l'evento on click
        Button btnAccedi=(Button)findViewById(R.id.btnAccedi);
        btnAccedi.setOnClickListener(this);
        Button btnRegistrati=(Button)findViewById(R.id.btnRegistrati);
        btnRegistrati.setOnClickListener(this);



        //Toast.makeText(this, getApplicationContext().getFilesDir().getPath().toString(), Toast.LENGTH_LONG).show();;
    }

    @Override
    public void onClick(View v)
    {
        //switch sugli id per intercettare il widget cliccato
        switch (v.getId()) {
            case R.id.btnAccedi:
                if ((getEditTextPassword().getText().toString().trim().length()>0 && getEditTextUtente().getText().toString().trim().length()>0))
                {
                    if (CheckUtente()) {
                        //stampo msg e mando nome utente al activity search view
                        Toast.makeText(this, "Benvenuto " + getEditTextUtente().getText().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, SearchView.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("utente", getUtenteBundle());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Utente o password errati!!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "Nome utente e/o password non presenti!!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnRegistrati:
                //se i campi non sono vuoti
                if ((getEditTextPassword().getText().toString().trim().length()>0 && getEditTextUtente().getText().toString().trim().length()>0)) {
                    if (Registrazione()) {
                        Toast.makeText(this, "Registrazione di " + getEditTextUtente().getText().toString() + " avvenuta con successo!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Utente già presente!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Nome utente e/o password non presenti!!", Toast.LENGTH_SHORT).show();
                }

        }
    }

    //Controlla credenziali
    public boolean CheckUtente() {

        String utenteInserito = getEditTextUtente().getText().toString();
        String passwordInserita = getEditTextPassword().getText().toString();

        return dBgestione.RicercaUtente(utenteInserito, passwordInserita,false);
    }

    //registra nuovo utente
    public boolean Registrazione()
    {
        String utenteInserito=getEditTextUtente().getText().toString();
        String passwordInserita=getEditTextPassword().getText().toString();
        return dBgestione.RegistraUtente(utenteInserito,"data","prova@mail.com",passwordInserita);
    }

}

