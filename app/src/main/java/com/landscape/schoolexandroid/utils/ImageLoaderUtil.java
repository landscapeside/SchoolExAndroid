package com.landscape.schoolexandroid.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.widget.ImageView;

import com.landscape.schoolexandroid.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageLoaderUtil {
    public static final int DEFAULT_LIST_ICON = 3000;

    public static final int DEFAULT_LIST_CART_ICON = 4000;

    public static final int[] getWhByType(int imageType) {
        int wh[] = new int[2];
        int screenType = getWhType();
        int[] height = null;
        switch (imageType) {
            // height = ResourcesUtil.getIntArray(R.array._height);
            // wh[0] = ResourcesUtil.getScreenWidth();
            // wh[1] = height[screenType];
            // break;
            case DEFAULT_LIST_ICON:
                wh[0] = ResourcesUtil
                        .getDimensionPixelSize(R.dimen.business_cover_width);
                wh[1] = ResourcesUtil
                        .getDimensionPixelSize(R.dimen.business_cover_height);
                break;
            case DEFAULT_LIST_CART_ICON:
                wh[0] = ResourcesUtil
                        .getDimensionPixelSize(R.dimen.cart_cover_width);
                wh[1] = (ResourcesUtil
                        .getDimensionPixelSize(R.dimen.cart_cover_height) * 100);
                break;
            default:
                break;
        }
        return wh;
    }

    private final static int WIDTH_480 = 480;
    private final static int WIDTH_720 = 720;
    private final static int WIDTH_800 = 800;
    private final static int WIDTH_1080 = 1080;

    private final static int TYPE_480 = 0;
    private final static int TYPE_720 = 1;
    private final static int TYPE_800 = 2;
    private final static int TYPE_1080 = 3;

    /**
     * 返回屏幕类型
     *
     * @return 返回屏幕大小类型，从0开始
     */
    public static int getWhType() {
        int type = -1;
        int screenWidth = ResourcesUtil.getScreenWidth();
        switch (screenWidth) {
            case WIDTH_480:
                type = TYPE_480;
                break;
            case WIDTH_720:
                type = TYPE_720;
                break;
            case WIDTH_800:
                type = TYPE_800;
                break;
            case WIDTH_1080:
                type = TYPE_1080;
                break;
            default:
                type = TYPE_720;
                break;
        }
        if (screenWidth > WIDTH_1080) {
            type = TYPE_1080;
        } else if (screenWidth < WIDTH_480) {
            type = TYPE_480;
        }
        return type;
    }


    public static void displayImage(String uri, ImageView imageView) {
        displayImage(uri,imageView,null);
    }

    public static void displayImage(String uri,ImageView imageView,DisplayImageOptions options){
        displayImage(uri,imageView,null,false);
    }

    public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options, boolean clean){
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (clean){
            imageLoader.clearDiskCache();
            imageLoader.clearMemoryCache();
            imageLoader.clearDiscCache();
        }
        imageLoader.displayImage(uri, imageView, options);
    }

    /**
     *
     * */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels){
        int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8 ){
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        }else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels){
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 150 : (int) Math.min(Math.floor(w / minSideLength),Math.floor(h / minSideLength));
        if (upperBound < lowerBound){
            return lowerBound;
        }
        if((maxNumOfPixels == -1) && (minSideLength == -1)){
            return 1;
        }else if (minSideLength == -1){
            return lowerBound;
        }else {
            return upperBound;
        }
    }

    public static byte[] getBitmapBytes(Bitmap bitmap) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        // bitmap.recycle();
        baos.close();
        return bytes;
    }


    // 图片压缩处理
    public static Bitmap getThumbUploadImage(String oldPath)
            throws Exception {
        if (TextUtils.isEmpty(oldPath)) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(oldPath, options);
        // int height = options.outHeight;
        int width = options.outWidth;
        // int reqHeight = 0;
        int reqWidth = 800;// bitmapMaxWidth;
        Matrix matrix = new Matrix();
        float scale = (float) reqWidth / (float) width;
        scale = (scale > 1.0 ? 1 : scale);
        matrix.postScale(scale, scale); // 长和宽放大缩小的比例
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(oldPath, options);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        Bitmap bbb = compressImage(resizeBmp);
        return bbb;
    }

    private static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 1000) { // 循环判断如果压缩后图片是否大于1000kb,大于继续压缩
            options -= 10;// 每次都减少10
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        try {
            isBm.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
