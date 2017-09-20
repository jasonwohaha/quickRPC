package netty.rpc.core.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import netty.rpc.core.serialize.support.RpcSerializeFrame;
import netty.rpc.core.serialize.support.RpcSerializeProtocol;

/**
 * Created by Administrator on 2017/8/6/006.
 */
public class MessageRecvChannelInitializer extends ChannelInitializer {

    private RpcSerializeProtocol protocol;
    private RpcSerializeFrame frame = null;

    public MessageRecvChannelInitializer(MessageRecvExecutor executor,RpcSerializeProtocol protocol) {
        this.protocol = protocol;
        frame = new RpcRecvSerializeFrame(executor);
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeLine = channel.pipeline();
        frame.select(protocol,pipeLine);
    }
}
