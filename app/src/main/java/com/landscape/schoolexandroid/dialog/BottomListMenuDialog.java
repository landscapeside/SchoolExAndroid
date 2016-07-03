package com.landscape.schoolexandroid.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.landscape.schoolexandroid.R;
import com.landscape.weight.ListMenuButtonLayout;

import java.util.List;

/**
 * Created by zhusx on 2015/10/16.
 */
public class BottomListMenuDialog extends FullBaseDialogDialog implements ListMenuButtonLayout.OnItemClickListener {
    private ListMenuButtonLayout mListView;
    private boolean isClickCancel = false;
    private ListMenuButtonLayout.OnItemClickListener mItemClick;

    public BottomListMenuDialog(Context context, String... strs) {
        this(context, true, strs);
    }

    public BottomListMenuDialog(Context context, boolean isClickCancel, String... strs) {
        super(context);
        this.isClickCancel = isClickCancel;
        init(strs);
    }

    public BottomListMenuDialog(Context context, boolean isClickCancel, List<String> strs) {
        super(context);
        this.isClickCancel = isClickCancel;
        String[] ss = new String[strs.size()];
        init(strs.toArray(ss));
    }

    private void init(String[] strs) {
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        if (strs == null) {
            return;
        }
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_menu_list_dialog_layout, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(getWindow().getAttributes().width, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(contentView, params);
        mListView = (ListMenuButtonLayout) findViewById(R.id.list_view);
        mListView.setButton(strs);
    }

    @Override
    public void show() {
        super.show();
        if (mListView == null) {
            dismiss();
        }
    }

    public void setOnItemClick(ListMenuButtonLayout.OnItemClickListener listener) {
        mItemClick = listener;
        mListView.setListener(this);
    }


    @Override
    public void onItemClick(int position) {
        if (mItemClick != null) {
            mItemClick.onItemClick(position);
        }
        if (isClickCancel) {
            dismiss();
        }
    }
}
