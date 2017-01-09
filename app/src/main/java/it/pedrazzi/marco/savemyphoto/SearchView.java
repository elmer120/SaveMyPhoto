package it.pedrazzi.marco.savemyphoto;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.lalosoft.easypermission.RegisterPermission;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView; //headers gridview

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@RegisterPermission(permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})

public class SearchView extends FragmentActivity implements AdapterView.OnItemClickListener,ActivityCompat.OnRequestPermissionsResultCallback{


    private ArrayList<FileMedia> listCamera;
    private ArrayList<FileMedia> listWhatApp;
    private StickyGridHeadersGridView gridHeadersGridView;

    // lista di fragment
    List<Fragment> fragments = new Vector<>();
    //pagerAdapter
    private PagerAdapter mPagerAdapter;
    // view pager
    private ViewPager viewPager;

    //action bar
    ActionBar actionBar;


    @Override //invocato all'avvio 1°
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this,"ON CREATE",Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        // creo i fragments e li aggiungo alla lista
        fragments.add(Fragment.instantiate(this, Page1Fragment.class.getName()));
        fragments.add(Fragment.instantiate(this, Page2Fragment.class.getName()));
        fragments.add(Fragment.instantiate(this, Page3Fragment.class.getName()));

        // creo l'adapter e lo agg al viewPager
        this.mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        viewPager = (ViewPager) super.findViewById(R.id.pager);
        viewPager.setAdapter(this.mPagerAdapter);

        //SimpleOnPageChangeListener in ascolto al cambio pagina cambia anche la selezione dei tab
        viewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });

        actionBar=getActionBar();
        // Modalita actionBar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //Tablistener in ascolto al cambio tab
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

            }
            @Override
            public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

            }
        };
        // Aggiungo 3 tab, specifico il testo, e il Listener
        //TODO usare strings.xml
        String[] nomiTab={"Foto","WhatsApp","Altro"};
        for (int i = 0; i < nomiTab.length; i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(nomiTab[i])
                            .setTabListener(tabListener));
        }

/*
        Intent intent = this.getIntent();
        this.listCamera =intent.getParcelableArrayListExtra("listaCamera");
        this.listWhatApp=intent.getParcelableArrayListExtra("listaWhatApp");
*/

    }




    @Override //invocato all'avvio 2° -//invocato alla ripresa dalla sospensione 2°
    protected void onStart() {
        Toast.makeText(this,"ON START",Toast.LENGTH_SHORT).show();
        super.onStart();
      //  if (this.listMedia==null || this.listMedia.isEmpty()) {
      //      ContentProviderScanner contentProviderScanner = new ContentProviderScanner(this);
       //     this.listMedia = contentProviderScanner.getListMedia(Album.All,true);
      //      }

        /*Page1Fragment.listMedia= listCamera;
        Page1Fragment.placeholder=
        Page2Fragment.listMedia= listWhatApp;
        Page2Fragment.placeholder= BitmapFactory.decodeResource(this.getResources(), R.drawable.placeholder);
*/
    }

    @Override //invocato all'avvio 3° -//invocato alla ripresa dalla sospensione 3°
    protected void onResume() {
        super.onResume();
        Toast.makeText(this,"ON RESUME",Toast.LENGTH_SHORT).show();
    }

    @Override //invocato alla sospensione 1°//invocato alla chiusura 1°
    protected void onPause() {
        super.onPause();
        Toast.makeText(this,"ON PAUSE",Toast.LENGTH_SHORT).show();
    }

    @Override //invocato alla sospensione 2° //invocato alla chiusura 2°
    protected void onStop() {
        super.onStop();
        Toast.makeText(this,"ON STOP",Toast.LENGTH_SHORT).show();
    }

    @Override //invocato alla chiusura 3°
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"ON DESTROY",Toast.LENGTH_SHORT).show();
    }



    @Override //invocato alla ripresa dalla sospensione 1°
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this,"ON RESTART",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String file= listCamera.get(position).getPath();
        Toast.makeText(this,file.toString(),Toast.LENGTH_SHORT).show();
    }

    /* Permessi runTime android 6 marshmallow da libreria esterna
    @Override
    public void onRequestPermissionGranted(String[] permission, int[] grantResults) {
        Toast.makeText(this,"Applicazione può continuare",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionDenied(String[] permission, int[] grantResults) {
        Toast.makeText(this,"Applicazione verrà terminata",Toast.LENGTH_SHORT).show();
    }*/

//-----------------------------------MENU E ALTRO----------------------------------------------------------------------
    //Options optionsmenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu,menu);
        return true;
    }

    @Override //optionsmenu gestione click
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch(id)
        {
            case 0:

                break;
            case 1:

        }
        return false;
    }

    //menu context
    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextmenu,menu);
    }*/

}
