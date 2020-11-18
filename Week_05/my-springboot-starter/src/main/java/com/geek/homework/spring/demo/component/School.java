package com.geek.homework.spring.demo.component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class School implements ISchool {

    public School(){
        log.info("Initializing School");
    }

    Student student100;

    Klass class1;

    public void setStudent100(Student student100) {
        this.student100 = student100;
    }

    public void setClass1(Klass class1) {
        this.class1 = class1;
    }

    @Override
    public void ding(){

        System.out.println("Class1 have students and one is " + this.student100);

    }

    public Student getStudent100() {
        return student100;
    }

    public Klass getClass1() {
        return class1;
    }
}
