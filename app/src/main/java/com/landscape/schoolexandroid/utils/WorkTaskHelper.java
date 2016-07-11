package com.landscape.schoolexandroid.utils;

import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.utils.datahelper.TimeConversion;

import java.util.ArrayList;
import java.util.Collections;
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
        return taskMap;
    }

    public static List<String> sortDate(Set<String> keys) {
        List<String> dates = new ArrayList<>();
        for (String key : keys) {
            dates.add(key);
        }
        Collections.sort(dates);
        Collections.reverse(dates);
        return dates;
    }
}
