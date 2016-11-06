package cn.liusiqian.remote.binderpool;

import android.os.RemoteException;

/**
 * Créé par liusiqian 16/11/6.
 */

public class AddImpl extends IAdd.Stub
{
    @Override
    public int add(int num1, int num2) throws RemoteException
    {
        return num1 + num2;
    }
}
