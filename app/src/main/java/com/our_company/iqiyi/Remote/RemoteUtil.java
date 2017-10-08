package com.our_company.iqiyi.Remote;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.our_company.iqiyi.Personal.Adapter.FriendListAdapter;
import com.our_company.iqiyi.R;
import com.our_company.iqiyi.Service.FriendService;
import com.our_company.iqiyi.Util.LoginUtil;
import com.our_company.iqiyi.Util.OnItemClickListener;
import com.our_company.iqiyi.bean.ThemeInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import xiyou.mobile.User;

/**
 * Created by miaojie on 2017/5/3.
 */

public class RemoteUtil {
    public static final String POST_TYPE_CLIENT="Client";
    public static final String POST_TYPE_MASTER="Master";
    public static Window clientWindow;
    public static String ipAdress="172.20.0.249";
    public static int port=4396;
    public static boolean isConnect;
    public static String type;
    public static Socket socketClient;
    public static ServerSocket socketMaster;
    public static Instrumentation instrumentation=new Instrumentation();
    public static int screenHeight;
    public static int screenWidth;
    public static Bitmap bitmap;
    public static Handler handler;
    public static Runnable runnable;
    public static double theLastX;
    public static double theLastY;
    public static boolean isFirstCmd=true;
    public static Context maxContext;
    public static Manager manager;
    public static MasterActivity masterActivity=null;
    public static int personal_uid=-1;
    public static boolean canTouch=false;
    public static boolean canShot=false;
    public static int cmdNum=0;


    public static Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return bitmap;//压缩好比例大小后再进行质量压缩
    }

    public static void showFriendListDialog(final Context context, final int type){
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(context);
        final View dialogView= LayoutInflater.from(context).inflate(R.layout.friend_dialog_view,null,false);
        Toolbar toolbar= (Toolbar) dialogView.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ThemeInfo.getThemeInfo().getPrimaryColor());
        final RecyclerView friendList= (RecyclerView) dialogView.findViewById(R.id.friend_list);
        friendList.setLayoutManager(new LinearLayoutManager(context));
        final Handler handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                final ArrayList<User> friendInfoList=User.get().friends;
                FriendListAdapter adapter=new FriendListAdapter(context,friendInfoList);

                adapter.setListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view) {
                        final Handler handler=new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                            }
                        };
                        bottomSheetDialog.dismiss();
                        final int position=friendList.getChildAdapterPosition(view);
//                Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
                        switch (type)
                        {
                            case FriendService.REQUEST_REMOTE_CONTROL:
                                if(!friendInfoList.get(position).isOnline())
                                {
                                    Toast.makeText(context, "该好友不在线！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                LoginUtil.mainActivity.startActivity(new Intent(LoginUtil.mainActivity, MasterActivity.class));
                                new Thread()
                                {
                                    @Override
                                    public void run() {
                                        super.run();
                                        User.get().requestControl(friendInfoList.get(position).getName());
                                    }
                                }.start();
                                break;
                            case FriendService.REQUEST_HAVING_FUNNY:
                                break;
                            case -1:
                                break;
                        }

                    }
                });
                friendList.setAdapter(adapter);
                bottomSheetDialog.setContentView(dialogView);
//                bottomSheetDialog.setTitle("我的好友");
                bottomSheetDialog.show();
            }
        };

        new Thread(){
            @Override
            public void run() {
                super.run();
                User.get().freshFriends();
                handler.sendMessage(handler.obtainMessage());
            }
        }.start();

    }
}
