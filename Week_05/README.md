## 本周作业完成情况
### （必做）写代码实现Spring Bean的装配，方式越多越好（XML、Annotation都可以）,提交到Github。
###  见spring-bean-create工程
###  主要实现了三个方式的Bean装配
####  1、xml，又分为以下几种：
##### 1）基于xml的普通装配,如下：
          <!-- 1、基于xml的普通装配 -->
          <bean id="student" class="com.geek.homework.week5.bean.xml.Student">
              <constructor-arg value="张三"/>
          </bean>
           
##### 2）byName形式的自动装配，如下：
         <!--
              2、byName形式的自动装配
              byName自动装配的执行过程：在这个Bean的定义中，找到setter方法，这里是setStudent()，
              其name是student，
              根据这个name（student）找打到id/name是student的Bean实例，将这个实例自动注入
              说明：自动装配只能完成setter形式的依赖注入，不能完成构造器方式的依赖注入，
              且只能注入其它Bean，不能注入String、数组、集合等Java自带的类型
          -->
          <bean id="teacher1" class="com.geek.homework.week5.bean.xml.Teacher" autowire="byName"/>
         
##### 3）根据要注入的Bean的类型来自动装配，如下：
             <!--
              3、根据要注入的Bean的类型来自动装配
              byType自动装配的执行过程：在这个Bean的定义中，找到setter方法，
              找到setter方法要注入的Bean的类型（Student），在Spring容器中找到Student类型的实例，注入
            -->
          <!--
              4、如果Spring容器中该依赖有多个配置，比如：张三和李四两个配置，都是student类型，
              则会报错，Spring容器不知道要注入的依赖是哪一个
          -->
          <bean id="student11" class="com.geek.homework.week5.bean.xml.Student">
              <constructor-arg value="张三"/>
          </bean>
          <bean id="student1" class="com.geek.homework.week5.bean.xml.Student">
              <constructor-arg value="李四"/>
          </bean>     
          <bean id="teacher2" class="com.geek.homework.week5.bean.xml.Teacher" autowire="byType"/>
            
##### 4）用构造器注入依赖，如下：
            <!--
              用构造器注入依赖，Spring容器会自动根据构造器中参数类型，用byType方式注入对应类型的依赖
              只能有一个构造器，否则Spring容器不知道使用哪个构造器
          -->
             <bean id="teacher3" class="com.geek.homework.week5.bean.xml.Teacher" autowire="constructor"/>
         
#####  5）在<beans>中设置默认的自动装配方式,如下：
       需要在beans配置中加上这个配置default-autowire="byName"，这里表示默认的自动装配方式是byName，如下：
            <!--
              在<beans>中设置默认的自动装配方式，在需要使用自动装配的<bean>指定autowire="default"，
              这样该<bean>使用的自动装配方式就是<beans>中设置的默认方式 
            -->
             <bean id="teacher4" class="com.geek.homework.week5.bean.xml.Teacher" autowire="default"/>
      
####  2、Annotation
            <!--
              使用注解的方式注入bean，基于注解的装配都需要用<context:annotation-config />开启注解装配，
              这句代码是告诉Spring容器，下面的这些bean使用的是注解装配
             -->
          <context:annotation-config />
          <!-- 用@Autowired -->
          <bean id="teacher5" class="com.geek.homework.week5.bean.annotation.Teacher"/>

          <!-- 用@Autowired和@Qualifier(value="student2")-->
          <bean id="student2" class="com.geek.homework.week5.bean.annotation.Student">
              <constructor-arg value="张三"/>
          </bean>
          <bean id="teacher6" class="com.geek.homework.week5.bean.annotation.Teacher2"/>

          <!-- @Resource方式 -->
          <bean id="teacher7" class="com.geek.homework.week5.bean.annotation.Teacher3"/>

          <!--
              使用@Service和component-scan
              会自动开启注解，所以不必再写context:annotataion-config
           -->
          <context:component-scan base-package="com.geek.homework.week5.bean.annotation.service"/>
          <bean id="student3" class="com.geek.homework.week5.bean.annotation.service.Student"/>
          
####   3、Java配置类
#####      需要定义一个config类作为Bean注入的配置类，如Config.java，代码如下：
            package com.geek.homework.week5.bean.config;

            import org.springframework.beans.factory.annotation.Value;
            import org.springframework.context.annotation.Bean;
            import org.springframework.context.annotation.Configuration;


            //表示这个类是用来配置Bean的
            @Configuration
            class Config {
                @Value("张三")
                String name;   //创建一个成员变量，相当于String name="张三";

                @Bean(name = "studentConfig")   //配置一个Bean，相当于xml中的一个<bean>
                public StudentConfig studentConfig() {
                    StudentConfig studentConfig = new StudentConfig(name);   //创建并返回Bean的实例。
                    return studentConfig;
                }

                @Bean(name = "teacherConfig")
                public TeacherConfig teacherConfig() {
                    return new TeacherConfig(studentConfig());  //创建并返回Bean的实例，因为写了构造器，所以可以直接构造器注入依赖。可直接调用本类中的其它方法创建依赖的实例，注入。
                }
            }
            
#####       然后在主类中的main方法调用方式如下：
            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);  //注意，和xml配置不同。参数是配置类。
            TeacherConfig teacher = applicationContext.getBean("teacherConfig", TeacherConfig.class);
            teacher.say();


       
         
#### （超级挑战）尝试使用ByteBuddy与Instrument实现一个简单JavaAgent实现无侵入下的 AOP；         
      见java-agent和java-agent-test
         
#### （必做）给前面课程提供的 Student/Klass/School 实现自动配置和 Starter。        
      见my-springboot-starter工程
      
#### （必做）研究一下 JDBC 接口和数据库连接池，掌握它们的设计和用法：  
####  1）使用 JDBC 原生接口，实现数据库的增删改查操作。  
####  2）使用事务，PrepareStatement 方式，批处理方式，改进上述操作。     
####  3）配置 Hikari 连接池，改进上述操作。提交代码到 Github。 
      见jdbc工程
