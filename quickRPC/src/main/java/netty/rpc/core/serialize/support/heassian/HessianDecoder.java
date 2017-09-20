package netty.rpc.core.serialize.support.heassian;

import netty.rpc.core.serialize.support.MessageCodecUtil;
import netty.rpc.core.serialize.support.MessageDecoder;

public class HessianDecoder extends MessageDecoder {

    public HessianDecoder(MessageCodecUtil util) {
        super(util);
    }
}