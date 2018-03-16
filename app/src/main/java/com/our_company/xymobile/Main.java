package com.our_company.xymobile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.our_company.xymobile.Activity.About;
import com.our_company.xymobile.Activity.LoginActivity;
import com.our_company.xymobile.Adapter.ChangeThemeAdapter;
import com.our_company.xymobile.Adapter.MyPagerAdapter;
import com.our_company.xymobile.Fragment.Fragment1;
import com.our_company.xymobile.Fragment.Fragment2;
import com.our_company.xymobile.Fragment.Fragment3;
import com.our_company.xymobile.Fragment.Fragment4;
import com.our_company.xymobile.Fragment.Fragment5;
import com.our_company.xymobile.Player.ShareService;
import com.our_company.xymobile.Remote.RemoteUtil;
import com.our_company.xymobile.Service.FriendService;
import com.our_company.xymobile.Util.LoginUtil;
import com.our_company.xymobile.Util.OnItemClickListener;
import com.our_company.xymobile.Util.RSAUtil;
import com.our_company.xymobile.View.DecoratorViewPager;
import com.our_company.xymobile.bean.ThemeInfo;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import xiyou.mobile.User;

public class Main extends AppCompatActivity   {

    private DecoratorViewPager pager;
    private Bitmap[]bitmaps=new Bitmap[5];
    private List<Fragment>fragList;
    private TabLayout tabLayout;
    private ArrayList<String>titleList;

    private FragmentManager fm=getSupportFragmentManager();
    private MyPagerAdapter myPagerAdapter;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView login_button;
    private LinearLayout search;
    private ArrayList<ThemeInfo> themeColorList;
    private PersonalReceiver personalReceiver;

    public static final String FRIEND_RECEIVER_ACTION="com.our_company.iqiyi.Personal.My.PersonalReceiver";
    class PersonalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int control=intent.getIntExtra("control",-1);
            MenuItem m= (MenuItem) findViewById(R.id.nav_quitButton);


            switch (control)
            {
                case LoginUtil.USER_LOGIN:
                    login_button.setText(LoginUtil.currentUser.getName());
//                    Menu_Adapter.loginText.setText("退出登录");


                    login_button.setClickable(false);
                    break;
                case  LoginUtil.USER_LOGOUT:
                    login_button.setText("登录/注册");

                    login_button.setClickable(true);
                    break;
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.real_main);

        initChooseThemeColor();

//        search= (LinearLayout) findViewById(R.id.search2);
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(Main.this, Search.class);
//                startActivity(intent);
//            }
//        });
        final Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        IntentFilter intentFilter=new IntentFilter(FRIEND_RECEIVER_ACTION);
        personalReceiver=new PersonalReceiver();
        registerReceiver(personalReceiver,intentFilter);
        navigationView= (NavigationView) findViewById(R.id.nav_view);
        initDrawerHead();
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                switch (id)
                {
                    case R.id.nav_addFriend:
                        if(!LoginUtil.isLogin)
                        {
                            makeToast("请登录以使用此功能");
                            return true;
                        }

                        AlertDialog.Builder builder=new AlertDialog.Builder(Main.this);

                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.setTitle("添加好友");
                        builder.setMessage("请输入好友的昵称:");
                        final EditText userNickName=new EditText(Main.this);
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
                        break;
                    case R.id.nav_myFriend:
                        if(!LoginUtil.isLogin)
                        {
                            makeToast("请登录以使用此功能");
                            return true;
                        }
                        RemoteUtil.showFriendListDialog(Main.this,-1);
                        break;
                    case R.id.nav_remoteAssistant:
                        if(!LoginUtil.isLogin)
                        {
                            makeToast("请登录以使用此功能");
                            return true;
                        }
                       RemoteUtil.showFriendListDialog(Main.this, FriendService.REQUEST_REMOTE_CONTROL);
                        break;
                    case R.id.nav_aboutUs:
                        startActivity(new Intent(Main.this, About.class));
                        break;
                    case R.id.nav_setting:
                        showChooseThemeDialog();
                        break;
                    case R.id.nav_quitButton:
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

                            makeToast("退出登录");
                        }
                        else
                        {
                           Main.this.finish();
                            makeToast("退出");
                        }
                        return true;

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        setSupportActionBar(toolbar);
        LoginUtil.mainActivity=this;
        RemoteUtil.maxContext=this;
        ;
        titleList=new ArrayList<>();
        titleList.add("推荐");
        titleList.add("时尚");
        titleList.add("运动");
        titleList.add("萌宠");
        titleList.add("搞笑");
//        titleList.add("更多");

        pager=(DecoratorViewPager) findViewById(R.id.pager);
        pager.setNestedpParent((ViewGroup) pager.getParent());

        fragList=new ArrayList<Fragment>();
        fragList.add(new Fragment1());
        fragList.add(new Fragment2());
        fragList.add(new Fragment3());
        fragList.add(new Fragment4());
        fragList.add(new Fragment5());
//        fragList.add(new PersonalFragment());
        tabLayout= (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("推荐"));
        tabLayout.addTab(tabLayout.newTab().setText("时尚"));
        tabLayout.addTab(tabLayout.newTab().setText("运动"));
        tabLayout.addTab(tabLayout.newTab().setText("萌宠"));
        tabLayout.addTab(tabLayout.newTab().setText("搞笑"));
//        tabLayout.addTab(tabLayout.newTab().setText("更多"));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        myPagerAdapter= new MyPagerAdapter(fm,fragList,titleList);

        pager.setAdapter(myPagerAdapter);
        pager.setOffscreenPageLimit(4);
        pager.setCurrentItem(0);
//        tabLayout.setupWithViewPager(pager);

//        tabLayout.setTabTextColors(getResources().getColor(R.color.colorTabUnCheck),getResources().getColor(R.color.colorTabCheck));
        tabLayout.setupWithViewPager(pager);

        //授予权限：

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,},
                1);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        testRSA();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(personalReceiver);
        stopService(new Intent(this,FriendService.class));
//      stopService(new Intent(this,ShareService.class));

    }

    private void showChooseThemeDialog()
    {
        final Dialog dialog=new Dialog(this);
        Window dialogWindow= dialog.getWindow();
        WindowManager.LayoutParams params=dialogWindow.getAttributes();
        dialog.setTitle("请选择主题");
        final RecyclerView chooseView=new RecyclerView(this);
        chooseView.setLayoutManager(new GridLayoutManager(this,4));
        ChangeThemeAdapter changeThemeAdapter=new ChangeThemeAdapter(themeColorList);
        chooseView.setAdapter(changeThemeAdapter);
        changeThemeAdapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
               int position = chooseView.getChildAdapterPosition(view);
                dialog.dismiss();
                ThemeInfo.getThemeInfo().setStatusBarColor(themeColorList.get(position).getStatusBarColor());
                ThemeInfo.getThemeInfo().setPrimaryColor(themeColorList.get(position).getPrimaryColor());
                initTheme();
            }
        });
        dialog.setContentView(chooseView);
        dialog.show();
    }
    private void initChooseThemeColor()
    {
        themeColorList=new ArrayList<>();
        int []colorId={R.color.redPrimary,R.color.redPrimaryDark,
                R.color.pinkPrimary,R.color.pinkPrimaryDark,
                R.color.purplePrimary,R.color.purplePrimary,
                R.color.darkBluePrimary,R.color.darkBluePrimaryDark,
                R.color.lightBluePrimary,R.color.lightBluePrimaryDark,
                R.color.cyanPrimary,R.color.cyanPrimaryDark,
                R.color.tealPrimary,R.color.tealPrimaryDark,
                R.color.greenPrimary,R.color.greenPrimaryDark,
                R.color.yellowPrimary,R.color.yellowPrimaryDark,
                R.color.orangePrimary,R.color.orangePrimaryDark,
                R.color.deepOrangePrimary,R.color.deepOrangePrimaryDark,
                R.color.brownPrimary,R.color.brownPrimaryDark,
                R.color.greyPrimary,R.color.greyPrimaryDark,
                R.color.blueGreyPrimary,R.color.blueGreyPrimaryDark,
                R.color.blackPrimary,R.color.blackPrimary,
                R.color.lightGreenPrimary,R.color.lightGreenPrimaryDark};
        for(int i=0;i<colorId.length;i+=2)
        {
            ThemeInfo themeInfo=new ThemeInfo();
            themeInfo.setPrimaryColor(getResources().getColor(colorId[i]));
            themeInfo.setStatusBarColor(getResources().getColor(colorId[i+1]));
            themeColorList.add(themeInfo);
        }
    }

    private void initTheme()
    {
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(ThemeInfo.getThemeInfo().getPrimaryColor());
        getWindow().setStatusBarColor(ThemeInfo.getThemeInfo().getStatusBarColor());
        tabLayout.setBackgroundColor(ThemeInfo.getThemeInfo().getPrimaryColor());
        navigationView.getHeaderView(0).findViewById(R.id.nav_headView).setBackgroundColor(ThemeInfo.getThemeInfo().getPrimaryColor());
    }

    private void makeToast(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShareService.activityContext=this;
        RemoteUtil.clientWindow=getWindow();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED&&requestCode==1) {
            //TODO:已授权
//            Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
        } else {
            //TODO:用户拒绝
            Toast.makeText(this, "请授权！", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initDrawerHead()
    {
        login_button= (TextView) navigationView.getHeaderView(0).findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main.this, LoginActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RemoteUtil.manager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Display display=getWindowManager().getDefaultDisplay();
        RemoteUtil.screenWidth=display.getWidth();
        RemoteUtil.screenHeight=display.getHeight();
        initTheme();
    }

    public void testRSA() {
        try {
            //===============生成公钥和私钥，公钥传给客户端，私钥服务端保留==================
            //生成RSA公钥和私钥，并Base64编码
            KeyPair keyPair = RSAUtil.getKeyPair();
            String publicKeyStr = RSAUtil.getPublicKey(keyPair);
            String privateKeyStr = RSAUtil.getPrivateKey(keyPair);
            System.out.println("RSA公钥Base64编码:" + publicKeyStr);
            System.out.println("RSA私钥Base64编码:" + privateKeyStr);

            //=================客户端=================
            //hello, i am infi, good night!加密
            String message = "hello, i am infi, good night!";
            //将Base64编码后的公钥转换成PublicKey对象
            PublicKey publicKey = RSAUtil.string2PublicKey(publicKeyStr);
            //用公钥加密
            byte[] publicEncrypt = RSAUtil.publicEncrypt(message.getBytes(), publicKey);
            //加密后的内容Base64编码
            String byte2Base64 = RSAUtil.byte2Base64(publicEncrypt);
            System.out.println("RSA公钥加密并Base64编码的结果：" + byte2Base64);


            //##############    网络上传输的内容有Base64编码后的公钥 和 Base64编码后的公钥加密的内容     #################


            //===================服务端================
            //将Base64编码后的私钥转换成PrivateKey对象
            PrivateKey privateKey = null;
            privateKey = RSAUtil.string2PrivateKey(privateKeyStr);
            //加密后的内容Base64解码
            byte[] base642Byte = RSAUtil.base642Byte(byte2Base64);
            //用私钥解密
            byte[] privateDecrypt = RSAUtil.privateDecrypt(base642Byte, privateKey);
            //解密后的明文
            System.out.println("RSA解密后的明文: " + new String(privateDecrypt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
