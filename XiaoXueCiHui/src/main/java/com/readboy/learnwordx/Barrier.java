package com.readboy.learnwordx;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.readboy.encrypt.EncryptReader;
import com.readboy.learnwordx.util.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


public class Barrier extends Activity implements View.OnClickListener {

    //关卡选择界面
    String TAG = "LearnWord";
//	GridView grid;

    Button close, wrongwords, myrates, board;

    public static Barrier instance;


    HorizontalScrollView stage_scroll;
    LinearLayout linear4;

    //颜色 75 172 198  //4b ac c6

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        instance = this;

        int tempstage;

        SharedPreferences sh = getSharedPreferences("barrier", MODE_PRIVATE);
        tempstage = sh.getInt("curstage", 0);

        String t1;

        if (tempstage > Util.curstage) {
            Util.curstage = tempstage;
        }
//        if(Util.curstage==0){
//            Util.curstage=1;
//        }
        for (int i = 1; i <= Util.curstage; i++) {
            t1 = "Stage" + (i) + "besttime";
            Util.spendtime[i] = sh.getInt(t1, 0);
        }


//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); //声明使用自定义标题 
//		Log.i(TAG, "进入关卡选择界面");
        setContentView(R.layout.barrier);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);//自定义布局赋值 

//		grid=(GridView)findViewById(R.id.barriers);

//		barad=new BarrierAdapter(this);
//		grid.setAdapter(barad);
//		grid.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
////				System.out.println(position);
//				Intent i=new Intent(Barrier.this, SelectWord.class);
//				i.putExtra("stage", position+1);
//                Util.stage=position+1;
//				startActivity(i);
//			}
//		});

//        Handler mh=new Handler();
//        mh.post(new Runnable() {
//            @Override
//            public void run() {
//              Util.re=new RecordEvent(getApplicationContext());
//            }
//        });语音相关        暂时屏蔽
        myrates = (Button) findViewById(R.id.myrates);
        close = (Button) findViewById(R.id.close);
        wrongwords = (Button) findViewById(R.id.wrongwordsbook);
        close.setOnClickListener(this);
        wrongwords.setOnClickListener(this);
        myrates.setOnClickListener(this);
        board = (Button) findViewById(R.id.rankinglist);
        board.setOnClickListener(this);
//        grid.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent e) {
//                if(e.getPointerCount()>1){
//                    return true;
//                }else {
//                    return false;
//                }
//
//
//            }
//        });

//        stage_scroll=(HorizontalScrollView)findViewById(R.id.barrier_stage_scroll);
//        linear4=(LinearLayout)findViewById(R.id.barrier_stage_liner4);
//        stage_scroll.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction()==MotionEvent.ACTION_DOWN){
//                    motiontemp=(int)event.getX();
//                }
//                if(event.getAction()==MotionEvent.ACTION_UP){
//                    motiontemp=0;
//                }
//
//                if(event.getAction()==MotionEvent.ACTION_MOVE){
//                    int a[]=new int[2];
//                    v17.getLocationInWindow(a);
//                    System.out.println("a[0]<1280"+(a[0]<1280));
//                    System.out.println("event.getX()-motiontemp"+(event.getX()-motiontemp));
//                    System.out.println("linertemplength"+linertemplength);
//
//
//                    if(a[0]<1280&&(event.getX()-motiontemp)<0&&linertemplength>60){
//                        LinearLayout.LayoutParams lp=new
//                                LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                        lp.setMargins(0,0,linertemplength--,0);
//                        linear4.setLayoutParams(lp);
//                    }else if(a[0]<1280&&(event.getX()-motiontemp)>0){
//                        LinearLayout.LayoutParams lp=new
//                                LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        lp.setMargins(0,0,linertemplength++,0);
//                        linear4.setLayoutParams(lp);
//                    }
//                }
//
//                return false;
//            }
//        });

        final IntentFilter homeFilter = new IntentFilter(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeReceiver, homeFilter);

    }

    BroadcastReceiver homeReceiver = new BroadcastReceiver() {

        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null
                        && reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
//                        Log.e(TAG, "SYSTEM_DIALOG_REASON_HOME_KEY");
                    // 监听home事件
                    Handler mh = new Handler();
                    mh.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Util.finish();
                            finish();
                        }
                    }, 200);


                }
            }
        }
    };

//    int linertemplength=212;
//    int motiontemp;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;


            case R.id.wrongwordsbook:
                Intent i = new Intent(this, ErrorWord.class);
                startActivity(i);
                break;

            case R.id.myrates:
                Intent ii = new Intent(this, Rate.class);
                startActivity(ii);
                break;


            case R.id.rankinglist:
                File f = new File("mnt/sdcard/.readboy/profile/userInfo");
                Intent j = new Intent();
                if (!f.exists()) {
                    j.setClass(this, WarnZhuCe.class);
                } else {
                    Properties props = new Properties();
                    try {

                        props.load(new InputStreamReader(new EncryptReader(Board.USERINFO_PATH), "gbk"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (Integer.parseInt(props.getProperty("uid")) != 0) {
                        j.setClass(this, Board.class);
                    } else {
                        j.setClass(this, WarnZhuCe.class);
                    }

                }
//                j.setClass(this,Board.class);
                startActivity(j);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        barad.notifyDataSetChanged();
        loadstage();
        Resources res = getResources();
        Configuration c = res.getConfiguration();
        c.fontScale = 1;
        res.updateConfiguration(c, res.getDisplayMetrics());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Entrance.writebardata();
//        System.exit(0);
//        Log.d("LearnWord","LearnWord Finshed");
        Intent i = new Intent();
        i.setAction("UPDATE_LearnWord_Widget");
        sendBroadcast(i);
        unregisterReceiver(homeReceiver);
    }

    View v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20;
    View v21, v22, v23, v24, v25, v26, v27, v28, v29, v30, v31, v32, v33, v34, v35, v36;

    void loadstage() {
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        v3 = findViewById(R.id.v3);
        v4 = findViewById(R.id.v4);
        v5 = findViewById(R.id.v5);
        v6 = findViewById(R.id.v6);
        v7 = findViewById(R.id.v7);
        v8 = findViewById(R.id.v8);
        v9 = findViewById(R.id.v9);
        v10 = findViewById(R.id.v10);
        v11 = findViewById(R.id.v11);
        v12 = findViewById(R.id.v12);
        v13 = findViewById(R.id.v13);
        v14 = findViewById(R.id.v14);
        v15 = findViewById(R.id.v15);
        v16 = findViewById(R.id.v16);
        v17 = findViewById(R.id.v17);
        v18 = findViewById(R.id.v18);
        v19 = findViewById(R.id.v19);
        v20 = findViewById(R.id.v20);
        v21 = findViewById(R.id.v21);
        v22 = findViewById(R.id.v22);
        v23 = findViewById(R.id.v23);
        v24 = findViewById(R.id.v24);
        v25 = findViewById(R.id.v25);
        v26 = findViewById(R.id.v26);
        v27 = findViewById(R.id.v27);
        v28 = findViewById(R.id.v28);
        v29 = findViewById(R.id.v29);
        v30 = findViewById(R.id.v30);
        v31 = findViewById(R.id.v31);
        v32 = findViewById(R.id.v32);
        v33 = findViewById(R.id.v33);
        v34 = findViewById(R.id.v34);
        v35 = findViewById(R.id.v35);
        v36 = findViewById(R.id.v36);


        setstage(v1, 1);
        setstage(v2, 2);
        setstage(v3, 3);
        setstage(v4, 4);
        setstage(v5, 5);
        setstage(v6, 6);
        setstage(v7, 7);
        setstage(v8, 8);
        setstage(v9, 9);
        setstage(v10, 10);
        setstage(v11, 11);
        setstage(v12, 12);
        setstage(v13, 13);
        setstage(v14, 14);
        setstage(v15, 15);
        setstage(v16, 16);
        setstage(v17, 17);
        setstage(v18, 18);
        setstage(v19, 19);
        setstage(v20, 20);
        setstage(v21, 21);
        setstage(v22, 22);
        setstage(v23, 23);
        setstage(v24, 24);
        setstage(v25, 25);
        setstage(v26, 26);
        setstage(v27, 27);
        setstage(v28, 28);
        setstage(v29, 29);
        setstage(v30, 30);
        setstage(v31, 31);
        setstage(v32, 32);
        setstage(v33, 33);
        setstage(v34, 34);
        setstage(v35, 35);
        setstage(v36, 36);
    }

    TextView stagename, stagenum;
    ImageView gift, locked;
    RatingBar rate;

    void setstage(View view, final int position) {
        if (position <= Util.curstage + 1) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Barrier.this, SelectWord.class);
                    i.putExtra("stage", position);
                    Util.stage = position;
                    startActivity(i);
//                   if(Warn.flag==false){
//                       i=new Intent(Barrier.this,Warn.class);
//                       startActivity(i);
//                   }
                }
            });
        } else {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                   toptoast();
                    Intent i = new Intent(Barrier.this, Warn.class);
                    startActivity(i);
                }
            });
        }

        stagename = (TextView) view.findViewById(R.id.stagename);
        rate = (RatingBar) view.findViewById(R.id.rate);
        stagenum = (TextView) view.findViewById(R.id.stagenum);
        gift = (ImageView) view.findViewById(R.id.stage_gift);
        locked = (ImageView) view.findViewById(R.id.stage_locked);

        stagename.setText("第" + Util.toCHS(position) + "关");
        stagenum.setText(position + "");
        if ((position) % 6 == 0) {
            gift.setVisibility(View.VISIBLE);
        } else {
            gift.setVisibility(View.GONE);
        }

        if (position > Util.curstage + 1) {
            locked.setVisibility(View.VISIBLE);
            stagenum.setVisibility(View.GONE);
        } else {
            locked.setVisibility(View.GONE);
            stagenum.setVisibility(View.VISIBLE);
        }

        if (Util.spendtime[position] == 0) {
            rate.setRating(0);
        } else {
            rate.setRating(rate(Util.spendtime[position]));
        }


    }

    int rate(int time) {
        int min = time / 60;
        int second = time % 60;
        int rate;
        if (time == 0) {
            return 0;
        }
        if (min < 3) {
            rate = 5;
        } else if (min < 6) {
            rate = 4;
        } else if (min < 8) {
            rate = 3;
        } else if (min < 10) {
            rate = 2;
        } else if (min < 11) {
            rate = 1;
        } else {
            rate = 0;
        }
        return rate;
    }

    Toast toast;

    void toptoast() {
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(this);
        TextView t = new TextView(this);
        t.setText("通过上一关进行解锁");
        t.setBackgroundResource(R.drawable.barrier_tips);
//        t.setWidth(450);
//        t.setHeight(36);
        t.setPadding(10, 10, 50, 10);
        t.setTextSize(18);
        t.setGravity(Gravity.CENTER);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setView(t);
        toast.show();
    }
}
