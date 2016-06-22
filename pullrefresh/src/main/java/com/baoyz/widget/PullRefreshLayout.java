package com.baoyz.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.security.InvalidParameterException;

/**
 * Created by baoyz on 14/10/30.
 */
public class PullRefreshLayout extends ViewGroup {

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    private static final int DRAG_MAX_DISTANCE = 64;
    private static final int INVALID_POINTER = -1;
    private static final float DRAG_RATE = .5f;

    public static final int STYLE_MATERIAL = 0;
    public static final int STYLE_CIRCLES = 1;
    public static final int STYLE_WATER_DROP = 2;
    public static final int STYLE_RING = 3;
    public static final int STYLE_SMARTISAN = 4;

    private View mTarget;
    Context mContext;
    private ImageView mRefreshView, mLoadView;
    private Interpolator mDecelerateInterpolator;
    private int mTouchSlop;
    private int mSpinnerFinalOffset;
    private int mTotalDragDistance;
    private RefreshDrawable mRefreshDrawable, mLoadDrawable;
    private int mCurrentOffsetTop, mCurrentOffsetBottom;
    private boolean mRefreshing, mLoading;
    private int mActivePointerId;
    private boolean mIsBeingDragged;
    private float mInitialMotionY;
    private int mFrom;
    private boolean mNotify;
    private OnRefreshListener mListener;
    private OnCancelRefreshListener mCancelRefreshListener;
    private int[] mColorSchemeColors;

    public int mDurationToStartPosition;
    public int mDurationToCorrectPosition;
    private int mInitialOffsetTop, mInitialOffsetBottom;
    private boolean mDispatchTargetTouchDown;
    private float mDragPercent;

    public PullRefreshLayout(Context context) {
        this(context, null);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.refresh_PullRefreshLayout);
        final int type = a.getInteger(R.styleable.refresh_PullRefreshLayout_refreshType, STYLE_MATERIAL);
        final int colorsId = a.getResourceId(R.styleable.refresh_PullRefreshLayout_refreshColors, 0);
        final int colorId = a.getResourceId(R.styleable.refresh_PullRefreshLayout_refreshColor, 0);
        a.recycle();
        mContext = context;
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        int defaultDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        mDurationToStartPosition = defaultDuration;
        mDurationToCorrectPosition = defaultDuration;
        mSpinnerFinalOffset = mTotalDragDistance = dp2px(DRAG_MAX_DISTANCE);

        if (colorsId > 0) {
            mColorSchemeColors = context.getResources().getIntArray(colorsId);
        } else {
            mColorSchemeColors = new int[]{Color.rgb(0xC9, 0x34, 0x37), Color.rgb(0x37, 0x5B, 0xF1), Color.rgb(0xF7, 0xD2, 0x3E), Color.rgb(0x34, 0xA3, 0x50)};
        }

        if (colorId > 0) {
            mColorSchemeColors = new int[]{context.getResources().getColor(colorId)};
        }
        mRefreshView = new ImageView(context);
        setRefreshStyle(type);
        mRefreshView.setVisibility(View.GONE);
        addView(mRefreshView, 0);
        mLoadView = new ImageView(mContext);
        mLoadView.setImageDrawable(mLoadDrawable);
        mLoadView.setVisibility(View.GONE);
        addView(mLoadView);
        setWillNotDraw(false);
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
    }

    public void setColorSchemeColors(int... colorSchemeColors) {
        mColorSchemeColors = colorSchemeColors;
        mRefreshDrawable.setColorSchemeColors(colorSchemeColors);
    }

    public void setColor(int color) {
        setColorSchemeColors(color);
    }

    public void setRefreshStyle(int type) {
        setRefreshing(false);
        switch (type) {
            case STYLE_MATERIAL:
                mRefreshDrawable = new MaterialDrawable(getContext(), this);
                mLoadDrawable = new MaterialDrawable(getContext(), this);
                break;
            case STYLE_CIRCLES:
                mRefreshDrawable = new CirclesDrawable(getContext(), this);
                mLoadDrawable = new CirclesDrawable(getContext(), this);
                break;
            case STYLE_WATER_DROP:
                mRefreshDrawable = new WaterDropDrawable(getContext(), this);
                mLoadDrawable = new WaterDropDrawable(getContext(), this);
                break;
            case STYLE_RING:
                mRefreshDrawable = new RingDrawable(getContext(), this);
                mLoadDrawable = new RingDrawable(getContext(), this);
                break;
            case STYLE_SMARTISAN:
                mRefreshDrawable = new SmartisanDrawable(getContext(), this);
                mLoadDrawable = new SmartisanDrawable(getContext(), this);
                break;
            default:
                throw new InvalidParameterException("Type does not exist");
        }
        mRefreshDrawable.setColorSchemeColors(mColorSchemeColors);
        mLoadDrawable.setColorSchemeColors(mColorSchemeColors);
        mRefreshView.setImageDrawable(mRefreshDrawable);
    }

    public void setRefreshDrawable(RefreshDrawable drawable) {
        setRefreshing(false);
        mRefreshDrawable = drawable;
        mRefreshDrawable.setColorSchemeColors(mColorSchemeColors);
        mRefreshView.setImageDrawable(mRefreshDrawable);
    }

    public void setLoadDrawable(RefreshDrawable drawable) {
        setLoading(false);
        mLoadDrawable = drawable;
        mLoadDrawable.setColorSchemeColors(mColorSchemeColors);
        mLoadView.setImageDrawable(mLoadDrawable);
    }

    public int getFinalOffset() {
        return mSpinnerFinalOffset;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ensureTarget();
        if (mTarget == null)
            return;

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingRight() - getPaddingLeft(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
        mTarget.measure(widthMeasureSpec, heightMeasureSpec);
        mRefreshView.measure(widthMeasureSpec, heightMeasureSpec);
        mLoadView.measure(widthMeasureSpec, heightMeasureSpec);
//        mRefreshView.measure(MeasureSpec.makeMeasureSpec(mRefreshViewWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mRefreshViewHeight, MeasureSpec.EXACTLY));
    }

    private void ensureTarget() {
        if (mTarget != null)
            return;
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != mRefreshView) {
                    mTarget = child;
//                    return;
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled() || (canChildScrollUp() && !mRefreshing && canChildScrollDown() && !mLoading)) {
            return false;
        }

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mRefreshing) {
                    setTargetOffsetTop(0, true);
                } else if (!mLoading) {
                    setTargetOffsetBottom(0, true);
                }
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                final float initialMotionY = getMotionEventY(ev, mActivePointerId);
                if (initialMotionY == -1) {
                    return false;
                }
                mInitialMotionY = initialMotionY;
                mInitialOffsetTop = mCurrentOffsetTop;
                mInitialOffsetBottom = mCurrentOffsetBottom;
                mDispatchTargetTouchDown = false;
                mDragPercent = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final float y = getMotionEventY(ev, mActivePointerId);
                if (y == -1) {
                    return false;
                }
                final float yDiff = y - mInitialMotionY;
                if (mRefreshing || mLoading) {
                    mIsBeingDragged = !(yDiff < 0 && mCurrentOffsetTop <= 0);
                } else if (yDiff > mTouchSlop && !mIsBeingDragged && !canChildScrollUp()) {
                    mIsBeingDragged = true;
                } else if (!mIsBeingDragged && !isFullShorter() && !canChildScrollDown() && yDiff < -mTouchSlop && onLoadListener != null) {
                    mIsBeingDragged = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (!mIsBeingDragged) {
            return super.onTouchEvent(ev);
        }

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float yDiff = y - mInitialMotionY;
                int targetY;
                if (mRefreshing || mLoading) {
                    if (mRefreshing) {
                        targetY = (int) (mInitialOffsetTop + yDiff);
                        if (canChildScrollUp()) {
                            targetY = -1;
                            mInitialMotionY = y;
                            mInitialOffsetTop = 0;
                            if (mDispatchTargetTouchDown) {
                                mTarget.dispatchTouchEvent(ev);
                            } else {
                                MotionEvent obtain = MotionEvent.obtain(ev);
                                obtain.setAction(MotionEvent.ACTION_DOWN);
                                mDispatchTargetTouchDown = true;
                                mTarget.dispatchTouchEvent(obtain);
                            }
                        } else {
                            if (targetY < 0) {
                                if (mDispatchTargetTouchDown) {
                                    mTarget.dispatchTouchEvent(ev);
                                } else {
                                    MotionEvent obtain = MotionEvent.obtain(ev);
                                    obtain.setAction(MotionEvent.ACTION_DOWN);
                                    mDispatchTargetTouchDown = true;
                                    mTarget.dispatchTouchEvent(obtain);
                                }
                                targetY = 0;
                            } else if (targetY > mTotalDragDistance) {
                                targetY = mTotalDragDistance;
                            } else {
                                if (mDispatchTargetTouchDown) {
                                    MotionEvent obtain = MotionEvent.obtain(ev);
                                    obtain.setAction(MotionEvent.ACTION_CANCEL);
                                    mDispatchTargetTouchDown = false;
                                    mTarget.dispatchTouchEvent(obtain);
                                }
                            }
                        }
                        setTargetOffsetTop(targetY - mCurrentOffsetTop, true);
                    } else {
                        targetY = (int) (mInitialOffsetTop + yDiff);
                        if (canChildScrollDown()) {
                            targetY = -1;
                            mInitialMotionY = y;
                            mInitialOffsetTop = 0;
                            if (mDispatchTargetTouchDown) {
                                mTarget.dispatchTouchEvent(ev);
                            } else {
                                MotionEvent obtain = MotionEvent.obtain(ev);
                                obtain.setAction(MotionEvent.ACTION_DOWN);
                                mDispatchTargetTouchDown = true;
                                mTarget.dispatchTouchEvent(obtain);
                            }
                        } else {
                            if (targetY >= 0) {
                                if (mDispatchTargetTouchDown) {
                                    mTarget.dispatchTouchEvent(ev);
                                } else {
                                    MotionEvent obtain = MotionEvent.obtain(ev);
                                    obtain.setAction(MotionEvent.ACTION_DOWN);
                                    mDispatchTargetTouchDown = true;
                                    mTarget.dispatchTouchEvent(obtain);
                                }
                                targetY = 0;
                            } else if (targetY < -mTotalDragDistance) {
                                targetY = -mTotalDragDistance;
                            } else {
                                if (mDispatchTargetTouchDown) {
                                    MotionEvent obtain = MotionEvent.obtain(ev);
                                    obtain.setAction(MotionEvent.ACTION_CANCEL);
                                    mDispatchTargetTouchDown = false;
                                    mTarget.dispatchTouchEvent(obtain);
                                }
                            }
                        }
                        setTargetOffsetBottom(targetY - mCurrentOffsetTop, true);
                    }
                } else {
                    final float scrollTop = yDiff * DRAG_RATE;
                    float originalDragPercent = scrollTop / mTotalDragDistance;
                    if (mCurrentOffsetTop >= 0 && originalDragPercent < 0 && !canChildScrollUp()) {
                        return false;
                    }
                    mDragPercent = Math.min(1f, Math.abs(originalDragPercent));
                    float extraOS = Math.abs(scrollTop) - mTotalDragDistance;
                    float slingshotDist = mSpinnerFinalOffset;
                    float tensionSlingshotPercent = Math.max(0,
                            Math.min(extraOS, slingshotDist * 2) / slingshotDist);
                    float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
                            (tensionSlingshotPercent / 4), 2)) * 2f;
                    float extraMove = (slingshotDist) * tensionPercent * 2;
                    targetY = (int) ((slingshotDist * mDragPercent) + extraMove);
                    if (yDiff > 0) {
                        if (mRefreshView.getVisibility() != View.VISIBLE) {
                            mRefreshView.setVisibility(View.VISIBLE);
                        }
                        if (scrollTop < mTotalDragDistance) {
                            mRefreshDrawable.setPercent(mDragPercent);
                        }
                        setTargetOffsetTop(targetY - mCurrentOffsetTop, true);
                    } else {
                        if (mLoadView.getVisibility() != View.VISIBLE) {
                            mLoadView.setVisibility(View.VISIBLE);
                        }
                        if (scrollTop < mTotalDragDistance) {
                            mLoadDrawable.setPercent(mDragPercent);
                        }
                        setTargetOffsetBottom(-targetY - mCurrentOffsetTop, true);
                    }
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN:
                final int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float overscrollTop = (y - mInitialMotionY) * DRAG_RATE;
                if (mRefreshing || mLoading) {
                    if (mDispatchTargetTouchDown) {
                        mTarget.dispatchTouchEvent(ev);
                        mDispatchTargetTouchDown = false;
                    }
//                    return false;
                }
                if (overscrollTop > mTotalDragDistance) {
                    if (mLoading) {
                        if (mCancelLoadListener != null) {
                            mCancelLoadListener.onCancel();
                        }
                        setLoading(false);
                    } else if (!canChildScrollUp()) {
                        setRefreshing(true, true);
                    }
                } else if (overscrollTop > 0 && overscrollTop <= mTotalDragDistance) {
                    if (!mRefreshing && !canChildScrollUp()) {
                        animateOffsetToStartPosition();
                    } else if (mLoading) {
                        if (mCancelLoadListener != null) {
                            mCancelLoadListener.onCancel();
                        }
                        setLoading(false);
                    }
                } else {
                    if (mRefreshing) {
                        animateOffsetToStartPosition();
                        if (mCancelRefreshListener != null) {
                            mCancelRefreshListener.onCancel();
                        }
                        mRefreshing = false;
                    } else if (overscrollTop < -mTotalDragDistance && !isFullShorter() && onLoadListener != null) {
                        setLoading(true);
                    } else if (overscrollTop < 0 && overscrollTop > -mTotalDragDistance && isTargetBottom() && onLoadListener != null) {
                        if (!mLoading && !isFullShorter()) {
                            animateOffsetToEndPosition();
                        }
                    }
                }
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                return false;
            }
        }

        return true;
    }

    public void setDurations(int durationToStartPosition, int durationToCorrectPosition) {
        mDurationToStartPosition = durationToStartPosition;
        mDurationToCorrectPosition = durationToCorrectPosition;
    }

    private void animateOffsetToStartPosition() {
        mFrom = mCurrentOffsetTop;
        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(mDurationToStartPosition);
        mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        mAnimateToStartPosition.setAnimationListener(mToStartListener);
        mRefreshView.clearAnimation();
        mRefreshView.startAnimation(mAnimateToStartPosition);
    }

    private void animateOffsetToEndPosition() {
        mFrom = mCurrentOffsetBottom;
        mAnimateToEndPosition.reset();
        mAnimateToEndPosition.setDuration(mDurationToStartPosition);
        mAnimateToEndPosition.setInterpolator(mDecelerateInterpolator);
        mAnimateToEndPosition.setAnimationListener(mToStartListener);
        mLoadView.clearAnimation();
        mLoadView.startAnimation(mAnimateToEndPosition);
    }

    private void animateOffsetToCorrectPosition() {
        mFrom = mCurrentOffsetTop;
        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(mDurationToCorrectPosition);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        mAnimateToCorrectPosition.setAnimationListener(mRefreshListener);
        mRefreshView.clearAnimation();
        mRefreshView.startAnimation(mAnimateToCorrectPosition);
    }

    private void animateOffsetToCorrectBottomPosition() {
        mFrom = mCurrentOffsetBottom;
        mAnimateToCorrectBottomPosition.reset();
        mAnimateToCorrectBottomPosition.setDuration(mDurationToCorrectPosition);
        mAnimateToCorrectBottomPosition.setInterpolator(mDecelerateInterpolator);
        mAnimateToCorrectBottomPosition.setAnimationListener(mLoadListener);
        mLoadView.clearAnimation();
        mLoadView.startAnimation(mAnimateToCorrectBottomPosition);
    }


    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            moveToStart(interpolatedTime);
        }
    };

    private final Animation mAnimateToEndPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            moveToEnd(interpolatedTime);
        }
    };

    private final Animation mAnimateToCorrectPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int endTarget = mSpinnerFinalOffset;
            int targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
            int offset = targetTop - mTarget.getTop();
            setTargetOffsetTop(offset, false /* requires update */);
        }
    };

    private final Animation mAnimateToCorrectBottomPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int endTarget = getPaddingTop() + getMeasuredHeight() - getPaddingBottom() - mSpinnerFinalOffset;
            int targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
            int offset = targetTop - mTarget.getBottom();
            setTargetOffsetBottom(offset, false /* requires update */);
        }
    };

    private void moveToStart(float interpolatedTime) {
        int targetTop = mFrom - (int) (mFrom * interpolatedTime);
        int offset = targetTop - mTarget.getTop();
        setTargetOffsetTop(offset, false);
        mRefreshDrawable.setPercent(mDragPercent * (1 - interpolatedTime));
    }

    private void moveToEnd(float interpolatedTime) {
        int targetEnd = mFrom + (int) ((getPaddingTop() + getMeasuredHeight() - getPaddingBottom() - mFrom) * interpolatedTime);
        int offset = targetEnd - mTarget.getBottom();
        setTargetOffsetBottom(offset, false);
        mLoadDrawable.setPercent(mDragPercent * (1 - interpolatedTime));
    }

    public void setRefreshing(boolean refreshing) {
        if (mRefreshing != refreshing) {
            setRefreshing(refreshing, false /* notify */);
        }
    }

    private void setRefreshing(boolean refreshing, final boolean notify) {
        if (mRefreshing != refreshing) {
            mNotify = notify;
            ensureTarget();
            mRefreshing = refreshing;
            if (mRefreshing) {
                mRefreshDrawable.setPercent(1f);
                animateOffsetToCorrectPosition();
            } else {
                animateOffsetToStartPosition();
            }
        }
    }

    public void setLoading(boolean loading) {
        if (mLoading != loading) {
            ensureTarget();
            mLoading = loading;
            if (mLoading) {
                mLoadDrawable.setPercent(1f);
                animateOffsetToCorrectBottomPosition();
            } else {
                animateOffsetToEndPosition();
            }
        }
    }

    private Animation.AnimationListener mRefreshListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            if (mRefreshing) {
                mRefreshView.setVisibility(VISIBLE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mRefreshing) {
                mRefreshDrawable.start();
                if (mNotify) {
                    if (mListener != null) {
                        mListener.onRefresh();
                    }
                }
            } else {
                mRefreshDrawable.stop();
                mRefreshView.setVisibility(View.GONE);
                animateOffsetToStartPosition();
            }
            mCurrentOffsetTop = mTarget.getTop();
        }
    };

    private Animation.AnimationListener mLoadListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            if (mLoading) {
                mLoadView.setVisibility(VISIBLE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mLoading) {
                mLoadDrawable.start();
                if (onLoadListener != null) {
                    onLoadListener.onLoad();
                }
            } else {
                mLoadDrawable.stop();
                mLoadView.setVisibility(View.GONE);
                animateOffsetToEndPosition();
            }
            mCurrentOffsetBottom = mTarget.getBottom();
        }
    };

    private Animation.AnimationListener mToStartListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            mRefreshDrawable.stop();
            mLoadDrawable.stop();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
//            mRefreshDrawable.stop();
            mRefreshView.setVisibility(View.GONE);
            mLoadView.setVisibility(GONE);
            mCurrentOffsetTop = mTarget.getTop();
            mCurrentOffsetBottom = mTarget.getBottom();
        }
    };

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }

    private void setTargetOffsetTop(int offset, boolean requiresUpdate) {
//        mRefreshView.bringToFront();
        mTarget.offsetTopAndBottom(offset);
        mCurrentOffsetTop = mTarget.getTop();
        mCurrentOffsetBottom = mTarget.getBottom();
        mRefreshDrawable.offsetTopAndBottom(offset);
        if (requiresUpdate && android.os.Build.VERSION.SDK_INT < 11) {
            invalidate();
        }
    }

    private void setTargetOffsetBottom(int offset, boolean requiresUpdate) {
//        mRefreshView.bringToFront();
        mTarget.offsetTopAndBottom(offset);
        mCurrentOffsetTop = mTarget.getTop();
        mCurrentOffsetBottom = mTarget.getBottom();
        mLoadDrawable.offsetTopAndBottom(offset);
        if (requiresUpdate && android.os.Build.VERSION.SDK_INT < 11) {
            invalidate();
        }
    }

    private boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    public boolean canChildScrollDown() {
//        if (android.os.Build.VERSION.SDK_INT < 14) {
        if (mTarget instanceof AbsListView) {
            final AbsListView absListView = (AbsListView) mTarget;
            View lastChild = absListView.getChildAt(absListView.getChildCount() - 1);
            if (lastChild != null) {
                if (absListView.getFirstVisiblePosition() == 0 && absListView.getLastVisiblePosition() == (absListView.getCount() - 1)) {
                    return false;
                }
                return (absListView.getLastVisiblePosition() < (absListView.getCount() - 1))
                        && lastChild.getBottom() > absListView.getPaddingBottom();
            } else {
                return false;
            }
        } else if (mTarget instanceof RecyclerView) {
            final RecyclerView recyclerView = (RecyclerView) mTarget;
            View lastChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
            RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
            if (lastChild != null) {
                return !(lm.canScrollVertically() && isTargetBottom());
            } else {
                return false;
            }
        } else {
            View scrollChild = ((ViewGroup) mTarget).getChildAt(0);
            if (scrollChild == null) {
                return false;
            } else {
                int childHeight = scrollChild.getMeasuredHeight();
                return (mTarget.getScaleY() + mTarget.getHeight()) < childHeight;
            }
        }
//        } else {
//            return ViewCompat.canScrollVertically(mTarget, 1);
//        }
    }

    private boolean isTargetBottom() {
        if (mTarget instanceof AbsListView) {
            final AbsListView absListView = (AbsListView) mTarget;
            View lastChild = absListView.getChildAt(absListView.getChildCount() - 1);
            if (lastChild != null) {
                if (absListView.getFirstVisiblePosition() > 0 && absListView.getLastVisiblePosition() == (absListView.getCount() - 1)) {
                    return true;
                }
            }
        } else if (mTarget instanceof RecyclerView) {
            final RecyclerView recyclerView = (RecyclerView) mTarget;
            LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
            int count = recyclerView.getAdapter().getItemCount() - 1;
            if (lm.findLastVisibleItemPosition() == count) {
                return true;
            }
        } else {
            View scrollChild = ((ViewGroup) mTarget).getChildAt(0);
            if (scrollChild != null) {
                int childHeight = scrollChild.getMeasuredHeight();
                return (mTarget.getScaleY() + mTarget.getHeight()) >= childHeight;
            }
        }
        return false;
    }

    public boolean isFullShorter() {
        if (mTarget instanceof AbsListView) {
            final AbsListView absListView = (AbsListView) mTarget;
            View lastChild = absListView.getChildAt(absListView.getChildCount() - 1);
            if (lastChild != null) {
                return (absListView.getFirstVisiblePosition() == 0 && absListView.getLastVisiblePosition() == (absListView.getCount() - 1));
            } else {
                return true;
            }
        } else if (mTarget instanceof RecyclerView) {
            final RecyclerView recyclerView = (RecyclerView) mTarget;
            LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (recyclerView.getAdapter() != null) {
                int count = recyclerView.getAdapter().getItemCount() - 1;
                return (recyclerView.getAdapter().getItemCount() == 0
                        || (lm.findLastVisibleItemPosition() == count
                        && lm.findFirstVisibleItemPosition() == 0));
            } else {
                return true;
            }
        } else {
            View scrollChild = ((ViewGroup) mTarget).getChildAt(0);
            if (scrollChild == null) {
                return true;
            } else {
                int childHeight = scrollChild.getMeasuredHeight();
                return childHeight < mTarget.getHeight();
            }
        }
    }

    private boolean resetLoadTop = false;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        ensureTarget();
        if (mTarget == null)
            return;

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();

        mTarget.layout(left, top + mTarget.getTop(), left + width - right, top + height - bottom + mTarget.getTop());
        mRefreshView.layout(left, top, left + width - right, top + height - bottom);
        mLoadView.layout(left, top, left + width - right, top + height - bottom);
        if (mLoadDrawable != null && !resetLoadTop) {
            mLoadDrawable.setTop(top + height - bottom + (getFinalOffset() - dp2px(40)) / 2);
            resetLoadTop = true;
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    private OnLoadListener onLoadListener = null;

    public interface OnLoadListener {
        void onLoad();
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    public void setOnCancelRefreshListener(OnCancelRefreshListener listener) {
        mCancelRefreshListener = listener;
    }

    public interface OnCancelRefreshListener {
        void onCancel();
    }

    private OnCancelLoadListener mCancelLoadListener = null;

    public void setOnCancelLoadListener(OnCancelLoadListener mCancelLoadListener) {
        this.mCancelLoadListener = mCancelLoadListener;
    }

    public interface OnCancelLoadListener {
        void onCancel();
    }

}
