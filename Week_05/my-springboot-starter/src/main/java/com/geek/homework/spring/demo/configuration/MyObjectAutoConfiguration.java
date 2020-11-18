package com.geek.homework.spring.demo.configuration;

import com.geek.homework.spring.demo.component.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动创建对象示例，例子中我们创建Teacher与Student对象。
 * 当项目打成jar包依赖到其他Spring容器中，这些对象我们可以自动进行注入
 */
@Configuration
@EnableConfigurationProperties(MyProperties.class)
public class MyObjectAutoConfiguration {



//    @Configuration
//    static class SchoolAutoConfiguration {
//
//        @Bean
//        @ConditionalOnClass({School.class, Student.class})
//        public static School school() {
//            return new School();
//        }
//    }

    @Configuration
    static class StudentAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public Student student(@Autowired MyProperties myProperties) {
            return new Student(myProperties.getStudentId(),myProperties.getStudentName());
        }
    }
}
