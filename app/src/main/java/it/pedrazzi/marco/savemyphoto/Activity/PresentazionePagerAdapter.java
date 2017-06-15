package it.pedrazzi.marco.savemyphoto.Activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.Http.Http;
import it.pedrazzi.marco.savemyphoto.Http.HttpDownloadAsync;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;
import it.pedrazzi.marco.savemyphoto.R;

public class PresentazionePagerAdapter extends PagerAdapter implements View.OnClickListener {

    private Context ctx;
    LayoutInflater mLayoutInflater;
    ArrayList<FileMedia> listMedia;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    int position;

    public PresentazionePagerAdapter(Context context,ArrayList<FileMedia> listMedia) {
        ctx = context;
        this.listMedia=listMedia;
        mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listMedia.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        this.setPosition(position);
        View itemView = mLayoutInflater.inflate(R.layout.image_view_presentazione, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        FileMedia media=this.listMedia.get(this.getPosition());
        //controllo che tipo di media è
        switch (media.getMimeType())
        {
            case "image/jpg":
            case "image/jpeg":
            case "image/png":


                imageView.setImageBitmap(BitmapFactory.decodeFile(media.getPath()));
                break;
            case "image/web":
                // richiesta get
                HttpDownloadAsync httpDownloadAsync=new HttpDownloadAsync(this.ctx,imageView);
                httpDownloadAsync.execute(media.getPath());
                break;
        }
        //setto i listener
        ImageButton imageButton=(ImageButton)itemView.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imageButton:
                FileMedia media=this.listMedia.get(this.getPosition());
                Toast.makeText(this.ctx,
                        "Nome: "+media.getNome()+
                                "\n DataAcqu: "+media.getDataAcquisizione()+
                                "\n MimeType: "+media.getMimeType()+
                                "\n Dimensione: "+media.getDimensione()+
                                "\n H: "+media.getAltezza()+
                                "\n L: "+media.getLarghezza()+
                                "\n Path: " + media.getPath()+
                                "\n Orientamento: "+media.getOrientamento()+
                                "\n Latitudine: " + media.getLatitudine()+
                                "\n Longitudine: " + media.getLongitudine()+
                                "\n Su server: " + media.getSuServer()+
                                "\n Su dispositivo: " + media.getSuDispositivo()
                        ,Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
