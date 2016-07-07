package com.landscape.schoolexandroid.ui.fragment.home;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.home.MenuView;
import com.squareup.picasso.Picasso;
import com.utils.image.ImageHelper;
import com.utils.system.ScreenParam;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

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
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_school)
    TextView tvSchool;
    @Bind(R.id.ll1)
    View userAccount;

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public int getResId() {
        return R.layout.view_left_menu;
    }

    @Override
    public void loadUserAccount(UserAccount userAccount) {
        if (!TextUtils.isEmpty(userAccount.getData().getPhoto())) {
            Picasso.with(getActivity())
                    .load(userAccount.getData().getPhoto())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(ivBottom);
        } else {
            ivBottom.setImageResource(R.mipmap.ic_launcher);
        }
        tvName.setText(userAccount.getData().getName());
        tvSchool.setText(userAccount.getData().getGradeName()+userAccount.getData().getTeamName()+userAccount.getData().getClassGroupName());
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
            lv.setSelection(0);
        } else {
            adapter.replaceAll(Arrays.asList(listData));
        }
    }

    @Override
    public int getCurrentIdx() {
        return lv.getSelectedItemPosition() == -1?0:lv.getSelectedItemPosition();
    }

    @Override
    public void setUserAccountListener(UserAccountListener userAccountListener) {
        userAccount.setOnClickListener(v -> userAccountListener.userAccount());
    }

    @Override
    public void setMenuItemSelectListener(OnMenuItemSelectListener listener) {
        menuItemSelectListener = listener;
    }
}
