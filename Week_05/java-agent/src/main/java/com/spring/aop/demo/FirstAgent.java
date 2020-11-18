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

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replace("/", ".");
        if (className.contains(injectedClassName)) {
            CtClass ctclass = null;
            try {
                ctclass = ClassPool.getDefault().get(className);// 使用全称,用于取得字节码类<使用javassist>
                CtMethod[] ctMethods = ctclass.getDeclaredMethods();
                for(CtMethod ctmethod : ctMethods){
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
