package com.geek.homework.week5.bean.config;


public class TeacherConfig2 {

    private StudentConfig2 studentConfig2;

    public void setStudentConfig(StudentConfig2 studentConfig2) {
        this.studentConfig2 = studentConfig2;
    }

    public void say() {
        System.out.println(studentConfig2.getName() + "，叫家长来一下。");
    }
}
