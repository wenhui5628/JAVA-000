package com.wwh.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 实际数据源配置
 */
@Component
@Data
@ConfigurationProperties(prefix = "")
public class DBProperties {
    //	private HikariDataSource master;
    //	private HikariDataSource slave1;
    //	private HikariDataSource slave2;
    //一次性从配置文件中读取所有数据源的配置
    private Map<String, HikariDataSource> hikari;
}
