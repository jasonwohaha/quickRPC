package netty.rpc.core.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import netty.rpc.core.client.MessageSendExecutor;
import netty.rpc.core.common.MessageRequest;
import netty.rpc.core.common.MessageResponse;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.util.concurrent.Callable;

/**
 * Created by Administrator on 2017/8/6/006.
 */
public class MessageRecvInitializeTask implements Callable<MessageResponse> {

    private MessageRequest request = null;
    private MessageResponse response = null;
    private MessageRecvExecutor executor;

    MessageRecvInitializeTask(MessageRequest request, MessageResponse response,MessageRecvExecutor executor) {
        this.request = request;
        this.response = response;
        this.executor = executor;
    }

    @Override
    public MessageResponse call() {
        response.setMessageId(request.getMessageId());
        try{
            Object result = reflect(request);
            response.setResultDesc(result);
            return response;
        }catch (Throwable t){
            response.setError(t.toString());
            t.printStackTrace();
            return null;
        }
    }

    private Object reflect(MessageRequest request) throws Throwable {
        String className = request.getClassName();
        Object serviceBean = executor.getHandlerMap().get(className);
        String methodName = request.getMethodName();
        Object[] parameters = request.getParametersVal();
        return MethodUtils.invokeMethod(serviceBean, methodName, parameters);
    }

}
