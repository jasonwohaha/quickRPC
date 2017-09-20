package netty.rpc.core.common;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/8/4/004.
 */
public class RpcThreadPool {
    static String name = "RpcThreadPool";
    public static ThreadPoolExecutor getExecutor(int threads,int quques){
        //核心线程数
        //最大线程数
        //线程存活时间
        //单位
        //阻塞队列
        //拒绝策略
        return new ThreadPoolExecutor(threads,threads,0,
                TimeUnit.MICROSECONDS,quques==0?new SynchronousQueue<Runnable>():(quques<0?new LinkedBlockingDeque<Runnable>():new LinkedBlockingDeque<Runnable>(quques)),
                new NamedThreadFactory(name,true),new AbortPolicyWithReport(name));
    }
}
