package netty.rpc.core.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.rpc.core.common.MessageRequest;
import netty.rpc.core.common.MessageResponse;

/**
 * Created by Administrator on 2017/8/6/006.
 */
public class MessageRecvHandler extends ChannelInboundHandlerAdapter {

    private MessageRecvExecutor executor;

    public MessageRecvHandler(MessageRecvExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageRequest request = (MessageRequest) msg;
        MessageResponse response = new MessageResponse();
        MessageRecvInitializeTask recvTask = new MessageRecvInitializeTask(request, response,executor);
        //不要阻塞nio线程，复杂的业务逻辑丢给专门的线程池
        executor.submit(recvTask,ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
