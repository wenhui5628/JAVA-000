package io.github.kimmking.gateway.router;

import java.util.List;

public class HttpEndpointRouterImpl implements HttpEndpointRouter{
    @Override
    public String route(List<String> endpoints) {
        int proxyServerKey = (int)(Math.random()*4);
//        int proxyServerKey = 0;
        System.out.println("生成随机数:"+proxyServerKey);
        return endpoints.get(proxyServerKey);
    }
}
