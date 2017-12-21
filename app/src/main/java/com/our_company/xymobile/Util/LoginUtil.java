package com.our_company.xymobile.Util;

import android.app.ProgressDialog;
import android.content.Intent;

import com.our_company.xymobile.Main;
import com.our_company.xymobile.Remote.RemoteUtil;

import xiyou.mobile.User;

/**
 * Created by miaojie on 2017/6/13.
 */

public class LoginUtil {

    public static final int USER_LOGIN=1;
    public static final int USER_LOGOUT=2;
    public static boolean isLogin=false;
    public static User currentUser=null;
    public static Main mainActivity;
    public static ProgressDialog waitingDialog=null;
    public static String intToIp(int i) {

        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }

    public static void notifyLogin()
    {
        Intent intent=new Intent(Main.FRIEND_RECEIVER_ACTION);
        intent.putExtra("control",USER_LOGIN);
        LoginUtil.isLogin=true;
        mainActivity.sendBroadcast(intent);
        RemoteUtil.personal_uid=Integer.parseInt(User.get().getUsrname().substring(7,User.get().getUsrname().length()-1));
    }
    public static void notifyLogout()
    {
        Intent intent=new Intent(Main.FRIEND_RECEIVER_ACTION);
        intent.putExtra("control",USER_LOGOUT);
        LoginUtil.isLogin=false;
        mainActivity.sendBroadcast(intent);
    }


}
