package com.our_company.iqiyi.Service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.our_company.iqiyi.Activity.LoginActivity;
import com.our_company.iqiyi.Remote.Manager;
import com.our_company.iqiyi.Remote.RemoteService;
import com.our_company.iqiyi.Remote.RemoteUtil;
import com.our_company.iqiyi.Util.LoginUtil;

import xiyou.mobile.User;

/**
 * Created by miaojie on 2017/6/13.
 */

public class FriendService extends Service {
//    public static final String REQUEST_ADD_FRIEND="request_add_friend";
//    public static final String REQUIRE_ADD_FRIEND="require_add_friend";

    public static final String FRIEND_RECEIVER_ACTION="com.our_company.iqiyi.Service.FriendReceiver";
    private String TAG="FriendService";
    public static final int REQUEST_ADD_FRIEND=0x001;
    public static final int REQUIRE_ADD_FRIEND=0x002;
    public static final int REQUIRE_ADD_FRIEND_REFUSED=0x007;
//    public static final String REQUSET_REMOTE_CONTROL="request_remote_control";
//    public static final String REQUIRE_REMOTE_CONTROL="require_remote_control";

    public static final int REQUEST_REMOTE_CONTROL=0x003;
    public static final int REQUIRE_REMOTE_CONTROL=0x004;
    public static final int REQUIRE_REMOTE_CONTROL_REFUSED=0x008;

//    public static final String REQUEST_HAVING_FUNNY="request_having_funny";
//    public static final String REQUIRE_HAVING_FUNNY="require_having_funny";

    public static final int REQUEST_HAVING_FUNNY=0x005;
    public static final int REQUIRE_HAVING_FUNNY=0x006;
    public static final int REQUIRE_HAVING_FUNNY_REFUSED=0x009;
    class FriendReceiver extends BroadcastReceiver {
        private Handler handler;
        public FriendReceiver(){
            handler=new Handler();
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            int control=intent.getIntExtra("control",-1);
            final String userNickName=intent.getStringExtra("nickName");
            AlertDialog.Builder builder=null;
            Log.e("收到了","123");
            switch (control)
            {
                case REQUEST_ADD_FRIEND:

                    builder=new AlertDialog.Builder(LoginUtil.mainActivity);

                    builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new Thread()
                            {
                                @Override
                                public void run() {
                                    super.run();
//                                    Toast.makeText(LoginUtil.mainActivity,"取消",Toast.LENGTH_SHORT).show();
                                    User.get().refusedAddFriend(userNickName);
                                }
                            }.start();
                        }
                    });
                    builder.setTitle("添加好友");
                    builder.setMessage(userNickName+" 请求加你为好友");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new Thread()
                            {
                                @Override
                                public void run() {
                                    super.run();
                                    User.get().permittAdd(userNickName);

                                }
                            }.start();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new Thread()
                                    {
                                        @Override
                                        public void run() {
                                            super.run();
                                            User.get().freshFriends();

                                        }
                                    }.start();
                                }
                            },200);

                        }
                    });
                    builder.create().show();
                    Log.e(TAG,"REQUEST_ADD_FRIEND");
                    break;
                case REQUIRE_ADD_FRIEND:
                    Toast.makeText(LoginUtil.mainActivity,userNickName+"同意加你为好友",Toast.LENGTH_SHORT).show();
                    new Thread()
                    {
                        @Override
                        public void run() {
                            super.run();
                            User.get().freshFriends();
                        }
                    }.start();
                    Log.e(TAG,"REQUIRE_ADD_FRIEND");
                    break;

                case REQUIRE_ADD_FRIEND_REFUSED:
                    Toast.makeText(LoginUtil.mainActivity,userNickName+"拒绝加你为好友",Toast.LENGTH_SHORT).show();
                    Log.e("拒绝了","123");
                    break;

                case REQUEST_REMOTE_CONTROL:
                    builder=new AlertDialog.Builder(LoginUtil.mainActivity);

                    builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new Thread()
                            {
                                @Override
                                public void run() {
                                    super.run();

                                    User.get().refusedControl(userNickName);
                                }
                            }.start();
//                            Toast.makeText(LoginUtil.mainActivity,"取消",Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setTitle("远程协助");
                    builder.setMessage(userNickName+" 请求控制你的手机");
                    builder.setPositiveButton("允许", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new Thread()
                            {
                                @Override
                                public void run() {
                                    super.run();
                                    User.get().permitControl(userNickName);
                                    RemoteUtil.ipAdress=User.get().getFriendIp(userNickName);
                                    Log.e("对方地址", RemoteUtil.ipAdress);
                                    Log.e("对方地址",User.get().getFriendIp(userNickName)+"--");
//                                    RemoteUtil.manager=new Manager(LoginUtil.mainActivity,LoginUtil.mainActivity);
//                                    RemoteUtil.manager.onCreate();
//                                    RemoteUtil.manager.onRecordClick(null);
                                    startService(new Intent(LoginUtil.mainActivity, RemoteService.class));     }
                            }.start();

                        }
                    });
                    builder.create().show();
                    Log.e(TAG,"REQUEST_REMOTE_CONTROL");
                    break;
                case REQUIRE_REMOTE_CONTROL:
                    LoginUtil.waitingDialog.dismiss();
                    Toast.makeText(LoginUtil.mainActivity,"开始控制",Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"REQUIRE_REMOTE_CONTROL");
                    break;
                case REQUIRE_REMOTE_CONTROL_REFUSED:
                    Log.e("拒绝收到了","567");
                    LoginUtil.waitingDialog.dismiss();
                    Toast.makeText(LoginUtil.mainActivity,"对方拒绝协助",Toast.LENGTH_SHORT).show();
                    if(RemoteUtil.masterActivity!=null)
                        RemoteUtil.masterActivity.finish();
                    break;

                case REQUEST_HAVING_FUNNY:
                    Log.e(TAG,"REQUEST_HAVING_FUNNY");
                    break;
                case REQUIRE_HAVING_FUNNY:
                    Log.e(TAG,"REQUIRE_HAVING_FUNNY");
                    break;

                case REQUIRE_HAVING_FUNNY_REFUSED:
                    break;
            }

        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("服务","st");
        FriendReceiver friendReceiver=new FriendReceiver();
        IntentFilter intentFilter=new IntentFilter(FRIEND_RECEIVER_ACTION);
        registerReceiver(friendReceiver,intentFilter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
