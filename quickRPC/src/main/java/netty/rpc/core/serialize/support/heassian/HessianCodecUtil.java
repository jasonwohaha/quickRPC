package netty.rpc.core.serialize.support.heassian;


import com.google.common.io.Closer;
import io.netty.buffer.ByteBuf;
import netty.rpc.core.serialize.support.MessageCodecUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/8/6/006.
 */
public class HessianCodecUtil implements MessageCodecUtil {

   private HessianSerializePool pool = HessianSerializePool.getHessianPoolInstance();

   private static Closer closer = Closer.create();

    @Override
    public void encode(ByteBuf out, Object message) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            closer.register(byteArrayOutputStream);
            HessianSerialize hessianSerialization = pool.borrow();
            hessianSerialization.serialize(byteArrayOutputStream,message);
            byte [] body = byteArrayOutputStream.toByteArray();
            out.writeInt(body.length);
            out.writeBytes(body);
            pool.restore(hessianSerialization);
        }
        finally {
            closer.close();
        }
    }

    @Override
    public Object decode(byte[] body) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            closer.register(byteArrayInputStream);
            HessianSerialize hessianSerialize = pool.borrow();
            Object object = hessianSerialize.deserialize(byteArrayInputStream);
            pool.restore(hessianSerialize);
            return object;
        }finally {
            closer.close();
        }
    }
}
