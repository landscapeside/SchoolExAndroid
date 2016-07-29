package com.landscape.schoolexandroid.presenter.worktask;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.landscape.schoolexandroid.common.BaseActivity;
import com.landscape.schoolexandroid.enums.CardType;
import com.landscape.schoolexandroid.mode.worktask.AlternativeContent;
import com.landscape.schoolexandroid.mode.worktask.AnswerType;
import com.landscape.schoolexandroid.mode.worktask.QuestionInfo;
import com.landscape.schoolexandroid.mode.worktask.StudentAnswer;
import com.landscape.schoolexandroid.presenter.BasePresenter;
import com.landscape.schoolexandroid.ui.fragment.worktask.answercard.DecideFragment;
import com.landscape.schoolexandroid.ui.fragment.worktask.answercard.EditRichFragment;
import com.landscape.schoolexandroid.ui.fragment.worktask.answercard.EditSimpleFragment;
import com.landscape.schoolexandroid.ui.fragment.worktask.answercard.MultiFragment;
import com.landscape.schoolexandroid.ui.fragment.worktask.answercard.SingleFragment;
import com.landscape.schoolexandroid.views.BaseView;
import com.landscape.schoolexandroid.views.worktask.answercard.DecideView;
import com.landscape.schoolexandroid.views.worktask.answercard.EditRichView;
import com.landscape.schoolexandroid.views.worktask.answercard.EditSimpleView;
import com.landscape.schoolexandroid.views.worktask.answercard.MultiView;
import com.landscape.schoolexandroid.views.worktask.answercard.SingleView;
import com.landscape.weight.answer.AnswerCardFragmentAdapter;
import com.utils.datahelper.CollectionUtils;
import com.utils.datahelper.JSONS;
import com.viewpagerindicator.CirclePageIndicator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 1 on 2016/7/18.
 */
public class AnswerCardPresenterImpl implements BasePresenter {

    /**
     * consts
     */
    final int TYPE_SINGLE = 1;
    final int TYPE_MULTI = 2;
    final int TYPE_EDIT = 3;
    final int TYPE_RICH = 4;
    final int TYPE_DECIDE = 5;
    final int TYPE_NONE = 6;

    /**
     * view
     */
    ViewPager pager;
    CirclePageIndicator indicator;

    /**
     * parent
     */
    BaseActivity parentContext;
    /**
     * adapter
     */
    AnswerCardFragmentAdapter adapter;


    /**
     * data
     */
    List<AlternativeContent> alternativeContent = new ArrayList<>();
    List<StudentAnswer> studentAnswers = new ArrayList<>();
    Map<String, StudentAnswer> answerMap = new HashMap<>();
    List<AnswerType> answerTypes = new ArrayList<>();
    Map<Integer, Map<String, String>> imageMap = new HashMap<>();
    QuestionInfo info;
    int SubjectTypeId = 0;

    List<Fragment> cards = new ArrayList<>();

    public AnswerCardPresenterImpl(BaseActivity context, ViewPager pager, CirclePageIndicator indicator) {
        parentContext = context;
        this.pager = pager;
        this.indicator = indicator;
    }


    public void loadAnswerCards(QuestionInfo questionInfo, int SubjectTypeId) {
        this.SubjectTypeId = SubjectTypeId;
        info = questionInfo;
        alternativeContent.clear();
        studentAnswers.clear();
        answerMap.clear();
        cards.clear();
        if (imageMap.get(info.getId()) == null) {
            imageMap.put(info.getId(), new HashMap<>());
        }
        if (!TextUtils.isEmpty(questionInfo.getAlternativeContent())) {
            Type alterType = new TypeToken<ArrayList<AlternativeContent>>() {
            }.getType();
            alternativeContent = JSONS.parseObject(questionInfo.getAlternativeContent(), alterType);
        }
        if (!TextUtils.isEmpty(questionInfo.getStudentsAnswer())) {
            Type studentType = new TypeToken<ArrayList<StudentAnswer>>() {
            }.getType();
            studentAnswers = JSONS.parseObject(questionInfo.getStudentsAnswer(), studentType);
        }
        Type type = new TypeToken<ArrayList<AnswerType>>() {
        }.getType();
        answerTypes = JSONS.parseObject(questionInfo.getAnswerType(), type);
        initAnswerMap();
        buildCardFragments();
        adapter = new AnswerCardFragmentAdapter(parentContext.getSupportFragmentManager(), cards);
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
//        if (adapter == null) {
//
//        } else {
//            adapter.resetData(cards);
//            indicator.notifyDataSetChanged();
//            indicator.setCurrentItem(0);
//        }

    }

    private void initAnswerMap() {
        for (AnswerType answerType : answerTypes) {
            StudentAnswer answer = new StudentAnswer();
            answer.Id = answerType.getId();
            answer.TypeId = answerType.getTypeId();
            answerMap.put(answer.Id, answer);
        }
        for (StudentAnswer studentAnswer : studentAnswers) {
            answerMap.put(studentAnswer.Id, studentAnswer);
        }
    }

    private void buildCardFragments() {
        for (int i = 0; i < getCount(); i++) {
            cards.add(getViewFragment(i));
        }
    }

    public int getCount() {
        return answerTypes.size();
    }

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

    public Fragment getViewFragment(int position) {
        switch (getItemViewType(position)) {
            case TYPE_SINGLE:
                return buildSingle(position);
            case TYPE_MULTI:
                return buildMulti(position);
            case TYPE_EDIT:
                return buildEditSimple(position);
            case TYPE_RICH:
                return buildEditRich(position);
            case TYPE_DECIDE:
                return buildDecide(position);
        }
        return null;
    }

    private Fragment buildSingle(int position) {
        StudentAnswer studentAnswer = answerMap.get(answerTypes.get(position).getId());
        SingleView singleFragment = new SingleFragment();
        singleFragment.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                singleFragment.setDataChangeListener(studentAnswer -> answerMap.put(studentAnswer.Id, studentAnswer));
                singleFragment.build(answerTypes.get(position),alternativeContent,studentAnswer);
            }

            @Override
            public void onDestroy() {

            }
        });
        return (Fragment) singleFragment;
    }

    private Fragment buildMulti(int position) {
        StudentAnswer studentAnswer = answerMap.get(answerTypes.get(position).getId());
        MultiView multiView = new MultiFragment();
        multiView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                multiView.setDataChangeListener(studentAnswer -> answerMap.put(studentAnswer.Id, studentAnswer));
                multiView.build(answerTypes.get(position),alternativeContent,studentAnswer);
            }

            @Override
            public void onDestroy() {

            }
        });
        return (Fragment) multiView;
    }

    private Fragment buildEditSimple(int position) {
        StudentAnswer studentAnswer = answerMap.get(answerTypes.get(position).getId());
        EditSimpleView editSimpleView = new EditSimpleFragment();
        editSimpleView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                editSimpleView.setDataChangeListener(studentAnswer -> answerMap.put(studentAnswer.Id, studentAnswer));
                editSimpleView.build(answerTypes.get(position),studentAnswer);
            }

            @Override
            public void onDestroy() {

            }
        });
        return (Fragment) editSimpleView;
    }

    private Fragment buildEditRich(int position) {
        StudentAnswer studentAnswer = answerMap.get(answerTypes.get(position).getId());
        EditRichView editRichView = new EditRichFragment();
        editRichView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                editRichView.setDataChangeListener(studentAnswer -> answerMap.put(studentAnswer.Id, studentAnswer));
                editRichView.build(answerTypes.get(position),studentAnswer,imageMap.get(info.getId()));
            }

            @Override
            public void onDestroy() {

            }
        });
        return (Fragment) editRichView;
    }

    private Fragment buildDecide(int position) {
        StudentAnswer studentAnswer = answerMap.get(answerTypes.get(position).getId());
        DecideView decideView = new DecideFragment();
        decideView.setLifeListener(new BaseView.ViewLifeListener() {
            @Override
            public void onInitialized() {
                decideView.setDataChangeListener(studentAnswer -> answerMap.put(studentAnswer.Id, studentAnswer));
                decideView.build(answerTypes.get(position),studentAnswer);
            }

            @Override
            public void onDestroy() {

            }
        });
        return (Fragment) decideView;
    }

    /**
     * 答案是否变换
     *
     * @return
     */
    public boolean isChanged() {
        List<StudentAnswer> oldAnswers = new ArrayList<>();
        Map<String, StudentAnswer> oldAnswerMap = new HashMap<>();
        if (!TextUtils.isEmpty(info.getStudentsAnswer())) {
            Type studentType = new TypeToken<ArrayList<StudentAnswer>>() {
            }.getType();
            oldAnswers = JSONS.parseObject(info.getStudentsAnswer(), studentType);
        }
        for (StudentAnswer studentAnswer : oldAnswers) {
            oldAnswerMap.put(studentAnswer.Id, studentAnswer);
        }
        for (AnswerType type : answerTypes) {
            StudentAnswer studentAnswer = answerMap.get(type.getId());
            StudentAnswer oldStudentAnswer = oldAnswerMap.get(type.getId());
            if (studentAnswer != null) {
                if (oldStudentAnswer == null && TextUtils.isEmpty(studentAnswer.Answer)) {
                    continue;
                }
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

    /**
     * 答案内容
     *
     * @return
     */
    public String getAnswer() {
        Set<String> keys = answerMap.keySet();
        List<StudentAnswer> answerList = new ArrayList<>();
        for (String key : keys) {
            answerList.add(answerMap.get(key));
        }
        return JSONS.parseJson(answerList);
    }


    @Override
    public void remove() {

    }

    @Override
    public void refreshData(Intent data) {

    }

    @Override
    public void back() {

    }

    public void hideSoftKeyBord() {
        for (Fragment card : cards) {
            if (card instanceof EditRichFragment) {
                ((EditRichView)card).hideSoftKeyBord();
            }
            if (card instanceof EditSimpleFragment) {
                ((EditSimpleView)card).hideSoftKeyBord();
            }
        }
    }
}
