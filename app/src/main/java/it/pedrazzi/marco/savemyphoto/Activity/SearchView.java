package it.pedrazzi.marco.savemyphoto.Activity;
import android.Manifest;
import android.app.ActionBar;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.widget.Toast;
import com.lalosoft.easypermission.RegisterPermission; //permessi x android >V5
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView; //headers gridview
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import it.pedrazzi.marco.savemyphoto.ConnectionCheckReceiver;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;
import it.pedrazzi.marco.savemyphoto.Fragments.Page1Fragment;
import it.pedrazzi.marco.savemyphoto.Fragments.Page2Fragment;
import it.pedrazzi.marco.savemyphoto.Fragments.Page3Fragment;
import it.pedrazzi.marco.savemyphoto.Fragments.PagerAdapter;
import it.pedrazzi.marco.savemyphoto.R;

@RegisterPermission(permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})

public class SearchView extends FragmentActivity implements ActivityCompat.OnRequestPermissionsResultCallback,Page1Fragment.OnPageListener {


    private ArrayList<FileMedia> listCamera;
    private ArrayList<FileMedia> listWhatApp;
    private StickyGridHeadersGridView gridHeadersGridView;
    private IntentFilter filter;
    private ConnectionCheckReceiver connectionCheckReceiver;

    String nomeUtente;
    Integer idDispositivo;

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
        Toast.makeText(this, "ON CREATE", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        //recupero dati da activity precedente
        this.nomeUtente = getIntent().getExtras().getString("nomeUtente");
        this.idDispositivo = getIntent().getExtras().getInt("idDispositivo");


        // creo i fragments e li aggiungo alla lista
        Fragment fragmentPage1 = Fragment.instantiate(this, Page1Fragment.class.getName());
        //passo dati al fragment1
        fragmentPage1.setArguments(getIntent().getExtras());
        Fragment fragmentPage2 = Fragment.instantiate(this, Page2Fragment.class.getName());
        Fragment fragmentPage3 = Fragment.instantiate(this, Page3Fragment.class.getName());
        fragments.add(fragmentPage1);
        fragments.add(fragmentPage2);
        fragments.add(fragmentPage3);

        // creo l'adapter e lo aggiungo al viewPager
        this.mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        viewPager = (ViewPager) super.findViewById(R.id.pager);
        viewPager.setAdapter(this.mPagerAdapter);

        //SimpleOnPageChangeListener in ascolto al cambio pagina cambia anche la selezione dei tab
        viewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });

        actionBar = getActionBar();
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
        String[] nomiTab = getResources().getStringArray(R.array.tab);
        for (int i = 0; i < nomiTab.length; i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(nomiTab[i])
                            .setTabListener(tabListener));
        }

    }


    //listener degli eventi dei fragment
    @Override
    public void onPage1(String s)
    {

        Toast.makeText(this, s + "", Toast.LENGTH_SHORT).show();

    }

    @Override //invocato all'avvio 2° -//invocato alla ripresa dalla sospensione 2°
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "ON START", Toast.LENGTH_SHORT).show();


        //istanzio il broadcast receiver
        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        connectionCheckReceiver = new ConnectionCheckReceiver();
    }

    @Override //invocato all'avvio 3° -//invocato alla ripresa dalla sospensione 3°
    protected void onResume() {
        super.onResume();


        //Quando ritorna attiva registro il broadcast receiver
        registerReceiver(connectionCheckReceiver, filter);

        //Toast.makeText(this, "ON RESUME", Toast.LENGTH_SHORT).show();
    }

    @Override //invocato alla sospensione 1°//invocato alla chiusura 1°
    protected void onPause() {
        super.onPause();
        //quando l'app è sospesa disattivo il broadcast receiver
        unregisterReceiver(connectionCheckReceiver);


        //Toast.makeText(this, "ON PAUSE", Toast.LENGTH_SHORT).show();
    }

    @Override //invocato alla sospensione 2° //invocato alla chiusura 2°
    protected void onStop() {
        super.onStop();
        //Toast.makeText(this, "ON STOP", Toast.LENGTH_SHORT).show();
    }

    @Override //invocato alla chiusura 3°
    protected void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "ON DESTROY", Toast.LENGTH_SHORT).show();
    }


    @Override //invocato alla ripresa dalla sospensione 1°
    protected void onRestart() {
        super.onRestart();
        //Toast.makeText(this, "ON RESTART", Toast.LENGTH_SHORT).show();
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

}
