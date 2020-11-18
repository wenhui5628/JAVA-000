package com.geek.homework.spring.demo;

import com.geek.homework.spring.demo.component.Student;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
		Student student = applicationContext.getBean(Student.class);
		System.out.println("id:"+student.getId() +",名字"+student.getName());
	}

}
