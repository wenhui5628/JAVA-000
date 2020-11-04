package io.github.kimmking.gateway.outbound.okhttp;

import io.github.kimmking.gateway.outbound.httpclient4.NamedThreadFactory;
import io.github.kimmking.gateway.router.HttpEndpointRouterImpl;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class OkhttpOutboundHandler {
    private ExecutorService proxyService;
    OkHttpClient client;
    List<String> endpoints;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public OkhttpOutboundHandler(List<String> endpoints) {
        this.endpoints = endpoints;
        int cores = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();//.DiscardPolicy();
        proxyService = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"), handler);
        client = new OkHttpClient();    //构造OkHttpClient客户端对象
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        String proxyServer = new HttpEndpointRouterImpl().route(endpoints);     //获取具体目标服务器ip地址
        String backendUrl = proxyServer.endsWith("/") ? proxyServer.substring(0, proxyServer.length() - 1) : proxyServer;
        final String url = backendUrl + fullRequest.uri();
        proxyService.submit(() -> fetchGet(fullRequest, ctx, url));
    }

    private void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {
        //获取所有请求头属性
        HttpHeaders header = inbound.headers();//获取请求头对象
        List<Map.Entry<String, String>> entriesList = header.entries(); //将包含的请求信息赋值到list中

        //将所有请求头信息赋值给OkHttpClient客户端的请求头
        HashMap<String, String> requestHeadMap = new HashMap<>();
        entriesList.forEach(headersMap -> requestHeadMap.put(headersMap.getKey(),headersMap.getValue()));
        Request.Builder builder = new Request.Builder();
        requestHeadMap.forEach(builder::header);

        //调用OkHttpClient客户端访问后端服务
        Request request = builder.url(url).build();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            log.info("响应内容为：" + responseBody);
            FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(responseBody.getBytes("UTF-8")));
            fullHttpResponse.headers().set("Content-Type", "application/json");
            fullHttpResponse.headers().setInt("Content-Length", fullHttpResponse.content().readableBytes());
            if (inbound != null) {
                if (!HttpUtil.isKeepAlive(inbound)) {
                    ctx.write(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(fullHttpResponse);
                }
            }
            ctx.flush();
        } catch (Exception e) {
            log.error("发送到服务端出错，错误信息为:" + e);
        }
    }

}
