package com.our_company.iqiyi.Adapter;

/**
 * Created by little star on 2017/5/22.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.our_company.iqiyi.Activity.Search;
import com.our_company.iqiyi.Net.Data;
import com.our_company.iqiyi.Player.VideoActivity;
import com.our_company.iqiyi.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewAdapter1 extends RecyclerView.Adapter {
    private Context context;
    private Bitmap[] bitmaps=new Bitmap[5];
    private List<Data>datalist=new ArrayList<>();
    private List<Data>judatalist=new ArrayList<>();
    private List<Data>zongyilist=new ArrayList<>();
    private List<Data>movielist=new ArrayList<>();
    public RecyclerviewAdapter1(Bitmap[] bm, List<Data> zixun_list, List<Data> ju, List<Data> zongyilist, List<Data> movielist) {
        this.bitmaps=bm;
        this.datalist=zixun_list;
        this.judatalist=ju;
        this.zongyilist=zongyilist;
        this.movielist=movielist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder=null;
        context=parent.getContext();

        if(viewType==1){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.temp,parent,false);
            holder=new ViewHolderHead(view);
            ViewHolderHead viewHolderHead = new ViewHolderHead(view);
            viewHolderHead.flipper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
 //                   Toast.makeText(context, "点击了", Toast.LENGTH_SHORT).show();
                }
            });

        }

        else if(viewType==2){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.view1_1,parent,false);
            holder=new ViewHolder2(view);
            ViewHolder2 viewHolder2=new ViewHolder2(view);
            viewHolder2.cardView11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+datalist.get(0).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",datalist.get(0));
                    context.startActivity(intent);
                }
            });
            viewHolder2.cardView12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+datalist.get(1).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",datalist.get(1));
                    context.startActivity(intent);
                }
            });
            viewHolder2.cardView21.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+datalist.get(2).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",datalist.get(2));
                    context.startActivity(intent);
                }
            });
            viewHolder2.cardView22.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+datalist.get(3).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",datalist.get(3));
                    context.startActivity(intent);
                }
            });
        }

        else if(viewType==3){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.view1_2,parent,false);
            holder=new ViewHolder3(view);

            ViewHolder3 viewHolder3=new ViewHolder3(view);
            viewHolder3.cardView11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+judatalist.get(0).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",judatalist.get(0));
                    context.startActivity(intent);
                }
            });
            viewHolder3.cardView12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+judatalist.get(1).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",judatalist.get(1));

                    context.startActivity(intent);
                }
            });
            viewHolder3.cardView21.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+judatalist.get(2).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",judatalist.get(2));

                    context.startActivity(intent);
                }
            });
            viewHolder3.cardView22.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+judatalist.get(3).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",judatalist.get(3));

                    context.startActivity(intent);
                }
            });
        }

        else if(viewType==4){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.view1_3,parent,false);
            holder=new ViewHolder4(view);

            ViewHolder4 viewHolder4=new ViewHolder4(view);
            viewHolder4.cardView11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+movielist.get(0).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",movielist.get(0));
                    context.startActivity(intent);
                }
            });
            viewHolder4.cardView12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+movielist.get(1).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",movielist.get(1));
                    context.startActivity(intent);
                }
            });
            viewHolder4.cardView21.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+movielist.get(2).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",movielist.get(2));
                    context.startActivity(intent);
                }
            });
            viewHolder4.cardView22.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+movielist.get(3).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",movielist.get(3));
                    context.startActivity(intent);
                }
            });
        }else{
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.view1_4,parent,false);
            holder=new ViewHolder5(view);

            ViewHolder5 viewHolder5=new ViewHolder5(view);
            viewHolder5.cardView11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+zongyilist.get(0).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",zongyilist.get(0));
                    context.startActivity(intent);
                }
            });
            viewHolder5.cardView12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+zongyilist.get(1).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",zongyilist.get(1));
                    context.startActivity(intent);
                }
            });
            viewHolder5.cardView21.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+zongyilist.get(2).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",zongyilist.get(2));
                    context.startActivity(intent);
                }
            });
            viewHolder5.cardView22.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+zongyilist.get(3).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, VideoActivity.class);
                    intent.putExtra("data",zongyilist.get(3));
                    context.startActivity(intent);
                }
            });

        }

        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Context contextV=holder.itemView.getContext();
        if(holder instanceof ViewHolderHead){
            for(int i=0;i<4;i++){
                ImageView imageView=new ImageView(contextV);
                imageView.setBackground(new BitmapDrawable(bitmaps[i]));
               // imageView.setImageBitmap(bitmaps[i]);
                ((ViewHolderHead)holder).flipper.addView(imageView);
            }
            //flipper.setInAnimation(this,R.anim);//动画
            ((ViewHolderHead)holder).flipper.setFlipInterval(3000);
            ((ViewHolderHead)holder).flipper.startFlipping();//开始播放
        }else if(holder instanceof ViewHolder2){
            //((ViewHolder2)holder).cardView11
            Log.e("datalist",datalist.size()+"");
            Glide.with(context).load(datalist.get(0).getImg()).into(((ViewHolder2) holder).imageView11);
            Glide.with(context).load(datalist.get(1).getImg()).into(((ViewHolder2) holder).imageView12);
            Glide.with(context).load(datalist.get(2).getImg()).into(((ViewHolder2) holder).imageView21);
            Glide.with(context).load(datalist.get(3).getImg()).into(((ViewHolder2) holder).imageView22);
            ((ViewHolder2) holder).textView11.setText(datalist.get(0).getTitle());
            ((ViewHolder2) holder).textView12.setText(datalist.get(1).getTitle());
            ((ViewHolder2) holder).textView21.setText(datalist.get(2).getTitle());
            ((ViewHolder2) holder).textView22.setText(datalist.get(3).getTitle());
            ((ViewHolder2) holder).textView11p.setText(datalist.get(0).getPlay_num());
            ((ViewHolder2) holder).textView12p.setText(datalist.get(1).getPlay_num());

        }else if(holder instanceof ViewHolder3){
            Glide.with(context).load(judatalist.get(0).getImg()).into(((ViewHolder3) holder).imageView11);
            Glide.with(context).load(judatalist.get(1).getImg()).into(((ViewHolder3) holder).imageView12);
            Glide.with(context).load(judatalist.get(2).getImg()).into(((ViewHolder3) holder).imageView21);
            Glide.with(context).load(judatalist.get(3).getImg()).into(((ViewHolder3) holder).imageView22);
            ((ViewHolder3) holder).textView11.setText(judatalist.get(0).getTitle());
            ((ViewHolder3) holder).textView12.setText(judatalist.get(1).getTitle());
            ((ViewHolder3) holder).textView21.setText(judatalist.get(2).getTitle());
            ((ViewHolder3) holder).textView22.setText(judatalist.get(3).getTitle());
        }else if(holder instanceof ViewHolder4){
            Glide.with(context).load(movielist.get(0).getImg()).into(((ViewHolder4) holder).imageView11);
            Glide.with(context).load(movielist.get(1).getImg()).into(((ViewHolder4) holder).imageView12);
            Glide.with(context).load(movielist.get(2).getImg()).into(((ViewHolder4) holder).imageView21);
            Glide.with(context).load(movielist.get(3).getImg()).into(((ViewHolder4) holder).imageView22);
            ((ViewHolder4) holder).textView11.setText(movielist.get(0).getTitle());
            ((ViewHolder4) holder).textView12.setText(movielist.get(1).getTitle());
            ((ViewHolder4) holder).textView21.setText(movielist.get(2).getTitle());
            ((ViewHolder4) holder).textView22.setText(movielist.get(3).getTitle());
        }else{
            Glide.with(context).load(zongyilist.get(0).getImg()).into(((ViewHolder5) holder).imageView11);
            Glide.with(context).load(zongyilist.get(1).getImg()).into(((ViewHolder5) holder).imageView12);
            Glide.with(context).load(zongyilist.get(2).getImg()).into(((ViewHolder5) holder).imageView21);
            Glide.with(context).load(zongyilist.get(3).getImg()).into(((ViewHolder5) holder).imageView22);
            ((ViewHolder5) holder).textView11.setText(zongyilist.get(0).getTitle());
            ((ViewHolder5) holder).textView12.setText(zongyilist.get(1).getTitle());
            ((ViewHolder5) holder).textView21.setText(zongyilist.get(2).getTitle());
            ((ViewHolder5) holder).textView22.setText(zongyilist.get(3).getTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 1;
        }
        else if(position==1){
            return 2;
        }
        else if(position==2){
            return 3;
        } else if(position==3){
            return 4;
        }
        else {
            return 5;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

     private class ViewHolderHead extends RecyclerView.ViewHolder {
         ViewFlipper flipper;
          ViewHolderHead(View itemView) {
            super(itemView);
            flipper = (ViewFlipper) itemView.findViewById(R.id.vf);
        }
    }

    private class ViewHolder2 extends RecyclerView.ViewHolder{
        CardView cardView11;
        CardView cardView12;
        CardView cardView21;
        CardView cardView22;
        ImageView imageView11;
        ImageView imageView12;
        ImageView imageView21;
        ImageView imageView22;
        TextView textView11;
        TextView textView12;
        TextView textView21;
        TextView textView22;
        TextView textView11p;
        TextView textView12p;
         ViewHolder2(View itemView) {
            super(itemView);
             cardView11= (CardView) itemView.findViewById(R.id.cv11);
             cardView12= (CardView) itemView.findViewById(R.id.cv12);
             cardView21= (CardView) itemView.findViewById(R.id.cv21);
             cardView22= (CardView) itemView.findViewById(R.id.cv22);
             imageView11= (ImageView) itemView.findViewById(R.id.iv11);
             imageView12= (ImageView) itemView.findViewById(R.id.iv12);
             imageView21= (ImageView) itemView.findViewById(R.id.iv21);
             imageView22= (ImageView) itemView.findViewById(R.id.iv22);
             textView11= (TextView) itemView.findViewById(R.id.tv11);
             textView12= (TextView) itemView.findViewById(R.id.tv12);
             textView21= (TextView) itemView.findViewById(R.id.tv21);
             textView22= (TextView) itemView.findViewById(R.id.tv22);
             textView11p= (TextView) itemView.findViewById(R.id.tv11p);
             textView12p= (TextView) itemView.findViewById(R.id.tv12p);
         }
    }

    private class ViewHolder3 extends RecyclerView.ViewHolder{
        CardView cardView11;
        CardView cardView12;
        CardView cardView21;
        CardView cardView22;
        ImageView imageView11;
        ImageView imageView12;
        ImageView imageView21;
        ImageView imageView22;
        TextView textView11;
        TextView textView12;
        TextView textView21;
        TextView textView22;
         ViewHolder3(View itemView) {
            super(itemView);
             cardView11= (CardView) itemView.findViewById(R.id.cv11);
             cardView12= (CardView) itemView.findViewById(R.id.cv12);
             cardView21= (CardView) itemView.findViewById(R.id.cv21);
             cardView22= (CardView) itemView.findViewById(R.id.cv22);
             imageView11= (ImageView) itemView.findViewById(R.id.iv11);
             imageView12= (ImageView) itemView.findViewById(R.id.iv12);
             imageView21= (ImageView) itemView.findViewById(R.id.iv21);
             imageView22= (ImageView) itemView.findViewById(R.id.iv22);
             textView11= (TextView) itemView.findViewById(R.id.tv11);
             textView12= (TextView) itemView.findViewById(R.id.tv12);
             textView21= (TextView) itemView.findViewById(R.id.tv21);
             textView22= (TextView) itemView.findViewById(R.id.tv22);
        }
    }

    private class ViewHolder4 extends RecyclerView.ViewHolder{
        CardView cardView11;
        CardView cardView12;
        CardView cardView21;
        CardView cardView22;
        ImageView imageView11;
        ImageView imageView12;
        ImageView imageView21;
        ImageView imageView22;
        TextView textView11;
        TextView textView12;
        TextView textView21;
        TextView textView22;
         ViewHolder4(View itemView) {
            super(itemView);
             cardView11= (CardView) itemView.findViewById(R.id.cv11);
             cardView12= (CardView) itemView.findViewById(R.id.cv12);
             cardView21= (CardView) itemView.findViewById(R.id.cv21);
             cardView22= (CardView) itemView.findViewById(R.id.cv22);
             imageView11= (ImageView) itemView.findViewById(R.id.iv11);
             imageView12= (ImageView) itemView.findViewById(R.id.iv12);
             imageView21= (ImageView) itemView.findViewById(R.id.iv21);
             imageView22= (ImageView) itemView.findViewById(R.id.iv22);
             textView11= (TextView) itemView.findViewById(R.id.tv11);
             textView12= (TextView) itemView.findViewById(R.id.tv12);
             textView21= (TextView) itemView.findViewById(R.id.tv21);
             textView22= (TextView) itemView.findViewById(R.id.tv22);
        }
    }

    private class ViewHolder5 extends RecyclerView.ViewHolder{
        CardView cardView11;
        CardView cardView12;
        CardView cardView21;
        CardView cardView22;
        ImageView imageView11;
        ImageView imageView12;
        ImageView imageView21;
        ImageView imageView22;
        TextView textView11;
        TextView textView12;
        TextView textView21;
        TextView textView22;
        ViewHolder5(View itemView) {
            super(itemView);
            cardView11= (CardView) itemView.findViewById(R.id.cv11);
            cardView12= (CardView) itemView.findViewById(R.id.cv12);
            cardView21= (CardView) itemView.findViewById(R.id.cv21);
            cardView22= (CardView) itemView.findViewById(R.id.cv22);
            imageView11= (ImageView) itemView.findViewById(R.id.iv11);
            imageView12= (ImageView) itemView.findViewById(R.id.iv12);
            imageView21= (ImageView) itemView.findViewById(R.id.iv21);
            imageView22= (ImageView) itemView.findViewById(R.id.iv22);
            textView11= (TextView) itemView.findViewById(R.id.tv11);
            textView12= (TextView) itemView.findViewById(R.id.tv12);
            textView21= (TextView) itemView.findViewById(R.id.tv21);
            textView22= (TextView) itemView.findViewById(R.id.tv22);
        }
    }
}
