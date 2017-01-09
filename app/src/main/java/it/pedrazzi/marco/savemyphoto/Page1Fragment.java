package it.pedrazzi.marco.savemyphoto;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView; //headers gridview

import java.util.ArrayList;

public class Page1Fragment extends Fragment {

    public static StickyGridHeadersGridView gridHeadersGridView;
    public static ArrayList<FileMedia> listMedia;
    public static Bitmap placeholder;
    public ContentProviderScanner contentProviderScanner;

    // activity listener interface
    private OnPageListener pageListener;
    public interface OnPageListener {
        public void onPage1(String s);
    }

    // onAttach : set activity listener
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

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>Any restored child fragments will be created before the base
     * <code>Fragment.onCreate</code> method returns.</p>
     *
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
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
        this.gridHeadersGridView=(StickyGridHeadersGridView)getView().findViewById(R.id.gridviewWithHeaders);
        this.gridHeadersGridView.setAdapter(new ImageAdapter(getContext(), listMedia, placeholder));
        //gridHeadersGridView.setOnItemClickListener(this);
    }
}
