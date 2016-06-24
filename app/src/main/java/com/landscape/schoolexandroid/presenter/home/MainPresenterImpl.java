package com.landscape.schoolexandroid.presenter.home;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.jsware.draglayout.DragCallBack;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.ui.activity.MainActivity;
import com.landscape.schoolexandroid.ui.fragment.DragContentFragment;
import com.landscape.schoolexandroid.ui.fragment.MenuFragment;
import com.landscape.schoolexandroid.ui.fragment.WorkTaskFragment;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.home.DragContentView;
import com.landscape.schoolexandroid.views.home.MenuView;
import com.landscape.schoolexandroid.views.home.WorkTaskListView;
import com.orhanobut.logger.Logger;
import com.utils.behavior.ActivityUtils;
import com.utils.behavior.AppFileUtils;
import com.utils.behavior.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/26.
 */
public class MainPresenterImpl implements MainPresenter {
    /**
     * ===============================
     * module-layer
     * ===============================
     */


    /**
     * ===============================
     * V-layer
     * ===============================
     */
    MainActivity mainActivity;
    DragContentView dragContent;
    MenuView menuView;
    /**
     * 内容视图
     * */
    WorkTaskListView workTaskListView;


    public MainPresenterImpl(MainActivity mainActivity) {
        dragContent = new DragContentFragment();
        menuView = new MenuFragment();
        workTaskListView = new WorkTaskFragment();
        this.mainActivity = mainActivity;
        ActivityUtils.addFragmentToActivity(mainActivity.getSupportFragmentManager(), (Fragment) menuView, R.id.drag_menu);
        ActivityUtils.addFragmentToActivity(mainActivity.getSupportFragmentManager(), (Fragment) dragContent, R.id.drag_content);
        setDragLayout(dragContent);
        this.initViews();
        refreshData(mainActivity.getIntent());
    }

    public void initViews() {
        dragContent.setPresenter(this);
        menuView.setPresenter(this);
        workTaskListView.setPresenter(this);
        dragContent.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                dragContent.setAvatarClick(() -> mainActivity.dl.open());
                dragContent.setContentFragment((Fragment) workTaskListView);
            }

            @Override
            public void onDestroy() {

            }
        });

        menuView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                String[] menuNames = new String[]{"作业本", "错题本", "丢分统计", "答题卡","收藏"};
                int[] menuDrawResIds = new int[]{R.mipmap.icon_zyb,R.mipmap.icon_ctb,R.mipmap.icon_dftj,R.mipmap.icon_dtk,R.mipmap.icon_sc};
                List<MenuView.MenuItemBean> menuItemBeanList = new ArrayList<>();
                for (int i = 0;i<menuNames.length;i++) {
                    MenuView.MenuItemBean bean = new MenuView.MenuItemBean();
                    bean.name = menuNames[i];
                    bean.drawResId = menuDrawResIds[i];
                    menuItemBeanList.add(bean);
                }
                menuView.listData(menuItemBeanList);
            }

            @Override
            public void onDestroy() {

            }
        });
        menuView.setMenuItemSelectListener(position -> {
            mainActivity.dl.close();
            // TODO: 2016/4/27 点击后进入不同的主页视图
            switch (position) {
                case 0:
                    // 作业本
                    dragContent.setTitle("作业本");
                    break;
                case 1:
                    // 错题本
                    dragContent.setTitle("错题本");
                    break;
                case 2:
                    // 丢分统计
                    dragContent.setTitle("丢分统计");
                    break;
                case 3:
                    // 答题卡
                    dragContent.setTitle("答题卡");
                    break;
                case 4:
                    // 收藏
                    dragContent.setTitle("收藏");
                    break;
            }
        });

        workTaskListView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                workTaskListView.setListItemSelectListener(position -> {
                    // TODO: 2016/6/22 跳转条目
                });
                workTaskListView.setRefreshListener(() -> {
                    // TODO: 2016/6/22 刷新数据
                });
            }

            @Override
            public void onDestroy() {

            }
        });
    }

    private void setDragLayout(DragCallBack callBack) {
        mainActivity.dl.setDragListener(callBack);
    }

    @Override
    public void refreshData(Intent data) {

    }
}
