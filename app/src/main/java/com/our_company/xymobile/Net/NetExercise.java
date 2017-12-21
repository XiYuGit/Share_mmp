package com.our_company.xymobile.Net;

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

/*
 * Created by little star on 2017/6/15.
 */

public class NetExercise {
    private Handler handler;
    public NetExercise() {}
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void getNet(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://baobab.kaiyanapp.com/api/v4/categories/videoList?start=10&num=10&strategy=date&id=18&udid=b37032bcc61348928fdf375011361a471a98cce5&vc=225&vn=3.12.0&deviceModel=1605-A01&first_channel=eyepetizer_360_market&last_channel=eyepetizer_360_market&system_version_code=23")
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
                message.what=2;
                handler.sendMessage(message);
            }
        });
    }


    public static ArrayList<Data> parseData(String data, int type){
        ArrayList<Data>datas=new ArrayList<>();
        try {

            JSONObject jsonObject=new JSONObject(data);
            JSONArray itemList=jsonObject.getJSONArray("itemList");
            for(int i=1;i<itemList.length();i++){
                try {
                    Data data1 = new Data();
                    JSONObject jsonObjectContnet = ((JSONObject) itemList.get(i)).getJSONObject("data").getJSONObject("content");

                    JSONObject jsonObjectp = jsonObjectContnet.getJSONObject("data");
                    JSONObject jsonObjectImg = jsonObjectp.getJSONObject("cover");
                    JSONArray jsonObjectUrl = jsonObjectp.getJSONArray("playInfo");
                    JSONObject jsonObject5 = (JSONObject) jsonObjectUrl.get(0);
                    //JSONArray jsonArray1 =jsonObject5.getJSONArray("urlList");

                    String title = jsonObjectp.getString("title");
                    //  String playUrl=jsonObjectp.getString("playUrl");
                    String playUrl = jsonObject5.getString("url");
                    String time = jsonObjectp.getString("duration");
                    String img = jsonObjectImg.getString("feed");

                    Log.e("eeeeeeeeeee", title);

                    data1.setTitle(title);
                    data1.setImg(img);
                    data1.setPlayUrlLow(playUrl);
                    data1.setPlayUrlNormal(playUrl);
                    data1.setPlayUrlHigh(playUrl);
                    data1.setNum(time);
                    data1.setScore(time);
                    data1.setPlay_num(time);

                    datas.add(data1);
                }catch(JSONException e){

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("json",e.toString());
        }
        return datas;

    }

}