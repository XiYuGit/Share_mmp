package com.our_company.xymobile.bean;

/**
 * Created by Administrator on 2017/7/2.
 */

public class ThemeInfo {
    private int statusBarColor;
    private int primaryColor;
    private static ThemeInfo currentTheme;

    public static ThemeInfo getThemeInfo()
    {
        if (currentTheme==null)
        {
            currentTheme=new ThemeInfo();
        }
        return currentTheme;
    }

    public int getStatusBarColor() {
        return statusBarColor;
    }

    public void setStatusBarColor(int statusBarColor) {
        this.statusBarColor = statusBarColor;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }
}
