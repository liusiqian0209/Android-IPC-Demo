package cn.liusiqian.binderpoolclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.CountDownLatch;

import cn.liusiqian.remote.binderpool.BinderCode;
import cn.liusiqian.remote.binderpool.IBinderPool;

/**
 * Créé par liusiqian 16/11/6.
 */

public class BinderPool
{
    private Context mContext;
    private CountDownLatch countDownLatch;
    private IBinderPool mBinderPool;

    private static volatile BinderPool instance;
    private BinderPool(Context context)
    {
        mContext = context.getApplicationContext();
        connectToRemoteService();
    }
    public static BinderPool getInstance(Context context)
    {
        if(instance == null)
        {
            synchronized (BinderPool.class)
            {
                if (instance == null)
                {
                    instance = new BinderPool(context);
                }
            }
        }
        return instance;
    }

    public IBinder queryBinder(int code)
    {
        IBinder binder = null;
        try
        {
            binder = mBinderPool.queryBinder(code);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        return binder;
    }

    private synchronized void connectToRemoteService()
    {
        countDownLatch = new CountDownLatch(1);
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("cn.liusiqian.remote","cn.liusiqian.remote.BinderPoolService"));
        mContext.bindService(intent,mServiceConnection,Context.BIND_AUTO_CREATE);
        try
        {
            countDownLatch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            //Service中onBind返回的是BinderPoolImpl反序列化的对象
            mBinderPool = IBinderPool.Stub.asInterface(iBinder);
            try
            {
                mBinderPool.asBinder().linkToDeath(deathRecipient,0);
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {

        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient()
    {
        @Override
        public void binderDied()
        {
            mBinderPool.asBinder().unlinkToDeath(deathRecipient,0);
            connectToRemoteService();   //Binder线程池中进行
        }
    };
}
