package com.geek.homework.spring.demo.component;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
public class Student implements Serializable {

    public Student(int id,String name){
        this.id = id;
        this.name = name;
        log.info("Initializing Student "+id+":"+name);
    }

    private int id;
    private String name;

    public int getId() {
        return id;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    @Override
    public String toString() {
        return "studentId:"+id+";studentName:"+name;
    }
}
