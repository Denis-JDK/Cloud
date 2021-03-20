package netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class AuthHandler extends SimpleChannelInboundHandler<String> {

    private EchoServer server;

    public AuthHandler(EchoServer server) {
        this.server = server;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected");
        ctx.writeAndFlush("Введите логин и пароль\n");
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        String [] data = s.split(" ");
        if (data != null && data.length == 2) {
            String login = data[0];
            String password = data[1];
            // по хорошему надо сходить в базу  проверить есть ли они или зарегать в базе
            ctx.pipeline().remove(AuthHandler.class);
            ctx.pipeline().addLast(new EchoHandler(server, login));
            ctx.pipeline().get(EchoHandler.class).channelActive(ctx);
        }
    }
}
