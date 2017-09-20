package netty.rpc.core.serialize.support.heassian;

import netty.rpc.core.serialize.support.MessageCodecUtil;
import netty.rpc.core.serialize.support.MessageEncoder;

public class HessianEncoder extends MessageEncoder {

    public HessianEncoder(MessageCodecUtil util) {
        super(util);
    }
}