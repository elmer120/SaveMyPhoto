package it.pedrazzi.marco.savemyphoto.Fragments;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;


//estendo la classe che gestisce lo swipe tra i fragment
//mantiene lo stato di tutti i fragment

public class PagerAdapter extends FragmentPagerAdapter
{

    private List<Fragment> fragments;

    // Costruttore -- passo la lista dei fragment al pagerAdapter
    public PagerAdapter(FragmentManager fragmentManager, List<Fragment> fragments)
    {
        super(fragmentManager);
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

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

}