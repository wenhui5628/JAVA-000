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
         
#####  5）在beans中设置默认的自动装配方式,如下：
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
##### 见java-agent和java-agent-test
##### 思路：我理解的无侵入，是指目前已经有在运行的代码，在不改动原代码和配置的情况下，使用AOP对原代码做一个增强，下面分别用了ByteBuddy和Instrument实现了一下这个问题，如下：
##### 一、使用ByteBuddy方式
##### 1.先准备一个业务类Service.java，如下：
          package com.spring.aop.demo.bytebuddy;

          public class Service {
              @Log
              public int foo(int value) {
                  System.out.println("foo: " + value);
                  return value;
              }

              public int bar(int value) {
                  System.out.println("bar: " + value);
                  return value;
              }
          }

##### 2、准备一个对业务类增强的一个切面类LoggerAdvisor.java，如下：
          package com.spring.aop.demo.bytebuddy;

          import net.bytebuddy.asm.Advice;

          import java.lang.reflect.Method;
          import java.util.Arrays;

          public class LoggerAdvisor {
              @Advice.OnMethodEnter
              public static void onMethodEnter(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments) {
                  if (method.getAnnotation(Log.class) != null) {
                      System.out.println("Enter " + method.getName() + " with arguments: " + Arrays.toString(arguments));
                  }
              }

              @Advice.OnMethodExit
              public static void onMethodExit(@Advice.Origin Method method, @Advice.AllArguments Object[] arguments, @Advice.Return Object ret) {
                  if (method.getAnnotation(Log.class) != null) {
                      System.out.println("Exit " + method.getName() + " with arguments: " + Arrays.toString(arguments) + " return: " + ret);
                  }
              }
          }

##### 3、运行的入口程序如下：
          package com.spring.aop.demo.bytebuddy;

          import net.bytebuddy.ByteBuddy;
          import net.bytebuddy.asm.Advice;
          import net.bytebuddy.matcher.ElementMatchers;

          public class Test {
              public static void main(String[] args) throws Exception {
                  Service service = new ByteBuddy()
                          .subclass(Service.class)    //表示对Service类做增强
                          .method(ElementMatchers.any())//对任意方法做增强
                          .intercept(Advice.to(LoggerAdvisor.class))//表示使用LoggerAdvisor这个类对服务类做增强
                          .make()
                          .load(Service.class.getClassLoader())
                          .getLoaded()
                          .newInstance();
                  service.bar(123);
                  service.foo(456);
              }
          }
          
 ##### 4、运行结果如下，Enter和Exit的打印信息就是对服务类做的增强
          bar: 123
          Enter foo with arguments: [456]
          foo: 456
          Exit foo with arguments: [456] return: 456
          、
 ####  二、使用Instrument的方式，这种方式会实现一个javaAgent，作为业务类运行时的一个引擎对类进行增强，如下：
 ##### 1、准备引擎类FirstAgent，如下:
          package com.spring.aop.demo;

          import javassist.ClassPool;
          import javassist.CtClass;
          import javassist.CtMethod;

          import java.lang.instrument.ClassFileTransformer;
          import java.lang.instrument.IllegalClassFormatException;
          import java.security.ProtectionDomain;

          public class FirstAgent implements ClassFileTransformer {
              public final String injectedClassName = "com.aop.wwh.Aop";
          //    public final String methodName = "hello";

              public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws                   IllegalClassFormatException {
                  className = className.replace("/", ".");
                  if (className.contains(injectedClassName)) {    //定义需要增强的类的规则，这里是所有类名包含com.aop.wwh.Aop这个关键字的都增强
                      CtClass ctclass = null;
                      try {
                          ctclass = ClassPool.getDefault().get(className);// 使用全称,用于取得字节码类<使用javassist>
                          CtMethod[] ctMethods = ctclass.getDeclaredMethods();
                          for(CtMethod ctmethod : ctMethods){ //这里表示对所有业务方法做增强
                              System.out.println("===调用的业务方法:"+ctmethod.getName());
                              ctmethod.insertBefore("{System.out.println(\"打印记录日志\"); }");
                          }
          //                CtMethod ctmethod = ctclass.getDeclaredMethod(methodName);// 得到这方法实例
          //                ctmethod.insertBefore("System.out.println(11111111);");
                          return ctclass.toBytecode();
                      } catch (Exception e) {
                          System.out.println(e.getMessage());
                          e.printStackTrace();
                      }
                  }
                  return null;
              }
          }

##### 2、引擎的入口类代码如下：
          package com.spring.aop.demo;

          import java.lang.instrument.Instrumentation;

          /**
           * 入口类
           */
          public class App {
              public static void premain(String agentOps, Instrumentation inst) {
                  System.out.println("=========premain方法执行========");
                  System.out.println(agentOps);
                  // 添加Transformer
                  inst.addTransformer(new FirstAgent());
              }
          }
##### 3、将这个引擎类打成jar包，aop-demo-1.0-SNAPSHOT.jar

##### 4、运行目标业务类，运行时加上以下参数：
      -javaagent:D:\geek\homework\JAVA-000\Week_05\java-agent\target\aop-demo-1.0-SNAPSHOT.jar
      
##### 5、运行结果如下：      
          =========premain方法执行========
          ===调用的业务方法:main
          ===调用的业务方法:hello
          打印记录日志
          打印记录日志
          this is agent-demo output
          
         
### （必做）给前面课程提供的 Student/Klass/School 实现自动配置和 Starter。        
####  见my-springboot-starter工程
####  步骤如下：
####  1、使用@SpringBootApplication标注SpringBoot 的主配置类，工程中指定了DemoApplication.java作为主配置类，代码如下：
          package com.geek.homework.spring.demo;

          import com.geek.homework.spring.demo.component.Klass;
          import com.geek.homework.spring.demo.component.School;
          import com.geek.homework.spring.demo.component.Student;
          import lombok.extern.slf4j.Slf4j;
          import org.springframework.boot.SpringApplication;
          import org.springframework.boot.autoconfigure.SpringBootApplication;
          import org.springframework.context.ConfigurableApplicationContext;

          import java.util.Arrays;

          @SpringBootApplication
          @Slf4j
          public class DemoApplication {

                    public static void main(String[] args) {
                              ConfigurableApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
                              Student student = applicationContext.getBean(Student.class);
                              School school = applicationContext.getBean(School.class);
                              Klass klass = applicationContext.getBean(Klass.class);
                              log.info("studentId:"+student.getId() +",studentName:"+student.getName());
                              log.info("schoolInfo have students:"+ Arrays.toString(school.getClass1().getStudents().toArray()) +",and student100                                                           info:"+school.getStudent100());
                              log.info("klass info:"+ Arrays.toString(klass.getStudents().toArray()));
                    }

          }
#### 2、使用@Configuration定义配置类，并通过@EnableConfigurationProperties注解指定配置类使用的读取application.yml配置文件的工具类，代码如下：
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

#### 3、在spring.factories配置文件中指定MyObjectAutoConfiguration作为自动装配的starter，配置如下：
          org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
          com.geek.homework.spring.demo.configuration.MyObjectAutoConfiguration

#### 4、运行结果如下：

      
### （必做）研究一下 JDBC 接口和数据库连接池，掌握它们的设计和用法：  
####  1）使用 JDBC 原生接口，实现数据库的增删改查操作。  
####  2）使用事务，PrepareStatement 方式，批处理方式，改进上述操作。     
####  3）配置 Hikari 连接池，改进上述操作。提交代码到 Github。 
      见jdbc工程
