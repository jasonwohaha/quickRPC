package netty.rpc.core.client;

import com.google.common.util.concurrent.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.rpc.core.common.RpcThreadPool;
import netty.rpc.core.serialize.support.RpcSerializeProtocol;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2017/8/4/004.
 */
public class RpcServerLoader {
    private final  static String DELIMITER = ":";

    private  ListeningExecutorService threadPoolExecutor = MoreExecutors.listeningDecorator(RpcThreadPool.getExecutor(16,-1));
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    private MessageSendHandler messageSendHandler;

    private InetSocketAddress serverAddr;
    private RpcSerializeProtocol protocol;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();


    public RpcServerLoader(String remoteAddr,RpcSerializeProtocol protocol) {
        String [] ipAddrr = remoteAddr.split(RpcServerLoader.DELIMITER);
        String host = ipAddrr[0];
        int port = Integer.parseInt(ipAddrr[1]);
        serverAddr = new InetSocketAddress(host,port);

        this.protocol = protocol;
        threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE,true);
                bootstrap.handler(new MessageSendChannelInitializer(protocol));
                ChannelFuture channelFuture = bootstrap.connect(serverAddr);
                //网络初始化完毕
                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        MessageSendHandler messageSendHandler = channelFuture.channel().pipeline().get(MessageSendHandler.class);
                        setMessageSendHandler(messageSendHandler);
                    }
                });
            }
        });
    }

    private void setMessageSendHandler(MessageSendHandler handler){
        try{
            lock.lock();
            this.messageSendHandler = handler;
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public MessageSendHandler getMessageSendHandler(){
        if(messageSendHandler!=null){
            return messageSendHandler;
        }
        try{
            lock.lock();
            if(messageSendHandler==null){
                condition.awaitUninterruptibly();
            }
            return messageSendHandler;
        }finally {
            lock.unlock();
        }
    }



    public void unLoad(){
        messageSendHandler.close();
        threadPoolExecutor.shutdown();
        eventLoopGroup.shutdownGracefully();
    }


}
