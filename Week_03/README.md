# 一、本周内容回顾  
## 1、Netty介绍以及运行原理
### 1）Netty概览
    定义：网络应用开发框架  
    特点：1、异步 2、事件驱动 3、基于NIO
    适用场景：1、服务端 2、客户端 3、TCP/UDP  
    
    需要了解的概念：
    什么是事件驱动？
    传统面向接口编程是以接口为媒介，实现调用接口者和接口实现者之间的解耦，但是这种解耦程度不是很高，如果接口发生变化，双方代码都需要变动，
    而事件驱动则是调用者和被调用者互相不知道对方，两者只和中间消息队列耦合。
    
### 2) Netty运行原理
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_03/img/netty%E5%8E%9F%E7%90%86%E5%9B%BE3.PNG)

### 3) 网络程序优化
    粘包和拆包（半包）
    例子：比如发送两个消息ABC和DEF，一次收到两个消息就是粘包，分三四次才收到消息就是拆包或者半包
    
    Netty 优化：  
    1、不要阻塞 EventLoop  
    2、系统参数优化  
    ulimit -a /proc/sys/net/ipv4/tcp_fin_timeout, TcpTimedWaitDelay  
    3、缓冲区优化  
    SO_RCVBUF/SO_SNDBUF/SO_BACKLOG/ REUSEXXX  
    4、心跳频率周期优化      
    心跳机制与断线重连       
    5、内存与 ByteBuffer 优化  
    DirectBuffer与HeapBuffer  
    6、其他优化  
    - ioRatio: - Watermark  
    - TrafficShaping  

### 4) Netty典型应用  
####   API 网关，实现的四大职能如下：
       1、请求接入：作为所有API接口服务请求的接入点
       2、业务聚合：作为所有后端业务服务的聚合点
       3、中介策略：实现安全、验证、路由、过滤、流控等策略
       4、统一管理：对所有API服务和策略进行统一管理
       


## 2、Java并发编程
### 1) 多线程基础
### 2）Java多线程
### 3）线程安全
### 4）线程池原理与应用  


# 二、作业完成情况  

## 1、按今天的课程要求，实现一个网关，基础代码可以 fork：https://github.com/kimmking/JavaCourseCodes02nio/nio02 文件夹下  

### 1)整合你上次作业的 httpclient/okhttp  
### 2)实现过滤器  
### 3)实现路由  
