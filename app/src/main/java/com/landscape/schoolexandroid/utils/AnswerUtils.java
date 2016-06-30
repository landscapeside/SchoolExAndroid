package com.landscape.schoolexandroid.utils;

import com.landscape.schoolexandroid.mode.worktask.QuestionInfo;
import com.utils.datahelper.CollectionUtils;
import com.utils.datahelper.JSONS;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by landscape on 2016/6/29.
 */
public class AnswerUtils {

    public static final int QUESTION_DONE = 1;
    public static final int QUESTION_UNDO = 2;

    public static List<Integer> transforStudentsAnswers(List<QuestionInfo> questionInfos) {
        List<Integer> result = new ArrayList<>();
        for (QuestionInfo info : questionInfos) {
            if (JSONS.parseJsonArray(info.getStudentsAnswer()) == null
                    || JSONS.parseJsonArray(info.getStudentsAnswer()).size() == 0) {
                // TODO: 2016/6/29 学生答案为空
                result.add(QUESTION_UNDO);
            } else {
                // TODO: 2016/6/29 该题已做
                result.add(QUESTION_DONE);
            }
        }
        return result;
    }

    public static int firstUndoQuestion(List<QuestionInfo> questionInfos) {
        int result = 0;
        if (CollectionUtils.isEmpty(questionInfos)) {
            throw new IllegalArgumentException("试题列表为空");
        }
        for (int i = 0;i<questionInfos.size();i++) {
            QuestionInfo info = questionInfos.get(i);
            if (JSONS.parseJsonArray(info.getStudentsAnswer()) == null
                    || JSONS.parseJsonArray(info.getStudentsAnswer()).size() == 0) {
                // TODO: 2016/6/29 学生答案为空
                return i;
            }
        }
        return result;
    }

}
