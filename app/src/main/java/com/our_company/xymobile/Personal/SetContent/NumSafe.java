package com.our_company.xymobile.Personal.SetContent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.our_company.xymobile.R;

/**
 * Created by little star on 2017/4/19.
 */

public class NumSafe extends AppCompatActivity implements View.OnClickListener{
    LinearLayout linearLayout1;
    LinearLayout linearLayout2;
    ImageButton button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition explode = TransitionInflater.from(this).inflateTransition(android.R.transition.explode);
        getWindow().setEnterTransition(explode);
        setContentView(R.layout.my_set_safe);
        linearLayout1= (LinearLayout) findViewById(R.id.linearLayout_safe_1);
        linearLayout2= (LinearLayout) findViewById(R.id.linearLayout_safe_2);
        button= (ImageButton) findViewById(R.id.button_safe_return);
        linearLayout1.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linearLayout_safe_1:

                break;
            case R.id.linearLayout_safe_2:

                break;

            case R.id.button_safe_return:
                finish();
                break;

        }
    }
}
