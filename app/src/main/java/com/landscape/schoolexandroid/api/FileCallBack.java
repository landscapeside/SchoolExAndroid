package com.landscape.schoolexandroid.api;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.utils.behavior.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by landscape on 2016/7/9.
 */
public abstract class FileCallBack extends CallContext implements Callback {

    protected File saveFile = null;

    protected Call okCall = null;

    public FileCallBack(Context context,File file) {
        super(context);
        saveFile = file;
    }

    public void setCall(Call call) {
        okCall = call;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        RetrofitService.netErr(mCall,e);
        err();
        destroy();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            long total = response.body().contentLength();
            fos = new FileOutputStream(saveFile);
            long sum = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
                int progress = (int) (sum * 1.0f / total * 100);
            }
            fos.flush();
            response(response);
        } catch (Exception e) {
            Logger.e(e.getMessage());
            err();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                err();
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                err();
            }
            destroy();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        RetrofitService.cancel(okCall);
        okCall = null;
    }

    public abstract void response(Response response);
    public abstract void err();
}
