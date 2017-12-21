package com.our_company.xymobile.Personal.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.our_company.xymobile.Personal.My_menu1;
import com.our_company.xymobile.R;
import com.our_company.xymobile.Remote.MasterActivity;
import com.our_company.xymobile.Service.FriendService;
import com.our_company.xymobile.Util.LoginUtil;
import com.our_company.xymobile.Util.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import xiyou.mobile.User;

/**
 * Created by little star on 2017/3/20.
 */

public class Menu_Adapter extends RecyclerView.Adapter {
    private final ThreadLocal<List<My_menu1>> my_menu1List = new ThreadLocal<>();
    private Context context;
    public static TextView loginText;
    public Menu_Adapter(List<My_menu1> menu1List){
        my_menu1List.set(menu1List);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder=null;
        context=parent.getContext();

        if(viewType==1){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.my_menu1,parent,false);
            final ViewHolder1 viewHolder1=new ViewHolder1(view);
            viewHolder1.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent=new Intent(v.getContext(),MyMoney.class);
//                    v.getContext().startActivity(intent);

                }
            });
            viewHolder1.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent=new Intent(v.getContext(),ReducePaper.class);
//                    v.getContext().startActivity(intent);

                }
            });
            viewHolder1.button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent=new Intent(v.getContext(),SalePaper.class);
//                    v.getContext().startActivity(intent);
                }
            });

        holder=new ViewHolder1(view);
        }
        else if(viewType==2){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.my_menu2,parent,false);
            final ViewHolder2 viewHolder2=new ViewHolder2(view);
//            viewHolder2.linearLayout1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                        Intent intent=new Intent(v.getContext(),MySend.class);
////                        v.getContext().startActivity(intent);
//                        if(!LoginUtil.isLogin)
//                        {
//                            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        showFriendListDialog(v.getContext(),FriendService.REQUEST_HAVING_FUNNY);
//                    }
//            });
            viewHolder2.linearLayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent=new Intent(v.getContext(),MyBuy.class);
//                    v.getContext().startActivity(intent);
                    if(!LoginUtil.isLogin)
                    {
                        Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showFriendListDialog(v.getContext(),FriendService.REQUEST_REMOTE_CONTROL);
                }
            });
            viewHolder2.linearLayout3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
//                    Intent intent=new Intent(v.getContext(),MySold.class);
//                    v.getContext().startActivity(intent);
                    if(!LoginUtil.isLogin)
                    {
                        Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(v.getContext(),"取消",Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setTitle("添加好友");
                    builder.setMessage("请输入好友的昵称:");
                    final EditText userNickName=new EditText(v.getContext());
                    builder.setView(userNickName);

                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new Thread()
                            {
                                @Override
                                public void run() {
                                    super.run();
                                    User.get().addFriend(userNickName.getText().toString());
//                                    Toast.makeText(v.getContext(),"添加成功"+userNickName.getText().toString(),Toast.LENGTH_SHORT).show();
                                }
                            }.start();
                           }
                    });
                    builder.create().show();
                }
            });
            viewHolder2.linearLayout4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!LoginUtil.isLogin)
                    {
                        Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    Intent intent=new Intent(v.getContext(),MyTalk.class);
//                    v.getContext().startActivity(intent);
                    showFriendListDialog(v.getContext(),-1);
                }
            });
            loginText=viewHolder2.logoutText;

            viewHolder2.linearLayout5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent=new Intent(v.getContext(),MySave.class);
//                    v.getContext().startActivity(intent);
                      if(LoginUtil.isLogin)
                      {
                            new Thread()
                            {
                                @Override
                                public void run() {
                                    super.run();
                                    User.get().logout();
                                    LoginUtil.notifyLogout();
                                }
                            }.start();

                            Toast.makeText(v.getContext(),"退出登录",Toast.LENGTH_SHORT).show();
                     }
                    else
                    {
                        LoginUtil.mainActivity.finish();
                        Toast.makeText(v.getContext(),"退出",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder=new ViewHolder2(view);
        }
        else if(viewType==3){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.my_menu3,parent,false);
            holder=new ViewHolder3(view);
        }
        else{
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.my_menu4,parent,false);
            holder=new ViewHolder4(view);
        }
        return holder;
    }

    private void showFriendListDialog(final Context context, final int type){
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(context);
        final View dialogView=LayoutInflater.from(context).inflate(R.layout.friend_dialog_view,null,false);
        final RecyclerView friendList= (RecyclerView) dialogView.findViewById(R.id.friend_list);
        friendList.setLayoutManager(new LinearLayoutManager(context));
        final Handler handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                final ArrayList<User>friendInfoList=User.get().friends;
                FriendListAdapter adapter=new FriendListAdapter(context,friendInfoList);

                adapter.setListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view) {
                        final Handler handler=new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                            }
                        };
                        bottomSheetDialog.dismiss();
                        final int position=friendList.getChildAdapterPosition(view);
//                Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
                        switch (type)
                        {
                            case FriendService.REQUEST_REMOTE_CONTROL:
                                new Thread()
                                {
                                    @Override
                                    public void run() {
                                        super.run();
                                        User.get().freshFriends();
                                        handler.sendMessage(handler.obtainMessage());
                                    }
                                }.start();
                                if(!friendInfoList.get(position).isOnline())
                                {
                                    Toast.makeText(context, "该好友不在线！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                LoginUtil.mainActivity.startActivity(new Intent(LoginUtil.mainActivity, MasterActivity.class));
                                new Thread()
                                {
                                    @Override
                                    public void run() {
                                        super.run();
                                        User.get().requestControl(friendInfoList.get(position).getName());
                                    }
                                }.start();
                                break;
                            case FriendService.REQUEST_HAVING_FUNNY:
                                break;
                            case -1:
                                break;
                        }

                    }
                });
                friendList.setAdapter(adapter);
                bottomSheetDialog.setContentView(dialogView);
                bottomSheetDialog.setTitle("我的好友");
                bottomSheetDialog.show();
            }
        };

        new Thread(){
            @Override
            public void run() {
                super.run();
                User.get().freshFriends();
                handler.sendMessage(handler.obtainMessage());
            }
        }.start();

    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder1){
            ((ViewHolder1)holder).textView.setText("我的会员");
            ((ViewHolder1)holder).imageView.setImageResource(R.drawable.money);
            ((ViewHolder1)holder).button1.setText("黄金会员");
            ((ViewHolder1)holder).button2.setText("兑换码\n"+"0");
            ((ViewHolder1)holder).button3.setText("会员特权");
                //(ViewHolder1)((ViewHolder1) holder).textView
        }else if(holder instanceof ViewHolder2){

        }else if(holder instanceof  ViewHolder3){

        }
        else{
            Glide.with(context).load(R.drawable.face).into(((ViewHolder4)holder).imageView);
            ((ViewHolder4)holder).textView.setText("elder");
        }

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position){
        if(position==0){
            return 1;
        }
        else if(position==1){
            return 2;
        }
        else if(position==2) {
            return 3;
        }
        else {
            return 4;
        }
    }
    public static class ViewHolder1 extends RecyclerView.ViewHolder{
        View view1;
        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;
        Button button1;
        Button button2;
        Button button3;
        public ViewHolder1(View view){
            super(view);
            linearLayout= (LinearLayout) view.findViewById(R.id.m1l1);
            textView= (TextView) view.findViewById(R.id.textViewm1);
            imageView= (ImageView) view.findViewById(R.id.imageViewm1);
            button1= (Button) view.findViewById(R.id.button1m1);
            button2= (Button) view.findViewById(R.id.button2m1);
            button3= (Button) view.findViewById(R.id.button3m1);
        }
    }
    public static class ViewHolder2 extends RecyclerView.ViewHolder{
        public LinearLayout linearLayout1;
        public  LinearLayout linearLayout2;
        public  LinearLayout linearLayout3;
        public  LinearLayout linearLayout4;
        public  LinearLayout linearLayout5;
        public TextView logoutText;
        public ViewHolder2(View view){
            super(view);
//            linearLayout1= (LinearLayout) view.findViewById(R.id.linearLayout_menu2_1);
            linearLayout2= (LinearLayout) view.findViewById(R.id.linearLayout_menu2_2);
            linearLayout3= (LinearLayout) view.findViewById(R.id.linearLayout_menu2_3);
            linearLayout4= (LinearLayout) view.findViewById(R.id.linearLayout_menu2_4);
            linearLayout5= (LinearLayout) view.findViewById(R.id.linearLayout_menu2_5);
            logoutText= (TextView) view.findViewById(R.id.logout_text);
        }
        }
    public static class ViewHolder3 extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder3(View view) {
            super(view);
            textView= (TextView) itemView.findViewById(R.id.my_text);

        }
    }
    public static class ViewHolder4 extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        public ViewHolder4(View view) {
            super(view);
            textView= (TextView) view.findViewById(R.id.textView_my_card);
            imageView= (ImageView) view.findViewById(R.id.imageView_my_card);
        }
    }

}
