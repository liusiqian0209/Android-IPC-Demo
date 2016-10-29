package cn.liusiqian.remote;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Créé par liusiqian 16/10/29.
 */

public class MessengerService extends Service
{
    public static final String TAG = MessengerService.class.getSimpleName();
    public static final int MSG_CLIENT = 1;
    public static final int MSG_REPLY = 2;
    public static final String MSG_CLIENT_DATA_KEY = "message";
    public static final String MSG_REPLY_DATA_KEY = "reply";

    private Handler mHandler;
    private Messenger messenger;

    public MessengerService()
    {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case MSG_CLIENT:
                        processRemoteData(msg.getData(),msg.replyTo);
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        };
        messenger = new Messenger(mHandler);
    }

    private void processRemoteData(Bundle data, Messenger replyTo)
    {
        if(data == null || data.isEmpty()){
            Log.i(TAG,"data is Empty");
            return;
        }

        String msg = data.getString(MSG_CLIENT_DATA_KEY);
        if(!TextUtils.isEmpty(msg)){
            Log.i(TAG,"Server get Data from Client:"+msg);
        }

        Message message = Message.obtain(null,MSG_REPLY);
        Bundle bundle = new Bundle();
        bundle.putString(MSG_REPLY_DATA_KEY,"已收到");
        message.setData(bundle);
        try
        {
            Log.i(TAG,"reply");
            replyTo.send(message);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        if(messenger == null)
        {
            throw new IllegalStateException("Messenger has not been initialized!");
        }
        Log.i(TAG,"onBind");
        return messenger.getBinder();
    }
}
