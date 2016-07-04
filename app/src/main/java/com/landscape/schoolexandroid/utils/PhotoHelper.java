package com.landscape.schoolexandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.landscape.schoolexandroid.dialog.BottomListMenuDialog;
import com.landscape.schoolexandroid.dialog.BottomPopWindow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

/**
 * Created by jiangshun on 2015/10/20.
 */
@Singleton
public class PhotoHelper {
    public static String TAG_CAMERA = "camera";
    public static String TAG_ALBUM = "album";
    public static String TAG_CANCEL = "cancel";

    public static final int CALL_SELECT_PHOTO = 99;

    public static final int SERVER_SELECT_PHOTO = 99;
    public static final int SERVER_CAPTURE_PHOTO = 100;
    public static final int SERVER_CROP_PHOTO = 102;
    public static final int REQUST_DETAIL = 101;

    static List<BottomPopWindow.PopViewData> add_pic_container = new ArrayList<>();

    static {
        add_pic_container.clear();
        add_pic_container.add(new BottomPopWindow.PopViewData("拍照", TAG_CAMERA));
        add_pic_container.add(new BottomPopWindow.PopViewData("相册中选取", TAG_ALBUM));
        add_pic_container.add(new BottomPopWindow.PopViewData("取消", TAG_CANCEL));
    }

    /**
     * 弹出操作对话框
     * @param context
     */
    public static void takePhoto(Context context) {
        takePhoto(context, 0, 0);
    }

    /**
     * 照相工具类-弹出操作对话框
     *
     * @param context 上下文
     * @param section 组索引
     * @param position 组内商品索引
     */
    public static void takePhoto(Context context, int section, int position) {
        BottomListMenuDialog dialog = new BottomListMenuDialog(context, true, "拍照", "相册中选择", "取消");
        dialog.setOnItemClick(position1 -> {
            switch (position1) {
                case 0:
                    Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent2.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    ((Activity) context).startActivityForResult(intent2, SERVER_CAPTURE_PHOTO);
                    break;
                case 1:
                    Intent intent3 = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    ((Activity) context).startActivityForResult(intent3, SERVER_SELECT_PHOTO);
                    break;
                case 2:
                    break;
            }
        });
        dialog.show();
    }

    public static void cropPhoto(Context context, Bitmap bitmap) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.putExtra("data", bitmap);
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:1
        intent.putExtra("outputX", 200);//图片输出大小
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        ((Activity) context).startActivityForResult(intent, SERVER_CROP_PHOTO);
    }

    /**
     * 将彩色图转换为纯黑白二色
     *
     * @param bmp 位图
     * @return 返回转换好的位图
     */
    public static Bitmap convertToBlackWhite(Bitmap bmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                //分离三原色
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                //转化成灰度像素
                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        //新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        //设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);

        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, 380, 460);
        return resizeBmp;
    }

    public static Bitmap compressFile(File tempFile) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getPath(),
                    options);
            // 压缩图片
//                    bitmap = compressImage(bitmap,500);

            if (bitmap != null) {
                // 保存图片
                saveFileByBitmap(bitmap,tempFile);
                return bitmap;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public static void saveFileByBitmap(Bitmap bitmap,File tempFile) throws IOException {
        if (bitmap != null) {
            // 保存图片
            FileOutputStream fos = null;
            fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        }
    }

    /**
     * 查看图片
     *
     * @param context
     * @param file
     */
    public static void showPics(Context context,File file) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        context.startActivity(intent);
    }

    /**
     * 缓存事件响应者
     */
    public static ImageView subcriberView;

    public static void loadImageIntoSubcriberView(Bitmap bitmap) {
        if (subcriberView != null) {
            subcriberView.setImageBitmap(bitmap);
        }
    }

}
