package com.javayh.client.heart;

import com.javayh.client.properties.HeartbeatClientProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;

/**
 * <p>
 * 客户端
 * </p>
 *
 * @version 1.0.0
 * @author Dylan-haiji
 * @since 2020/3/10
 */
// @EnableConfigurationProperties(value = HeartbeatClientProperties.class)
public class HeartbeatClient {

	private final static Logger LOGGER = LoggerFactory.getLogger(HeartbeatClient.class);

	private EventLoopGroup group = new NioEventLoopGroup();

	private SocketChannel socketChannel;

	@Autowired(required = false)
	private HeartbeatClientProperties properties;

	@PostConstruct
	public void start() throws InterruptedException {
		Bootstrap bootstrap = new Bootstrap();
		/**
		 * NioSocketChannel用于创建客户端通道，而不是NioServerSocketChannel。
		 * 请注意，我们不像在ServerBootstrap中那样使用childOption()，因为客户端SocketChannel没有父服务器。
		 */
		bootstrap.group(group).channel(NioSocketChannel.class)
				.handler(new CustomerHandleInitializer());
		/**
		 * 启动客户端 我们应该调用connect()方法而不是bind()方法。
		 */
		ChannelFuture future = bootstrap
				.connect(properties.getHost(), properties.getPort()).sync();
		if (future.isSuccess()) {
			LOGGER.info("启动 Netty 成功");
		}

		socketChannel = (SocketChannel) future.channel();

	}

}
