package com.geek.homework.week5.bean.annotation;

import javax.annotation.Resource;

public class Teacher3 {
    /***
     * 如果找不到name/id为指定值的Bean，或缺省name直接写@Resource，
     * 则以默认的getName方式：写在字段上默认name为成员变量名（student）
     * 如果还是找不到依赖的Bean，则以byType方式注入
     */
    @Resource(name = "student2")
    private Student student;

    public void say() {
        System.out.println(student.getName() + "，叫家长来一下。");
    }

}
