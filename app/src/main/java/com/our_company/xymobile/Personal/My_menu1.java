package com.our_company.xymobile.Personal;

/**
 * Created by little star on 2017/3/20.
 */

public class My_menu1 {
    private String name;
    private int  imageId;
    private  int  imageId2;
    public My_menu1(String name,int imageId,int imageId2){
        this.name=name;
        this.imageId=imageId;
        this.imageId2=imageId2;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
    public int getImageId2(){
        return imageId2;
    }
}
