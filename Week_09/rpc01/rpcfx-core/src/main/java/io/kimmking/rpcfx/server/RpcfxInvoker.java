package io.kimmking.rpcfx.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.kimmking.rpcfx.RpcException;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResolver;
import io.kimmking.rpcfx.api.RpcfxResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class RpcfxInvoker<T> {

    private RpcfxResolver resolver;

    public RpcfxInvoker(RpcfxResolver resolver) {
        this.resolver = resolver;
    }

    public RpcfxResponse invoke(RpcfxRequest request){
        RpcfxResponse response = new RpcfxResponse();
        Class<T> serviceClass = request.getServiceClass();

        // 作业1：改成泛型和反射
//        Object service = resolver.resolve(serviceClass);//this.applicationContext.getBean(serviceClass);

        T service = resolver.resolveGeneric(serviceClass);//this.applicationContext.getBean(serviceClass);

        try {
//            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Method method = resolveMethodFromClassAndParam(service.getClass(), request.getMethod());

            Object result = method.invoke(service, request.getParams()); // dubbo, fastjson,
            // 两次json序列化能否合并成一个
            response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            response.setStatus(true);
            int num = 1/0;
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
    }

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
    }

    private Method resolveMethodFromClassAndParam(Class<?> klass, String methodName) throws NoSuchMethodException {
        return klass.getMethod(methodName, int.class);
    }

}
