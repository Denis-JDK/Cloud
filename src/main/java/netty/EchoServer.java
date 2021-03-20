package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.ConcurrentHashMap;



public class EchoServer {

    private ConcurrentHashMap<ChannelHandlerContext, String> clients;

    public EchoServer() {
        clients = new ConcurrentHashMap<>();
        EventLoopGroup auth = new NioEventLoopGroup(3);
        EventLoopGroup worker = new NioEventLoopGroup();

        try {

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(auth, worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {

                    channel.pipeline().addLast( //труба на которую вешаем handler порядок их важен
                            new StringDecoder(),
                            new StringEncoder(),
                            new AuthHandler(EchoServer.this)
                    );
                }
            });
            ChannelFuture future = bootstrap.bind(8189).sync();
            System.out.println("Server started");
            future.channel().closeFuture().sync(); // блокирующая
        } catch (InterruptedException e) {
            System.out.println("Server was broken");
        } finally {
            auth.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public ConcurrentHashMap<ChannelHandlerContext, String> getClients() {
        return clients;
    }

    public static void main(String[] args) {
        new EchoServer();
    }
}