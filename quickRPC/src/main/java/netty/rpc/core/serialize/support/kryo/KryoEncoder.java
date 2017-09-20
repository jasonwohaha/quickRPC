package netty.rpc.core.serialize.support.kryo;

import netty.rpc.core.serialize.support.MessageCodecUtil;
import netty.rpc.core.serialize.support.MessageEncoder;

public class KryoEncoder extends MessageEncoder {

    public KryoEncoder(MessageCodecUtil util) {
        super(util);
    }
}