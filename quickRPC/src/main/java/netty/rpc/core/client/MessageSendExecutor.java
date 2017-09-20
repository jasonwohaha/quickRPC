package netty.rpc.core.client;

import netty.rpc.core.serialize.support.RpcSerializeProtocol;

import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2017/8/4/004.
 */
public class MessageSendExecutor {

    private RpcServerLoader loader ;

    public MessageSendExecutor(String remoteAddress, RpcSerializeProtocol protocol){
        loader = new RpcServerLoader(remoteAddress,protocol);
    }

    public void stop(){
        loader.unLoad();
    }

    public <T> T execute(Class<T> rpcInterface){
        return (T)Proxy.newProxyInstance(rpcInterface.getClassLoader(),new Class<?>[]{rpcInterface},new MessageSendProxy<T>(loader));
    }
}
