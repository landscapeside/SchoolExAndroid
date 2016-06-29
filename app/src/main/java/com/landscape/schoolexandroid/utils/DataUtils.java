package com.landscape.schoolexandroid.utils;

import com.landscape.schoolexandroid.mode.worktask.QuestionInfo;
import com.utils.datahelper.JSONS;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by landscape on 2016/6/29.
 */
public class DataUtils {

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


}
