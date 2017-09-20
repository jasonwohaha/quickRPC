package netty.rpc.core.serialize.support.heassian;

import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * Created by Administrator on 2017/8/6/006.
 */
public class HessianSerializePool {
    private GenericObjectPool<HessianSerialize> hessianPool;
    private static volatile HessianSerializePool poolFactory = null;

    private HessianSerializePool() {
        hessianPool = new GenericObjectPool<HessianSerialize>(new HessianSerializeFactory());
    }

    public static HessianSerializePool getHessianPoolInstance() {
        if (poolFactory == null) {
            synchronized (HessianSerializePool.class) {
                if (poolFactory == null) {
                    poolFactory = new HessianSerializePool();
                }
            }
        }
        return poolFactory;
    }

    public HessianSerialize borrow() {
        try {
            return getHessianPool().borrowObject();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void restore(final HessianSerialize object) {
        getHessianPool().returnObject(object);
    }


    public GenericObjectPool<HessianSerialize> getHessianPool() {
        return hessianPool;
    }


}