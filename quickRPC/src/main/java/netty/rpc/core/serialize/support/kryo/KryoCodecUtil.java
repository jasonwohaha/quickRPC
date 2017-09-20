package netty.rpc.core.serialize.support.kryo;

import com.esotericsoftware.kryo.pool.KryoPool;
import com.google.common.io.Closer;
import io.netty.buffer.ByteBuf;
import netty.rpc.core.serialize.support.MessageCodecUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/8/6/006.
 */
public class KryoCodecUtil implements MessageCodecUtil {
    private KryoPool pool;
    private static Closer closer = Closer.create();

    public KryoCodecUtil(KryoPool pool) {
        this.pool = pool;
    }

    @Override
    public void encode(ByteBuf out, Object message) throws IOException {
            try{
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                closer.register(byteArrayOutputStream);
                KryoSerialize kryoSerialization = new KryoSerialize(pool);
                kryoSerialization.serialize(byteArrayOutputStream,message);
                byte[] body = byteArrayOutputStream.toByteArray();
                int dataLength = body.length;
                out.writeInt(dataLength);
                out.writeBytes(body);
            }finally {
                    closer.close();
            }
    }

    @Override
    public Object decode(byte[] body) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            closer.register(byteArrayInputStream);
            KryoSerialize kryoSerialization = new KryoSerialize(pool);
            Object obj = kryoSerialization.deserialize(byteArrayInputStream);
            return obj;
        } finally {
            closer.close();
        }
    }
}
