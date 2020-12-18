package io.kimmking.rpcfx.api;

public interface RpcfxResolver {

    Object resolve(String serviceClass);

    <T>T resolveGeneric(Class<T> serviceClass);
}
