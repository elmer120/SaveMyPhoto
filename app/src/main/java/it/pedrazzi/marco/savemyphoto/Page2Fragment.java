package it.pedrazzi.marco.savemyphoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.ArrayList;

import static it.pedrazzi.marco.savemyphoto.Page1Fragment.listMedia;

/**
 * Created by elmer on 05/01/17.
 */

public class Page2Fragment extends Fragment {

    public static StickyGridHeadersGridView gridHeadersGridView;
    public static ArrayList<FileMedia> listMedia;
    public static Bitmap placeholder;
    public ContentProviderScanner contentProviderScanner;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // fragment not when container null
        if (container == null) {
            return null;
        }
        // inflate view from layout
        View view = (LinearLayout)inflater.inflate(R.layout.fragment2,container,false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contentProviderScanner=new ContentProviderScanner(this.getContext());
        this.listMedia=contentProviderScanner.getListMedia(Album.WhatsApp,true);
        this.placeholder= BitmapFactory.decodeResource(this.getResources(), R.drawable.placeholder);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.gridHeadersGridView=(StickyGridHeadersGridView)getView().findViewById(R.id.gridviewWithHeaders);
        this.gridHeadersGridView.setAdapter(new ImageAdapter(getContext(), listMedia, placeholder));
    }
}
