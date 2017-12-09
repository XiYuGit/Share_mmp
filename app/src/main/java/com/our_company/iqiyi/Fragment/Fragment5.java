package com.our_company.iqiyi.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.our_company.iqiyi.Adapter.RecyclerviewAdapter4;
import com.our_company.iqiyi.Adapter.RecyclerviewAdapter5;
import com.our_company.iqiyi.Net.Data;
import com.our_company.iqiyi.Net.NetExercise;
import com.our_company.iqiyi.Net.NetJoke;
import com.our_company.iqiyi.Net.NetPet;
import com.our_company.iqiyi.R;
import com.our_company.iqiyi.bean.ThemeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by little star on 2017/5/27.
 */

public class Fragment5 extends Fragment {
    View view;
    private NetJoke net_movie = new NetJoke();
    private List<Data> cateList=new ArrayList<>();
    private String id;
    private String title;
    private String shorttile;
    private String img;
    private String play_num;
    private String score;
    private ProgressBar progressBar;
    String tvid;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String responseData = (String) msg.obj;
          //  Log.e("eeeeeeeeeeeeee",responseData);
            cateList= NetJoke.parseData(responseData, Data.GET_ALL);
            init();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.view5, container, false);
        progressBar= (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setDrawingCacheBackgroundColor(ThemeInfo.getThemeInfo().getStatusBarColor());
        if(progressBar.getVisibility()==View.GONE) progressBar.setVisibility(View.VISIBLE);
        net_movie.setHandler(handler);
        net_movie.getNet();
        return view;
    }
    private void init(){
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.rv4);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        RecyclerviewAdapter5 recyclerviewAdapter5=new RecyclerviewAdapter5(cateList);
        recyclerView.setAdapter(recyclerviewAdapter5);
        progressBar.setVisibility(View.GONE);
    }

}
