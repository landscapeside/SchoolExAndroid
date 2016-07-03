package com.utils.behavior;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/5/17.
 */
public class AppFileUtils {

    static String rootPath = "";
    static String downFolder = "download";
    static String picFolder = "pics";

    public static void init(String rootFolder) {
        File dir = null,downDir=null,picDir = null;
        dir = new File(Environment.getExternalStorageDirectory(), rootFolder);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        rootPath = dir.getAbsolutePath();
        downDir = new File(getDownPath());
        if (!downDir.exists()) {
            downDir.mkdirs();
        }
        picDir = new File(getPicsPath());
        if (!picDir.exists()) {
            picDir.mkdirs();
        }
    }

    public static String getRootPath() {
        return rootPath;
    }

    public static String getDownPath() {
        return rootPath + File.separator + downFolder;
    }

    public static String getPicsPath() {
        return rootPath + File.separator + picFolder;
    }

    public static String mergePath(String... paths) {
        String result = "";
        for (String path : paths) {
            result += File.separator + path;
        }
        return result;
    }

    public static String makeIfNotExist(String path) {
        File dir = null;
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    public static boolean copyAssets(Context context, String path, String release) {
        try {
            String str[] = context.getAssets().list(path);
            if (str.length > 0) {//如果是目录
                File file = new File(path);
                file.mkdirs();
                for (String string : str) {
                    path = path + "/" + string;
                    release = release + "/" +string;
                    // textView.setText(textView.getText()+"\t"+path+"\t");
                    copyAssets(context, path,release);
                    path = path.substring(0, path.lastIndexOf('/'));
                    release = release.substring(0, release.lastIndexOf('/'));
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(path);
                FileOutputStream fos = new FileOutputStream(new File(release));
                byte[] buffer = new byte[1024];
                int count = 0;
                while (true) {
                    count++;
                    int len = is.read(buffer);
                    if (len == -1) {
                        break;
                    }
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
