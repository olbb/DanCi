package com.readboy.learnwordx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.readboy.learnwordx.util.BackMusic;
import com.readboy.learnwordx.util.Util;
import com.readboy.learnwordx.util.Word;
import com.readboy.learnwordx.view.TouchpadView;

import java.util.List;
import java.util.regex.Pattern;

public class LearnErrorWord extends Activity implements OnClickListener, OnCheckedChangeListener {

    //程序入口
    String tag = "LearnWord";

    public static LearnErrorWord instance;


    //
    RadioGroup rd;
    RadioButton rddef;
    TextView word_name, word_phon, word_expl, word_exampen, word_exampcn;
    Button nextword, lastword, readword, gotest;
    Button goont;

    LinearLayout wordinfo_def;
    LinearLayout wordinfo_examp;
    LinearLayout wordinfo_spell;
    Chronometer timer;

    //拼写界面
    TextView spell_word, spell_exapl, spell_tips;
    ImageView spell_result;

    Button spell_read, spell_enter, spell_del;
    public EditText spell_text;
    TouchpadView spell_touch;

    InputMethodManager im;

    static Word word;
    public static List<Word> words;
    int index = 0;


    //发音结果相关
    TextView readscore, readgrade;
    RelativeLayout readresult;

    public static Handler mh;

    Button home, close, config;
    ToggleButton music;

    BackMusic bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//		Log.i(tag, "LearnWord Oncreate");
        setContentView(R.layout.learnmain);
        instance = this;
        init();
        Util.curstate = "LearnErrorWord";

        word = words.get(index);
        if (words.size() <= 1) {

            nextword.setVisibility(View.GONE);
        }

        updatespellui();
        updatewordui();
//            onTestWord();

        Intent i = getIntent();
        boolean isfortest;
        isfortest = i.getBooleanExtra("isfortest", false);
        if (isfortest) {
            onTestWord();
        }

        bm = new BackMusic(this);


//        Util.re.islearning=true;语音相关       屏蔽
        getRootView(this).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
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
                            if (nextword.getVisibility() == View.VISIBLE) {
                                onClick(nextword);
                            }
                        } else {//上一个
                            if (lastword.getVisibility() == View.VISIBLE) {
                                onClick(lastword);
                            }

                        }
                        break;
                }


                return true;
            }
        });
    }

    private static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);

    }

    int current_x;

    boolean ontest = false;

    public void onTestWord() {
//        this.word=word;
        nextword.setVisibility(View.GONE);
        goont.setVisibility(View.VISIBLE);
        ontest = true;
//        timer.setBase(TestWords.instance.timer.getBase());
//        timer.start();


    }

    private void init() {
        // TODO Auto-generated method stub
        rd = (RadioGroup) findViewById(R.id.rds);
        wordinfo_def = (LinearLayout) findViewById(R.id.def_wordinfo);
        wordinfo_examp = (LinearLayout) findViewById(R.id.wordinfo_examp);
        wordinfo_spell = (LinearLayout) findViewById(R.id.spellword);
        wordinfo_spell.setVisibility(View.GONE);

        timer = (Chronometer) findViewById(R.id.timer);
        ImageView timericon = (ImageView) findViewById(R.id.timericon);
        timericon.setVisibility(View.GONE);
        timer.setVisibility(View.GONE);
        word_name = (TextView) findViewById(R.id.word_word);
        word_phon = (TextView) findViewById(R.id.word_phon);
        word_expl = (TextView) findViewById(R.id.word_expl);
        word_exampen = (TextView) findViewById(R.id.word_exampen);
        word_exampcn = (TextView) findViewById(R.id.word_exampcn);
        nextword = (Button) findViewById(R.id.nextword);
        lastword = (Button) findViewById(R.id.lastword);
        readword = (Button) findViewById(R.id.readword);
        gotest = (Button) findViewById(R.id.gotest);
        rddef = (RadioButton) findViewById(R.id.word_def);
        im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //拼写
        spell_read = (Button) findViewById(R.id.spell_read);
        spell_word = (TextView) findViewById(R.id.spell_name);
        spell_touch = (TouchpadView) findViewById(R.id.spell_touchpad);
        spell_touch.learn = true;
        spell_exapl = (TextView) findViewById(R.id.spell_exampl);
        spell_del = (Button) findViewById(R.id.spell_del);
        spell_enter = (Button) findViewById(R.id.spell_enter);
        spell_text = (EditText) findViewById(R.id.spell_edittext);
        spell_result = (ImageView) findViewById(R.id.result);
        goont = (Button) findViewById(R.id.goont);
        goont.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        spell_tips = (TextView) findViewById(R.id.spell_tips);
        spell_tips.setVisibility(View.GONE);


        readgrade = (TextView) findViewById(R.id.readgrade);
        readscore = (TextView) findViewById(R.id.readscore);
        readresult = (RelativeLayout) findViewById(R.id.readresult);


        music = (ToggleButton) findViewById(R.id.music);
        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Config.music = b;
            }
        });

        nextword.setOnClickListener(this);
        lastword.setOnClickListener(this);
        readword.setOnClickListener(this);
        gotest.setOnClickListener(this);
        rd.setOnCheckedChangeListener(this);
        spell_read.setOnClickListener(this);
        spell_touch.setOnTouchListener(spell_touch);
        spell_del.setOnClickListener(this);
        spell_enter.setOnClickListener(this);
        spell_text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    onClick(spell_enter);
                    return true;
                }

                return false;
            }
        });
//        record=(Button)findViewById(R.id.record);
//        record.setOnClickListener(recordEvent);
        mh = new Handler();
//        Util.re.islearning=true;
//        Util.re.setDialog(this);

        home = (Button) findViewById(R.id.home);
        close = (Button) findViewById(R.id.close);
        config = (Button) findViewById(R.id.config);

        home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.finish();

            }
        });
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        config.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LearnErrorWord.this, Config.class);
                startActivity(i);
            }
        });
    }


//    Runnable record=new Runnable() {
//        @Override
//        public void run() {
//            Util.re.show();
//        }
//    };


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.nextword:
                int size = words.size() - 1;
                if (index < size) {
                    index++;
                    word = words.get(index);
                    updatewordui();

                    if (index == size) {
                        nextword.setVisibility(View.GONE);
//					gotest.setVisibility(View.VISIBLE);
                    }
                    if (lastword.getVisibility() == View.GONE) {
                        lastword.setVisibility(View.VISIBLE);
                    }
                    if (!rddef.isChecked()) {
                        rddef.setChecked(true);
                    }

                }

                break;

            case R.id.lastword:
                if (index > 0) {
                    index--;
                    word = words.get(index);
                    updatewordui();
                    if (index == 0) {
                        lastword.setVisibility(View.GONE);
                    }
                    if (nextword.getVisibility() == View.GONE) {
                        nextword.setVisibility(View.VISIBLE);
                        gotest.setVisibility(View.GONE);
                    }
                    if (!rddef.isChecked()) {
                        rddef.setChecked(true);
                    }
                }

                break;

            case R.id.readword:
                word.readword();
                break;

            case R.id.spell_read:
                word.readword();
                break;

            case R.id.spell_del:

                String s = spell_text.getText().toString();
                if (s.length() > 0) {
                    spell_text.setText(s.substring(0, s.length() - 1));
                }
                break;

            case R.id.spell_enter:
                String temp;
                im.hideSoftInputFromWindow(spell_text.getWindowToken(), 0);

                temp = spell_text.getText().toString();
                spell_del.setEnabled(false);

                if (temp.length() < 1) {
                    return;
                }
                if (temp.toLowerCase().equals(word.word.toLowerCase())) {
//                Toast.makeText(this,"拼写正确",Toast.LENGTH_SHORT).show();
                    spell_result.setVisibility(View.VISIBLE);
                    spell_result.setImageResource(R.drawable.answer_right);
                    bm.play("right");
                } else {
//                Toast.makeText(this,"拼写错误,请重试",Toast.LENGTH_SHORT).show();
                    spell_result.setVisibility(View.VISIBLE);
                    spell_result.setImageResource(R.drawable.answer_wrong);
                    bm.play("wrong");
                }
                mh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        spell_text.setText("");
                        spell_touch.mpath.reset();
                        spell_touch.hwpoints.removeAll(spell_touch.hwpoints);
                        spell_text.setText("");
                        spell_touch.invalidate();
                        spell_result.setVisibility(View.GONE);
                        spell_del.setEnabled(true);
                    }
                }, 500);


                break;


            default:
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rd.getCheckedRadioButtonId() == R.id.word_spell) {
            spell_text.clearFocus();
            im.hideSoftInputFromWindow(spell_text.getWindowToken(), 0);
        }

        if (spell_touch != null && Config.type) {
            spell_touch.mh.postDelayed(spell_touch.rr, 100);
        }


    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // TODO Auto-generated method stub
        wordinfo_def.setVisibility(View.GONE);
        wordinfo_examp.setVisibility(View.GONE);
        wordinfo_spell.setVisibility(View.GONE);
        im.hideSoftInputFromWindow(spell_text.getWindowToken(), 0);

        readresult.setVisibility(View.GONE);
//        mh.removeCallbacks(record);

        switch (checkedId) {
            case R.id.word_def:
                wordinfo_def.setVisibility(View.VISIBLE);
                updatewordui();
                break;

            case R.id.word_exampl:
                wordinfo_examp.setVisibility(View.VISIBLE);
                word_exampen.setText(getexampen());
                word_exampcn.setText(word.exampcn);
                break;

            case R.id.word_spell:
                wordinfo_spell.setVisibility(View.VISIBLE);
//                spell_text.requestFocus();
                updatespellui();
                spell_word.setText(word.word);
                spell_exapl.setText(word.expl);
                spell_text.setText("");
                spell_touch.mpath.reset();
                spell_touch.hwpoints.removeAll(spell_touch.hwpoints);

                spell_touch.invalidate();
                break;

            case R.id.word_read:
                wordinfo_def.setVisibility(View.VISIBLE);
                word.readword();
//                Util.re.setWord(word.word);
//                mh.postDelayed(record,1000);


//                re.startrecord();
                break;

            default:
                break;
        }

    }


    private void updatewordui() {
        // TODO Auto-generated method stub
        word.readword();
        spell_result.setVisibility(View.GONE);
        word_name.setText(word.word);
        word_phon.setText(word.phon);
        word_expl.setText(word.expl);
        readresult.setVisibility(View.GONE);

    }

    public void updatespellui() {
        if (rd.getCheckedRadioButtonId() != R.id.word_spell) {
            return;
        }
        spell_result.setVisibility(View.GONE);
//        Log.i(tag, "updatespellui");
        if (Config.type) {
            spell_del.setVisibility(View.VISIBLE);
            spell_touch.setVisibility(View.VISIBLE);
            spell_text.setEnabled(false);
            spell_text.clearFocus();
//            im.hideSoftInputFromWindow(spell_text.getWindowToken(),0);
        } else {
            spell_text.requestFocus();
            spell_text.setEnabled(true);
            im.showSoftInput(spell_text, InputMethodManager.SHOW_FORCED);
            spell_del.setVisibility(View.GONE);
            spell_touch.setVisibility(View.GONE);
        }

    }

    private SpannableString getexampen() {
        // TODO Auto-generated method stub
        SpannableString str = new SpannableString(word.exampen);
        int start, end;
        start = word.exampen.toLowerCase().indexOf(word.word.toLowerCase());
        end = start;
//        = start + word.word.length();

        Pattern p = Pattern.compile("[^a-zA-Z]");
        String temp[] = p.split(word.exampen);
        for (String a : temp) {

            if ((a.toLowerCase()).startsWith(word.word.toLowerCase())) {
                end += a.length();
            }
        }
//        if (start!=-1&&end!=-1){
        str.setSpan(new ForegroundColorSpan(Color.rgb(0, 160, 255)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }

        return str;
    }

    public static void read() {
        word.readword();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {

            int score = data.getIntExtra("score", 0);
            readresult.setVisibility(View.VISIBLE);
            readscore.setText("综合能力评分:" + score);
            String s;
            if (score < 60) {
                s = getString(R.string.readgrade1);
            } else if (score < 70) {
                s = getString(R.string.readgrade2);
            } else if (score < 80) {
                s = getString(R.string.readgrade3);
            } else if (score < 90) {
                s = getString(R.string.readgrade4);
            } else {
                s = getString(R.string.readgrade5);
            }
            readgrade.setText(s);
        }
    }


    public void set(final int score) {
        mh.post(new Runnable() {
            @Override
            public void run() {
                readresult.setVisibility(View.VISIBLE);
                readscore.setText("综合能力评分:" + score);
                String s;
                if (score < 60) {
                    s = getString(R.string.readgrade1);
                } else if (score < 70) {
                    s = getString(R.string.readgrade2);
                } else if (score < 80) {
                    s = getString(R.string.readgrade3);
                } else if (score < 90) {
                    s = getString(R.string.readgrade4);
                } else {
                    s = getString(R.string.readgrade5);
                }
                readgrade.setText(s);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        music.setChecked(Config.music);
        Resources res = getResources();
        Configuration c = res.getConfiguration();
        c.fontScale = 1;
        res.updateConfiguration(c, res.getDisplayMetrics());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        try{
//            Util.re.mh.post(Util.re.stoprecord);
//        }catch (Exception e){
//
//        }
//        Util.finish();
//        Intent i=new Intent(Barrier.instance,ErrorWord.class);
//        startActivity(i);

    }
}
