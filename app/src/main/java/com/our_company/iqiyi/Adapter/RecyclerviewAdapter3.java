package com.our_company.iqiyi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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


public class RecyclerviewAdapter3 extends RecyclerView.Adapter {
    private Context context;
    private List<Data> movielist=new ArrayList<>();
    public RecyclerviewAdapter3(List<Data>movielist){
        this.movielist=movielist;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        context = parent.getContext();
        RecyclerView.ViewHolder holder = null;

        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view2_2, parent, false);
            holder=new  ViewHolderHead(view);
            return holder;
        } else {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view2_4, parent, false);
            holder = new ViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolderHead) {
            Glide.with(context).load(movielist.get(0).getImg()).into(((ViewHolderHead) holder).imageView11);
            ((ViewHolderHead) holder).textView11.setText(movielist.get(0).getTitle());
            ((ViewHolderHead) holder).textView12.setText(movielist.get(0).getScore());
            ViewHolderHead viewHolderHead = (ViewHolderHead) holder;
            viewHolderHead.cardView11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VideoActivity.class);
                    intent.putExtra("data", movielist.get(0));
                    context.startActivity(intent);
                }
            });

        } else {
            Glide.with(context).load(movielist.get(position * 2).getImg()).into(((ViewHolder) holder).imageView11);
            Glide.with(context).load(movielist.get(position * 2 + 1).getImg()).into(((ViewHolder) holder).imageView12);
            ((ViewHolder) holder).textView11.setText(movielist.get(position * 2).getTitle());
            ((ViewHolder) holder).textView12.setText(movielist.get(position * 2 + 1).getTitle());
            ((ViewHolder) holder).textViewtag1.setText(movielist.get(position * 2 ).getScore());
            ((ViewHolder) holder).textViewtag2.setText(movielist.get(position * 2 + 1).getScore());
            ViewHolder viewHolder = (ViewHolder) holder;
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
        TextView textViewtag1;
        TextView textViewtag2;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView11= (CardView) itemView.findViewById(R.id.dcv21);
            cardView12= (CardView) itemView.findViewById(R.id.dcv22);
            imageView11= (ImageView) itemView.findViewById(R.id.iv11);
            imageView12= (ImageView) itemView.findViewById(R.id.iv12);
            textView11= (TextView) itemView.findViewById(R.id.tv11);
            textView12= (TextView) itemView.findViewById(R.id.tv12);
            textViewtag1= (TextView) itemView.findViewById(R.id.tag1);
            textViewtag2= (TextView) itemView.findViewById(R.id.tag2);
        }
    }
}
