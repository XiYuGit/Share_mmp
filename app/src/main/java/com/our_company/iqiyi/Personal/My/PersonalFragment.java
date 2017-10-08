package com.our_company.iqiyi.Personal.My;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.our_company.iqiyi.Activity.LoginActivity;
import com.our_company.iqiyi.Personal.Adapter.FriendListAdapter;
import com.our_company.iqiyi.Personal.Adapter.Menu_Adapter;
import com.our_company.iqiyi.Personal.DividerItemDecoration;
import com.our_company.iqiyi.Personal.My_menu1;
import com.our_company.iqiyi.R;
import com.our_company.iqiyi.Util.LoginUtil;
import com.our_company.iqiyi.Util.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import xiyou.mobile.User;


public class PersonalFragment extends Fragment implements View.OnClickListener {
    private ImageButton set;
    private ImageButton message;
    CircleImageView circleImageView;
    private List<My_menu1>my_menu1=new ArrayList<>();
    private View view;
    private List<Activity> activities = new ArrayList<Activity>();
    private Button login_button;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_main,container,false);
        circleImageView= (CircleImageView) view.findViewById(R.id.circleimage);
     //   circleImageView.setImageDrawable(R.drawable.head);


        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView1);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        Menu_Adapter menu_adapter=new Menu_Adapter(my_menu1);
        recyclerView.setAdapter(menu_adapter);

        set= (ImageButton) view.findViewById(R.id.set);
        message= (ImageButton)view. findViewById(R.id.message);
        set.setOnClickListener(this);
        message.setOnClickListener(this);

        login_button= (Button) view.findViewById(R.id.login_button);
        login_button .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        if(LoginUtil.isLogin)
        {
            login_button.setText(LoginUtil.currentUser.getName());
        }
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        /*getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS *//*| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION*//*);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN *//*| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION *//*| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//         //   window.setNavigationBarColor(Color.TRANSPARENT);
//        }
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        Transition explode = TransitionInflater.from(this).inflateTransition(android.R.transition.explode);
//        getWindow().setEnterTransition(explode);*/
//        setContentView(R.layout.activity_main);
//
//
//        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.recyclerView1);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
//
//        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        Menu_Adapter menu_adapter=new Menu_Adapter(my_menu1);
//        recyclerView.setAdapter(menu_adapter);
//
//        set= (ImageButton) findViewById(R.id.set);
//        message= (ImageButton) findViewById(R.id.message);
//        set.setOnClickListener(this);
//        message.setOnClickListener(this);
//
//    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set:
//                Intent intent1=new Intent(getContext(),My_set.class);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    startActivity(intent1, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
//                }
                break;
            case R.id.message :
//                Intent intent2=new Intent(getContext(),talk.class);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    startActivity(intent2, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
//                }

//                showFriendListDialog();
//
                break;

        }
    }
    private void showFriendListDialog(){
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(getContext());
        View dialogView=LayoutInflater.from(getContext()).inflate(R.layout.friend_dialog_view,null,false);
        final RecyclerView friendList= (RecyclerView) dialogView.findViewById(R.id.friend_list);
        friendList.setLayoutManager(new LinearLayoutManager(getContext()));
        FriendListAdapter adapter=new FriendListAdapter(getContext());
        adapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                bottomSheetDialog.dismiss();
                int position=friendList.getChildAdapterPosition(view);
                Toast.makeText(getContext(),""+position,Toast.LENGTH_SHORT).show();
                final ProgressDialog waitingDialog=
                        new ProgressDialog(getContext());
                waitingDialog.setTitle("我是一个等待Dialog");
                waitingDialog.setMessage("等待中...");
                waitingDialog.setIndeterminate(true);
                waitingDialog.setCancelable(false);
                waitingDialog.show();
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        waitingDialog.dismiss();
                    }
                },2000);
            }
        });
        friendList.setAdapter(adapter);
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.setTitle("我的好友");
        bottomSheetDialog.show();

    }
    public void exit(){
        for (Activity activity : activities) {
            if (activity!=null) {
                activity.finish();
            }
        }
        System.exit(0);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode==KeyEvent.KEYCODE_BACK) {
//            Log.i("ifififififi","dadada");
//            AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
//            dialog.setTitle("提示");
//            dialog.setMessage("确定退出?");
//            dialog.setCancelable(false);
//            dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    exit();
//                }
//            });
//            dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                }
//            });
//            dialog.show();
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}
