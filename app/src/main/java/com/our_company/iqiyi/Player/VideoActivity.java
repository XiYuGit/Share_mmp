package com.our_company.iqiyi.Player;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;

import com.our_company.iqiyi.Net.Data;
import com.our_company.iqiyi.R;
import com.our_company.iqiyi.Remote.RemoteInfo;
import com.our_company.iqiyi.Remote.RemoteUtil;

import java.io.IOException;
import java.io.ObjectOutputStream;

import cn.jzvd.JZVideoPlayer;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class VideoActivity extends AppCompatActivity {

    private MPlayer qv;
    private Button send,get;
    private String playSouce="http://i.snssdk.com/neihan/video/playback/?video_id=361b1c3b0c8642a5a1f77c0eb5acf08e&quality=480p&line=0&is_gif=0.mp4",title;
    private boolean synced=false;
    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlayout);
        ShareService.activityContext=this;
        startService(new Intent(this,ShareService.class));
        data=(Data)getIntent().getSerializableExtra("data");
        if(RemoteUtil.socketClient!=null)
        {
            if(RemoteUtil.socketClient.isConnected())
            {
                new Thread()
                {
                    @Override
                    public void run() {
                        RemoteInfo info=new RemoteInfo();
                        info.setType("heng");
                        ObjectOutputStream objectOutputStream= null;
                        try {
                            objectOutputStream = new ObjectOutputStream( RemoteUtil.socketClient.getOutputStream());
                            objectOutputStream.writeObject(info);
                            objectOutputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            }
        }
        //playSouce=data.getTv_id();

        qv=(MPlayer)findViewById(R.id.player);
        /*int w=((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        int h=w*9/16;
        if (w>((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight())
            h=((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();

        qv.setLayoutParams(new RelativeLayout.LayoutParams(w,h));
        cv.setLayoutParams(new RelativeLayout.LayoutParams(w,h));*/
        qv.setSource(data.getPlayUrlLow(),data.getTitle());
        qv.switchScreen(true);
        //qv.startWindowFullscreen();
        //title=data.getTitle();
        //if (data!=null)
        //(data.getTitle());

        //
    }

    private void solveIntent(int w,int h)
    {
        if (getIntent().getStringExtra("name")!=null)
        {
            int sw=getIntent().getIntExtra("width",w);
            int sh=getIntent().getIntExtra("height",h);
            Log.e("xx","sizeset"+sw+":"+sh);
            qv.enableSync(w,h,sw,sh,getIntent().getStringExtra("name"),true);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        qv=(MPlayer)findViewById(R.id.player);
        qv.setSource(data.getPlayUrlHigh(),data.getTitle());

//        setContentView(R.layout.playlayout);
//
//        qv=(MPlayer)findViewById(R.id.player);
        int w=((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        int h=w*9/16;
//
        if (newConfig.orientation==ORIENTATION_LANDSCAPE) {
            h = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        }
//
//        cv.switchScreen(newConfig.orientation==ORIENTATION_LANDSCAPE);
//        qv.setLayoutParams(new RelativeLayout.LayoutParams(w,h));
//        cv.setLayoutParams(new RelativeLayout.LayoutParams(w,h));
        if (newConfig.orientation==ORIENTATION_LANDSCAPE)
            solveIntent(w,h);
//        cv.setQiyi(qv);
//        cv.setSource(playSouce);
//        cv.setTitle(title);

    }

    @Override
    protected void onDestroy() {
        qv.quitSync();
        super.onDestroy();
        if(RemoteUtil.socketClient!=null)
        {
            if(RemoteUtil.socketClient.isConnected())
            {
                new Thread()
                {
                    @Override
                    public void run() {
                        RemoteInfo info=new RemoteInfo();
                        info.setType("shu");
                        ObjectOutputStream objectOutputStream= null;
                        try {
                            objectOutputStream = new ObjectOutputStream( RemoteUtil.socketClient.getOutputStream());
                            objectOutputStream.writeObject(info);
                            objectOutputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();

            }
        }
        //qv.release();
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            //return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ShareService.activityContext=this;
        RemoteUtil.clientWindow=getWindow();
    }
}
