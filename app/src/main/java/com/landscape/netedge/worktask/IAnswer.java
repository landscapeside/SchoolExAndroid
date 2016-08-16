package com.landscape.netedge.worktask;

import com.edge.annotation.NetBegin;
import com.edge.annotation.NetEnd;
import com.landscape.netedge.BaseEdge;
import com.landscape.schoolexandroid.mode.BaseBean;
import com.landscape.schoolexandroid.mode.account.UserFile;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.landscape.schoolexandroid.mode.worktask.QuestionInfo;

/**
 * Created by 1 on 2016/7/1.
 */
public interface IAnswer extends BaseEdge {
    @NetBegin
    void finish();
    @NetEnd
    void finishResult(BaseBean result);
    @NetBegin
    void uploadFile();
    @NetEnd
    void uploadSuc(UserFile result);

    @NetBegin
    void submitAnswer(boolean finish);
    @NetEnd
    void submitAnswerSuc();

}
