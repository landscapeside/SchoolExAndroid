package com.utils.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by 1 on 2016/4/12.
 */
public class ImageHelper {
    public static Bitmap scaleBitmap(Bitmap src,float scale){
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static Bitmap scaleBitmap(Bitmap src,float widthScale,float heightScale){
        Matrix matrix = new Matrix();
        matrix.postScale(widthScale, heightScale);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static Drawable scaleBitmapToDrawable(Bitmap src, float scale) {
        return new BitmapDrawable(scaleBitmap(src, scale));
    }

    public static Drawable scaleDrawable(Resources sResouces,Drawable drawable, float scaleRate) {
        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return null;
        }
        Bitmap oldBmp = ((BitmapDrawable) drawable).getBitmap();
        if (oldBmp == null || oldBmp.isRecycled()) {
            return drawable;
        }

        Bitmap newBmp = Bitmap.createScaledBitmap(oldBmp,
                (int) (drawable.getMinimumWidth() * scaleRate),
                (int) (drawable.getMinimumHeight() * scaleRate), true);

        return new BitmapDrawable(sResouces, newBmp);
    }
}
