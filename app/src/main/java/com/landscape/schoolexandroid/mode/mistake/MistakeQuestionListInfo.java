package com.landscape.schoolexandroid.mode.mistake;

import com.landscape.schoolexandroid.mode.BaseBean;

import java.util.List;

/**
 * Created by 1 on 2016/7/12.
 */
public class MistakeQuestionListInfo {


    /**
     * $id : 2
     * StudentQuestionsTasksID : 28401
     * ExaminationPapersID : 1706
     * ExaminationPapersName : 7-6物理组题测试
     * Count : 5
     * ErrorList : [{"$id":"3","querstionId":342398,"sortId":1},{"$id":"4","querstionId":339303,"sortId":3},{"$id":"5","querstionId":338759,"sortId":4},{"$id":"6","querstionId":201231,"sortId":4},{"$id":"7","querstionId":200436,"sortId":5}]
     */

    private int StudentQuestionsTasksID;
    private int ExaminationPapersID;
    private String ExaminationPapersName;
    private int Count;
    private List<MistakeQuestionInfo> ErrorList;

    public int getStudentQuestionsTasksID() {
        return StudentQuestionsTasksID;
    }

    public void setStudentQuestionsTasksID(int StudentQuestionsTasksID) {
        this.StudentQuestionsTasksID = StudentQuestionsTasksID;
    }

    public int getExaminationPapersID() {
        return ExaminationPapersID;
    }

    public void setExaminationPapersID(int ExaminationPapersID) {
        this.ExaminationPapersID = ExaminationPapersID;
    }

    public String getExaminationPapersName() {
        return ExaminationPapersName;
    }

    public void setExaminationPapersName(String ExaminationPapersName) {
        this.ExaminationPapersName = ExaminationPapersName;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int Count) {
        this.Count = Count;
    }

    public List<MistakeQuestionInfo> getErrorList() {
        return ErrorList;
    }

    public void setErrorList(List<MistakeQuestionInfo> errorList) {
        ErrorList = errorList;
    }
}
