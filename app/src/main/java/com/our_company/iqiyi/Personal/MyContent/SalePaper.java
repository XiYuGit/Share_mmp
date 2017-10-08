package com.our_company.iqiyi.Personal.MyContent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.our_company.iqiyi.R;

/**
 * Created by little star on 2017/4/21.
 */

public class SalePaper extends AppCompatActivity {
    ImageButton imageButton;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_salepaper);

        imageButton= (ImageButton) findViewById(R.id.button_safe_return);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
