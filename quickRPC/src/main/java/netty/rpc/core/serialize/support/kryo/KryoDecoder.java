package netty.rpc.core.serialize.support.kryo;

import netty.rpc.core.serialize.support.MessageCodecUtil;
import netty.rpc.core.serialize.support.MessageDecoder;

public class KryoDecoder extends MessageDecoder {

    public KryoDecoder(MessageCodecUtil util) {
        super(util);
    }
}