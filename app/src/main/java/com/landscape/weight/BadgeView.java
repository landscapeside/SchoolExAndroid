package com.landscape.weight;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 1 on 2016/3/15.
 */
public class BadgeView extends TextView {

    Context mContext;

    public BadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void setBadge(CharSequence badgeNum){
        if (TextUtils.isEmpty(badgeNum)) {
            setVisibility(GONE);
        }
        setVisibility(VISIBLE);
        setText(badgeNum);
        if (badgeNum.length() < 2) {
            ViewGroup.LayoutParams params = getLayoutParams();
            params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, mContext.getResources().getDisplayMetrics());
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, mContext.getResources().getDisplayMetrics());
            setLayoutParams(params);
            invalidate();
        } else {
            ViewGroup.LayoutParams params = getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            setLayoutParams(params);
            setGravity(Gravity.TOP);
            setPadding(10,0,10,0);
            invalidate();
        }
    }
}
