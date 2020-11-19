package com.geek.homework.spring.demo.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
@Slf4j
public class Klass {
    public Klass(){
        log.info("Initializing Klass");
    }

    List<Student> students;

    public void dong(){
        System.out.println(this.getStudents());
    }

}
