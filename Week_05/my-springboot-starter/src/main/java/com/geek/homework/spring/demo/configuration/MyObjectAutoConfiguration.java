package com.geek.homework.spring.demo.configuration;
import com.geek.homework.spring.demo.component.Klass;
import com.geek.homework.spring.demo.component.School;
import com.geek.homework.spring.demo.component.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动创建对象示例，例子中我们创建Teacher与Student对象。
 * 当项目打成jar包依赖到其他Spring容器中，这些对象我们可以自动进行注入
 */
@Configuration
@EnableConfigurationProperties(MyProperties.class)
public class MyObjectAutoConfiguration {

    @Configuration
    static class SchoolAutoConfiguration {

        @Bean
        @ConditionalOnClass({School.class, Student.class , Klass.class})
        @ConditionalOnProperty(name = "school.enabled", havingValue = "true", matchIfMissing = true)
        public static School school() {
            School school = new School();
            Klass klass = new Klass();
            List<Student> students = new ArrayList<>();
            Student student1 = new Student(100,"xiaoli");
            Student student2 = new Student(101,"xiaodong");
            students.add(student1);
            students.add(student2);
            school.setStudent100(student1);
            klass.setStudents(students);
            school.setClass1(klass);
            return school;
        }
    }

    @Configuration
    static class KlassAutoConfiguration {
        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty(name = "klass.enabled", havingValue = "true", matchIfMissing = true)
        public Klass klass(@Autowired MyProperties myProperties) {
            Klass klass = new Klass();
            List<Student> students = new ArrayList<>();
            Student student1 = new Student(1,"lili");
            Student student2 = new Student(2,"momo");
            students.add(student1);
            students.add(student2);
            klass.setStudents(students);
            return klass;
        }
    }

    @Configuration
    static class StudentAutoConfiguration {
        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty(name = "student.enabled", havingValue = "true", matchIfMissing = true)
        public Student student(@Autowired MyProperties myProperties) {
            return new Student(myProperties.getStudentId(), myProperties.getStudentName());
        }
    }
}
