package netty.rpc.core.serialize.support.heassian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import netty.rpc.core.serialize.support.RpcSerialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/8/6/006.
 */
public class HessianSerialize implements RpcSerialize {
    @Override
    public void serialize(OutputStream output, Object object) throws IOException {
        Hessian2Output ho = new Hessian2Output(output);
        ho.startMessage();
        ho.writeObject(object);
        ho.completeMessage();
        ho.close();
    }

    @Override
    public Object deserialize(InputStream input) throws IOException {
        Hessian2Input hi = new Hessian2Input(input);
        hi.startMessage();
        Object result = hi.readObject();
        hi.completeMessage();
        hi.close();
        return result;
    }
}
