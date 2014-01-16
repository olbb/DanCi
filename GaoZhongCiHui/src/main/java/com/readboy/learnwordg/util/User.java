package com.readboy.learnwordg.util;

import android.graphics.drawable.Drawable;

/**
 * Created by mao on 13-11-8.
 */
public class User {

    public int uid = 0;


    public String imagePath;
    public String username;
    public String realname;

    public String usergrade;
    public String time;
    public int stage;
    public int pre;

    public Drawable userimg;

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", imagePath='" + imagePath + '\'' +
                ", username='" + username + '\'' +
                ", realname='" + realname + '\'' +
                ", usergrade='" + usergrade + '\'' +
                ", time='" + time + '\'' +
                ", stage=" + stage +
                ", userimg=" + userimg +
                '}';
    }

    public void recyle() {
        if (userimg != null) {
//            userimg.recycle();
        }

    }
}
