package com.our_company.iqiyi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.our_company.iqiyi.Net.Data;
import com.our_company.iqiyi.Player.VideoActivity;
import com.our_company.iqiyi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiYu on 2017/12/12.
 */

public class RecyclerviewAdapter1_switch extends RecyclerView.Adapter {

    private Bitmap[] bitmaps=new Bitmap[5];
    private List<Data> datalist=new ArrayList<>();
    private List<Data> playUrl =new ArrayList<>();
    private Context context;



    public RecyclerviewAdapter1_switch(Bitmap[] bitmap, List<Data> hotList, List<Data> exerciseList, List<Data> petList, List<Data> cateList) {
        this.bitmaps=bitmap;
        this.datalist=hotList;
        this.playUrl=hotList;
        Log.e("viewswitch","0");
        Log.e("viewswitch",datalist.size()+"");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("viewswitch","1_1");
        RecyclerView.ViewHolder holder=null;
        context=parent.getContext();
        Log.e("viewswitch","1");
        if(viewType==1){
            View view = LayoutInflater.from(context).inflate(R.layout.temp,parent,false);
            holder= new ViewHolderHead(view);
            final ViewHolderHead viewHolderHead =new ViewHolderHead(view);
            viewHolderHead.flipper.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int position = viewHolderHead.flipper.getDisplayedChild();
                    Log.e("clickkkkkkkk",position+"");
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",datalist.get(position));
                    context.startActivity(intent);
                }
            });
            Log.e("viewswitch","2");
            ((ViewHolderHead)holder).flipper.setFlipInterval(3000);
            ((ViewHolderHead)holder).flipper.startFlipping();//开始播放

        }else{
            Log.e("viewswitch","3");
            View view = LayoutInflater.from(context).inflate(R.layout.view1_1_switch,parent,false);
            holder= new ViewHolderBody(view);
            final ViewHolderBody viewHolderBody =new ViewHolderBody(view);
            viewHolderBody.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position =viewHolderBody.getAdapterPosition();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",datalist.get(position));
                    context.startActivity(intent);
                }}
            );
        }
        Log.e("viewswitch","4");
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("viewswitch","5");
         Context context =holder.itemView.getContext();
        if(holder instanceof ViewHolderHead){
            for(int i=0;i<4;i++) {
                ImageView imageView = new ImageView(context);
                imageView.setBackground(new BitmapDrawable(bitmaps[i]));
                ((ViewHolderHead) holder).flipper.addView(imageView);
            }
            Log.e("viewswitch","6");
        }else{
            Glide.with(context).load(datalist.get(position).getImg()).into(((ViewHolderBody)holder).imageView);
            ((ViewHolderBody)holder).textViewNum.setText(datalist.get(position).getPlay_num());
            ((ViewHolderBody)holder).textViewTime.setText(" "+Integer.parseInt(datalist.get(position).getScore())/60+":"+Integer.parseInt(datalist.get(position).getScore())%60);
            ((ViewHolderBody)holder).textViewTitle.setText(datalist.get(position).getTitle());

        }
        Log.e("viewswitch","7");
    }



    @Override
    public int getItemCount() {
        return datalist.size()-1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            Log.e("viewswitch","8");
            return 1;
        }else{
            Log.e("viewswitch","9");
            return 2;
        }
    }

    private class ViewHolderBody extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textViewTime;
        private TextView textViewNum;
        private TextView textViewTitle;
        public ViewHolderBody(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.cardIcon);
            textViewNum = (TextView) itemView.findViewById(R.id.cardScore);
            textViewTime = (TextView) itemView.findViewById(R.id.cardTime);
            textViewTitle = (TextView) itemView.findViewById(R.id.cardTitle);
        }
    }

    private class  ViewHolderHead extends RecyclerView.ViewHolder{
        ViewFlipper flipper;
        public ViewHolderHead(View itemView) {
            super(itemView);
            flipper = (ViewFlipper) itemView.findViewById(R.id.vf);
        }
    }


}
