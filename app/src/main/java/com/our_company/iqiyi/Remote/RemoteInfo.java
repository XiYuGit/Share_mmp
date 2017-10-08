package com.our_company.iqiyi.Remote;

/**
 * Created by miaojie on 2017/5/3.
 */

import android.graphics.Path;

import java.io.Serializable;

public class RemoteInfo implements Serializable {
    private byte[] remoteDesktop;//
    private String type;//发送端的类型
    private float coordX;//传来的X坐标
    private float coordY;//传来的Y坐标
    private int coordType;

    public float getCoordX() {
        return coordX;
    }

    public void setCoordX(float coordX) {
        this.coordX = coordX;
    }

    public float getCoordY() {
        return coordY;
    }

    public void setCoordY(float coordY) {
        this.coordY = coordY;
    }

    public int getCoordType() {
        return coordType;
    }

    public void setCoordType(int coordType) {
        this.coordType = coordType;
    }

    public RemoteInfo() {
    }

    public byte[] getRemoteDesktop() {
        return this.remoteDesktop;
    }

    public void setRemoteDesktop(byte[] remoteDesktop) {
        this.remoteDesktop = remoteDesktop;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
