package io.kimmking.rpcfx.demo.consumer.netty;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxResponse;
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
public class NettyClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        FullHttpResponse response = (FullHttpResponse) msg;

        ByteBuf respJson = response.content();
        HttpHeaders headers = response.headers();
        RpcfxResponse result = JSON.parseObject(respJson.toString(CharsetUtil.UTF_8), RpcfxResponse.class);
        if(!result.isStatus()){    //为true表示处理成功,为false表示处理失败
            System.out.println("======客户端请求服务端失败，异常信息为:"+result.getException().getLocalizedMessage());
        }
        System.out.println("======response json:" + System.getProperty("line.separator") + respJson.toString(CharsetUtil.UTF_8));
        System.out.println("======response headers:" + System.getProperty("line.separator") + headers.toString());
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        URI url = new URI("/");
        String reqJson = "{\"method\":\"findById\",\"params\":[1],\"serviceClass\":\"io.kimmking.rpcfx.demo.api.UserService\"}";
        System.out.println("======req json: " + reqJson);

        //配置HttpRequest的请求数据和一些配置信息
        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_0, HttpMethod.POST, url.toASCIIString(), Unpooled.wrappedBuffer(reqJson.getBytes("UTF-8")));

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

