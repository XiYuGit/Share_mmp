package com.our_company.xymobile.Adapter;

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
import com.our_company.xymobile.Net.Data;
import com.our_company.xymobile.Player.VideoActivity;
import com.our_company.xymobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by little star on 2017/6/17.
 */

public class SearchRecyclerView extends RecyclerView.Adapter {

    private Context context;
    private List<Data> movielist=new ArrayList<>();
    public SearchRecyclerView(List<Data>movielist){
        this.movielist=movielist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.view2_1,parent,false);
        RecyclerView.ViewHolder holder=null;
        holder=new ViewHolder(view);
        SearchRecyclerView.ViewHolder viewHolder = new SearchRecyclerView.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(position==0){
//            movielist.remove(0);//删除数据源
//            notifyItemRemoved(0);//刷新被删除的地方
//            notifyItemRangeChanged(0, getItemCount()); //
            Glide.with(context).load(movielist.get(0).getImg()).into(((SearchRecyclerView.ViewHolder) holder).imageView11);
            Glide.with(context).load(movielist.get(1).getImg()).into(((SearchRecyclerView.ViewHolder) holder).imageView12);
            ((SearchRecyclerView.ViewHolder) holder).textView11.setText(movielist.get(0).getTitle());
            ((SearchRecyclerView.ViewHolder) holder).textView12.setText(movielist.get(1).getTitle());
        }else{
//            movielist.remove(position*2);//删除数据源
//            notifyItemRemoved(position*2);//刷新被删除的地方
//            movielist.remove(position*2+1);//删除数据源
//            notifyItemRemoved(position*2+1);//刷新被删除的地方
//            notifyItemRangeChanged(position*2, getItemCount()); //
//            notifyItemRangeChanged(position*2+1, getItemCount()); //
            Glide.with(context).load(movielist.get(position*2).getImg()).into(((SearchRecyclerView.ViewHolder) holder).imageView11);
            Glide.with(context).load(movielist.get(position*2+1).getImg()).into(((SearchRecyclerView.ViewHolder) holder).imageView12);
            ((SearchRecyclerView.ViewHolder) holder).textView11.setText(movielist.get(position*2).getTitle());
            ((SearchRecyclerView.ViewHolder) holder).textView12.setText(movielist.get(position*2+1).getTitle());
        }
        SearchRecyclerView.ViewHolder viewHolder= (SearchRecyclerView.ViewHolder) holder;
        viewHolder.cardView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, VideoActivity.class);
                intent.putExtra("data",movielist.get(position*2));
                context.startActivity(intent);
            }
        });
        viewHolder.cardView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, VideoActivity.class);
                intent.putExtra("data",movielist.get(position*2+1));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movielist.size()/2;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView11;
        CardView cardView12;
        ImageView imageView11;
        ImageView imageView12;
        TextView textView11;
        TextView textView12;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView11= (CardView) itemView.findViewById(R.id.dcv21);
            cardView12= (CardView) itemView.findViewById(R.id.dcv22);
            imageView11= (ImageView) itemView.findViewById(R.id.iv11);
            imageView12= (ImageView) itemView.findViewById(R.id.iv12);
            textView11= (TextView) itemView.findViewById(R.id.tv11);
            textView12= (TextView) itemView.findViewById(R.id.tv12);
        }
    }

}
