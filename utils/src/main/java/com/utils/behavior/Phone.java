package com.utils.behavior;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by 1 on 2016/3/7.
 */
public class Phone {
    public static void call(Context context, String phone){
        if (TextUtils.isEmpty(phone)) {
            return;
        }
        Intent dail = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        dail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(dail);
    }
}
