package com.our_company.iqiyi.Personal.SetContent;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.our_company.iqiyi.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by little star on 2017/4/19.
 */

public class MyInformation extends AppCompatActivity implements View.OnClickListener{
    LinearLayout linearLayout1;
    LinearLayout linearLayout2;
    LinearLayout linearLayout3;
    LinearLayout linearLayout4;
    CircleImageView circleImageView;
    ImageButton imagebutton;
    TextView textView;
    private  Uri photouri;
    private Context context=null;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_information);
        context=this;
        textView= (TextView) findViewById(R.id.textview_informayion_name);
        circleImageView= (CircleImageView) findViewById(R.id.circleimage);
        linearLayout1= (LinearLayout) findViewById(R.id.linearLayout1);
        linearLayout2= (LinearLayout) findViewById(R.id.linearLayout2);
        linearLayout3= (LinearLayout) findViewById(R.id.linearLayout3);
        linearLayout4= (LinearLayout) findViewById(R.id.linearLayout4);
        imagebutton= (ImageButton) findViewById(R.id.imagebutton);
        linearLayout1.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        linearLayout3.setOnClickListener(this);
        linearLayout4.setOnClickListener(this);
        imagebutton.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photouri));
                    circleImageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= 19) {
                    handleImageOnKitKat(data);
                } else {
                    handleImageBeforeKitKat(data);
                }
            }
        }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri =data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];//解析数字格式的id
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        displayImage(imagePath);
    }
    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath){
        if(imagePath!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            circleImageView.setImageBitmap(bitmap);
        }else{
            Toast.makeText(context, "filed", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Intent intent=new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent,2);//打开相册
                }else{
                    Toast.makeText(context, "请打开权限才能使用", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imagebutton:
                finish();
                break;
            case R.id.linearLayout1:
                showPopupWindow(v);
                break;
            case R.id.linearLayout2:
                showDialog2();
                break;
            case R.id.linearLayout3:

                break;
            case R.id.linearLayout4:

                break;

        }
    }
    private void showDialog2(){
        final EditText editText=new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("会员名")
                .setIcon(R.drawable.help)
                .setView(editText)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                 @Override
                    public void onClick(DialogInterface dialog, int which) {
                    //获得dialog 中 edittext的值
                        textView.setText(editText.getText());
                    }
                })
                .show();
//        android.support.v7.app.AlertDialog.Builder builder=new android.support.v7.app.AlertDialog.Builder(this);
//        builder.setTitle("会员名");
//        builder.setNegativeButton("取消",null);
//        builder.setPositiveButton("确定",null);
//        builder.setView(new EditText(this));
//        builder.show();
    }
    private void creame(){
        File photo=new File(getExternalCacheDir(),"takephoto.jpg");
                try {
                    if (photo.exists()) {
                        photo.delete();
                    }
                    photo.createNewFile();
                }catch(IOException i){
                    i.printStackTrace();
                }
                if(Build.VERSION.SDK_INT>=24){
                    photouri= FileProvider.getUriForFile(MyInformation.this,"com.School_Second.provider",photo);
                }else{
                    photouri=Uri.fromFile(photo);
                }
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photouri);
                startActivityForResult(intent,1);
    }
    private void photoGraph(){

    }
    private void showPopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        final View contentView = LayoutInflater.from(context).inflate(R.layout.information_popupwindow, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置按钮的点击事件
        Button button1 = (Button) contentView.findViewById(R.id.button1);
        Button button2 = (Button) contentView.findViewById(R.id.button2);
        Button button3 = (Button) contentView.findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                popupWindow.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MyInformation.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MyInformation.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    Intent intent=new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    startActivityForResult(intent,2);//打开相册
                }

                popupWindow.dismiss();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               creame();
                popupWindow.dismiss();
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("mengdd", "onTouch : ");
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.information_back));
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);
    }

}
