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

import com.our_company.iqiyi.Adapter.RecyclerviewAdapter3;
import com.our_company.iqiyi.Net.Data;
import com.our_company.iqiyi.Net.NetCate;
import com.our_company.iqiyi.Net.NetExercise;
import com.our_company.iqiyi.R;
import com.our_company.iqiyi.bean.ThemeInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Fragment3 extends Fragment {
	View view;
	private NetExercise net_movie = new NetExercise();
	private List<Data> exerciseList;
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
			exerciseList= NetExercise.parseData(responseData,Data.GET_ALL);
//			parseJson(responseData);
			init();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		view = inflater.inflate(R.layout.view3, container, false);
		progressBar= (ProgressBar) view.findViewById(R.id.progressBar);
		progressBar.setDrawingCacheBackgroundColor(ThemeInfo.getThemeInfo().getStatusBarColor());
		if(progressBar.getVisibility()==View.GONE) progressBar.setVisibility(View.VISIBLE);
		net_movie.setHandler(handler);
		net_movie.getNet();
		return view;
	}

	private void init(){
		RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.rv3);

		LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(linearLayoutManager);

		RecyclerviewAdapter3 recyclerviewAdapter3=new RecyclerviewAdapter3(exerciseList);
		recyclerView.setAdapter(recyclerviewAdapter3);
		progressBar.setVisibility(View.GONE);
	}
}
