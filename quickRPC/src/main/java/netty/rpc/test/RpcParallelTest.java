package netty.rpc.test;

import netty.rpc.core.client.MessageCallBack;
import netty.rpc.core.client.MessageSendExecutor;
import netty.rpc.core.serialize.support.RpcSerializeProtocol;
import netty.rpc.servicebean.Calculate;
import org.apache.commons.lang3.time.StopWatch;

import java.util.List;


public class RpcParallelTest {

    public static void main(String[] args) throws Exception {
        final MessageSendExecutor executor = new MessageSendExecutor("127.0.0.1:18888", RpcSerializeProtocol.KRYSERIALIZE);
        Calculate calc = executor.execute(Calculate.class);
        //并行度10000
        int parallel = 10000;


        //开始计时
        StopWatch sw = new StopWatch();
        sw.start();
        for (int index = 0; index < parallel; index++) {
            //CalcParallelRequestThread client = new CalcParallelRequestThread(calc, barrier, finish, index);
           // new Thread(client).start();
            System.out.println("calc add result:[" + calc.add(1,2) + "]");
        }

        sw.stop();

        String tip = String.format("RPC调用总共耗时: [%s] 毫秒", sw.getTime());
        System.out.println(tip);

        //executor.stop();
    }
}