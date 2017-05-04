package it.pedrazzi.marco.savemyphoto.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

import it.pedrazzi.marco.savemyphoto.DbLocale.DbString;
import it.pedrazzi.marco.savemyphoto.R;

public class ChooseAccessActivity extends Activity implements View.OnClickListener {

 private Button btnRegistrati;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_choose_access);
        ImageView background=(ImageView)findViewById(R.id.background);
        background.setImageDrawable(getResources().getDrawable(R.drawable.splash));

        this.btnRegistrati=(Button)findViewById(R.id.btnRegistrati);
        this.btnRegistrati.setOnClickListener(this);

        Button btnAccedi=(Button)findViewById(R.id.btnAccedi);
        btnAccedi.setOnClickListener(this);

        //se il database esiste il dispositivo è già registrato in un account sul server
        //dunque non pùò registrarsi un altra volta
        if(DatabaseCheck(this, DbString.nomeDB))
        {
            this.btnRegistrati.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override //invocato alla ripresa dalla sospensione 1°
    protected void onRestart() {
        super.onRestart();
        //se il database esiste il dispositivo è già registrato in un account sul server
        //dunque non pùò registrarsi un altra volta
        if(DatabaseCheck(this,DbString.nomeDB))
        {
            btnRegistrati.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnAccedi:
                AvviaActivity(AccediActivity.class);
                break;


            case R.id.btnRegistrati:
                AvviaActivity(RegistrazioneActivity.class);
                break;
        }
    }

    //passa all'activity successiva inviando la scelta effettuata
    private void AvviaActivity(Class activityClass)
    {
        Intent intent = new Intent(this,activityClass);
        startActivity(intent);
    }

    //Controllo che il database esista
    private static boolean DatabaseCheck(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}
