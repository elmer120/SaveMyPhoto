package it.pedrazzi.marco.savemyphoto;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.Checkable;

/**
 * Created by Elmer on 05/04/2017.
 */

public class CheckableLayout extends FrameLayout implements Checkable {

    private boolean mChecked;

    public CheckableLayout(Context context) {
        super(context);
    }

    @SuppressWarnings("deprecation")
    public void setChecked(boolean checked) {
        mChecked = checked;
        setBackgroundDrawable(checked ? getResources().getDrawable(
                android.R.drawable.btn_minus) : null);
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void toggle() {
        setChecked(!mChecked);
    }

}