### 作业完成情况
#### （必做）按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。
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

#### （必做）读写分离-动态切换数据源版本1.0
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

#### （必做）读写分离-数据库框架版本2.0
