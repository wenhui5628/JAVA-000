### 作业完成情况
#### 一、（必做）按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。
##### 采用以下几种方式测试插入效率
##### 1、用java的Statement方式逐笔插入，代码如下：
      Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/db?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
            connection = DriverManager.getConnection(url,"root","");
            statement = connection.createStatement();
            System.out.println("导入数据开始!");
            long start=System.currentTimeMillis();
            for(int i=1;i<= 1000000;i++){
                String insertSql = "insert into order_master(order_code,customer_id,order_money,order_status,address,create_time,update_time)  
                values(1,1,1,0,'address',now(),now())";
                statement.executeUpdate(insertSql);
            }
            System.out.println("导入数据结束,使用时间："+ (System.currentTimeMillis()-start) + " ms");
            
#### 执行结果如下，大概使用了99分钟：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_07/img/%E4%BD%BF%E7%94%A8java-statement%E5%AF%BC%E5%85%A5%E4%B8%80%E7%99%BE%E4%B8%87%E6%95%B0%E6%8D%AE.PNG)

#### 2、用java的PreparedStatement单笔执行，代码如下：
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/db?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
            connection = DriverManager.getConnection(url,"root","");
            String insertSql = "insert into order_master(order_code,customer_id,order_money,order_status,address,create_time,update_time) values(?,?,?,?,?,now(),now())";
            preparedStatement = connection.prepareStatement(insertSql);
            for(int i=1;i<= 1000000;i++){
                preparedStatement.setInt(1,1);
                preparedStatement.setInt(2,1);
                preparedStatement.setInt(3,1);
                preparedStatement.setInt(4,0);
                preparedStatement.setString(5,"address");
                preparedStatement.executeUpdate();
            }
            System.out.println("PrepareStatement单笔提交方式导入数据结束,使用时间："+ (System.currentTimeMillis()-start) + " ms");
            
 #### 执行结果如下，大概使用了104分钟，看起来和PreparedStatement单笔执行比较，效率上并没有提升：
 ![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_07/img/java-prepareStatement%E5%AF%BC%E5%85%A5%E4%B8%80%E7%99%BE%E4%B8%87%E6%95%B0%E6%8D%AE(%E4%B8%8D%E5%88%86%E6%89%B9).PNG)
 
 #### 3、用java的PreparedStatement结合addBatch方法分批执行，每5万笔提交一次，代码如下：
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/db?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
            connection = DriverManager.getConnection(url,"root","");
            connection.setAutoCommit(false);
            String insertSql = "insert into order_master(order_code,customer_id,order_money,order_status,address,create_time,update_time) values(?,?,?,?,?,now(),now())";
            preparedStatement = connection.prepareStatement(insertSql);
            for(int i=1;i<= 1000000;i++){
                preparedStatement.setInt(1,1);
                preparedStatement.setInt(2,1);
                preparedStatement.setInt(3,1);
                preparedStatement.setInt(4,0);
                preparedStatement.setString(5,"address");
                preparedStatement.executeUpdate();
                preparedStatement.addBatch();
                //分段提交
                if((i%50000==0&& i!=0)||i== (1000000 -1)){
                    preparedStatement.executeBatch();
                    connection.commit();
                    connection.setAutoCommit(false);
                    preparedStatement = connection.prepareStatement(insertSql);
                }
            }
            System.out.println("PrepareStatement每5万笔提交一次方式导入数据结束,使用时间："+ (System.currentTimeMillis()-start) + " ms");
            
#### 执行结果如下，大概只使用了4.8分钟，跟前面两种方式比较，效率有了质的飞跃，如下：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_07/img/%E4%BD%BF%E7%94%A8PrepareStatement%E5%8A%A0%E5%88%86%E6%89%B9(50000%E7%AC%94)%E6%8F%90%E4%BA%A4%E4%B8%80%E6%AC%A1.PNG)

#### 4、用java的PreparedStatement结合addBatch方法分批执行，每10万笔提交一次，代码如下：
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/db?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
            connection = DriverManager.getConnection(url,"root","");
            connection.setAutoCommit(false);
            String insertSql = "insert into order_master(order_code,customer_id,order_money,order_status,address,create_time,update_time) values(?,?,?,?,?,now(),now())";
            preparedStatement = connection.prepareStatement(insertSql);
            for(int i=1;i<= 1000000;i++){
                preparedStatement.setInt(1,1);
                preparedStatement.setInt(2,1);
                preparedStatement.setInt(3,1);
                preparedStatement.setInt(4,0);
                preparedStatement.setString(5,"address");
                preparedStatement.executeUpdate();
                preparedStatement.addBatch();
                //分段提交
                if((i%100000==0&& i!=0)||i== (1000000 -1)){
                    preparedStatement.executeBatch();
                    connection.commit();
                    connection.setAutoCommit(false);
                    preparedStatement = connection.prepareStatement(insertSql);
                }
            }
            System.out.println("PrepareStatement每10万笔提交一次方式导入数据结束,使用时间："+ (System.currentTimeMillis()-start) + " ms");
            
#### 执行结果如下，大概使用了5.6分钟，跟每5万次提交一次比较，效率稍微有一点下降：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_07/img/java-prepareStatement%E5%AF%BC%E5%85%A5%E4%B8%80%E7%99%BE%E4%B8%87%E6%95%B0%E6%8D%AE(%E5%8D%81%E4%B8%87%E4%B8%80%E4%B8%AA%E6%89%B9%E6%AC%A1).PNG)

#### 5、用java的PreparedStatement结合addBatch方法分批执行，全部100万笔数据添加到batch后最后才作为一个批次一起提交，代码如下：
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/db?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
            connection = DriverManager.getConnection(url,"root","");
            connection.setAutoCommit(false);
            String insertSql = "insert into order_master(order_code,customer_id,order_money,order_status,address,create_time,update_time) values(?,?,?,?,?,now(),now())";
            preparedStatement = connection.prepareStatement(insertSql);
            for(int i=1;i<= 1000000;i++){
                preparedStatement.setInt(1,1);
                preparedStatement.setInt(2,1);
                preparedStatement.setInt(3,1);
                preparedStatement.setInt(4,0);
                preparedStatement.setString(5,"address");
                preparedStatement.executeUpdate();
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
            System.out.println("PrepareStatement单笔提交方式导入数据结束,使用时间："+ (System.currentTimeMillis()-start) + " ms");
#### 执行结果如下，大概使用了10.9分钟，和前面每5万笔提交一次以及每10万笔提交一次效率有所下降：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_07/img/java-prepareStatement%E5%AF%BC%E5%85%A5%E4%B8%80%E7%99%BE%E4%B8%87%E6%95%B0%E6%8D%AE(%E6%89%80%E6%9C%89%E6%95%B0%E6%8D%AE%E4%BD%9C%E4%B8%BA%E4%B8%80%E4%B8%AA%E6%89%B9%E6%AC%A1%E6%8F%90%E4%BA%A4).PNG)

#### 6、通过java代码生成100万笔记录的insert语句到txt文件中，然后使用load命令执行导入，生成insert语句的代码如下：
      public class GenSql {
          private static String filePath = "D:\\batchInsert.sql";

          private static void saveAsFileWriter(String content) throws IOException{
              try(FileWriter fwriter = new FileWriter(filePath, true)){
                  fwriter.write(content);
              }
          }

          public static void main(String[] args) throws IOException {
              for(int i=0;i<1000000;i++){
                  String sql = "insert into order_master(order_code,customer_id,order_money,order_status,address,create_time,update_time) 
                  values(1,1,1,0,'address',now(),now());"+"\r\n";
                  saveAsFileWriter(sql);
              }
          }
      }
      
#### 导入数据的命令如下：
LOAD DATA LOCAL INFILE 'D:/batchInsert.sql' INTO TABLE order_master;

#### 执行结果如下，可以看到，只使用了25秒即完成了100万笔数据的导入，在试验的所有方法中，效率是最高的：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_07/img/load%E5%91%BD%E4%BB%A4%E5%AF%BC%E5%85%A5%E4%B8%80%E7%99%BE%E4%B8%87%E7%AC%94%E6%95%B0%E6%8D%AE.PNG)

#### 二、（必做）读写分离-动态切换数据源版本1.0
####  具体要求如下：
#### 1、基于 Spring/Spring Boot，配置多个数据源(例如2个，master 和 slave)；
#### 2、根据具体的 Service 方法是否会操作数据，注入不同的数据源,1.0版本；
#### 3、改进一下1.1：基于操作 AbstractRoutingDataSource 和自定义注解 readOnly 之类的，简化自动切换数据源；
#### 4、改进二下1.2：支持配置多个从库；
#### 5、改进三下1.3：支持多个从库的负载均衡。
#### 实现的效果图如下，根据具体的业务类型，动态切换数据源，如增删改这些请求使用主库，查询请求使用从库
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_07/img/%E5%A4%9A%E6%95%B0%E6%8D%AE%E6%BA%90%E5%88%87%E6%8D%A21.png)

#### 实现如下：
#### 1、搭建主从复制环境，这一步是参考老师发的md文件中的步骤完成，主数据库端口使用3306，两个从数据库端口分别使用3316和3326；
#### 2、分别使用了spring和spring Boot实现了动态切换数据源的效果，spring版本见工程dynamic-datasource-spring，spring Boot版本见工程dynamic-datasource-springboot；
#### 3、spring版本的数据源配置见spring-mybatis.xml配置文件，spring boot版本数据源配置见application.yml配置文件；
#### 4、spring版本的切换数据源涉及一下几步操作：
##### 1）MultiDataSource.java类，继承AbstractRoutingDataSource，使用到AbstractRoutingDataSource的两个属性defaultTargetDataSource和targetDataSources，defaultTargetDataSource为默认目标数据源，targetDataSources（map类型）存放用来切换的数据源，配置完以后，其他地方用到数据源的话，都引用multiDataSource，MultiDataSource的代码如下：
      package com.wwh.dataSource;

      import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

      public class MultiDataSource extends AbstractRoutingDataSource{

            /*ThreadLocal线程本地变量或线程本地存储，ThreadLocal为变量在每个线程中都创建了一个副本，
             * 那么每个线程可以访问自己内部的副本变量。
            */
            private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();

            /**
             * 设置dataSourceKey的值
             * @param dataSource
             */
            public static void setDataSourceKey(String dataSource) {
                  dataSourceKey.set(dataSource);
            }
            /**
             * 清除dataSourceKey的值
             */
            public static void toDefault() {
                  dataSourceKey.remove();
            }
            /**
             * 返回当前dataSourceKey的值
             */
            @Override
            protected Object determineCurrentLookupKey() {
                  return dataSourceKey.get();
            }

      }

##### 2）涉及的配置如下，从spring-mybatis.xml配置文件中可以看到，beanid为multiDataSource的配置设置了defaultTargetDataSource和targetDataSources，defaultTargetDataSource引用的dataSource数据源是3306端口的主数据源，而targetDataSources配置使用的dataSource2和dataSource3分别是端口号为3316和3326的从数据源：
      <context:property-placeholder location="classpath:jdbc.properties"/>

          <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource"
                init-method="init" destroy-method="close">
              <property name="driverClassName" value="${jdbc.driverClassName}"/>
              <property name="url" value="${jdbc.url}"/>
              <property name="username" value="${jdbc.username}"/>
              <property name="password" value="${jdbc.password}"/>
              <!-- 配置初始化大小、最小、最大 -->
              <property name="initialSize" value="1"/>
              <property name="minIdle" value="1"/>
              <property name="maxActive" value="10"/>
              <!-- 配置获取连接等待超时的时间 -->
              <property name="maxWait" value="10000"/>
              <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
              <property name="timeBetweenEvictionRunsMillis" value="60000"/>
              <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
              <property name="minEvictableIdleTimeMillis" value="300000"/>
              <property name="testWhileIdle" value="true"/>
              <!-- 这里建议配置为TRUE，防止取到的连接不可用
              testOnBorrow：申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
              testOnReturn：归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能 -->
              <property name="validationQuery" value="select 1"></property>
              <property name="testOnBorrow" value="true"/>
              <property name="testOnReturn" value="false"/>
              <!-- 打开PSCache，并且指定每个连接上PSCache的大小 -->
              <property name="poolPreparedStatements" value="true"/>
              <property name="maxPoolPreparedStatementPerConnectionSize"
                        value="20"/>
              <!-- 开启Druid的监控统计功能 -->
              <property name="filters" value="stat"></property>
          </bean>

          <bean id="dataSource2" class="com.alibaba.druid.pool.DruidDataSource"
                init-method="init" destroy-method="close">
              <property name="driverClassName" value="${jdbc.driverClassName1}"/>
              <property name="url" value="${jdbc.url1}"/>
              <property name="username" value="${jdbc.username1}"/>
              <property name="password" value="${jdbc.password1}"/>
              <!-- 配置初始化大小、最小、最大 -->
              <property name="initialSize" value="1"/>
              <property name="minIdle" value="1"/>
              <property name="maxActive" value="10"/>
              <!-- 配置获取连接等待超时的时间 -->
              <property name="maxWait" value="10000"/>
              <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
              <property name="timeBetweenEvictionRunsMillis" value="60000"/>
              <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
              <property name="minEvictableIdleTimeMillis" value="300000"/>
              <property name="testWhileIdle" value="true"/>
              <!-- 这里建议配置为TRUE，防止取到的连接不可用 -->
              <property name="validationQuery" value="select 1"></property>
              <property name="testOnBorrow" value="true"/>
              <property name="testOnReturn" value="false"/>
              <!-- 打开PSCache，并且指定每个连接上PSCache的大小 -->
              <property name="poolPreparedStatements" value="true"/>
              <property name="maxPoolPreparedStatementPerConnectionSize"
                        value="20"/>
              <!-- 开启Druid的监控统计功能 -->
              <property name="filters" value="stat"></property>
          </bean>

          <bean id="dataSource3" class="com.alibaba.druid.pool.DruidDataSource"
                init-method="init" destroy-method="close">
              <property name="driverClassName" value="${jdbc.driverClassName2}"/>
              <property name="url" value="${jdbc.url2}"/>
              <property name="username" value="${jdbc.username2}"/>
              <property name="password" value="${jdbc.password2}"/>
              <!-- 配置初始化大小、最小、最大 -->
              <property name="initialSize" value="1"/>
              <property name="minIdle" value="1"/>
              <property name="maxActive" value="10"/>
              <!-- 配置获取连接等待超时的时间 -->
              <property name="maxWait" value="10000"/>
              <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
              <property name="timeBetweenEvictionRunsMillis" value="60000"/>
              <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
              <property name="minEvictableIdleTimeMillis" value="300000"/>
              <property name="testWhileIdle" value="true"/>
              <!-- 这里建议配置为TRUE，防止取到的连接不可用 -->
              <property name="validationQuery" value="select 1"></property>
              <property name="testOnBorrow" value="true"/>
              <property name="testOnReturn" value="false"/>
              <!-- 打开PSCache，并且指定每个连接上PSCache的大小 -->
              <property name="poolPreparedStatements" value="true"/>
              <property name="maxPoolPreparedStatementPerConnectionSize"
                        value="20"/>
              <!-- 开启Druid的监控统计功能 -->
              <property name="filters" value="stat"></property>
          </bean>

          <bean id="multiDataSource" class="com.wwh.dataSource.MultiDataSource">
              <property name="defaultTargetDataSource" ref="dataSource"></property>
              <property name="targetDataSources">
                  <map>
                      <entry key="dataSource2" value-ref="dataSource2"></entry>
                      <entry key="dataSource3" value-ref="dataSource3"></entry>
                  </map>
              </property>
          </bean>
          <!-- mybatis -->
          <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
              <property name="dataSource" ref="multiDataSource"></property>
              <property name="configLocation" value="classpath:mybatis-config.xml"></property>
              <property name="mapperLocations" value="classpath:com/wwh/mapper/*.xml"></property>
          </bean>
          <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
              <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"></property>
              <property name="basePackage" value="com.wwh.mapper"></property>
          </bean>

          <!-- 添加事务管理 -->
          <tx:annotation-driven transaction-manager="transactionManager"/>

          <bean id="transactionManager"
                class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
              <property name="dataSource" ref="multiDataSource"></property>
          </bean>  
          
##### 3)切换数据源时，可以通过操作MultiDataSource类的setDataSourceKey方法对数据源进行手动切换，见代码TestDynamicDataSource中的test方法，如下：
        Cost cost = new Cost(111);
        costService.insert(cost);
        MultiDataSource.setDataSourceKey("dataSource2");
        cost.setMoney(222);
        costService.insert(cost);
        MultiDataSource.toDefault();
        cost.setMoney(333);
        costService.insert(cost);
##### 这段代码先是使用了默认数据源插入了一笔金额为111的记录，再通过 MultiDataSource.setDataSourceKey("dataSource2")方法将数据源切换到dataSource2 从数据源插入了一笔金额为222的记录，最后再通过MultiDataSource.toDefault()方法将数据源设置为默认数据源并插入一笔金额为333的记录，从而实现数据源的切换；

##### 4)对于切换数据源，工程中还有另外一种方式，即通过自定义注解的方式实现数据源的切换，这里定义了DynamicRoutingDataSource这个注解用来切换数据源，代码如下：
      package com.wwh.annotation;

      import java.lang.annotation.Documented;
      import java.lang.annotation.ElementType;
      import java.lang.annotation.Inherited;
      import java.lang.annotation.Retention;
      import java.lang.annotation.RetentionPolicy;
      import java.lang.annotation.Target;

      @Target({ElementType.METHOD,ElementType.TYPE})
      @Retention(RetentionPolicy.RUNTIME)
      @Documented
      @Inherited
      public @interface DynamicRoutingDataSource {

            String value() default "dataSource";

      }
      
##### 这个注解需要配置切面使用，所以编写了一个切面HandlerDataSourceAop.java，使用@Before和@After，在调用目标方法前，进行aop拦截，通过解析注解上的值来切换数据源。在调用方法结束后，切回默认数据源,代码如下：
      package com.wwh.aop;

      import java.lang.reflect.Method;

      import org.aspectj.lang.JoinPoint;
      import org.aspectj.lang.annotation.After;
      import org.aspectj.lang.annotation.Aspect;
      import org.aspectj.lang.annotation.Before;
      import org.aspectj.lang.annotation.Pointcut;
      import org.springframework.stereotype.Component;

      import com.wwh.annotation.DynamicRoutingDataSource;
      import com.wwh.dataSource.MultiDataSource;

      @Aspect
      @Component
      public class HandlerDataSourceAop {

            /**
             * @within匹配类上的注解
             * @annotation匹配方法上的注解
             */
            @Pointcut("@within(com.wwh.annotation.DynamicRoutingDataSource)||@annotation(com.wwh.annotation.DynamicRoutingDataSource)")
            public void pointcut() {
            }

            @Before(value = "pointcut()")
            public void beforeOpt(JoinPoint joinPoint) {
                  // 反射获取Method
                  Object target = joinPoint.getTarget();
                  Class<?> clazz = target.getClass();
                  Method[] methods = clazz.getMethods();
                  DynamicRoutingDataSource annotation = null;
                  for (Method method : methods) {
                        if (joinPoint.getSignature().getName().equals(method.getName())) {
                              annotation = method.getAnnotation(DynamicRoutingDataSource.class);
                              if (annotation == null) {
                                    annotation = joinPoint.getTarget().getClass().getAnnotation(DynamicRoutingDataSource.class);
                                    if (annotation == null) {
                                          return;
                                    }
                              }
                        }
                  }
                  String dataSourceName = annotation.value();
                  MultiDataSource.setDataSourceKey(dataSourceName);
                  System.out.println("切到" + dataSourceName + "数据库");
            }

            @After(value = "pointcut()")
            public void afterOpt() {
                  MultiDataSource.toDefault();
                  System.out.println("切回默认数据库");
            }
      }

##### 使用这个自定义注解切换数据源只需要把@DynamicRoutingDataSource注解加到方法或者类上即可，见TestDynamicDataSource.java的test2和test3方法

##### 5、spring boot版本的切换数据源主要采用的是自定义注解的方式实现，主要涉及以下几步：
##### 1）配置文件，主要的配置文件application.yml的配置如下：
      #应用配置
      spring:
        application:
          name: dynamic-datasource-springboot
      server:
        port: 80
      #日志配置
      logging:
        path: logs
        config: classpath:logback-spring.xml
        level:
          com:
            wwh:
              mapper: info
      #主数据源配置
      hikari:
        master:
          jdbc-url: jdbc:mysql://127.0.0.1:3306/db?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false
          username: root
          password:
          maximum-pool-size: 10
          pool-name: master
          connection-timeout: 30000
          idle-timeout: 600000
          max-lifetime: 1765000
          data-source-properties:
            cachePrepStmts: true
            prepStmtCacheSize: 250
            prepStmtCacheSqlLimit: 2048
            useServerPrepStmts: true
            useLocalSessionState: true
            useLocalTransactionState: true
            rewriteBatchedStatements: true
            cacheResultSetMetadata: true
            cacheServerConfiguration: true
            elideSetAutoCommits: true
            maintainTimeStats: false
        #从数据源1配置
        slave1:
          jdbc-url: jdbc:mysql://127.0.0.1:3316/db?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false
          username: root
          password:
          maximum-pool-size: 10
          pool-name: slave1
          connection-timeout: 30000
          idle-timeout: 600000
          max-lifetime: 1765000
          read-only: true
        slave2:
          jdbc-url: jdbc:mysql://127.0.0.1:3326/db?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false
          username: root
          password:
          maximum-pool-size: 10
          pool-name: slave2
          connection-timeout: 30000
          idle-timeout: 600000
          max-lifetime: 1765000
          read-only: true
      #从数据库路由规则配置
      slave:
        hosts: slave1,slave2
      #mybatis配置
      mybatis:
        type-aliases-package: com.wwh.pojo
        mapper-locations: classpath:/META-INF/mybatis/mapper/*.xml

##### 2）创建DynamicDataSource.java，继承AbstractRoutingDataSource，用于动态指定数据源类，代码如下:
      /**
       * 动态数据源实现类
       */
      @Slf4j
      public class DynamicDataSource extends AbstractRoutingDataSource{
            //数据源路由，此方用于产生要选取的数据源逻辑名称
            @Override
            protected Object determineCurrentLookupKey() {
                  //从共享线程中获取数据源名称
                  return DynamicDataSourceHolder.getDataSource();
            }
      }
      
##### 3)创建数据源切换方法注解TargetDataSource,当AOP检测到方法上有该注解时，根据注解中value对应的名称进行切换，如下：      
      /**
       * 目标数据源注解，注解在方法上指定数据源的名称
       */
      @Retention(RetentionPolicy.RUNTIME)
      @Target(ElementType.METHOD)
      public @interface TargetDataSource {
            String value();//此处接收的是数据源的名称
      }

##### 4)定义处理AOP切面DataSourceAspect.java,在切面前做数据源切换，切面完成后移除数据源名称，多个从数据源的负载均衡算法在slaveLoadBalance方法中实现，代码如下：
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
      
##### 5)定义数据源配置类DBProperties.java，代码如下：
      /**
       * 实际数据源配置
       */
      @Component
      @Data
      @ConfigurationProperties(prefix = "")
      public class DBProperties {
          //一次性从配置文件中读取所有数据源的配置
          private Map<String, HikariDataSource> hikari;
      }
      
##### 6）在DataSourceConfig.java中采用@Bean注解完成动态数据源对象的申明，代码如下：
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

                  //采用AbstractRoutingDataSource的对象包装多数据源
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
      
##### 7、在mapper接口方法上做切换，见UserInfoMapper.java，代码如下：
      public interface UserInfoMapper {
            /**
             * 从master数据源中获取用户信息
             */
            //@TargetDataSource("master")
            UserInfo selectByOddUserId(Integer id);
            /**
             * 从slave数据源中获取用户信息
             */
            @TargetDataSource("slave")
            UserInfo selectByEvenUserId(Integer id);
      }
      
##### 8、通过junit，运行AppTest结果如下，可以看到，调用了两次mapper的方法，一个使用了主数据源master，另一个根据负载均衡算法使用到了第二个从数据源slave2，如下图所示：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_07/img/%E5%A4%9A%E6%95%B0%E6%8D%AE%E6%BA%90%E5%88%87%E6%8D%A2%E6%89%A7%E8%A1%8C%E7%BB%93%E6%9E%9Cspringboot.PNG)

#### 三、（必做）读写分离-数据库框架版本2.0
