package com.our_company.xymobile.Player;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.our_company.xymobile.Net.Data;
import com.our_company.xymobile.R;
import com.our_company.xymobile.Remote.RemoteInfo;
import com.our_company.xymobile.Remote.RemoteUtil;

import java.io.IOException;
import java.io.ObjectOutputStream;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class VideoActivity extends AppCompatActivity {

    private String playSouce="http://i.snssdk.com/neihan/video/playback/?video_id=361b1c3b0c8642a5a1f77c0eb5acf08e&quality=480p&line=0&is_gif=0.mp4",title;
    private Data data;

    private PlayerPresenter mPresenter=new PlayerPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlayout2);
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
        mPresenter.init(getWindow().getDecorView(),data,this);
    }

    private void solveIntent(int w,int h)
    {
        if (getIntent().getStringExtra("name")!=null)
        {
            int sw=getIntent().getIntExtra("width",w);
            int sh=getIntent().getIntExtra("height",h);

            mPresenter.enableSync(w,h,sw,sh,getIntent().getStringExtra("name"),true);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        //qv=(MPlayer)findViewById(R.id.player);
        //qv.setSource(data.getPlayUrlHigh(),data.getTitle());
        int w=((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        int h=((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();;

        if (newConfig.orientation==ORIENTATION_LANDSCAPE)
            solveIntent(w,h);

    }

    @Override
    protected void onDestroy() {
        mPresenter.quitSync();
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
        mPresenter.release();
    }

    @Override
    public void onBackPressed() {
//        if (JZVideoPlayer.backPress()) {
//            //return;
//        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
//        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ShareService.activityContext=this;
        RemoteUtil.clientWindow=getWindow();
    }
}
