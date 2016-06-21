package com.utils.system;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by 1 on 2016/3/7.
 */
public class ScreenParam {

    public static int screenHeight = 0;
    public static int screenWidth = 0;

    public static float density = 0;

    public static void init(Context context){
        Resources res = context.getResources();
        screenHeight = res.getDisplayMetrics().heightPixels;
        screenWidth = res.getDisplayMetrics().widthPixels;
        density = res.getDisplayMetrics().density;
    }

}
