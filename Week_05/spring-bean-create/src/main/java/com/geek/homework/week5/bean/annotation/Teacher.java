package com.geek.homework.week5.bean.annotation;

import com.geek.homework.week5.bean.xml.Student;
import org.springframework.beans.factory.annotation.Autowired;

public class Teacher {
    /***
     * 不必写setter方法，也不必写构造器。在依赖的对象上添加@Autowired注解，即按照类型自动装配依赖。
     * 上面在Student类型的依赖上添加了@Autowired注解，会自动在Spring容器中，找到Student类型的Bean注入。
     * 相当于byType
     */
    @Autowired
    private Student student;

    public void say() {
        System.out.println(student.getName() + "，叫家长来一下。");
    }

}
