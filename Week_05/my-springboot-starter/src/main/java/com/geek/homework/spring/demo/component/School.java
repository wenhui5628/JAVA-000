package com.geek.homework.spring.demo.component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@ToString
@Slf4j
public class School implements ISchool {
    public School(){
        log.info("Initializing School");
    }

    Student student100;

    Klass class1;

    @Override
    public void ding(){
        System.out.println("Class1 have students and one is " + this.student100);
    }
}
