package com.wwh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author wuwenhui
 * @Date 2020/12/02 下午5:42
 **/
@SpringBootApplication(scanBasePackages = {
        "com.wwh"
})
@MapperScan(basePackages = "com.wwh.dao")
public class ShardingjdbcXaApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShardingjdbcXaApplication.class, args);
    }
}
