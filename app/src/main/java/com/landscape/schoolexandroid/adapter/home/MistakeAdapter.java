package com.landscape.schoolexandroid.adapter.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.adapter.SectionedBaseAdapter;
import com.landscape.schoolexandroid.enums.SubjectType;
import com.landscape.schoolexandroid.enums.TaskStatus;
import com.landscape.schoolexandroid.mode.mistake.MistakeInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.landscape.schoolexandroid.utils.MistakeHelper;
import com.utils.datahelper.TimeConversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 1 on 2016/7/11.
 */
public class MistakeAdapter extends SectionedBaseAdapter {

    Context mContext;

    private final static int ITEM_TYPE_COUNT = 1;

    private final static int TYPE_MISTAKE = 0;
    private final static int TYPE_TITLE = 1;

    Map<String, List<MistakeInfo>> data = new HashMap<>();
    List<String> dates = new ArrayList<>();

    public MistakeAdapter(Context context, List<MistakeInfo> data) {
        mContext = context;
        this.data = MistakeHelper.sortMistakeByDate(data);
        dates = MistakeHelper.sortDate(this.data.keySet());

    }

    public void replaceAll(List<MistakeInfo> data) {
        this.data.clear();
        dates.clear();
        this.data = MistakeHelper.sortMistakeByDate(data);
        dates = MistakeHelper.sortDate(this.data.keySet());
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int section, int position) {
        return position;
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return dates.size();
    }

    @Override
    public int getCountForSection(int section) {
        return data.get(dates.get(section)).size();
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        if (position >= getCount()) {
            return null;
        }
        int type = getItemViewType(section, position);
        TaskViewHolder viewHolder = null;
        if (convertView == null || ((Integer) convertView.getTag(R.id.list_item_type)) != type) {
            switch (type) {
                case TYPE_MISTAKE:
                    viewHolder = new TaskViewHolder();
                    convertView = viewHolder.mItemView;
                    break;
            }
            convertView.setTag(R.id.list_item_type, type);
            convertView.setTag(R.id.list_item_view, viewHolder);
        } else {
            switch (type) {
                case TYPE_MISTAKE:
                    viewHolder = (TaskViewHolder) convertView.getTag(R.id.list_item_view);
                    break;
            }
        }
        viewHolder.bindData(section,position);
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        TitleViewHolder viewHolder = null;
        if (convertView == null || ((Integer) convertView.getTag(R.id.list_item_type)) != TYPE_TITLE) {
            convertView = View.inflate(mContext, R.layout.item_task_title, null);
            viewHolder = new TitleViewHolder(convertView);
            convertView.setTag(R.id.list_item_type, TYPE_TITLE);
            convertView.setTag(R.id.list_item_view, viewHolder);
        } else {
            viewHolder = (TitleViewHolder) convertView.getTag(R.id.list_item_view);
        }
        viewHolder.bindData(section);
        return convertView;
    }

    class TitleViewHolder {
        @Bind(R.id.tv_date)
        TextView tvDate;

        public TitleViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bindData(int position) {
            tvDate.setText(dates.get(position));
        }
    }

    public class TaskViewHolder {

        static final String errCountFormat = "做错%s题";

        public View mItemView;

        @Bind(R.id.icon_class)
        ImageView iconClass;
        @Bind(R.id.iv_arrow)
        ImageView ivArrow;
        @Bind(R.id.tv_state)
        TextView tvState;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_finish_time)
        TextView tvFinishTime;

        public TaskViewHolder() {
            mItemView = View.inflate(mContext, R.layout.item_task, null);
            ButterKnife.bind(this, mItemView);
        }

        public void bindData(int section,int position) {
            MistakeInfo info = data.get(dates.get(section)).get(position);
            tvName.setText(info.getName());
            tvFinishTime.setText("错题时间：" + TimeConversion.getData(TimeConversion.getDurationWithGMT(info.getDateTime())));
            tvFinishTime.setVisibility(View.GONE);
            tvState.setText(String.format(errCountFormat,info.getErrorCount()));
            tvState.setTextColor(mContext.getResources().getColor(R.color.state_red));
            iconClass.setImageResource(SubjectType.getSubjectType(info.getSubjectTypeName()).getDrawableResId());
            mItemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick((MistakeInfo) v.getTag(R.id.list_item_obj));
                }
            });
            mItemView.setTag(R.id.list_item_obj,info);
        }

    }

    public interface OnItemClickListener{
        void onItemClick(MistakeInfo info);
    }
    OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
