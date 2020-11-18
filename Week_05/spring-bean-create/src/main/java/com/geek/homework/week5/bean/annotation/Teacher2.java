package com.geek.homework.week5.bean.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class Teacher2 {
    /***
     * 在依赖的Bean实例上添加@Qualifier，@Qualifier不能单独用，还需要添加@Autowired。
     * @Qualifier是byName方式的自动装配，需要用value指定依赖Bean的id/name，
     * Spring容器根据这个value找到id/name为vBean注入
     */
    @Autowired
//    @Qualifier(value="student2")
    @Qualifier("student2")
    private Student student;

    public void say() {
        System.out.println(student.getName() + "，叫家长来一下。");
    }

}
