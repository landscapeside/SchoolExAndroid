package com.landscape.netedge.mistake;

import com.edge.annotation.NetBegin;
import com.edge.annotation.NetEnd;
import com.landscape.netedge.BaseEdge;
import com.landscape.schoolexandroid.mode.mistake.MistakeQuestionBean;

/**
 * Created by 1 on 2016/7/12.
 */
public interface IMistake extends BaseEdge {
    @NetBegin
    void getQuestion(int taskId);
    @NetEnd
    void getQuestionSuc(MistakeQuestionBean result);
}
