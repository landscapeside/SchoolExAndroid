package com.landscape.schoolexandroid.mode;

/**
 * Created by 1 on 2016/6/21.
 */
public class BaseBean {

    /**
     * $id : 1
     * errorId : 0
     * message : 登录成功
     * isSuccess : true
     * data : {"$id":"2","studentId":1424,"Name":"秦龙","LoginName":"xsebd06","NickName":null,"Photo":"http://images.cqebd.cn/vagerent/20160513/201605132240132227.jpg","Gender":"男","GradeName":"七年级","TeamName":"测试一班","ClassGroupName":"第二小组","SubjectType":[{"$id":"3","Id":1,"Name":"地理","Status":-1},{"$id":"4","Id":2,"Name":"化学","Status":-1},{"$id":"5","Id":3,"Name":"历史","Status":-1},{"$id":"6","Id":4,"Name":"美术","Status":-1},{"$id":"7","Id":5,"Name":"生物","Status":-1},{"$id":"8","Id":6,"Name":"书法","Status":-1},{"$id":"9","Id":7,"Name":"数学","Status":0},{"$id":"10","Id":8,"Name":"体育","Status":-1},{"$id":"11","Id":9,"Name":"物理","Status":-1},{"$id":"12","Id":10,"Name":"信息技术","Status":-1},{"$id":"13","Id":11,"Name":"音乐","Status":-1},{"$id":"14","Id":12,"Name":"英语","Status":0},{"$id":"15","Id":13,"Name":"语文","Status":-1},{"$id":"16","Id":14,"Name":"政治","Status":-1},{"$id":"17","Id":15,"Name":"写字","Status":-1},{"$id":"18","Id":16,"Name":"科学","Status":-1}],"ExaminationPapersType":[{"$id":"19","Id":8,"Name":"假期练习"},{"$id":"20","Id":9,"Name":"课时练习"},{"$id":"21","Id":10,"Name":"语法练习"},{"$id":"22","Id":11,"Name":"单词过关练习"},{"$id":"23","Id":12,"Name":"总复习"},{"$id":"24","Id":1,"Name":"单元测试"},{"$id":"25","Id":2,"Name":"期中测试"},{"$id":"26","Id":3,"Name":"期末测试"},{"$id":"27","Id":5,"Name":"课堂练习"},{"$id":"28","Id":6,"Name":"课后练习"},{"$id":"29","Id":7,"Name":"周末练习"}],"ImagesUrl":"http://images.cqebd.cn","ImagesTag":"{#imgDomain}","Flower":1}
     */

    protected int errorId;
    protected String message;
    protected boolean isSuccess;

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
