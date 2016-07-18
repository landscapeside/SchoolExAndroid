package com.landscape.weight.answer;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
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
import com.landscape.schoolexandroid.common.BaseActivity;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.enums.CardType;
import com.landscape.schoolexandroid.enums.PagerType;
import com.landscape.schoolexandroid.mode.worktask.AlternativeContent;
import com.landscape.schoolexandroid.mode.worktask.AnswerType;
import com.landscape.schoolexandroid.mode.worktask.QuestionInfo;
import com.landscape.schoolexandroid.mode.worktask.StudentAnswer;
import com.landscape.schoolexandroid.presenter.worktask.AnswerCardPresenterImpl;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.utils.PhotoHelper;
import com.landscape.weight.AvatarImageView;
import com.squareup.picasso.Picasso;
import com.utils.datahelper.CollectionUtils;
import com.utils.datahelper.JSONS;
import com.utils.system.ScreenParam;
import com.viewpagerindicator.CirclePageIndicator;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by landscape on 2016/7/2.
 */
public class AnswerCardViewNew extends RelativeLayout {

    Context mContext;

    AnswerCardPresenterImpl presenter;


    ViewPager pager;
    CirclePageIndicator indicator;

    CardAdapter adapter = null;

    QuestionInfo info;
    int SubjectTypeId = 0;
    List<AlternativeContent> alternativeContent = new ArrayList<>();
    List<StudentAnswer> studentAnswers = new ArrayList<>();
    Map<String, StudentAnswer> answerMap = new HashMap<>();
    List<AnswerType> answerTypes = new ArrayList<>();
    Map<Integer, Map<String, String>> imageMap = new HashMap<>();

    public AnswerCardViewNew(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_answer_card_new, this, true);
        init();
    }

    private void init() {
        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        presenter = new AnswerCardPresenterImpl((BaseActivity) mContext,pager,indicator);
    }

    public void loadAnswerCards(QuestionInfo questionInfo, int SubjectTypeId) {
        presenter.loadAnswerCards(questionInfo,SubjectTypeId);
    }

    public boolean isChanged() {
        return presenter.isChanged();
    }

    public String getAnswer() {
        return presenter.getAnswer();
    }

    class CardAdapter extends BaseAdapter {

        final int TYPE_SINGLE = 1;
        final int TYPE_MULTI = 2;
        final int TYPE_EDIT = 3;
        final int TYPE_RICH = 4;
        final int TYPE_DECIDE = 5;
        final int TYPE_NONE = 6;

        public void refreshCards() {
            notifyDataSetChanged();
        }

        @Override
        public int getViewTypeCount() {
            return 6;
        }

        @Override
        public int getItemViewType(int position) {
            if (CollectionUtils.isIn(
                    CardType.getType(answerTypes.get(position).getTypeId()),
                    CardType.SINGLE_CHOOSE,
                    CardType.LISTEN_SINGLE_CHOOSE
            )) {
                /**
                 * 单选
                 */
                return TYPE_SINGLE;
            } else if (CollectionUtils.isIn(
                    CardType.getType(answerTypes.get(position).getTypeId()),
                    CardType.DECIDE)) {
                /**
                 * 判断
                 * */
                return TYPE_DECIDE;
            } else if (CollectionUtils.isIn(
                    CardType.getType(answerTypes.get(position).getTypeId()),
                    CardType.MULTI_CHOOSE)) {
                /**
                 * 多选
                 */
                return TYPE_MULTI;
            } else if (CollectionUtils.isIn(
                    CardType.getType(answerTypes.get(position).getTypeId()),
                    CardType.PACK,
                    CardType.LISTEN_PACK)) {
                /**
                 * 填空
                 */
                /*除了英语填空，其他都需要拍照*/
                if (SubjectTypeId != 12) {
                    return TYPE_RICH;
                }
                return TYPE_EDIT;
            } else if (CollectionUtils.isIn(
                    CardType.getType(answerTypes.get(position).getTypeId()),
                    CardType.EXPLAIN)) {
                /**
                 * 解答
                 */
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
                    convertView = buildEditView(convertView, position, false);
                    break;
                case TYPE_RICH:
                    convertView = buildEditView(convertView, position, true);
                    break;
                case TYPE_DECIDE:
                    convertView = buildDecideView(convertView, position);
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
                viewHolder = new SingleViewHolder(convertView);
                convertView.setTag(R.id.list_item_view, viewHolder);
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
                viewHolder = new MultiViewHolder(convertView);
                convertView.setTag(R.id.list_item_view, viewHolder);
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
                viewHolder = new EditViewHolder(convertView);
                convertView.setTag(R.id.list_item_view, viewHolder);
            } else {
                viewHolder = (EditViewHolder) convertView.getTag(R.id.list_item_view);
            }
            viewHolder.build(answerTypes.get(position), imgEnable);
            return convertView;
        }

        private View buildDecideView(View convertView, int position) {
            DecideViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_answer_decide, null);
                viewHolder = new DecideViewHolder(convertView);
                convertView.setTag(R.id.list_item_view, viewHolder);
            } else {
                viewHolder = (DecideViewHolder) convertView.getTag(R.id.list_item_view);
            }
            viewHolder.build(answerTypes.get(position));
            return convertView;
        }

    }

    class DecideViewHolder {
        @Bind(R.id.radio_group)
        RadioGroup group;
        @Bind(R.id.radio_def)
        RadioButton radioDef;
        @Bind(R.id.radio_right)
        RadioButton radioRight;
        @Bind(R.id.radio_err)
        RadioButton radioErr;

        public DecideViewHolder(View container) {
            ButterKnife.bind(this, container);
        }

        public void build(AnswerType type) {
            radioDef.setChecked(true);
            radioRight.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    StudentAnswer studentAnswer = answerMap.get(type.getId());
                    if (studentAnswer == null) {
                        studentAnswer = new StudentAnswer();
                        studentAnswer.Id = type.getId();
                        studentAnswer.TypeId = type.getTypeId();
                    }
                    studentAnswer.Answer = "T";
                    answerMap.put(type.getId(), studentAnswer);
                }
            });
            radioErr.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    StudentAnswer studentAnswer = answerMap.get(type.getId());
                    if (studentAnswer == null) {
                        studentAnswer = new StudentAnswer();
                        studentAnswer.Id = type.getId();
                        studentAnswer.TypeId = type.getTypeId();
                    }
                    studentAnswer.Answer = "F";
                    answerMap.put(type.getId(), studentAnswer);
                }
            });
            StudentAnswer studentAnswer = answerMap.get(type.getId());
            if (studentAnswer != null) {
                if (studentAnswer.Answer.equals("T")) {
                    radioRight.setChecked(true);
                } else {
                    radioErr.setChecked(true);
                }
            }
        }
    }

    class SingleViewHolder {
        @Bind(R.id.tv_index)
        TextView tvIndex;
        @Bind(R.id.radio_group)
        RadioGroup group;

        public SingleViewHolder(View container) {
            ButterKnife.bind(this, container);
        }

        public void build(AnswerType type) {
            tvIndex.setText("" + type.getId());
            group.removeAllViews();
            for (int i = 0; i < alternativeContent.size(); i++) {
                RadioButton radioButton = (RadioButton) View.inflate(mContext, R.layout.view_radio_button, null);
                radioButton.setText(alternativeContent.get(i).Id);
                group.addView(radioButton);
                RadioGroup.LayoutParams layoutParams = (RadioGroup.LayoutParams) radioButton.getLayoutParams();
                layoutParams.setMargins(20, 20, 20, 20);
                layoutParams.width = ScreenParam.dp2px(mContext, 30);
                layoutParams.height = ScreenParam.dp2px(mContext, 30);
                radioButton.setLayoutParams(layoutParams);
                radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        StudentAnswer studentAnswer = answerMap.get(type.getId());
                        if (studentAnswer == null) {
                            studentAnswer = new StudentAnswer();
                            studentAnswer.Id = type.getId();
                            studentAnswer.TypeId = type.getTypeId();
                        }
                        studentAnswer.Answer = radioButton.getText().toString();
                        answerMap.put(type.getId(), studentAnswer);
                    }
                });
            }
            StudentAnswer studentAnswer = answerMap.get(type.getId());
            if (studentAnswer != null) {
                for (int i = 0; i < alternativeContent.size(); i++) {
                    if (alternativeContent.get(i).Id.equals(studentAnswer.Answer)) {
                        RadioButton radioButton = (RadioButton) group.getChildAt(i);
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        }
    }

    class MultiViewHolder {

        @Bind(R.id.tv_index)
        TextView tvIndex;
        @Bind(R.id.multi_content)
        LinearLayout multiContent;

        public MultiViewHolder(View container) {
            ButterKnife.bind(this, container);
        }

        public void build(AnswerType type) {
            tvIndex.setText("" + type.getId());
            multiContent.removeAllViews();
            for (int i = 0; i < alternativeContent.size(); i++) {
                TextView radioButton = (TextView) View.inflate(mContext, R.layout.view_multi_button, null);
                radioButton.setText(alternativeContent.get(i).Id);
                radioButton.setOnClickListener(v -> {
                    v.setSelected(!v.isSelected());
                    StudentAnswer studentAnswer = answerMap.get(type.getId());
                    if (studentAnswer == null) {
                        studentAnswer = new StudentAnswer();
                        studentAnswer.Id = type.getId();
                        studentAnswer.TypeId = type.getTypeId();
                    }
                    if (v.isSelected()) {
                        if (TextUtils.isEmpty(studentAnswer.Answer)) {
                            studentAnswer.Answer = radioButton.getText().toString();
                        } else {
                            String[] answers = studentAnswer.Answer.split(",");
                            String[] newAnswers = new String[answers.length + 1];
                            System.arraycopy(answers, 0, newAnswers, 0, answers.length);
                            newAnswers[newAnswers.length - 1] = radioButton.getText().toString();
                            ArrayList<String> answerList = new ArrayList<>(Arrays.asList(newAnswers));
                            Collections.sort(answerList);
                            newAnswers = answerList.toArray(new String[0]);
                            studentAnswer.Answer = StringUtils.join(newAnswers, ",");
                        }
                    } else {
                        if (!TextUtils.isEmpty(studentAnswer.Answer)) {
                            String[] answers = studentAnswer.Answer.split(",");
                            ArrayList<String> answerList = new ArrayList<>(Arrays.asList(answers));
                            for (int i1 = 0; i1 < answerList.size(); i1++) {
                                if (answerList.get(i1).equals(radioButton.getText().toString())) {
                                    answerList.remove(i1);
                                    break;
                                }
                            }
                            String[] newAnswers = answerList.toArray(new String[0]);
                            studentAnswer.Answer = StringUtils.join(newAnswers, ",");
                        }
                    }
                    answerMap.put(type.getId(), studentAnswer);
                });
                multiContent.addView(radioButton);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) radioButton.getLayoutParams();
                layoutParams.setMargins(20, 20, 20, 20);
                layoutParams.width = ScreenParam.dp2px(mContext, 30);
                layoutParams.height = ScreenParam.dp2px(mContext, 30);
                radioButton.setLayoutParams(layoutParams);
            }
            StudentAnswer studentAnswer = answerMap.get(type.getId());
            if (studentAnswer != null) {
                for (int i = 0; i < alternativeContent.size(); i++) {
                    if (studentAnswer.Answer.contains(alternativeContent.get(i).Id)) {
                        TextView radioButton = (TextView) multiContent.getChildAt(i);
                        radioButton.setSelected(true);
                    }
                }
            }

        }

    }

    class EditViewHolder {

        boolean initFlag = false;
        AnswerType type = null;
        EditTextWatcher textWatcher = null;

        @Bind(R.id.tv_index)
        TextView tvIndex;
        @Bind(R.id.ll_pics)
        View llPics;
        @Bind(R.id.img_pic)
        AvatarImageView imgPic;
        @Bind(R.id.edit_content)
        EditText editContent;

        public EditViewHolder(View contariner) {
            ButterKnife.bind(this, contariner);
        }

        public void build(AnswerType type, boolean picEnable) {
            this.type = type;
            tvIndex.setText(type.getId());
            final String strFormat = "<img src=\"%s\"/>";
            llPics.setVisibility(picEnable ? VISIBLE : GONE);
            imgPic.setImageResource(R.color.transparent);
            imgPic.setTag(R.id.image_file_path, "");
            if (textWatcher != null) {
                editContent.removeTextChangedListener(textWatcher);
            }
            textWatcher = new EditTextWatcher();
            editContent.addTextChangedListener(textWatcher);
            imgPic.setTAGChangeListener(new AvatarImageView.TAGChangeListener() {
                @Override
                public void tagChanged(Object tag) {

                }

                @Override
                public void tagChanged(int key, Object tag) {
                    if (key == R.id.image_url && !initFlag) {
                        StudentAnswer studentAnswer = answerMap.get(type.getId());
                        if (studentAnswer == null) {
                            studentAnswer = new StudentAnswer();
                            studentAnswer.Id = type.getId();
                            studentAnswer.TypeId = type.getTypeId();
                        }
                        if (!TextUtils.isEmpty((String) tag)) {
                            String imgAnswer = String.format(strFormat, (String) tag);
                            studentAnswer.Answer = editContent.getText().toString() + imgAnswer;
                        }
                        answerMap.put(type.getId(), studentAnswer);
                    }
                    if (key == R.id.image_file_path && !TextUtils.isEmpty((String) tag)) {
                        imageMap.get(info.getId()).put(type.getId(), (String) tag);
                    }
                }
            });
            initFlag = true;
            editContent.setText("");
        }

        class EditTextWatcher implements TextWatcher {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editContent.removeTextChangedListener(this);
                StudentAnswer studentAnswer = answerMap.get(type.getId());
                if (studentAnswer == null) {
                    studentAnswer = new StudentAnswer();
                    studentAnswer.Id = type.getId();
                    studentAnswer.TypeId = type.getTypeId();
                }
                if (!initFlag) {
                    if (studentAnswer.Answer.contains("<img")) {
                        String imgAnswer = studentAnswer.Answer.substring(studentAnswer.Answer.indexOf("<img"));
                        studentAnswer.Answer = editContent.getText().toString() + imgAnswer;
                    } else {
                        studentAnswer.Answer = editContent.getText().toString();
                    }
                    answerMap.put(type.getId(), studentAnswer);
                } else {
                    /**
                     * 设置默认值
                     */
                    if (studentAnswer != null) {

                        if (!TextUtils.isEmpty(imageMap.get(info.getId()).get(type.getId()))) {
                            imgPic.setTag(R.id.image_file_path, imageMap.get(info.getId()).get(type.getId()));
                            imgPic.setImageBitmap(BitmapFactory.decodeFile(imageMap.get(info.getId()).get(type.getId())));
                        } else if (studentAnswer.Answer.contains("<img")) {
                            String imgAnswer = studentAnswer.Answer.substring(studentAnswer.Answer.indexOf("<img src=\""));
                            imgAnswer = imgAnswer.replace("<img src=\"", "").replace("\"/>", "");
                            Picasso.with(mContext).load(imgAnswer).into(imgPic);
                            imgPic.setTag(R.id.image_url, imgAnswer);
                        }

                        if (studentAnswer.Answer.contains("<img")) {
                            String editAnswer = studentAnswer.Answer.substring(0, studentAnswer.Answer.indexOf("<img"));
                            editContent.setText(editAnswer);
                        } else {
                            if (!TextUtils.isEmpty(studentAnswer.Answer)) {
                                editContent.setText(studentAnswer.Answer);
                            }
                        }
                    }
                    initFlag = false;
                }
                editContent.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }

        @OnClick(R.id.icon_camera)
        public void camera(View view) {
            PhotoHelper photoHelper = PhotoHelper.getInstance();
            PhotoHelper.subcriberView = imgPic;
            photoHelper.takePhoto(mContext);
        }

        @OnClick(R.id.img_pic)
        public void showPic(View view) {
            // TODO: 2016/7/3 查看图片
            String filePath = (String) imgPic.getTag(R.id.image_file_path);
            PhotoHelper.subcriberView = imgPic;
            if (TextUtils.isEmpty(filePath)) {
                showHttpPic((String) imgPic.getTag(R.id.image_url));
                return;
            }
            File file = new File(filePath);
            if (!file.exists()) {
                showHttpPic((String) imgPic.getTag(R.id.image_url));
                return;
            }
            PhotoHelper.thumbDrawable = imgPic.getDrawable();
            Intent intent = new Intent(mContext, PagerActivity.class);
            intent.putExtra(Constant.PIC_IS_HTTP, false);
            intent.putExtra(Constant.PIC_PATH, file.getAbsolutePath());
            intent.putExtra(Constant.PAGER_TYPE, PagerType.SHOW_PIC.getType());
            PhotoHelper.showPic(mContext, intent);
        }

        private void showHttpPic(String url) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            PhotoHelper.thumbDrawable = imgPic.getDrawable();
            Intent intent = new Intent(mContext, PagerActivity.class);
            intent.putExtra(Constant.PIC_IS_HTTP, true);
            intent.putExtra(Constant.PIC_PATH, url);
            intent.putExtra(Constant.PAGER_TYPE, PagerType.SHOW_PIC.getType());
            PhotoHelper.showPic(mContext, intent);
        }
    }

}
