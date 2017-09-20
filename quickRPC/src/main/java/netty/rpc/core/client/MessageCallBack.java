package netty.rpc.core.client;
import netty.rpc.core.common.MessageResponse;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2017/8/4/004.
 */

public class MessageCallBack {
    private MessageResponse response;
    private Lock lock = new ReentrantLock();
    private Condition finish = lock.newCondition();
    private static ExecutorService service = Executors.newSingleThreadExecutor();

    public void stop(){
        service.shutdown();
    }

    //get方法是callerThread执行的。
    public Object get() throws InterruptedException{
        try{
            lock.lock();
            if(response==null){
                finish.awaitUninterruptibly();
            }
            return response.getResultDesc();
        }
        finally {
            lock.unlock();
        }
    }

    //异步获得返回结果
    public void addListenner(MessageCallBackListenner listenner){
        service.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    lock.lock();
                    if(response==null){
                        finish.awaitUninterruptibly();
                    }
                    listenner.success(response);
                }finally {
                    lock.unlock();
                }
            }
        });
    }

    //set方法是NIO线程执行的。
    public void set(MessageResponse response){
        try{
            lock.lock();
            this.response = response;
            finish.signalAll();
        }
        finally {
            lock.unlock();
        }
    }



}
