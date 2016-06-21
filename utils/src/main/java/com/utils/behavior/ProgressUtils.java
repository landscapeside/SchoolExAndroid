package com.utils.behavior;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by 1 on 2016/5/27.
 */
public class ProgressUtils {

    private static ProgressDialog _proDialog;

    public static void showProgressDialog(Context context,String message,ICancelListener cancelListener) {
        try{
            if(_proDialog==null||!_proDialog.isShowing()){
                _proDialog = new ProgressDialog(context);
                _proDialog.setCanceledOnTouchOutside(false);
                _proDialog.setCancelable(true);
                _proDialog.setTitle(null);
                _proDialog.setOnCancelListener(arg0 -> {
                    if (cancelListener != null) {
                        cancelListener.cancel();
                    }
                });
                _proDialog.setOnDismissListener(arg0 -> {
                    // TODO Auto-generated method stub
                });
            }

            _proDialog.setMessage(message);
            if (!((Activity)context).isFinishing())
                _proDialog.show();
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    public static boolean isProgressDialogShowing() {
        try{
            if (null != _proDialog && _proDialog.isShowing()) {
                return true;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return false;
    }

    public static void dismissProgressDialog() {
        try{
            if (null != _proDialog && _proDialog.isShowing()) {
                _proDialog.dismiss();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public interface ICancelListener{
        void cancel();
    }


}
