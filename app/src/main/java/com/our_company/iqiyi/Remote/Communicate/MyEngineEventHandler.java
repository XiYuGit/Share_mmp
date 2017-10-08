package com.our_company.iqiyi.Remote.Communicate;

import android.content.Context;
import android.util.Log;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

/**
 * Created by miaojie on 2017/6/18.
 */

public class MyEngineEventHandler {
    public MyEngineEventHandler(Context ctx, EngineConfig config) {
        this.mContext = ctx;
        this.mConfig = config;
    }

    private final EngineConfig mConfig;

    private final Context mContext;

    private final ConcurrentHashMap<AGEventHandler, Integer> mEventHandlerList = new ConcurrentHashMap<>();

    public void addEventHandler(AGEventHandler handler) {
        this.mEventHandlerList.put(handler, 0);
    }

    public void removeEventHandler(AGEventHandler handler) {
        this.mEventHandlerList.remove(handler);
    }

    public final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

        @Override
        public void onUserJoined(int uid, int elapsed) {
        }

        @Override
        public void onUserOffline(int uid, int reason) {

            // FIXME this callback may return times
            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onUserOffline(uid, reason);
            }
        }

        @Override
        public void onRtcStats(RtcStats stats) {
        }

        @Override
        public void onAudioVolumeIndication(AudioVolumeInfo[] speakerInfos, int totalVolume) {
            if (speakerInfos == null) {
                // quick and dirty fix for crash
                // TODO should reset UI for no sound
                return;
            }

            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS, (Object) speakerInfos);
            }
        }

        @Override
        public void onLeaveChannel(RtcStats stats) {

        }

        @Override
        public void onLastmileQuality(int quality) {
        }

        @Override
        public void onError(int error) {

            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR, error, RtcEngine.getErrorDescription(error));
            }
        }

        @Override
        public void onAudioQuality(int uid, int quality, short delay, short lost) {
            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_AUDIO_QUALITY, uid, quality, delay, lost);
            }
        }

        @Override
        public void onConnectionLost() {

            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_APP_ERROR, ConstantApp.AppError.NO_NETWORK_CONNECTION);
            }
        }

        @Override
        public void onConnectionInterrupted() {

            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_APP_ERROR, ConstantApp.AppError.NO_NETWORK_CONNECTION);
            }
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            Log.e("成功","123");
            mConfig.mUid = uid;

            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                Log.e("成功","123");
                AGEventHandler handler = it.next();
                handler.onJoinChannelSuccess(channel, uid, elapsed);
            }
        }

        public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
        }

        public void onWarning(int warn) {
        }

        @Override
        public void onAudioRouteChanged(int routing) {

            Iterator<AGEventHandler> it = mEventHandlerList.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED, routing);
            }

        }
    };

}