package com.our_company.iqiyi.Personal.My;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.our_company.iqiyi.Main;
import com.our_company.iqiyi.Personal.SetContent.MyInformation;
import com.our_company.iqiyi.Personal.SetContent.MyUs;
import com.our_company.iqiyi.Personal.SetContent.NoticeSet;
import com.our_company.iqiyi.Personal.SetContent.NumSafe;
import com.our_company.iqiyi.R;


/**
 * Created by little star on 2017/3/20.
 */

public class My_set extends AppCompatActivity implements View.OnClickListener {
    LinearLayout linearLayout1;
    LinearLayout linearLayout2;
    LinearLayout linearLayout3;
    LinearLayout linearLayout4;
    LinearLayout linearLayout5;
    ImageButton imageButton;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition explode = TransitionInflater.from(this).inflateTransition(android.R.transition.explode);
        getWindow().setEnterTransition(explode);
        setContentView(R.layout.my_set);

        linearLayout1= (LinearLayout) findViewById(R.id.linearLayoutset1);
        linearLayout2= (LinearLayout) findViewById(R.id.linearLayoutset2);
        linearLayout3= (LinearLayout) findViewById(R.id.linearLayoutset3);
        linearLayout4= (LinearLayout) findViewById(R.id.linearLayoutset4);
        linearLayout5= (LinearLayout) findViewById(R.id.linearLayoutset5);
        imageButton= (ImageButton) findViewById(R.id.my_set_return);
        linearLayout1.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        linearLayout3.setOnClickListener(this);
        linearLayout4.setOnClickListener(this);
        linearLayout5.setOnClickListener(this);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(My_set.this,Main.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(My_set.this).toBundle());
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            Intent intent=new Intent(My_set.this,Main.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(My_set.this).toBundle());
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.my_set_return:
                Intent intent=new Intent(My_set.this,Main.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(My_set.this).toBundle());
                }
                break;
            case R.id.linearLayoutset1:
                Intent intent1=new Intent(My_set.this,MyInformation.class);
                startActivity(intent1);
                break;
            case R.id.linearLayoutset2:
                Intent intent2=new Intent(My_set.this,NumSafe.class);
                startActivity(intent2);
                break;
            case R.id.linearLayoutset3:
                Intent intent3=new Intent(My_set.this, NoticeSet.class);
                startActivity(intent3);
                break;
            case R.id.linearLayoutset4:
                Toast.makeText(this, "当前已是最新版本", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linearLayoutset5:
                Intent intent5=new Intent(My_set.this,MyUs.class);
                startActivity(intent5);
                break;
        }
    }
}
