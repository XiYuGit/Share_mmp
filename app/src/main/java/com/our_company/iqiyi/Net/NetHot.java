package com.our_company.iqiyi.Net;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
    NetHot(){}
    public void setHandler(Handler handler){this.handler=handler;}

    public void getNet(){
        OkHttpClient okHttpClient;
        Request request=new Request;
        request.newBuilder()
                .url("http://baobab.kaiyanapp.com/api/v4/discovery/hot")
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
                message.what=2;
                handler.sendMessage(message);

            }
        });
    }

    public class ArrayList<Data> parseData(String data,int type){
        ArrayList<Data> datas =new ArrayList<>();
        try{
            JSONObject jsonObject =new JSONObject(data);
            JSONArray jsonArray   =new JSONArray("itemList");
             for(int i=2;i<jsonArray.length();i++){.
                 Data data1 =new Data();
                 JSONObject jsonObject1 =((JSONObject)jsonArray.get(i)).getJSONObject("data");
                 JSONObject jsonObject2 =jsonObject1.getJSONObject("cover");



             }







        }catch(JSONException e){
            e.printStackTrace();
        }

    }

}
