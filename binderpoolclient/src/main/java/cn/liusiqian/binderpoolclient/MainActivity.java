package cn.liusiqian.binderpoolclient;

import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.liusiqian.remote.binderpool.BinderCode;
import cn.liusiqian.remote.binderpool.IAdd;
import cn.liusiqian.remote.binderpool.ISub;

public class MainActivity extends AppCompatActivity
{
    private EditText edit1, edit2;
    private Button btnAdd, btnSub;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                BinderPool.getInstance(MainActivity.this);
            }
        }).start();
    }

    private void initWidget()
    {
        handler = new Handler();
        edit1 = (EditText) findViewById(R.id.edit1);
        edit2 = (EditText) findViewById(R.id.edit2);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnSub = (Button) findViewById(R.id.btn_sub);
        btnAdd.setOnClickListener(ocl);
        btnSub.setOnClickListener(ocl);
    }

    private View.OnClickListener ocl = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            int num1 = Integer.valueOf(edit1.getEditableText().toString());
            int num2 = Integer.valueOf(edit2.getEditableText().toString());
            doWork(view.getId(), num1, num2);
        }
    };

    private void doWork(@IdRes final int id, final int num1, final int num2)
    {

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                IBinder binder = null;
                int result = 0;
                try
                {
                    switch (id)
                    {
                        case R.id.btn_add:
                            binder = BinderPool.getInstance(MainActivity.this).queryBinder(BinderCode.BINDER_CODE_ADD);
                            result = (IAdd.Stub.asInterface(binder)).add(num1, num2);
                            break;
                        case R.id.btn_sub:
                            binder = BinderPool.getInstance(MainActivity.this).queryBinder(BinderCode.BINDER_CODE_SUB);
                            result = (ISub.Stub.asInterface(binder)).sub(num1, num2);
                            break;
                        default:
                    }
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
                final int finalResult = result;
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(MainActivity.this, "Result is " + finalResult, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
}
