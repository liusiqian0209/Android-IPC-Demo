package cn.liusiqian.aidlclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import cn.liusiqian.remote.aidl.IOnNewStudentAddedListener;
import cn.liusiqian.remote.aidl.Student;

import java.util.ArrayList;
import java.util.List;

import cn.liusiqian.remote.aidl.IStudentManager;

public class MainActivity extends AppCompatActivity
{
    private View btn;
    private ServiceConnection connection;

    private IStudentManager stuManager;
    private List<Student> students;

    private IOnNewStudentAddedListener stuAddListener;
    private IBinder.DeathRecipient deathRecipient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initWidget();

        stuAddListener = new IOnNewStudentAddedListener.Stub()
        {
            @Override
            public void onNewStudentAdded(Student student) throws RemoteException
            {
                Log.i("AIDL-Client","onNewStudentAdded--Thread:"+Thread.currentThread().getName());
            }
        };

        connection = new ServiceConnection()
        {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                stuManager = IStudentManager.Stub.asInterface(service);
                try
                {
                    stuManager.addOnNewStudentAddedListener(stuAddListener);
                    addNewStudents();
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
                try
                {
                    service.linkToDeath(deathRecipient,0);
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {
                Log.i("AIDL-Client","onServiceDisconnected--Thread:"+Thread.currentThread().getName());
            }
        };

        deathRecipient = new IBinder.DeathRecipient()
        {
            @Override
            public void binderDied()
            {
                Log.i("AIDL-Client","binderDied--Thread:"+Thread.currentThread().getName());
                try
                {
                    stuManager.removeOnNewStudentAddedListener(stuAddListener);
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    stuManager.asBinder().unlinkToDeath(deathRecipient,0);
                }
            }
        };
    }

    private void initData()
    {
        students = new ArrayList<>();
        String[] names = {"Tom","Alice","Jhon","Mary","Jack"};

        for (int i = 0; i < 5; i++)
        {
            Student student = new Student("id-"+i,names[i],i%2==0);
            students.add(student);
        }
    }

    private void initWidget()
    {
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bindRemoteService();
            }
        });
    }

    private void addNewStudents()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    for(int i=0;i<students.size();i++)
                    {
                        stuManager.addNewStudent(students.get(i));
                    }
                    Thread.sleep(2000);
                    List<Student> stus = stuManager.getStudents();
                    Log.i("AIDL-Client","size of Students = "+stus.size());
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void bindRemoteService()
    {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("cn.liusiqian.remote", "cn.liusiqian.remote.AIDLService"));
        bindService(intent, connection, BIND_AUTO_CREATE);
    }
}
