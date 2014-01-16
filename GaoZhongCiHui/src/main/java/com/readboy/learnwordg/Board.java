package com.readboy.learnwordg;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.readboy.encrypt.EncryptReader;
import com.readboy.learnwordg.adapter.BoardAdapter;
import com.readboy.learnwordg.util.User;
import com.readboy.learnwordg.util.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by mao on 13-11-12.
 */
public class Board extends Activity implements View.OnClickListener {

    Button zb, zongb, tgb;
    Button upload, userinfo;

    Button adduser, upuser;

    TextView username, usergrade;
    ImageView userimg;

    TextView totaltime, totalrank, weekrank;
    ListView total, week;
    static BoardAdapter totaladp, weekadp;
    public static List<User> totallist, weeklist;

    public static MHandler hanler;

    public static Board instance;

    LinearLayout totalview, weekview, totaltitle, weektitle;

    ProgressBar prog1, prog2;

    public static int id;//防止消息混乱

    /**
     * 用户信息存在目录地址. [含文件名]
     */
    public static final String USERINFO_PATH = "mnt/sdcard/.readboy/profile/userInfo.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.board);
        instance = this;
        id = (int) (Math.random() * 100000);
//        Log.d("LearnWord",id+" ");
        zb = (Button) findViewById(R.id.test_getzb);
        zongb = (Button) findViewById(R.id.test_getrb);
        tgb = (Button) findViewById(R.id.test_gettg);
        adduser = (Button) findViewById(R.id.test_adduser);
        upuser = (Button) findViewById(R.id.test_updateuser);
        upload = (Button) findViewById(R.id.test_uploadimg);
        userinfo = (Button) findViewById(R.id.test_getuserinfo);
        zb.setOnClickListener(this);
        zongb.setOnClickListener(this);
        tgb.setOnClickListener(this);
        upuser.setOnClickListener(this);
        adduser.setOnClickListener(this);
        upload.setOnClickListener(this);
        userinfo.setOnClickListener(this);

        username = (TextView) findViewById(R.id.board_username);
        usergrade = (TextView) findViewById(R.id.board_usergrade);
        userimg = (ImageView) findViewById(R.id.board_userimg);

        totaltime = (TextView) findViewById(R.id.board_totaltime);
        totalrank = (TextView) findViewById(R.id.board_totalrank);

        weekrank = (TextView) findViewById(R.id.board_weekrank);
        total = (ListView) findViewById(R.id.list_total);
        week = (ListView) findViewById(R.id.list_week);

        totalview = (LinearLayout) findViewById(R.id.board_total_view);
        weekview = (LinearLayout) findViewById(R.id.board_week_view);

        totaltitle = (LinearLayout) findViewById(R.id.totaltitle);
        weektitle = (LinearLayout) findViewById(R.id.weektitle);

        total.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                totaltitle.invalidate();
            }
        });

        week.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                weektitle.invalidate();
            }
        });

        prog1 = (ProgressBar) findViewById(R.id.prog1);
        prog2 = (ProgressBar) findViewById(R.id.prog2);

        totallist = new ArrayList<User>();
        weeklist = new ArrayList<User>();

        hanler = new MHandler();

        readuserinfo();
//        Util.user=new User();
//        Util.user.realname="王德军";
//        Util.user.uid=73245;
//        Util.user.usergrade="幼儿园";
        if (!getWifiState()) {
            ((LinearLayout) findViewById(R.id.board_nowifi)).setVisibility(View.VISIBLE);
            totalview.setVisibility(View.GONE);
            weekview.setVisibility(View.GONE);
            prog2.setVisibility(View.GONE);
            prog1.setVisibility(View.GONE);
            ((Button) findViewById(R.id.board_gonetsetting)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setAction(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(i);
                    finish();
                }
            });
            return;
        }
//        else if(Util.user==null){
//            ((LinearLayout)findViewById(R.id.board_nonetid)).setVisibility(View.VISIBLE);
//            return;
//        }

        username.setText(Util.user.realname);
        usergrade.setText(Util.user.usergrade);
        File f = null;
        if (Util.user.imagePath != null) {
            f = new File(Util.user.imagePath);
            if (f.exists()) {
                try {
                    Util.user.userimg = Drawable.createFromPath(Util.user.imagePath);
                    userimg.setImageDrawable(Util.user.userimg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


//        if(totaladp==null){
//
//            totaladp=new BoardAdapter(this,totallist);
//        }else{
//            totaladp.updateAdapter(this,totallist);
//        }
//        if(weekadp==null){
//            weekadp=new BoardAdapter(this,weeklist);
//        }else {
//            weekadp.updateAdapter(this,weeklist);
//        }
//
//
//        total.setAdapter(totaladp);
//        week.setAdapter(weekadp);


        onClick(userinfo);
        onClick(zb);
        onClick(zongb);
    }

    String url = "";
    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
    BasicNameValuePair pair;


    @Override
    public void onClick(View v) {

        pairs.clear();

        switch (v.getId()) {


            case R.id.test_getzb:
                Util.httpget(Util.getWeek, id);
                break;

            case R.id.test_getrb:
                Util.httpget(Util.getTotal, id);
                break;

            case R.id.test_adduser:

                pair = new BasicNameValuePair("username", Util.user.realname);
                pairs.add(pair);
                System.out.println(pairs.toString());
                Util.httpPost(Util.ADD_USER, pairs);
                break;

            case R.id.test_updateuser:
                pair = new BasicNameValuePair("username", Util.user.realname);
                pairs.add(pair);
                Util.httpPost(Util.UPDATE_USER, pairs);
                break;

            case R.id.test_gettg:
//                pair=new BasicNameValuePair()

                Util.httpget(Util.getstage, id);
                break;

            case R.id.test_uploadimg:
                Util.upload();
                break;

            case R.id.test_getuserinfo:
                Util.httpget(Util.userinfo, id);
                break;
        }
    }

    public void readuserinfo() {
        Properties props = new Properties();

        try {

            props.load(new InputStreamReader(new EncryptReader(USERINFO_PATH), "gbk"));
            Util.user = new User();
            Util.user.uid = Integer.parseInt(props.getProperty("uid"));
            Util.user.realname = props.getProperty("realname");
            Util.user.imagePath = props.getProperty("imagePath");
            Util.user.username = props.getProperty("username");
            Util.user.usergrade = Util.getGrade(Integer.parseInt(props.getProperty("grade")));

            SharedPreferences sh;
            SharedPreferences.Editor ed;
            sh = getSharedPreferences("user", MODE_PRIVATE);
            ed = sh.edit();
            if (Util.user.uid != 0) {

//                url=Util.url+Util.ADD_USER;
                onClick(adduser);
                addtime(0);
            }


            if (sh.getInt("uid", 0) != Util.user.uid) {
                ed.putInt("uid", Util.user.uid);
            }
            if (!sh.getString("realname", "").equals(Util.user.realname)) {
                if (!sh.getString("realname", "").equals("") && sh.getInt("uid", 0) != 0) {
                    onClick(upuser);
                }
                ed.putString("realname", Util.user.realname);
            }
            if (!sh.getString("imagePath", "").equals(Util.user.imagePath)) {
                ed.putString("imagePath", Util.user.imagePath);

            }
            if (!sh.getString("username", "").equals(Util.user.username)) {

                ed.putString("username", Util.user.username);

            }
            File f = new File(Util.user.imagePath);
            int hashcode = (int) f.length();

//            if(sh.getInt("imghashcode",0)!=hashcode){

//               ed.putInt("imghashcode",hashcode);
            onClick(upload);
//            }
            ed.commit();


            onClick(upuser);
//            Log.d("LearnWord", Util.user.toString());


        } catch (IOException e) {

        }


    }

    private void addtime(int time) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//        long temp= SystemClock.elapsedRealtime()-timer.getBase();
//        int second=(int)temp/1000;

        NameValuePair pair = new BasicNameValuePair("time", time + "");
        pairs.add(pair);
        Util.httpPost(Util.sendtime, pairs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Resources res = getResources();
        Configuration c = res.getConfiguration();
        c.fontScale = 1;
        res.updateConfiguration(c, res.getDisplayMetrics());
        if (getWifiState()) {
            ((LinearLayout) findViewById(R.id.board_nowifi)).setVisibility(View.GONE);
        }
    }


    public final static int UPDATE_USER_INFO = 10001;
    public final static int UPDATE_WEEK_BOARD = 10002;
    public final static int UPDATE_TOTAL_BOARD = 10003;
    public static String totaltimestr;
    public static int totalnum;
    public static int weeknum;

    public class MHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle data = msg.getData();
            int id = data.getInt("id");
            int what = data.getInt("what");
//            Log.d("LearnWord","id"+id+"what"+what);
            if (id != Board.id) {
//                Log.i("ss",Board.id+" "+id);
                return;
            }
            switch (what) {
                case UPDATE_USER_INFO:
                    try {
                        totaltime.setText("总用时: " + totaltimestr);
                        if (weeknum > 0) {
                            weekrank.setText("" + weeknum);
                        } else {
                            weekrank.setText("--");
                        }
                        if (totalnum > 0) {
                            totalrank.setText("" + totalnum);
                        } else {
                            totalrank.setText("--");
                        }
                        SharedPreferences sh = getSharedPreferences("barrier", MODE_PRIVATE);
                        SharedPreferences.Editor ed = sh.edit();
                        ed.putString("zongyongshi", totaltimestr);
                        ed.putInt("zongpaiming", totalnum);
                        ed.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;

                case UPDATE_WEEK_BOARD:
                    try {
                        prog1.setVisibility(View.GONE);
                        weekadp = new BoardAdapter(Board.this, weeklist);
                        week.setAdapter(weekadp);
//                        weekadp.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;

                case UPDATE_TOTAL_BOARD:
                    try {
                        prog2.setVisibility(View.GONE);
                        totaladp = new BoardAdapter(Board.this, totallist);
                        total.setAdapter(totaladp);
//                        totaladp.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        for (User user : weeklist) {
            user.recyle();
        }
        for (User user : totallist) {
            user.recyle();
        }
        hanler.removeMessages(UPDATE_TOTAL_BOARD);
        hanler.removeMessages(UPDATE_WEEK_BOARD);
        hanler.removeMessages(UPDATE_USER_INFO);

        Util.onBoardDestory();
    }

    boolean getWifiState() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] nfs = cm.getAllNetworkInfo();
            for (NetworkInfo nf : nfs) {
                if (nf.isConnected()) {
                    return true;
                }
            }
        }

        return false;
    }
}
