package com.wwh.config;

import com.wwh.annotation.TargetDataSource;
import com.wwh.dds.DynamicDataSourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Random;

/**
 * 数据源AOP切面定义
 */
@Component
@Aspect
@Slf4j
public class DataSourceAspect {
	@Value("${slave.hosts}")
	private String slaveHosts;

	//切换放在mapper接口的方法上，所以这里要配置AOP切面的切入点
	@Pointcut("execution( * com.wwh.mapper.*.*(..))")
	public void dataSourcePointCut() {
	}

	@Before("dataSourcePointCut()")
	public void before(JoinPoint joinPoint) {
		Object target = joinPoint.getTarget();
		String method = joinPoint.getSignature().getName();
		Class<?>[] clazz = target.getClass().getInterfaces();
		Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
		try {
			Method m = clazz[0].getMethod(method, parameterTypes);
			//如果方法上存在切换数据源的注解，则根据注解内容进行数据源切换
			if (m != null && m.isAnnotationPresent(TargetDataSource.class)) {
				TargetDataSource data = m.getAnnotation(TargetDataSource.class);
				String dataSourceName = data.value();
				//判断指定的数据源类型，如果是slave，则调用负载均衡方法，随机分配slave数据库
				if (dataSourceName.equals("slave")){
					dataSourceName = slaveLoadBalance();
				}
				DynamicDataSourceHolder.putDataSource(dataSourceName);
				log.debug("current thread " + Thread.currentThread().getName() + " add " + dataSourceName + " to ThreadLocal");
			} else {
				log.debug("switch datasource fail,use default");
			}
		} catch (Exception e) {
			log.error("current thread " + Thread.currentThread().getName() + " add data to ThreadLocal error", e);
		}
	}

	//执行完切面后，将线程共享中的数据源名称清空
	@After("dataSourcePointCut()")
	public void after(JoinPoint joinPoint){
		DynamicDataSourceHolder.removeDataSource();
	}

	//自己实现的随机指定slave数据源的简单的负载均衡算法
	private  String slaveLoadBalance() {
		String[] slaves = slaveHosts.split(",");
		//通过随机获取数组中数据库的名称来随机分配要使用的数据库
		int num = new Random().nextInt(slaves.length);
		return slaves[num];
	}
}
