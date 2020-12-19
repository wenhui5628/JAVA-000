## 作业说明
*1、（必做）改造自定义RPC的程序，提交到github：
 1）尝试将服务端写死查找接口实现类变成泛型和反射；
 2）尝试将客户端动态代理改成AOP，添加异常处理 ；
 3）尝试使用Netty+HTTP作为client端传输方式*

### 1）尝试将服务端写死查找接口实现类变成泛型和反射；
见RpcfxInvoker.java，主要改动点为：  
1.将RpcfxRequest对象的serviceClass属性类型由原来的String改为Class<T>，同时将RpcfxRequest改为RpcfxRequest<T>添加泛型支持；  
2.RpcfxResolver类添加<T>T resolveGeneric(Class<T> serviceClass);通过此方法获取Bean对象；  
具体实现代码如下：
```java
 @Override
    public <T> T resolveGeneric(Class<T> serviceClass) {
        return this.applicationContext.getBean(serviceClass);
    }
```
3.invoke类的invoke方法改为以下方式获取泛型的Service对象
```java
 T service = resolver.resolveGeneric(serviceClass);//this.applicationContext.getBean(serviceClass);
```
    
### 2）尝试将客户端动态代理改成AOP，添加异常处理 ；


#### 设置MySQL



