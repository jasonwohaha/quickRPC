package netty.rpc.core.client;

import netty.rpc.core.common.MessageRequest;
import netty.rpc.core.common.MessageResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Administrator on 2017/8/4/004.
 */
public class MessageSendProxy<T> implements InvocationHandler {

    private RpcServerLoader loader;

    public MessageSendProxy(RpcServerLoader loader){
        this.loader = loader;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MessageRequest request = new MessageRequest();
        request.setMessageId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setTypeParameters(method.getParameterTypes());
        request.setParametersVal(args);

        MessageSendHandler handler = loader.getMessageSendHandler();
        //MessageCallBack类似于Future接口
        MessageCallBack callBack = handler.sendRequest(request);
        return callBack.get();
    }
}
