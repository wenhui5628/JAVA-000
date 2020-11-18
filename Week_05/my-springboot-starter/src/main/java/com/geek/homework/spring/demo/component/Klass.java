package com.geek.homework.spring.demo.component;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Klass {

    public Klass(){
        log.info("Initializing Klass");
    }

    List<Student> students;

    public void dong(){
        System.out.println(this.getStudents());
//        System.out.println(students.toArray());
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
