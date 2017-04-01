package it.pedrazzi.marco.savemyphoto;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView; //headers gridview

import java.util.ArrayList;

public class Page1Fragment extends Fragment implements StickyGridHeadersGridView.OnItemClickListener, StickyGridHeadersGridView.OnLongClickListener{

    public static StickyGridHeadersGridView gridHeadersGridView;
    public static ArrayList<FileMedia> listMedia;
    public static Bitmap placeholder;
    public ContentProviderScanner contentProviderScanner;



   /*segnala il momento in cui il Fragment scopre l’Activity di appartenenza.
   Attenzione che a quel punto l’Activity non è stata ancora creata
   quindi si può solo conservare un riferimento ad essa ma non interagirvi
    */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // if implemented by activity, set listener
        if(activity instanceof OnPageListener) {
            pageListener = (OnPageListener) activity;
        }
    }

     /*È il momento in cui viene creato il layout del Fragment.
    Solitamente qui si fa uso del LayoutInflater*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("Fragment1","createView");
        // fragment not when container null
        if (container == null) {
            return null;
        }
        // inflate view from layout
        View view = (LinearLayout)inflater.inflate(R.layout.fragment1,container,false);

        return view;
    }

    /*segnala che la creazione dell’Activity è stata completata,
    vi si può interagire in tutto e per tutto.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Fragment1","onActivityCreated");

    }

   /*
     *Chiamato tra on Attach e onStart il fragment si sta creando
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contentProviderScanner=new ContentProviderScanner(this.getContext());
        this.listMedia=contentProviderScanner.getListMedia(Album.Camera,true);
        this.placeholder= BitmapFactory.decodeResource(this.getResources(), R.drawable.placeholder);
    }

    /**
    Chiamato quando il fragment è creato e visibile all'utente
     */
    @Override
    public void onStart() {
        super.onStart();
        this.gridHeadersGridView=(StickyGridHeadersGridView)getView().findViewById(R.id.gridviewWithHeaders);
        this.gridHeadersGridView.setAdapter(new ImageAdapter(getContext(), listMedia, placeholder));
        gridHeadersGridView.setOnItemClickListener(this);

    }

    //-----------------------------------EVENTI----------------------------------------------------------------


    // riferimento all'activity
    private OnPageListener pageListener;

    //click su un elemento del listview
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        //ANIMAZIONE
        //Animation anim=AnimationUtils.loadAnimation(getActivity().getBaseContext(),R.anim.selected);
        //view.startAnimation(anim);

        inviaDatiActivity("nome:"+this.listMedia.get(i).getNome());//stampo nome file
    }


    //click lungo su un elemento del listview
    @Override
    public boolean onLongClick(View view) {
        //inviaDatiActivity("selezionato",this.listMedia.get(view.).getNome());
        return true;
    }

    //Interfaccia applicata all'activity
    public interface OnPageListener {
        public void onPage1(String s);
    }

    // Metodo per inviare gli eventi all'activity (es. click) questo
    // perchè è sconsigliato gestire gli eventi localmente nel fragment
    private void inviaDatiActivity(String s) {
        pageListener.onPage1(s);
    }

}
