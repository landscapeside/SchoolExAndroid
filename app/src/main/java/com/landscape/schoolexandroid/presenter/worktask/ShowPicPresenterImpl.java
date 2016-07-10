package com.landscape.schoolexandroid.presenter.worktask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.api.FileCallBack;
import com.landscape.schoolexandroid.api.RetrofitService;
import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.datasource.worktask.TaskOptionDataSource;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.ui.fragment.worktask.ShowPicFragment;
import com.landscape.schoolexandroid.utils.PhotoHelper;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.worktask.ShowPicView;
import com.tu.crop.CropHelper;
import com.utils.behavior.FragmentsUtils;
import com.utils.behavior.ProgressUtils;
import com.utils.behavior.ToastUtil;

import java.io.File;

import javax.inject.Inject;

import okhttp3.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by landscape on 2016/7/9.
 */
public class ShowPicPresenterImpl implements BasePresenter {

    @Inject
    TaskOptionDataSource taskOptionDataSource;

    ShowPicView showPicView;

    boolean isHttp = false;
    String picPath = "";
    File downLoadFile = null;

    /**
     * parent
     */
    PagerActivity pagerActivity;

    public ShowPicPresenterImpl(PagerActivity pagerActivity) {
        this.pagerActivity = pagerActivity;
        ((BaseApp) pagerActivity.getApplication()).getAppComponent().inject(this);
        showPicView = new ShowPicFragment();
        pagerActivity.setToolbarTitle("查看图片");
        FragmentsUtils.addFragmentToActivity(pagerActivity.getSupportFragmentManager(), (Fragment) showPicView, R.id.contentPanel);
        picPath = pagerActivity.getIntent().getStringExtra(Constant.PIC_PATH);
        isHttp = pagerActivity.getIntent().getBooleanExtra(Constant.PIC_IS_HTTP, false);
        initViews();
    }

    public void initViews() {
        showPicView.setPresenter(this);
        showPicView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                if (isHttp) {
                    ProgressUtils.showProgressDialog(pagerActivity, "", () -> {
                        RetrofitService.cancel();
                        if (downLoadFile.exists()) {
                            downLoadFile.delete();
                        }
                    });
                    downLoadFile = new File(CropHelper.getCropFilePath(pagerActivity));
                    showPicView.showThumb(PhotoHelper.thumbDrawable);
                    taskOptionDataSource
                            .downloadFile(pagerActivity, picPath, downLoadFile)
                            .doOnNext(o -> {
                                ProgressUtils.dismissProgressDialog();
                                showPicView.showPic(Drawable.createFromPath(downLoadFile.getAbsolutePath()));
                            })
                            .doOnError(throwable -> {
                                ProgressUtils.dismissProgressDialog();
                                ToastUtil.show(pagerActivity, "文件下载失败");
                            }).onErrorResumeNext(Observable.empty())
                            .subscribe();
                } else {
                    showPicView.showPic(Drawable.createFromPath(picPath));
                }
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
        Intent data = new Intent();
        if (isHttp && downLoadFile.exists()) {
            data.putExtra(Constant.PIC_PATH, downLoadFile.getAbsolutePath());
            pagerActivity.setResult(Activity.RESULT_OK,data);
        } else {
            pagerActivity.setResult(Activity.RESULT_CANCELED);
        }
        pagerActivity.finish();
    }
}
