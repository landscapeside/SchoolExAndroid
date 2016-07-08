package com.landscape.schoolexandroid.presenter.useraccount;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.landscape.schoolexandroid.enums.PagerType;
import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.useraccount.UserAccountFragment;
import com.landscape.schoolexandroid.utils.PhotoHelper;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.useraccount.UserAccountView;
import com.utils.behavior.FragmentsUtils;

import javax.inject.Inject;

/**
 * Created by 1 on 2016/7/7.
 */
public class UserAccountPresenterImpl implements BasePresenter,PhotoHelper.PhotoCallbk {

    @Inject
    UserAccountDataSource userAccountDataSource;

    UserAccountView userAccountView;

    PhotoHelper photoHelper;

    /**
     * parent
     */
    PagerActivity pagerActivity;

    public UserAccountPresenterImpl(PagerActivity pagerActivity) {
        this.pagerActivity = pagerActivity;
        ((BaseApp) pagerActivity.getApplication()).getAppComponent().inject(this);
        pagerActivity.setToolbarTitle("个人中心");
        pagerActivity.showRightImgBtn(this::FeedBk);
        userAccountView = new UserAccountFragment();
        FragmentsUtils.addFragmentToActivity(pagerActivity.getSupportFragmentManager(), (Fragment) userAccountView, R.id.contentPanel);
        initViews();
        photoHelper = PhotoHelper.getInstance();
        photoHelper.setPhotoCallbk(this);
    }

    public void initViews() {
        userAccountView.setPresenter(this);
        userAccountView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                userAccountView.refreshUserInfo(userAccountDataSource.getUserAccount());
                userAccountView.setLvSmall(
                        userAccountDataSource.getFlowerLvSmall(
                                userAccountDataSource.getUserAccount().getData().getFlower()));
                userAccountView.setLvNormal(
                        userAccountDataSource.getFlowerLvNormal(
                                userAccountDataSource.getUserAccount().getData().getFlower()));
                userAccountView.setLvBig(
                        userAccountDataSource.getFlowerLvBig(
                                userAccountDataSource.getUserAccount().getData().getFlower()));
                userAccountView.setBtnClickListener(new UserAccountView.BtnClickListener() {
                    @Override
                    public void avatar() {
                        PhotoHelper photoHelper = PhotoHelper.getInstance();
                        photoHelper.takePhoto(pagerActivity);
                    }

                    @Override
                    public void userModity() {
                        pagerActivity.startActivity(new Intent(pagerActivity,PagerActivity.class)
                                .putExtra(Constant.PAGER_TYPE, PagerType.USER_MODIFY.getType()));
                    }

                    @Override
                    public void flowerChart() {
                        pagerActivity.startActivity(new Intent(pagerActivity,PagerActivity.class)
                                .putExtra(Constant.PAGER_TITLE,"红花排行榜")
                                .putExtra(Constant.CHART_TYPE,ChartPresenterImpl.FLOWERS)
                                .putExtra(Constant.PAGER_TYPE, PagerType.CHART.getType()));
                    }

                    @Override
                    public void wonderChart() {
                        pagerActivity.startActivity(new Intent(pagerActivity,PagerActivity.class)
                                .putExtra(Constant.PAGER_TITLE,"点赞排行榜")
                                .putExtra(Constant.CHART_TYPE,ChartPresenterImpl.WONDER)
                                .putExtra(Constant.PAGER_TYPE, PagerType.CHART.getType()));
                    }
                });
            }

            @Override
            public void onDestroy() {

            }
        });
    }

    @Override
    public void remove() {

    }

    @Override
    public void refreshData(Intent data) {

    }

    @Override
    public void back() {
        pagerActivity.finish();
    }

    private void FeedBk(View view) {

    }

    @Override
    public void onPhotoCropped(Uri uri) {
        PhotoHelper.loadImageIntoSubcriberView(uri);
    }
}
