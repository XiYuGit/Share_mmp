package com.our_company.xymobile.Net;

import java.io.Serializable;

/**
 * Created by little star on 2017/6/12.
 */

public class Data implements Serializable{
    public static final int GET_ALL=1;
    public static final int GET_RECOMMEND=2;
    private String id;
    private String title;
    private String shorttile;
    private String img;
    private String aid;
    private String tv_id;
    private String description;
    private String num;
    private String play_num;
    private String score;
    private String playUrlLow;
    private String playUrlNormal;
    private String playUrlHigh;
    public Data(){}
    public Data(String id, String title, String shorttitle, String img, String tvid){
        this.id=id;
        this.title=title;
        this.shorttile=shorttitle;
        this.img=img;
        this.tv_id=tvid;
    }
    public Data(String id, String title, String shorttitle, String img, String tvid, String play_num){
        this.id=id;
        this.title=title;
        this.shorttile=shorttitle;
        this.img=img;
        this.tv_id=tvid;
        this.play_num=play_num;
    }
    public Data(String id, String title, String shorttitle, String img, String tvid, String play_num, String score){
        this.id=id;
        this.title=title;
        this.shorttile=shorttitle;
        this.img=img;
        this.tv_id=tvid;
        this.play_num=play_num;
        this.score=score;
    }

    public void setPlay_num(String play_num) {
        this.play_num = play_num;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPlayUrlLow() {
        return playUrlLow;
    }

    public void setPlayUrlLow(String playUrlLow) {
        this.playUrlLow = playUrlLow;
    }

    public String getPlayUrlNormal() {
        return playUrlNormal;
    }

    public void setPlayUrlNormal(String playUrlNormal) {
        this.playUrlNormal = playUrlNormal;
    }

    public String getPlayUrlHigh() {
        return playUrlHigh;
    }

    public void setPlayUrlHigh(String playUrlHigh) {
        this.playUrlHigh = playUrlHigh;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShorttile() {
        return shorttile;
    }

    public String getImg() {
        return img;
    }

    public String getDescription() {
        return description;
    }

    public String getNum() {
        return num;
    }

    public String getAid() {
        return aid;
    }

    public String getTv_id() {
        return tv_id;
    }

    public String getPlay_num() {
        return play_num;
    }

    public String getScore() {
        return score;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setShorttile(String shorttile) {
        this.shorttile = shorttile;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public void setTv_id(String tv_id) {
        this.tv_id = tv_id;
    }
}
