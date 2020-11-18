package com.geek.homework.spring.demo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 用于实现读取application.yml中的配置
 */
@ConfigurationProperties(prefix = MyProperties.PREFIX)
public class MyProperties {

    public static final String PREFIX = "my";

    private String studentName;

    private int studentId;

    public String getStudentName() {
        return studentName;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }


}