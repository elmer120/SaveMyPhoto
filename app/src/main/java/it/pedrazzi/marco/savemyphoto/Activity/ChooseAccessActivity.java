package it.pedrazzi.marco.savemyphoto.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

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


    @Override
    protected void onStart() {
        super.onStart();

        //TODO rivedere permessi
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report)
            {
                if(report.isAnyPermissionPermanentlyDenied())
                {
                    //Toast.makeText(this,"L'applicazione richiede tutti i permessi siano accettati...l'applicazione verrà terminata!",Toast.LENGTH_LONG).show();
                }
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token)
            {

            }
        }).check();
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
