package com.fishbuddy.sidemenu;

/**
 * Created by android-4 on 5/31/2018.
 */

public class NavDrawerItem  {

    private String title;
    private String count = "0";
    private int image_final = 0;
    private boolean isCounterVisible = false;


    public NavDrawerItem(String title, int image){
        this.title = title;
        this.image_final =image;
    }

    public NavDrawerItem(String title, boolean isCounterVisible, String count, int image){
        this.title = title;
        this.isCounterVisible = isCounterVisible;
        this.count = count;

    }

    public String getTitle() {
        return title;
    }

    public int getimage() {
        return image_final;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }




}

