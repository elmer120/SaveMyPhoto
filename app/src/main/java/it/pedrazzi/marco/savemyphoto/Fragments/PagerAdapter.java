package it.pedrazzi.marco.savemyphoto.Fragments;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;



public class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    // Costruttore -- passo la lista dei fragment al pagerAdapter
    public PagerAdapter(FragmentManager supportFragmentManager, List<Fragment> fragments)
    {
        super(supportFragmentManager);
        this.fragments=fragments;
    }

    // ritorna il fragment in base alla posizione
    @Override
    public Fragment getItem(int position)
    {
        return this.fragments.get(position);
    }

    // totale fragment da scorrere
    @Override
    public int getCount()
    {
        return this.fragments.size();
    }

}