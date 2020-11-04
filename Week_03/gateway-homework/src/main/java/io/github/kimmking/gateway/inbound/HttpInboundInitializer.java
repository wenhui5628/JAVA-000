package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.filter.HttpRequestHeaderFilter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.List;

public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {
	
	private List<String> proxyServer;
	
	public HttpInboundInitializer(List<String> proxyServer) {
		this.proxyServer = proxyServer;
	}
	
	@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline channelPipeline = ch.pipeline();
		channelPipeline.addLast(new HttpServerCodec());
		channelPipeline.addLast(new HttpObjectAggregator(1024 * 1024));
		channelPipeline.addLast(new HttpRequestHeaderFilter());
		channelPipeline.addLast(new HttpInboundHandler(this.proxyServer));
	}
}
