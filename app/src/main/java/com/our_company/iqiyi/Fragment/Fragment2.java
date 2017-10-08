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

import com.our_company.iqiyi.Adapter.RecyclerviewAdapter2;
import com.our_company.iqiyi.Net.Data;
import com.our_company.iqiyi.Net.NetFashion;
import com.our_company.iqiyi.R;
import com.our_company.iqiyi.bean.ThemeInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Fragment2 extends Fragment {
	View view;
	private NetFashion net_fashion = new NetFashion();
	private List<Data> movielist=new ArrayList<>();
	private String id;
	private String title;
	private String shorttile;
	private String img;
	private String tvid;
	private String play_num;
	private String score;
	private ProgressBar progressBar;
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String responseData = (String) msg.obj;
//			parseJson(responseData);
//			init();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.view2, container, false);
		progressBar= (ProgressBar) view.findViewById(R.id.progressBar);
		progressBar.setDrawingCacheBackgroundColor(ThemeInfo.getThemeInfo().getStatusBarColor());
		if(progressBar.getVisibility()==View.GONE) progressBar.setVisibility(View.VISIBLE);
		net_fashion.setHandler(handler);
		net_fashion.getNet();
		return view;
	}

//	public void parseJson(String jData) {
//		JSONObject jsonObject = null;
//		JSONArray jsonArray = null;
//		try {
//			jsonObject = new JSONObject(jData);
//			JSONObject jsonObject1=jsonObject.getJSONObject("data");
//			jsonArray=jsonObject1.getJSONArray("video_list");
//			for (int i = 0; i < jsonArray.length(); i++) {
//				jsonObject = jsonArray.getJSONObject(i);
//				id = jsonObject.getString("id");
//				title = jsonObject.getString("title");
//				shorttile = jsonObject.getString("short_title");
//				tvid=jsonObject.getString("tv_id");
//				play_num=jsonObject.getString("play_count_text");
//				score=jsonObject.getString("sns_score");
//				img = jsonObject.getString("img");
//				img = img.substring(0, img.length() - 4) + "_480_360" + img.substring(img.length() - 4, img.length());
//				Data movie = new Data(id, title, shorttile, img,tvid,play_num,score );
//				movielist.add(movie);
//			}
//		}catch (JSONException e) {
//			e.printStackTrace();
//			Log.e("errrrrrrrrrr","eeeeeeeeee");
//
//
//
//
//			Log.e("errrrrrrrrrr","rrrrrrrrrrr");
//
//		}

//	}
  	private void init(){
		RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.rv2);

		LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(linearLayoutManager);

		RecyclerviewAdapter2 recyclerviewAdapter2=new RecyclerviewAdapter2(movielist);
		recyclerView.setAdapter(recyclerviewAdapter2);
		progressBar.setVisibility(View.GONE);
	}

}
