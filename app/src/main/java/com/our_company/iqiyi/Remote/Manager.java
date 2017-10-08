package com.our_company.iqiyi.Remote;

/**
 * Created by miaojie on 2017/5/29.
 */

//        import android.app.Activity;
//        import android.content.Context;
//        import android.content.Intent;
//        import android.graphics.Bitmap;
//        import android.graphics.BitmapFactory;
//        import android.graphics.PixelFormat;
//        import android.hardware.display.DisplayManager;
//        import android.hardware.display.VirtualDisplay;
//        import android.media.Image;
//        import android.media.ImageReader;
//        import android.media.MediaRecorder;
//        import android.media.projection.MediaProjection;
//        import android.media.projection.MediaProjectionManager;
//        import android.net.Uri;
//        import android.os.AsyncTask;
//        import android.os.Handler;
//        import android.util.DisplayMetrics;
//        import android.util.Log;
//        import android.view.View;
//        import android.widget.RelativeLayout;
//        import android.widget.Toast;
//        import android.widget.ToggleButton;
//
//        import java.io.ByteArrayOutputStream;
//        import java.io.File;
//        import java.io.FileNotFoundException;
//        import java.io.FileOutputStream;
//        import java.io.IOException;
//        import java.nio.ByteBuffer;
//
///**
// * Created by Administrator on 2016/9/19.
// */
//public class Manager extends RelativeLayout {
//    private final int REQUEST_CODE = 0x001;
//    private final int RESULT_CODE = 0x002;
//    private final String FILE_PATH = "/sdcard/capture.mp4";
//
//    private final int DISPLAY_WIDTH = 1280;
//    private final int DISPLAY_HEIGHT = 720;
//
//    private Context mContext;
//    private Activity mActivity;
//
//    private ToggleButton mToggleButton;
//    private MediaProjectionManager mMediaProjectionManager;
//    private MediaProjection mMediaProjection;
//    private VirtualDisplay mVirtualDisplay;
//    private MediaProjectionCallback mMediaProjectionCallback;
//    private int mScreenDensity;
//    DisplayMetrics displayMetrics;
//    private MediaRecorder mMediaRecorder;
//    private ImageReader imageReader;
//    private Image image;
//    private   int num=0;
//    public Manager(Context mContext,Activity mActivity) {
//        super(mContext);
//        this.mContext = mContext;
//        this.mActivity = mActivity;
//    }
//
//    public void onCreate(){
//        Log.d("zhangjunling","onCreate()");
//        initData();
//        createUI();
//
//        mMediaProjectionManager = (MediaProjectionManager) mContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
//        mMediaRecorder = new MediaRecorder();
//        mMediaProjectionCallback = new MediaProjectionCallback();
//        imageReader=ImageReader.newInstance(displayMetrics.widthPixels,displayMetrics.heightPixels, PixelFormat.RGBA_8888, 2);
//       /* initRecorder(FILE_PATH);
//        prepareRecorder();*/
//    }
//
//    private void initData() {
//        displayMetrics  = new DisplayMetrics();
//        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        mScreenDensity = displayMetrics.densityDpi;
//
//    }
//
//    private void createUI() {
//        mToggleButton = new ToggleButton(mContext);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT
//                ,LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        // mToggleButton.setChecked(false);
//        // mToggleButton.setTextOn("关闭录制");
//        //mToggleButton.setTextOff("打开录制");
//        mToggleButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onRecordClick(v);
//            }
//        });
//        addView(mToggleButton,params);
//    }
//
//    private void onRecordClick(View v) {
//
//        if(((ToggleButton)v).isChecked()){
//            Log.d("zhangjunling","checked true");
//            initRecorder(FILE_PATH);
//            prepareRecorder();
//            if(null == mMediaProjection){
//                Intent intent = new Intent(mMediaProjectionManager.createScreenCaptureIntent());
//                mActivity.startActivityForResult(intent,REQUEST_CODE);
//                return;
//            }
//
//            mVirtualDisplay = createVirtualDisplay();
//            mMediaRecorder.start();
//        }
////        else{
////            Log.d("zhangjunling","checked false");
////            stopRecord();
////            mVirtualDisplay.release();
////            /*initRecorder(FILE_PATH);
////            prepareRecorder();*/
////        }
//    }
//
//    private void stopRecord() {
//        mMediaRecorder.stop();
//        mMediaRecorder.reset();
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d("zhangjunling","onActivityResult()00");
//        if(requestCode != REQUEST_CODE){
//            Toast.makeText(mContext,"REQUEST_CODE error",Toast.LENGTH_SHORT).show();
//            mToggleButton.setChecked(false);
//            return;
//        }
//        if(resultCode != mActivity.RESULT_OK){
//            Toast.makeText(mContext,"RESULT_CODE error",Toast.LENGTH_SHORT).show();
//            mToggleButton.setChecked(false);
//            return;
//        }
//
//        mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode,data);
//        mMediaProjection.registerCallback(mMediaProjectionCallback , null);
//        mVirtualDisplay = createVirtualDisplay();
//        image=imageReader.acquireNextImage();
//
//       final Handler handler=new Handler();
//       handler.postDelayed(new Runnable() {
//           @Override
//           public void run() {
//
//               getShot();
//               handler.postDelayed(this,100);
//           }
//       },100);
//
//
//       // mMediaRecorder.start();
//        Log.d("zhangjunling","onActivityResult()01");
//    }
//    private void getShot()
//    {
//        Log.e("来了","开始了");
//       Image image=imageReader.acquireNextImage();
//        if (image==null)
//            return;
//
//        Bitmap bitmap=createShot(image);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//
//            Log.e("成功",baos.size()+"");
//        try {
//            FileOutputStream fileOutputStream=new FileOutputStream("/sdcard/pics/asd"+num+".png");
//            fileOutputStream.write(baos.toByteArray());
//            fileOutputStream.flush();
//            fileOutputStream.close();
//            num++;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private Bitmap createShot(Image image)
//    {
//
//        int width = image.getWidth();
//        int height = image.getHeight();
//        final Image.Plane[] planes = image.getPlanes();
//        final ByteBuffer buffer = planes[0].getBuffer();
//        //每个像素的间距
//        int pixelStride = planes[0].getPixelStride();
//        //总的间距
//        int rowStride = planes[0].getRowStride();
//        int rowPadding = rowStride - pixelStride * width;
//        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
//        bitmap.copyPixelsFromBuffer(buffer);
//        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
//        image.close();
//        return bitmap;
//    }
//    private final class MediaProjectionCallback extends MediaProjection.Callback{
//
//        @Override
//        public void onStop() {
//            Log.d("zhangjunling","MediaProjectionCallback::onStop()");
//            super.onStop();
//            if(mToggleButton.isChecked()){
//                mToggleButton.setChecked(false);
//                stopRecord();
//                mVirtualDisplay.release();
//                mMediaProjection.stop();
//                mMediaProjection.unregisterCallback(mMediaProjectionCallback);
//                mMediaProjection = null;
//            }
//        }
//    }
//
//    private VirtualDisplay createVirtualDisplay() {
//        /**
//         * 创建虚拟画面
//         * 第一个参数：虚拟画面名称
//         * 第二个参数：虚拟画面的宽度
//         * 第三个参数：虚拟画面的高度
//         * 第四个参数：虚拟画面的标志
//         * 第五个参数：虚拟画面输出的Surface
//         * 第六个参数：虚拟画面回调接口
//         */
//        return mMediaProjection
//                .createVirtualDisplay("MainActivity", DISPLAY_WIDTH,
//                        DISPLAY_HEIGHT, mScreenDensity,
//                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
//                        imageReader.getSurface()/*mImageReader.getSurface()*/,
//                        null /* Callbacks */, null /* Handler */);
//    }
//
//    private void prepareRecorder() {
//        try {
//            mMediaRecorder.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//            mActivity.finish();
//        }
//    }
//
//    private void initRecorder(String filePath) {
//        /**
//         *  视频编码格式：default，H263，H264，MPEG_4_SP
//         获得视频资源：default，CAMERA
//         音频编码格式：default，AAC，AMR_NB，AMR_WB
//         获得音频资源：defalut，camcorder，mic，voice_call，voice_communication,
//         voice_downlink,voice_recognition, voice_uplink
//         输出方式：amr_nb，amr_wb,default,mpeg_4,raw_amr,three_gpp
//         */
//        //设置音频源
////        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        //设置视频源：Surface和Camera 两种
//        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
//        //设置视频输出格式
//        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        //设置视频编码格式
//        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//        //设置音频编码格式
////        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        //设置视频编码的码率
//        mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
//        //设置视频编码的帧率
//        mMediaRecorder.setVideoFrameRate(30);
//        //设置视频尺寸大小
//        mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
//        //设置视频输出路径
//        mMediaRecorder.setOutputFile(filePath);
//
//    }
//
//
//    public void onResume(){
//        Log.d("zhangjunling","onResume()");
//    }
//
//
//    public void onPause(){
//        Log.d("zhangjunling","onPause()");
//    }
//
//    public void onDestroy(){
//
//        Log.d("zhangjunling","onDestroy()");
//        if(mToggleButton.isChecked()){
//            mToggleButton.setChecked(false);
//        }
//        if(null != mMediaRecorder){
//            // mMediaRecorder.stop();
//            mMediaRecorder.release();
//            mMediaRecorder = null;
//        }
//        if(null != mVirtualDisplay){
//            mVirtualDisplay.release();
//            mVirtualDisplay = null;
//        }
//        if(null != mMediaProjection){
//            mMediaProjection.unregisterCallback(mMediaProjectionCallback);
//            mMediaProjection.stop();
//            mMediaProjection = null;
//        }
//    }
//
//}

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.our_company.iqiyi.Util.LoginUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/9/19.
 */
public class Manager extends RelativeLayout {
    private final int REQUEST_CODE = 0x001;
    private final int RESULT_CODE = 0x002;
    private String FILE_PATH = "/sdcard/capture";

    private  int DISPLAY_WIDTH = 1280;
    private  int DISPLAY_HEIGHT = 720;

    private Context mContext;
    private Activity mActivity;

    private ToggleButton mToggleButton;
    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionCallback mMediaProjectionCallback;
    private int mScreenDensity;
//    private int num=0;
    private MediaRecorder mMediaRecorder=null;
    private Handler bmpHandler;
    private int i=0;
    private Handler thisHandler;
    public Manager(Context mContext,Activity mActivity) {
        super(mContext);
        this.mContext = mContext;
        this.mActivity = mActivity;
//        onCreate();
    }

    public void setBmpHandler(Handler bmpHandler) {
        this.bmpHandler = bmpHandler;
    }

    public void onCreate(){
        Log.d("zhangjunling","onCreate()");
        initData();
        createUI();

        mMediaProjectionManager = (MediaProjectionManager) mContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Log.e("创建了","MediaRecorder");
        mMediaRecorder = new MediaRecorder();
        mMediaProjectionCallback = new MediaProjectionCallback();
       /* initRecorder(FILE_PATH);
        prepareRecorder();*/
    }

    private void initData() {
        DisplayMetrics displayMetrics  = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenDensity = displayMetrics.densityDpi;
        Display display=mActivity.getWindowManager().getDefaultDisplay();
        DISPLAY_WIDTH=display.getWidth();
        DISPLAY_HEIGHT=display.getHeight();
    }

    private void createUI() {
        mToggleButton = new ToggleButton(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT
                ,LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        // mToggleButton.setChecked(false);
        // mToggleButton.setTextOn("关闭录制");
        //mToggleButton.setTextOff("打开录制");

        mToggleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecordClick(v);
            }
        });
        addView(mToggleButton,params);
    }

    public void onRecordClick(View v) {
        if (null == mMediaProjection) {
            Log.e("进来了","12345");
            Intent intent = new Intent(mMediaProjectionManager.createScreenCaptureIntent());
            mActivity.startActivityForResult(intent, REQUEST_CODE);
            return;
        }



//            num++;
//            if(((ToggleButton)v).isChecked()){
//
//                Log.d("zhangjunling","checked true");
//                Log.e("开始了","123");
//                FILE_PATH="/sdcard/pics/capture"+num+".mp4";
//                initRecorder(FILE_PATH);
//                prepareRecorder();
//                if(null == mMediaProjection){
//                    Intent intent = new Intent(mMediaProjectionManager.createScreenCaptureIntent());
//                    mActivity.startActivityForResult(intent,REQUEST_CODE);
//                    return;
//                }
//
//                mVirtualDisplay = createVirtualDisplay();
//                mMediaRecorder.start();
//            }else{
//                Log.e("浪死了！","456");
//                Log.d("zhangjunling","checked false");
//                stopRecord();
//                mVirtualDisplay.release();
//            /*initRecorder(FILE_PATH);
//            prepareRecorder();*/
//            }
//        }catch (Exception e)
//        {
//            mMediaRecorder.reset();
//            Log.e("真的他妈浪死了","456");
//        }

    }

    private void stopRecord() {
        try {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
        }catch (Exception e)
        {
//            mMediaRecorder.stop();
            mMediaRecorder.reset();
            Log.e("真的他妈浪死了",e.toString());
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("zhangjunling","onActivityResult()00");
        if(requestCode != REQUEST_CODE){
            Toast.makeText(mContext,"REQUEST_CODE error",Toast.LENGTH_SHORT).show();
            mToggleButton.setChecked(false);
            return;
        }
        if(resultCode != mActivity.RESULT_OK){
            Toast.makeText(mContext,"RESULT_CODE error",Toast.LENGTH_SHORT).show();
            mToggleButton.setChecked(false);
            return;
        }
//        File aaa= Environment.getExternalStorageDirectory();
        File aaa=LoginUtil.mainActivity.getCacheDir();
        final File file=new File(aaa.getPath()+"/pics");
        if (!file.exists())
            file.mkdirs();
        Log.e("手机路径",file.getPath());
        FILE_PATH=file.getPath()+"/capture"+ i+".mp4";
        Log.e("路径发",FILE_PATH);
                initRecorder(FILE_PATH);
                prepareRecorder();
        Log.e("result","123");
        mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode,data);
        mMediaProjection.registerCallback(mMediaProjectionCallback , null);
        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
        try {
            final Handler handler=new Handler();
            final Runnable runnable2=new Runnable() {
                @Override
                public void run() {
                    Log.e("浪死了",i+"");
                    stopRecord();
                    mVirtualDisplay.release();
//                    if(i<2)
//                        return;
                    FILE_PATH=file.getPath()+"/capture"+ i+".mp4";
//                    String path="/sdcard/FINAL_RENDER_3.mp4";
                    Log.e("路径收",FILE_PATH);
                    MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
                    try {

                        mediaMetadataRetriever.setDataSource(FILE_PATH);
                    }catch (Exception e) {

                        Log.e("又他妈浪死了",e.toString());
                        mediaMetadataRetriever.release();
                        if(thisHandler==null)
                            return;
                        else
                        {
                            if (!RemoteUtil.isConnect&&i>10)
                            {
                                Log.e("remote","结束");
                                RemoteUtil.manager.onDestroy();
                                return;
                            }
                            Message message=new Message();
                            message.what=1;
                            thisHandler.sendMessage(message);
                        }
                        return;
                    }
                    if(thisHandler==null)
                        return;
                    else
                    {
                        Message message=new Message();
                        message.what=1;
                        if (!RemoteUtil.isConnect&&i>10)
                        {
                            Log.e("remote","结束");
                            RemoteUtil.manager.onDestroy();
                            return;
                        }
                        thisHandler.sendMessage(message);
                    }
//                    String duration= mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//                  Log.e("asd",duration);
//                    RemoteUtil.bitmap=mediaMetadataRetriever.getFrameAtTime();


                    Log.e("成功1","123");
                    Bitmap bitmap1=mediaMetadataRetriever.getFrameAtTime();
                    mediaMetadataRetriever.release();
                    RemoteUtil.bitmap=bitmap1;
                    Intent intent=new Intent("ASDFG");
                    intent.putExtra("control","updateScreen");
                    mContext.sendBroadcast(intent);
//                    Message message1=new Message();
//                    message1.obj=bitmap1;
//                    message1.what=1;
//                    bmpHandler.sendMessage(message1);
                    Log.e("成功2","123");

                }
            };
            final Runnable runnable1=new Runnable() {
                @Override
                public void run() {

                    i++;
                    Log.d("zhangjunling", "checked true");
                    Log.e("开始了", ""+i);
                    FILE_PATH=file.getPath()+"/capture"+ i+".mp4";
                    Log.e("路径发",FILE_PATH);
                    try {
                        initRecorder(FILE_PATH);
                        prepareRecorder();

                    }catch (Exception e)
                    {
//                        mMediaRecorder.stop();
                        mMediaRecorder.reset();
                        Log.e("知道浪死了","123");
                        handler.postDelayed(this,90);
                        return;
                    }


                    mVirtualDisplay = createVirtualDisplay();
                    mMediaRecorder.start();

                    Log.e("执行了","123");
                    Log.e("进去前",i+"");


                    if(thisHandler!=null)
                    {
                        Log.e("handler","不为空");
                        Message message=new Message();
                        message.what=2;
                        if (!RemoteUtil.isConnect&&i>10)
                        {
                            Log.e("remote","结束");
                            RemoteUtil.manager.onDestroy();
                            return;
                        }
                        thisHandler.sendMessage(message);
                    }
                    else
                    {
                        handler.postDelayed(runnable2,105);
                        handler.postDelayed(this,120);

                    }
                    Log.e("执行了","456");
//                    handler.postDelayed(this,90);

            /*initRecorder(FILE_PATH);
            prepareRecorder();*/
                }
            };
            handler.postDelayed(runnable1,10);
            thisHandler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what==1)
                    {
                        Log.e("handler","2");
                        if (!RemoteUtil.isConnect&&i>10)
                        {
                            Log.e("remote","结束");
                            RemoteUtil.manager.onDestroy();
                            return;
                        }
                        handler.postDelayed(runnable1,0);
                    }else {
                        Log.e("handler","1");
                        if (!RemoteUtil.isConnect&&i>10)
                        {
                            Log.e("remote","结束");
                            RemoteUtil.manager.onDestroy();
                            return;
                        }
                        handler.postDelayed(runnable2,105);
                    }
                }
            };
        }catch (Exception e)
        {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            Log.e("真的他妈浪死了",e.toString());
        }

        Log.d("zhangjunling","onActivityResult()01");
    }

    private final class MediaProjectionCallback extends MediaProjection.Callback{

        @Override
        public void onStop() {
            Log.d("zhangjunling","MediaProjectionCallback::onStop()");
            super.onStop();
            if(mToggleButton.isChecked()){
                mToggleButton.setChecked(false);
                stopRecord();
                mVirtualDisplay.release();
                mMediaProjection.stop();
                mMediaProjection.unregisterCallback(mMediaProjectionCallback);
                mMediaProjection = null;
            }
        }
    }

    private VirtualDisplay createVirtualDisplay() {
        /**
         * 创建虚拟画面
         * 第一个参数：虚拟画面名称
         * 第二个参数：虚拟画面的宽度
         * 第三个参数：虚拟画面的高度
         * 第四个参数：虚拟画面的标志
         * 第五个参数：虚拟画面输出的Surface
         * 第六个参数：虚拟画面回调接口
         */
        return mMediaProjection
                .createVirtualDisplay("MainActivity", DISPLAY_WIDTH,
                        DISPLAY_HEIGHT, mScreenDensity,
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                        mMediaRecorder.getSurface()/*mImageReader.getSurface()*/,
                        null /* Callbacks */, null /* Handler */);
    }

    private void prepareRecorder() {
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            mActivity.finish();
        }
    }

    private void initRecorder(String filePath) {
        /**
         *  视频编码格式：default，H263，H264，MPEG_4_SP
         获得视频资源：default，CAMERA
         音频编码格式：default，AAC，AMR_NB，AMR_WB
         获得音频资源：defalut，camcorder，mic，voice_call，voice_communication,
         voice_downlink,voice_recognition, voice_uplink
         输出方式：amr_nb，amr_wb,default,mpeg_4,raw_amr,three_gpp
         */
        mMediaRecorder.reset();
        //设置音频源
//        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置视频源：Surface和Camera 两种
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        //设置视频输出格式
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //设置视频编码格式
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //设置音频编码格式
//        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //设置视频编码的码率
        mMediaRecorder.setVideoEncodingBitRate(10000);
        //设置视频编码的帧率
        mMediaRecorder.setVideoFrameRate(30);
        //设置视频尺寸大小
        mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        //设置视频输出路径
        mMediaRecorder.setOutputFile(filePath);

    }


    public void onResume(){
        Log.d("zhangjunling","onResume()");
    }


    public void onPause(){
        Log.d("zhangjunling","onPause()");
    }

    public void onDestroy(){

        Log.d("zhangjunling","onDestroy()");
        if(mToggleButton.isChecked()){
            mToggleButton.setChecked(false);
        }
        if(null != mMediaRecorder){
            // mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        if(null != mVirtualDisplay){
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }
        if(null != mMediaProjection){
            mMediaProjection.unregisterCallback(mMediaProjectionCallback);
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

}
