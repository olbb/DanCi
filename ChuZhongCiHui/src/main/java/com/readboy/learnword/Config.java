package com.readboy.learnword;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * Created by Mar on 13-10-7.
 */
public class Config extends Activity {

    public static int time = 30;//30 30s 20 20s 10 10s
    public static boolean type = false;//true  手写 false      键盘
    public static boolean music = true;//音效

    public static Config instance;
    RadioGroup timeline, userintype;
    RadioButton time30s, time20s, time10s, bytouch, bykeyboard;
    Button saveconfig, exitconfig;

    String tag = "LearnWord";
//    ToggleButton musicbtn;

    boolean timechanged;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        timechanged = false;
        setContentView(R.layout.config);
        init();
//        Log.i(tag, "Config onCreate");
        setconfig();
    }

    void init() {
        timeline = (RadioGroup) findViewById(R.id.timeline);
        userintype = (RadioGroup) findViewById(R.id.userintype);
        time30s = (RadioButton) findViewById(R.id.time30s);
        time20s = (RadioButton) findViewById(R.id.time20s);
        time10s = (RadioButton) findViewById(R.id.time10s);
        time10s = (RadioButton) findViewById(R.id.time10s);
        bytouch = (RadioButton) findViewById(R.id.bytouch);
        bykeyboard = (RadioButton) findViewById(R.id.bykeyboard);
        saveconfig = (Button) findViewById(R.id.saveconfig);
        exitconfig = (Button) findViewById(R.id.exitconfig);

        timeline.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                timechanged = true;
            }
        });

        saveconfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getchecked(timeline);
                getchecked(userintype);
//                music=musicbtn.isChecked();
                if (timechanged) {
                    try {
                        TestWords.instance.deadline = 0;
                    } catch (Exception e) {

                    }
                }

                finish();
            }
        });

        exitconfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Handler mh = new Handler();
        mh.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    LearnWord.instance.updatespellui();
                } catch (Exception e) {

                }
                try {
                    LearnErrorWord.instance.updatespellui();
                } catch (Exception e) {

                }
                try {
                    TestWords.instance.updatespellui();
                } catch (Exception e) {

                }
            }
        }, 100);
    }

    void setconfig() {


        switch (time) {

            case 10:
                time10s.setChecked(true);

                break;

            case 20:
                time20s.setChecked(true);
                break;

            case 30:
                time30s.setChecked(true);
                break;
        }

        if (type) {
            bytouch.setChecked(true);
        } else {
            bykeyboard.setChecked(true);
        }

    }

    public void getchecked(RadioGroup radioGroup) {

        int i = radioGroup.getCheckedRadioButtonId();
        switch (i) {
            case R.id.time10s:
                time = 10;

                break;

            case R.id.time20s:
                time = 20;
                break;

            case R.id.time30s:
                time = 30;
                break;

            case R.id.bykeyboard:
                type = false;


                break;

            case R.id.bytouch:

                type = true;
                break;

        }


    }
}
