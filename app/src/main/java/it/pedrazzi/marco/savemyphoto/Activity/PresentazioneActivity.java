package it.pedrazzi.marco.savemyphoto.Activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.Media.Album;
import it.pedrazzi.marco.savemyphoto.Media.ContentProviderScanner;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;
import it.pedrazzi.marco.savemyphoto.R;

/**
 * Created by Elmer on 04/06/2017.
 */

public class PresentazioneActivity extends Activity implements View.OnClickListener{

    public ArrayList<FileMedia> getListMedia()
    {
        return listMedia;
    }
    ArrayList<FileMedia> listMedia;
    String nomeUtente;
    int idDispositivo;

    PresentazionePagerAdapter presentazionePagerAdapter;
    ViewPager mViewPager;
    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentazione);

        //recupero dati da activity precedente
        Bundle bundle=this.getIntent().getExtras();
        this.nomeUtente =bundle.getString("nomeUtente");
        this.idDispositivo = bundle.getInt("idDispositivo");
        this.position = bundle.getInt("position");
        this.listMedia= bundle.getParcelableArrayList("listMedia");

        //istanzio il pagerAdapter
        this.presentazionePagerAdapter = new PresentazionePagerAdapter(this,listMedia);
        this.mViewPager = (ViewPager) findViewById(R.id.presentatore);
        mViewPager.setAdapter(presentazionePagerAdapter);

        //setto i listener
        ImageButton imageButton=(ImageButton)this.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {

            case R.id.imageButton:
                FileMedia media=this.listMedia.get(this.mViewPager.getCurrentItem());
                Toast.makeText(this,
                        "Nome: "+media.getNome()+
                                "\n DataAcqu: "+media.getDataAcquisizione()+
                                "\n MimeType: "+media.getMimeType()+
                                "\n Dimensione: "+media.getDimensione()+
                                "\n H: "+media.getAltezza()+
                                "\n L: "+media.getLarghezza()+
                                "\n Path: " + media.getPath()+
                                "\n Orientamento: "+media.getOrientamento()+
                                "\n Latitudine: " + media.getLatitudine()+
                                "\n Longitudine: " + media.getLongitudine()+
                                "\n Su server: " + media.getSuServer()+
                                "\n Su dispositivo: " + media.getSuDispositivo()
                        ,Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
