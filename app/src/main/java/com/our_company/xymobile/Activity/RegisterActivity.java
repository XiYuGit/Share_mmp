package com.our_company.xymobile.Activity;

/**
 * Created by miaojie on 2017/6/12.
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.our_company.xymobile.R;
import com.our_company.xymobile.bean.ThemeInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import xiyou.mobile.User;

/**
 * Created by miaojie on 2017/5/22.
 */

public class RegisterActivity extends Activity {
    private EditText userNickName;
    private EditText userName;
    private EditText passWord;
    private Button registerButton;
    private Button backButton;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        initTheme();
        userName= (EditText) findViewById(R.id.registerUserName);
        userNickName= (EditText) findViewById(R.id.registerUserNickName);
        passWord= (EditText) findViewById(R.id.registerUserPassWord);
        registerButton= (Button) findViewById(R.id.registerRegister);
        backButton= (Button) findViewById(R.id.registerBack);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                        Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        RegisterActivity.this.finish();
                        break;
                    case 2:

                        break;
                }
            }
        };
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String stringUserName=userName.getText().toString();
                final String stringPassWord=passWord.getText().toString();
                final String stringNickName=userNickName.getText().toString();

                if(stringNickName.equals("")||stringPassWord.equals("")||stringUserName.equals(""))
                {
                    return;
                }

                if(!isChinaPhoneLegal(stringUserName))
                {
                    Toast.makeText(RegisterActivity.this, "手机号不正确！请重新输入", Toast.LENGTH_SHORT).show();
                    userName.setText("");
                    passWord.setText("");
                    return;
                }
                new Thread()
                {
                    @Override
                    public void run() {
                        super.run();
                        Message message=handler.obtainMessage();

                        User.register(stringUserName,stringPassWord,stringNickName);
                        message.what=1;
                        handler.sendMessage(message);


                    }
                }.start();

            }
        });
    }
    private void initTheme()
    {
        LinearLayout linearLayout= (LinearLayout) findViewById(R.id.top);
        linearLayout.setBackgroundColor(ThemeInfo.getThemeInfo().getPrimaryColor());
        getWindow().setStatusBarColor(ThemeInfo.getThemeInfo().getStatusBarColor());
    }

    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
