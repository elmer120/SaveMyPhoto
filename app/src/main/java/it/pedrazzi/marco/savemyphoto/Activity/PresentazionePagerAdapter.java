package it.pedrazzi.marco.savemyphoto.Activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.Http.Http;
import it.pedrazzi.marco.savemyphoto.Http.HttpDownloadAsync;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;
import it.pedrazzi.marco.savemyphoto.R;

public class PresentazionePagerAdapter extends PagerAdapter {

    private Context ctx;
    LayoutInflater mLayoutInflater;
    ArrayList<FileMedia> listMedia;
    int position;

    public PresentazionePagerAdapter(Context context,ArrayList<FileMedia> listMedia) {
        ctx = context;
        this.listMedia=listMedia;
        this.position=position;
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
        View itemView = mLayoutInflater.inflate(R.layout.image_view_presentazione, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        FileMedia media=this.listMedia.get(position);
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

}
