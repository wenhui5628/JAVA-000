package com.wwh.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 实际数据源配置
 */
//@Component
//@Data
//@ConfigurationProperties(prefix = "hikari")
public class DBPropertiesBak {
	private HikariDataSource master;
	private HikariDataSource slave1;
	private HikariDataSource slave2;
}
