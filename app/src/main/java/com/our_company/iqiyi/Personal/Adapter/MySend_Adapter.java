package com.our_company.iqiyi.Personal.Adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.our_company.iqiyi.R;

import java.util.List;

/**
 * Created by little star on 2017/4/30.
 */

public class MySend_Adapter extends RecyclerView.Adapter {
    private List<MySend_Data>list;
    public MySend_Adapter(List<MySend_Data> mySend_datas) {
        list=mySend_datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder=null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_send_recycleview,parent,false);
        holder=new ViewHolder1(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MySend_Data mySendData=list.get(position);
        ((ViewHolder1)holder).textView1.setText(mySendData.getName());
        ((ViewHolder1)holder).textViewMoney.setText(mySendData.getMoney());
        ((ViewHolder1)holder).textView2.setText(mySendData.getTime());
        ((ViewHolder1)holder).textView22.setText(mySendData.getTime2());
        ((ViewHolder1)holder).textView3.setText(mySendData.getState());
        ((ViewHolder1)holder).textView33.setText(mySendData.getState2());
        ((ViewHolder1)holder).imageView.setImageResource(mySendData.getImageId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textViewMoney;
        TextView textView2;
        TextView textView22;
        TextView textView3;
        TextView textView33;
        ImageView imageView;
        public ViewHolder1(View itemView) {
            super(itemView);
            textView1= (TextView) itemView.findViewById(R.id.textViewName);
            textViewMoney= (TextView) itemView.findViewById(R.id.textViewMoney);
            textView2= (TextView) itemView.findViewById(R.id.textViewTime);
            textView22= (TextView) itemView.findViewById(R.id.textViewtime);
            textView3= (TextView) itemView.findViewById(R.id.textViewState);
            textView33= (TextView) itemView.findViewById(R.id.textViewstate);
            imageView= (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
