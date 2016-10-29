package cn.liusiqian.messenger;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final int MSG_CLIENT = 1;
    public static final int MSG_REPLY = 2;
    public static final String MSG_CLIENT_DATA_KEY = "message";
    public static final String MSG_REPLY_DATA_KEY = "reply";

    private ServiceConnection mConnection;
    private Handler mReplyHandler;
    private Messenger mReplyMessenger;

    private View btnBind;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnBind = findViewById(R.id.btn);
        btnBind.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bindRemoteService();
            }
        });

        initServiceComponents();
    }

    private void bindRemoteService()
    {
        Intent intent = new Intent();
        /*显式启动*/
//        intent.setComponent(new ComponentName("cn.liusiqian.remote","cn.liusiqian.remote.MessengerService"));

        /*隐式启动*/
        intent.setAction("cn.liusiqian.remote.MessengerService");
        //一定要setPackage
        intent.setPackage("cn.liusiqian.remote");
        bindService(intent,mConnection,BIND_AUTO_CREATE);
    }

    private void initServiceComponents()
    {
        mConnection = new ServiceConnection()
        {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                Messenger messenger = new Messenger(service);
                Message msg = Message.obtain(null,MSG_CLIENT);
                Bundle bundle = new Bundle();
                bundle.putString(MSG_CLIENT_DATA_KEY,"Hello World!");
                msg.setData(bundle);
                //set Reply
                msg.replyTo = mReplyMessenger;
                try
                {
                    messenger.send(msg);
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {

            }
        };

        mReplyHandler = new Handler(){
            @Override
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case MSG_REPLY:
                        processReply(msg.getData());
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        };

        mReplyMessenger = new Messenger(mReplyHandler);
    }

    private void processReply(Bundle data)
    {
        if(data == null || data.isEmpty())
        {
            return;
        }

        String msg = data.getString(MSG_REPLY_DATA_KEY);
        if(!TextUtils.isEmpty(msg)){
            Log.i(TAG,"Get Reply from Server:"+msg);
        }
    }
}
