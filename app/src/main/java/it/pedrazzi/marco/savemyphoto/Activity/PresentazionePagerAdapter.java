package it.pedrazzi.marco.savemyphoto.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import it.pedrazzi.marco.savemyphoto.Http.HttpDownloadAsync;
import it.pedrazzi.marco.savemyphoto.Media.FileMedia;
import it.pedrazzi.marco.savemyphoto.R;

public class PresentazionePagerAdapter extends PagerAdapter {

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    private Context ctx;

    public void setLayoutInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    LayoutInflater layoutInflater;

    public ArrayList<FileMedia> getListMedia() {
        return listMedia;
    }

    public void setListMedia(ArrayList<FileMedia> listMedia) {
        this.listMedia = listMedia;
    }

    ArrayList<FileMedia> listMedia;

    public PresentazionePagerAdapter(Context ctx,ArrayList<FileMedia> listmedia) {
        this.setCtx(ctx);
        this.setLayoutInflater((LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        this.setListMedia(listmedia);
    }

    @Override
    public int getCount()
    {
        return this.getListMedia().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = this.layoutInflater.inflate(R.layout.elemento_presentazione, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        FileMedia media=this.listMedia.get(position);
        BitmapFactory.Options opzioniBitmap = new BitmapFactory.Options();
        opzioniBitmap.inSampleSize=4;
        //codifica più leggera ogni pixel 2 byte
        opzioniBitmap.inPreferredConfig = Bitmap.Config.RGB_565;
        //controllo che tipo di media è
        switch (media.getMimeType())
        {
            case "image/jpg":
            case "image/jpeg":
            case "image/png":
                imageView.setAdjustViewBounds(true);
                imageView.setPadding(0,0,0,0);
                imageView.setImageBitmap(BitmapFactory.decodeFile(media.getPath(),opzioniBitmap));
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

}
