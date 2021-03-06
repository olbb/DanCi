package com.readboy.learnwordg;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ReadboyTextView;

import com.readboy.encrypt.EncryptReader;
import com.readboy.learnwordg.util.User;
import com.readboy.learnwordg.util.Util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by mao on 13-11-5.
 */
public class Entrance extends Activity {

    static boolean flag = false;

    static SharedPreferences sh;
    static SharedPreferences.Editor ed;

    ReadboyTextView rt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Util.curstage == 0) {
            Util.curstage = 1;
        }
        rt = new ReadboyTextView(this);
        rt = null;
        Util.copyfile(this, "gaozhongcihui.rf4");
        Util.copyfile(this, "bcdkey.BIN");
        Util.copyfile(this, "letter.BIN");


        sh = getSharedPreferences("barrier", MODE_PRIVATE);
        ed = sh.edit();
        readbardata();


        if (!flag) {
            Util.finish();
            try {
                Barrier.instance.finish();
            } catch (Exception e) {

            }
        }


        Intent j = new Intent(Entrance.this, Barrier.class);
        startActivity(j);

        Intent i = new Intent();
        i.setClass(this, Rate.class);
        startActivity(i);
        Warn.flag = true;
        readuserinfo();
        finish();

//        bm=new BackMusic(this);
//        mh=new Handler();
//        run.run();

//

    }

//    int i=0;
//    BackMusic bm;
//    Handler mh;
//    Runnable run=new Runnable() {
//        @Override
//        public void run() {
//            i++;
//            bm.play2("chenghao"+i);
//            if(i>8)return;
//            mh.postDelayed(run,3000);
//        }
//    };

    public static final String USERINFO_PATH = "mnt/sdcard/.readboy/profile/userInfo.txt";

    public static void readuserinfo() {
        Properties props = new Properties();

        try {
            props.load(new InputStreamReader(new EncryptReader(USERINFO_PATH), "gbk"));
            Util.user = new User();
            Util.user.uid = Integer.parseInt(props.getProperty("uid"));
            Util.user.realname = props.getProperty("realname");
            Util.user.imagePath = props.getProperty("imagePath");
            Util.user.username = props.getProperty("username");

//            Log.d("LearnWord", Util.user.toString());


        } catch (IOException e) {

        }


    }


    public static void writebardata() {
        if (sh == null) {
            return;
        }

        ed.putInt("curstage", Util.curstage);
        String t1;
        for (int i = 1; i <= 46; i++) {
            t1 = "Stage" + (i) + "besttime";
            ed.putInt(t1, Util.spendtime[i]);
        }
        ed.commit();


    }

    public static void readbardata() {
        if (sh == null) {
            return;
        }
        Util.curstage = sh.getInt("curstage", 0);

        String t1;
        for (int i = 1; i <= Util.curstage; i++) {
            t1 = "Stage" + (i) + "besttime";
            Util.spendtime[i] = sh.getInt(t1, 0);
        }

    }

}
