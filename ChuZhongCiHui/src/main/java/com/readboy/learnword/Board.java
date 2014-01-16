package com.readboy.learnword;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.readboy.encrypt.EncryptReader;
import com.readboy.learnword.adapter.BoardAdapter;
import com.readboy.learnword.util.User;
import com.readboy.learnword.util.Util;

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
public class Board extends Activity implements View.OnClickListener ,RadioGroup.OnCheckedChangeListener {

    Button zb, zongb, tgb;
    Button upload, userinfo;

    Button adduser, upuser;

    TextView username, usergrade;
    ImageView userimg;

    TextView totaltime, totalrank, weekrank;
    ListView total, week ,tiaozhan;
    static BoardAdapter totaladp, weekadp;
    public static List<User> totallist, weeklist;

    public static MHandler hanler;

    public static Board instance;

    LinearLayout totalview, weekview, totaltitle, weektitle ,qfinfo,tiaozhanview,tiaozhantitle;
    RelativeLayout tiaozhaninfo;

    ProgressBar prog1, prog2;

    RadioGroup rdg;
    RadioButton qfb,tzb;
    Button tx,xl;//腾讯微博       新浪微博
    LinearLayout msgview,rongyuview;
    Button msg,rongyu,msgback,rongyuback;

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

        qfinfo=(LinearLayout)findViewById(R.id.board_qinfen_info);

        inittiaozhanui();

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





        onClick(userinfo);
        onClick(zb);
        onClick(zongb);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if(group.equals(rdg)){

            rongyuview.setVisibility(View.GONE);
            msgview.setVisibility(View.GONE);

            switch (checkedId){
                case R.id.rbtn_qfb:
                    qfinfo.setVisibility(View.VISIBLE);
                    tiaozhaninfo.setVisibility(View.GONE);
                    weekview.setVisibility(View.VISIBLE);
                    totalview.setVisibility(View.VISIBLE);
                    tiaozhanview.setVisibility(View.GONE);
                    break;

                case R.id.rbtn_tzb:
                    qfinfo.setVisibility(View.GONE);
                    tiaozhaninfo.setVisibility(View.VISIBLE);
                    weekview.setVisibility(View.GONE);
                    totalview.setVisibility(View.GONE);
                    tiaozhanview.setVisibility(View.VISIBLE);
                    break;
            }


        }

    }

    public void inittiaozhanui(){
        rdg=(RadioGroup)findViewById(R.id.rdg_board);
        rdg.setOnCheckedChangeListener(this);
        tiaozhanview=(LinearLayout)findViewById(R.id.board_tiaozhan_view);
        tiaozhantitle=(LinearLayout)findViewById(R.id.tiaozhantitle);
        tiaozhaninfo=(RelativeLayout)findViewById(R.id.board_tiaozhan_info);
        tx=(Button)findViewById(R.id.tx);
        xl=(Button)findViewById(R.id.xl);
        msg=(Button)findViewById(R.id.board_tiaozhan_msg);
        rongyu=(Button)findViewById(R.id.board_tiaozhan_ryq);
        msgback=(Button)findViewById(R.id.board_msg_back);
        rongyuback=(Button)findViewById(R.id.board_rongyu_back);
        tx.setOnClickListener(this);
        xl.setOnClickListener(this);
        msg.setOnClickListener(this);
        rongyu.setOnClickListener(this);
        msgback.setOnClickListener(this);
        rongyuback.setOnClickListener(this);

        msgview=(LinearLayout)findViewById(R.id.board_msg_view);
        rongyuview=(LinearLayout)findViewById(R.id.board_rongyu_view);


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

            case R.id.board_tiaozhan_ryq:
                rongyuview.setVisibility(View.VISIBLE);
                msgview.setVisibility(View.GONE);
                break;

            case R.id.board_rongyu_back:
                rongyuview.setVisibility(View.GONE);
                break;

            case  R.id.board_tiaozhan_msg:
                rongyuview.setVisibility(View.GONE);
                msgview.setVisibility(View.VISIBLE);
                break;

            case R.id.board_msg_back:
                msgview.setVisibility(View.GONE);
                break;

            case R.id.tx:

                break;

            case R.id.xl:
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
        if(totaladp!=null)totaladp.recycle();

        if(weekadp!=null)weekadp.recycle();
        hanler.removeMessages(UPDATE_TOTAL_BOARD);
        hanler.removeMessages(UPDATE_WEEK_BOARD);
        hanler.removeMessages(UPDATE_USER_INFO);

        Util.onBoardDestory();
        System.gc();
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
