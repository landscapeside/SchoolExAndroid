package com.landscape.schoolexandroid.mode.worktask;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 1 on 2016/6/27.
 */
public class ExaminationTaskInfo implements Parcelable {

    /**
     * $id : 35
     * StudentQuestionsTasksID : 11478
     * IsTasks : false //判断是否在答题时间
     * PuchDateTime : 2016-03-29T13:34:05.5  //推送时间
     * CanStartDateTime : 2016-03-29T13:33:29 /允许答题的开始时间
     * CanEndDateTime : 2016-04-09T14:33:29 //允许答题的限制结束时间
     * StartTime : 2016-03-29T13:34:42.533 //学生答题的开始时间
     * EndTime : 2016-03-29T13:35:59.097,//学生答题的结束
     * DateTime : 2016-06-27T13:19:12.2241452
     * Duration : 3 ,//答题分钟数
     * Status : 2 //z状态 -1 初始状态 0 开始做题 1 做题完成 2老师已批阅
     * ExaminationPapersId : 1464 //试卷ID
     * Name : 数学组卷测试1 //试卷名称
     * SubjectTypeName : 数学 //科目
     * PapersTypeName : 课堂练习
     * Count : 9  //题目条数
     * Fraction : 6 //总分
     */

    private int StudentQuestionsTasksID;
    private boolean IsTasks;
    private String PuchDateTime;
    private String CanStartDateTime;
    private String CanEndDateTime;
    private String StartTime;
    private String EndTime;
    private String DateTime;
    private int Duration;
    private int Status;
    private int ExaminationPapersId;
    private String Name;
    private String SubjectTypeName;
    private String PapersTypeName;
    private int Count;
    private float Fraction;

    public int getStudentQuestionsTasksID() {
        return StudentQuestionsTasksID;
    }

    public void setStudentQuestionsTasksID(int StudentQuestionsTasksID) {
        this.StudentQuestionsTasksID = StudentQuestionsTasksID;
    }

    public boolean isIsTasks() {
        return IsTasks;
    }

    public void setIsTasks(boolean IsTasks) {
        this.IsTasks = IsTasks;
    }

    public String getPuchDateTime() {
        return PuchDateTime;
    }

    public void setPuchDateTime(String PuchDateTime) {
        this.PuchDateTime = PuchDateTime;
    }

    public String getCanStartDateTime() {
        return CanStartDateTime;
    }

    public void setCanStartDateTime(String CanStartDateTime) {
        this.CanStartDateTime = CanStartDateTime;
    }

    public String getCanEndDateTime() {
        return CanEndDateTime;
    }

    public void setCanEndDateTime(String CanEndDateTime) {
        this.CanEndDateTime = CanEndDateTime;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String DateTime) {
        this.DateTime = DateTime;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int Duration) {
        this.Duration = Duration;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
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

    public int getCount() {
        return Count;
    }

    public void setCount(int Count) {
        this.Count = Count;
    }

    public float getFraction() {
        return Fraction;
    }

    public void setFraction(float Fraction) {
        this.Fraction = Fraction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.StudentQuestionsTasksID);
        dest.writeByte(this.IsTasks ? (byte) 1 : (byte) 0);
        dest.writeString(this.PuchDateTime);
        dest.writeString(this.CanStartDateTime);
        dest.writeString(this.CanEndDateTime);
        dest.writeString(this.StartTime);
        dest.writeString(this.EndTime);
        dest.writeString(this.DateTime);
        dest.writeInt(this.Duration);
        dest.writeInt(this.Status);
        dest.writeInt(this.ExaminationPapersId);
        dest.writeString(this.Name);
        dest.writeString(this.SubjectTypeName);
        dest.writeString(this.PapersTypeName);
        dest.writeInt(this.Count);
        dest.writeFloat(this.Fraction);
    }

    public ExaminationTaskInfo() {
    }

    protected ExaminationTaskInfo(Parcel in) {
        this.StudentQuestionsTasksID = in.readInt();
        this.IsTasks = in.readByte() != 0;
        this.PuchDateTime = in.readString();
        this.CanStartDateTime = in.readString();
        this.CanEndDateTime = in.readString();
        this.StartTime = in.readString();
        this.EndTime = in.readString();
        this.DateTime = in.readString();
        this.Duration = in.readInt();
        this.Status = in.readInt();
        this.ExaminationPapersId = in.readInt();
        this.Name = in.readString();
        this.SubjectTypeName = in.readString();
        this.PapersTypeName = in.readString();
        this.Count = in.readInt();
        this.Fraction = in.readFloat();
    }

    public static final Creator<ExaminationTaskInfo> CREATOR = new Creator<ExaminationTaskInfo>() {
        @Override
        public ExaminationTaskInfo createFromParcel(Parcel source) {
            return new ExaminationTaskInfo(source);
        }

        @Override
        public ExaminationTaskInfo[] newArray(int size) {
            return new ExaminationTaskInfo[size];
        }
    };
}
