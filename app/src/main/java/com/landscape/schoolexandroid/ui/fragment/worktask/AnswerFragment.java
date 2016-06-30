package com.landscape.schoolexandroid.ui.fragment.worktask;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.common.BaseWebFragment;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.worktask.AnswerView;
import com.landscape.weight.FlingRelativeLayout;
import com.orhanobut.logger.Logger;
import com.utils.datahelper.RxCounter;
import com.utils.datahelper.TimeConversion;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by 1 on 2016/6/30.
 */
public class AnswerFragment extends BaseWebFragment implements AnswerView<BasePresenter> {

    String url = "";
    BtnClickListener btnClickListener;
    Subscription subscription = null;

    @Bind(R.id.tv_time_left)
    TextView tvTime;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.ll_infos)
    View llInfos;

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public int getResId() {
        return R.layout.view_answer;
    }

    @Override
    public void refresh() {
        previewTask(url);
    }

    @Override
    public void previewTask(String url) {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("H5地址不能为空");
        }
        Logger.i(url);
        this.url = url;
        mWebView.loadUrl(url);
    }

    @Override
    public void startTimeTick(int startTime) {
        int seconds = startTime * 60;
        if (subscription != null) {
            subscription.unsubscribe();
        }
        subscription = RxCounter.counter(seconds, 0)
                .doOnNext(this::tick)
                .doOnCompleted(this::tickComplete)
                .subscribe();
    }

    @Override
    public void setLocation(int idx,int total) {
        tvLocation.setText(idx+"/"+total);
    }

    @Override
    public void setEnable(boolean isEnable) {
        llInfos.setVisibility(isEnable?View.VISIBLE:View.GONE);
    }

    private void tick(int time) {
        tvTime.setText(TimeConversion.getHourMinSecondsData(time * 1000));
    }

    private void tickComplete() {

    }

    @OnClick(R.id.ll_finish)
    void finish(View view) {
        //TODO
        if (checkAnswer() && btnClickListener != null) {
            btnClickListener.finish();
        }
    }

    @OnClick(R.id.ll_location)
    void location(View view) {
        //TODO
        if (btnClickListener != null) {
            btnClickListener.location();
        }
    }

    private boolean checkAnswer() {

        return true;
    }

    @Override
    public void onDestroyView() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroyView();
    }

    @Override
    public void setBtnClickListener(BtnClickListener btnClickListener) {
        this.btnClickListener = btnClickListener;
    }

    @Override
    public void setFlingListener(FlingRelativeLayout.FlingListener flingListener) {
        webViewLayout.setFlingListener(flingListener);
    }
}
