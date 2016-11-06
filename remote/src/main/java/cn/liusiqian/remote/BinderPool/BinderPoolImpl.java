package cn.liusiqian.remote.binderpool;

import android.os.IBinder;
import android.os.RemoteException;

/**
 * Créé par liusiqian 16/11/6.
 */

public class BinderPoolImpl extends IBinderPool.Stub
{
    @Override
    public IBinder queryBinder(int code) throws RemoteException
    {
        IBinder binder = null;
        switch (code)
        {
            case BinderCode.BINDER_CODE_ADD:
                binder = new AddImpl();
                break;
            case BinderCode.BINDER_CODE_SUB:
                binder = new SubImpl();
                break;
            default:
                break;
        }
        return binder;
    }
}
