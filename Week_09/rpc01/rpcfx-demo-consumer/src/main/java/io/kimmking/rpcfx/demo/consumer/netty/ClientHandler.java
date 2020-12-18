package io.kimmking.rpcfx.demo.consumer.netty;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * 客户端处理器
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        FullHttpResponse response = (FullHttpResponse) msg;

        ByteBuf content = response.content();
        HttpHeaders headers = response.headers();

        System.out.println("content:" + System.getProperty("line.separator") + content.toString(CharsetUtil.UTF_8));
        System.out.println("headers:" + System.getProperty("line.separator") + headers.toString());
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        URI url = new URI("/");
        String meg = "{\"method\":\"findById\",\"params\":[1],\"serviceClass\":\"io.kimmking.rpcfx.demo.api.UserService\"}";
//        String json = JSON.toJSONString(meg);
        //转成btye数组   utf-8模式
//        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        //配置HttpRequest的请求数据和一些配置信息
        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_0, HttpMethod.POST, url.toASCIIString(), Unpooled.wrappedBuffer(meg.getBytes("UTF-8")));

        request.headers()
                .set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8")
                //开启长连接
                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                //设置传递请求内容的长度
                .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

        //发送数据
        ctx.writeAndFlush(request);
    }
}

