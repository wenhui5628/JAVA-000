package com.geek.homework.spring.demo;

import com.geek.homework.spring.demo.component.Klass;
import com.geek.homework.spring.demo.component.School;
import com.geek.homework.spring.demo.component.Student;
import com.sun.deploy.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class DemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
		Student student = applicationContext.getBean(Student.class);
		School school = applicationContext.getBean(School.class);
		Klass klass = applicationContext.getBean(Klass.class);
		log.info("studentId:"+student.getId() +",studentName:"+student.getName());
		log.info("schoolInfo have students:"+ Arrays.toString(school.getClass1().getStudents().toArray()) +",and student100 info:"+school.getStudent100());
		log.info("klass info:"+ Arrays.toString(klass.getStudents().toArray()));
	}

}
