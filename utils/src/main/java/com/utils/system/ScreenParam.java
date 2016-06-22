package com.utils.system;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

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

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal)
    {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
