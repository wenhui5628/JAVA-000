package com.geek.homework.week5.bean.config;


public class TeacherConfig {

    public TeacherConfig(StudentConfig studentConfig) {
        this.studentConfig = studentConfig;
    }

    private StudentConfig studentConfig;

    public void say() {
        System.out.println(studentConfig.getName() + "，叫家长来一下。");
    }

}
