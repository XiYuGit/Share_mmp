package com.our_company.iqiyi.Player;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.our_company.iqiyi.R;

import java.util.ArrayList;

import xiyou.mobile.User;

/**
 * Created by user on 2017/6/14.
 */

public class MDialog extends Dialog {

    private PlayerPresenter cv;
    private ViewPager vp;
    private TextView title1,title2;
    private static User.OnPermitSyncListener syncl=null;
    private static User.OnRefusedSyncListener rsyncl=null;

    private static AlertDialog defaultD,huanchongD,waitD;

    private H mh=new H();

    public MDialog(Context context, PlayerPresenter v) {
        super(context, R.style.MDialog);
        this.cv=v;
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.width=((Activity)context).getWindowManager().getDefaultDisplay().getWidth()/5*2;
        lp.height=((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
        lp.gravity= Gravity.RIGHT;
        getWindow().setAttributes(lp);

    }

    public void removeAllListener()
    {
        if (syncl!=null)
            User.get().removeOnPermitSyncListener(syncl);

        if (rsyncl!=null)
            User.get().removeOnRefusedSyncListener(rsyncl);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //removeAllListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (User.get()!=null) {
            removeAllListener();
            User.get().addOnPermitSyncListener(syncl = new User.OnPermitSyncListener() {
                @Override
                public void onPermitSync(String name) {
                    MDialog.this.cv.enableSync(name);
                    User.get().sendScreenSize(cv.getWidth(), cv.getHeight(), name);
                    Log.e("sendData", "" + cv.getSource());
                    User.get().sendPlayData(cv.getSource(), name);
                }
            });

            User.get().addOnRefusedSyncListener(rsyncl = new User.OnRefusedSyncListener() {
                @Override
                public void onRefused(String name) {
                    mh.sendEmptyMessage(0);
                }
            });
        }
        new Thread()
        {
            @Override
            public void run() {

            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_more);


        vp=(ViewPager) findViewById(R.id.more_pager);
        vp.setAdapter(new A());
        title1=(TextView)findViewById(R.id.title1) ;
        title1.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
        //title2=(TextView)findViewById(R.id.title2) ;
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position)
                {
                    case 0:
                        title1.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                        //title2.setTextColor(Color.WHITE);
                        break;
                    case 1:
                        //title2.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                        title1.setTextColor(Color.WHITE);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class SyncThread extends Thread
    {
        private String name;
        public SyncThread(String name)
        {
            this.name=name;
        }

        @Override
        public void run() {
            super.run();
            if (cv.getSyncName()!=null)
                User.get().endSync(cv.getSyncName());

            User.get().requestSync(name);
        }
    }

    class A extends PagerAdapter
    {
        private ArrayList<View> views=new ArrayList<>();
        private View list,please_login;

        public A()
        {
            please_login=getLayoutInflater().inflate(R.layout.pleaselogin,null,false);
            list=getLayoutInflater().inflate(R.layout.friends,null,false);
            views.add(list);
            final ListView friends_list=(ListView)list.findViewById(R.id.friends_list);

            friends_list.setAdapter(new LA());

            friends_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    new SyncThread(User.get().friends.get(position).getName()).start();
                }
            });
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position),position);
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.remove(position));
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        public void notifyLogin()
        {
            views.remove(0);
            views.add(0,please_login);
            notifyDataSetChanged();
        }

        public void notifyLoged()
        {
            views.remove(0);
            views.add(0,list);
            notifyDataSetChanged();
        }


        class LA extends BaseAdapter
        {

            @Override
            public int getCount() {
                if (User.get()==null)
                {
                    notifyLogin();
                    return 0;
                }

                return User.get().friends.size();
            }

            @Override
            public Object getItem(int position) {
                return User.get().friends.get(position).getName();
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v= getLayoutInflater().inflate(R.layout.text_item,null,false);
                //v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300));
                if (User.get()==null) {
                    notifyLogin();
                    return v;
                }
                else
                {

                }

                TextView t=(TextView)v.findViewById(R.id.text_item);
                if (User.get().friends.get(position).isOnline())
                {
                    t.setTextColor(Color.GREEN);
                }else
                    t.setTextColor(Color.LTGRAY);
                t.setText(User.get().friends.get(position).getName());

                return v;
            }
        }
    }

    public static void showDefault(Context c, String title, String msg, OnClickListener permit, OnClickListener refuse)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(c);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("确认",permit);
        builder.setNegativeButton("拒绝",refuse);
        builder.show();
    }

    public static void showHuanchong(Context c,OnCancelListener cancel)
    {
        if (huanchongD!=null)
            huanchongD.dismiss();

        AlertDialog.Builder builder=new AlertDialog.Builder(c);
        builder.setTitle("请稍后");
        builder.setMessage("正在缓冲。。。。");
        builder.setPositiveButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancelHuanchong();
            }
        });
        builder.setOnCancelListener(cancel);
        builder.setCancelable(false);
        huanchongD=builder.create();
        huanchongD.show();

        Log.e("xx","show   ==============");
    }

    public static void cancelHuanchong()
    {
        if (huanchongD!=null)
            huanchongD.cancel();
    }

    public static void dismissHuanchong()
    {
        if (huanchongD!=null)
            huanchongD.dismiss();
    }


    public static void showWait(Context c,  OnCancelListener cancel)
    {
        if (waitD!=null)
            waitD.dismiss();

        AlertDialog.Builder builder=new AlertDialog.Builder(c);
        builder.setTitle("请稍后");
        builder.setMessage("等待好友。。。。");
        builder.setPositiveButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancelWait();
            }
        });
        builder.setCancelable(false);
        builder.setOnCancelListener(cancel);
        waitD=builder.create();
        waitD.show();
    }

    public static void cancelWait()
    {
        if (waitD!=null)
            waitD.cancel();
    }

    public static void dismissWait()
    {
        if (waitD!=null)
            waitD.dismiss();
    }

    class H extends android.os.Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Toast.makeText(getContext(), "对方拒绝了你的请求", Toast.LENGTH_SHORT).show();
        }

    }
}
