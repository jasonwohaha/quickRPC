package netty.rpc.core.server;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import netty.rpc.core.serialize.support.MessageCodecUtil;
import netty.rpc.core.serialize.support.RpcSerializeFrame;
import netty.rpc.core.serialize.support.RpcSerializeProtocol;
import netty.rpc.core.serialize.support.heassian.HessianCodecUtil;
import netty.rpc.core.serialize.support.heassian.HessianDecoder;
import netty.rpc.core.serialize.support.heassian.HessianEncoder;
import netty.rpc.core.serialize.support.kryo.KryoCodecUtil;
import netty.rpc.core.serialize.support.kryo.KryoDecoder;
import netty.rpc.core.serialize.support.kryo.KryoEncoder;
import netty.rpc.core.serialize.support.kryo.KryoPoolFactory;

/**
 * Created by Administrator on 2017/8/6/006.
 */
public class RpcRecvSerializeFrame implements RpcSerializeFrame{
    private MessageRecvExecutor executor = null;

    public RpcRecvSerializeFrame(MessageRecvExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline) {
        switch (protocol){
            case JDKSERIALIZE:{
                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, MessageCodecUtil.MESSAGE_LENGTH, 0, MessageCodecUtil.MESSAGE_LENGTH));
                pipeline.addLast(new LengthFieldPrepender(MessageCodecUtil.MESSAGE_LENGTH));
                pipeline.addLast(new ObjectEncoder());
                pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                pipeline.addLast(new MessageRecvHandler(executor));
                break;
            }
            case KRYSERIALIZE:{
                KryoCodecUtil util = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
                pipeline.addLast(new KryoEncoder(util));
                pipeline.addLast(new KryoDecoder(util));
                pipeline.addLast(new MessageRecvHandler(executor));
                break;
            }
            case HESSIANSERIALIZE:{
                HessianCodecUtil util = new HessianCodecUtil();
                pipeline.addLast(new HessianEncoder(util));
                pipeline.addLast(new HessianDecoder(util));
                pipeline.addLast(new MessageRecvHandler(executor));
                break;
            }
        }
    }
}
