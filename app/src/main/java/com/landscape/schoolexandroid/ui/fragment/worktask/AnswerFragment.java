package com.landscape.schoolexandroid.ui.fragment.worktask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.common.BaseWebFragment;
import com.landscape.schoolexandroid.mode.worktask.QuestionInfo;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.worktask.AnswerView;
import com.landscape.slidinguppanel.SlidingUpPanelLayout;
import com.landscape.slidinguppanel.WrapSlidingDrawer;
import com.landscape.weight.AnswerCardView;
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
    TimeCounterCallbk timeCounterCallbk;
    Subscription subscription = null;

    @Bind(R.id.tv_time_left)
    TextView tvTime;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.ll_infos)
    View llInfos;
    @Bind(R.id.slidingDrawer)
    WrapSlidingDrawer slidingDrawer;
    @Bind(R.id.handlebg)
    ImageView handlebg;
    @Bind(R.id.content)
    AnswerCardView answerCardView;

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public int getResId() {
        return R.layout.view_answer;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webViewLayout.setTouchListener(() -> slidingDrawer.close());
        slidingDrawer.setOnDrawerOpenListener(() -> handlebg.setImageResource(R.drawable.icon_answer_handle_open));
        slidingDrawer.setOnDrawerCloseListener(() -> handlebg.setImageResource(R.drawable.icon_answer_handle_close));
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
        if (slidingDrawer.isShown()) {
            slidingDrawer.close();
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

    @Override
    public void setAnswerCard(QuestionInfo info) {
        answerCardView.loadAnswerCards(info);
    }

    @Override
    public boolean isAnswerChanged() {
        return answerCardView.isChanged();
    }

    @Override
    public String getAnswer() {
        return answerCardView.getAnswer();
    }

    private void tick(int time) {
        tvTime.setText(TimeConversion.getHourMinSecondsData(time * 1000));
    }

    private void tickComplete() {
        if (timeCounterCallbk != null) {
            timeCounterCallbk.timeOut();
        }
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
    public void setTimeCounterCallbk(TimeCounterCallbk timeCounterCallbk) {
        this.timeCounterCallbk = timeCounterCallbk;
    }

    @Override
    public void setFlingListener(FlingRelativeLayout.FlingListener flingListener) {
        webViewLayout.setFlingListener(flingListener);
    }
}
