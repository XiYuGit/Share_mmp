package com.our_company.iqiyi.Remote;

//import android.graphics.Bitmap;
//import android.os.Handler;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//
//import java.io.BufferedWriter;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.net.Socket;
//
///**
// * Created by miaojie on 2017/5/3.
// */
//
//public class ClientThread extends Thread {
//    private Handler handler;
//    private String ipAdress;
//    private Window clientWindow;
//    private Bitmap clientScreen;
//    private boolean isConnect;
//    private int port;
//
//    public ClientThread(String ipAdress,int port)
//    {
//
//        this.ipAdress=ipAdress;
//        this.port=port;
//        try {
//            RemoteUtil.socketClient=new Socket(ipAdress,port);
//            Log.e("ClientThread","创建");
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e("ClientThread",e.toString());
//        }
//        Log.e("ClientThread","创建");
//    }
//    public Handler getHandler() {
//        return handler;
//    }
//
//    public void setHandler(Handler handler) {
//        this.handler = handler;
//    }
//
//    public String getIpAdress() {
//        return ipAdress;
//    }
//
//    public void setIpAdress(String ipAdress) {
//        this.ipAdress = ipAdress;
//    }
//
//    public Window getClientWindow() {
//        return clientWindow;
//    }
//
//    public void setClientWindow(Window clientWindow) {
//        this.clientWindow = clientWindow;
//    }
//
//    public Bitmap getClientScreen() {
//        return clientScreen;
//    }
//
//    public void setClientScreen(Bitmap clientScreen) {
//        this.clientScreen = clientScreen;
//    }
//
//    @Override
//    public void run() {
//        super.run();
//        Log.e("ClientThread","1111");
//        while( RemoteUtil.socketClient.isConnected())
//        {
//          //  Log.e("ClientThread","发送");
////            Log.e("ClientThread",(RemoteUtil.clientWindow==null)+"11");
////            if(RemoteUtil.clientWindow==null)
////                continue;
////            View view=RemoteUtil.clientWindow.getDecorView().getRootView();
////            view.destroyDrawingCache();
////            view.setDrawingCacheEnabled(true);
//          //  Log.e("asd",(view.getDrawingCache()==null)+"");
//
//
//            try {
//                if (RemoteUtil.bitmap==null)
//                    continue;
//                clientScreen= RemoteUtil.comp(RemoteUtil.bitmap);
//                ByteArrayOutputStream temp=new ByteArrayOutputStream();
//                clientScreen.compress(Bitmap.CompressFormat.PNG,100,temp);
//                clientScreen.recycle();
//                RemoteInfo info=new RemoteInfo();
//                info.setRemoteDesktop(temp.toByteArray());
//                info.setType(RemoteUtil.POST_TYPE_CLIENT);
//                ObjectOutputStream objectOutputStream=null;
//                Log.e("ClientThread","发送1");
//                Thread.sleep(60);
////                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(RemoteUtil.socketClient.getOutputStream()));
////                String pic=new String(temp.toByteArray());
////                bufferedWriter.write(pic);
////                bufferedWriter.newLine();
////                bufferedWriter.flush();
////                RemoteUtil.socketClient.getOutputStream().write(temp.toByteArray());
////
////                RemoteUtil.socketClient.getOutputStream().flush();
////                Log.e("ClientThread","发送1"+temp.toByteArray().length);
//                 objectOutputStream=new ObjectOutputStream( RemoteUtil.socketClient.getOutputStream());
//                objectOutputStream.writeObject(info);
//                objectOutputStream.flush();
//                Log.e("ClientThread","发送");
////                OutputStream outputStream=socketClient.getOutputStream();
////                outputStream.write("asd".getBytes());
////                outputStream.flush();
//            } catch (IOException e) {
//                Log.e("出错了", "asd");
//            } catch (Exception e) {
//                Log.e("出错了","qwe");
//            }
//            Log.e("ClientThread","发送2");
//        }
//    }
//}

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by miaojie on 2017/5/3.
 */

public class ClientThread extends Thread{
    private Handler handler;
    private String ipAdress;
    private Window clientWindow;
    private Bitmap clientScreen;
    private boolean isConnect;
    private int port;

    public ClientThread(String ipAdress,int port)
    {

        this.ipAdress=ipAdress;
        this.port=port;
        try {
            if(RemoteUtil.socketClient==null)
                RemoteUtil.socketClient=new Socket(RemoteUtil.ipAdress,RemoteUtil.port);
            RemoteUtil.isConnect=true;
            Log.e("ClientThread","创建");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ClientThread",e.toString());
        }
        Log.e("ClientThread","创建");
    }
    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public String getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }

    public Window getClientWindow() {
        return clientWindow;
    }

    public void setClientWindow(Window clientWindow) {
        this.clientWindow = clientWindow;
    }

    public Bitmap getClientScreen() {
        return clientScreen;
    }

    public void setClientScreen(Bitmap clientScreen) {
        this.clientScreen = clientScreen;
    }


    public void run() {

        Log.e("ClientThread","1111");
            //  Log.e("ClientThread","发送");
//            Log.e("ClientThread",(RemoteUtil.clientWindow==null)+"11");
//            if(RemoteUtil.clientWindow==null)
//                continue;
//            View view=RemoteUtil.clientWindow.getDecorView().getRootView();
//            view.destroyDrawingCache();
//            view.setDrawingCacheEnabled(true);
            //  Log.e("asd",(view.getDrawingCache()==null)+"");
        RemoteUtil.canShot=true;
            try {
                while( RemoteUtil.socketClient.isConnected())
                {

                    if(RemoteUtil.clientWindow==null)
                        continue;
                    View view=RemoteUtil.clientWindow.getDecorView().getRootView();
                    view.destroyDrawingCache();
                    view.setDrawingCacheEnabled(true);
                    //  Log.e("asd",(view.getDrawingCache()==null)+"");


                    try {
                        clientScreen= RemoteUtil.comp(view.getDrawingCache());
                        ByteArrayOutputStream temp=new ByteArrayOutputStream();
                        clientScreen.compress(Bitmap.CompressFormat.PNG,100,temp);
                        clientScreen.recycle();
                        RemoteInfo info=new RemoteInfo();
                        info.setRemoteDesktop(temp.toByteArray());
                        info.setType(RemoteUtil.POST_TYPE_CLIENT);
                        ObjectOutputStream objectOutputStream=null;

                        Thread.sleep(60);
                        objectOutputStream=new ObjectOutputStream( RemoteUtil.socketClient.getOutputStream());

                        objectOutputStream.writeObject(info);
                        objectOutputStream.flush();

                    } catch (IOException e) {
                        Log.e("出错了", "asd");

                    } catch (Exception e) {

                        Log.e("出错了",e.toString());
                    }
                    if(RemoteUtil.socketClient.isClosed())
                    {
                        Log.e("是否连接","123");
                        break;
                    }
                    Log.e("ClientThread","发送2");
                }
                Log.e("确实控制结束","456");
            }catch (NullPointerException  e)
            {
                Log.e("确实控制结束","123");
            }
            RemoteUtil.canShot=false;

        }
    }


