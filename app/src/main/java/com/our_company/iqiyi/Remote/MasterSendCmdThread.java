package com.our_company.iqiyi.Remote;

import android.util.Log;
import android.view.MotionEvent;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by miaojie on 2017/5/5.
 */

public class MasterSendCmdThread extends Thread {
    private MotionEvent event;
    private Socket clientSocket;
    private int coordType;
    private double scaleX,scaleY;
    String cmd="";
    public MasterSendCmdThread(double scaleX ,double scaleY,Socket clientSocket,int coordType)
    {
        this.scaleX=scaleX;
        this.scaleY=scaleY;
        this.clientSocket=clientSocket;
        this.coordType=coordType;
    }
    @Override
    public void run() {
        ObjectOutputStream objectOutputStream= null;
        try {
            RemoteUtil.cmdNum++;

            cmd=coordType+","+scaleX+","+scaleY;
            OutputStream outputStream=clientSocket.getOutputStream();

//          BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(outputStream);
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(cmd);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            if(!RemoteUtil.canShot&&RemoteService.masterThread!=null)
                if(!RemoteService.masterThread.isAlive())
                {
                    Log.e("重启","123");
                    RemoteService.masterThread=new MasterThread(RemoteUtil.port,MasterActivity.handler);
                    RemoteService.masterThread.start();

                }
//            outputStream.write();
//            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
//            RemoteInfo remoteInfo=new RemoteInfo();
//            remoteInfo.setType(RemoteUtil.POST_TYPE_MASTER);
//            remoteInfo.setCoordType(coordType);
//            remoteInfo.setCoordX(event.getX());
//            remoteInfo.setCoordY(event.getY());
//            objectOutputStream.writeObject(remoteInfo);
//            objectOutputStream.flush();
            RemoteUtil.cmdNum--;

            Log.e("receivceCmd",cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
