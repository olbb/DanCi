package com.readboy.learnword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.readboy.learnword.util.BackMusic;
import com.readboy.learnword.util.Util;
import com.readboy.learnword.view.PinBar;

/**
 * Created by Mar on 13-10-8.
 */
public class Rate extends Activity {

    TextView jindu, yibei, yongshi, yiwanchen, design,chorcj;
    Button close, chuanguan;
    PinBar pinbar;

    BackMusic bm;

    public static Rate instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rate);
//          setContentView(new PinBar(this));
        instance = this;
        jindu = (TextView) findViewById(R.id.rate_jindu);
        yibei = (TextView) findViewById(R.id.rate_yibei);
        yongshi = (TextView) findViewById(R.id.rate_yongshi);
        yiwanchen = (TextView) findViewById(R.id.rate_yiwanchen);
        close = (Button) findViewById(R.id.rate_close);
        pinbar = (PinBar) findViewById(R.id.rate_pinbar);
        chuanguan = (Button) findViewById(R.id.rate_chuanguan);
        design = (TextView) findViewById(R.id.rate_design);
        bm = new BackMusic(this);

        chorcj = (TextView) findViewById(R.id.chorcj);

        if(Util.cj[Util.curstage/6]==1){
            chorcj.setText("当前称号");
        }else {
            chorcj.setText("当前成就");
        }


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        update();
        chuanguan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Rate.this, SelectWord.class);
                i.putExtra("stage", Util.curstage);
                startActivity(i);
                finish();
            }
        });
    }

    int precent = 0;
    int aimprecent;
    Handler mh;

    public void update() {
//        yiwanchen.setText(""+(Util.curstage-1)*5);
        yiwanchen.setText(100 + "");

        jindu.setText("" + (Util.curstage) + "/36");
        yibei.setText("" + (Util.curstage) * 10);
        pinbar.setPrecent(0);
//        aimprecent=Util.curstage*5-5;
//        aimprecent=180;
        design.setText(Util.getChengHao(this, Util.curstage / 6));

        yongshi.setText(totaltime());
        precent = 0;
        if (mh == null) {
            mh = new Handler();
        }
        aimprecent = (Util.curstage) * 100 / 36 + 100;
//        aimprecent=200;
        mh.postDelayed(pre, 300);
        mh.postDelayed(new Runnable() {
            @Override
            public void run() {
                bm.play("gundong");
            }
        }, 300);
//        bm.play("sucessed");


    }

    Runnable pre = new Runnable() {
        @Override
        public void run() {

            pinbar.setPrecent(precent);
            if (precent < aimprecent) {
                precent++;

                if (precent > 100) {
                    yiwanchen.setText(precent - 100 + "");
                } else {
                    yiwanchen.setText((100 - precent) + "");
                }
                mh.postDelayed(pre, 10);
            } else {
                bm.stop();
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mh.removeCallbacks(pre);
        bm.stop();
    }

    String totaltime() {
        int temp = 0;
        for (int a : Util.spendtime) {
            temp = temp + a;
        }
        int h, m;

        m = temp / 60 % 60;

        h = temp / 3600;

        if (h != 0) {
            return h + "小时" + m + "分";
        } else {
            return m + "分" + temp % 60 + "秒";
        }


    }

//    String design(){
//        String t="无";
//        if ((Util.curstage-1)/5==1){
//            t="初来乍到";
//        }else if ((Util.curstage-1)/5==2){
//            t="锋芒乍现";
//        }else if ((Util.curstage-1)/5==3){
//            t="小有成就";
//        }else if ((Util.curstage-1)/5==4){
//            t="声名显赫";
//        }
//        return t;
//    }


}