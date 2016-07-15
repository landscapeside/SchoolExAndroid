package com.landscape.schoolexandroid.dialog;

import android.app.Dialog;
import android.content.Context;
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
public abstract class TimeAlertDialog extends Dialog {

    @Bind(R.id.tittlev_tv)
    TextView tittlevTv;

    public TimeAlertDialog(Context context, String title) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_dialog);
        ButterKnife.bind(this);
        tittlevTv.setText(title);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setOnCancelListener(dialog -> onOkClick(null));
    }

    @OnClick(R.id.sure)
    public void onOkClick(View view){
        dismiss();
        onOk();
    }

    public abstract void onOk();
}
