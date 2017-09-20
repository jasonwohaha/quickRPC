package netty.rpc.core.serialize.support;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public interface MessageCodecUtil {

    //RPC消息报文头长度4个字节
    final public static int MESSAGE_LENGTH = 4;

    public void encode(final ByteBuf out, final Object message) throws IOException;

    public Object decode(byte[] body) throws IOException;
}