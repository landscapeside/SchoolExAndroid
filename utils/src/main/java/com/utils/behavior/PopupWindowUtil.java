package com.utils.behavior;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.utils.R;
import com.utils.system.ScreenParam;

import java.util.Collections;

/**
 * Created by 1 on 2016/7/12.
 */
public class PopupWindowUtil {

    static PopupWindow popupWindow;

    private static final int WINDOW_WIDTH = 150;
    private static final int WINDOW_HEIGHT = 200;

    public static void showPopupWindow(Context context,View parent,View contentView) {
        dismiss();
        popupWindow = new PopupWindow(
                contentView,
                ScreenParam.dp2px(context,WINDOW_WIDTH),
                ScreenParam.dp2px(context,WINDOW_HEIGHT), true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int xPos = -popupWindow.getWidth() / 2
                + parent.getWidth() / 2;
        popupWindow.showAsDropDown(parent, 0, 4);
    }

    public static void dismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

}
