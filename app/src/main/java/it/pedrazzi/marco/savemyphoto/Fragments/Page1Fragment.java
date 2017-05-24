package it.pedrazzi.marco.savemyphoto.Fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView; //headers gridview

import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.Http.HttpDownloadAsync;
import it.pedrazzi.marco.savemyphoto.Media.Album;
import it.pedrazzi.marco.savemyphoto.Media.ContentProviderScanner;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;
import it.pedrazzi.marco.savemyphoto.Galleria.ImageAdapter;
import it.pedrazzi.marco.savemyphoto.Http.HttpUploadAsync;
import it.pedrazzi.marco.savemyphoto.R;
import it.pedrazzi.marco.savemyphoto.WebService.Autogenerate.RRCArrayOfString;
import it.pedrazzi.marco.savemyphoto.WebService.Autogenerate.RRCWSsaveMyPhotoSoap;
import it.pedrazzi.marco.savemyphoto.WebService.GetMediaOnServer;

public class Page1Fragment extends Fragment implements StickyGridHeadersGridView.OnItemClickListener, StickyGridHeadersGridView.OnItemLongClickListener,StickyGridHeadersGridView.MultiChoiceModeListener{

    public static GridView gridHeadersGridView;
    public static ArrayList<FileMedia> listMedia;
    public static Bitmap placeholder;
    public ContentProviderScanner contentProviderScanner;
    ImageAdapter ImageAdapter;
    public static String nomeUtente;
    private int idDispositivo;



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
        //recupero i dati passati dall'activity
        this.nomeUtente = this.getArguments().getString("nomeUtente");
        this.idDispositivo = this.getArguments().getInt("idDispositivo");

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
        this.gridHeadersGridView=(GridView)getView().findViewById(R.id.gridviewWithHeaders);
        ImageAdapter =new ImageAdapter(getContext(), listMedia, placeholder);
        this.gridHeadersGridView.setAdapter(ImageAdapter);
        gridHeadersGridView.setOnItemClickListener(this);
        gridHeadersGridView.setMultiChoiceModeListener(this);
        gridHeadersGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridHeadersGridView.setOnItemLongClickListener(this);

    }

    //-----------------------------------EVENTI TOUCH----------------------------------------------------------------


    // riferimento all'activity
    private OnPageListener pageListener;

    //click su un elemento del listview
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FileMedia media=this.listMedia.get(i);



        Toast.makeText(         this.getContext(),
                                "Nome: "+media.getNome()+
                                "\n MimeType: "+media.getMimeType()+
                                "\n Dimensione: "+media.getDimensione()+
                                "\n H: "+media.getAltezza()+
                                "\n L: "+media.getLarghezza()+
                                "\n Path: " + media.getPath()+
                                "\n Orientamento: "+media.getOrientamento()+
                                "\n Selezionata: " + media.getSelezionata()+
                                "\n Su server: " + media.getSuServer()+
                                "\n Su dispositivo: " + media.getSuDispositivo(),
                                Toast.LENGTH_SHORT).show();
        Log.i("Evento","onItemClick");
    }


    //click lungo su un elemento del listview
    //callback setOnItemLongClick
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i("Evento","onLongClick");

            return false;
        }


    //---------------------------------ACTION MODE INIZIO --------------------------------------------------------

    //callback multichoice
    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean state) {
        FileMedia media=this.listMedia.get(i);
        if(state)
        {
            media.setSelezionata(true);
        }
        else
        {
            media.setSelezionata(false);
        }


        //Imposto titolo dinamicamente
        String titolo="";
        int countSelezione=this.gridHeadersGridView.getCheckedItemCount();
        if(countSelezione==1){titolo=" Selezionata";} else {titolo=" Selezionate";}
        actionMode.setTitle(countSelezione+titolo);

        //notifico all'adapter il cambio della base dati
        ImageAdapter.notifyDataSetChanged();
        Log.i("Evento","onItemCheckedStateChanged");
    }

    //callback classe Action mode
    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        //popolo il menu contestuale
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.contexmenumultiselection, menu);
        Log.i("Evento","onCreateActionMode");
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        Log.i("Evento","onPrepareActionMode");
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        //ricerco la voce del menu selezionata
        switch (menuItem.getItemId())
        {
            case R.id.Elimina:

                GetMediaOnServer getMediaOnServer=new GetMediaOnServer(this.getContext(),this.nomeUtente,this.idDispositivo,this.listMedia,this.ImageAdapter);
                getMediaOnServer.execute();



                Log.i("ActionMode","Click su elimina");
                actionMode.finish();
                break;

            case R.id.backup:
                Log.i("ActionMode","Click su backup");

                //recupero la lista di elementi selezionati e li invio

                final ArrayList<FileMedia> elementiSelezionati=new ArrayList<FileMedia>();
                Integer elementiGiaSuServer=0;
                for (FileMedia media:listMedia)
                {
                    //se l'elemento è selezionato
                    if(media.getSelezionata())
                    {
                        //e il media non è su server
                        if (!media.getSuServer())
                        {
                            //aggiungo il media alla lista da inviare
                            elementiSelezionati.add(media);

                        }
                        else //se è già su server
                        {
                            elementiGiaSuServer++;
                        }
                    }

                }
                //informo l'utente della quantita di elementi già su server
                if(elementiGiaSuServer>0)
                {
                    //TODO toast non funziona
                    Toast.makeText(this.getContext(),"Impossibile eseguire l'upload di "+elementiGiaSuServer.toString()+"media \n risultano già presenti su cloud!",Toast.LENGTH_SHORT);
                }
                //invio
                HttpUploadAsync httpMultipartAsync=new HttpUploadAsync(this.getContext(),this.nomeUtente,this.idDispositivo);
                httpMultipartAsync.execute(elementiSelezionati);
                actionMode.finish();
                break;
        }
        Log.i("Evento","onActionItemClicked");
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        //All'uscita dall'action mode ripristino gli elementi della lista come non selezionati
        for (FileMedia fileMedia:listMedia)
        {fileMedia.setSelezionata(false);}

        Log.i("Evento","onDestroyActionMode");
    }

//---------------------------------ACTION MODE FINE --------------------------------------------------------



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
