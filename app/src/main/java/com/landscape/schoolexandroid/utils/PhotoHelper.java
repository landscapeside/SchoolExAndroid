package com.landscape.schoolexandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.landscape.schoolexandroid.dialog.BottomListMenuDialog;
import com.landscape.schoolexandroid.dialog.BottomPopWindow;
import com.landscape.schoolexandroid.dialog.CropDialog;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.squareup.otto.Subscribe;
import com.tu.crop.CropHandler;
import com.tu.crop.CropHelper;
import com.tu.crop.CropParams;

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
 * Created by landscape on 2015/10/20.
 */
public class PhotoHelper implements CropHandler {
    private CropParams mCropParams = new CropParams();
    private Context context;
    private static PhotoHelper instance;
    public static String TAG_CAMERA = "camera";
    public static String TAG_ALBUM = "album";
    public static String TAG_CANCEL = "cancel";

    public static final int CALL_SELECT_PHOTO = 99;

    public static final int SERVER_CROP_PHOTO = 102;
    public static final int REQUST_DETAIL = 101;

    int requstCodeTmp = -1;

    static List<BottomPopWindow.PopViewData> add_pic_container = new ArrayList<>();

    static {
        add_pic_container.clear();
        add_pic_container.add(new BottomPopWindow.PopViewData("拍照", TAG_CAMERA));
        add_pic_container.add(new BottomPopWindow.PopViewData("相册中选取", TAG_ALBUM));
        add_pic_container.add(new BottomPopWindow.PopViewData("取消", TAG_CANCEL));
    }

    private PhotoHelper() {}

    public static PhotoHelper getInstance() {
        if (instance == null) {
            instance = new PhotoHelper();
        }
        return instance;
    }

    /**
     * 弹出操作对话框
     * @param context
     */
    public void takePhoto(Context context) {
        takePhoto(context, 0, 0);
    }

    /**
     * 照相工具类-弹出操作对话框
     *
     * @param context 上下文
     * @param section 组索引
     * @param position 组内商品索引
     */
    public void takePhoto(Context context, int section, int position) {
        mCropParams = new CropParams();
        this.context = context;
        BottomListMenuDialog dialog = new BottomListMenuDialog(context, true, "拍照", "相册中选择", "取消");
        dialog.setOnItemClick(position1 -> {
            switch (position1) {
                case 0:
                    requstCodeTmp = CropHelper.REQUEST_CAMERA;
                    mCropParams = CropParams.initCropParams();
                    mCropParams.crop = "false";
                    ((Activity)context).startActivityForResult(CropHelper.buildCaptureIntent(mCropParams.uri),
                            CropHelper.REQUEST_CAMERA);
                    break;
                case 1:
                    requstCodeTmp = CropHelper.REQUEST_GALLERY;
                    mCropParams = CropParams.initCropParams();
                    mCropParams.crop = "false";
                    ((Activity)context).startActivityForResult(CropHelper.buildGalleryIntent(), CropHelper.REQUEST_GALLERY);
                    break;
                case 2:
                    break;
            }
        });
        dialog.show();
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

//        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, 380, 460);
//        return resizeBmp;
        return newBmp;
//        return bmp;
    }

    public void saveFileByBitmap(Bitmap bitmap,File tempFile) throws IOException {
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

    public static void showPic(Context context, Intent intent) {
        ((Activity)context).startActivityForResult(intent,REQUST_DETAIL);
    }

    /**
     * 缓存事件响应者
     */
    public static ImageView subcriberView;

    public static void loadImageIntoSubcriberView(Uri uri) {
        if (subcriberView != null) {
            subcriberView.setImageURI(uri);
//            subcriberView.setImageBitmap(bitmap);
        }
    }

    /**
     * 缓存缩略图
     * */
    public static Drawable thumbDrawable;

    @Override
    public void onPhotoCropped(Uri uri) {
        if (requstCodeTmp != CropHelper.REQUEST_CROP) {
            CropDialog dialog = new CropDialog(context, "是否需要裁剪？") {
                @Override
                public void onOk() {
                    switch (requstCodeTmp) {
                        case CropHelper.REQUEST_GALLERY:
                            if (CropHelper.isKitKat()) {
                                PhotoHelper.this.getCropParams().uri = CropHelper.uriFormat(context, PhotoHelper.this.getCropParams().uri);
                            }
                            ((Activity) context).startActivityForResult(
                                    CropHelper.buildCropFromUriIntent(PhotoHelper.this),
                                    CropHelper.REQUEST_CROP);
                            requstCodeTmp = CropHelper.REQUEST_CROP;
                            break;
                        case CropHelper.REQUEST_CAMERA:
                            ((Activity) context).startActivityForResult(
                                    CropHelper.buildCropFromUriIntent(PhotoHelper.this),
                                    CropHelper.REQUEST_CROP);
                            requstCodeTmp = CropHelper.REQUEST_CROP;
                            break;
                    }
                }

                @Override
                public void onCancel() {
                    if (photoCallbk != null) {
                        photoCallbk.onPhotoCropped(uri);
                    }
                }
            };
            dialog.show();
        } else {
            if (photoCallbk != null) {
                photoCallbk.onPhotoCropped(uri);
            }
        }
    }

    @Override
    public void onCropCancel() {

    }

    @Override
    public void onCropFailed(String message) {

    }

    @Override
    public CropParams getCropParams() {
        return mCropParams;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        ((Activity)context).startActivityForResult(intent,requestCode);
    }

    private PhotoCallbk photoCallbk = null;

    public void setPhotoCallbk(PhotoCallbk callbk) {
        photoCallbk = callbk;
    }

    public interface PhotoCallbk{
        void onPhotoCropped(Uri uri);
    }
}
