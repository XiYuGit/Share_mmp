package com.our_company.xymobile.Fragment;
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

import com.our_company.xymobile.Adapter.RecyclerviewAdapter1_switch;
import com.our_company.xymobile.Net.Data;
import com.our_company.xymobile.Net.NetHot;
import com.our_company.xymobile.Net.NetPet;
import com.our_company.xymobile.R;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends Fragment {
	private View view;
	//private List <RecyclerviewAdapter1> recyclerviewAdapter1List=new ArrayList<>();
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
					Log.e("listSIe=ze",hotList.size()+"");
					if(hotList.size()>4&&hotList.size()-4>4) {
						for (int i = 0; i <4; i++) {
							imgUrl[i] = hotList.get(hotList.size()-i-4).getImg();
							imgPlayUrl[i] = hotList.get(hotList.size()-i-4).getPlayUrlHigh();
						}
						setImage(imgUrl);
					}
					break;
			}
		}
	};

	public Handler handler1=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			init();
		}
	};

	public Fragment1(){}

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
		NetHot netHot =new NetHot();
		netHot.setHandler(handler);
		netHot.getNet();
	}

	void  init(){
		RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.rcv);
		LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(layoutManager);
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
