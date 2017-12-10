package com.our_company.iqiyi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.our_company.iqiyi.Net.Data;
import com.our_company.iqiyi.Player.VideoActivity;
import com.our_company.iqiyi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by little star on 2017/6/15.
 */

public class RecyclerviewAdapter5 extends RecyclerView.Adapter {
    private Context context;
    private List<Data> movielist=new ArrayList<>();
    public RecyclerviewAdapter5(List<Data>movielist){
        this.movielist=movielist;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        context = parent.getContext();
        RecyclerView.ViewHolder holder = null;

        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view2_3, parent, false);
            holder=new RecyclerviewAdapter5.ViewHolderHead(view);
            return holder;
        } else {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view2_4, parent, false);
            holder = new RecyclerviewAdapter5.ViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RecyclerviewAdapter5.ViewHolderHead) {
            Glide.with(context).load(movielist.get(0).getImg()).into(((RecyclerviewAdapter5.ViewHolderHead) holder).imageView11);
            ((RecyclerviewAdapter5.ViewHolderHead) holder).textView11.setText(movielist.get(0).getTitle());
            String temp= " "+Integer.parseInt(movielist.get(0).getScore())/60+" '"+Integer.parseInt(movielist.get(0).getScore())%60;
            ((RecyclerviewAdapter5.ViewHolderHead) holder).textView12.setText(temp);
            RecyclerviewAdapter5.ViewHolderHead viewHolderHead = (RecyclerviewAdapter5.ViewHolderHead) holder;
            viewHolderHead.cardView11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VideoActivity.class);
                    intent.putExtra("data", movielist.get(0));
                    context.startActivity(intent);
                }
            });

        } else {
            Glide.with(context).load(movielist.get(position * 2).getImg()).into(((RecyclerviewAdapter5.ViewHolder) holder).imageView11);
            Glide.with(context).load(movielist.get(position * 2 + 1).getImg()).into(((RecyclerviewAdapter5.ViewHolder) holder).imageView12);
            ((RecyclerviewAdapter5.ViewHolder) holder).textView11.setText(movielist.get(position * 2).getTitle());
            ((RecyclerviewAdapter5.ViewHolder) holder).textView12.setText(movielist.get(position * 2 + 1).getTitle());
            ((ViewHolder) holder).textViewTag1.setText(" "+Integer.parseInt(movielist.get(position * 2 ).getScore())/60+" '"+Integer.parseInt(movielist.get(position * 2 ).getScore())%60);
            ((ViewHolder) holder).textViewTag2.setText(" "+Integer.parseInt(movielist.get(position * 2 +1).getScore())/60+" '"+Integer.parseInt(movielist.get(position * 2 +1).getScore())%60);
            RecyclerviewAdapter5.ViewHolder viewHolder = (RecyclerviewAdapter5.ViewHolder) holder;
            viewHolder.cardView11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+movielist.get(position*2).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, VideoActivity.class);
                    intent.putExtra("data", movielist.get(position * 2));
                    context.startActivity(intent);
                }
            });
            viewHolder.cardView12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(context, "戳到我了喵~~ ,人家的id是"+movielist.get(position*2+1).getId().toString()+"呢！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, VideoActivity.class);
                    intent.putExtra("data", movielist.get(position * 2 + 1));
                    context.startActivity(intent);
                }
            });
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(position==0) return 0;
        return position;
    }

    @Override
    public int getItemCount() {
        return movielist.size()/2;
    }

    class ViewHolderHead extends RecyclerView.ViewHolder{
        CardView cardView11;
        ImageView imageView11;
        TextView textView11;
        TextView textView12;
        public ViewHolderHead(View itemView) {
            super(itemView);
            cardView11= (CardView) itemView.findViewById(R.id.cv11);
            imageView11= (ImageView) itemView.findViewById(R.id.iv11);
            textView11= (TextView) itemView.findViewById(R.id.tv11);
            textView12= (TextView) itemView.findViewById(R.id.tv_tag);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView11;
        CardView cardView12;
        ImageView imageView11;
        ImageView imageView12;
        TextView textView11;
        TextView textView12;
        TextView textViewTag1;
        TextView textViewTag2;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView11= (CardView) itemView.findViewById(R.id.dcv21);
            cardView12= (CardView) itemView.findViewById(R.id.dcv22);
            imageView11= (ImageView) itemView.findViewById(R.id.iv11);
            imageView12= (ImageView) itemView.findViewById(R.id.iv12);
            textView11= (TextView) itemView.findViewById(R.id.tv11);
            textView12= (TextView) itemView.findViewById(R.id.tv12);
            textViewTag1= (TextView) itemView.findViewById(R.id.tag1);
            textViewTag2= (TextView) itemView.findViewById(R.id.tag2);
        }
    }
}
