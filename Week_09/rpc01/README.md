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
1、由于客户端能知道的只有接口，并不知道接口的实现类，要改成AOP的话，我想到的办法只有在客户端创建一个实现类UserServiceImpl实现UserService接口，
重写里面的findById方法，但里面不处理任何逻辑；
2、针对UserServiceImpl的findById方法声明一个切面，在切面中执行调用后台服务，获取后台返回的处理结果等一系列动作，见RpcAspect.java，代码如下：
```java
package io.kimmking.rpcfx.demo.consumer;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;

import static io.kimmking.rpcfx.client.Rpcfx.RpcfxInvocationHandler.JSONTYPE;

@Component
@Aspect
public class RpcAspect {
    @Pointcut("execution(* io.kimmking.rpcfx.demo.consumer.UserServiceImpl.findById(..))")
    public void execute() {
    }

    @Around("execute()")
    public Object doAround(ProceedingJoinPoint joinpoint) {
        System.out.println("======进入Rpc客戶端切面");
        try {
            RpcfxRequest request = new RpcfxRequest();
            Signature sig = joinpoint.getSignature();
            MethodSignature msig = null;
            if (!(sig instanceof MethodSignature)) {
                throw new IllegalArgumentException("该注解只能用于方法");
            }
            msig = (MethodSignature) sig;
            Object target = joinpoint.getTarget();
            Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
            Class<?>[] interfaces = target.getClass().getInterfaces();
            Class interfaceClass = interfaces[0];
            request.setServiceClass(interfaceClass);
            request.setMethod(currentMethod.getName());
            //通过joinPoint.getArgs()获取Args参数
            Object[] params = joinpoint.getArgs();//2.传参
            request.setParams(params);
            RpcfxResponse response = post(request, "http://localhost:8080/");
            if(!response.isStatus()){    //为true表示处理成功,为false表示处理失败
                System.out.println("======客户端请求服务端失败，异常信息为:"+response.getException().getLocalizedMessage());
            }
            // 这里判断response.status，处理异常
            // 考虑封装一个全局的RpcfxException
            Object result = JSON.parse(response.getResult().toString());
            System.out.println("======Rpc客戶端切面处理完毕");
            return result;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    private RpcfxResponse post(RpcfxRequest req, String url) throws IOException {
        String reqJson = JSON.toJSONString(req);
        System.out.println("req json: " + reqJson);
        // 1.可以复用client
        // 2.尝试使用httpclient或者netty client
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String respJson = client.newCall(request).execute().body().string();
        System.out.println("resp json: " + respJson);
        return JSON.parseObject(respJson, RpcfxResponse.class);
    }
}
```
3.涉及的applicationContext.xml配置如下：
```properties
<context:component-scan base-package="io.kimmking.rpcfx.demo.consumer"/>
    <aop:aspectj-autoproxy expose-proxy="true"></aop:aspectj-autoproxy>
    <!-- 1、基于xml的普通装配 -->
    <bean id="userService" class="io.kimmking.rpcfx.demo.consumer.UserServiceImpl"/>
```
4.在rpcfx-core中自定义一个异常类RpcException.java，用于供服务器处理出错时使用,代码如下:
```java
package io.kimmking.rpcfx.exception;

public class RpcException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private String code;

    public RpcException(){
        super();
    }
    public RpcException(String errorCode,String errMsg){
        super(errMsg);
        code=errorCode;
    }
    public String getCode() {
        return code;
    }
}
```
5.在服务端RpcfxInvoker.java中加上异常处理代码，如下：
```java
  try {
//            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Method method = resolveMethodFromClassAndParam(service.getClass(), request.getMethod());

            Object result = method.invoke(service, request.getParams()); // dubbo, fastjson,
            // 两次json序列化能否合并成一个
            response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            response.setStatus(true);
            return response;
        } catch (Exception e) {
            System.err.println("服务端处理出现异常，异常信息为"+e);
            // 3.Xstream
            // 2.封装一个统一的RpcfxException
            // 客户端也需要判断异常
            response.setException(new RpcException("RPCE001", e.getLocalizedMessage()));
            response.setStatus(false);
            return response;
        }
```
6.客户端对异常处理如下：
```java
// 这里判断response.status，处理异常
// 考虑封装一个全局的RpcfxException
RpcfxResponse response = post(request, "http://localhost:8080/");
if(!response.isStatus()){    //为true表示处理成功,为false表示处理失败
  System.out.println("======客户端请求服务端失败，异常信息为:"+response.getException().getLocalizedMessage());
}
```

#### 3）尝试使用Netty+HTTP作为client端传输方式
见rpcfx-demo-consumer的NettyClient.java和NettyClientHandler.java
这里需要注意的点是构造FullHttpRequest请求对象时，要将请求设置成POST模式，并且请求头需要指定报文格式为json报文，如下：
```java
        //配置HttpRequest的请求数据和一些配置信息
        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_0, HttpMethod.POST, url.toASCIIString(), Unpooled.wrappedBuffer(reqJson.getBytes("UTF-8")));

        request.headers()
                .set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8")
                //开启长连接
                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                //设置传递请求内容的长度
                .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
```


