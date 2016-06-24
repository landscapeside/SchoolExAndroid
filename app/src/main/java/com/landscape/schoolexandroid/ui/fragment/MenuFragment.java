package com.landscape.schoolexandroid.ui.fragment;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.home.MenuView;
import com.utils.image.ImageHelper;
import com.utils.system.ScreenParam;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * Created by 1 on 2016/4/26.
 */
public class MenuFragment extends BaseFragment implements MenuView<BasePresenter> {

    QuickAdapter adapter;
    MenuView.OnMenuItemSelectListener menuItemSelectListener;
    @Bind(R.id.iv_bottom)
    ImageView ivBottom;
    @Bind(R.id.lv)
    ListView lv;

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public int getResId() {
        return R.layout.view_left_menu;
    }

    @Override
    public void listData(List<MenuItemBean> listData) {
        if (adapter == null) {
            adapter = new QuickAdapter<MenuItemBean>(getActivity(), R.layout.item_text, listData) {
                @Override
                protected void convert(BaseAdapterHelper helper, MenuItemBean item) {
                    TextView textView = helper.getView(R.id.text1);
                    textView.setText(item.name);
                    // 设置图标
                    Drawable mLeftDrawable = getActivity().getResources().getDrawable(item.drawResId);
                    mLeftDrawable = ImageHelper.scaleDrawable(
                            getResources(), mLeftDrawable,
                            ((float) ScreenParam.dp2px(getActivity(), 30f)) / (float) mLeftDrawable.getMinimumHeight());
                    mLeftDrawable.setBounds(0, 0, mLeftDrawable.getMinimumWidth(), mLeftDrawable.getMinimumHeight());
                    textView.setCompoundDrawables(mLeftDrawable, null, null, null);
                }
            };
            lv.setAdapter(adapter);
            lv.setOnItemClickListener((parent, view, position, id) -> {
                if (menuItemSelectListener != null) {
                    menuItemSelectListener.onSelect(position);
                }
            });
        } else {
            adapter.replaceAll(Arrays.asList(listData));
        }
    }

    @Override
    public void setMenuItemSelectListener(OnMenuItemSelectListener listener) {
        menuItemSelectListener = listener;
    }
}
