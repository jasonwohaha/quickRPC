package netty.rpc.core.server;

import com.google.common.util.concurrent.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.rpc.core.common.*;
import netty.rpc.core.serialize.support.RpcSerializeProtocol;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Administrator on 2017/8/6/006.
 */
public class  MessageRecvExecutor implements ApplicationContextAware ,InitializingBean{

    private String serverAddress;

    private final static String DELIMITER = ":";

    private  volatile ListeningExecutorService threadPoolExecutor = MoreExecutors.listeningDecorator(RpcThreadPool.getExecutor(16,-1));

    private   Map<String, Object> handlerMap = new ConcurrentHashMap<String, Object>();

    public MessageRecvExecutor(String serverAddress,String serializeProtocol) {
        this.serverAddress = serverAddress;
        this.serializeProtocol = Enum.valueOf(RpcSerializeProtocol.class,serializeProtocol);
    }

    private RpcSerializeProtocol serializeProtocol = RpcSerializeProtocol.JDKSERIALIZE;

    //NIO线程调用
    public  void submit(Callable<MessageResponse> task, ChannelHandlerContext ctx){
        ListenableFuture< MessageResponse> listenableFuture= threadPoolExecutor.submit(task);
        Futures.addCallback(listenableFuture, new FutureCallback<MessageResponse>() {
            @Override
            public void onSuccess(MessageResponse response) {
                if(response==null){
                    System.out.println("err");
                    return;
                }
                ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        System.out.println("RPC Server Send message-id respone:" + response.getMessageId());
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        },threadPoolExecutor);
    }

    public Map<String, Object> getHandlerMap() {
        return handlerMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
                ThreadFactory threadRpcFactory = new NamedThreadFactory("NettyRPC ThreadFactory",false);
                int parallel = Runtime.getRuntime().availableProcessors()*2;

                EventLoopGroup boss = null;
                EventLoopGroup worker = null;
                try{
                    ServerBootstrap bootstrap = new ServerBootstrap();
                    boss = new NioEventLoopGroup();
                    worker = new NioEventLoopGroup(parallel,threadRpcFactory, SelectorProvider.provider());
                    bootstrap.group(boss,worker).channel(NioServerSocketChannel.class)
                            .childHandler(new MessageRecvChannelInitializer(MessageRecvExecutor.this,serializeProtocol))
                            .option(ChannelOption.SO_BACKLOG,128)
                            .childOption(ChannelOption.SO_KEEPALIVE,true);
                    String[] ipAddr = serverAddress.split(MessageRecvExecutor.DELIMITER);
                    if(ipAddr.length==2){
                        String host = ipAddr[0];
                        int port = Integer.parseInt(ipAddr[1]);
                        ChannelFuture future = bootstrap.bind(host, port).syncUninterruptibly();
                        System.out.printf("Netty RPC Server get success ip:%s port:%d\n", host, port);
                        future.channel().closeFuture().syncUninterruptibly();
                    }
                    else{
                        System.out.printf("Netty RPC Server get fail!\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    worker.shutdownGracefully();
                    boss.shutdownGracefully();
                }
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        try{
            MessageKeyVal keyVal = (MessageKeyVal) ctx.getBean(Class.forName("netty.rpc.core.common.MessageKeyVal"));
            Map<String, Object> rpcServiceObject = keyVal.getMessageKeyVal();
            Set s = rpcServiceObject.entrySet();
            Iterator<Map.Entry<String, Object>> it = s.iterator();
            Map.Entry<String, Object> entry;
            while (it.hasNext()) {
                entry = it.next();
                handlerMap.put(entry.getKey(), entry.getValue());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
