package com.landscape.weight;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.landscape.schoolexandroid.R;
import com.utils.system.ScreenParam;


/**
 * Created by zhusx on 2015/10/28.
 */
public class ListMenuButtonLayout extends ViewGroup implements View.OnClickListener {
    public ListMenuButtonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setButton(String... button) {
        removeAllViews();
        if (button == null || button.length == 0) {
            return;
        }

        if (button.length == 1) {
            View childView = initView(button[0], 0, false, false);
            childView.setBackgroundDrawable(getDrawableListByType(true, true, true, true));
            return;
        }

        if (button.length == 2) {
            View childView = initView(button[0], 0, false, false);
            childView.setBackgroundDrawable(getDrawableListByType(true, true, true, true));
            childView = initView(button[1], 1, false, false);
            childView.setBackgroundDrawable(getDrawableListByType(true, true, true, true));
            return;
        }

        for (int i = 0; i < button.length; i++) {
            String bu = button[i];
            View childView = null;

            if (i == (button.length - 1)) {
                //false, false, true, true
                childView = initView(bu, i, false, false);
                childView.setBackgroundDrawable(getDrawableListByType(true, true, true, true));
                continue;
            }
            if (i == 0) {
                childView = initView(bu, i, false, true);
                childView.setBackgroundDrawable(getDrawableListByType(true, true, false, false));
                continue;
            }

            if (i == (button.length - 2)) {
                childView = initView(bu, i, false, false);
                childView.setBackgroundDrawable(getDrawableListByType(false, false, true, true));
                continue;
            }
            childView = initView(bu, i, false, true);
            childView.setBackgroundDrawable(getDrawableListByType(false, false, false, false));
        }
    }

    public View initView(String button, int position, boolean isShowLine, boolean isShowBottomLine) {
        View childView = LayoutInflater.from(getContext()).inflate(R.layout.alert_ui_dialog_button_item_layout, null);
        TextView textView = (TextView) childView.findViewById(R.id.txt);
        textView.setText(button);
        childView.setTag(position);
        childView.setOnClickListener(this);
        childView.findViewById(R.id.line).setVisibility(isShowLine ? View.VISIBLE : View.GONE);
        childView.findViewById(R.id.line_bottom).setVisibility(isShowBottomLine ? View.VISIBLE : View.GONE);
        addView(childView);
        return childView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        if (getChildCount() == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (ScreenParam.dp2px(getContext(), 50) * childCount + ScreenParam.dp2px(getContext(), 10));
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }
        int item = (int) ScreenParam.dp2px(getContext(), 50);
        int ten = (int) ScreenParam.dp2px(getContext(), 10);
        for (int i = childCount - 1; i >= 0; i--) {
            View view = getChildAt(i);
            if (i == childCount - 1) {
                view.layout(0, getHeight() - item, getWidth(), getHeight());
                view.measure(getWidth(), item);
                continue;
            }
            view.layout(0, getHeight() - item * (childCount - i) - ten, getWidth(), getHeight() - item * (childCount - i - 1) - ten);
            view.measure(getWidth(), item);
        }
    }

    @Override
    public void onClick(View v) {
        Object o = v.getTag();
        if (o == null) {
            return;
        }

        int position = (int) o;
        if (mListener != null) {
            mListener.onItemClick(position);
        }
    }


    public StateListDrawable getDrawableListByType(boolean leftTop, boolean rightTop, boolean rightBottom, boolean leftBottom) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable selectDrawable = getWhitShape(5, 0xffCCCCCC, leftTop, rightTop, rightBottom, leftBottom);
        Drawable defaultDrawable = getWhitShape(5, 0xffffffff, leftTop, rightTop, rightBottom, leftBottom);

        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, selectDrawable);
        stateListDrawable.addState(new int[]{}, defaultDrawable);
        return stateListDrawable;
    }

    public Drawable getWhitShape(int radius, int bgColor, boolean leftTop, boolean rightTop, boolean rightBottom, boolean leftBottom) {
        float r = ScreenParam.dp2px(getContext(), radius);
        float a1 = 0, a2 = 0, a3 = 0, a4 = 0, a5 = 0, a6 = 0, a7 = 0, a8 = 0;
        if (leftTop) {
            a1 = r;
            a2 = r;
        }
        if (rightTop) {
            a3 = r;
            a4 = r;
        }

        if (rightBottom) {
            a5 = r;
            a6 = r;
        }

        if (leftBottom) {
            a7 = r;
            a8 = r;
        }

        float[] outerRadii = new float[]{a1, a2, a3, a4, a5, a6, a7, a8};
        RoundRectShape rrs = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable sd = new ShapeDrawable(rrs);
        sd.getPaint().setColor(bgColor);
        return sd;
    }


    private OnItemClickListener mListener;

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(int position);
    }
}
