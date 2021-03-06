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
