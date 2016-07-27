package com.landscape.schoolexandroid.ui.fragment.worktask.answercard;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.landscape.schoolexandroid.R;
import com.landscape.schoolexandroid.common.BaseFragment;
import com.landscape.schoolexandroid.constant.Constant;
import com.landscape.schoolexandroid.enums.PagerType;
import com.landscape.schoolexandroid.mode.worktask.AnswerType;
import com.landscape.schoolexandroid.mode.worktask.StudentAnswer;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.activity.PagerActivity;
import com.landscape.schoolexandroid.utils.PhotoHelper;
import com.landscape.schoolexandroid.views.worktask.answercard.EditRichView;
import com.landscape.schoolexandroid.views.worktask.answercard.EditSimpleView;
import com.landscape.weight.AvatarImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 1 on 2016/7/18.
 */
public class EditRichFragment extends BaseFragment implements EditRichView<BasePresenter> {

    final String strFormat = "<img src=\"%s\"/>";

    DataChangeListener changeListener;
    StudentAnswer studentAnswer;
    AnswerType type = null;
    EditTextWatcher textWatcher = null;
    boolean initFlag = false;
    Map<String,String> imageMap = new HashMap<>();

    @Bind(R.id.ll_pics)
    View llPics;
    @Bind(R.id.img_pic)
    AvatarImageView imgPic;
    @Bind(R.id.edit_content)
    EditText editContent;


    @Override
    public int getLayoutResId() {
        return getResId();
    }

    @Override
    public int getResId() {
        return R.layout.item_answer_edit_pager;
    }

    @Override
    public void build(AnswerType type, StudentAnswer studentanswer, Map<String,String> localeImageMap) {
        studentAnswer = studentanswer;
        llPics.setVisibility(View.VISIBLE);
        this.type = type;
        imageMap = localeImageMap;
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
                    if (studentAnswer == null) {
                        studentAnswer = new StudentAnswer();
                        studentAnswer.Id = type.getId();
                        studentAnswer.TypeId = type.getTypeId();
                    }
                    if (!TextUtils.isEmpty((String) tag)) {
                        String imgAnswer = String.format(strFormat, (String) tag);
                        studentAnswer.Answer = editContent.getText().toString() + imgAnswer;
                    }
                    if (changeListener != null) {
                        changeListener.onDataChanged(studentAnswer);
                    }
                }
                if (key == R.id.image_file_path && !TextUtils.isEmpty((String) tag)) {
                    localeImageMap.put(type.getId(), (String) tag);
                }
            }
        });
        initFlag = true;
        editContent.setText("");
    }

    @Override
    public void hideSoftKeyBord() {
        if (getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editContent.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    @Override
    public void setDataChangeListener(DataChangeListener listener) {
        changeListener = listener;
    }

    @OnClick(R.id.icon_camera)
    public void camera(View view) {
        PhotoHelper photoHelper = PhotoHelper.getInstance();
        PhotoHelper.subcriberView = imgPic;
        photoHelper.takePhoto(getActivity());
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
        Intent intent = new Intent(getActivity(), PagerActivity.class);
        intent.putExtra(Constant.PIC_IS_HTTP, false);
        intent.putExtra(Constant.PIC_PATH, file.getAbsolutePath());
        intent.putExtra(Constant.PAGER_TYPE, PagerType.SHOW_PIC.getType());
        PhotoHelper.showPic(getActivity(), intent);
    }

    private void showHttpPic(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        PhotoHelper.thumbDrawable = imgPic.getDrawable();
        Intent intent = new Intent(getActivity(), PagerActivity.class);
        intent.putExtra(Constant.PIC_IS_HTTP, true);
        intent.putExtra(Constant.PIC_PATH, url);
        intent.putExtra(Constant.PAGER_TYPE, PagerType.SHOW_PIC.getType());
        PhotoHelper.showPic(getActivity(), intent);
    }

    class EditTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            editContent.removeTextChangedListener(this);
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
                if (changeListener != null) {
                    changeListener.onDataChanged(studentAnswer);
                }
            } else {
                /**
                 * 设置默认值
                 */
                if (studentAnswer != null) {

                    if (!TextUtils.isEmpty(imageMap.get(type.getId()))) {
                        imgPic.setTag(R.id.image_file_path, imageMap.get(type.getId()));
                        imgPic.setImageBitmap(BitmapFactory.decodeFile(imageMap.get(type.getId())));
                    } else if (studentAnswer.Answer.contains("<img")) {
                        String imgAnswer = studentAnswer.Answer.substring(studentAnswer.Answer.indexOf("<img src=\""));
                        imgAnswer = imgAnswer.replace("<img src=\"", "").replace("\"/>", "");
                        Picasso.with(getActivity()).load(imgAnswer).into(imgPic);
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
}
