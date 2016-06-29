package com.landscape.netedge.worktask;

import com.edge.annotation.NetBegin;
import com.edge.annotation.NetEnd;
import com.landscape.netedge.BaseEdge;
import com.landscape.schoolexandroid.mode.worktask.ExaminationPaperListInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;

/**
 * Created by 1 on 2016/6/28.
 */
public interface IWorkTask extends BaseEdge {
    @NetBegin
    void getExaminationPaper(ExaminationTaskInfo taskInfo);
    @NetEnd
    void paperResult(ExaminationPaperListInfo result);

}
