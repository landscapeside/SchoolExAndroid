package com.landscape.schoolexandroid.views.home;

import com.landscape.schoolexandroid.mode.mistake.MistakeInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.BaseView;

import java.util.List;

/**
 * Created by 1 on 2016/6/22.
 */
public interface MistakeListView<T extends BasePresenter> extends BaseListView<T> {

    void listMistake(List<MistakeInfo> listData);

    interface OnListItemSelectListener{
        void onSelect(MistakeInfo mistakeInfo);
    }

    void setListItemSelectListener(OnListItemSelectListener listener);

}
