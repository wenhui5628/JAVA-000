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
#### 粘包和拆包（半包）
    例子：比如发送两个消息ABC和DEF，一次收到两个消息就是粘包，分三四次才收到消息就是拆包或者半包
    
#### Netty 优化：  
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
       
####   网关的分类
       1、流量网关
       关注稳定与安全，全局性流控，日志统计，防止 SQL 注入，防止 Web 攻击，屏蔽工具扫描，黑白 IP 名单，证书/加解密处理
       2、业务网关
       提供更好的服务，服务级别流控，服务降级与熔断，路由与负载均衡、灰度策略，服务过滤、聚合与发现，权限验证与用户等级策略，业务规则与参数校验，多级缓存策略
       


## 2、Java并发编程
### 1) 多线程基础
        操作系统以线程作为基本的调度单元
        
        进程与线程的区别：
        1、进程是操作系统资源分配的基本单位，而线程是处理器任务调度和执行的基本单位。还存在资源开销、包含关系、内存分配、影响关系、执行过程等区别。
        2、同一进程的线程共享本进程的地址空间和资源，而进程之间的地址空间和资源相互独立。
        
####   线程创建过程：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_03/img/%E7%BA%BF%E7%A8%8B%E5%88%9B%E5%BB%BA%E8%BF%87%E7%A8%8B.png)
        
### 2）Java多线程
####   创建线程的两种方式：
        1、实现Runnable接口
        2、继承Thread
        
####    线程状态：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_03/img/%E7%BA%BF%E7%A8%8B%E7%8A%B6%E6%80%81%E5%9B%BE.png)
        
####    Thread 类重要属性和方法
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_03/img/Thread%E9%87%8D%E8%A6%81%E5%B1%9E%E6%80%A7%E6%96%B9%E6%B3%95.png)
        
####    wait & notify的区别
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_03/img/wait%26notify.png)
        
####    Thread 的状态改变操作
        1. Thread.sleep(long millis)，一定是当前线程调用此方法，当前线程进入 TIMED_WAITING 状态，但不释放对象锁，millis 后线程自动苏醒进入就绪状态。
        作用：给其它线程执行机会的最佳方式。
        2. Thread.yield()，一定是当前线程调用此方法，当前线程放弃获取的 CPU 时间片，但不释放锁资源，由运行状态变为就绪状态，让 OS 再次选择线程。
        作用：让相同优先级的线程轮流执行，但并不保证一定会轮流执行。实际中无法保证yield() 达到让步目的，因为让步的线程还有可能被线程调度程序再次选中。Thread.yield() 不会导致阻塞。
        该方法与sleep() 类似，只是不能由用户指定暂停多长时间。
        3. t.join()/t.join(long millis)，当前线程里调用其它线程 t 的 join 方法，当前线程进入WAITING/TIMED_WAITING 状态，当前线程不会释放已经持有的对象锁。
        线程t执行完毕或者 millis 时间到，当前线程进入就绪状态。
        4. obj.wait()，当前线程调用对象的 wait() 方法，当前线程释放对象锁，进入等待队列。依靠 notify()/notifyAll() 唤醒或者 wait(long timeout) timeout 时间到自动唤醒。
        5. obj.notify() 唤醒在此对象监视器上等待的单个线程，选择是任意性的。notifyAll() 唤醒在此对象监视器上等待的所有线程。
        
####    Thread 的中断与异常处理
        1. 线程内部自己处理异常，不溢出到外层。
        2. 如果线程被 Object.wait, Thread.join 和 Thread.sleep 三种方法之一阻塞，此时调用该线程的interrupt() 方法，那么该线程将抛出一个 InterruptedException 中断异常（该线程必须事
        先预备好处理此异常），从而提早地终结被阻塞状态。如果线程没有被阻塞，这时调用interrupt() 将不起作用，直到执行到 wait(),sleep(),join() 时,才马上会抛出InterruptedException。
        3. 如果是计算密集型的操作怎么办？
        分段处理，每个片段检查一下状态，是不是要终止。
        
####    Thread 状态图如下：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_03/img/Thread%E7%8A%B6%E6%80%81%E5%9B%BE.png)   
        
        
### 3）线程安全
####    多线程执行会遇到什么问题?
        多个线程竞争同一资源时，如果对资源的访问顺序敏感，就称存在竞态条件。
        导致竞态条件发生的代码区称作临界区。
        不进行恰当的控制，会导致线程安全问题
        
####    并发相关的性质
#####   原子性：
        原子操作，注意跟事务 ACID 里原子性的区别与联系
        对基本数据类型的变量的读取和赋值操作是原子性操作，即这些操作是不可被中断的，要么执行，要么不执行。
        可见性：对于可见性，Java 提供了 volatile 关键字来保证可见性。
        当一个共享变量被 volatile 修饰时，它会保证修改的值会立即被更新到主存，当有其他线程需要读取时，它会去内存中读取新值。
        另外，通过 synchronized 和 Lock 也能够保证可见性，synchronized 和 Lock 能保证同一时刻只有一个线程获取锁然后执行同步代码，并且在释放锁之前会将对变量的修改刷新到主存当中。
#####   注意：volatile 并不能保证原子性。
        
#####   有序性：
        Java 允许编译器和处理器对指令进行重排序，但是重排序过程不会影响到单线程程序的执行，却会影响到多线程并发执行的正确性。可以通过 volatile 关键字来保证一定的“有序      
        性”（synchronized 和 Lock也可以）。
        
#####   happens-before 原则（先行发生原则）：
        1. 程序次序规则：一个线程内，按照代码先后顺序
        2. 锁定规则：一个 unLock 操作先行发生于后面对同一个锁的 lock 操作
        3. Volatile 变量规则：对一个变量的写操作先行发生于后面对这个变量的读操作
        4. 传递规则：如果操作 A 先行发生于操作 B，而操作 B 又先行发生于操作 C，则可以得出 A 先于 C
        5. 线程启动规则：Thread 对象的 start() 方法先行发生于此线程的每个一个动作
        6. 线程中断规则：对线程 interrupt() 方法的调用先行发生于被中断线程的代码检测到中断事件的发生
        7. 线程终结规则：线程中所有的操作都先行发生于线程的终止检测，我们可以通过 Thread.join() 方法结束、Thread.isAlive() 的返回值手段检测到线程已经终止执行
        8. 对象终结规则：一个对象的初始化完成先行发生于他的 finalize() 方法的开始
       
####   并发相关的处理方法
#####  synchronized 的实现
        1. 使用对象头标记字(Object monitor)
        2. Synchronized 方法优化
        3. 偏向锁: BiaseLock
        
        哪种方式性能更高？
        • 同步块 : 粒度小
        • 同步方法: 专有指令
        
 #####  volatile关键字
        1. 每次读取都强制从主内存刷数据
        2. 适用场景： 单个线程写；多个线程读
        3. 原则： 能不用就不用，不确定的时候也不用
        4. 替代方案： Atomic 原子操作类
        
 #####  final关键字
        final关键字的用法如下：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_03/img/final.png)
        
        final 声明的引用类型与原生类型在处理时有什么区别?
        Java 里的常量替换。写代码最大化用 final 是个好习惯。

### 4）线程池原理与应用  
####   线程池的四大组件
       1. Excutor: 执行者 – 顶层接口
       2. ExcutorService: 接口 API
       3. ThreadFactory: 线程工厂
       4. Excutors: 工具类
       
####   类图如下：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_03/img/%E7%BA%BF%E7%A8%8B%E6%B1%A0%E7%9B%B8%E5%85%B3%E7%B1%BB%E5%9B%BE.png)       
####   Executor – 执行者
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_03/img/Executor2.png)        
####   ExecutorService
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_03/img/ExecutorService.png)
####   ThreadFactory
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_03/img/ThreadFactory2.png)        
####   ThreadPoolExecutor 提交任务逻辑:
        1. 判断 corePoolSize 【创建】
        2. 加入 workQueue
        3. 判断 maximumPoolSize 【创建】
        4. 执行拒绝策略处理器
        
####   ThreadPoolExecutor属性和方法
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_03/img/ThreadPoolExecutor.png)
        
####   线程池参数
#####  缓冲队列
        BlockingQueue 是双缓冲队列。BlockingQueue 内部使用两条队列，允许两个线程同时向队列一个存储，一个取出操作。在保证并发安全的同时，提高了队列的存取效率。
        1. ArrayBlockingQueue:规定大小的 BlockingQueue，其构造必须指定大小。其所含的对象是 FIFO 顺序排序的。
        2. LinkedBlockingQueue:大小不固定的 BlockingQueue，若其构造时指定大小，生成的 BlockingQueue 有大小限制，不指定大小，其大小有 Integer.MAX_VALUE 来
        决定。其所含的对象是 FIFO 顺序排序的。
        3. PriorityBlockingQueue:类似于 LinkedBlockingQueue，但是其所含对象的排序不是 FIFO，而是依据对象的自然顺序或者构造函数的 Comparator 决定。
        4. SynchronizedQueue:特殊的 BlockingQueue，对其的操作必须是放和取交替完成。
        
#####  拒绝策略
        1. ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出 RejectedExecutionException异常。
        2. ThreadPoolExecutor.DiscardPolicy：丢弃任务，但是不抛出异常。
        3. ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新提交被拒绝的任务。
        4. ThreadPoolExecutor.CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务。
        
####  创建线程池方法
#####   1. newSingleThreadExecutor
        创建一个单线程的线程池。这个线程池只有一个线程在工作，也就是相当于单线程串行执行所有任
        务。如果这个唯一的线程因为异常结束，那么会有一个新的线程来替代它。此线程池保证所有任务
        的执行顺序按照任务的提交顺序执行。
#####   2.newFixedThreadPool
        创建固定大小的线程池。每次提交一个任务就创建一个线程，直到线程达到线程池的最大大小。线
        程池的大小一旦达到最大值就会保持不变，如果某个线程因为执行异常而结束，那么线程池会补充
        一个新线程。
#####   3. newCachedThreadPool
        创建一个可缓存的线程池。如果线程池的大小超过了处理任务所需要的线程，
        那么就会回收部分空闲（60秒不执行任务）的线程，当任务数增加时，此线程池又可以智能的添 加新线程来处理任务。此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统（或者
        说JVM）能够创建的最大线程大小。
#####   4.newScheduledThreadPool
        创建一个大小无限的线程池。此线程池支持定时以及周期性执行任务的需求。

# 二、作业完成情况  

## 1、按今天的课程要求，实现一个网关，基础代码可以 fork：https://github.com/kimmking/JavaCourseCodes02nio/nio02 文件夹下  
### 1）整合你上次作业的 httpclient/okhttp  
####  作业一整合后的处理流程图如下：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_03/img/%E7%BD%91%E5%85%B31.png)

####   涉及修改的代码如下：
#####  1、根据老师的HttpOutboundHandler改造一个OkhttpOutboundHandler类，并将上周完成的客户端代码融合到这个类中，具体代码见工程中的这个类
       io.github.kimmking.gateway.outbound.okhttp.OkhttpOutboundHandler
        
#####  2、修改HttpInboundHandler类，这里由于第三步路由的目标访问机器有多台，故这里构造OkhttpOutboundHandler对象时传入集合类型的endpoints，指定所有目标访问机器,修改代码如下：
       private final List<String> endpoints;
       private OkhttpOutboundHandler handler;

       public HttpInboundHandler(List<String> proxyServer) {
            this.endpoints = proxyServer;
            //整合OkHttp客户端到网关
            handler = new OkhttpOutboundHandler(this.endpoints);
       }
       
        
           
### 2)实现过滤器  
####   作业二实现的效果如下图所示：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_03/img/%E7%BD%91%E5%85%B32.png)
####   涉及修改的代码如下：
####   1、新增HttpRequestHeaderFilter类，继承ChannelInboundHandlerAdapter并实现HttpRequestFilter接口，在filter方法中往请求报文头塞入nio字段，并重写channelRead方法，在channelRead方法中先调用filter方法给请求报文头赋值，再调用ChannelHandlerContext的fireChannelRead方法讲数据流往后传递，代码如下：
        @Override
        public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
            fullRequest.headers().set("nio","wuwenhui");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            filter(fullHttpRequest, ctx);
            ctx.fireChannelRead(fullHttpRequest);
        }
        
        2、在类HttpInboundInitializer的initChannel方法中加入过滤器HttpRequestHeaderFilter，如下：
        @Override
        public void initChannel(SocketChannel ch) {
            ChannelPipeline channelPipeline = ch.pipeline();
            channelPipeline.addLast(new HttpServerCodec());
            channelPipeline.addLast(new HttpObjectAggregator(1024 * 1024));
            channelPipeline.addLast(new HttpRequestHeaderFilter());
            channelPipeline.addLast(new HttpInboundHandler(this.proxyServer));
        }

### 3)实现路由  
####   作业三实现的效果如下图所示：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_03/img/%E7%BD%91%E5%85%B33.png)
####   涉及修改的代码如下：
####   1、修改NettyServerApplication类，初始化目标服务机器列表，修改代码如下：
        final static List<String> endpoints = new ArrayList<>();

        /***
         * 初始化时生成一个目标服务器的IP列表，用于端口和IP之间的映射关系
         */
        static{
            endpoints.add("http://localhost:8088");
            endpoints.add("http://localhost:8801");
            endpoints.add("http://localhost:8802");
            endpoints.add("http://localhost:8803");
        }
        
        HttpInboundServer server = new HttpInboundServer(port, endpoints);
        
####    2、新增HttpEndpointRouterImpl类，实现HttpEndpointRouter接口，使用生成随机数的方法随机将请求转到目标机器，如下：
        public String route(List<String> endpoints) {
            int proxyServerKey = (int)(Math.random()*4);
            System.out.println("生成随机数:"+proxyServerKey);
            return endpoints.get(proxyServerKey);
        }
        
####    3、在OkhttpOutboundHandler类的handle方法中调用HttpEndpointRouterImpl的route方法，获取指定的目标服务器地址，如下：
        public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
            String proxyServer = new HttpEndpointRouterImpl().route(endpoints);
            String backendUrl = proxyServer.endsWith("/") ? proxyServer.substring(0, proxyServer.length() - 1) : proxyServer;
            final String url = backendUrl + fullRequest.uri();
            proxyService.submit(() -> fetchGet(fullRequest, ctx, url));
        }
        
        
        
