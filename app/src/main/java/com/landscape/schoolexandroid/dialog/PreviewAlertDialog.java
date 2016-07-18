package com.landscape.schoolexandroid.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.landscape.schoolexandroid.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 1 on 2016/7/1.
 */
public abstract class PreviewAlertDialog extends Dialog {

    @Bind(R.id.tittlev_tv)
    TextView tittlevTv;

    public PreviewAlertDialog(Context context, String title) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.preview_dialog);
        ButterKnife.bind(this);
        tittlevTv.setText(title);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setOnCancelListener(dialog -> onCancel());
    }

    @OnClick(R.id.cancle)
    public void onCancel(View view) {
        dismiss();
        onCancel();
    }

    @OnClick(R.id.sure)
    public void onOkClick(View view) {
        dismiss();
        onOk();
    }

    public abstract void onOk();

    public abstract void onCancel();
}
