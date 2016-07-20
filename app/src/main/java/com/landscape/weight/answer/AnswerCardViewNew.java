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

}
