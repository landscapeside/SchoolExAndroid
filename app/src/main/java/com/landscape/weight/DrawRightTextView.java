package com.landscape.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class DrawRightTextView extends TextView {

    public DrawRightTextView(Context context) {
        super(context);
    }

    public DrawRightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawRightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                Log.d("mytest", "action up");
                boolean isClean = (event.getX() > (getWidth() - getTotalPaddingRight()))
                        && (event.getX() < (getWidth() - getPaddingRight()));
                if (isClean) {
                    if (onRightClickListener != null) {
                        onRightClickListener.onRightBtnClick();
                    }
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private OnRightClickListener onRightClickListener = null;

    public void setOnRightClickListener(OnRightClickListener onRightClickListener) {
        this.onRightClickListener = onRightClickListener;
    }

    public interface OnRightClickListener{
        void onRightBtnClick();
    }

    public void setShakeAnimation() {
        this.clearAnimation();
        this.setAnimation(shakeAnimation(5));
    }

    public Animation shakeAnimation(int CycleTimes) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 10);
        translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }
}