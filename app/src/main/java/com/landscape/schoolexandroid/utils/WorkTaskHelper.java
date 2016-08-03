package com.landscape.schoolexandroid.utils;

import com.landscape.schoolexandroid.mode.account.UserAccount;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.utils.datahelper.TimeConversion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 1 on 2016/7/11.
 */
public class WorkTaskHelper {

    public static Map<String, List<ExaminationTaskInfo>> sortTaskByDate(List<ExaminationTaskInfo> infos) {
        Map<String, List<ExaminationTaskInfo>> taskMap = new HashMap<>();
        for (int i=0;i<infos.size();i++) {
            String date = TimeConversion.getYearsMonthsData(
                    TimeConversion.getDurationWithGMT(infos.get(i).getCanStartDateTime()));
            List<ExaminationTaskInfo> tempTasks = taskMap.get(date);
            if (tempTasks == null) {
                tempTasks = new ArrayList<>();
                taskMap.put(date, tempTasks);
            }
            tempTasks.add(infos.get(i));
        }
        Set<String> keys = taskMap.keySet();
        for (String key : keys) {
            List<ExaminationTaskInfo> taskInfoList = taskMap.get(key);
            sortTaskListByTime(taskInfoList);
        }
        return taskMap;
    }

    public static void sortTaskListByTime(List<ExaminationTaskInfo> source) {
        Collections.sort(source, new Comparator<ExaminationTaskInfo>() {
            @Override
            public int compare(ExaminationTaskInfo lhs, ExaminationTaskInfo rhs) {
                return rhs.getPuchDateTime().compareTo(lhs.getPuchDateTime());
            }
        });
    }

    public static List<String> sortMonth(Set<String> keys) {
        List<String> months = new ArrayList<>();
        for (String key : keys) {
            months.add(key);
        }
        Collections.sort(months);
        Collections.reverse(months);
        return months;
    }

    public static List<UserAccount.DataBean.SubjectTypeBean> getValidSubjectType(List<UserAccount.DataBean.SubjectTypeBean> source) {
        List<UserAccount.DataBean.SubjectTypeBean> result = new ArrayList<>();
        for (UserAccount.DataBean.SubjectTypeBean subjectTypeBean : source) {
            if (subjectTypeBean.getStatus() == 0) {
                result.add(subjectTypeBean);
            }
        }
        return result;
    }

    public static List<UserAccount.DataBean.SubjectTypeBean> addDefSubjectType(List<UserAccount.DataBean.SubjectTypeBean> source) {
        List<UserAccount.DataBean.SubjectTypeBean> result = new ArrayList<>();
        UserAccount.DataBean.SubjectTypeBean def = new UserAccount.DataBean.SubjectTypeBean();
        def.setName("全部");
        def.setStatus(0);
        def.setId(-1);
        result.add(def);
        result.addAll(source);
        return result;
    }

    public static List<UserAccount.DataBean.ExaminationPapersTypeBean> addDefPaperType(List<UserAccount.DataBean.ExaminationPapersTypeBean> source) {
        List<UserAccount.DataBean.ExaminationPapersTypeBean> result = new ArrayList<>();
        UserAccount.DataBean.ExaminationPapersTypeBean def = new UserAccount.DataBean.ExaminationPapersTypeBean();
        def.setId(-1);
        def.setName("全部");
        result.add(def);
        result.addAll(source);
        return result;
    }
}
