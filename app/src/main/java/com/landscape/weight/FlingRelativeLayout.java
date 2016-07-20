package com.landscape.weight;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

/**
 * Created by 1 on 2016/6/30.
 */
public class FlingRelativeLayout extends RelativeLayout {

    private static final int INVALID_POINTER = -1;

    private float mInitialMotionX,mInitialMotionY;
    private int mTouchSlop;
    private int mActivePointerId = INVALID_POINTER;
    private static final int FLING_SLOP = 50;

    public FlingRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                final float initialMotionX = getMotionEventX(ev, mActivePointerId);
                final float initialMotionY = getMotionEventY(ev, mActivePointerId);
                if (initialMotionX == -1 || initialMotionY == -1) {
                    return false;
                }
                mInitialMotionX = initialMotionX;
                mInitialMotionY = initialMotionY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final float x = getMotionEventX(ev, mActivePointerId);
                final float y = getMotionEventY(ev, mActivePointerId);
                if (x == -1 || y == -1) {
                    return false;
                }
                final float xDiff = x - mInitialMotionX;
                final float yDiff = y - mInitialMotionY;

                if (xDiff > FLING_SLOP && Math.abs(xDiff) > Math.abs(yDiff)) {
                    // TODO: 2016/6/30 prev
                    if (flingListener != null) {
                        flingListener.prev();
                    }
                    if (touchListener != null) {
                        touchListener.onTouch();
                    }
                } else if (xDiff < -FLING_SLOP && Math.abs(xDiff) > Math.abs(yDiff)) {
                    // TODO: 2016/6/30 next
                    if (flingListener != null) {
                        flingListener.next();
                    }
                    if (touchListener != null) {
                        touchListener.onTouch();
                    }
                }
                mActivePointerId = INVALID_POINTER;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private float getMotionEventX(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getX(ev, index);
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }

    public interface FlingListener{
        void prev();
        void next();
    }

    FlingListener flingListener = null;

    public void setFlingListener(FlingListener flingListener) {
        this.flingListener = flingListener;
    }

    public interface TouchListener{
        void onTouch();
    }
    TouchListener touchListener = null;

    public void setTouchListener(TouchListener touchListener) {
        this.touchListener = touchListener;
    }
}
