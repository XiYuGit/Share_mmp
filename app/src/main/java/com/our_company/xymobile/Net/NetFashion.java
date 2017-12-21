package com.our_company.xymobile.Net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by little star on 2017/6/14.
 */

public class NetFashion {
    private Handler handler;
    public NetFashion() {}
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void getNet(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://baobab.kaiyanapp.com/api/v4/categories/videoList?start=10&num=10&strategy=date&id=24&udid=b37032bcc61348928fdf375011361a471a98cce5&vc=225&vn=3.12.0&deviceModel=1605-A01&first_channel=eyepetizer_360_market&last_channel=eyepetizer_360_market&system_version_code=23")
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
                Log.e("responseDataData", responseData);

                Message message=handler.obtainMessage();
                message.obj=responseData;
                message.what=3;
                handler.sendMessage(message);
            }
        });
    }



}
