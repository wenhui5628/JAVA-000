package io.github.kimmking.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpRequestHeaderFilter extends ChannelInboundHandlerAdapter implements HttpRequestFilter{
    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        fullRequest.headers().set("nio","wuwenhui");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
        filter(fullHttpRequest, ctx);   //往请求报文头中塞入nio字段
        ctx.fireChannelRead(fullHttpRequest);//将数据流往后传递
    }
}
