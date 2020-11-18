package com.geek.homework.week5.bean.xml;

public class Teacher2 {
    private Student student;

    public Teacher2(Student student){
          this.student=student;
    }

    public void say() {
        System.out.println(student.getName() + "，叫你家长来一下。");
    }

}
