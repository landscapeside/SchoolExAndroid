package com.landscape.schoolexandroid.enums;

/**
 * Created by 1 on 2016/7/12.
 */
public enum ExamPaperType {
    HOLIDAY(8,"假期练习"),
    LESSON(9,"课时练习"),
    SYNTAX(10,"语法练习"),
    WORDS(11,"单词过关练习"),



    ;

    int code;
    String name;

    ExamPaperType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
