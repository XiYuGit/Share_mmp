package com.our_company.iqiyi.Personal.Adapter;

import java.util.PriorityQueue;

/**
 * Created by little star on 2017/4/30.
 */

public class MySend_Data {

    private String name;
    private String money;
    private String time;
    private String time2;
    private String state;
    private String state2;
    private int imageId;
    public MySend_Data(String name,String money,String time2,String time ,String state2,String state,int imageId) {
        this.name = name;
        this.money=money;
        this.time = time;
        this.state = state;
        this.imageId = imageId;
        this.time2=time2;
        this.state2=state2;
    }
    public String getName(){return name;}
    public String getMoney(){return money;}
    public String getTime(){return time;}
    public String getTime2(){return time2;}
    public String getState(){return state;}
    public String getState2(){return state2;}
    public int getImageId() {return imageId;}

}
