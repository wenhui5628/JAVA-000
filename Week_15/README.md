### 训练营学习总结
为期将近四个月的Java训练营结束了，在这几个月中，在老师的指导下，比较系统的学习了以下几块内容：
- JVM
- NIO
- 并发编程
- Spring和ORM等框架
- Mysql数据库和SQL
- 分库分表
- RPC和微服务
- 分布式缓存
- 分布式消息队列  
除了这些之外，老师还给我们加课讲了很多扩展的知识，包括平时工作的一些提高效率的方法，比如如何管理时间，ToDoList的使用技巧，如何做工作汇报，如何提高自己的软技能，如何面试等等，整个课程下来，其实真的感觉学到不少东西，但是老师教的这些信息量太大了，一下子吸收不过来，一个方面是自己课下投入的时间还不够多，思考和总结的时间不够，另一方面是自己时间管理方面做的不好，没有一个清晰的计划并且缺少执行力，所以导致后面有不少课程落下了，但是不管怎样，最终坚持下来学习完整个课程，还是给自己总结一下，目前学到了哪些东西，后续还需要继续提升的方向，算是给自己一个交代，我打算从自己完成的作业的维度对自己的学习做一个总结，大致的总结如下：
- 第一周([作业详情](https://github.com/wenhui5628/JAVA-000/tree/main/Week_01))：
```
1、自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内
容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。文件群里提供。
2、画一张图，展示 Xmx、Xms、Xmn、Meta、DirectMemory、Xss 这些内存参数的
关系
```
这一周，主要学到的内容是字节码是怎么一回事，以及如何解析生成的字节码文件，这一部分知识是了解JVM的最基本的知识，另外一个比较重要的，是类加载器，包括类的生命周期，类的加载时机，类加载器的类型，以及双亲委派模型是怎么回事，并通过作业写了一个简单的自定义类加载器，加深理解；最后就是学习JAVA内存模型，包括栈空间，堆空间，非堆等等，并通过作业，画了一张JVM内存图，对这些内存空间也有了更进一步的了解。

- 第二周([作业详情](https://github.com/wenhui5628/JAVA-000/tree/main/Week_02))
```
1、使用 GCLogAnalysis.java 自己演练一遍串行/并行/CMS/G1的案例。
2、使用压测工具（wrk或sb），演练gateway-server-0.0.1-SNAPSHOT.jar 示例。
3、写一段代码，使用 HttpClient 或 OkHttp 访问 http://localhost:8801，代码提交到
Github。
```
这一周，主要学到的是不同类型的垃圾回收器的特点以及区别，并通过设置不同的JVM参数，以及指定使用不同的垃圾回收器运行GCLogAnalysis.java这个程序直观的认识到各种垃圾收集器之间的区别；此外，这周还接触了netty，并通过作业学习如何使用httpClient和OkHttp发起请求到服务端，为后续使用netty实现简易网关做准备。

- 第三周([作业详情](https://github.com/wenhui5628/JAVA-000/tree/main/Week_03))

- 第四周([作业详情](https://github.com/wenhui5628/JAVA-000/tree/main/Week_04))

- 第五周([作业详情](https://github.com/wenhui5628/JAVA-000/tree/main/Week_05))

- 第六周[作业详情](https://github.com/wenhui5628/JAVA-000/tree/main/Week_06)

- 第七周[作业详情](https://github.com/wenhui5628/JAVA-000/tree/main/Week_07)

- 第八周[作业详情](https://github.com/wenhui5628/JAVA-000/tree/main/Week_08)

- 第九周[作业详情](https://github.com/wenhui5628/JAVA-000/tree/main/Week_09)

- 第十周

- 第十一周[作业详情](https://github.com/wenhui5628/JAVA-000/tree/main/Week_11)

- 第十二周[作业详情](https://github.com/wenhui5628/JAVA-000/tree/main/Week_12)

- 第十三周

- 第十四周

- 第十五周[作业详情](https://github.com/wenhui5628/JAVA-000/tree/main/Week_04)

最后，补上一张知识点汇总的思维导图：
![image](https://raw.githubusercontent.com/wenhui5628/JAVA-000/main/Week_15/JAVA%E8%BF%9B%E9%98%B6%E8%AE%AD%E7%BB%83%E8%90%A5.png)



