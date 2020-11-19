package com.geek.homework.week5.bean.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);  //注意，和xml配置不同。参数是配置类。
        TeacherConfig teacher = applicationContext.getBean("teacherConfig", TeacherConfig.class);
        teacher.say();
//        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config2.class);  //注意，和xml配置不同。参数是配置类。
//        TeacherConfig2 teacher2 = applicationContext.getBean("teacherConfig2", TeacherConfig2.class);
//        teacher2.say();
    }
}
