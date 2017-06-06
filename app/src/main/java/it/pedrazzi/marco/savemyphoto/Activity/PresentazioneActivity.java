package it.pedrazzi.marco.savemyphoto.Activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.Media.Album;
import it.pedrazzi.marco.savemyphoto.Media.ContentProviderScanner;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;
import it.pedrazzi.marco.savemyphoto.R;

/**
 * Created by Elmer on 04/06/2017.
 */

public class PresentazioneActivity extends Activity {

    ArrayList<FileMedia> listMedia;
    String nomeUtente;
    int idDispositivo;
    int position;
    PresentazionePagerAdapter presentazionePagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentazione);

        //recupero dati da activity precedente
        Bundle bundle=this.getIntent().getExtras();
        this.nomeUtente =bundle.getString("nomeUtente");
        this.idDispositivo = bundle.getInt("idDispositivo");
        this.position = bundle.getInt("position");
        this.listMedia=bundle.getParcelableArrayList("listMedia");
        this.presentazionePagerAdapter = new PresentazionePagerAdapter(this,listMedia);
        this.mViewPager = (ViewPager) findViewById(R.id.presentatore);
        mViewPager.setAdapter(presentazionePagerAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mViewPager.setCurrentItem(position);
    }

}
