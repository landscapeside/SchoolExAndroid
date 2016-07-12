package com.landscape.schoolexandroid.mode.mistake;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 1 on 2016/7/12.
 */
public class MistakeInfo implements Parcelable {

    /**
     * $id : 2
     * DateTime : 2016-07-06T16:08:21.257
     * StudentQuestionsTasksID : 28401
     * ExaminationPapersId : 1706
     * Name : 7-6物理组题测试
     * SubjectTypeName : 物理
     * PapersTypeName : 总复习
     * ErrorCount : 5
     */

    private String DateTime;
    private int StudentQuestionsTasksID;
    private int ExaminationPapersId;
    private String Name;
    private String SubjectTypeName;
    private String PapersTypeName;
    private int ErrorCount;

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String DateTime) {
        this.DateTime = DateTime;
    }

    public int getStudentQuestionsTasksID() {
        return StudentQuestionsTasksID;
    }

    public void setStudentQuestionsTasksID(int StudentQuestionsTasksID) {
        this.StudentQuestionsTasksID = StudentQuestionsTasksID;
    }

    public int getExaminationPapersId() {
        return ExaminationPapersId;
    }

    public void setExaminationPapersId(int ExaminationPapersId) {
        this.ExaminationPapersId = ExaminationPapersId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getSubjectTypeName() {
        return SubjectTypeName;
    }

    public void setSubjectTypeName(String SubjectTypeName) {
        this.SubjectTypeName = SubjectTypeName;
    }

    public String getPapersTypeName() {
        return PapersTypeName;
    }

    public void setPapersTypeName(String PapersTypeName) {
        this.PapersTypeName = PapersTypeName;
    }

    public int getErrorCount() {
        return ErrorCount;
    }

    public void setErrorCount(int ErrorCount) {
        this.ErrorCount = ErrorCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.DateTime);
        dest.writeInt(this.StudentQuestionsTasksID);
        dest.writeInt(this.ExaminationPapersId);
        dest.writeString(this.Name);
        dest.writeString(this.SubjectTypeName);
        dest.writeString(this.PapersTypeName);
        dest.writeInt(this.ErrorCount);
    }

    public MistakeInfo() {
    }

    protected MistakeInfo(Parcel in) {
        this.DateTime = in.readString();
        this.StudentQuestionsTasksID = in.readInt();
        this.ExaminationPapersId = in.readInt();
        this.Name = in.readString();
        this.SubjectTypeName = in.readString();
        this.PapersTypeName = in.readString();
        this.ErrorCount = in.readInt();
    }

    public static final Parcelable.Creator<MistakeInfo> CREATOR = new Parcelable.Creator<MistakeInfo>() {
        @Override
        public MistakeInfo createFromParcel(Parcel source) {
            return new MistakeInfo(source);
        }

        @Override
        public MistakeInfo[] newArray(int size) {
            return new MistakeInfo[size];
        }
    };
}
