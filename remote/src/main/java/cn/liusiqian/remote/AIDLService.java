package cn.liusiqian.remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.liusiqian.remote.aidl.IOnNewStudentAddedListener;
import cn.liusiqian.remote.aidl.IStudentManager;
import cn.liusiqian.remote.aidl.Student;

/**
 * Créé par liusiqian 16/10/31.
 */

public class AIDLService extends Service
{
    private List<Student> students;
    private RemoteCallbackList<IOnNewStudentAddedListener> mListCallbacks;

    public AIDLService()
    {
        students = new CopyOnWriteArrayList<>();
        mListCallbacks = new RemoteCallbackList<>();
    }

    private IBinder mBinder = new IStudentManager.Stub()
    {
        @Override
        public void addNewStudent(Student student) throws RemoteException
        {
            students.add(student);
        }

        @Override
        public List<Student> getStudents() throws RemoteException
        {
            return students;
        }

        @Override
        public void addOnNewStudentAddedListener(IOnNewStudentAddedListener listener) throws RemoteException
        {
            mListCallbacks.register(listener);
        }

        @Override
        public void removeOnNewStudentAddedListener(IOnNewStudentAddedListener listener) throws RemoteException
        {
            mListCallbacks.register(listener);
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }
}
