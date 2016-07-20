package com.landscape.weight;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by 1 on 2016/7/19.
 */
public class ScrollWebView extends WebView {

    private static final int INVALID_POINTER = -1;

    boolean isTop = false;

    private float mInitialMotionX,mInitialMotionY;
    private int mActivePointerId = INVALID_POINTER;
    private static final int FLING_SLOP = 50;

    public ScrollWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWebView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                final float initialMotionX = getMotionEventX(event, mActivePointerId);
                final float initialMotionY = getMotionEventY(event, mActivePointerId);
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
                final float x = getMotionEventX(event, mActivePointerId);
                final float y = getMotionEventY(event, mActivePointerId);
                if (x == -1 || y == -1) {
                    return false;
                }
                final float xDiff = x - mInitialMotionX;
                final float yDiff = y - mInitialMotionY;

                if (Math.abs(xDiff) > Math.abs(yDiff)) {
                    if (xDiff > FLING_SLOP) {
                        // TODO: 2016/7/19 prev
                        if (!canScrollLeft() && dragHorizontalListener != null) {
                            dragHorizontalListener.leftDrag();
                        }
                    } else if (xDiff < -FLING_SLOP) {
                        // TODO: 2016/7/19 next
                        if (!canScrollRight() && dragHorizontalListener != null) {
                            dragHorizontalListener.rightDrag();
                        }
                    }
                } else {
                    if (yDiff > FLING_SLOP) {
                        // TODO: 2016/7/19 refresh
                        if (isTop() && refreshListener != null) {
                            refreshListener.onRefresh();
                        }
                    }
                }
                mActivePointerId = INVALID_POINTER;
                break;
        }

        return super.onTouchEvent(event);
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

    public boolean isTop() {
        isTop = false;
        float webcontent = getContentHeight() * getScale();// webview的高度
        float webnow = getMeasuredHeight() + getScrollY();// 当前webview的高度
        if (Math.abs(webcontent - webnow) < 1) {
            // 已经处于底端
            // Log.i("TAG1", "已经处于底端");
        } else if (getScrollY() == 0) {
            // Log.i("TAG1", "已经处于顶端");
            isTop = true;
        }
        return isTop;
    }

    public boolean canScrollLeft() {
        return canScrollHor(-1);
    }

    public boolean canScrollRight() {
        return canScrollHor(1);
    }

    public boolean canScrollHor(int direction) {
        final int offset = computeHorizontalScrollOffset();
        final int range = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
        if (range == 0) return false;
        if (direction < 0) {
            return offset > 0;
        } else {
            return offset < range - 1;
        }
    }

    DragHorizontalListener dragHorizontalListener = null;

    public void setDragHorizontalListener(DragHorizontalListener dragHorizontalListener) {
        this.dragHorizontalListener = dragHorizontalListener;
    }

    public interface DragHorizontalListener{
        void leftDrag();
        void rightDrag();
    }

    RefreshListener refreshListener = null;

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public interface RefreshListener{
        void onRefresh();
    }

}
