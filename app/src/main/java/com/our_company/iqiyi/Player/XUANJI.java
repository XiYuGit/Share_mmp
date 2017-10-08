package com.our_company.iqiyi.Player;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.our_company.iqiyi.Player.view.ControlView;
import com.our_company.iqiyi.R;

/**
 * Created by Administrator on 2017/6/17.
 */

public class XUANJI extends Dialog {

    private ControlView cv;
    private GridView gv;
    private Context c;

    public XUANJI(Context context, ControlView v) {
        super(context, R.style.MDialog);
        this.cv=v;
        c=context;
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.width=((Activity)context).getWindowManager().getDefaultDisplay().getWidth()/5*2;
        //lp.height=((Activity)context).getWindowManager().getDefaultDisplay().getHeight();
        lp.height=100;
        lp.gravity= Gravity.RIGHT;
        getWindow().setAttributes(lp);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v= LayoutInflater.from(c).inflate(R.layout.xuanji,null,false);
        setContentView(v);
        v.setLayoutParams(new FrameLayout.LayoutParams(((Activity)c).getWindowManager().getDefaultDisplay().getWidth()/5*2,((Activity)c).getWindowManager().getDefaultDisplay().getHeight()));

        gv=(GridView)findViewById(R.id.xuanji_grid);
        String []x=new String[100];
        for (int i=0;i<100;i++)
        {
            x[i]=""+(i+1);
        }
        gv.setAdapter(new ArrayAdapter<String>(c,R.layout.text,x));
    }
}
