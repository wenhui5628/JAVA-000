package com.wwh.config;

import com.wwh.dds.DynamicDataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 数据源配置
 */
@Configuration
@EnableScheduling
@Slf4j
public class DataSourceConfig {

	@Autowired
	private DBProperties properties;

	private static final String KEY_MASTER = "master";

	@Bean(name = "dataSource")
	public DataSource dataSource() {
		//按照目标数据源名称和目标数据源对象的映射存放在Map中
		Map<Object, Object> targetDataSources = new HashMap<>();
		//获取配置文件中的数据源
		Map<String, HikariDataSource> hikaris = properties.getHikari();
		Set<String> keys = hikaris.keySet();
		HikariDataSource hikariDataSource = null;
		HikariDataSource masterDB = null;
		String poolName = "";
		for (String key : keys){
			hikariDataSource = hikaris.get(key);
			poolName = hikariDataSource.getPoolName();
			targetDataSources.put(hikariDataSource.getPoolName(),hikariDataSource);
			if (poolName.equals(KEY_MASTER)){
				masterDB = hikariDataSource;
			}
		}

		//采用是想AbstractRoutingDataSource的对象包装多数据源
		DynamicDataSource dataSource = new DynamicDataSource();
		dataSource.setTargetDataSources(targetDataSources);
		//设置默认的数据源，当拿不到数据源时，使用此配置
		if (null != masterDB){
			dataSource.setDefaultTargetDataSource(masterDB);
		}else {
			log.error("Can't find master db, project will be exit");
			System.exit(0);
		}
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager txManager() {
		return new DataSourceTransactionManager(dataSource());
	}

}
