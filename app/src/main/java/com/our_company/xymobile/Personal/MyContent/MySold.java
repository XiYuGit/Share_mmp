package com.our_company.xymobile.Personal.MyContent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.our_company.xymobile.Personal.Adapter.MySend_Adapter;
import com.our_company.xymobile.Personal.Adapter.MySend_Data;
import com.our_company.xymobile.Personal.DividerItemDecoration;
import com.our_company.xymobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by little star on 2017/4/29.
 */

public class MySold extends AppCompatActivity {
    List<MySend_Data> mySendDatas=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_sold);
        ImageButton imageButton;
        imageButton= (ImageButton) findViewById(R.id.button_safe_return);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();
    }
    private void init(){
        MySend_Data mySendData1=new MySend_Data("商品名","¥0.00","发布时间","2017.01.01","商品状态","已出售",R.drawable.goods);
        mySendDatas.add(mySendData1);
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.recyclerView1);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        MySend_Adapter mySendAdapter=new MySend_Adapter(mySendDatas);
        recyclerView.setAdapter(mySendAdapter);
    }
}
