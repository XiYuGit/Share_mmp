package com.our_company.iqiyi.Player.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import xiyou.mobile.BridgeNative;
import xiyou.mobile.User;

/**
 * Created by user on 2017/5/31.
 */

public class DrawView extends View{

    private ArrayList<Path> paths=new ArrayList<>(),rpaths=new ArrayList<>();
    private Paint p=new Paint();
    private int color=Color.BLUE;
    private float x,y;
    private String name=null;
    private int sw, sh,w,h;
    private boolean synced=false;

    private User.OnActionListener actionl;
    private User.OnClearScreenListener clearl;

    public DrawView(Context c)
    {
        super(c);
        init();
    }


    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setColor(int c)
    {
        color=c;
        p.setColor(c);
    }

    public void removeAllListener()
    {
        if (actionl!=null)
        {
            User.get().removeOnActionListener(actionl);
        }

        if (clearl!=null)
            User.get().removeOnClearScreenListener(clearl);
    }

    public void enableSync(String name)
    {
        this.name=name;
    }

    public void enableSync(int w,int h,int sw,int sh,String name)
    {
        this.name=name;
        synced=true;
        this.w=w;
        this.h=h;
        this.sw=sw;
        this.sh=sh;
        new Thread(){
            @Override
            public void run() {
                super.run();

                if (User.get()==null)
                    return ;
                if (actionl!=null)
                    User.get().removeOnActionListener(actionl);
                User.get().addOnActionListener(actionl=new User.OnActionListener() {
                    @Override
                    public void onAction(int action, int x, int y) {
                        //Log.e("xx","action"+(float)x/DrawView.this.sw*DrawView.this.w+":"+(float)y/DrawView.this.sh*DrawView.this.h);
                        switch (action)
                        {
                            case BridgeNative.ACTION_DOWN:

                                rpaths.add(new Path());
                                rpaths.get(rpaths.size()-1).reset();;
                                rpaths.get(rpaths.size()-1).moveTo((float)x/DrawView.this.sw*DrawView.this.w,(float)y/DrawView.this.sh*DrawView.this.h);
                                break;
                            case BridgeNative.ACTION_MOVE:
                                if (rpaths.size()!=0){
                                    rpaths.get(rpaths.size()-1).lineTo((float)x/DrawView.this.sw*DrawView.this.w,(float)y/DrawView.this.sh*DrawView.this.h);
                                    //paths.get(paths.size()-1).moveTo(x,y);
                                }
                                break;
                        }
                        postInvalidate();
                    }
                });

                if (clearl!=null)
                    User.get().removeOnClearScreenListener(clearl);
                User.get().addOnClearScreenListener(clearl=new User.OnClearScreenListener() {
                    @Override
                    public void onClear() {
                        clear();
                        //postInvalidate();
                    }
                });
            }
        }.start();
    }

    private void init()
    {
        setClickable(true);

        setBackgroundColor(Color.TRANSPARENT);
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        p.setColor(Color.BLUE);
    }

    public void clear()
    {
        rpaths.clear();
        paths.clear();
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);


        for (int i=0;i<paths.size();i++)
           c.drawPath(paths.get(i),p);

        for (int i=0;i<rpaths.size();i++)
            c.drawPath(rpaths.get(i),p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        //if (synced)
        //    return false;
        switch (motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                paths.add(new Path());
                paths.get(paths.size()-1).reset();;
                paths.get(paths.size()-1).moveTo(motionEvent.getX(),motionEvent.getY());
                x=motionEvent.getX();
                y=motionEvent.getY();

                if (name!=null)
                    new SendThread(BridgeNative.ACTION_DOWN,(int)x,(int)y,name).start();
                return true;
            case MotionEvent.ACTION_UP:
                return true;
            case MotionEvent.ACTION_MOVE:
                if (paths.size()!=0){
                    paths.get(paths.size()-1).lineTo(motionEvent.getX(),motionEvent.getY());
                    //paths.get(paths.size()-1).moveTo(x,y);
                }

                if (name!=null)
                    new SendThread(BridgeNative.ACTION_MOVE,(int)motionEvent.getX(),(int)motionEvent.getY(),name).start();
                break;
            default:

                break;
        }

        invalidate();
        return false;
    }

    class SendThread extends Thread
    {
        int action,x,y;
        String name;
        public SendThread(int action,int x,int y,String name)
        {
            this.x=x;
            this.y=y;
            this.action=action;
            this.name=name;
        }

        @Override
        public void run() {
            super.run();
            User.get().sendAction(action,x,y,name);
            //Log.e("xx","send"+x+":"+y);
        }
    }
}
