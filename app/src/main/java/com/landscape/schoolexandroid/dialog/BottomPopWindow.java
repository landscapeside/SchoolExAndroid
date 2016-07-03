package com.landscape.schoolexandroid.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.utils.ResourcesUtil;

import java.util.List;

public class BottomPopWindow {
    private Context context;
    private PopupWindow popupWindow;
    private List<PopViewData> viewDatas;
    private OnClickListener clickListener;
    LinearLayout container;

    public BottomPopWindow(Context context, List<PopViewData> viewDatas, OnClickListener clickListener) {
        this.context = context;
        this.viewDatas = viewDatas;
        this.clickListener = clickListener;
        // 组合子view,遍历viewDatas
        if (viewDatas != null && !viewDatas.isEmpty()) {

            container = new LinearLayout(context);
            container.setOrientation(LinearLayout.VERTICAL);
            container.setBackgroundColor(Color.GRAY);
            for (PopViewData viewData : viewDatas) {
                // 生成TextView
                View gv = generateTextView(viewData.getText(), viewData.getTag());
                container.addView(gv);
            }
            container.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.pop_background));
            RelativeLayout relativeLayout = new RelativeLayout(context);
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            relativeLayout.setLayoutParams(param);

            RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            relativeLayout.addView(container, mLayoutParams);// #30000000
            relativeLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    dismiss();
                }
            });
            // relativeLayout.setBackgroundDrawable(new
            // ColorDrawable(0x55000000));
            // relativeLayout.setBackgroundColor(Color.parseColor("#30000000"));
            popupWindow = new PopupWindow(relativeLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(0x55000000));
            // popupWindow.setBackgroundDrawable(context.getResources()
            // .getDrawable(R.drawable.pop_background));
            // popupWindow.setAnimationStyle(R.style.PopupAnimation);
        }

    }

    public View generateTextView(String text, String tag) {
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) ResourcesUtil.getDesity() * 45);
        param.setMargins(0, 0, 0, 2);
        tv.setLayoutParams(param);
        tv.setText(text);

        tv.setGravity(Gravity.CENTER);
        // tv.setPadding(0, 30, 0, 30);
        tv.setTextSize(16);

        tv.setBackgroundColor(Color.WHITE);
        if (text.equals("取消")) {
            LinearLayout container = new LinearLayout(context);
            tv.setTextColor(context.getResources().getColor(R.color.default_lev3));
            container.setOrientation(LinearLayout.VERTICAL);
            View v = new View(context);
            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)ResourcesUtil.getDesity() * 5);
            v.setLayoutParams(param2);
            v.setBackgroundColor(context.getResources().getColor(R.color.pagebackground));
            container.addView(v);
            container.addView(tv);
            container.setTag(tag);
            container.setOnClickListener(clickListener);
            // container.setBackgroundColor(Color.GRAY);
            return container;
        }
        tv.setTag(tag);
        tv.setOnClickListener(clickListener);
        tv.setTextColor(context.getResources().getColor(R.color.default_lev2));
        return tv;

    }

    public void show() {
        if (popupWindow != null) {
            container.startAnimation(AnimationUtils.loadAnimation(context, R.anim.menu_up));
            popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            // WindowManager.LayoutParams lp =
            // context.getWindow().getAttributes();
            // lp.alpha = 0.3f;
            // context.getWindow().setAttributes(lp);
        }

    }

    public void dismiss() {
        if (popupWindow != null) {
            // WindowManager.LayoutParams lp =
            // context.getWindow().getAttributes();
            // lp.alpha = 1f;
            // context.getWindow().setAttributes(lp);
            //			container.clearAnimation();
            Animation downAnim = AnimationUtils.loadAnimation(context, R.anim.menu_down_fix);
            container.startAnimation(downAnim);
            downAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            popupWindow.dismiss();
                        }
                    });
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

    }

    /**
     * 存储数据
     *
     * @author dell12
     */
    public static class PopViewData {
        private String text;
        private String tag;

        public PopViewData(String text, String tag) {
            this.text = text;
            this.tag = tag;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

    }
}
