package com.our_company.iqiyi.Player;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.our_company.iqiyi.Net.Data;

import xiyou.mobile.User;

/**
 * Created by user on 2017/6/14.
 */

public class ShareService extends Service {

    private static ShareService instance=null;
    private User.OnRequestSyncListener requestSyncListener=null;
    private User.OnDataSetListener dataSetListener=null;
    private User.OnScreenSizeSetListener screenSizeSetListener=null;

    private boolean setOne=false,setdata=false,setsize=false;
    private Intent i=null;
    private String data,name;
    private H mh=new H();

    private int w,h;
    public static Context activityContext=null;

    private static final int SHOWDIALOG=0;
    private static final int STARTACTIVITY=1;
    private static final int ENDSYNC=2;

    public ShareService()
    {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (instance!=null)
            return super.onStartCommand(intent, flags, startId);
        instance=this;

        init();
        return super.onStartCommand(intent, flags, startId);
    }

    public static void notifyLoged()
    {
        instance.init();
    }

    private void init()
    {
        Log.e("xx","shareservice init");
        if (User.get()==null)
            return ;
        i=new Intent(ShareService.this,VideoActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        new Thread()
        {
            @Override
            public void run() {
                //User.login("test1","test1");
                removeListeners();
                User.get().addOnRequestSyncListener(requestSyncListener=new User.OnRequestSyncListener() {
                    @Override
                    public void onRequestSync(String name) {

                        ShareService.this.name=name;
                        mh.sendEmptyMessage(SHOWDIALOG);
                    }
                });

                User.get().addOnDataSetListener(dataSetListener=new User.OnDataSetListener() {
                    @Override
                    public void onDataSet(String data, String name) {
                        ShareService.this.data=data;
                        if (setsize) {
                            mh.sendEmptyMessage(STARTACTIVITY);
                        }

                        setdata=true;
                    }
                });

                User.get().addOnScreenSizeSetListener(screenSizeSetListener=new User.OnScreenSizeSetListener() {
                    @Override
                    public void onScreenSizeSet(String name, int w, int h) {
                        ShareService.this.w=w;
                        ShareService.this.h=h;
                        if (setdata)
                        {
                            mh.sendEmptyMessage(STARTACTIVITY);
                        }else
                            setsize=true;
                    }
                });

            }
        }.start();
    }

    private void removeListeners()
    {
        if (requestSyncListener!=null&&User.get()!=null)
            User.get().removeOnRequestSyncListener(requestSyncListener);

        if (dataSetListener!=null&&User.get()!=null)
            User.get().removeOnDataSetListener(dataSetListener);

        if (screenSizeSetListener!=null&&User.get()!=null)
            User.get().removeOnScreenSizeSetListener(screenSizeSetListener);
    }

    public static void endSync()
    {
        ShareService.instance.mh.sendEmptyMessage(ENDSYNC);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activityContext=null;
        instance=null;
        removeListeners();
    }

    class H extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case SHOWDIALOG:
                    MDialog.showDefault(activityContext, "共享请求", name + "邀请与您共享视频,是否同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread()
                            {
                                @Override
                                public void run() {
                                    super.run();
                                    User.get().permitSync(ShareService.this.name);
                                }
                            }.start();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread()
                            {
                                @Override
                                public void run() {
                                    super.run();
                                    User.get().refusedSync(ShareService.this.name);
                                }
                            }.start();
                        }
                    });
                    break;
                case STARTACTIVITY:
                    i.putExtra("name",name);
                    i.putExtra("data",new Data("","","","",data));
                    i.putExtra("width",w);
                    i.putExtra("height",h);
                    setdata=setsize=false;
                    startActivity(i);
                    break;
                case ENDSYNC:
                    Toast.makeText(ShareService.this,"视频分享已结束",Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }
}
