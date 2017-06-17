package it.pedrazzi.marco.savemyphoto.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import it.pedrazzi.marco.savemyphoto.Activity.PresentazioneActivity;
import it.pedrazzi.marco.savemyphoto.Media.Album;
import it.pedrazzi.marco.savemyphoto.Media.ContentProviderScanner;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;
import it.pedrazzi.marco.savemyphoto.Galleria.ImageAdapter;
import it.pedrazzi.marco.savemyphoto.Http.HttpUploadAsync;
import it.pedrazzi.marco.savemyphoto.R;
import it.pedrazzi.marco.savemyphoto.WebService.AggiornaMediaAsync;
import it.pedrazzi.marco.savemyphoto.WebService.GetMediaOnServerAsync;

public class Page1Fragment extends Fragment implements GridView.OnItemClickListener, AbsListView.MultiChoiceModeListener{

    public static GridView gridView;
    public static ArrayList<FileMedia> listMedia;
    public static Bitmap placeholder;
    public ContentProviderScanner contentProviderScanner;
    public ImageAdapter ImageAdapter;
    public static String nomeUtente;
    private int idDispositivo;
    boolean pulsanteBackup;
    boolean eliminazioneDefinitiva;


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

        //visualizzo l'options menu
        setHasOptionsMenu(true);


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

        //creo la base dati per la gridview
        this.contentProviderScanner=new ContentProviderScanner(this.getContext());
        this.listMedia=contentProviderScanner.getListMedia(Album.Camera,true);
        this.placeholder= BitmapFactory.decodeResource(this.getResources(), R.drawable.placeholder);
        Log.i(this.getClass().getSimpleName(),"OnCreate");
    }

    /**
    Chiamato quando il fragment è creato e visibile all'utente
     */
    @Override
    public void onStart()
    {
        super.onStart();
        //recupero la gridview
        this.gridView =(GridView)getView().findViewById(R.id.gridviewFragment1);
        if(this.ImageAdapter==null)
        {
            this.ImageAdapter = new ImageAdapter(getContext(), listMedia, placeholder);
        }
        this.gridView.setAdapter(ImageAdapter);

        //registro callbacks
        //evento touch
        gridView.setOnItemClickListener(this);
        //action mode
        gridView.setMultiChoiceModeListener(this);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);

        //controllo se ci sono media su server
        GetMediaOnServerAsync getMediaOnServer=new GetMediaOnServerAsync(this.getContext(),this.nomeUtente,this.idDispositivo,this.listMedia,this.ImageAdapter);
        getMediaOnServer.execute();
        Log.i(this.getClass().getSimpleName(),"OnStart");
    }



    //-----------------------------------EVENTI TOUCH----------------------------------------------------------------


    // riferimento all'activity
    private OnPageListener pageListener;

    //click su un elemento del listview
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FileMedia media=this.listMedia.get(i);
        Bundle bundle = new Bundle();
        bundle.putInt("position",i);
        bundle.putString("nomeUtente",nomeUtente);
        bundle.putInt("idDispositivo",idDispositivo);
        bundle.putParcelableArrayList("listMedia",this.listMedia);
        Intent intent=new Intent(this.getContext(), PresentazioneActivity.class);
        intent.putExtras(bundle);
        this.getActivity().startActivity(intent);
    }
    //---------------------------------ACTION MODE INIZIO --------------------------------------------------------

    //callback multichoice
    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean state) {
        FileMedia media=this.listMedia.get(i);

        //seleziono o no il media
        media.setSelezionata(state);

        //Imposto titolo dinamicamente
        String titolo="";
        int countSelezione=this.gridView.getCheckedItemCount();
        if(countSelezione==1){titolo=" Selezionata";} else {titolo=" Selezionate";}
        actionMode.setTitle(countSelezione+titolo);

        //notifico all'adapter il cambio della base dati cosi evidenzia l'immagine
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
        this.pulsanteBackup=false;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        //mostro o no il tasto di backup
        MenuItem backup=menu.findItem(R.id.backup);
        backup.setVisible(this.pulsanteBackup);
        Log.i("Evento","onPrepareActionMode");
        return false;
    }

    @Override
    public boolean onActionItemClicked(final ActionMode actionMode, MenuItem menuItem) {

        //se almeno un media non è sul server invalido il l'action mode, che cosi passa ad onPrepareActionMode
        pulsanteBackup=false;
        for (FileMedia media:listMedia)
        {
            //se media è selezionato e solo sul dispositivo
            if (media.getSelezionata())
            {
                if(!media.getSuServer())
                {
                    //mostro il pulsante di backup
                    this.pulsanteBackup=true;

                    Log.i("ActionMode","invalidata");
                }
                actionMode.invalidate();
            }
        }

        //ricerco la voce del menu selezionata
        switch (menuItem.getItemId())
        {
            case R.id.elimina:
                Log.i("ActionMode","Click su elimina");
                final ArrayList<FileMedia> mediaDaEliminareDef=new ArrayList<FileMedia>();
                final ArrayList<FileMedia> mediaDaEliminareConBackup=new ArrayList<FileMedia>();

                for (FileMedia media:listMedia)
                {
                    //se l'elemento è selezionato
                    if(media.getSelezionata())
                    {
                        //se non è su server
                        if(!media.getSuServer())
                        {
                            mediaDaEliminareDef.add(media);
                        }
                        else if(media.getSuServer() && media.getSuDispositivo())
                        {
                            mediaDaEliminareConBackup.add(media);
                        }

                    }

                }
                //averto l'utente che la cancellazione è definitiva
                if(mediaDaEliminareDef.size()>0)
                {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(this.getContext(), R.style.MyAlertDialog);
                    builder.setTitle("Eliminazione definitiva")
                            .setMessage("Uno o più media non hanno nessun backup, \nl'eliminazione sarà definitiva, procedere?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    EliminaMedia(mediaDaEliminareDef,mediaDaEliminareConBackup,true);
                                    actionMode.finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    actionMode.finish();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    //ritornando false blocco l'action mode dicendogli che non ha ancora gestito l'evento
                    return false;
                }

                if(mediaDaEliminareConBackup.size()>0)
                {
                    EliminaMedia(mediaDaEliminareDef,mediaDaEliminareConBackup,false);
                }
                actionMode.finish();
                break;

            case R.id.backup:
                Log.i("ActionMode","Click su backup");

                //recupero la lista di elementi selezionati e li invio

                final ArrayList<FileMedia> mediaDaInviare=new ArrayList<FileMedia>();

                for (FileMedia media:listMedia)
                {
                    //se l'elemento è selezionato
                    if(media.getSelezionata())
                    {
                        if(!media.getSuServer())
                        {    //aggiungo il media alla lista da inviare
                            mediaDaInviare.add(media);
                        }
                    }

                }

                //invio
                ProgressBar progressBar=(ProgressBar)this.getActivity().findViewById(R.id.progressBar);
                HttpUploadAsync httpMultipartAsync=new HttpUploadAsync(this.getContext(),this.nomeUtente,this.idDispositivo,this.ImageAdapter,progressBar);
                httpMultipartAsync.execute(mediaDaInviare);
                actionMode.finish();
                break;
        }
        Log.i("Evento","onActionItemClicked");

        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        //All'uscita dall'action mode ripristino gli elementi della lista come non selezionati
        //imposto la var pulsante backup a false
        for (FileMedia fileMedia:listMedia)
        {fileMedia.setSelezionata(false);}
        this.pulsanteBackup=false;
        this.ImageAdapter.notifyDataSetChanged();
        Log.i("Evento","onDestroyActionMode");
    }

//---------------------------------ACTION MODE FINE --------------------------------------------------------

    //-----------------------------------MENU E ALTRO----------------------------------------------------------------------
    //Options optionsmenu
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.optionsmenu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override //optionsmenu gestione click
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        switch(id)
        {
            //TODO sistemare
            case R.id.refresh:
                GetMediaOnServerAsync getMediaOnServer=new GetMediaOnServerAsync(this.getContext(),this.nomeUtente,this.idDispositivo,this.listMedia,this.ImageAdapter);
                getMediaOnServer.execute();
                break;
            case R.id.check:
                Intent intent=new Intent(this.getContext(), PresentazioneActivity.class);
                this.getActivity().startActivity(intent);
                break;
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



    //Interfaccia applicata all'activity
    public interface OnPageListener {
        public void onPage1(String s);
    }

    // Metodo per inviare gli eventi all'activity (es. click) questo
    // perchè è sconsigliato gestire gli eventi localmente nel fragment
    private void inviaDatiActivity(String s) {
        pageListener.onPage1(s);
    }


    public void EliminaMedia(ArrayList<FileMedia> mediaDaEliminareDef,ArrayList<FileMedia> mediaDaEliminareConBackup,Boolean mediaSenzaBackup)
    {

        Toast.makeText(this.getContext(),"Eliminazione in corso...",Toast.LENGTH_SHORT).show();
        if(mediaSenzaBackup)
        {
            for (FileMedia media:mediaDaEliminareDef)
            {
                //elimino da ram
                this.listMedia.remove(media);
                //elimino da file system
                new File(media.getPath()).delete();
            }

        }

        for (FileMedia media:mediaDaEliminareConBackup)
        {
            //aggiorno il media nel db remoto mettendo dispositivo a 0
            AggiornaMediaAsync aggiornaMediaAsync=new AggiornaMediaAsync(this.getContext(),media.getPath(),this.ImageAdapter);
            aggiornaMediaAsync.execute(new String[]{""+this.idDispositivo,media.getNome()});
            //elimino da ram
            this.listMedia.remove(media);
            //this.ImageAdapter.getCachePhoto().Clear();

        }
        Toast.makeText(this.getContext(),"Eliminazione completata",Toast.LENGTH_SHORT).show();
        //controllo se ci sono media su server
        GetMediaOnServerAsync getMediaOnServer=new GetMediaOnServerAsync(this.getContext(),this.nomeUtente,this.idDispositivo,this.listMedia,this.ImageAdapter);
        getMediaOnServer.execute();



        //this.ImageAdapter.notifyDataSetChanged();
        //this.ImageAdapter.getCachePhoto().Clear();
    }
}
