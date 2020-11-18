package com.geek.homework.week5.bean.annotation.service;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class Teacher {
    @Resource(name = "student3")
    private Student student;

    public void say() {
        System.out.println(student.getName() + "，叫家长来一下。");
    }

}
