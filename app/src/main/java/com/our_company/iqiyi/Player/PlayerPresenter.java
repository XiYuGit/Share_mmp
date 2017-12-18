package com.our_company.iqiyi.Player;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.our_company.iqiyi.Net.Data;
import com.our_company.iqiyi.Player.view.DrawView;
import com.our_company.iqiyi.R;
import com.our_company.iqiyi.Remote.Communicate.AGEventHandler;
import com.our_company.iqiyi.Remote.Communicate.EngineConfig;
import com.our_company.iqiyi.Remote.Communicate.MyEngineEventHandler;
import com.our_company.iqiyi.Remote.Communicate.WorkerThread;
import com.our_company.iqiyi.Remote.RemoteUtil;
import com.our_company.iqiyi.Util.Rf;

import java.lang.reflect.Field;

import xiyou.mobile.User;

/**
 * Created by Administrator on 2017/12/15.
 */

public class PlayerPresenter implements PlaybackControlView.VisibilityListener,View.OnClickListener,Runnable,Player.EventListener,AGEventHandler,TimeBar.OnScrubListener {
    private static Field sController,sNext,sPrevious,sTimebar,sForward,sRewind;
    static
    {
        try {
            sController=SimpleExoPlayerView.class.getDeclaredField("controller");
            sNext=PlaybackControlView.class.getDeclaredField("nextButton");
            sPrevious=PlaybackControlView.class.getDeclaredField("previousButton");
            sTimebar=PlaybackControlView.class.getDeclaredField("timeBar");
            sForward=PlaybackControlView.class.getDeclaredField("fastForwardButton");
            sRewind=PlaybackControlView.class.getDeclaredField("rewindButton");
            sController.setAccessible(true);
            sNext.setAccessible(true);
            sPrevious.setAccessible(true);
            sRewind.setAccessible(true);
            sTimebar.setAccessible(true);
            sForward.setAccessible(true);
        } catch (NoSuchFieldException e) {
            Log.e("xx","-------------"+e.toString());
        }
    }

    private MDialog mdialog = null;
    private SimpleExoPlayerView mView;
    private SimpleExoPlayer mPlayer;
    private DefaultTrackSelector mTracker;

    private static String name = null;
    private H mh=new H();

    private View mTop;
    private View  draw;
    public DrawView dv;
    private ImageButton draw_cancel, draw_ok, draw_more,play_more, play_draw,finish;
    private View fastForward,rewind;
    private TimeBar timebar;

    private volatile boolean mBandSync=false;

    private boolean isController=false,firstWait=false,mLoaded=false;
    private int mAutoPause=0;
    private boolean land = true, synced = false, ended = false, running = true, startSync = false, shouldStart = false;
    private boolean seeking = true;
    private boolean performPlay=false;
    private boolean isPlay=true;
    private int width;
    private boolean sizeSet = false;
    private int w, h, sw, sh;

    private Data mData;

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    private DataSource.Factory mDataFactory;

    private Activity mActivity;

    private User.OnSeekListener seekl = null;
    private User.OnStartPauseListener spl = null;
    private User.OnScreenSizeSetListener sizeSetl = null;
    private User.OnEndSyncListener endl = null;

    public void init(View v, Data data,Activity a)
    {

        mdialog = new MDialog(a, this);

        mData=data;
        mActivity=a;
        mView= (SimpleExoPlayerView) v.findViewById(R.id.player_exo);
        mTop=v.findViewById(R.id.layout_top_pl2);

        mTracker=new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(BANDWIDTH_METER));
        mPlayer= ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(v.getContext()),mTracker);
        mView.setPlayer(mPlayer);
        mView.setControllerVisibilityListener(this);
        initSuper(this);

        mDataFactory=new DefaultDataSourceFactory(v.getContext(),
                Util.getUserAgent(v.getContext(), "Share"), BANDWIDTH_METER);
        MediaSource source=new ExtractorMediaSource(Uri.parse(data.getPlayUrlLow()),mDataFactory,new DefaultExtractorsFactory(),null,null);

        mPlayer.prepare(source);
        mPlayer.addListener(this);

        LayoutInflater lf = LayoutInflater.from(v.getContext());
        draw = lf.inflate(R.layout.drawlayout, null);
        ((ViewGroup)v).addView(draw);
        //draw.setVisibility(View.GONE);
        dv = (DrawView) draw.findViewById(R.id.draw);
        draw_cancel = (ImageButton) draw.findViewById(R.id.draw_cancel);
        draw_ok = (ImageButton) draw.findViewById(R.id.draw_ok);
        draw_more = (ImageButton) draw.findViewById(R.id.draw_more);
        finish=(ImageButton)v.findViewById(R.id.back_pl2);
        play_more = (ImageButton) v.findViewById(R.id.play_more);
        play_draw = (ImageButton)v.findViewById(R.id.play_draw);
        draw_cancel.setOnClickListener(this);
        draw_ok.setOnClickListener(this);
        draw_more.setOnClickListener(this);
        play_more.setOnClickListener(this);
        play_draw.setOnClickListener(this);
        finish.setOnClickListener(this);

        draw_more.setVisibility(View.GONE);
        setDrawVisibility(View.GONE);
        if (name == null) {
            play_draw.setVisibility(View.GONE);
            mPlayer.setPlayWhenReady(true);
        }else
        {
            mPlayer.setPlayWhenReady(false);
        }

        new Thread(this).start();
    }

    public void setDrawVisibility(int i)
    {
        draw_cancel.setVisibility(i);
        draw_ok.setVisibility(i);

        if (i==View.VISIBLE)
        {
            dv.setTouchable(true);
        }else
        {
            dv.setTouchable(false);
        }
    }

    public int getWidth()
    {
        return mView.getWidth();
    }

    public int getHeight()
    {
        return mView.getHeight();
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

    public void enableSync(String name)
    {
        if (seeking)
        {

            //mh.sendEmptyMessage(H.STARTPAUSE);
            shouldStart=true;
        }

        this.name=name;
        sizeSet=true;
        if (sizeSetl!=null)
            User.get().removeOnScreenSizeSetListener(sizeSetl);
        User.get().addOnScreenSizeSetListener(sizeSetl=new User.OnScreenSizeSetListener() {
            @Override
            public void onScreenSizeSet(String name, int w, int h) {
                sw=w;
                sh=h;
                mh.sendEmptyMessage(H.SYNC);
            }
        });
        //mh.sendEmptyMessage(SYNC);

        mh.sendEmptyMessage(H.SHOWWAIT);
        isController=true;
    }

    protected final WorkerThread worker() {
        return ((App) mActivity.getApplication()).getWorkerThread();
    }

    protected final EngineConfig config() {
        return ((App) mActivity.getApplication()).getWorkerThread().getEngineConfig();
    }

    protected final MyEngineEventHandler event() {
        return ((App) mActivity.getApplication()).getWorkerThread().eventHandler();
    }


    protected void initUIandEvent() {
        event().addEventHandler(this);

        String channelName = RemoteUtil.personal_uid+"";

        worker().joinChannel(channelName, config().mUid);

    }


    public void enableSync(int w,int h,int sw,int sh,String name,boolean sync)
    {

        initUIandEvent();
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
                    User.get().sendScreenSize(PlayerPresenter.this.w,PlayerPresenter.this.h,PlayerPresenter.this.name);
                }
            }.start();

        this.name=name;

        play_more.setVisibility(View.GONE);
        play_draw.setVisibility(View.VISIBLE);
        draw_more.setVisibility(View.GONE);

//        WindowManager.LayoutParams params = ((Activity)c).getWindow().getAttributes();
//        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        ((Activity)c).getWindow().setAttributes(params);
//        ((Activity)c).getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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

                mView.post(new Runnable() {
                    @Override
                    public void run() {

                        firstWait=true;
                        if (!mBandSync)
                        {
                            if (!mPlayer.getPlayWhenReady()||Math.abs(position-mPlayer.getCurrentPosition())>1000)
                            {
                                try {
                                    mPlayer.seekTo(position);
                                    if (!mPlayer.getPlayWhenReady())
                                    {
                                        mPlayer.setPlayWhenReady(true);
                                        mAutoPause++;
                                    }
                                }catch (Exception e)
                                {

                                }
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
//                if (shouldStart)
//                {
//                    shouldStart=false;
//                    new Thread()
//                    {
//                        @Override
//                        public void run() {
//                            super.run();
//                            //User.get().startpause(MPlayer.this.name);
//                        }
//                    }.start();
//                }

                mh.sendEmptyMessage(H.STARTPAUSE);
            }
        });
//        if (mPlayer.getPlayWhenReady())
//        {
//            new Thread()
//            {
//                @Override
//                public void run() {
//                    super.run();
//                    User.get().startpause(PlayerPresenter.name);
//                }
//            }.start();
//        }
    }

    private void endSync()
    {
        ended=true;
        ShareService.endSync();
        if (!isController)
            mActivity.finish();
        else {
            removeListener();
            play_more.setVisibility(View.VISIBLE);
            play_draw.setVisibility(View.GONE);
            //draw.setVisibility(View.GONE);
            setDrawVisibility(View.GONE);
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
        worker().leaveChannel("1");
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

    public void release()
    {
        mPlayer.release();
    }

    public void onPause()
    {
        mPlayer.setPlayWhenReady(false);
    }

    public void onResume()
    {
        //mPlayer.setPlayWhenReady(true);
    }


    public String getSyncName()
    {
        return name;
    }

    public String getSource()
    {
        return mData.getPlayUrlLow();
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

    @Override
    public void onVisibilityChange(int i) {
        mTop.setVisibility(i);
    }

    @Override
    public void onClick(View v) {
        if (v==fastForward||v==rewind)
        {
            mBandSync=true;
        }

        switch (v.getId())
        {
            case R.id.back_pl2:
                mActivity.finish();
                break;
            case R.id.draw_cancel:
                dv.clear();
                mView.postInvalidate();
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
                setDrawVisibility(View.GONE);
                //draw_cancel.setVisibility(View.GONE);
                //draw_ok.setVisibility(View.GONE);
                play_draw.setVisibility(View.VISIBLE);
                //play_more.setVisibility(View.VISIBLE);
                break;
            case R.id.draw_more:
                break;
            case R.id.back:
                mActivity.finish();
                break;
            case R.id.play_more:
                if (User.get()==null)
                {
                    Toast.makeText(mActivity, "请先登录以使用此功能", Toast.LENGTH_SHORT).show();
                }else
                    mdialog.show();
                break;
            case R.id.play_draw:
                play_draw.setVisibility(View.GONE);
                //draw.setVisibility(View.VISIBLE);
                setDrawVisibility(View.VISIBLE);
                play_more.setVisibility(View.GONE);
                //draw_cancel.setVisibility(View.VISIBLE);
                //draw_ok.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }

    @Override
    public void run() {
        while(running)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (seeking) {
                mh.sendEmptyMessage(H.SEEK);
                if (startSync)
                    if (isController)
                        if (name!=null)
                        {
                            if (mPlayer.getPlayWhenReady())
                            {
                                User.get().syncSeek((int)mPlayer.getCurrentPosition(),name);
//                                mView.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                    }
//                                });
                            }
                        }

            }
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object o) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {

    }

    @Override
    public void onLoadingChanged(boolean b) {

    }

    @Override
    public void onPlayerStateChanged(final boolean b, int i) {
        if (mAutoPause!=0)
        {
            mAutoPause--;
            return;
        }

        if (synced)
        {
            if (isController)
            {
                //控制方
                if (!firstWait)
                {
                    //被控制方未缓冲完成，
                    if (i==Player.STATE_READY)
                    {
                        //如果加载完成时暂停等待
                        if (mPlayer.getPlayWhenReady())
                        {
                            mPlayer.setPlayWhenReady(false);
                        }

                        //缓存好通知对方
                        if (!mLoaded)
                        {
                            mLoaded=true;
                            new Thread()
                            {
                                @Override
                                public void run() {
                                    super.run();
                                    User.get().syncSeek(0,name);
                                }
                            }.start();
                        }
                    }
                }else
                {
                    //被控制方缓存完成
                    if (i==Player.STATE_READY)
                    {
                        if(!mLoaded)
                        {
                            mLoaded=true;
                            //初次加载成功，直接播放
                            if (!mPlayer.getPlayWhenReady())
                            {
                                //开始播放
                                mPlayer.setPlayWhenReady(true);
                            }
                        }else
                        {
                            if (!b)
                            {
                                //暂停
                                new Thread(){
                                    @Override
                                    public void run() {
                                        super.run();
                                        User.get().startpause(name);
                                    }
                                }.start();
                            }else
                            {
                                //播放
                                new Thread()
                                {
                                    @Override
                                    public void run() {
                                        super.run();
                                        User.get().syncSeek((int)mPlayer.getCurrentPosition(),name);
                                    }
                                }.start();
                            }
                        }
                    }
                }
            }else
            {
                //被控制方
                if (!firstWait)
                {
                    //对方未缓冲完成
                    if (i==Player.STATE_READY)
                    {
                        //告诉对方缓冲好了，暂停播放
                        new Thread()
                        {
                            @Override
                            public void run() {
                                super.run();
                                User.get().syncSeek(0,name);
                            }
                        }.start();
                        if (mPlayer.getPlayWhenReady())
                        {
                            mPlayer.setPlayWhenReady(false);
                        }

                        if (!mLoaded)
                        {
                            mLoaded=true;
                            new Thread()
                            {
                                @Override
                                public void run() {
                                    super.run();
                                    User.get().syncSeek(0,name);
                                }
                            }.start();
                        }
                    }
                }else
                {
                    //对方已经缓存完成
                    if (i==Player.STATE_READY)
                    {
                        if (!mLoaded)
                        {
                            //刚刚缓存好
                            //告诉对方缓冲好了,开始播放
                            mLoaded=true;
                            new Thread()
                            {
                                @Override
                                public void run() {
                                    super.run();
                                    User.get().syncSeek(0,name);
                                }
                            }.start();
                        }else
                        {
                            if (!b)
                            {
                                //暂停
                                new Thread(){
                                    @Override
                                    public void run() {
                                        super.run();
                                        User.get().startpause(name);
                                    }
                                }.start();
                            }else
                            {
                                //播放
                                new Thread()
                                {
                                    @Override
                                    public void run() {
                                        super.run();
                                        User.get().syncSeek((int)mPlayer.getCurrentPosition(),name);
                                    }
                                }.start();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onRepeatModeChanged(int i) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean b) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {

    }

    @Override
    public void onPositionDiscontinuity(int i) {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {
        if (mBandSync)
        {
            mBandSync=false;

            if (synced)
                new Thread()
                {
                    @Override
                    public void run() {
                        super.run();
                        User.get().syncSeek((int)mPlayer.getCurrentPosition(),name);
                    }
                }.start();
        }
    }

    @Override
    public void onScrubStart(TimeBar timeBar, long l) {

    }

    @Override
    public void onScrubMove(TimeBar timeBar, long l) {

    }

    @Override
    public void onScrubStop(TimeBar timeBar, long l, boolean b) {
        mBandSync=true;
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
                    enableSync(mView.getWidth(),mView.getHeight(),sw,sh,name,true);
                    break;
                case END:
                    endSync();
                    break;
                case STARTPAUSE:

                    MDialog.dismissWait();
                    performPlay=true;
                    if (mPlayer.getPlayWhenReady())
                    {
                        mAutoPause++;
                    }
                    mPlayer.setPlayWhenReady(false);
                    //firstWait=true;
                    //mPlayer.stop();
                    break;
                case CANCELHUANCHONG:
                    MDialog.dismissHuanchong();
                    break;
                case CANCELWAIT:
                    MDialog.dismissWait();
                    break;
                case SHOWWAIT:
                    MDialog.showWait(mActivity, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            mActivity.finish();
                        }
                    });
                    break;
            }

        }
    }

    private static void initSuper(PlayerPresenter pp)
    {
        try {
            Object controller= sController.get(pp.mView);
            ((View)sNext.get(controller)).setLayoutParams(new LinearLayout.LayoutParams(0,0));
            ((View)sPrevious.get(controller)).setLayoutParams(new LinearLayout.LayoutParams(0,0));

            pp.fastForward=(View)sForward.get(controller);
            pp.rewind=(View)sRewind.get(controller);
            pp.timebar=(TimeBar) sTimebar.get(controller);

            pp.fastForward.setOnClickListener(new ClickListenerStub(pp,Rf.getOnClickListener(pp.fastForward)));
            pp.rewind.setOnClickListener(new ClickListenerStub(pp,Rf.getOnClickListener(pp.rewind)));
            pp.timebar.addListener(pp);
        } catch (IllegalAccessException e) {
            Log.e("xx","----------"+e.toString());
        }
    }

    private static class ClickListenerStub implements View.OnClickListener
    {
        View.OnClickListener before,after;
        ClickListenerStub(View.OnClickListener b, View.OnClickListener a)
        {
            before=b;
            after=a;
        }

        @Override
        public void onClick(View v) {
            before.onClick(v);
            after.onClick(v);
        }
    }
}
