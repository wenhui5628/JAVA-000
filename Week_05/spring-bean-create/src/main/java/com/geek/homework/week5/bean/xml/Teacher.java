package com.geek.homework.week5.bean.xml;

public class Teacher {
    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    public void say() {
        System.out.println(student.getName() + "，叫家长来一下。");
    }

}
