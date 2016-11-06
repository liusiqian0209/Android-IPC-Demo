package cn.liusiqian.remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cn.liusiqian.remote.binderpool.BinderPoolImpl;

/**
 * Créé par liusiqian 16/11/6.
 */

public class BinderPoolService extends Service
{
    private BinderPoolImpl mBinderPool = new BinderPoolImpl();

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinderPool;
    }
}
