package com.our_company.iqiyi.Remote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import xiyou.mobile.BridgeNative;

import static com.our_company.iqiyi.Remote.RemoteUtil.socketMaster;

/**
 * Created by miaojie on 2017/5/3.
 */

public class MasterThread extends Thread {

    private Handler handler;
    private int port;
    public MasterThread(int port,Handler handler) throws IOException {
        this.handler=handler;
        this.port=port;
        if(socketMaster==null)
          socketMaster=new ServerSocket(4396);
    }

    @Override
    public void run() {
        super.run();
        try {
            Log.e("MasterThread","等待");
            Message msg=handler.obtainMessage();
            msg.what=4396;
            handler.sendMessageDelayed(msg,2000);

            if(RemoteUtil.socketClient==null)
                 RemoteUtil.socketClient=socketMaster.accept();
            handler.removeMessages(4396);
            Log.e("MasterThread","链接");
            while( RemoteUtil.socketClient.isConnected())
            {
                if(RemoteUtil.socketClient.isClosed())
                {
                    Log.e("是否连接","123");
                    break;
                }
//                Log.e("MasterThread","传输"+socketClient.isInputShutdown());
                InputStream inputStream=RemoteUtil.socketClient.getInputStream();
                ObjectInputStream objectInputStream=new ObjectInputStream(inputStream);

                RemoteUtil.canTouch=true;

////                byte[]bytes=new byte[1000];
////                int num=inputStream.read(bytes);
////                Log.e("MasterThread",new String(bytes,0,num));
////               socketClient.getInputStream();
//                Log.e("MasterThread","传输");
                RemoteInfo remoteInfo= (RemoteInfo) objectInputStream.readObject();
                if(remoteInfo==null)
                {
                    Log.e("结束了","return");
                    return;
                }
//                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
//                String pic=bufferedReader.readLine();
//                int num;
//                byte[]bytes=new byte[1000000];
//                num=inputStream.read(bytes);
//                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,num);
                Message message=new Message();
                message.what=1;
                message.obj=remoteInfo;
                handler.sendMessage(message);
//                Log.e("MaskuterThread",num+"");


            }
        } catch (IOException e) {
            e.printStackTrace();

            Log.e("结束了",e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("结束了",e.toString());
            e.printStackTrace();
        }
        Log.e("结束了","123");
//        RemoteUtil.canTouch=false;


    }
}
