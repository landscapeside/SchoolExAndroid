package com.landscape.schoolexandroid.ui.fragment;

import android.widget.ImageView;
import android.widget.ListView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.home.MenuView;

import java.util.Arrays;

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
    public void listData(String[] listData) {
        if (adapter == null) {
            adapter = new QuickAdapter<String>(getActivity(), R.layout.item_text, Arrays.asList(listData)) {
                @Override
                protected void convert(BaseAdapterHelper helper, String item) {
                    helper.setText(R.id.text1, item);
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
