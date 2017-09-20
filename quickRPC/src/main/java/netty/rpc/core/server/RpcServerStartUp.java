package netty.rpc.core.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 2017/8/6/006.
 */
public class RpcServerStartUp {
    public static void main(String [] args){
        //服务器启动入口
        new ClassPathXmlApplicationContext("rpc-invoke-config.xml");
    }
}
