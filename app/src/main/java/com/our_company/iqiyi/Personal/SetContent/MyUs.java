package com.our_company.iqiyi.Personal.SetContent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.our_company.iqiyi.R;


/**
 * Created by little star on 2017/4/19.
 */

public class MyUs extends AppCompatActivity {
    ImageButton imageButton;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_us);
        imageButton= (ImageButton) findViewById(R.id.imagebutton_us);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
