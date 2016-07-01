package com.landscape.schoolexandroid.views.worktask;

import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.BaseView;

import java.util.List;

/**
 * Created by landscape on 2016/6/29.
 */
public interface QuestionLocationView<T extends BasePresenter> extends BaseView<T> {

    void listData(int current,List<Integer> locations);

    interface OnListItemSelectListener{
        void onSelect(int position);
    }

    void setListItemSelectListener(OnListItemSelectListener listener);

}
