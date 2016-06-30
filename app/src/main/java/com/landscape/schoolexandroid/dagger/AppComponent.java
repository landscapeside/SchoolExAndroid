package com.landscape.schoolexandroid.dagger;

import com.landscape.schoolexandroid.common.BaseApp;
import com.landscape.schoolexandroid.presenter.home.MainPresenterImpl;
import com.landscape.schoolexandroid.presenter.worktask.AnswerPresenterImpl;
import com.landscape.schoolexandroid.presenter.worktask.PreviewTaskPresenterImpl;
import com.landscape.schoolexandroid.ui.activity.LoginActivity;
import com.landscape.schoolexandroid.ui.activity.SplashActivity;

import javax.inject.Singleton;

import dagger.Component;


/**
 * AppComponent
 * Created by 1 on 15-8-26.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(BaseApp baseApp);
    void inject(SplashActivity splashActivity);
    void inject(LoginActivity loginActivity);
    void inject(MainPresenterImpl mainPresenter);
    void inject(PreviewTaskPresenterImpl previewTaskPresenter);
    void inject(AnswerPresenterImpl answerPresenter);
}
