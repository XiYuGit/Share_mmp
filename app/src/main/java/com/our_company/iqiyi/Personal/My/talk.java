package com.our_company.iqiyi.Personal.My;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.our_company.iqiyi.R;


/**
 * Created by little star on 2017/3/20.
 */

public class talk extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk);




    }
    public static class Msg{
        public static final int TYPE_RECEIVED = 0;
        public static final int TYPE_SENT=1;
        private String content;
        private int type;
        public Msg(String content,int type){
            this.content=content;
            this.type=type;
        }
        public String getContent(){
            return  content;
        }
        public int getType(){
            return type;
        }

    }
}
