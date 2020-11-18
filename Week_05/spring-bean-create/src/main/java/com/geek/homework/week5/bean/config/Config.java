package com.geek.homework.week5.bean.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//表示这个类是用来配置Bean的
@Configuration
class Config {
    @Value("张三")
    String name;   //创建一个成员变量，相当于String name="张三";

    @Bean(name = "studentConfig")   //配置一个Bean，相当于xml中的一个<bean>
    public StudentConfig studentConfig() {
        StudentConfig studentConfig = new StudentConfig(name);   //创建并返回Bean的实例。
        return studentConfig;
    }

    @Bean(name = "teacherConfig")
    public TeacherConfig teacherConfig() {
        return new TeacherConfig(studentConfig());  //创建并返回Bean的实例，因为写了构造器，所以可以直接构造器注入依赖。可直接调用本类中的其它方法创建依赖的实例，注入。
    }


}

