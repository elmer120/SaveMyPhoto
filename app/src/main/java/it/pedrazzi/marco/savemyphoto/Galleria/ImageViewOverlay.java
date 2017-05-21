package it.pedrazzi.marco.savemyphoto.Galleria;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import it.pedrazzi.marco.savemyphoto.R;

/**
 * Created by Elmer on 15/05/2017.
 */

public class ImageViewOverlay extends android.support.v7.widget.AppCompatImageView {

    Context ctx;
    boolean suServer;
    boolean suDipositivo;
    Bitmap iconaServer;
    Bitmap iconaDispositivo;

    public ImageViewOverlay(Context context,boolean suServer,boolean suDispositivo)
    {
        super(context);
        this.ctx=context;
        this.suServer=suServer;
        this.suDipositivo=suDispositivo;
        this.iconaServer=BitmapFactory.decodeResource(this.ctx.getResources(), R.drawable.ic_server);
        this.iconaDispositivo=BitmapFactory.decodeResource(this.ctx.getResources(), R.drawable.ic_dispositivo);
        //definisco l'altezza minima
        this.setMinimumHeight(300);
        //scalo l'img centrandola
        this.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    //Al render del imageview
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (suServer)
        {
            //rendo il bitmap mutabile
            this.iconaServer = iconaServer.copy(Bitmap.Config.ARGB_8888, true);
            canvas.drawBitmap(iconaServer, this.getPaddingLeft()+this.iconaServer.getHeight(),(this.getHeight()-this.iconaServer.getHeight()), null);

        }
        if(suDipositivo)
        {
            //rendo il bitmap mutabile
            this.iconaDispositivo = iconaDispositivo.copy(Bitmap.Config.ARGB_8888, true);
            canvas.drawBitmap(iconaDispositivo, this.getPaddingLeft(),(this.getHeight()-this.iconaDispositivo.getHeight()), null);

        }


    }

}
