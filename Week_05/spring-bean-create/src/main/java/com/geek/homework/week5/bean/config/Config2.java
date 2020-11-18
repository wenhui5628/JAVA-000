package com.geek.homework.week5.bean.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//表示这个类是用来配置Bean的
@Configuration
class Config2 {
    @Value("张三")
    String name;   //创建一个成员变量，相当于String name="张三";

    @Bean(name = "studentConfig2")   //配置一个Bean，相当于xml中的一个<bean>
    public StudentConfig2 studentConfig2() {
        StudentConfig2 studentConfig2 = new StudentConfig2();   //创建并返回Bean的实例。
        studentConfig2.setName(name);
        return studentConfig2;
    }

    @Bean(name = "teacherConfig2")
    public TeacherConfig2 teacherConfig2() {
        TeacherConfig2 teacherConfig2 = new TeacherConfig2();
        teacherConfig2.setStudentConfig(studentConfig2());
        return teacherConfig2;
    }


}

