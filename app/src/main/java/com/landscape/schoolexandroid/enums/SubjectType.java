package com.landscape.schoolexandroid.enums;

import com.landscape.schoolexandroid.R;

/**
 * Created by 1 on 2016/7/11.
 */
public enum SubjectType {
    GEO("地理", 1 , R.drawable.icon_dl),
    CHYMIST("化学", 2 , R.drawable.icon_hx),
    HISTORY("历史", 3 , R.drawable.icon_ls),
    ART("美术", 4 , R.drawable.icon_meishu),
    LIVE("生物", 5 , R.drawable.icon_sw),
    HAND("书法", 6 , R.drawable.icon_shufa),
    MATH("数学", 7 , R.drawable.icon_sx),
    SPORT("体育", 8 , R.drawable.icon_tiyu),
    PHYSIC("物理", 9, R.drawable.icon_wl),
    INFOMATION("信息技术", 10 , R.drawable.icon_xinxi),
    MUSIC("音乐", 11 , R.drawable.icon_yinyue),
    EN("英语", 12 , R.drawable.icon_yy),
    CN("语文", 13 , R.drawable.icon_yw),
    POLITY("政治", 14 , R.drawable.icon_zz),
    WRITE("写字", 15 , R.drawable.icon_xiezi),
    SCIENCE("科学", 16 , R.drawable.icon_keji),
    OTHER("其它", 17 , R.drawable.icon_gengduo);

    String name;
    int type;
    int drawableResId = -1;

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getDrawableResId() {
        return drawableResId;
    }

    SubjectType(String name, int type, int resId) {
        this.name = name;
        this.type = type;
        drawableResId = resId;
    }

    public static SubjectType getSubjectType(String name) {
        if (name.equals("地理")) {
            return GEO;
        } else if (name.equals("化学")) {
            return CHYMIST;
        } else if (name.equals("历史")) {
            return HISTORY;
        } else if (name.equals("美术")) {
            return ART;
        } else if (name.equals("生物")) {
            return LIVE;
        } else if (name.equals("书法")) {
            return HAND;
        } else if (name.equals("数学")) {
            return MATH;
        } else if (name.equals("体育")) {
            return SPORT;
        } else if (name.equals("物理")) {
            return PHYSIC;
        } else if (name.equals("信息技术")) {
            return INFOMATION;
        } else if (name.equals("音乐")) {
            return MUSIC;
        } else if (name.equals("英语")) {
            return EN;
        } else if (name.equals("语文")) {
            return CN;
        } else if (name.equals("政治")) {
            return POLITY;
        } else if (name.equals("写字")) {
            return WRITE;
        } else if (name.equals("科学")) {
            return SCIENCE;
        } else {
            return OTHER;
        }
    }

    public static SubjectType getSubjectType(int type) {
        switch (type) {
            case 1:
                return GEO;
            case 2:
                return CHYMIST;
            case 3:
                return HISTORY;
            case 4:
                return ART;
            case 5:
                return LIVE;
            case 6:
                return HAND;
            case 7:
                return MATH;
            case 8:
                return SPORT;
            case 9:
                return PHYSIC;
            case 10:
                return INFOMATION;
            case 11:
                return MUSIC;
            case 12:
                return EN;
            case 13:
                return CN;
            case 14:
                return POLITY;
            case 15:
                return WRITE;
            case 16:
                return SCIENCE;
            default:
                return OTHER;
        }
    }
}
