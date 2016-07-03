package com.landscape.schoolexandroid.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.landscape.schoolexandroid.R;


/**
 * Created by zhusx on 2015/9/24.
 */
public class FullBaseDialogDialog extends Dialog {

    protected final Point p = new Point();
    public static final int ANIM_BOTTOM = R.style.AnimBottom;

    public FullBaseDialogDialog(Context context) {
        super(context, R.style.FULL_DIALOG);
        init();
    }

    protected void init() {
        Window window = getWindow();
        window.setWindowAnimations(ANIM_BOTTOM);
        getSize(getWindow().getWindowManager(), p);
        window.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL);
        window.setLayout(p.x, -2);
    }

    public static void getSize(WindowManager wm, Point p) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            wm.getDefaultDisplay().getSize(p);
        } else {
            p.x = wm.getDefaultDisplay().getWidth();
            p.y = wm.getDefaultDisplay().getHeight();
        }
    }

}
