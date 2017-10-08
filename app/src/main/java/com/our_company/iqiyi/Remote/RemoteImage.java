package com.our_company.iqiyi.Remote;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by miaojie on 2017/5/2.
 */

public class RemoteImage extends ImageView {
    private String TAG = "远程桌面";

    public RemoteImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Socket clientSocket = RemoteUtil.socketClient;
    private boolean canTouch;

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        MasterSendCmdThread masterSendCmdThread = null;
        if(!RemoteUtil.canTouch)
        {
            Log.e("不能点",123+"");
            return true;
        }
        Log.e("不能点",456+"");
        clientSocket = RemoteUtil.socketClient;

        float height = getMeasuredHeight();
        float width = getMeasuredWidth();

        switch (event.getAction()) {
            case MotionEvent.ACTION_BUTTON_PRESS:

                masterSendCmdThread = new MasterSendCmdThread(event.getX() * 1.0 / width, event.getY() * 1.0 / height, clientSocket, MotionEvent.ACTION_BUTTON_PRESS);
                masterSendCmdThread.start();
                Log.e(TAG, "ACTION_BUTTON_PRESS");
                break;
            case MotionEvent.ACTION_BUTTON_RELEASE:
                masterSendCmdThread = new MasterSendCmdThread(event.getX() * 1.0 / width, event.getY() * 1.0 / height, clientSocket, MotionEvent.ACTION_BUTTON_RELEASE);
                masterSendCmdThread.start();
                Log.e(TAG, "ACTION_BUTTON_RELEASE");
                break;
            case MotionEvent.ACTION_CANCEL:
                masterSendCmdThread = new MasterSendCmdThread(event.getX() * 1.0 / width, event.getY() * 1.0 / height, clientSocket, MotionEvent.ACTION_CANCEL);
                masterSendCmdThread.start();
                Log.e(TAG, "ACTION_CANCEL");
                break;

            case MotionEvent.ACTION_DOWN:
                masterSendCmdThread = new MasterSendCmdThread(event.getX() * 1.0 / width, event.getY() * 1.0 / height, clientSocket, MotionEvent.ACTION_DOWN);
                masterSendCmdThread.start();
                Log.e(TAG, "ACTION_DOWN-----" + event.getX() + "Y----" + event.getY());
                break;
            case MotionEvent.ACTION_HOVER_ENTER:
                masterSendCmdThread = new MasterSendCmdThread(event.getX() * 1.0 / width, event.getY() * 1.0 / height, clientSocket, MotionEvent.ACTION_HOVER_ENTER);
                masterSendCmdThread.start();
                Log.e(TAG, "ACTION_HOVER_ENTER");
                break;
            case MotionEvent.ACTION_HOVER_EXIT:
                masterSendCmdThread = new MasterSendCmdThread(event.getX() * 1.0 / width, event.getY() * 1.0 / height, clientSocket, MotionEvent.ACTION_HOVER_EXIT);
                masterSendCmdThread.start();
                Log.e(TAG, "ACTION_HOVER_EXIT");
                break;
            case MotionEvent.ACTION_UP:
                masterSendCmdThread = new MasterSendCmdThread(event.getX() * 1.0 / width, event.getY() * 1.0 / height, clientSocket, MotionEvent.ACTION_UP);
                masterSendCmdThread.start();
                Log.e(TAG, "ACTION_UP---" + event.getX() + "Y----" + event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                masterSendCmdThread = new MasterSendCmdThread(event.getX() * 1.0 / width, event.getY() * 1.0 / height, clientSocket, MotionEvent.ACTION_MOVE);
                masterSendCmdThread.start();
                Log.e(TAG, "ACTION_MOVE---" + event.getX() + "Y----" + event.getY() + "Height---" + height + "---" + getX() / width * 1.0);
                break;
        }

        return true;
    }

    public void setCanTouch(boolean canTouch) {
        this.canTouch = canTouch;
    }
}
