package io.github.kimmking.gateway;


import io.github.kimmking.gateway.inbound.HttpInboundServer;

import java.util.*;

public class NettyServerApplication {
    
    public final static String GATEWAY_NAME = "NIOGateway";
    public final static String GATEWAY_VERSION = "1.0.0";

    final static List<String> endpoints = new ArrayList<>();

    /***
     * 初始化时生成一个目标服务器的IP列表，用于端口和IP之间的映射关系
     */
    static{
        endpoints.add("http://localhost:8088");
        endpoints.add("http://localhost:8801");
        endpoints.add("http://localhost:8802");
        endpoints.add("http://localhost:8803");
    }
    
    public static void main(String[] args) {
        String proxyPort = System.getProperty("proxyPort","8888");
        int port = Integer.parseInt(proxyPort);
        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION +" starting...");
        HttpInboundServer server = new HttpInboundServer(port, endpoints);
        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION +" started at http://localhost:" + port);
        try {
            server.run();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
