package com.landscape.schoolexandroid.presenter.home;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.jsware.draglayout.DragCallBack;
import com.landscape.event.RefreshListEvent;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.api.BaseCallBack;
import com.landscape.schoolexandroid.api.RetrofitService;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.landscape.schoolexandroid.datasource.home.WorkTaskDataSource;
import com.landscape.schoolexandroid.enums.PagerType;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskListInfo;
import com.landscape.schoolexandroid.ui.activity.MainActivity;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.home.DragContentFragment;
import com.landscape.schoolexandroid.ui.fragment.home.MenuFragment;
import com.landscape.schoolexandroid.ui.fragment.home.WorkTaskFragment;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.home.DragContentView;
import com.landscape.schoolexandroid.views.home.MenuView;
import com.landscape.schoolexandroid.views.home.WorkTaskListView;
import com.orhanobut.logger.Logger;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.utils.behavior.FragmentsUtils;
import com.utils.behavior.ToastUtil;
import com.utils.datahelper.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016/4/26.
 */
public class MainPresenterImpl implements MainPresenter {
    /**
     * ===============================
     * module-layer
     * ===============================
     */
    @Inject
    WorkTaskDataSource workTaskDataSource;
    @Inject
    UserAccountDataSource userAccountDataSource;

    /***
     * bus
     */
    @Inject
    Bus mBus;

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

    /**
     * 数据
     */
    List<ExaminationTaskInfo> taskInfos;

    public MainPresenterImpl(){}

    public MainPresenterImpl(MainActivity mainActivity) {
        dragContent = new DragContentFragment();
        menuView = new MenuFragment();
        workTaskListView = new WorkTaskFragment();
        ((BaseApp)mainActivity.getApplication()).getAppComponent().inject(this);
        this.mainActivity = mainActivity;
        FragmentsUtils.addFragmentToActivity(mainActivity.getSupportFragmentManager(), (Fragment) menuView, R.id.drag_menu);
        FragmentsUtils.addFragmentToActivity(mainActivity.getSupportFragmentManager(), (Fragment) dragContent, R.id.drag_content);
        setDragLayout(dragContent);
        this.initViews();
        refreshData(mainActivity.getIntent());
        mBus.register(this);
    }

    public void initViews() {
        dragContent.setPresenter(this);
        menuView.setPresenter(this);
        workTaskListView.setPresenter(this);
        dragContent.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                dragContent.setAvatarClick(() -> openSlideMenu());
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
                menuView.loadUserAccount(userAccountDataSource.getUserAccount());
            }

            @Override
            public void onDestroy() {

            }
        });
        menuView.setMenuItemSelectListener(position -> {
            shutDownSlideMenu();
            // TODO: 2016/4/27 点击后进入不同的主页视图
            switch (position) {
                case 0:
                    // 作业本
                    dragContent.setTitle("作业本");
                    dragContent.setContentFragment((Fragment) workTaskListView);
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
                    if (CollectionUtils.isEmpty(taskInfos)) {
                        throw new ArrayIndexOutOfBoundsException("任务列表为空");
                    }
                    if (position >= taskInfos.size()) {
                        throw new ArrayIndexOutOfBoundsException("超出任务列表长度");
                    }
                    gotoPreviewTask(position);
                });
                refreshWorkTask();
            }

            @Override
            public void onDestroy() {

            }
        });
    }

    protected void workTaskResult(ExaminationTaskListInfo result) {
        // TODO: 2016/6/27 更新列表
        if (result.isIsSuccess()) {
            taskInfos = result.getData();
            workTaskListView.listData(taskInfos);
        } else {
            ToastUtil.show(mainActivity,result.getMessage());
        }
    }

    public void shutDownSlideMenu() {
        mainActivity.dl.close();
    }

    public void openSlideMenu() {
        mainActivity.dl.open();
    }

    protected void setDragLayout(DragCallBack callBack) {
        mainActivity.dl.setDragListener(callBack);
    }

    public void gotoPreviewTask(int position) {
        mainActivity.startActivity(new Intent(mainActivity, PagerActivity.class)
                .putExtra(Constant.PAGER_TYPE, PagerType.PREVIEW_TASK.getType())
                .putExtra(Constant.TASK_INFO,taskInfos.get(position)));
    }

    @Override
    public void remove() {
        mBus.unregister(this);
    }

    @Override
    public void refreshData(Intent data) {

    }

    @Override
    public void back() {

    }

    @Subscribe
    public void onRefreshEvent(RefreshListEvent refreshListEvent) {
        refreshList();
    }

    @Override
    public void refreshList() {
        Logger.i("menuview current idx ---->" +menuView.getCurrentIdx());
        switch (menuView.getCurrentIdx()) {
            case 0:
                // 作业本
                refreshWorkTask();
                break;
        }
    }

    protected void refreshWorkTask() {
        BaseCallBack<ExaminationTaskListInfo> callBack = new BaseCallBack<ExaminationTaskListInfo>(mainActivity) {
            @Override
            public void response(ExaminationTaskListInfo response) {
                workTaskResult(response);
            }

            @Override
            public void err() {
                workTaskListView.cancelRefresh();
            }
        };
        workTaskListView.setRefreshListener(() -> workTaskDataSource.request(callBack.setContext(mainActivity)));
        workTaskListView.startRefresh();
        workTaskDataSource.request(callBack);
    }
}
