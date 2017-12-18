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
import com.our_company.iqiyi.Adapter.RecyclerviewAdapter1_switch;
import com.our_company.iqiyi.Net.Data;
import com.our_company.iqiyi.Net.NetHot;
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
import java.util.HashMap;
import java.util.List;

public class Fragment1 extends Fragment {
	private View view;
	private List <RecyclerviewAdapter1> recyclerviewAdapter1List=new ArrayList<>();
	private Context context;
	private Bitmap[] bm=new Bitmap[5];
	private String[] imgUrl= new String[6];
	private String[] imgPlayUrl= new String[6];
	private String id=null;
	private String title=null;
	private String shorttile=null;
	private String img=null;
	private String tvid=null;
	private String play_num=null;
	private List<Data>hotList=new ArrayList<>();
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
					hotList=NetHot.parseData(responseData,Data.GET_RECOMMEND);
					Log.e("handlerrrr",hotList.size()+"");
					if(hotList.size()>4) {
						int n=hotList.size()-4<4?hotList .size()-4:4;
						for (int i = 0; i < n; i++) {
							imgUrl[i] = hotList.get(hotList.size()-i-4).getImg();
							imgPlayUrl[i] = hotList.get(hotList.size()-i-4).getPlayUrlHigh();
						}
						setImage(imgUrl);
					}
//					petList=NetCate.parseData(responseData,Data.GET_RECOMMEND);
					break;
//				case 2:
//					exerciseList=NetCate.parseData(responseData,Data.GET_RECOMMEND);
//					break;
//				case 3:
//					fashionList=NetCate.parseData(responseData,Data.GET_RECOMMEND);
//					break;
//				case 4:
//					cateList=NetCate.parseData(responseData,Data.GET_RECOMMEND);
//					break;
			}
//			if(cateList.size()!=0&&exerciseList.size()!=0 &&fashionList.size()!=0&&petList.size()!=0)
//			{
//				setImage(imgUrl);
//			}
		}
	};
	public Handler handler1=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Log.e("views","end2");
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
			//init();
		} else {
			ViewGroup viewGroup = (ViewGroup) parentView.getParent();
			if (viewGroup != null)
				viewGroup.removeView(parentView);
		}
		return	view;
	}

	private void getInfo(){


		NetHot netHot =new NetHot();
		netHot.setHandler(handler);
		netHot.getNet();

		//netFirst.setHandler(handler);	//数据写死
		//netFirst.getNet();

//		NetExercise net_exercise =new NetExercise();
//		net_exercise.setHandler(handler);
//		net_exercise.getNet();
//
//		NetFashion net_fashion =new NetFashion();
//		net_fashion.setHandler(handler);
//		net_fashion.getNet();
//
//		NetCate net_cate =new NetCate();
//		net_cate.setHandler(handler);
//		net_cate.getNet();
	}

	void  init(){

		RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.rcv);

		LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(layoutManager);

//		RecyclerviewAdapter1 recyclerviewAdapter1=new RecyclerviewAdapter1(bm,fashionList,exerciseList,petList,cateList);
//		recyclerView.setAdapter(recyclerviewAdapter1);
		RecyclerviewAdapter1_switch recyclerviewAdapter1=new RecyclerviewAdapter1_switch(bm,hotList,exerciseList,petList,cateList);
		recyclerView.setAdapter(recyclerviewAdapter1);

		progressBar.setVisibility(View.GONE);

	}

	public void setImage(final String [] imgUrl){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for(int i=0;i<4;i++) {
						Log.e("views","url::::"+imgUrl[i]);
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
					Log.e("views","end");
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
