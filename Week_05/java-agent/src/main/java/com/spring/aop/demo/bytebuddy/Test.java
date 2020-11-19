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
