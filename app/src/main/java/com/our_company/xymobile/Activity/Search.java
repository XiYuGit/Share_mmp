package com.our_company.xymobile.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.our_company.xymobile.Adapter.SearchRecyclerView;
import com.our_company.xymobile.Net.Data;
import com.our_company.xymobile.R;
import com.our_company.xymobile.bean.ThemeInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by little star on 2017/6/17.
 */

public class Search extends AppCompatActivity {
    private String id;
    private String title;
    private String shorttile;
    private String img;
    private String string;
    private String tvid;
    private EditText editText;
    private ImageView imageView;
    private ImageView imageBack;
    private List<Data> movielist=new ArrayList<>();
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    SearchRecyclerView searchRecyclerView;
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String responseData= (String) msg.obj;
            Log.e("test",responseData);
            parseJson(responseData);
            init();
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        imageBack= (ImageView) findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar= (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setBackgroundColor(ThemeInfo.getThemeInfo().getPrimaryColor());
        getWindow().setStatusBarColor(ThemeInfo.getThemeInfo().getStatusBarColor());

        editText= (EditText) findViewById(R.id.edit_text);
        imageView= (ImageView) findViewById(R.id.image);
        recyclerView= (RecyclerView) findViewById(R.id.srv);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(editText.getText()!=null){
                     string=editText.getText().toString();
                     Log.e("string",string);
                //     searchRecyclerView.notifyDataSetChanged();
                     getNet();
                }
            }
        });
    }

    private void init(){

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    //    linearLayoutManager.removeAllViews();
    //
        SearchRecyclerView searchRecyclerView=new SearchRecyclerView(movielist);
        recyclerView.setAdapter(searchRecyclerView);

    }

    public void getNet(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://iface.qiyi.com/openapi/batch/search?key="+string+"4&from=mobile_list&page_size=30&version=7.5&app_k=f0f6c3ee5709615310c0f053dc9c65f2&app_v=8.4&app_t=0&platform_id=12&dev_os=10.3.1&dev_ua=iPhone9,3&dev_hw=%7B%22cpu%22%3A0%2C%22gpu%22%3A%22%22%2C%22mem%22%3A%2250.4MB%22%7D&net_sts=1&scrn_sts=1&scrn_res=1334*750&scrn_dpi=153600&qyid=87390BD2-DACE-497B-9CD4-2FD14354B2A4&secure_v=1&secure_p=iPhone&core=1&req_sn=1493946331320&req_times=1")
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("info_callFailure", e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.e("responseDataData", responseData);

                Message message=handler.obtainMessage();
                message.obj=responseData;
                handler.sendMessage(message);
            }
        });
    }

    public void parseJson(String jData) {
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            jsonObject = new JSONObject(jData);
            //JSONObject jsonObject1=jsonObject.getJSONObject("data");
            jsonArray=jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                id = jsonObject.getString("id");
                title = jsonObject.getString("title");
                shorttile = jsonObject.getString("short_title");
                img = jsonObject.getString("img");
                tvid=jsonObject.getString("tv_id");
                img = img.substring(0, img.length() - 4) + "_480_360" + img.substring(img.length() - 4, img.length());
                Data movie = new Data(id, title, shorttile, img,tvid);
                movielist.add(movie);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
