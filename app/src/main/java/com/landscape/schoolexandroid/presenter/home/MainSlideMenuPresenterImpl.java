package com.landscape.schoolexandroid.presenter.home;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.enums.PagerType;
import com.landscape.schoolexandroid.ui.activity.MainActivity;
import com.landscape.schoolexandroid.ui.activity.MainSlideMenuActivity;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.home.DragContentFragment;
import com.landscape.schoolexandroid.ui.fragment.home.MenuFragment;
import com.landscape.schoolexandroid.ui.fragment.home.WorkTaskFragment;
import com.landscape.schoolexandroid.views.BaseView;
import com.utils.behavior.FragmentsUtils;

/**
 * Created by 1 on 2016/7/5.
 */
public class MainSlideMenuPresenterImpl extends MainPresenterImpl {

    MainSlideMenuActivity mainSlideMenuActivity;

    SlidingMenu mSlideMenu;

    public MainSlideMenuPresenterImpl(MainSlideMenuActivity mainSlideMenuActivity) {
        this.mainSlideMenuActivity = mainSlideMenuActivity;

        dragContent = new DragContentFragment();
        menuView = new MenuFragment();
        workTaskListView = new WorkTaskFragment();
        ((BaseApp)mainSlideMenuActivity.getApplication()).getAppComponent().inject(this);
        FragmentsUtils.addFragmentToActivity(mainSlideMenuActivity.getSupportFragmentManager(), (Fragment) dragContent, R.id.main_slide_content);
        initSlideMenu();
        initViews();
        refreshData(mainSlideMenuActivity.getIntent());
        mBus.register(this);
    }

    private void initSlideMenu() {
        mSlideMenu = new SlidingMenu(mainSlideMenuActivity);
        mSlideMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        mSlideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 设置滑动菜单视图的宽度
        mSlideMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        mSlideMenu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        //把滑动菜单添加进所有的Activity中，可选值SLIDING_CONTENT ， SLIDING_WINDOW
        mSlideMenu.attachToActivity(mainSlideMenuActivity, SlidingMenu.SLIDING_WINDOW);
        mSlideMenu.setMenu(R.layout.menu_setting);
        //为侧滑菜单设置布局
        FragmentsUtils.addFragmentToActivityStateLoss(mainSlideMenuActivity.getSupportFragmentManager(), (Fragment) menuView, R.id.setting_content);
    }

    @Override
    public void shutDownSlideMenu() {
        if (mSlideMenu.isMenuShowing()) {
            mSlideMenu.toggle();
        }
    }

    @Override
    public void openSlideMenu() {
        mSlideMenu.showMenu();
    }

    @Override
    public void gotoPreviewTask(int position) {
        mainSlideMenuActivity.startActivity(new Intent(mainSlideMenuActivity, PagerActivity.class)
                .putExtra(Constant.PAGER_TYPE, PagerType.PREVIEW_TASK.getType())
                .putExtra(Constant.TASK_INFO,taskInfos.get(position)));
    }
}
