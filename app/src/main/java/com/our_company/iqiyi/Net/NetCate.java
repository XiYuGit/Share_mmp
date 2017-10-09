package com.our_company.iqiyi.Net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by little star on 2017/6/15.
 */

public class NetCate {
    private Handler handler;
    public NetCate() {}
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void getNet(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://baobab.kaiyanapp.com/api/v4/categories/videoList?start=10&num=10&strategy=date&id=4&udid=b37032bcc61348928fdf375011361a471a98cce5&vc=225&vn=3.12.0&deviceModel=1605-A01&first_channel=eyepetizer_360_market&last_channel=eyepetizer_360_market&system_version_code=23")
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
                message.what=4;
                handler.sendMessage(message);
            }
        });
    }
    public static ArrayList<Data>parseData(String data,int type)
    {

        ArrayList<Data>datas=new ArrayList<>();
        try {
            JSONObject jsonObject=new JSONObject(data);
            JSONArray itemList=jsonObject.getJSONArray("itemList");
            int border;
            if(type==Data.GET_ALL)
                border=itemList.length();
            else
                border=4;
            for (int i=0;i<border;i++)
            {
                Data temp=new Data();

                JSONObject item=itemList.getJSONObject(i).getJSONObject("data");
                JSONObject header=item.getJSONObject("header");
                JSONObject content=item.getJSONObject("content").getJSONObject("data");

                temp.setShorttile(header.getString("title"));
                temp.setImg(header.getString("icon"));
                temp.setTitle(content.getJSONObject("author").getString("description"));
                JSONArray urlList=content.getJSONArray("playInfo");
                switch (urlList.length())
                {
                    case 1:
                        temp.setPlayUrlLow(urlList.getJSONObject(0).getString("url"));
                        break;
                    case 2:
                        temp.setPlayUrlLow(urlList.getJSONObject(0).getString("url"));
                        temp.setPlayUrlNormal(urlList.getJSONObject(1).getString("url"));
                        break;
                    case 3:
                        temp.setPlayUrlLow(urlList.getJSONObject(0).getString("url"));
                        temp.setPlayUrlNormal(urlList.getJSONObject(1).getString("url"));
                        temp.setPlayUrlHigh(urlList.getJSONObject(2).getString("url"));
                        break;
                }
                temp.setNum(content.getJSONObject("consumption").getInt("shareCount")+"");
                temp.setPlay_num(content.getJSONObject("consumption").getInt("collectionCount")+"");
                datas.add(temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("json",e.toString());
        }
        return datas;
    }

}
