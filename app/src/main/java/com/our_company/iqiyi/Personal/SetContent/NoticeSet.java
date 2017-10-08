package com.our_company.iqiyi.Personal.SetContent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;

import com.our_company.iqiyi.R;


/**
 * Created by little star on 2017/4/20.
 */

public class NoticeSet extends AppCompatActivity {
    Switch aSwitch1;
    Switch aSwitch2;
    Switch aSwitch3;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_set_notice);
        aSwitch1= (Switch) findViewById(R.id.switch1);
        aSwitch2= (Switch) findViewById(R.id.switch2);
        aSwitch3= (Switch) findViewById(R.id.switch3);
        aSwitch1.setChecked(true);
        aSwitch2.setChecked(true);
        aSwitch3.setChecked(true);
        imageButton= (ImageButton) findViewById(R.id.button_safe_return);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
