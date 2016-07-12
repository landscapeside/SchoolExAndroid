package com.landscape.schoolexandroid.presenter.home;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.jsware.draglayout.DragCallBack;
import com.landscape.event.RefreshUserEvent;
import com.landscape.event.RefreshListEvent;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.api.BaseCallBack;
import com.landscape.schoolexandroid.common.AppConfig;
import com.landscape.schoolexandroid.common.BaseActivity;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.landscape.schoolexandroid.datasource.home.MistakeDataSource;
import com.landscape.schoolexandroid.datasource.home.WorkTaskDataSource;
import com.landscape.schoolexandroid.enums.PagerType;
import com.landscape.schoolexandroid.enums.TaskStatus;
import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.landscape.schoolexandroid.mode.mistake.MistakeInfo;
import com.landscape.schoolexandroid.mode.mistake.MistakeListInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskListInfo;
import com.landscape.schoolexandroid.ui.activity.MainActivity;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.home.DragContentFragment;
import com.landscape.schoolexandroid.ui.fragment.home.MenuFragment;
import com.landscape.schoolexandroid.ui.fragment.home.MistakeFragment;
import com.landscape.schoolexandroid.ui.fragment.home.WorkTaskFragment;
import com.landscape.schoolexandroid.ui.fragment.worktask.PreviewTaskFragment;
import com.landscape.schoolexandroid.utils.WorkTaskHelper;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.home.BaseListView;
import com.landscape.schoolexandroid.views.home.DragContentView;
import com.landscape.schoolexandroid.views.home.MenuView;
import com.landscape.schoolexandroid.views.home.MistakeListView;
import com.landscape.schoolexandroid.views.home.WorkTaskListView;
import com.landscape.schoolexandroid.views.worktask.PreviewTaskView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.utils.behavior.FragmentsUtils;
import com.utils.behavior.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016/4/26.
 */
public class MainPresenterImpl implements MainPresenter {
    /**
     * ===============================
     * const
     * ===============================
     */
    protected static final String collectUrlFormat = "studentCollect/StudentCollectList?studentid=%s";

    /**
     * ===============================
     * module-layer
     * ===============================
     */
    @Inject
    WorkTaskDataSource workTaskDataSource;
    @Inject
    UserAccountDataSource userAccountDataSource;
    @Inject
    MistakeDataSource mistakeDataSource;

    /***
     * bus
     */
    @Inject
    protected Bus mBus;

    /**
     * ===============================
     * V-layer
     * ===============================
     */
    BaseActivity mainActivity;
    DragContentView dragContent;
    MenuView menuView;
    MistakeListView mistakeListView;

    /**
     * 内容视图
     * */
    WorkTaskListView workTaskListView;
    PreviewTaskView collectView;

    /**
     * 数据
     */
    List<ExaminationTaskInfo> taskInfos;
    List<MistakeInfo> mistakeInfos;
    List<UserAccount.DataBean.SubjectTypeBean> subjectTypes = new ArrayList<>();
    List<UserAccount.DataBean.ExaminationPapersTypeBean> paperTypes = new ArrayList<>();
    List<TaskStatus> taskStatusList = new ArrayList<>();
    UserAccount.DataBean.SubjectTypeBean workTaskSubjectType,mistakeSubjectType;
    UserAccount.DataBean.ExaminationPapersTypeBean workTaskExamPaperType,mistakeExamPaperType;
    TaskStatus workTaskStatus,mistakeStatus;

    public MainPresenterImpl(){}

    public MainPresenterImpl(BaseActivity mainActivity) {
        dragContent = new DragContentFragment();
        menuView = new MenuFragment();
        workTaskListView = new WorkTaskFragment();
        collectView = new PreviewTaskFragment();
        mistakeListView  = new MistakeFragment();
        ((BaseApp)mainActivity.getApplication()).getAppComponent().inject(this);
        initFilters();
        this.mainActivity = mainActivity;
        mBus.register(this);
        setDragLayout(dragContent);
        this.initViews();
        refreshData(mainActivity.getIntent());
        attachToActivity();
    }

    protected void initFilters() {
        subjectTypes = userAccountDataSource.getUserAccount().getData().getSubjectType();
        subjectTypes = WorkTaskHelper.getValidSubjectType(subjectTypes);
        paperTypes = userAccountDataSource.getUserAccount().getData().getExaminationPapersType();
        taskStatusList = TaskStatus.getStatusFilters();

        subjectTypes = WorkTaskHelper.addDefSubjectType(subjectTypes);
        paperTypes = WorkTaskHelper.addDefPaperType(paperTypes);
    }

    protected void attachToActivity() {
        FragmentsUtils.addFragmentToActivity(mainActivity.getSupportFragmentManager(), (Fragment) menuView, R.id.drag_menu);
        FragmentsUtils.addFragmentToActivity(mainActivity.getSupportFragmentManager(), (Fragment) dragContent, R.id.drag_content);
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
                menuView.setUserAccountListener(() -> toUserCenter());
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
                    dragContent.setContentFragment((Fragment) mistakeListView);
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
                    dragContent.setContentFragment((Fragment) collectView);
                    break;
            }
        });

        /**
         * 作业本
         */
        workTaskListView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                workTaskListView.subjectFilter(subjectTypes);
                workTaskListView.typeFilter(paperTypes);
                workTaskListView.stateFilter(taskStatusList);
                workTaskListView.setOnFilterSelector(new BaseListView.OnFilterSelector() {
                    @Override
                    public void onSubjectSelect(UserAccount.DataBean.SubjectTypeBean subjectType) {
                        workTaskSubjectType = subjectType;
                        refreshWorkTask();
                    }

                    @Override
                    public void onTypeSelect(UserAccount.DataBean.ExaminationPapersTypeBean paperMode) {
                        workTaskExamPaperType = paperMode;
                        refreshWorkTask();
                    }

                    @Override
                    public void onStateSelect(TaskStatus status) {
                        workTaskStatus = status;
                        refreshWorkTask();
                    }
                });
                workTaskListView.setListItemSelectListener(taskInfo -> {
                    if (taskInfo == null) {
                        throw new IllegalArgumentException("任务对象为空");
                    }
                    gotoPreviewTask(taskInfo);
                });
                refreshWorkTask();
            }

            @Override
            public void onDestroy() {

            }
        });

        /**
         * 错题本
         * */
        mistakeListView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                mistakeListView.subjectFilter(subjectTypes);
                mistakeListView.typeFilter(paperTypes);
                mistakeListView.stateFilter(taskStatusList);
                mistakeListView.setOnFilterSelector(new BaseListView.OnFilterSelector() {
                    @Override
                    public void onSubjectSelect(UserAccount.DataBean.SubjectTypeBean subjectType) {
                        workTaskSubjectType = subjectType;
                        refreshMistake();
                    }

                    @Override
                    public void onTypeSelect(UserAccount.DataBean.ExaminationPapersTypeBean paperMode) {
                        workTaskExamPaperType = paperMode;
                        refreshMistake();
                    }

                    @Override
                    public void onStateSelect(TaskStatus status) {
                        workTaskStatus = status;
                        refreshMistake();
                    }
                });
                mistakeListView.setListItemSelectListener(mistakeInfo -> {
                    if (mistakeInfo == null) {
                        throw new IllegalArgumentException("错题对象为空");
                    }
                    gotoPreviewMistake(mistakeInfo);
                });
                refreshMistake();
            }

            @Override
            public void onDestroy() {

            }
        });


        /**
         * 收藏
         */
        collectView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                collectView.startEnable(false);
                collectView.previewTask(
                        AppConfig.BASE_WEB_URL +
                        String.format(collectUrlFormat,
                                userAccountDataSource.getUserAccount().getData().getStudentId()));
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
            workTaskListView.listWork(taskInfos);
        } else {
            ToastUtil.show(mainActivity,result.getMessage());
        }
    }

    public void shutDownSlideMenu() {
        ((MainActivity)mainActivity).dl.close();
    }

    public void openSlideMenu() {
        ((MainActivity)mainActivity).dl.open();
    }

    protected void setDragLayout(DragCallBack callBack) {
        if (mainActivity instanceof MainActivity) {
            ((MainActivity)mainActivity).dl.setDragListener(callBack);
        }
    }

    public void gotoPreviewTask(ExaminationTaskInfo taskInfo) {
        mainActivity.startActivity(new Intent(mainActivity, PagerActivity.class)
                .putExtra(Constant.PAGER_TYPE, PagerType.PREVIEW_TASK.getType())
                .putExtra(Constant.TASK_INFO,taskInfo));
    }

    public void gotoPreviewMistake(MistakeInfo mistakeInfo) {
        mainActivity.startActivity(new Intent(mainActivity, PagerActivity.class)
                .putExtra(Constant.PAGER_TYPE, PagerType.PREVIEW_MISTAKE.getType())
                .putExtra(Constant.MISTAKE_INFO,mistakeInfo));
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

    public void toUserCenter() {
        mainActivity.startActivity(new Intent(mainActivity,PagerActivity.class)
                .putExtra(Constant.PAGER_TYPE, PagerType.USER_CENTER.getType()));
    }

    @Subscribe
    public void onRefreshEvent(RefreshListEvent refreshListEvent) {
        refreshList();
    }

    @Subscribe
    public void onRefreshAvatar(RefreshUserEvent refreshAvatarEvent) {
        menuView.loadUserAccount(userAccountDataSource.getUserAccount());
    }

    @Override
    public void refreshList() {
        switch (menuView.getCurrentIdx()) {
            case 0:
                // 作业本
                refreshWorkTask();
                break;
            case 1:
                // 错题本
                refreshMistake();
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
        workTaskListView.setRefreshListener(() -> {
            workTaskDataSource.request(
                    workTaskSubjectType==null?null:workTaskSubjectType.getId(),
                    workTaskExamPaperType == null?null:workTaskExamPaperType.getId(),
                    workTaskStatus==null?null:workTaskStatus.getStatus(),
                    callBack.setContext(mainActivity));
        });
        workTaskListView.startRefresh();
        workTaskDataSource.request(
                workTaskSubjectType==null?null:workTaskSubjectType.getId(),
                workTaskExamPaperType == null?null:workTaskExamPaperType.getId(),
                workTaskStatus==null?null:workTaskStatus.getStatus(),
                callBack);
    }

    protected void refreshMistake(){
        BaseCallBack<MistakeListInfo> callBack = new BaseCallBack<MistakeListInfo>(mainActivity) {
            @Override
            public void response(MistakeListInfo response) {
                mistakeResult(response);
            }

            @Override
            public void err() {
                mistakeListView.cancelRefresh();
            }
        };
        mistakeListView.setRefreshListener(() -> {
            mistakeDataSource.request(
                    workTaskSubjectType==null?null:workTaskSubjectType.getId(),
                    workTaskExamPaperType == null?null:workTaskExamPaperType.getId(),
                    workTaskStatus==null?null:workTaskStatus.getStatus(),
                    callBack.setContext(mainActivity));
        });
        mistakeListView.startRefresh();
        mistakeDataSource.request(
                workTaskSubjectType==null?null:workTaskSubjectType.getId(),
                workTaskExamPaperType == null?null:workTaskExamPaperType.getId(),
                workTaskStatus==null?null:workTaskStatus.getStatus(),
                callBack);
    }

    protected void mistakeResult(MistakeListInfo result) {
        if (result.isIsSuccess()) {
            mistakeInfos = result.getData();
            mistakeListView.listMistake(mistakeInfos);
        } else {
            ToastUtil.show(mainActivity,result.getMessage());
        }
    }
}
