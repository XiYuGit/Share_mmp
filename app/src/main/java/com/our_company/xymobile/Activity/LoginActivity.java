package com.our_company.xymobile.Activity;

/**
 * Created by miaojie on 2017/6/12.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.our_company.xymobile.Main;
import com.our_company.xymobile.Player.ShareService;
import com.our_company.xymobile.R;
import com.our_company.xymobile.Service.FriendService;
import com.our_company.xymobile.Util.LoginUtil;
import com.our_company.xymobile.bean.ThemeInfo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import xiyou.mobile.User;

import static com.our_company.xymobile.Util.LoginUtil.notifyLogin;


/**
 * Created by miaojie on 2017/5/22.
 */

public class LoginActivity extends Activity {
    private EditText userName;
    private EditText passWord;
    private Button loginButton;
    private Button registerButton;
//    private ArrayList<UserInfo>userInfos;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initTheme();
        userName= (EditText) findViewById(R.id.login_userName);
        passWord= (EditText) findViewById(R.id.login_passWord);
        loginButton= (Button) findViewById(R.id.login_login);
        registerButton= (Button) findViewById(R.id.login_register);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        passWord.setText("");
                        Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        LoginUtil.isLogin=true;
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        notifyLogin();
                        ShareService.notifyLoged();
                        LoginActivity.this.finish();
                        if(passWord.getText().toString()!=null) {
                            save(passWord.getText().toString());
                        }
                        break;
                    case 3:
                        Toast.makeText(LoginActivity.this, "此账号已登录！", Toast.LENGTH_SHORT).show();
                        return;
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        Message message=handler.obtainMessage();
                        String name=null;
                        name=  User.login(userName.getText().toString(),passWord.getText().toString())+"";
                        if(name.equals(User.LOG_FAIL))
                        {
                            message.what=1;
                            handler.sendMessage(message);
                            return;
                        }
                        if (name.equals("hasLoged"))
                        {
                            message.what=3;
                            handler.sendMessage(message);
                            return;
                        }
                        startService(new Intent(LoginActivity.this,FriendService.class));
                        LoginUtil.currentUser=User.get();
                        setAddFriendListener();//设置添加朋友监听
                        setRemoteControlListener();//设置远程控制监听
                        WifiManager wifiManager= (WifiManager) getSystemService(Context.WIFI_SERVICE);
                        if(wifiManager.isWifiEnabled())
                        {
                            int ipAdress=wifiManager.getConnectionInfo().getIpAddress();
                            String ip=LoginUtil.intToIp(ipAdress);
                            Log.e("ip",ip);
                            User.get().setIp(ip);
                        }
                        message.what=2;
                        handler.sendMessage(message);
                    }
                }.start();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setRemoteControlListener()
    {

        User.get().addOnRequestControlListener(new User.OnRequestControlListener() {
            @Override
            public void onRequest(String name) {
                Intent intent=new Intent(FriendService.FRIEND_RECEIVER_ACTION);
                intent.putExtra("control",FriendService.REQUEST_REMOTE_CONTROL);
                intent.putExtra("nickName",name);
                LoginUtil.mainActivity.sendBroadcast(intent);
            }
        });

        User.get().addOnPermitControlListener(new User.OnPermitControlListener() {
            @Override
            public void onPermit(String name) {
                Intent intent=new Intent(FriendService.FRIEND_RECEIVER_ACTION);
                intent.putExtra("control",FriendService.REQUIRE_REMOTE_CONTROL);
                intent.putExtra("nickName",name);
                LoginUtil.mainActivity.sendBroadcast(intent);
            }
        });
        User.get().addOnRefusedControlListener(new User.OnRefusedControlListener() {
            @Override
            public void onRefused(String name) {
                Log.e("拒绝收到了","123");
                Intent intent=new Intent(FriendService.FRIEND_RECEIVER_ACTION);
                intent.putExtra("control",FriendService.REQUIRE_REMOTE_CONTROL_REFUSED);
                intent.putExtra("nickName",name);
                LoginUtil.mainActivity.sendBroadcast(intent);
            }
        });
    }

    private void setAddFriendListener()
    {
        User.get().addOnAddFriendListener(new User.OnAddFriendListener() {
            @Override
            public void onAddFriend(String name) {
                Intent intent=new Intent(FriendService.FRIEND_RECEIVER_ACTION);
                intent.putExtra("control",FriendService.REQUEST_ADD_FRIEND);
                intent.putExtra("nickName",name);
                LoginUtil.mainActivity.sendBroadcast(intent);
            }
        });

        User.get().addOnPermittAddListener(new User.OnPermittAddListener() {
            @Override
            public void onPermittAdd(String name) {
                Intent intent=new Intent(FriendService.FRIEND_RECEIVER_ACTION);
                intent.putExtra("control",FriendService.REQUIRE_ADD_FRIEND);
                intent.putExtra("nickName",name);
                LoginUtil.mainActivity.sendBroadcast(intent);
            }
        });
        User.get().addOnRefusedAddFriendListener(new User.OnRefusedAddFriendListener() {
            @Override
            public void onRefused(String name) {
                Intent intent=new Intent(FriendService.FRIEND_RECEIVER_ACTION);
                intent.putExtra("control",FriendService.REQUIRE_ADD_FRIEND_REFUSED);
                intent.putExtra("nickName",name);
                LoginUtil.mainActivity.sendBroadcast(intent);
            }
        });
    }
    private void initTheme()
    {
        LinearLayout linearLayout= (LinearLayout) findViewById(R.id.top);
        linearLayout.setBackgroundColor(ThemeInfo.getThemeInfo().getPrimaryColor());
        getWindow().setStatusBarColor(ThemeInfo.getThemeInfo().getStatusBarColor());
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK==keyCode)
        {
            Intent intent=new Intent(LoginActivity.this,Main.class);
            startActivity(intent);
            LoginActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    void save(String password){
       String key = getMd5(password);
        SharedPreferences sharedPreferences= getSharedPreferences("key",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("key",key);
        editor.apply();
    }
    static String getMd5(String s){
        String str="";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte [] messageByte=s.getBytes("UTF-8");
            byte [] md5Byte=messageDigest.digest(messageByte);
            str=bytestoHex(md5Byte);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    static String bytestoHex(byte[]bytes){
        StringBuffer hexStr= new StringBuffer();
        int num;
        for(int i=0;i<bytes.length;i++){
            num=bytes[i];
            if(num<0){
                num+=256;
            }
            if(num<16){
                hexStr.append(0);
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }
}
