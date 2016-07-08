package com.landscape.schoolexandroid.enums;

/**
 * Created by 1 on 2016/7/8.
 */
public enum AnswerMode {
    EXAM(0)/*考试*/,
    TRAIN(1)/*练习*/,
    NONE(-1);

    int code;

    public int getCode() {
        return code;
    }

    AnswerMode(int code) {
        this.code = code;
    }

    public static AnswerMode getAnswerMode(int code) {
        switch (code) {
            case 0:
                return EXAM;
            case 1:
                return TRAIN;
        }
        return NONE;
    }
}
