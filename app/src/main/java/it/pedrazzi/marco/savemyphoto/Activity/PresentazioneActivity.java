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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentazione);
        this.listMedia=new ContentProviderScanner(this).getListMedia(Album.Camera,true);
        PresentazionePagerAdapter presentazionePagerAdapter = new PresentazionePagerAdapter(this,listMedia);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.presentatore);
        mViewPager.setAdapter(presentazionePagerAdapter);
    }
}
