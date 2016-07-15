package com.landscape.schoolexandroid.mode.account;

import com.landscape.schoolexandroid.mode.BaseBean;

import java.util.List;

/**
 * Created by 1 on 2016/6/21.
 */
public class UserAccount extends BaseBean {

    /**
     * $id : 2
     * studentId : 1424
     * Name : 秦龙
     * LoginName : xsebd06
     * NickName : null
     * Photo : http://images.cqebd.cn/vagerent/20160513/201605132240132227.jpg
     * Gender : 男
     * SchoolName: 重庆教育大数据研究院
     * GradeName : 七年级
     * TeamName : 测试一班
     * ClassGroupName : 第二小组
     * SubjectType : [{"$id":"3","Id":1,"Name":"地理","Status":-1},{"$id":"4","Id":2,"Name":"化学","Status":-1},{"$id":"5","Id":3,"Name":"历史","Status":-1},{"$id":"6","Id":4,"Name":"美术","Status":-1},{"$id":"7","Id":5,"Name":"生物","Status":-1},{"$id":"8","Id":6,"Name":"书法","Status":-1},{"$id":"9","Id":7,"Name":"数学","Status":0},{"$id":"10","Id":8,"Name":"体育","Status":-1},{"$id":"11","Id":9,"Name":"物理","Status":-1},{"$id":"12","Id":10,"Name":"信息技术","Status":-1},{"$id":"13","Id":11,"Name":"音乐","Status":-1},{"$id":"14","Id":12,"Name":"英语","Status":0},{"$id":"15","Id":13,"Name":"语文","Status":-1},{"$id":"16","Id":14,"Name":"政治","Status":-1},{"$id":"17","Id":15,"Name":"写字","Status":-1},{"$id":"18","Id":16,"Name":"科学","Status":-1}]
     * ExaminationPapersType : [{"$id":"19","Id":8,"Name":"假期练习"},{"$id":"20","Id":9,"Name":"课时练习"},{"$id":"21","Id":10,"Name":"语法练习"},{"$id":"22","Id":11,"Name":"单词过关练习"},{"$id":"23","Id":12,"Name":"总复习"},{"$id":"24","Id":1,"Name":"单元测试"},{"$id":"25","Id":2,"Name":"期中测试"},{"$id":"26","Id":3,"Name":"期末测试"},{"$id":"27","Id":5,"Name":"课堂练习"},{"$id":"28","Id":6,"Name":"课后练习"},{"$id":"29","Id":7,"Name":"周末练习"}]
     * ImagesUrl : http://images.cqebd.cn
     * ImagesTag : {#imgDomain}
     * Flower : 1
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int studentId;
        private String Name;
        private String LoginName;
        private Object NickName;
        private String Photo;
        private String Gender;
        private String SchoolName;
        private String GradeName;
        private String TeamName;
        private String ClassGroupName;
        private String ImagesUrl;
        private String ImagesTag;
        private int Flower;
        /**
         * $id : 3
         * Id : 1
         * Name : 地理
         * Status : -1
         */

        private List<SubjectTypeBean> SubjectType;
        /**
         * $id : 19
         * Id : 8
         * Name : 假期练习
         */

        private List<ExaminationPapersTypeBean> ExaminationPapersType;

        public int getStudentId() {
            return studentId;
        }

        public void setStudentId(int studentId) {
            this.studentId = studentId;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getLoginName() {
            return LoginName;
        }

        public void setLoginName(String LoginName) {
            this.LoginName = LoginName;
        }

        public Object getNickName() {
            return NickName;
        }

        public void setNickName(Object NickName) {
            this.NickName = NickName;
        }

        public String getPhoto() {
            return Photo;
        }

        public void setPhoto(String Photo) {
            this.Photo = Photo;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String Gender) {
            this.Gender = Gender;
        }

        public String getGradeName() {
            return GradeName;
        }

        public void setGradeName(String GradeName) {
            this.GradeName = GradeName;
        }

        public String getSchoolName() {
            return SchoolName;
        }

        public void setSchoolName(String schoolName) {
            SchoolName = schoolName;
        }

        public String getTeamName() {
            return TeamName;
        }

        public void setTeamName(String TeamName) {
            this.TeamName = TeamName;
        }

        public String getClassGroupName() {
            return ClassGroupName;
        }

        public void setClassGroupName(String ClassGroupName) {
            this.ClassGroupName = ClassGroupName;
        }

        public String getImagesUrl() {
            return ImagesUrl;
        }

        public void setImagesUrl(String ImagesUrl) {
            this.ImagesUrl = ImagesUrl;
        }

        public String getImagesTag() {
            return ImagesTag;
        }

        public void setImagesTag(String ImagesTag) {
            this.ImagesTag = ImagesTag;
        }

        public int getFlower() {
            return Flower;
        }

        public void setFlower(int Flower) {
            this.Flower = Flower;
        }

        public List<SubjectTypeBean> getSubjectType() {
            return SubjectType;
        }

        public void setSubjectType(List<SubjectTypeBean> SubjectType) {
            this.SubjectType = SubjectType;
        }

        public List<ExaminationPapersTypeBean> getExaminationPapersType() {
            return ExaminationPapersType;
        }

        public void setExaminationPapersType(List<ExaminationPapersTypeBean> ExaminationPapersType) {
            this.ExaminationPapersType = ExaminationPapersType;
        }

        public static class SubjectTypeBean {
            private int Id;
            private String Name;
            private int Status;

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public int getStatus() {
                return Status;
            }

            public void setStatus(int Status) {
                this.Status = Status;
            }
        }

        public static class ExaminationPapersTypeBean {
            private int Id;
            private String Name;

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }
        }
    }
}
