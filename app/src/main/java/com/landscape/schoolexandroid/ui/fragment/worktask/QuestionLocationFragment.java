package com.landscape.schoolexandroid.ui.fragment.worktask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.utils.AnswerUtils;
import com.landscape.schoolexandroid.views.worktask.QuestionLocationView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by landscape on 2016/6/29.
 */
public class QuestionLocationFragment extends BaseFragment implements QuestionLocationView<BasePresenter> {

    int currentIdx = 0;

    QuickAdapter adapter = null;
    @Bind(R.id.grid_location)
    GridView gridLocation;

    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public void listData(int current, List<Integer> locations) {
        this.currentIdx = current;
        if (adapter == null) {
            adapter = new QuickAdapter<Integer>(getActivity(), R.layout.item_location, locations) {
                @Override
                protected void convert(BaseAdapterHelper helper, Integer item) {
                    int position = helper.getPosition();
                    TextView tvLocation = helper.getView(R.id.tv_location);
                    tvLocation.setText("" + (position+1));
//                    tvLocation.setSelected(item == AnswerUtils.QUESTION_DONE);
//                    tvLocation.setSelected(true);
                    if (position == currentIdx) {
                        tvLocation.setTextColor(getResources().getColor(R.color.white));
                        tvLocation.setBackgroundResource(R.drawable.btn_badge_solid_gray);
                    } else {
                        if (item == AnswerUtils.QUESTION_DONE) {
                            tvLocation.setTextColor(getResources().getColor(R.color.white));
                            tvLocation.setBackgroundResource(R.drawable.btn_badge_solid_green);
                        } else {
                            tvLocation.setTextColor(getResources().getColorStateList(R.color.location_text_selector));
                            tvLocation.setBackgroundResource(R.drawable.badge_selector);
                        }
                    }
//                    if (position == currentIdx) {
//                        tvLocation.setBackgroundResource(R.drawable.btn_badge_solid_green);
//                    } else {
//                        tvLocation.setBackgroundResource(R.drawable.badge_selector);
//                        tvLocation.setSelected(item == AnswerUtils.QUESTION_DONE);
//                    }
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
