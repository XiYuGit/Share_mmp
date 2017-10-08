package com.our_company.iqiyi.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.our_company.iqiyi.R;
import com.our_company.iqiyi.Util.OnItemClickListener;
import com.our_company.iqiyi.bean.ThemeInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/2.
 */

public class ChangeThemeAdapter extends RecyclerView.Adapter {
    private OnItemClickListener listener;
    private ArrayList<ThemeInfo>themeInfos;



    public ChangeThemeAdapter(ArrayList<ThemeInfo> themeInfos) {

        this.themeInfos = themeInfos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.change_skin_item,parent,false);
        final ThemeHolder themeHolder=new ThemeHolder(view);
        if (listener!=null)
        {
            themeHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(themeHolder.itemView);
                }
            });
        }
        return themeHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ThemeHolder themeHolder= (ThemeHolder) holder;
        themeHolder.imageView.setColorFilter(themeInfos.get(position).getPrimaryColor());
    }
    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return themeInfos.size();
    }
    class ThemeHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public ThemeHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.change_skin_item);
        }
    }
}
