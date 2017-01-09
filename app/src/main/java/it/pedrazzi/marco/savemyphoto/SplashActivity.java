package it.pedrazzi.marco.savemyphoto;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{

    /*ArrayList<FileMedia> listCamera;
    ArrayList<FileMedia> listWhatsApp;*/
    TextView txtLoad;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        View decorView=getWindow().getDecorView(); //ottengo la view del activity
        int uiOptions=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);//nascondo temporaneamente i tasti navigazione virtuali
        //imposto l'evento click sul componente
        ImageView imageView=(ImageView)findViewById(R.id.splashImageview);
        imageView.setOnClickListener(this);
        this.txtLoad=(TextView)findViewById(R.id.txtLoad);
    }

    @Override
    protected void onStart() {
        super.onStart();
        txtLoad.setText("Recupero media dal dispositivo in corso..");
        //ContentProviderScanner contentProviderScanner=new ContentProviderScanner(this);


        this.intent=new Intent(this,SearchView.class);

        /*this.listCamera =new ContentProviderScanner(this).getListMedia(Album.Camera,true);

        intent.putParcelableArrayListExtra("listaCamera",this.listCamera);

        this.listWhatsApp=new ContentProviderScanner(this).getListMedia(Album.WhatsApp,true);
        intent.putParcelableArrayListExtra("listaWhatApp",this.listWhatsApp);*/


            new CountDownTimer(5000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    txtLoad.setText("Recupero media dal dispositivo in corso.."+millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    txtLoad.setText("Avvio applicazione");
                    startActivity(intent);
                    finish();



                }

            }.start();

    }


    @Override
    public void onClick(View v) {

        Toast.makeText(this,"click rilevato",Toast.LENGTH_SHORT).show();

    }


}
