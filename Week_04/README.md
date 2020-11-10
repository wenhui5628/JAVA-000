# 本周作业完成情况：
## Week04 作业题目（周四）：

### 2.（必做）思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程？写出你的方法，越多越好，提交到 Github。
#### 采用的方式有以下11种：
##### 1）使用FutureTask类和Thread类结合调起一个异步线程，见AsynTask1.java
      package com.geek.homework.week4.wwh;

      import java.util.concurrent.Callable;
      import java.util.concurrent.ExecutionException;
      import java.util.concurrent.FutureTask;

      /**
       * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
       * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
       * <p>
       * 使用FutureTask类和Thread类结合调起一个异步线程
       */
      public class AsynTask1 {

          public static void main(String[] args) {
              System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
              long start=System.currentTimeMillis();
              // 1、创建一个futureTask对象，并将实现了Callable接口的Task对象通过构造器注入到futureTask对象中
              FutureTask<Integer> futureTask = new FutureTask<>(new Task1());

              //2、通过Thread类异步调起一个线程，并通过start方法调用task对象中的call方法执行计算
              new Thread(futureTask).start();

              //3、获取异步调起的线程的返回值
              int result = 0;
              try {
                  result = futureTask.get();
              } catch (InterruptedException e) {
                  e.printStackTrace();
              } catch (ExecutionException e) {
                  e.printStackTrace();
              }

              // 4、输出异步线程返回的结果
              System.out.println("异步计算结果为："+result);

              System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

              System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
          }
      }
          class Task1 implements Callable {

              @Override
              public Integer call() throws Exception {
                  return sum();
              }

              private int sum() {
                  System.out.println("===" + Thread.currentThread().getName() + "线程正在执行计算");
                  int result = fibo(36);
                  System.out.println("===" + Thread.currentThread().getName() + "线程完成计算");
                  return result;
              }

              private int fibo(int a) {
                  if (a < 2)
                      return 1;
                  return fibo(a - 1) + fibo(a - 2);
              }
          }
##### 2）使用FutureTask类和Executors类的newCachedThreadPool方法结合调起一个异步线程，见AsynTask2.java
            package com.geek.homework.week4.wwh;

            import java.util.concurrent.*;

            /**
             * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
             * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
             * <p>
             * 使用FutureTask类和Executors类的newCachedThreadPool方法结合调起一个异步线程
             */
            public class AsynTask2 {
                public static void main(String[] args) throws InterruptedException, ExecutionException {
                    System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
                    long start=System.currentTimeMillis();

                    //1、通过Executors工具类的newCachedThreadPool方法创建一个可缓存的线程池
                    ExecutorService executor = Executors.newCachedThreadPool();
                    //2、构造一个FutureTask，并将实现了Callable接口的Task对象通过构造器注入到futureTask对象中
                    FutureTask futureTask = new FutureTask(new Task2());

                    //3、通过ExecutorService的submit方法通过FutureTask对象，最终调用Task对象的call方法
                    executor.submit(futureTask);

                    //4、获取Task的call方法的返回值
                    System.out.println("异步计算结果为："+futureTask.get());
                    System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

                    //5、关闭线程池
                    executor.shutdown();

                    System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
                }
            }

                class Task2 implements Callable {

                    @Override
                    public Integer call() throws Exception {
                        return sum();
                    }

                    private int sum() {
                        System.out.println("===" + Thread.currentThread().getName() + "线程正在执行计算");
                        int result = fibo(36);
                        System.out.println("===" + Thread.currentThread().getName() + "线程完成计算");
                        return result;
                    }

                    private int fibo(int a) {
                        if (a < 2)
                            return 1;
                        return fibo(a - 1) + fibo(a - 2);
                    }
                }

##### 3）使用Future<Integer>和Executors类的newCachedThreadPool方法结合调起一个异步线程，见AsynTask3.java
            package com.geek.homework.week4.wwh;

            import java.util.concurrent.*;

            /**
             * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
             * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
             * <p>
             * 使用Future<Integer>和Executors类的newCachedThreadPool方法结合调起一个异步线程
             */
            public class AsynTask3 {
                public static void main(String[] args) throws InterruptedException, ExecutionException {
                    System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");

                    long start=System.currentTimeMillis();
                    //1、通过Executors工具类的newCachedThreadPool方法创建一个可缓存的线程池
                    ExecutorService executor = Executors.newCachedThreadPool();

                    //2、通过ExecutorService的submit方法调用Task对象
                    Future<Integer> result1 = executor.submit(new Task3());
                    System.out.println("异步计算结果为："+result1.get());
                    System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
                    executor.shutdown();

                    System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
                }
            }

            class Task3 implements Callable {

                @Override
                public Integer call() throws Exception {
                    return sum();
                }

                private int sum() {
                    System.out.println("===" + Thread.currentThread().getName() + "线程正在执行计算");
                    int result = fibo(36);
                    System.out.println("===" + Thread.currentThread().getName() + "线程完成计算");
                    return result;
                }

                private int fibo(int a) {
                    if (a < 2)
                        return 1;
                    return fibo(a - 1) + fibo(a - 2);
                }
            }

##### 4）使用Future<Integer>和ThreadFactory结合调起一个异步线程，见AsynTask4.java
            package com.geek.homework.week4.wwh;

            import com.geek.homework.week4.wwh.factory.CustomThreadFactory;

            import java.util.concurrent.*;

            /**
             * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
             * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
             * <p>
             * 使用Future<Integer>和ThreadFactory结合调起一个异步线程
             */
            public class AsynTask4 {
                public static void main(String[] args) throws InterruptedException, ExecutionException {
                    System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");

                    long start=System.currentTimeMillis();
                    //1、通过ThreadFactory获取一个ExecutorService对象
                    ExecutorService executor = initThreadPoolExecutor();

                    //2、通过ExecutorService的submit方法调用Task对象
                    Future<Integer> result1 = executor.submit(new Task4());

                    //3、获取并打印返回结果
                    System.out.println("异步计算结果为："+result1.get());
                    System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

                    //4、关闭线程池
                    executor.shutdown();

                    System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
                }

                /***
                 * 初始化线程池
                 * @return
                 */
                public static ThreadPoolExecutor initThreadPoolExecutor() {
                    int coreSize = Runtime.getRuntime().availableProcessors();
                    int maxSize = Runtime.getRuntime().availableProcessors() * 2;
                    BlockingQueue<Runnable> workQueue = new
                            LinkedBlockingDeque<>(500);
                    CustomThreadFactory threadFactory = new CustomThreadFactory();
                    ThreadPoolExecutor executor = new ThreadPoolExecutor(coreSize,
                            maxSize,
                            1, TimeUnit.MINUTES, workQueue, threadFactory);
                    return executor;
                }

            }

            class Task4 implements Callable {

                @Override
                public Integer call(){
                    return sum();
                }

                private int sum() {
                    System.out.println("===" + Thread.currentThread().getName() + "线程正在执行计算");
                    int result = fibo(36);
                    System.out.println("===" + Thread.currentThread().getName() + "线程完成计算");
                    return result;
                }

                private int fibo(int a) {
                    if (a < 2)
                        return 1;
                    return fibo(a - 1) + fibo(a - 2);
                }
            }
      
##### 5）使用CountDownLatch和ExecutorService结合调起一个异步线程，见AsynTask5.java
            package com.geek.homework.week4.wwh;

            import java.util.concurrent.*;

            /**
             * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
             * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
             * <p>
             * 使用CountDownLatch和ExecutorService结合调起一个异步线程
             */
            public class AsynTask5 {
                public static void main(String[] args) throws InterruptedException {
                    System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
                    CountDownLatch latch = new CountDownLatch(1);  //定义一个初始值为1的计数器
                    //初始化线程池
                    ExecutorService executor = Executors.newCachedThreadPool();
                    //构造将要执行的任务对象，并将计数器传入此对象
                    Task5 task5 = new Task5(latch);
                    //通过线程池异步调起任务
                    executor.execute(()->{
                        task5.sum();
                    });
                    //实现对计数器等于 0 的等待，当计数器的值为0时，就可以进行后面的操作
                    latch.await();
                    //获取任务的执行结果
                    System.out.println("异步计算结果为："+task5.getValue());
                    System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
                    executor.shutdown();
                }
            }

                class Task5 {
                    private CountDownLatch latch;   //计数器

                    public Task5(CountDownLatch latch){
                        this.latch = latch;
                    }

                    private volatile Integer result = null;

                    public int sum() {
                        System.out.println("===" + Thread.currentThread().getName() + "线程正在执行计算");
                        result = fibo(36);
                        System.out.println("===" + Thread.currentThread().getName() + "线程完成计算");
                        latch.countDown();  //计算完毕后，对计数器的值减1
                        return result;
                    }

                    private int fibo(int a) {
                        if (a < 2)
                            return 1;
                        return fibo(a - 1) + fibo(a - 2);
                    }

                    public int getValue(){
                        return result;
                    }
                }

##### 6）使用CompletableFuture和Future<Integer>结合调起一个异步线程，见AsynTask6.java
            package com.geek.homework.week4.wwh;

            import java.util.concurrent.CompletableFuture;
            import java.util.concurrent.ExecutionException;
            import java.util.concurrent.Future;

            /**
             * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
             * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
             * <p>
             * 使用CompletableFuture和Future<Integer>结合调起一个异步线程
             */
            public class AsynTask6 {
                public static void main(String[] args){
                    System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
                    long start = System.currentTimeMillis();
                    //调用任务的异步方法getNumAsync异步获取计算的结果
                    Future<Integer> future = new Task6().getNumAsync();
                    long invocationTime = System. currentTimeMillis() - start;
                    System.out.println( "调用接口时间：" + invocationTime +  "毫秒");
                    int result = 0;
                    try {
                        result = future.get();  //获取异步方法中future的返回值
                    }  catch (InterruptedException e) {
                        e.printStackTrace();
                    }  catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    long retrievalTime = System. currentTimeMillis() - start;

                    //打印执行结果
                    System.out.println("异步计算结果为："+result);
                    System.out.println( "返回结果消耗时间：" + retrievalTime +  "毫秒");
                    System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
                }

            }

            class Task6 {

                private int sum() {
                    System.out.println("==="+Thread.currentThread().getName()+"线程正在执行计算");
                    int result =  fibo(36);
                    System.out.println("==="+Thread.currentThread().getName()+"线程完成计算");
                    return result;
                }

                private int fibo(int a) {
                    if ( a < 2)
                        return 1;
                    return fibo(a-1) + fibo(a-2);
                }

                /***
                 * 异步获取数字
                 * @return
                 */
                public Future<Integer> getNumAsync(){
                    CompletableFuture<Integer> future =  new CompletableFuture<>(); //构造一个CompletableFuture对象
                    //启动一个线程，调用sum方法，并将结果赋值到future对象，从而在别人调getNumAsync方法时，能获取异步调起线程的返回值
                    new Thread(() -> {
                        int result = sum();
                        future.complete(result);
                    }).start();
                    return future;
                }
            }      
##### 7) 使用CompletableFuture的supplyAsync方法调起一个异步线程，见AsynTask7.java
            package com.geek.homework.week4.wwh;

            import java.util.concurrent.CompletableFuture;
            import java.util.concurrent.ExecutionException;
            import java.util.concurrent.Future;

            /**
             * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
             * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
             * <p>
             * 使用CompletableFuture的supplyAsync方法调起一个异步线程
             */
            public class AsynTask7 {
                public static void main(String[] args){
                    System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
                    long start = System. currentTimeMillis();

                    //通过CompletableFuture对象的supplyAsync方法直接调起一个异步线程并获取返回结果,在异步调起的操作中，指定执行Task中的sum方法
                    Future<Integer> future  = CompletableFuture.supplyAsync(() -> new Task7().sum());

                    long retrievalTime = System. currentTimeMillis() - start;

                    //打印返回结果
                    try {
                        System.out.println("异步计算结果为："+future.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    System.out.println( "返回结果消耗时间：" + retrievalTime +  "毫秒");
                    System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
                }

            }

            class Task7 {

                public int sum() {
                    System.out.println("==="+Thread.currentThread().getName()+"线程正在执行计算");
                    int result =  fibo(36);
                    System.out.println("==="+Thread.currentThread().getName()+"线程完成计算");
                    return result;
                }

                private int fibo(int a) {
                    if ( a < 2)
                        return 1;
                    return fibo(a-1) + fibo(a-2);
                }
            }

##### 8）使用wait和notify方法调起一个异步线程，见AsynTask8.java
            package com.geek.homework.week4.wwh;

            /**
             * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
             * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
             * <p>
             * 使用wait和notify方法调起一个异步线程
             */
            public class AsynTask8 {
                public static void main(String[] args) {
                    System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
                    long start = System.currentTimeMillis();
                    final Task8 task8 = new Task8();

                    //启动一个新线程，并指定此线程执行Task的sum方法
                    Thread t1 = new Thread(() -> {
                        task8.sum();

                    }, "t1");
                    t1.start();

                    try {
                        //获取任务的执行结果
                        int result = task8.getResult();
                        System.out.println("异步计算结果为：" + result);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    long retrievalTime = System.currentTimeMillis() - start;

                    System.out.println("返回结果消耗时间：" + retrievalTime + "毫秒");
                    System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
                }
            }

            class Task8 {
                private volatile Integer result = null;

                public synchronized int sum() {
                    System.out.println("===" + Thread.currentThread().getName() + "线程正在执行计算");
                    result = fibo(36);
                    notifyAll();    //计算完毕，唤醒正在等待的所有线程，可以取值
                    System.out.println("===" + Thread.currentThread().getName() + "线程完成计算");
                    return result;
                }

                private int fibo(int a) {
                    if (a < 2)
                        return 1;
                    return fibo(a - 1) + fibo(a - 2);
                }

                public synchronized int getResult() throws InterruptedException {
                    while (result == null) {
                        wait();   //当result的值为null时，表示未进行计算，进入等待
                    }
                    return result;
                }
            }

##### 9）使用 Join 方式调起一个异步线程，见AsynTask9.java
            package com.geek.homework.week4.wwh;

            import java.util.concurrent.atomic.AtomicInteger;

            /**
             * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
             * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
             * <p>
             * 使用 Join 方式调起一个异步线程
             */
            public class AsynTask9 {
                public static void main(String[] args) throws InterruptedException {
                    System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
                    long start = System.currentTimeMillis();

                    Task9 task = new Task9();
                    //启动新线程，进行计算
                    Thread thread = new Thread(() -> {
                        task.value.set(task.sum());
                    });
                    thread.start();

                    thread.join();  //等待线程执行完毕

                    int result = task.value.get(); //得到返回值

                    System.out.println("异步计算结果为：" + result);
                    System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
                    System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
                }
            }

            class Task9 {
                public AtomicInteger value = new AtomicInteger();   //使用并发原子类保证操作的原子性

                public int sum() {
                    System.out.println("==="+Thread.currentThread().getName()+"线程正在执行计算");
                    int result =  fibo(36);
                    System.out.println("==="+Thread.currentThread().getName()+"线程完成计算");
                    return result;
                }

                private int fibo(int a) {
                    if (a < 2) {
                        return 1;
                    }
                    return fibo(a - 1) + fibo(a - 2);
                }
            }

##### 10）用 CyclicBarrier方式调起一个异步线程，见AsynTask10.java
            package com.geek.homework.week4.wwh;

            import java.util.concurrent.*;

            /**
             * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
             * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
             * <p>
             * 使用 CyclicBarrier方式调起一个异步线程
             */
            public class AsynTask10 {
                public static void main(String[] args){
                    System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
                    long start = System.currentTimeMillis();

                    Task10 task10 = new Task10();
                    //构造一个初始值为1的CyclicBarrier对象，并指定计数器的值为0时的回调函数为Task的getValue方法
                    CyclicBarrier cyclicBarrier = new CyclicBarrier(1, new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("异步计算结束，结果为：" +task10.getValue());   //打印计算完毕的结果
                        }
                    });
                    task10.setCyclicBarrier(cyclicBarrier);     //将cyclicBarrier复制给Task对象，让两者关联起来
                    new Thread(task10).start(); //调起一个新的线程，执行task的run方法

                    System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
                    System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
                }
            }

            class Task10 implements Runnable{
                private CyclicBarrier barrier;

                public void setCyclicBarrier(CyclicBarrier barrier){
                    this.barrier = barrier;
                }

                private volatile Integer result = null;

                public void sum(){
                    System.out.println("===" + Thread.currentThread().getName() + "线程正在执行计算");
                    result = fibo(36);
                    System.out.println("===" + Thread.currentThread().getName() + "线程完成计算");
                    try {
                        barrier.await();    //通过这个方法，对计数器的值减1，当计数器的值为0时，会自动回调指定的回调函数
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                private int fibo(int a) {
                    if (a < 2)
                        return 1;
                    return fibo(a - 1) + fibo(a - 2);
                }

                public int getValue(){
                    return result;
                }

                @Override
                public void run() {
                    //这里加上synchronized关键字，保证同一时间只有一个线程在进行此计算
                    synchronized (this){
                        sum();
                    }
                }
            }

##### 11）使用Lock结合Condition方式调起异步线程，见AsynTask11.java
            package com.geek.homework.week4.wwh;

            import java.util.concurrent.locks.Condition;
            import java.util.concurrent.locks.Lock;
            import java.util.concurrent.locks.ReentrantLock;

            /**
             * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
             * 异步运行一个方法，拿到这个方法的返回值后，退出主线程
             * <p>
             * 使用Lock结合Condition方式调起异步线程
             */
            public class AsynTask11 {
                public static void main(String[] args) throws InterruptedException {
                    System.out.println("主线程" + Thread.currentThread().getName() + ":begin!");
                    long start=System.currentTimeMillis();
                    final Task11 task11 = new Task11();
                    Thread thread = new Thread(() -> {
                        task11.sum(36);
                    });
                    thread.start();

                    int result = task11.getValue(); //这是得到的返回值

                    System.out.println("异步计算结果为：" + result);
                    System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
                    System.out.println("主线程" + Thread.currentThread().getName() + ":end!");
                }

            }

            class Task11 {
                private volatile Integer value = null;
                private Lock lock = new ReentrantLock();
                private Condition condition = lock.newCondition();

                public void sum(int num) {
                    System.out.println("===" + Thread.currentThread().getName() + "线程正在执行计算");
                    lock.lock();
                    try {
                        value = fibo(num);
                        condition.signal(); //给等待线程发送唤醒信号，类似notify操作，此处表示已经计算完成，唤醒其他线程可以获取值
                    } finally {
                        lock.unlock();
                    }
                    System.out.println("===" + Thread.currentThread().getName() + "线程完成计算");
                }

                private int fibo(int a) {
                    if (a < 2) {
                        return 1;
                    }
                    return fibo(a - 1) + fibo(a - 2);
                }

                public int getValue() throws InterruptedException {
                    lock.lock();
                    try {
                        while (value == null) {
                            condition.await();  //等待，类似Object的wait操作
                        }
                    } finally {
                        lock.unlock();
                    }
                    return value;
                }
            }



## Week04 作业题目（周六）：

### 4.（必做）把多线程和并发相关知识带你梳理一遍，画一个脑图，截图上传到 Github 上。可选工具：xmind，百度脑图，wps，MindManage 或其他。

#### 整理的思维导图如下：
![image](https://github.com/wenhui5628/JAVA-000/blob/main/Week_04/JAVA%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B%E6%80%9D%E7%BB%B4%E5%AF%BC%E5%9B%BE.png)
