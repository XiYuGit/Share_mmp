package com.our_company.iqiyi.Remote;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

import com.our_company.iqiyi.Util.LoginUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.SocketException;

/**
 * Created by miaojie on 2017/5/5.
 */

public class ClientReceiveCmdThread extends Thread {
    String cmd;
    public static int timeCount=0;

    @Override
    public void run() {

            if(RemoteUtil.socketClient!=null)
            while( RemoteUtil.socketClient.isConnected())
            {
                Log.e("是否连接",RemoteUtil.socketClient.isConnected()+"");
                try {
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(RemoteUtil.socketClient.getInputStream()));
//                    Log.e("")
                   cmd= bufferedReader.readLine();
                    if(cmd==null)
                    {
                        RemoteUtil.isConnect=false;
                        Log.e("结束","123");
                        break;
                    }
                   String[]cmds= cmd.split(",");
                    Log.e("ClientReceiveCmdThread",cmd);
                    Intent intent=new Intent("ASDFG");
                    intent.putExtra("control","startReciveCmd");
                    RemoteUtil.maxContext.sendBroadcast(intent);
                    receivceCmd(RemoteUtil.instrumentation,Integer.parseInt(cmds[0]),Double.parseDouble(cmds[1]),Double.parseDouble(cmds[2]));

//                    if(RemoteUtil.handler.postDelayed(runnable,2000))
//                        Log.e("postdelay","成功");
//                    ObjectInputStream objectInputStream=new ObjectInputStream( RemoteUtil.socketClient.getInputStream());
//                    RemoteInfo remoteInfo= (RemoteInfo) objectInputStream.readObject();
//                    receivceCmd(RemoteUtil.instrumentation,remoteInfo.getCoordType(),remoteInfo.getCoordX(),remoteInfo.getCoordY());
                    Log.e("ClientReceiveCmdThread","执行成功");
                } catch (IOException e) {
                    e.printStackTrace();
                    LoginUtil.mainActivity.stopService(new Intent(LoginUtil.mainActivity,RemoteService.class));
                    return;
                } catch (Exception e)
                {
                    Log.e("ClientReceiveCmdThread","他妈的大异常"+e.toString());
                }
                if(RemoteUtil.socketClient.isClosed())
                {
                    Log.e("是否连接","123");
                    break;
                }
            }
        Log.e("结束了","remote");

        LoginUtil.mainActivity.stopService(new Intent(LoginUtil.mainActivity,RemoteService.class));

    }

    public static void receivceCmd(Instrumentation instrumentation,int type,double x,double y)
    {
        Log.e("尺寸：",RemoteUtil.screenWidth+"-----"+RemoteUtil.screenHeight);

        Log.e("receivceCmd","type:"+type+"--x："+x+"--y:"+y+"--RemoteUtil.socketClient.getInputStream()"+RemoteUtil.screenWidth);

        if(type==0x123)
        {
            return;
        }
        if(type==0x456)
        {
            RemoteUtil.masterActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        x=x*RemoteUtil.screenWidth;
        y=y*RemoteUtil.screenHeight;
        RemoteUtil.theLastX=x;
        RemoteUtil.theLastY=y;
        instrumentation.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(),type,(float) x,(float) y,0));
    }
}
