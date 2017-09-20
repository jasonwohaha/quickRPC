package netty.rpc.core.serialize.support;

/**
 * Created by Administrator on 2017/8/6/006.
 */
public enum  RpcSerializeProtocol {
    JDKSERIALIZE("jdknative"),KRYSERIALIZE("kryo"),HESSIANSERIALIZE("hessian");

    private String serializeProtocol;

    private RpcSerializeProtocol(String serializeProtocol) {
        this.serializeProtocol = serializeProtocol;
    }

    public String getProtocol() {
        return serializeProtocol;
    }

}
