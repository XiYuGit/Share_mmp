package com.our_company.iqiyi.Net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by little star on 2017/5/31.
 */

public class NetPet {

    private Handler handler;
    public NetPet() {}
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void getNet(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://baobab.kaiyanapp.com/api/v4/categories/videoList?start=10&num=10&strategy=date&id=26&udid=b37032bcc61348928fdf375011361a471a98cce5&vc=225&vn=3.12.0&deviceModel=1605-A01&first_channel=eyepetizer_360_market&last_channel=eyepetizer_360_market&system_version_code=23")
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("info_callFailure", e.toString());
                getNet();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.e("responseData", responseData);

                Message message=handler.obtainMessage();
                message.obj=responseData;
                message.what=1;
                handler.sendMessage(message);
            }
        });
    }
}