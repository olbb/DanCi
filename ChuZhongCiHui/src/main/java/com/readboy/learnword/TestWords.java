package com.readboy.learnword;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.readboy.learnword.util.BackMusic;
import com.readboy.learnword.util.Data;
import com.readboy.learnword.util.DbHelper;
import com.readboy.learnword.util.Util;
import com.readboy.learnword.util.Word;
import com.readboy.learnword.view.SmallPinBar;
import com.readboy.learnword.view.TouchpadView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mar on 13-9-22.
 */


public class TestWords extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, Chronometer.OnChronometerTickListener {

    String tag = "LearnWord";
    public Chronometer timer;
    int index;
    boolean gameover = false;
    Word word;
    Data data;
    int step = 1;//2中译英1英译中3听写4造句

    public static TestWords instance;

    public static long basetime;

    TextView toptext;
    TextView bottomtext;
    TextView spelltop;
    TextView spellbottom;

    boolean endflag;

    public RadioGroup answer;
    public RadioButton a, b, c, d;
    ImageView ia, ib, ic, id, ir;
    RelativeLayout test_translate;
    Button goback, goon, read, del, enter, home, close, config;
    LinearLayout spellword;
    TouchpadView touchpadView;
    public EditText spelled;
    ImageView spell_result;
    Tips tips;
    int hasstep;
    DbHelper db;
    InputMethodManager im;

    TextView tip;

    BackMusic bm;

    Handler mh;

    List<TestWordStep> testwords;

    ToggleButton music;


    SmallPinBar pinBar;
    TextView jindu, weiwanchen, yiwanchen, wanchenshu, zongtishu;
    int tishu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testwords);
        Util.curstate = "TestWord";

        instance = this;
        endflag=false;

        im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        Log.i(tag,"TestWords onCreate");
        db = new DbHelper(this);
//        Log.d(tag,"错词总共有"+db.getCount()+"条");

        data = SelectWord.data;
        timer = (Chronometer) findViewById(R.id.timer);
        if (basetime == 0) {
            timer.setBase(SystemClock.elapsedRealtime());
        } else {
            if (LearnWord.instance.timer != null) {
                timer.setBase(LearnWord.instance.timer.getBase());
            } else {
                timer.setBase(basetime);
            }

        }

        bm = new BackMusic(this);
        timer.start();
        toptext = (TextView) findViewById(R.id.test_toptext);
        bottomtext = (TextView) findViewById(R.id.test_bottomtext);
        answer = (RadioGroup) findViewById(R.id.test_answer);
        a = (RadioButton) findViewById(R.id.test_answer_a);
        b = (RadioButton) findViewById(R.id.test_answer_b);
        c = (RadioButton) findViewById(R.id.test_answer_c);
        d = (RadioButton) findViewById(R.id.test_answer_d);
        ia = (ImageView) findViewById(R.id.test_result_a);
        ib = (ImageView) findViewById(R.id.test_result_b);
        ic = (ImageView) findViewById(R.id.test_result_c);
        id = (ImageView) findViewById(R.id.test_result_d);
        ir = (ImageView) findViewById(R.id.test_result);
        tip = (TextView) findViewById(R.id.tips);

        test_translate = (RelativeLayout) findViewById(R.id.test_translate);
        goback = (Button) findViewById(R.id.gobacklearn);
        goon = (Button) findViewById(R.id.goontest);
        //拼写
        spellword = (LinearLayout) findViewById(R.id.spellword);
        read = (Button) findViewById(R.id.spell_read);
        spelltop = (TextView) findViewById(R.id.spell_name);
        spellbottom = (TextView) findViewById(R.id.spell_exampl);
        touchpadView = (TouchpadView) findViewById(R.id.spell_touchpad);
        spellword.setVisibility(View.GONE);

        touchpadView.learn = false;
        del = (Button) findViewById(R.id.spell_del);
        enter = (Button) findViewById(R.id.spell_enter);
        spelled = (EditText) findViewById(R.id.spell_edittext);
        read.setOnClickListener(this);
        del.setOnClickListener(this);
        enter.setOnClickListener(this);

        spell_result = (ImageView) findViewById(R.id.result);


        goback.setOnClickListener(this);
        goon.setOnClickListener(this);

        home = (Button) findViewById(R.id.home);
        close = (Button) findViewById(R.id.close);
        config = (Button) findViewById(R.id.config);
        home.setOnClickListener(this);
        close.setOnClickListener(this);
        config.setOnClickListener(this);

        pinBar = (SmallPinBar) findViewById(R.id.test_pinbar);
        weiwanchen = (TextView) findViewById(R.id.test_weiwanchen);
        yiwanchen = (TextView) findViewById(R.id.test_yiwanchen);
        wanchenshu = (TextView) findViewById(R.id.test_wanchenshu);
        jindu = (TextView) findViewById(R.id.test_jindu);
        zongtishu = (TextView) findViewById(R.id.test_zongtishu);


//        Util.re.setDialog(this);
//        Util.re.islearning=false;
        tips = new Tips(this);
//        getWord();

        timer.setOnChronometerTickListener(this);

        mh = new Handler();
        testwords = setrandomstep(SelectWord.words);
        tishu = testwords.size();
        getWord();

        music = (ToggleButton) findViewById(R.id.music);
        music.setChecked(Config.music);
        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Config.music = b;
            }
        });

        getRootView(this).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {

                if (second >= 60) {
                    timer.setBase(timer.getBase() + (SystemClock.elapsedRealtime() - temptime));
                    timer.start();
                }
                second = 0;

                switch (e.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        current_x = (int) e.getX();
                        break;

                    case MotionEvent.ACTION_UP:
                        int dex = (int) e.getX() - current_x;
                        if (Math.abs(dex) < Util.min_shift) {
                            return true;
                        }
                        if (dex < 0) {//下一个
                            if (goon.getVisibility() == View.VISIBLE) {
//                                Log.e("LearnWord","用户滑屏跳转到下一个单词"+testwords.size());
                                onClick(goon);
                            }
                        }
                        break;
                }

                return true;
            }
        });

        spelled.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    onClick(enter);
                    return true;
                }
                return false;
            }
        });

    }

    int current_x;

    private static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);

    }

    List<TestWordStep> tres;
    List<TestWordStep> temp;

    List<TestWordStep> setrandomstep(List<Word> words) {

        tres = new ArrayList();
        temp = new ArrayList();//临时缓存
        TestWordStep tstep;
        int postion = 0;
        for (Word word : words) {
            if (word.checked) {
                for (int i = 1; i < 5; i++) {
                    tstep = new TestWordStep();
                    tstep.postion = postion;
                    tstep.step = i;

                    temp.add(tstep);
                }
            } else {
                int a = (int) (Math.random() * 4) + 1;
                int b = a;
                while (b == a) {
                    b = (int) (Math.random() * 4) + 1;
                }
                tstep = new TestWordStep();
                tstep.postion = postion;
                tstep.step = a;
                temp.add(tstep);
                tstep = new TestWordStep();
                tstep.postion = postion;
                tstep.step = b;
                temp.add(tstep);
            }
            postion++;
        }
        while (temp.size() > 0) {
            int r = (int) (Math.random() * temp.size());
            tstep = temp.get(r);
            tres.add(tstep);
            temp.remove(r);
        }

        //防止step3 随机出现在第一步,造成软键盘显示不良

        TestWordStep t = tres.get(0);
        if (t.step == 3 && tres.size() > 1) {
            int i = 1;
            TestWordStep t2;
            while ((t2 = tres.get(i)).step == 3) {
                i++;
            }
            //交换tres 0 i
            tres.set(0, t2);
            tres.set(i, t);
//            Log.d(tag,"防止step3 随机出现在第一步,造成软键盘显示不良");
//            Log.d(tag,"0与"+i+"进行交换");
        }


        return tres;
    }


    public int deadline = 0;


    @Override
    public void onChronometerTick(Chronometer chronometer) {
        if (second < 60) {
            second++;
//            Log.d("LearnWord","second ++: "+second);
        } else {
            timer.stop();
            temptime = SystemClock.elapsedRealtime();
        }

        if (goon.getVisibility() == View.GONE) {
            if (second < 60) {
//                Log.i("LearnWord","deadline :"+deadline);
                deadline++;
//                Log.d("LearnWord","deadline++ :"+deadline);
            }

            if (deadline > Config.time) {
//                Log.i("LearnWord","over time");
                deadline = 0;
                tip.setText("答题超时");
                bm.play("overtime");
//                if (step==2||step==4){
//                    Util.re.forresult=false;
//                    Util.re.mh.post(Util.re.stoprecord);
//                }
                if (step == 3) {
                    spelled.setText("答题超时");
                    onClick(enter);

                } else {
                    onCheckedChanged(answer, 1000);
                }
            }
        }


    }

    long costtime;

    Runnable musicsucess = new Runnable() {
        @Override
        public void run() {
            bm.play("sucessed");
        }
    };

    Runnable musicnewstage = new Runnable() {
        @Override
        public void run() {
            bm.play2("newstage");
        }
    };

    public void getWord() {

        int wc = tishu - testwords.size();
        int precent = wc * 100 / tishu;
        pinBar.setPrecent(precent);
        weiwanchen.setText("未完成:" + (100 - precent) + "%");
        yiwanchen.setText("已完成:" + precent + "%");
        wanchenshu.setText("已完成: " + wc + "题");
        zongtishu.setText("总题数: " + tishu + "题");
        jindu.setText(precent + "");


        if (testwords.size() > 0) {
            TestWordStep t = testwords.get(0);
            word = SelectWord.words.get(t.postion);
            step = t.step;
            testwords.remove(0);
//            Util.re.setWord(word.word);
            updateui();
        } else {
            try{
                wait(300);
            }catch (Exception e){

            }

            String time = timer.getText().toString();
            String[] ts = time.split(":");
            int min, second;
            int temp;
            if (ts.length == 2) {
                min = Integer.parseInt(ts[0]);
                second = Integer.parseInt(ts[1]);

            } else {
                min = Integer.parseInt(ts[1]);
                second = Integer.parseInt(ts[2]);
            }
            temp = (min * 60 + second) * 1000;
            int rate;
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
            timer.setOnChronometerTickListener(null);

            //闯关失败
            if (gameover) {
                tips.rate.setVisibility(View.VISIBLE);
                tips.resultimg.setImageResource(R.drawable.game_failed);
                tips.result.setText(getString(R.string.game_failed));
                tips.spendtime.setText("本关用时：" + min + "分" + second + "秒");
                tips.rate.setRating(0);
                tips.gobarrier.setVisibility(View.GONE);
                tips.wrongwords.setVisibility(View.VISIBLE);
                tips.show((AnimationDrawable) getResources().getDrawable(R.drawable.ku));

                bm.play("failed");

            } else if (Util.stage == Util.curstage + 1) {
                if (Util.curstage <= 36) {
                    Util.curstage = Util.stage;
//                    Log.d("LearnWord","curstage is"+Util.curstage);
                }
                if (Util.stage % 6 != 0) {
                    bm.play("chuanguanchengong");
                }

                mh.postDelayed(musicsucess, 2000);

                Intent i = new Intent();
                i.setAction("UPDATE_LearnWord_Widget");
                Entrance.writebardata();
                sendBroadcast(i);


                Util.spendtime[Util.stage] = (int) (temp / 1000);

                tips.gobarrier.setVisibility(View.VISIBLE);
                tips.wrongwords.setVisibility(View.GONE);
                if (Util.stage % 6 == 0) {//成就
//                    tips.resultimg.setImageResource(R.drawable.game_sucess);
                    String t = Util.getChengHao(this, Util.stage / 6);

                    bm.play2("chenghao" + Util.stage / 6);
//                    Intent s=new Intent(this,ChengHao.class);
//                    startActivity(s);
                    ChengHao chengHao = new ChengHao(this);


//                    if (Util.stage/6==1){
//                        t="初来乍到";
//                    }else if (Util.stage/6==2){
//                        t="锋芒乍现";
//                    }else if (Util.stage/6==3){
//                        t="小有成就";
//                    }else if (Util.stage/6==4){
//                        t="声名显赫";
//                    }else if(Util.stage/6==6){
//                        t=getString()
//                    }
//                    String s="完成"+ Util.stage+"关,已背单词"+ Util.stage*10+"个,\n目前进度"+ Util.stage*100/36+"%\n获得称号  "+t;
//                    SpannableString sb =new SpannableString(s);
//                    sb.setSpan(new ForegroundColorSpan(Color.RED),s.length()-4,s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    tips.result.setText("恭喜你");
//                    tips.spendtime.setText(sb);
//                    tips.rate.setVisibility(View.INVISIBLE);
//                    tips.title.setText("奖励称号");
//                    tips.show((AnimationDrawable)getResources().getDrawable(R.drawable.xiao));
                    bm.play("getrate");
                } else//解锁新关卡
                {
                    tips.gobarrier.setVisibility(View.VISIBLE);
                    tips.wrongwords.setVisibility(View.GONE);
                    tips.resultimg.setImageResource(R.drawable.game_sucess);
                    tips.result.setText(getString(R.string.game_sucess));
                    tips.spendtime.setText("本关用时：" + min + "分" + second + "秒");
                    tips.rate.setRating(rate);
                    tips.show((AnimationDrawable) getResources().getDrawable(R.drawable.xiao));
                    if (Util.spendtime[Util.stage] > (int) (temp / 1000) || Util.spendtime[Util.stage] == 0) {
                        Util.spendtime[Util.stage] = (int) (temp / 1000);
                    }

                    mh.postDelayed(musicnewstage, 2000);
                }


            } else {
                bm.play("闯关成功");
                if (Util.spendtime[Util.stage] > (int) (temp / 1000) || Util.spendtime[Util.stage] == 0) {
                    Util.spendtime[Util.stage] = (int) (temp / 1000);
                }
                bm.play("sucessed");
                tips.resultimg.setImageResource(R.drawable.game_sucess);
                tips.result.setText(getString(R.string.game_sucess));
                tips.spendtime.setText("本关用时：" + min + "分" + second + "秒");
                tips.rate.setVisibility(View.VISIBLE);
                tips.rate.setRating(rate);
                tips.show((AnimationDrawable) getResources().getDrawable(R.drawable.xiao));

            }
            if (!gameover) {
                if (Util.spendtime[Util.stage] > (int) (temp / 1000) || Util.spendtime[Util.stage] == 0) {
//                    Log.i(tag,"send level info :level "+Util.stage+" time:"+(int)temp/1000);
                    List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                    NameValuePair pair = new BasicNameValuePair("level", Util.stage + "");
                    pairs.add(pair);
                    pair = new BasicNameValuePair("time", (int) temp / 1000 + "");
                    pairs.add(pair);
                    Util.httpPost(Util.sendstage, pairs);
                }
            }
        }
    }


    void updateui() {

        test_translate.setVisibility(View.GONE);
        goon.setVisibility(View.GONE);
        ia.setVisibility(View.GONE);
        ib.setVisibility(View.GONE);
        ic.setVisibility(View.GONE);
        id.setVisibility(View.GONE);
        ir.setVisibility(View.GONE);

        boolean clickable = true;
//        if(step<5){
//            clickable=true;
//        }else{
//            clickable=false;
//        }

        a.setClickable(clickable);
        b.setClickable(clickable);
        c.setClickable(clickable);
        d.setClickable(clickable);
        a.setPressed(false);
        b.setPressed(false);
        c.setPressed(false);
        d.setPressed(false);
        answer.setOnCheckedChangeListener(null);
        answer.clearCheck();
        answer.setOnCheckedChangeListener(this);
        spellword.setVisibility(View.GONE);
        goback.setVisibility(View.GONE);
        spell_result.setVisibility(View.GONE);


//        System.out.println("Step is:"+step);
        deadline = 0;
//        if(step==2||step==4){
//            tip.setText("请使用语音作答");
//        }else{
        tip.setText("");
//        }

        im.hideSoftInputFromWindow(spelled.getWindowToken(), 0);

//        step=3;

        switch (step) {
            case 1:
                test_translate.setVisibility(View.VISIBLE);
                toptext.setText(word.word);
                bottomtext.setVisibility(View.INVISIBLE);
                setRandomChn();
                answer.setOnCheckedChangeListener(this);
                break;

            case 2:
                test_translate.setVisibility(View.VISIBLE);
                toptext.setText(word.expl);
                bottomtext.setVisibility(View.INVISIBLE);
                setRandomEn();

                answer.setOnCheckedChangeListener(this);
//                Util.re.setSent(a.getText().toString()+"|"+b.getText().toString()+"|"+c.getText().toString()+"|"+d.getText().toString());
//                Util.re.show();
                break;

            case 3:
//                spell_result.setImageDrawable(null);
                spellword.setVisibility(View.VISIBLE);
                del.setEnabled(true);
                enter.setEnabled(true);
                updatespellui();
                touchpadView.setOnTouchListener(touchpadView);
                word.readword();
                spelltop.setText(word.expl);
                spellbottom.setText(word.phon);
                spelled.setText("");

                break;

            case 4:
                test_translate.setVisibility(View.VISIBLE);
                bottomtext.setVisibility(View.VISIBLE);
                String s = word.exampen;
                String m = word.word.replaceAll("[a-zA-Z]", "_");
//                Log.d("LearnWord",word.toString());
                s = s.replaceAll(word.word, m);
                String tmp = word.word.substring(0, 1).toUpperCase() + word.word.substring(1);
                s = s.replaceAll(tmp, m);
                toptext.setText(s);
                bottomtext.setText(word.exampcn);
                setRandomEn();
                answer.setOnCheckedChangeListener(this);
//                Util.re.setSent(a.getText().toString()+"|"+b.getText().toString()+"|"+c.getText().toString()+"|"+d.getText().toString());
//                Util.re.show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bm.playflag = true;
        if (temptime != 0&&!endflag) {
            timer.setBase(timer.getBase() + (SystemClock.elapsedRealtime() - temptime));
            timer.start();
        }
        timer.setOnChronometerTickListener(this);
//        if(step==3){
//            updatespellui();
//        }
//
        music.setChecked(Config.music);
        Resources res = getResources();
        Configuration c = res.getConfiguration();
        c.fontScale = 1;
        res.updateConfiguration(c, res.getDisplayMetrics());



//        if(tempscond!=0&&goon.VISIBLE==View.GONE){
//            mh.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    String time=timer.getText().toString();
//                    String s[]=time.split(":");
//                    int ss=Integer.parseInt(s[0])*60+Integer.parseInt(s[1]);
////                    Log.i("Learnword",ss+" ");
//                    deadline=ss-tempscond+deadline;
//
//                    if(deadline>Config.time){
//                        deadline=0;
//                        tip.setText("答题超时");
//                        bm.play("overtime");
//                        if (step==3){
//                            spelled.setText("答题超时");
//                            onClick(enter);
//
//                        }else{
//                            onCheckedChanged(answer,1000);
//                        }
//                    }
//                }
//            },200);
//
//        }

    }

    public void updatespellui() {
        if (step != 3) {
            return;
        }


        if (Config.type) {
            spelled.clearFocus();
            spelled.setEnabled(false);
            del.setVisibility(View.VISIBLE);
            touchpadView.setVisibility(View.VISIBLE);
        } else {
            spelled.setEnabled(true);
            spelled.requestFocus();
            spelled.setSelection(spelled.getText().toString().length());
            im.showSoftInput(spelled, InputMethodManager.SHOW_FORCED);
            del.setVisibility(View.GONE);
            touchpadView.setVisibility(View.GONE);
        }
        if (!enter.isEnabled())
            spelled.setEnabled(false);

    }


    @Override
    protected void onPause() {
//        Log.i("LearnWord","TestWord onPause");
        super.onPause();
        bm.playflag = false;
        if (step == 3) {
            spelled.clearFocus();
            im.hideSoftInputFromWindow(spelled.getWindowToken(), 0);
            if (touchpadView != null && Config.type) {
                touchpadView.mh.postDelayed(touchpadView.rr, 100);
            }
        }
        timer.stop();
        temptime = SystemClock.elapsedRealtime();
        timer.setOnChronometerTickListener(this);
    }

    int tempscond = 0;

    @Override
    protected void onStop() {
        super.onStop();
//        Log.i("LearnWord","TestWord onStop");
        String time = timer.getText().toString();
        String s[] = time.split(":");
        tempscond = Integer.parseInt(s[0]) * 60 + Integer.parseInt(s[1]);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if (radioGroup == answer) {
            radioGroup.setOnCheckedChangeListener(null);
            boolean t = false;
            switch (i) {
                case R.id.test_answer_a:
                    t = match(a.getText().toString());
                    if (!t) {
                        ia.setVisibility(View.VISIBLE);
                        ia.setImageResource(R.drawable.answer_wrong);
                    }
                    break;

                case R.id.test_answer_b:
                    t = match(b.getText().toString());
                    if (!t) {
                        ib.setVisibility(View.VISIBLE);
                        ib.setImageResource(R.drawable.answer_wrong);
                    }
                    break;

                case R.id.test_answer_c:
                    t = match(c.getText().toString());
                    if (!t) {
                        ic.setVisibility(View.VISIBLE);
                        ic.setImageResource(R.drawable.answer_wrong);
                    }
                    break;

                case R.id.test_answer_d:
                    t = match(d.getText().toString());
                    if (!t) {
                        id.setVisibility(View.VISIBLE);
                        id.setImageResource(R.drawable.answer_wrong);
                    }
                    break;

                case 1001://答对
                    t = true;
                    break;

                case 1000://答错
                    t = false;
                    ir.setVisibility(View.VISIBLE);


                    break;

            }
            if (match(a.getText().toString())) {
                ia.setVisibility(View.VISIBLE);
                ia.setImageResource(R.drawable.answer_right);
//                if (t){
//                    a.setChecked(true);
//                }

            } else if (match(b.getText().toString())) {
                ib.setVisibility(View.VISIBLE);
                ib.setImageResource(R.drawable.answer_right);
//                if (t){
//                    b.setChecked(true);
//                }
            } else if (match(c.getText().toString())) {
                ic.setVisibility(View.VISIBLE);
                ic.setImageResource(R.drawable.answer_right);
//                if (t){
//                    c.setChecked(true);
//                }
            } else if (match(d.getText().toString())) {
                id.setVisibility(View.VISIBLE);
                id.setImageResource(R.drawable.answer_right);
//                if (t){
//                    d.setChecked(true);
//                }
            }
            if (!t) {
                bm.play("wrong");
                gameover = true;
                goback.setVisibility(View.VISIBLE);
                db.into(word);

            } else {
                bm.play("right");
            }


            a.setClickable(false);
            b.setClickable(false);
            c.setClickable(false);
            d.setClickable(false);
            goon.setVisibility(View.VISIBLE);
            if (testwords.size() == 0) {
                timer.stop();
                temptime = timer.getBase();
                timer.setOnChronometerTickListener(null);
                costtime = SystemClock.elapsedRealtime() - temptime;
            }
            if (t) {
//                mh.postDelayed(next,1500);
                mh.post(next);
            }

        }
    }

    int second = 0;
    long temptime;


    @Override
    public void onClick(View view) {

        if (second >= 60) {
            timer.setBase(timer.getBase() + (SystemClock.elapsedRealtime() - temptime));
            timer.start();
        }
//        Log.i("","second is "+second);
        second = 0;

        switch (view.getId()) {
            case R.id.goontest:
//                if(goon.getVisibility()==View.GONE){
//                    return;
//                }
                synchronized (this) {
                    goon.setVisibility(View.GONE);
                    mh.removeCallbacks(next);
//                    Log.e("LearnWord","用户跳转到下一个单词"+testwords.size());
                    getWord();
                }
                ;


                break;

            case R.id.spell_read:
                word.readword();
                break;

            case R.id.spell_enter:
                String temp;
//                if (Config.type){
//                    temp=userin.getText().toString();
//                }else{
//                    temp=spelled.getText().toString();
//                }
                temp = spelled.getText().toString();

                if (temp.length() < 1) {
                    return;
                }
                enter.setEnabled(false);
                del.setEnabled(false);
                if (temp.toLowerCase().equals(word.word.toLowerCase())) {
//                    Toast.makeText(this, "拼写正确", Toast.LENGTH_SHORT).show();
                    bm.play("right");
                    spell_result.setVisibility(View.VISIBLE);
                    spell_result.setImageResource(R.drawable.answer_right);
//                    mh.postDelayed(next,1500);
                    mh.post(next);
                } else {
//                    Toast.makeText(this,"拼写错误",Toast.LENGTH_SHORT).show();
                    spell_result.setImageResource(R.drawable.answer_wrong);
                    spell_result.setVisibility(View.VISIBLE);
                    gameover = true;
                    db.into(word);
                    goback.setVisibility(View.VISIBLE);
                    bm.play("wrong");
                }
                if (testwords.size() == 0) {
                    timer.stop();
                    temptime = timer.getBase();
                    costtime = SystemClock.elapsedRealtime() - temptime;
                }


//                userin.setText("");
                touchpadView.mpath.reset();
                touchpadView.hwpoints.removeAll(touchpadView.hwpoints);
//                spelled.setText("");
                touchpadView.invalidate();


                goon.setVisibility(View.VISIBLE);
                touchpadView.setOnTouchListener(null);
//                im.hideSoftInputFromWindow(spelled.getWindowToken(), 0);
//                enter.setEnabled(false);
                spelled.setEnabled(false);
                break;

            case R.id.spell_del:

                String s = spelled.getText().toString();
                if (s.length() > 0) {
                    spelled.setText(s.substring(0, s.length() - 1));
                }
                break;

            case R.id.gobacklearn:
                LearnErrorWord.words = new ArrayList<Word>();
                LearnErrorWord.words.add(word);

//                mh.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
                Intent i = new Intent(TestWords.this, LearnErrorWord.class);
                i.putExtra("isfortest", true);
                startActivity(i);
//                    }
//                },10000);


//                finish();
                break;

            case R.id.close:
//                Config.type=!Config.type;
//                updatespellui();
                finish();
                break;

            case R.id.config:
                Intent j = new Intent(TestWords.this, Config.class);
                startActivity(j);

                break;

            case R.id.home:
                Util.finish();
                break;
        }
    }

    boolean match(String s) {
        if (s.equals(word.word) || s.equals(word.expl)) {
            return true;
        }
        return false;
    }

    Word getrword() {//获取随机单词
        Word tword;
        int random = word.index;
        while (random == word.index) {
            random = (int) (Math.random() * 360 + 1);
        }
        tword = data.getword(random);
        if (tword.word != null && tword.expl != null) {
//            Log.i(tag,tword.toString());
            return tword;
        } else {
//            Log.e(tag,word.toString());

            return getrword();
        }


    }

    void setRandomChn() {
        int x = (int) (Math.random() * 4 + 1);
        setRbtn(x, word.expl);
        Word tw;
        int tmp[] = new int[3];
        int tmpindex = 0;
        for (int i = 1; i < 5; i++) {
            if (i != x) {

                tw = getrword();
                while (tw.index == tmp[0] || tw.index == tmp[1] || tw.index == tmp[2]) {
                    tw = getrword();
                }
                tmp[tmpindex] = tw.index;
                tmpindex++;
                setRbtn(i, tw.expl);
            }
        }
    }

    void setRandomEn() {
        int x = (int) (Math.random() * 4 + 1);
        setRbtn(x, word.word);
        Word tw;
        int tmp[] = new int[3];
        int tmpindex = 0;
        for (int i = 1; i < 5; i++) {
            if (i != x) {

                tw = getrword();
                while (tw.index == tmp[0] || tw.index == tmp[1] || tw.index == tmp[2]) {
                    tw = getrword();
                }
                tmp[tmpindex] = tw.index;
                tmpindex++;
                setRbtn(i, tw.word);
            }
        }
    }

    void setRbtn(int xx, String s) {
        switch (xx) {
            case 1:
                a.setText(s);
                break;

            case 2:
                b.setText(s);
                break;

            case 3:
                c.setText(s);
                break;

            case 4:
                d.setText(s);
                break;

        }
    }


    protected void toast(final String str) {


        TextView t = new TextView(this);
        t.setTextSize(20);
        t.setText(str);
        t.setGravity(Gravity.CENTER);
        t.setBackgroundResource(R.drawable.toastback);
        Toast tt = new Toast(this);
        tt.setView(t);
        tt.setGravity(Gravity.CENTER, 0, 0);
        tt.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.i("LearnWord","TestWord onDestroy");
        try {
            db.close();
        } catch (Exception e) {

        }

        basetime = timer.getBase();
        if (bm != null) {
            bm.stop();
        }

//        try{
//            Util.re.mh.post(Util.re.stoprecord);
//        }catch (Exception e){
//
//        }
        timer.setOnChronometerTickListener(null);

        try{
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//        long temp= SystemClock.elapsedRealtime()-timer.getBase();
//        if(testwords.size()==0){
//            int second=(int)costtime/1000;
//        }else{
//            int second=(int)temp/1000;
//        }
            String time = timer.getText().toString();
            String s[] = time.split(":");
            int second = Integer.parseInt(s[0]) * 60 + Integer.parseInt(s[1]);


//        Log.d(tag,"LearnWord addtime:"+second);
            NameValuePair pair = new BasicNameValuePair("time", second + "");
            pairs.add(pair);
            Util.httpPost(Util.sendtime, pairs);
        }catch (Exception e){
            e.printStackTrace();
        }

        Entrance.writebardata();

        mh.removeCallbacks(musicnewstage);
        mh.removeCallbacks(musicsucess);

    }


    Runnable next = new Runnable() {
        @Override
        public void run() {
            if (goon.getVisibility() == View.VISIBLE) {
//                Log.e("LearnWord","自动跳转到下一个单词"+testwords.size());
                goon.setVisibility(View.GONE);
                onClick(goon);

            }

        }
    };

    class TestWordStep {

        int postion;
        int step;

    }


}
