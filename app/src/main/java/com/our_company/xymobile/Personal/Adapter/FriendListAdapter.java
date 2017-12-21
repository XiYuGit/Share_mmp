package com.our_company.xymobile.Personal.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.our_company.xymobile.R;
import com.our_company.xymobile.Util.OnItemClickListener;

import java.util.ArrayList;

import xiyou.mobile.User;

/**
 * Created by miaojie on 2017/6/12.
 */

public class FriendListAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<User>friendlist;
    private OnItemClickListener listener;
    public FriendListAdapter(Context c)
    {
        context=c;
    }

    public FriendListAdapter(Context context, ArrayList<User> friendlist) {
        this.context = context;
        this.friendlist = friendlist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.friend_item,parent,false);
        if(listener!=null)
        {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(view);
                }
            });
        }
        return new FriendHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(friendlist!=null)
        {
            FriendHolder friendHolder= (FriendHolder) holder;
            if (friendlist.get(position).isOnline())
            {
                friendHolder.userNickName.setHintTextColor(Color.GREEN);
                friendHolder.userNickName.setText(friendlist.get(position).getName());
            }
            else {
                friendHolder.userNickName.setHintTextColor(Color.BLACK);
                friendHolder.userNickName.setText(friendlist.get(position).getName());

            }
        }
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return friendlist.size();
    }
    class FriendHolder extends RecyclerView.ViewHolder{
        public TextView userNickName;
        public FriendHolder(View itemView) {
            super(itemView);
            userNickName= (TextView) itemView.findViewById(R.id.friend_item_userNickName);
        }
    }
}
