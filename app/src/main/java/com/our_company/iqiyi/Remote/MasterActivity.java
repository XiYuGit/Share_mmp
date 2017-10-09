package com.our_company.iqiyi.Remote;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.our_company.iqiyi.Player.App;
import com.our_company.iqiyi.R;
import com.our_company.iqiyi.Remote.Communicate.AGEventHandler;
import com.our_company.iqiyi.Remote.Communicate.ConstantApp;
import com.our_company.iqiyi.Remote.Communicate.EngineConfig;
import com.our_company.iqiyi.Remote.Communicate.MyEngineEventHandler;
import com.our_company.iqiyi.Remote.Communicate.WorkerThread;
import com.our_company.iqiyi.Util.LoginUtil;

import java.io.File;
import java.io.IOException;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import xiyou.mobile.User;


/**
 * Created by miaojie on 2017/5/3.
 */

public class MasterActivity extends Activity  implements AGEventHandler {
    public static Handler handler;
    private RemoteImage remoteView;
    private Button remoteCancel;
    private RtcEngine mRtcEngine;
    protected RtcEngine rtcEngine() {
        return ((App) getApplication()).getWorkerThread().getRtcEngine();
    }

    protected final WorkerThread worker() {
        return ((App) getApplication()).getWorkerThread();
    }

    protected final EngineConfig config() {
        return ((App) getApplication()).getWorkerThread().getEngineConfig();
    }

    protected final MyEngineEventHandler event() {
        return ((App) getApplication()).getWorkerThread().eventHandler();
    }


    protected void initUIandEvent() {
        event().addEventHandler(this);

        String channelName = RemoteUtil.personal_uid+"";

        worker().joinChannel(channelName, config().mUid);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.master_activity);
        RemoteUtil.masterActivity=this;
        LoginUtil.waitingDialog=
                new ProgressDialog(MasterActivity.this);
        LoginUtil.waitingDialog.setTitle("等待对方响应");
        LoginUtil.waitingDialog.setMessage("等待中...");
        LoginUtil.waitingDialog.setIndeterminate(true);
        LoginUtil.waitingDialog.setCancelable(false);
        LoginUtil.waitingDialog.show();


        remoteView= (RemoteImage) findViewById(R.id.remote_image);
        remoteCancel= (Button) findViewById(R.id.remote_cancel);
        remoteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    RemoteUtil.socketClient.close();
                    RemoteUtil.socketClient=null;
                    RemoteUtil.socketMaster.close();

                    RemoteUtil.socketMaster=null;
                    MasterActivity.this.finish();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        RemoteUtil.masterActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                RemoteInfo remoteInfo= (RemoteInfo) msg.obj;
                if(remoteInfo.getType()!=null)
                {
                    Log.e("asd",remoteInfo.getType());
                    if(remoteInfo.getType().equals("heng")){
                        RemoteUtil.masterActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        return;
                    }
                    if (remoteInfo.getType().equals("shu"))
                    {
                        RemoteUtil.masterActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        return;
                    }
                }

                byte[]remoteBytes= remoteInfo.getRemoteDesktop();
                Bitmap bitmap= BitmapFactory.decodeByteArray(remoteBytes,0,remoteBytes.length);
                remoteView.setImageBitmap(bitmap);
            }
        };
        initUIandEvent();

        try {
            RemoteService.masterThread=new MasterThread(RemoteUtil.port,handler);
            RemoteService.masterThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        remoteView.setCanTouch(false);
        RemoteUtil.socketClient=null;
        RemoteUtil.socketMaster=null;
        RemoteUtil.canTouch=false;
        worker().leaveChannel("1");
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {

    }

    @Override
    public void onUserOffline(int uid, int reason) {

    }

    @Override
    public void onExtraCallback(int type, Object... data) {

    }
}
