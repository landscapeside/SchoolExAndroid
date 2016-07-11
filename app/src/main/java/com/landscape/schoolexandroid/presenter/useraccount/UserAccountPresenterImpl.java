package com.landscape.schoolexandroid.presenter.useraccount;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;

import com.landscape.event.FinishPagerEvent;
import com.landscape.event.RefreshUserEvent;
import com.landscape.netedge.account.IUser;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.api.BaseCallBack;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.account.UserAccountDataSource;
import com.landscape.schoolexandroid.enums.PagerType;
import com.landscape.schoolexandroid.mode.BaseBean;
import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.landscape.schoolexandroid.mode.account.UserFile;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.useraccount.UserAccountFragment;
import com.landscape.schoolexandroid.utils.PhotoHelper;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.useraccount.UserAccountView;
import com.orhanobut.logger.Logger;
import com.squareup.otto.Bus;
import com.tu.crop.BitmapUtil;
import com.tu.crop.CropHelper;
import com.utils.behavior.FragmentsUtils;
import com.utils.behavior.ToastUtil;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by 1 on 2016/7/7.
 */
public class UserAccountPresenterImpl implements BasePresenter,PhotoHelper.PhotoCallbk,IUser {

    @Inject
    UserAccountDataSource userAccountDataSource;
    @Inject
    Bus mBus;

    UserAccountView userAccountView;

    PhotoHelper photoHelper;

    IUser mOptions;
    Bitmap simpleBitmap = null;
    File picFile = null;
    String Photo;

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
        mOptions = (IUser) pagerActivity.mProxy.createProxyInstance(this);
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
        pagerActivity.startActivity(new Intent(pagerActivity,PagerActivity.class)
                .putExtra(Constant.PAGER_TYPE, PagerType.FEEDBK.getType()));
    }

    @Override
    public void onPhotoCropped(Uri uri) {
        try {
            picFile = new File(BitmapUtil.saveFile(pagerActivity, CropHelper.decodeUriAsBitmap(pagerActivity,uri)));
            mOptions.uploadFile();
        } catch (IOException e) {
            Logger.e(e.getMessage());
        }

        PhotoHelper.loadImageIntoSubcriberView(uri);
    }

    @Override
    public void netErr() {

    }

    @Override
    public void uploadFile() {
        userAccountDataSource.uploadFile(picFile, new BaseCallBack<UserFile>(pagerActivity) {
            @Override
            public void response(UserFile response) {
                mOptions.uploadSuc(response);
            }

            @Override
            public void err() {
                mOptions.netErr();
            }
        });
    }

    @Override
    public void uploadSuc(UserFile result) {
        if (result.isIsSuccess()) {
            PhotoHelper.loadImageIntoSubcriberView(Uri.fromFile(picFile));
            mOptions.updateUser(result.getData());
        } else {
            ToastUtil.show(pagerActivity,result.getMessage());
        }
    }

    @Override
    public void updateUser(String Photo) {
        this.Photo = Photo;
        userAccountDataSource.updateStudent(Photo, new BaseCallBack<BaseBean>(pagerActivity) {
            @Override
            public void response(BaseBean response) {
                mOptions.updateSuc(response);
            }

            @Override
            public void err() {
                mOptions.netErr();
            }
        });
    }

    @Override
    public void updateSuc(BaseBean result) {
        if (result.isIsSuccess()) {
            UserAccount userAccount = userAccountDataSource.getUserAccount();
            userAccount.getData().setPhoto(Photo);
            userAccountDataSource.saveUserAccount(userAccount);
            mBus.post(new RefreshUserEvent());
        } else {
            ToastUtil.show(pagerActivity,result.getMessage());
        }
    }
}
