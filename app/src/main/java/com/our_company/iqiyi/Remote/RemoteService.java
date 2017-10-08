package com.our_company.iqiyi.Remote;

import android.app.Instrumentation;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.our_company.iqiyi.R;
import com.our_company.iqiyi.Remote.Communicate.EngineConfig;
import com.our_company.iqiyi.Remote.Communicate.MyEngineEventHandler;
import com.our_company.iqiyi.Util.LoginUtil;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import xiyou.mobile.User;

import static com.our_company.iqiyi.Remote.ClientReceiveCmdThread.receivceCmd;

/**
 * Created by miaojie on 2017/5/3.
 */

public class RemoteService extends Service{
    private String TAG="RemoteService";
   private Reciver reciver;
   private ClientThread clientThread;
    private RtcEngine mRtcEngine;
    public static MasterThread masterThread;
   public class Reciver extends BroadcastReceiver {

       public Reciver()
       {
           RemoteUtil.handler=new Handler();
       }
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("收到了","123");
//            RemoteUtil.ipAdress=intent.getStringExtra("IPadress");
            if(intent.getStringExtra("control").equals("updateScreen"))
            {
                if(RemoteUtil.socketClient.isClosed()){
                    Log.e("结束了","remote");
                    Toast.makeText(RemoteService.this, "控制结束", Toast.LENGTH_SHORT).show();
                    RemoteUtil.manager.onDestroy();
                }
            }
            if (intent.getStringExtra("control").equals("startReciveCmd"))
            {
                if(!RemoteUtil.isFirstCmd)
                {
                    RemoteUtil.handler.removeCallbacks(RemoteUtil.runnable);
                    Log.e("第一次","qwe");
                }
                Log.e("来了","123");
               RemoteUtil.isFirstCmd=false;
                RemoteUtil.runnable=new Runnable() {
                    @Override
                    public void run() {
                        new Thread()
                        {
                            @Override
                            public void run() {
                                super.run();
                                receivceCmd(RemoteUtil.instrumentation,1,RemoteUtil.theLastX,RemoteUtil.theLastY);
                                RemoteUtil.isFirstCmd=true;
                                Log.e("postdelay","成功");
                            }
                        }.start();
                    }
                };
                RemoteUtil.handler.postDelayed(RemoteUtil.runnable,100);
            }
//            Log.e("地址",intent.getStringExtra("IPAdress"));
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("已开启","asd");
        reciver=new Reciver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("ASDFG");
        registerReceiver(reciver,intentFilter);
        String appId = "e581e1feef22478d80839d8b9986a904";

        MyEngineEventHandler engineEventHandler=new MyEngineEventHandler(this,new EngineConfig());
        mRtcEngine = RtcEngine.create(this, appId, engineEventHandler.mRtcEventHandler);
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
        mRtcEngine.enableAudioVolumeIndication(200, 3); // 200 ms
        int aa=mRtcEngine.enableAudio();
        Log.e("音频",aa+"");
        mRtcEngine.setLogFile(Environment.getExternalStorageDirectory()
                + File.separator + this.getPackageName() + "/log/agora-rtc.log");
        mRtcEngine.joinChannel(null, "001", "OpenVCall", RemoteUtil.personal_uid);
        new Thread()
        {
            @Override
            public void run() {

                Log.e("地址",RemoteUtil.ipAdress);
//                try {
//                    RemoteUtil.socketClient=new Socket(RemoteUtil.ipAdress,RemoteUtil.port);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                clientThread= new ClientThread(RemoteUtil.ipAdress,RemoteUtil.port);
                clientThread.start();
                Log.e("地址",RemoteUtil.ipAdress+"");
                ClientReceiveCmdThread receiveCmdThread=new ClientReceiveCmdThread();
                receiveCmdThread.start();

            }
        }.start();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy");
        RemoteUtil.isConnect=false;
        RemoteUtil.socketClient=null;
        Toast.makeText(RemoteService.this, "控制结束", Toast.LENGTH_SHORT).show();
        unregisterReceiver(reciver);
        mRtcEngine.leaveChannel();
    }
}
