package com.landscape.weight;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.enums.CardType;
import com.landscape.schoolexandroid.mode.worktask.AnswerType;
import com.landscape.schoolexandroid.mode.worktask.QuestionInfo;
import com.landscape.schoolexandroid.utils.PhotoHelper;
import com.utils.behavior.AppFileUtils;
import com.utils.datahelper.CollectionUtils;
import com.utils.datahelper.JSONS;
import com.utils.system.ScreenParam;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by landscape on 2016/7/2.
 */
public class AnswerCardView extends RelativeLayout {

    Context mContext;

    ListView listCard;
    ImageView iconIndicator;

    CardAdapter adapter = null;

    QuestionInfo info;
    List<AlternativeContent> alternativeContent = new ArrayList<>();
    List<StudentAnswer> studentAnswers = new ArrayList<>();
    Map<String, StudentAnswer> answerMap = new HashMap<>();
    List<AnswerType> answerTypes = new ArrayList<>();

    public AnswerCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_answer_card, this, true);
        init();
    }

    private void init() {
        listCard = (ListView) findViewById(R.id.list_card);
        iconIndicator = (ImageView) findViewById(R.id.icon_indicator);
        listCard.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (listCard.getFirstVisiblePosition() == 0 && adapter.getCount() > 1) {
                            // TODO: 2016/7/3 显示向下的图标
                        } else if (adapter.getCount() > 1 && listCard.getLastVisiblePosition() == (adapter.getCount() - 1)) {
                            // TODO: 2016/7/3 显示向上的图标
                        } else {
                            // TODO: 2016/7/3 不显示图标
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    public void loadAnswerCards(QuestionInfo questionInfo) {
        info = questionInfo;
        alternativeContent.clear();
        studentAnswers.clear();
        answerMap.clear();
        if (!TextUtils.isEmpty(questionInfo.getAlternativeContent())) {
            Type alterType = new TypeToken<ArrayList<AlternativeContent>>() {}.getType();
            alternativeContent = JSONS.parseObject(questionInfo.getAlternativeContent(), alterType);
        }
        if (!TextUtils.isEmpty(questionInfo.getStudentsAnswer())) {
            Type studentType = new TypeToken<ArrayList<StudentAnswer>>() {}.getType();
            studentAnswers = JSONS.parseObject(questionInfo.getStudentsAnswer(), studentType);
        }
        for (StudentAnswer studentAnswer : studentAnswers) {
            answerMap.put(studentAnswer.Id, studentAnswer);
        }
        Type type = new TypeToken<ArrayList<AnswerType>>() {}.getType();
        answerTypes = JSONS.parseObject(questionInfo.getAnswerType(), type);
        if (adapter == null) {
            adapter = new CardAdapter();
            listCard.setAdapter(adapter);
            adapter.refreshCards();
        } else {
            adapter.refreshCards();
        }
    }

    // TODO: 2016/7/3 答案是否变换
    public boolean isChanged() {
        List<StudentAnswer> oldAnswers = new ArrayList<>();
        Map<String, StudentAnswer> oldAnswerMap = new HashMap<>();
        if (!TextUtils.isEmpty(info.getStudentsAnswer())) {
            Type studentType = new TypeToken<ArrayList<StudentAnswer>>() {}.getType();
            oldAnswers = JSONS.parseObject(info.getStudentsAnswer(), studentType);
        }
        for (StudentAnswer studentAnswer : oldAnswers) {
            oldAnswerMap.put(studentAnswer.Id, studentAnswer);
        }
        for (AnswerType type : answerTypes) {
            StudentAnswer studentAnswer = answerMap.get(type.getId());
            StudentAnswer oldStudentAnswer = oldAnswerMap.get(type.getId());
            if (studentAnswer != null) {
                if (oldStudentAnswer == null) {
                    return true;
                }
                if (!studentAnswer.Answer.equals(oldStudentAnswer.Answer)) {
                    return true;
                }
            }
        }
        return false;
    }

    // TODO: 2016/7/3 答案内容
    public String getAnswer() {
        return "";
    }

    class CardAdapter extends BaseAdapter{

        final int TYPE_SINGLE = 1;
        final int TYPE_MULTI = 2;
        final int TYPE_EDIT = 3;
        final int TYPE_RICH= 4;
        final int TYPE_NONE = 5;

        public void refreshCards() {
            notifyDataSetChanged();
        }

        @Override
        public int getViewTypeCount() {
            return 5;
        }

        @Override
        public int getItemViewType(int position) {
            if (CollectionUtils.isIn(
                    CardType.getType(answerTypes.get(position).getTypeId()),
                    CardType.SINGLE_CHOOSE,
                    CardType.DECIDE,
                    CardType.LISTEN_SINGLE_CHOOSE
            )) {
                // TODO: 2016/7/2 单选
                return TYPE_SINGLE;
            } else if (CollectionUtils.isIn(
                    CardType.getType(answerTypes.get(position).getTypeId()),
                    CardType.MULTI_CHOOSE,
                    CardType.LISTEN_MULTI_CHOOSE)) {
                // TODO: 2016/7/2 多选
                return TYPE_MULTI;
            } else if (CollectionUtils.isIn(
                    CardType.getType(answerTypes.get(position).getTypeId()),
                    CardType.PACK)){
                // TODO: 2016/7/2 填空
                return TYPE_EDIT;
            } else if (CollectionUtils.isIn(
                    CardType.getType(answerTypes.get(position).getTypeId()),
                    CardType.EXPLAIN)) {
                // TODO: 2016/7/2 解答
                return TYPE_RICH;
            }
            return TYPE_NONE;
        }

        @Override
        public int getCount() {
            return answerTypes.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (getItemViewType(position)) {
                case TYPE_SINGLE:
                    convertView = buildSingleView(convertView, position);
                    break;
                case TYPE_MULTI:
                    convertView = buildMultiView(convertView, position);
                    break;
                case TYPE_EDIT:
                    convertView = buildEditView(convertView, position,false);
                    break;
                case TYPE_RICH:
                    convertView = buildEditView(convertView, position,true);
                    break;
                default:

                    break;
            }
            return convertView;
        }

        private View buildSingleView(View convertView, int position) {
            SingleViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_answer_single, null);
                viewHolder  = new SingleViewHolder(convertView);
                convertView.setTag(R.id.list_item_view,viewHolder);
            } else {
                viewHolder = (SingleViewHolder) convertView.getTag(R.id.list_item_view);
            }
            viewHolder.build(answerTypes.get(position));
            return convertView;
        }
        private View buildMultiView(View convertView, int position) {
            MultiViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_answer_multi, null);
                viewHolder  = new MultiViewHolder(convertView);
                convertView.setTag(R.id.list_item_view,viewHolder);
            } else {
                viewHolder = (MultiViewHolder) convertView.getTag(R.id.list_item_view);
            }
            viewHolder.build(answerTypes.get(position));
            return convertView;
        }

        private View buildEditView(View convertView, int position, boolean imgEnable) {
            EditViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_answer_edit, null);
                viewHolder  = new EditViewHolder(convertView);
                convertView.setTag(R.id.list_item_view,viewHolder);
            } else {
                viewHolder = (EditViewHolder) convertView.getTag(R.id.list_item_view);
            }
            viewHolder.build(answerTypes.get(position),imgEnable);
            return convertView;
        }

    }

    class AlternativeContent{
        public String Id;
        public String Content;
    }

    class StudentAnswer{
        public String Answer;
        public String Id;
    }

    class SingleViewHolder{

        @Bind(R.id.radio_group)
        RadioGroup group;

        public SingleViewHolder(View container) {
            ButterKnife.bind(this,container);
        }

        public void build(AnswerType type){
            group.removeAllViews();
            for (int i = 0;i<alternativeContent.size();i++) {
                RadioButton radioButton = (RadioButton) View.inflate(mContext, R.layout.view_radio_button, null);
                radioButton.setText(alternativeContent.get(i).Id);
                group.addView(radioButton);
                RadioGroup.LayoutParams layoutParams = (RadioGroup.LayoutParams) radioButton.getLayoutParams();
                layoutParams.setMargins(20,20,20,20);
                layoutParams.width = ScreenParam.dp2px(mContext, 30);
                layoutParams.height = ScreenParam.dp2px(mContext, 30);
                radioButton.setLayoutParams(layoutParams);
                radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        StudentAnswer studentAnswer = answerMap.get(type.getId());
                        if (studentAnswer == null) {
                            studentAnswer = new StudentAnswer();
                            studentAnswer.Id = type.getId();
                        }
                        studentAnswer.Answer = radioButton.getText().toString();
                        answerMap.put(type.getId(), studentAnswer);
                    }
                });
            }
            StudentAnswer studentAnswer = answerMap.get(type.getId());
            if (studentAnswer != null) {
                for (int i= 0;i<alternativeContent.size();i++) {
                    if (alternativeContent.get(i).Id.equals(studentAnswer.Answer)) {
                        RadioButton radioButton = (RadioButton) group.getChildAt(i);
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        }
    }

    class MultiViewHolder{

        @Bind(R.id.multi_content)
        LinearLayout multiContent;

        public MultiViewHolder(View container) {
            ButterKnife.bind(this, container);
        }

        public void build(AnswerType type) {
            multiContent.removeAllViews();
            for (int i = 0;i<alternativeContent.size();i++) {
                TextView radioButton = (TextView) View.inflate(mContext, R.layout.view_multi_button, null);
                radioButton.setText(alternativeContent.get(i).Id);
                radioButton.setOnClickListener(v -> {
                    v.setSelected(!v.isSelected());
                    if (v.isSelected()) {
                        StudentAnswer studentAnswer = answerMap.get(type.getId());
                        if (studentAnswer == null) {
                            studentAnswer = new StudentAnswer();
                            studentAnswer.Id = type.getId();
                        }
                        // TODO: 2016/7/3 更改学生答案
                        answerMap.put(type.getId(), studentAnswer);
                    }
                });
                multiContent.addView(radioButton);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) radioButton.getLayoutParams();
                layoutParams.setMargins(20,20,20,20);
                layoutParams.width = ScreenParam.dp2px(mContext, 30);
                layoutParams.height = ScreenParam.dp2px(mContext, 30);
                radioButton.setLayoutParams(layoutParams);
            }
            StudentAnswer studentAnswer = answerMap.get(type.getId());
            if (studentAnswer != null) {
                for (int i= 0;i<alternativeContent.size();i++) {
                    if (studentAnswer.Answer.contains(alternativeContent.get(i).Id)) {
                        TextView radioButton = (TextView) multiContent.getChildAt(i);
                        radioButton.setSelected(true);
                    }
                }
            }

        }

    }

    class EditViewHolder{

        @Bind(R.id.ll_pics)
        View llPics;
        @Bind(R.id.img_pic)
        ImageView imgPic;
        @Bind(R.id.edit_content)
        EditText editContent;

        public EditViewHolder(View contariner) {
            ButterKnife.bind(this, contariner);
        }

        public void build(AnswerType type,boolean picEnable) {
            llPics.setVisibility(picEnable?VISIBLE:GONE);
            editContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        @OnClick(R.id.icon_camera)
        public void camera(View view) {
            PhotoHelper.takePhoto(mContext);
        }

        @OnClick(R.id.img_pic)
        public void showPic(View view) {
            // TODO: 2016/7/3 查看图片
            PhotoHelper.showPics(mContext, null);
        }

    }

}
