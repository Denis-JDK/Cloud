package netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Enumeration;

public class EchoHandler extends SimpleChannelInboundHandler<String> {

    private EchoServer server;
    private String userName;


    public EchoHandler (EchoServer server, String userName) {

        this.server = server;
        this.userName = userName;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(userName + "Welcome\n");
        server.getClients().put(ctx, userName);
            }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected");
        server.getClients().remove(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext stx, String s) throws Exception {
       Enumeration<ChannelHandlerContext> clients = server.getClients().keys();
       /*for (ChannelHandlerContext client: clients) {
            client.writeAndFlush(userName + ":" + s);}*/
        while (clients.hasMoreElements()) {
            clients.nextElement().writeAndFlush(userName + ":" + s);
        }

        System.out.println("received: " + s);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
