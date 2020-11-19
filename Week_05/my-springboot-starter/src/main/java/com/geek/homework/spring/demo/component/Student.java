package com.geek.homework.spring.demo.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString
@Slf4j
public class Student implements Serializable {
    public Student(int id,String name){
        this.id = id;
        this.name = name;
        log.info("Initializing Student,id is:"+id+",name is:"+name);
    }
    private int id;
    private String name;
}
