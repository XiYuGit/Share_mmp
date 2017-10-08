package com.our_company.iqiyi.Player.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.our_company.iqiyi.Player.MDialog;
import com.our_company.iqiyi.Player.ShareService;
import com.our_company.iqiyi.Player.XUANJI;
import com.our_company.iqiyi.R;
import com.our_company.iqiyi.Remote.Communicate.EngineConfig;
import com.our_company.iqiyi.Remote.Communicate.MyEngineEventHandler;
import com.our_company.iqiyi.Remote.RemoteUtil;
import com.qiyi.video.playcore.ErrorCode;
import com.qiyi.video.playcore.IQYPlayerHandlerCallBack;
import com.qiyi.video.playcore.QiyiVideoView;

import java.io.File;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import xiyou.mobile.User;

/**
 * Created by user on 2017/5/31.
 */

public class ControlView extends RelativeLayout implements View.OnClickListener,Runnable{

    private MDialog mdialog=null;

    private RtcEngine mRtcEngine;
    private View play,draw,play_control,play_control_back;
    public DrawView dv;
    private ImageButton draw_cancel,draw_ok,draw_more,
    play_back,play_start,play_full,play_more,play_draw;
    private Button xuanji;
    private QiyiVideoView qiyi;
    private SeekBar seek;
    private TextView currentTime,duration;
    private static int history=-1;
    private boolean land=false,synced=false,ended=false,running=true,startSync=false,shouldStart=false;

    private boolean seeking=false;
    private int width;
    private boolean sizeSet=false;
    private String name=null;
    private Context c;
    private String playSource=null;
    private int w,h,sw,sh;

    private User.OnSeekListener seekl=null;
    private User.OnStartPauseListener spl=null;
    private User.OnScreenSizeSetListener sizeSetl=null;
    private User.OnEndSyncListener endl=null;

    private H mh=new H();

    public ControlView(Context context) {
        super(context);
        init(context);
        addView(draw);
        addView(play);
        //draw.setVisibility(View.GONE);
        draw_cancel.setVisibility(View.GONE);
        draw_ok.setVisibility(View.GONE);
    }

    public ControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        addView(draw);
        addView(play);

        draw_cancel.setVisibility(View.GONE);
        draw_ok.setVisibility(View.GONE);
        //draw.setVisibility(View.GONE);
    }


    public String getSource()
    {
        return playSource;
    }

    public void setQiyi(QiyiVideoView v)
    {
        qiyi=v;
        qiyi.setPlayerCallBack(new MCallback());
    }

    private void init(Context c)
    {
        mdialog=new MDialog(c,this);
        this.c=c;
        LayoutInflater lf = LayoutInflater.from(c);
        play=lf.inflate(R.layout.play_control,null);
        draw=lf.inflate(R.layout.drawlayout,null);
        dv=(DrawView) draw.findViewById(R.id.draw);

        draw_cancel=(ImageButton) draw.findViewById(R.id.draw_cancel);
        draw_ok=(ImageButton)draw.findViewById(R.id.draw_ok);
        draw_more=(ImageButton)draw.findViewById(R.id.draw_more);
        play_back=(ImageButton)play.findViewById(R.id.play_back);
        play_start=(ImageButton)play.findViewById(R.id.play_start);
        play_full=(ImageButton)play.findViewById(R.id.play_full);
        play_more=(ImageButton)play.findViewById(R.id.play_more);
        play_draw=(ImageButton)play.findViewById(R.id.play_draw);
        play_control=play.findViewById(R.id.play_control);
        play_control_back=play.findViewById(R.id.play_control_back);
        currentTime=(TextView)play.findViewById(R.id.play_time_now);
        duration=(TextView)play.findViewById(R.id.play_time_all);
        seek=(SeekBar)play.findViewById(R.id.play_seek);
        xuanji=(Button)play.findViewById(R.id.b_xuanji);

        xuanji.setOnClickListener(this);
        draw_cancel.setOnClickListener(this);
        draw_ok.setOnClickListener(this);
        draw_more.setOnClickListener(this);
        play_back.setOnClickListener(this);
        play_start.setOnClickListener(this);
        play_full.setOnClickListener(this);
        play_more.setOnClickListener(this);
        play_draw.setOnClickListener(this);
        play_control_back.setOnClickListener(this);
        play_control.setOnClickListener(this);

        seek.setOnSeekBarChangeListener(new SeekBar.    OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int a=progress;
                int sec=a%60;
                int min=a/60%60;
                int h=a/60/60;
                if (h!=0)
                    currentTime.setText(String.format("%02d:%02d:%02d",h,min,sec));
                else
                    currentTime.setText(String.format("%02d:%02d",min,sec));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (name!=null)
                {
                    new SeekThread(seekBar.getProgress()*1000).start();
                }
                qiyi.seekTo(seekBar.getProgress()*1000);
            }
        });

        if (name==null)
        {
            play_draw.setVisibility(View.GONE);
        }

        if (!land)
        {
            play_more.setVisibility(View.GONE);
        }

        draw_more.setVisibility(View.GONE);
        new Thread(this).start();

        if(User.get()==null)
        play_more.setVisibility(View.GONE);
        play_full.setVisibility(View.GONE);
        xuanji.setVisibility(View.GONE);

        Log.e("xx","---------------------------------");
        MDialog.showHuanchong(c, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                MDialog.dismissHuanchong();
                ((Activity)ControlView.this.c).finish();
            }
        });
    }

    public void removeListener()
    {
        if (seekl!=null)
            User.get().removeOnSeekListener(seekl);

        if (spl!=null)
            User.get().removeOnStartPauseListener(spl);

        if (sizeSetl!=null)
            User.get().removeOnScreenSizeSetListener(sizeSetl);

        dv.removeAllListener();
        mdialog.removeAllListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.b_xuanji:
                new XUANJI(c,this).show();
                break;
            case R.id.draw_cancel:
                dv.clear();
                postInvalidate();
                new Thread()
                {
                    @Override
                    public void run() {
                        super.run();
                        User.get().clearScreen(name);
                    }
                }.start();
                break;
            case R.id.draw_ok:
                //draw.setVisibility(View.GONE);
                draw_cancel.setVisibility(View.GONE);
                draw_ok.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                break;
            case R.id.draw_more:
                break;
            case R.id.play_back:
                ((Activity) c).finish();
                break;
            case R.id.play_full:
                if (!land) {
                    ((Activity) c).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    //setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

                    WindowManager.LayoutParams params = ((Activity)c).getWindow().getAttributes();
                    params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    ((Activity)c).getWindow().setAttributes(params);
                    ((Activity)c).getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    land=true;
                }
                else {
                    ((Activity) c).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    //v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                    WindowManager.LayoutParams params = ((Activity)c).getWindow().getAttributes();
                    params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    ((Activity)c).getWindow().setAttributes(params);
                    ((Activity)c).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                }
                break;
            case R.id.play_more:
                if (User.get()==null)
                {
                    Toast.makeText(c, "请先登录以使用此功能", Toast.LENGTH_SHORT).show();
                }else
                mdialog.show();
                break;
            case R.id.play_start:
                if (qiyi.isPlaying())
                {
                    play_start.setImageResource(R.drawable.start);
                    qiyi.pause();
                }else
                {
                    play_start.setImageResource(R.drawable.pause);
                    qiyi.start();
                }

                if (name!=null)
                new Thread()
                {
                    @Override
                    public void run() {
                        super.run();
                        User.get().startpause(name);
                    }
                }.start();
                break;
            case R.id.play_draw:
                //draw.setVisibility(View.VISIBLE);
                draw_cancel.setVisibility(VISIBLE);
                draw_ok.setVisibility(VISIBLE);
                play.setVisibility(View.GONE);
                break;
            case R.id.play_control:
                play_control.setVisibility(View.GONE);
                break;
            case R.id.play_control_back:
                if (play_control.getVisibility()==View.GONE)
                play_control.setVisibility(View.VISIBLE);
                else
                    play_control.setVisibility(View.GONE);
                break;
            default:
                play_control.setVisibility(View.VISIBLE);
        }
    }


    public void setSource(String s)
    {
        playSource=s;
        qiyi.setPlayData(s);

    }

    public void enableSync(String name)
    {
        if (seeking)
        {
            qiyi.seekTo(0);
            mh.sendEmptyMessage(H.STARTPAUSE);
            shouldStart=true;
        }

        this.name=name;
        sizeSet=true;
        Log.e("xx","enable sync1");
        if (sizeSetl!=null)
            User.get().removeOnScreenSizeSetListener(sizeSetl);
        User.get().addOnScreenSizeSetListener(sizeSetl=new User.OnScreenSizeSetListener() {
            @Override
            public void onScreenSizeSet(String name, int w, int h) {
                ControlView.this.sw=w;
                ControlView.this.sh=h;
                mh.sendEmptyMessage(H.SYNC);
            }
        });
        //mh.sendEmptyMessage(SYNC);

        mh.sendEmptyMessage(H.SHOWWAIT);
    }

    public void enableSync(int w,int h,int sw,int sh,String name,boolean sync)
    {
        String appId = "e581e1feef22478d80839d8b9986a904";
        MyEngineEventHandler engineEventHandler=new MyEngineEventHandler(c,new EngineConfig());
        mRtcEngine = RtcEngine.create(c, appId, engineEventHandler.mRtcEventHandler);
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
        mRtcEngine.enableAudioVolumeIndication(200, 3); // 200 ms
        int aa=mRtcEngine.enableAudio();
        Log.e("音频",aa+"");
        mRtcEngine.setLogFile(Environment.getExternalStorageDirectory()
                + File.separator + c.getPackageName() + "/log/agora-rtc.log");
        mRtcEngine.joinChannel(null, "001", "OpenVCall", RemoteUtil.personal_uid);

        Log.e("xx","enable sync");
        this.w=w;
        this.h=h;
        ended=false;
        synced=sync;
        if (!sizeSet)
            new Thread()
            {
                @Override
                public void run() {
                    super.run();
                    User.get().sendScreenSize(ControlView.this.w,ControlView.this.h,ControlView.this.name);
                }
            }.start();

        this.name=name;
        //mh.sendEmptyMessage(SYNC);

       // synced=true;
        draw.setVisibility(View.VISIBLE);
        play.setVisibility(View.VISIBLE);
        play_full.setVisibility(View.GONE);
        play_more.setVisibility(View.GONE);
        play_draw.setVisibility(View.VISIBLE);
        //play_start.setVisibility(View.GONE);

        //draw_cancel.setVisibility(View.GONE);
        draw_more.setVisibility(View.GONE);
        //draw_ok.setVisibility(View.GONE);

        WindowManager.LayoutParams params = ((Activity)c).getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        ((Activity)c).getWindow().setAttributes(params);
        ((Activity)c).getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        land=true;
        dv.enableSync(w,h,sw,sh,name);

        if (endl!=null)
            User.get().removeOnEndSyncListener(endl);
        User.get().addOnEndSyncListener(endl=new User.OnEndSyncListener() {
            @Override
            public void onEndSync(String name) {
                mh.sendEmptyMessage(H.END);
            }
        });

        if (seekl!=null)
            User.get().removeOnSeekListener(seekl);

        User.get().addOnSeekListener(seekl=new User.OnSeekListener() {
            @Override
            public void onSeek(int position, String name) {
                if (Math.abs(position-qiyi.getCurrentPosition())>500)
                {
                    qiyi.seekTo(position);
                }
            }
        });
        if (spl!=null)
            User.get().removeOnStartPauseListener(spl);
        User.get().addOnStartPauseListener(spl=new User.OnStartPauseListener() {
            @Override
            public void onStartPause() {
                    startSync=true;
                Log.e("xx","--------------------start sync");
                mh.sendEmptyMessage(H.CANCELWAIT);
                if (shouldStart)
                {
                    shouldStart=false;
                    new Thread()
                    {
                        @Override
                        public void run() {
                            super.run();
                            User.get().startpause(ControlView.this.name);
                        }
                    }.start();
                }

                mh.sendEmptyMessage(H.STARTPAUSE);
            }
        });
    }

    private void endSync()
    {
        ended=true;
        ShareService.endSync();
        if (synced)
        ((Activity)c).finish();
        else {
            removeListener();
            //play.setVisibility(View.GONE);
            //play_full.setVisibility(View.VISIBLE);
            play_more.setVisibility(View.VISIBLE);
            play_draw.setVisibility(View.GONE);
            draw.setVisibility(View.GONE);
            //play_start.setVisibility(View.GONE);

            //draw_cancel.setVisibility(View.GONE);
            draw_more.setVisibility(View.VISIBLE);
            name=null;
            synced=false;
            sizeSet=false;

            mRtcEngine.leaveChannel();
        }
    }

    public void quitSync()
    {
        running=false;
        if (name==null)
            return ;

        removeListener();
        mRtcEngine.leaveChannel();
        if (!ended)
        {
            ShareService.endSync();
            ended=true;
            new Thread()
            {
                @Override
                public void run() {
                    super.run();
                    User.get().endSync(name);
                    name=null;
                    synced=false;
                    sizeSet=false;
                }
            }.start();
        }else
        {
            name=null;
            synced=false;
            sizeSet=false;
        }


    }

    public void switchScreen(boolean land)
    {
        //MDialog.dismissHuanchong();
        this.land=land;
        //setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if (!land)
            play_more.setVisibility(View.GONE);
        else {
            WindowManager.LayoutParams params = ((Activity)c).getWindow().getAttributes();
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            ((Activity)c).getWindow().setAttributes(params);
            ((Activity)c).getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            play_more.setVisibility(View.VISIBLE);
        }
    }

    public void saveSeek()
    {
        history=qiyi.getCurrentPosition()-1000;
        if (history<0)
            history=-1;
    }


    public void setTitle(String title)
    {
        ((TextView)play.findViewById(R.id.title_play_name)).setText(title);
    }


    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //sizeSet=false;
        //onSizeChanged(0,0,0,0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        //if (sizeSet)
        //    return ;
/*
        sizeSet=true;
        w=((WindowManager)c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        h=w*9/16;
        //qiyi.setLayoutParams(new RelativeLayout.LayoutParams(w,h));
        setLayoutParams(new RelativeLayout.LayoutParams(w,h));*/
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void run() {
        while(running)
        {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (seeking) {
                mh.sendEmptyMessage(H.SEEK);
                if (startSync)
                if (!synced)
                if (name!=null)
                User.get().syncSeek(qiyi.getCurrentPosition(),name);
            }
        }
    }

    class MCallback implements IQYPlayerHandlerCallBack {

        public static final int IDLE=0;
        public static final int INITED=1;
        public static final int PREPARED=2;
        public static final int CANREAD=4;
        public static final int ADSING=8;
        public static final int PLAYING=16;
        public static final int ONEEND=32;
        public static final int ERROR=64;
        public static final int ALLEND=128;

        @Override
        public void OnSeekSuccess(long l) {
        }

        @Override
        public void OnWaiting(boolean b) {
        }

        @Override
        public void OnError(ErrorCode errorCode) {
            switch(errorCode)
            {
                case TIME_OUT:
                case REQUEST_ERROR:
                    //setSource(playSource);
                    break;
            }
        }

        @Override
        public void OnPlayerStateChanged(int i) {

            switch (i)
            {
                case IDLE:
                    break;
                case INITED:
                    break;
                case PREPARED:
                    break;
                case CANREAD:
                    break;
                case ADSING:
                    break;
                case PLAYING:
                    mh.sendEmptyMessage(H.CANCELHUANCHONG);
                    mh.sendEmptyMessage(H.SEEKINIT);
                    seeking=true;
                    if (history!=-1)
                    {
                        qiyi.seekTo(history);
                        history=-1;
                    }

                    if (name!=null)
                    {
                        //startSync=true;
                        new Thread()
                        {
                            @Override
                            public void run() {
                                super.run();
                                User.get().startpause(name);
                            }
                        }.start();

                        if (!startSync) {
                            Log.e("xx","---------------pause");

                            mh.sendEmptyMessage(H.STARTPAUSE);
                        }
                    }
                    //qiyi.pause();
                    break;
                case ONEEND:
                    seeking=false;
                    break;
                case ERROR:
                    seeking=false;
                    break;
                case ALLEND:
                    seeking=false;
                    break;
            }
        }
    }

    public String getSyncName()
    {
        return name;
    }

    class H extends Handler
    {
        public static final int SEEKINIT=0;
        public static final int SEEK=1;
        public static final int SYNC=2;
        public static final int END=3;
        public static final int STARTPAUSE=4;
        public static final int CANCELHUANCHONG=5;
        public static final int CANCELWAIT=6;
        public static final int SHOWWAIT=7;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what)
            {
                case SEEKINIT:
                    seek.setMax(qiyi.getDuration()/1000);
                    int a=qiyi.getDuration()/1000;
                    int sec=a%60;
                    int min=a/60%60;
                    int h=a/60/60;
                    if (h!=0)
                        duration.setText(String.format("%02d:%02d:%02d",h,min,sec));
                    else
                        duration.setText(String.format("%02d:%02d",min,sec));
                    break;
                case SEEK:
                    seek.setProgress(qiyi.getCurrentPosition()/1000);
                    break;
                case SYNC:
                    enableSync(getWidth(),getHeight(),sw,sh,name,false);
                    break;
                case END:
                    endSync();
                    break;
                case STARTPAUSE:

                    if (!seeking)
                        break;

                    MDialog.dismissWait();
                    if (qiyi.isPlaying())
                    {
                        play_start.setImageResource(R.drawable.start);
                        qiyi.pause();
                    }else
                    {
                        play_start.setImageResource(R.drawable.pause);
                        qiyi.start();
                    }
                    break;
                case CANCELHUANCHONG:
                    MDialog.dismissHuanchong();
                    break;
                case CANCELWAIT:
                    MDialog.dismissWait();
                    break;
                case SHOWWAIT:
                    MDialog.showWait(c, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            ((Activity)c).finish();
                        }
                    });
                    break;
            }

        }
    }

    class SeekThread extends Thread
    {
        int position;
        public SeekThread(int position)
        {
            this.position=position;
        }

        @Override
        public void run() {
            super.run();
            User.get().syncSeek(position,name);
        }
    }

}
