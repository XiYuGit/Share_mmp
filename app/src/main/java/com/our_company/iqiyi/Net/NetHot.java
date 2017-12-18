package com.our_company.iqiyi.Net;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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
 * Created by XiYu on 2017/12/12.
 */

public class NetHot {
    private android.os.Handler handler;
    public NetHot(){}
    public void setHandler(Handler handler){this.handler=handler;}

    public void getNet(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request=new Request.Builder()
                .url("http://baobab.kaiyanapp.com/api/v4/discovery/hot?udid=f5e4254be4b540419cfe8bc028c09cf5a0588e41&vc=230&vn=3.14&deviceModel=FRD-AL00&first_channel=eyepetizer_zhihuiyun_market&last_channel=eyepetizer_zhihuiyun_market&system_version_code=24")
                .build();
        Call call =okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                 getNet();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData =response.body().string();
                Message message =handler.obtainMessage();
                message.obj=responseData;
                message.what=1;
                handler.sendMessage(message);
            }
        });
    }

    public static  ArrayList<Data> parseData(String data ,int type){
        ArrayList<Data> datas =new ArrayList<>();
        Log.e("viewswitch","josns1");
        try{
            JSONObject jsonObject =new JSONObject(data);
            JSONArray jsonArray   =jsonObject.getJSONArray("itemList");
             for(int i=2;i<jsonArray.length();i++) {
                 try {
                     if (((JSONObject) jsonArray.get(i)).getString("type").length() == 10) {
                         Data data1 = new Data();
                         JSONObject jsonObject1 = ((JSONObject) jsonArray.get(i)).getJSONObject("data").getJSONObject("content").getJSONObject("data");
                         JSONObject jsonObject2 = jsonObject1.getJSONObject("cover");
                         JSONObject jsonObject3 = jsonObject1.getJSONObject("consumption");
                         JSONArray jsonObject4 = jsonObject1.getJSONArray("playInfo");
                         JSONObject jsonObject5 = (JSONObject) jsonObject4.get(0);
                         JSONArray jsonArray1 = jsonObject5.getJSONArray("urlList");
                         JSONObject jsonObject6 = (JSONObject) jsonArray1.get(0);


                         String title = jsonObject1.getString("title");
                         String playUrl = jsonObject5.getString("url");
                         String time = jsonObject1.getString("duration");
                         String img = jsonObject2.getString("feed");
                         String play_num = jsonObject3.getString("collectionCount");

                         Log.e("viewswitch", "josns2");

                         data1.setTitle(title);
                         data1.setImg(img);
                         data1.setPlayUrlLow(playUrl);
                         data1.setPlayUrlNormal(playUrl);
                         data1.setPlayUrlHigh(playUrl);
                         data1.setNum(time);
                         data1.setScore(time);
                         data1.setPlay_num(play_num);

                         datas.add(data1);
                     }
                 }catch (JSONException e)
                 {

                 }
             }
        }catch(JSONException e){
            e.printStackTrace();
        }
        Log.e("viewswitch","josns3");
        return datas;
    }

}
