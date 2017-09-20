package netty.rpc.core.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/8/4/004.
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    private final AtomicInteger mThreadNum = new AtomicInteger(1);

    private final String prefix;

    private  final boolean deamon;



    public NamedThreadFactory(String prefix,boolean deamon){
        this.prefix = prefix+"-thread-";
        this.deamon=deamon;
        SecurityManager s = System.getSecurityManager();
    }

    public Thread newThread(Runnable r) {
        String name = prefix+mThreadNum.getAndIncrement();
        Thread ret = new Thread(r,name);
        ret.setDaemon(deamon);
        return ret;
    }
}
