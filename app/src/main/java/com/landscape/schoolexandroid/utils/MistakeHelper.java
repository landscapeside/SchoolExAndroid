package com.landscape.schoolexandroid.utils;

import com.landscape.schoolexandroid.mode.mistake.MistakeInfo;
import com.landscape.schoolexandroid.mode.worktask.ExaminationTaskInfo;
import com.utils.datahelper.TimeConversion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 1 on 2016/7/12.
 */
public class MistakeHelper {

    public static Map<String, List<MistakeInfo>> sortMistakeByDate(List<MistakeInfo> infos) {
        Map<String, List<MistakeInfo>> mistakeMap = new HashMap<>();
        for (int i=0;i<infos.size();i++) {
            String date = TimeConversion.getYearsMonthsData(
                    TimeConversion.getDurationWithGMT(infos.get(i).getDateTime()));
            List<MistakeInfo> tempMistakes = mistakeMap.get(date);
            if (tempMistakes == null) {
                tempMistakes = new ArrayList<>();
                mistakeMap.put(date, tempMistakes);
            }
            tempMistakes.add(infos.get(i));
        }
        return mistakeMap;
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
