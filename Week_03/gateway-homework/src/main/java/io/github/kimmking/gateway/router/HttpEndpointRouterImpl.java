package io.github.kimmking.gateway.router;

import java.util.List;

public class HttpEndpointRouterImpl implements HttpEndpointRouter{
    @Override
    public String route(List<String> endpoints) {
        int proxyServerKey = (int)(Math.random()*4);
        return endpoints.get(proxyServerKey);
    }
}
