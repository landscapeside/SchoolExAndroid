package com.landscape.schoolexandroid.mode.worktask;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 1 on 2016/6/28.
 */
public class QuestionInfo implements Parcelable {


    /**
     * $id : 4
     * Id : 2389836 //试题ID
     * QuestionTypeId : 1
     * =======================
     * Id	Name
     1	选择题
     2	填空题
     3	解答题
     4	判断题
     5	多选题
     12	听力单选题
     13	听力填空题
     26	复合题
     * =======================
     * AlternativeContent : // （选择题和多选题有值） 题目选项 数组 根据数组的长度 判断选项的长度
     * [{"Id":"A","Content":"某厂生产的电灯使用寿命"},{"Id":"B","Content":"全国初中生的视力情况<br>"},{"Id":"C","Content":"某校七年级学生的身高情况"},{"Id":"D","Content":"\"娃哈哈\"产品的合格率"}]
     * AnswerType : [{"Id":"1" //第几个空 ,"Content":"$option:1$","TypeId":1 //试题类型id 即QuestionTypeID }]
     * Answer : //正确答案 [{"Id":"1" //第几个空 ,"Answer":"C" //正确答案 }]
     * Fraction : 4  //试题分数
     * Sort : 1  //排序编号
     * StudentsAnswer : [
     {
     "Answer" : "C",  //学生选这个的答案
     "Id" : 1, //第几个空（如果有多个答案类容）
     "TypeId" : 1 //试题类型id 即QuestionTypeID
     }
     ]
     */

    private int Id;
    private int QuestionTypeId;
    private String AlternativeContent;
    private String AnswerType;
    private String Answer;
    private int Fraction;
    private int Sort;
    private String StudentsAnswer;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getQuestionTypeId() {
        return QuestionTypeId;
    }

    public void setQuestionTypeId(int QuestionTypeId) {
        this.QuestionTypeId = QuestionTypeId;
    }

    public String getAlternativeContent() {
        return AlternativeContent;
    }

    public void setAlternativeContent(String AlternativeContent) {
        this.AlternativeContent = AlternativeContent;
    }

    public String getAnswerType() {
        return AnswerType;
    }

    public void setAnswerType(String AnswerType) {
        this.AnswerType = AnswerType;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String Answer) {
        this.Answer = Answer;
    }

    public int getFraction() {
        return Fraction;
    }

    public void setFraction(int Fraction) {
        this.Fraction = Fraction;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int Sort) {
        this.Sort = Sort;
    }

    public String getStudentsAnswer() {
        return StudentsAnswer;
    }

    public void setStudentsAnswer(String StudentsAnswer) {
        this.StudentsAnswer = StudentsAnswer;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Id);
        dest.writeInt(this.QuestionTypeId);
        dest.writeString(this.AlternativeContent);
        dest.writeString(this.AnswerType);
        dest.writeString(this.Answer);
        dest.writeInt(this.Fraction);
        dest.writeInt(this.Sort);
        dest.writeString(this.StudentsAnswer);
    }

    public QuestionInfo() {
    }

    protected QuestionInfo(Parcel in) {
        this.Id = in.readInt();
        this.QuestionTypeId = in.readInt();
        this.AlternativeContent = in.readString();
        this.AnswerType = in.readString();
        this.Answer = in.readString();
        this.Fraction = in.readInt();
        this.Sort = in.readInt();
        this.StudentsAnswer = in.readString();
    }

    public static final Creator<QuestionInfo> CREATOR = new Creator<QuestionInfo>() {
        @Override
        public QuestionInfo createFromParcel(Parcel source) {
            return new QuestionInfo(source);
        }

        @Override
        public QuestionInfo[] newArray(int size) {
            return new QuestionInfo[size];
        }
    };
}
