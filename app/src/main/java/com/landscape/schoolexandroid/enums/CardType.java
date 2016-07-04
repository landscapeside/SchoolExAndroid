package com.landscape.schoolexandroid.enums;

/**
 * Created by landscape on 2016/7/3.
 */
public enum CardType {
    SINGLE_CHOOSE(1)/*选择题*/,
    PACK(2)/*填空题*/,
    EXPLAIN(3)/*解答题*/,
    DECIDE(4)/*判断题*/,
    MULTI_CHOOSE(5)/*多选题*/,
    LISTEN_SINGLE_CHOOSE(12)/*听力单选题*/,
    LISTEN_PACK(13)/*听力填空题*/,
    NONE(0);

    int code = 0;

    public int getCode() {
        return code;
    }

    CardType(int code) {
        this.code = code;
    }

    public static CardType getType(int code) {
        switch (code) {
            case 1:
                return SINGLE_CHOOSE;
            case 2:
                return PACK;
            case 3:
                return EXPLAIN;
            case 4:
                return DECIDE;
            case 5:
                return MULTI_CHOOSE;
            case 12:
                return LISTEN_SINGLE_CHOOSE;
            case 13:
                return LISTEN_PACK;
            default:
                return NONE;
        }
    }
}
