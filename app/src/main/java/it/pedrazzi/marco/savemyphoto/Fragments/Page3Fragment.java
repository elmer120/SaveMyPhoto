package it.pedrazzi.marco.savemyphoto.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.pedrazzi.marco.savemyphoto.R;

/**
 * Created by elmer on 05/01/17.
 */
public class Page3Fragment extends Fragment {


public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // fragment not when container null
        if (container == null) {
        return null;
        }
        // inflate view from layout
        View view = (LinearLayout)inflater.inflate(R.layout.fragment3,container,false);

        return view;
        }

    // set text helper function
    public void setText(String item) {
            TextView view = (TextView) getView().findViewById(R.id.tvText3);
            view.setText(item);
            }

}
