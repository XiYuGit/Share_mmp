package com.our_company.iqiyi.Fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.our_company.iqiyi.Adapter.RecyclerviewAdapter1;
import com.our_company.iqiyi.Net.Data;
import com.our_company.iqiyi.Net.NetPet;
import com.our_company.iqiyi.Net.NetExercise;
import com.our_company.iqiyi.Net.NetFashion;
import com.our_company.iqiyi.Net.NetCate;
import com.our_company.iqiyi.R;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends Fragment {
	private View view;
	private List<RecyclerviewAdapter1>recyclerviewAdapter1List=new ArrayList<>();
	private Context context;
	private Bitmap[] bm=new Bitmap[5];
	private String[] imgUrl= new String[6];
	private String id=null;
	private String title=null;
	private String shorttile=null;
	private String img=null;
	private String tvid=null;
	private String play_num=null;
	private List<Data>pic_list;
	private List<Data>cateList=new ArrayList<>();
	private List<Data>exerciseList=new ArrayList<>();
	private NetPet netFirst=new NetPet();
	private List<Data>fashionList=new ArrayList<>();
	private List<Data>petList=new ArrayList<>();
	private ProgressBar progressBar;
	private View parentView=null;
	public Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String responseData= (String) msg.obj;
			switch (msg.what)
			{
				case 1:
					petList=NetCate.parseData(responseData,Data.GET_RECOMMEND);
					break;
				case 2:
					exerciseList=NetCate.parseData(responseData,Data.GET_RECOMMEND);
					break;
				case 3:
					fashionList=NetCate.parseData(responseData,Data.GET_RECOMMEND);
					break;
				case 4:
					cateList=NetCate.parseData(responseData,Data.GET_RECOMMEND);
					break;
			}
			Log.e("size",cateList.size()+"-"+exerciseList.size()+"-"+fashionList.size()+"-"+petList.size());
			if(cateList.size()!=0&&exerciseList.size()!=0
					&&fashionList.size()!=0&&petList.size()!=0)
			{
				Log.e("初始化","111");
				setImage(imgUrl);

			}
		}
	};
	public Handler handler1=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			init();
		}
	};
	public Fragment1(){

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (parentView == null) {

			view=inflater.inflate(R.layout.view1,container,false);
			progressBar= (ProgressBar) view.findViewById(R.id.progressBar);
			if(progressBar.getVisibility()==View.GONE) progressBar.setVisibility(View.VISIBLE);
			getInfo();


		} else {
			ViewGroup viewGroup = (ViewGroup) parentView.getParent();
			if (viewGroup != null)
				viewGroup.removeView(parentView);
		}
		return	view;
	}

	private void getInfo(){
		netFirst.setHandler(handler);
		netFirst.getNet();
		NetExercise net_exercise =new NetExercise();
		net_exercise.setHandler(handler);
		net_exercise.getNet();

		NetFashion net_fashion =new NetFashion();
		net_fashion.setHandler(handler);
		net_fashion.getNet();

		NetCate net_cate =new NetCate();
		net_cate.setHandler(handler);
		net_cate.getNet();

		imgUrl[0]="http://img.kaiyanapp.com/29ee466fbbf8e1677811ffa7e34acdcc.jpeg?imageMogr2/quality/60/format/jpg";
		imgUrl[1]="http://img.kaiyanapp.com/654fe9e1ef7293b3e3a95a9b2118a045.jpeg?imageMogr2/quality/60/format/jpg";
		imgUrl[2]="http://img.kaiyanapp.com/2702dc22d19020ea06a03fd8fbe506a9.png?imageMogr2/quality/60/format/jpg";
		imgUrl[3]="http://img.kaiyanapp.com/ed5b31dc5664914665c054727c09a735.png?imageMogr2/quality/60/format/jpg";
		imgUrl[4]="http://img.kaiyanapp.com/3e99e33cf55d3c442573681e727dce10.jpeg?imageMogr2/quality/60/format/jpg";
		imgUrl[5]="http://img.kaiyanapp.com/dd6ff5fa783d48a3ee91c33a77ed18d9.png?imageMogr2/quality/60/format/jpg";
	}

	void  init(){
		RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.rcv);


		LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(layoutManager);

		RecyclerviewAdapter1 recyclerviewAdapter1=new RecyclerviewAdapter1(bm,fashionList,exerciseList,petList,cateList);
		recyclerView.setAdapter(recyclerviewAdapter1);

		progressBar.setVisibility(View.GONE);

	}
//	public void parseJson(String jData) {
//		JSONObject jsonObject = null;
//		JSONArray jsonArray = null;
//		try {
//			jsonObject = new JSONObject(jData);
//			jsonArray = jsonObject.getJSONArray("data");
//
//			jsonObject = jsonArray.getJSONObject(0);
//
//			jsonArray = jsonObject.getJSONArray("video_list");
//			for (int i = 0; i < 5; i++) {
//				jsonObject = jsonArray.getJSONObject(i);
//				imgUrl[i] = jsonObject.getString("img");
//			}
//
//			jsonObject = new JSONObject(jData);
//			jsonArray = jsonObject.getJSONArray("data");
//			jsonObject = jsonArray.getJSONObject(1);
//			jsonArray = jsonObject.getJSONArray("video_list");
//			for (int i = 0; i < 4; i++) {
//				jsonObject = jsonArray.getJSONObject(i);
//				id = jsonObject.getString("id");
//				title = jsonObject.getString("title");
//				shorttile = jsonObject.getString("short_title");
//				tvid=jsonObject.getString("tv_id");
//				play_num=jsonObject.getString("play_count_text");
//				img = jsonObject.getString("img");
//				img = img.substring(0, img.length() - 4) + "_480_360" + img.substring(img.length() - 4, img.length());
//				Data zixun = new Data(id, title, shorttile, img,tvid,play_num);
//				zixun_list.add(zixun);
//			}
//
//			jsonObject = new JSONObject(jData);
//			jsonArray = jsonObject.getJSONArray("data");
//			jsonObject = jsonArray.getJSONObject(2);
//			jsonArray = jsonObject.getJSONArray("video_list");
//			for (int i = 0; i < 4; i++) {
//				jsonObject = jsonArray.getJSONObject(i);
//				id = jsonObject.getString("id");
//				title = jsonObject.getString("title");
//				shorttile = jsonObject.getString("short_title");
//				img = jsonObject.getString("img");
//				play_num=jsonObject.getString("play_count_text");
//				tvid=jsonObject.getString("tv_id");
//				img = img.substring(0, img.length() - 4) + "_480_360" + img.substring(img.length() - 4, img.length());
//				Data dianshiju = new Data(id, title, shorttile, img,tvid,play_num);
//				dianshiju_list.add(dianshiju);
//			}
//
//			jsonObject = new JSONObject(jData);
//			jsonArray = jsonObject.getJSONArray("data");
//			jsonObject = jsonArray.getJSONObject(3);
//			jsonArray = jsonObject.getJSONArray("video_list");
//			for (int i = 0; i < 4; i++) {
//				jsonObject = jsonArray.getJSONObject(i);
//				id = jsonObject.getString("id");
//				title = jsonObject.getString("title");
//				shorttile = jsonObject.getString("short_title");
//				img = jsonObject.getString("img");
//				play_num=jsonObject.getString("play_count_text");
//				tvid=jsonObject.getString("tv_id");
//				img = img.substring(0, img.length() - 4) + "_480_360" + img.substring(img.length() - 4, img.length());
//				Data movie = new Data(id, title, shorttile, img,tvid,play_num);
//				movie_list.add(movie);
//				}
//
//			jsonObject = new JSONObject(jData);
//			jsonArray = jsonObject.getJSONArray("data");
//			jsonObject = jsonArray.getJSONObject(4);
//			jsonArray = jsonObject.getJSONArray("video_list");
//			for (int i = 0; i < 4; i++) {
//				jsonObject = jsonArray.getJSONObject(i);
//				id = jsonObject.getString("id");
//				title = jsonObject.getString("title");
//				shorttile = jsonObject.getString("short_title");
//				img = jsonObject.getString("img");
//				play_num=jsonObject.getString("play_count_text");
//				tvid=jsonObject.getString("tv_id");
//				img = img.substring(0, img.length() - 4) + "_480_360" + img.substring(img.length() - 4, img.length());
//				Data zongyi = new Data(id, title, shorttile, img,tvid,play_num);
//				zongyi_list.add(zongyi);
//			}
//
//		}catch (JSONException e) {
//
//			Log.e("errrrrrrrrrr","eeeeeeeeeeeeee");
//
//
//			Log.e("errrrrrrrrrr", "rrrrrrrrrrr");
//
//			e.printStackTrace();
//		}
//	}

	public void setImage(final String [] imgUrl){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for(int i=0;i<5;i++) {
						URL iconUrl = new URL(imgUrl[i]);
						URLConnection conn = iconUrl.openConnection();
						HttpURLConnection http = (HttpURLConnection) conn;
						int length = http.getContentLength();
						conn.connect();
						// 获得图像的字符流
						InputStream is = conn.getInputStream();
						BufferedInputStream bis = new BufferedInputStream(is, length);
						bm[i] = BitmapFactory.decodeStream(bis);
						bis.close();
						is.close();// 关闭流
					}
					Message message=handler1.obtainMessage();
					message.obj=bm;
					handler1.sendMessage(message);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

}
