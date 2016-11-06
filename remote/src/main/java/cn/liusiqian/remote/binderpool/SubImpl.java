package cn.liusiqian.remote.binderpool;

import android.os.RemoteException;

/**
 * Créé par liusiqian 16/11/6.
 */

public class SubImpl extends ISub.Stub
{
    @Override
    public int sub(int num1, int num2) throws RemoteException
    {
        return num1 - num2;
    }
}
