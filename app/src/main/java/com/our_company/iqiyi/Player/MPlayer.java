package com.our_company.iqiyi.Player;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.our_company.iqiyi.Player.view.DrawView;
import com.our_company.iqiyi.R;
import com.our_company.iqiyi.Remote.Communicate.AGEventHandler;
import com.our_company.iqiyi.Remote.Communicate.EngineConfig;
import com.our_company.iqiyi.Remote.Communicate.MyEngineEventHandler;
import com.our_company.iqiyi.Remote.Communicate.WorkerThread;
import com.our_company.iqiyi.Remote.RemoteUtil;

import java.io.File;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayerStandard;
import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import xiyou.mobile.User;

/**
 * Created by Administrator on 2017/10/6.
 */

public class MPlayer extends JZVideoPlayerStandard implements Runnable,AGEventHandler{

    private MDialog mdialog = null;

    private RtcEngine mRtcEngine;
    private View  draw;
    public DrawView dv;
    private ImageButton draw_cancel, draw_ok, draw_more,play_more, play_draw;
    private static int history = -1;
    private boolean land = true, synced = false, ended = false, running = true, startSync = false, shouldStart = false;

    private boolean seeking = true;
    private boolean performPlay=false;
    private int width;
    private boolean sizeSet = false;
    private static String name = null;
    private Context c;
    private static String playSource = null;
    private int w, h, sw, sh;

    private User.OnSeekListener seekl = null;
    private User.OnStartPauseListener spl = null;
    private User.OnScreenSizeSetListener sizeSetl = null;
    private User.OnEndSyncListener endl = null;



    private H mh=new H();

    public MPlayer(Context context) {
        super(context);
        mdialog = new MDialog(context, this);
        Log.e("xx","init");
    }

    public MPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mdialog = new MDialog(context, this);
        Log.e("xx","init");
    }

    @Override
    public int getLayoutId() {
        return R.layout.mplayer;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        Log.e("xx","init 1");
        this.c = context;
        LayoutInflater lf = LayoutInflater.from(c);
        draw = lf.inflate(R.layout.drawlayout, null);
        addView(draw);
        draw.setVisibility(View.GONE);
        dv = (DrawView) draw.findViewById(R.id.draw);

        draw_cancel = (ImageButton) draw.findViewById(R.id.draw_cancel);
        draw_ok = (ImageButton) draw.findViewById(R.id.draw_ok);
        draw_more = (ImageButton) draw.findViewById(R.id.draw_more);
        play_more = (ImageButton) findViewById(R.id.play_more);
        play_draw = (ImageButton)findViewById(R.id.play_draw);
        draw_cancel.setOnClickListener(this);
        draw_ok.setOnClickListener(this);
        draw_more.setOnClickListener(this);
        play_more.setOnClickListener(this);
        play_draw.setOnClickListener(this);

        if (name == null) {
            play_draw.setVisibility(View.GONE);
        }

        if (!land) {
            //play_more.setVisibility(View.GONE);
        }

        draw_more.setVisibility(View.GONE);
        new Thread(this).start();

        //if (User.get() == null)
            //play_more.setVisibility(View.GONE);

//        MDialog.showHuanchong(c, new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialogInterface) {
//                MDialog.dismissHuanchong();
//                ((Activity) MPlayer.this.c).finish();
//            }
//        });
        //startWindowFullscreen();
    }

    public void removeListener() {
        if (seekl != null)
            User.get().removeOnSeekListener(seekl);

        if (spl != null)
            User.get().removeOnStartPauseListener(spl);

        if (sizeSetl != null)
            User.get().removeOnScreenSizeSetListener(sizeSetl);

        dv.removeAllListener();
        mdialog.removeAllListener();
    }

    @Override
    public void run() {
        while(running)
        {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (seeking) {
                mh.sendEmptyMessage(H.SEEK);
                if (startSync)
                    if (!synced)
                        if (name!=null)
                        {
                            post(new Runnable() {
                                @Override
                                public void run() {
                                    User.get().syncSeek(getCurrentPositionWhenPlaying(),name);
                                }
                            });
                        }

            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.start:
                if (!performPlay&&name!=null) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            User.get().startpause(name);
                        }
                    }.start();
                }else
                {
                    performPlay=false;
                }
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
                draw.setVisibility(View.GONE);
                draw_cancel.setVisibility(View.GONE);
                draw_ok.setVisibility(View.GONE);
                play_draw.setVisibility(View.VISIBLE);
                //play_more.setVisibility(View.VISIBLE);
                break;
            case R.id.draw_more:
                break;
            case R.id.back:
                ((Activity) c).finish();
                break;
            case R.id.play_more:
                if (User.get()==null)
                {
                    Toast.makeText(c, "请先登录以使用此功能", Toast.LENGTH_SHORT).show();
                }else
                    mdialog.show();
                break;
            case R.id.play_draw:
                play_draw.setVisibility(View.GONE);
                draw.setVisibility(View.VISIBLE);
                play_more.setVisibility(View.GONE);
                draw_cancel.setVisibility(VISIBLE);
                draw_ok.setVisibility(VISIBLE);
                break;
            default:
        }
    }

    public void setSource(String s,String t)
    {
        playSource=s;
        setUp(s
                , JZVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, t);

    }

    public void enableSync(String name)
    {
        if (seeking)
        {

            //mh.sendEmptyMessage(H.STARTPAUSE);
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
                MPlayer.this.sw=w;
                MPlayer.this.sh=h;
                mh.sendEmptyMessage(H.SYNC);
            }
        });
        //mh.sendEmptyMessage(SYNC);

        mh.sendEmptyMessage(H.SHOWWAIT);
    }
    protected RtcEngine rtcEngine() {
        return ((App) ((Activity)c).getApplication()).getWorkerThread().getRtcEngine();
    }

    protected final WorkerThread worker() {
        return ((App) ((Activity)c).getApplication()).getWorkerThread();
    }

    protected final EngineConfig config() {
        return ((App) ((Activity)c).getApplication()).getWorkerThread().getEngineConfig();
    }

    protected final MyEngineEventHandler event() {
        return ((App) ((Activity)c).getApplication()).getWorkerThread().eventHandler();
    }


    protected void initUIandEvent() {
        event().addEventHandler(this);

        String channelName = RemoteUtil.personal_uid+"";

        worker().joinChannel(channelName, config().mUid);

    }

    public void enableSync(int w,int h,int sw,int sh,String name,boolean sync)
    {

        initUIandEvent();
        Log.e("xx","enable sync");
        MDialog.dismissWait();
        startSync=true;
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
                    User.get().sendScreenSize(MPlayer.this.w,MPlayer.this.h,MPlayer.this.name);
                }
            }.start();

        this.name=name;
        //mh.sendEmptyMessage(SYNC);

        // synced=true;
        //draw.setVisibility(View.VISIBLE);
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
            public void onSeek(final int position, String name) {

                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (Math.abs(position-getCurrentPositionWhenPlaying())>3000)
                        {
                            try {
                                JZMediaManager.instance().mediaPlayer.seekTo(position);
                            }catch (Exception e)
                            {

                            }
                        }
                        }
                    });
                }
        });
        if (spl!=null)
            User.get().removeOnStartPauseListener(spl);
        User.get().addOnStartPauseListener(spl=new User.OnStartPauseListener() {
            @Override
            public void onStartPause() {
                startSync=true;
                mh.sendEmptyMessage(H.CANCELWAIT);
                if (shouldStart)
                {
                    shouldStart=false;
                    new Thread()
                    {
                        @Override
                        public void run() {
                            super.run();
                            //User.get().startpause(MPlayer.this.name);
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
            worker().leaveChannel("1");
        }
    }

    public void quitSync()
    {
        running=false;
        if (name==null)
            return ;

        removeListener();
//        mRtcEngine.leaveChannel();
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

    public String getSyncName()
    {
        return name;
    }

    public String getSource()
    {
        return playSource;
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
                case SEEKINIT:break;
                case SEEK:
                    break;
                case SYNC:
                    enableSync(getWidth(),getHeight(),sw,sh,name,false);
                    break;
                case END:
                    endSync();
                    break;
                case STARTPAUSE:

                    MDialog.dismissWait();
                    performPlay=true;
                    startButton.performClick();
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
}
