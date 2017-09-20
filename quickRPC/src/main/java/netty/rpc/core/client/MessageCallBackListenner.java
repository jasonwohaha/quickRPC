package netty.rpc.core.client;

import netty.rpc.core.common.MessageResponse;

/**
 * Created by Administrator on 2017/8/8/008.
 */
public interface MessageCallBackListenner {
    void success(MessageResponse response);
}
