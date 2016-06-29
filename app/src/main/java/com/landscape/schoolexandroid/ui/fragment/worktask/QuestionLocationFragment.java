package com.landscape.schoolexandroid.ui.fragment.worktask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.views.worktask.QuestionLocationView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by landscape on 2016/6/29.
 */
public class QuestionLocationFragment extends BaseFragment implements QuestionLocationView<BasePresenter> {

    QuickAdapter adapter = null;
    @Bind(R.id.grid_location)
    GridView gridLocation;

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public void listData(List<Integer> locations) {
        if (adapter == null) {
            adapter = new QuickAdapter<Integer>(getActivity(),R.layout.item_location,locations) {
                @Override
                protected void convert(BaseAdapterHelper helper, Integer item) {
                    int position = helper.getPosition();
                    helper.setText(R.id.tv_location, "" + position);
                }
            };
            gridLocation.setAdapter(adapter);
        } else {
            adapter.replaceAll(locations);
        }
    }

    @Override
    public void setListItemSelectListener(OnListItemSelectListener listener) {
        gridLocation.setOnItemClickListener((parent, view, position, id) -> listener.onSelect(position));
    }

    @Override
    public int getResId() {
        return R.layout.view_question_location;
    }
}
