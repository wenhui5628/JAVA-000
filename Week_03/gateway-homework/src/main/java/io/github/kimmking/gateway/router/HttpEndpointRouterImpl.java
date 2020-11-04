package io.github.kimmking.gateway.router;

import java.util.List;

public class HttpEndpointRouterImpl implements HttpEndpointRouter{
    /***
     * 生成随机数，并以这个随机数为索引从所有服务器地址集合中获取其中一台
     * @param endpoints
     * @return
     */
    @Override
    public String route(List<String> endpoints) {
        int proxyServerKey = (int)(Math.random()*4);
        return endpoints.get(proxyServerKey);
    }
}
