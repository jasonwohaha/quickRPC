package netty.rpc.core.serialize.support;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import sun.misc.MessageUtils;

/**
 * Created by Administrator on 2017/8/6/006.
 */
public class MessageEncoder extends MessageToByteEncoder<Object> {

    private MessageCodecUtil util;
    public MessageEncoder(MessageCodecUtil util) {
        this.util = util;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf out) throws Exception {
        util.encode(out,o);
    }
}
