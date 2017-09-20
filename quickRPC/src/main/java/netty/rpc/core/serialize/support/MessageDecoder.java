package netty.rpc.core.serialize.support;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Administrator on 2017/8/6/006.
 */
public class MessageDecoder extends ByteToMessageDecoder {
    private MessageCodecUtil util;
    public MessageDecoder(MessageCodecUtil util) {
        this.util = util;
    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
        if(in.readableBytes()<MessageCodecUtil.MESSAGE_LENGTH){
            return;
        }
        in.markReaderIndex();

        int messageLength = in.readInt();
        if(messageLength<0){
              ctx.close();
              return;
        }
        if(in.readableBytes()<messageLength){
            in.resetReaderIndex();
        }
        else{
            byte [] body = new byte[messageLength];
           in.readBytes(body);
           Object oj = util.decode(body);
           list.add(oj);
        }
    }
}
