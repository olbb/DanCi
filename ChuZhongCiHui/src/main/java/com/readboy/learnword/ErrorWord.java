package com.readboy.learnword;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.readboy.learnword.adapter.ErrorWordAdapter;
import com.readboy.learnword.util.Data;
import com.readboy.learnword.util.DbHelper;
import com.readboy.learnword.util.Util;
import com.readboy.learnword.util.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mar on 13-9-30.
 */
public class ErrorWord extends Activity {

    ErrorWordAdapter edp;
    ListView list;
    DbHelper db;
    public static List<Word>words;

    TextView titles,tips,isempty;
    TextView t3,titles2;
    View v2;

    Button gotest,golearn,godel;
    public CheckBox selectall;
    Button home,config,close;
    public TextView checkednum;

    public static ErrorWord instance;
    ToggleButton     music;

    Toast tt;
    View examp;
    TextView exampen,exampcn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectword);
        db=new DbHelper(this);

        instance=this;
        titles=(TextView)findViewById(R.id.titles);

        titles2=(TextView)findViewById(R.id.titles2);
        t3=(TextView)findViewById(R.id.t3);
        v2=findViewById(R.id.t2);
        t3.setVisibility(View.GONE);
        v2.setVisibility(View.GONE);
        titles2.setVisibility(View.GONE);

        tips=(TextView)findViewById(R.id.t1);
        gotest=(Button)findViewById(R.id.gotest);
        golearn=(Button)findViewById(R.id.golearn);
        godel=(Button)findViewById(R.id.godel);
        selectall=(CheckBox)findViewById(R.id.selectall);
        titles.setText("测试中出错的单词");
        tips.setText("备注:勾选单词以完成对错词的学习.");
        checkednum=(TextView)findViewById(R.id.checkednums);
        gotest.setVisibility(View.GONE);
        godel.setVisibility(View.VISIBLE);
        isempty=(TextView)findViewById(R.id.isempty);
        home=(Button)findViewById(R.id.home);
        config=(Button)findViewById(R.id.config);
        close=(Button)findViewById(R.id.close);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.finish();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ErrorWord.this,Config.class);
                startActivity(i);
            }
        });
        music=(ToggleButton)findViewById(R.id.music);
        music.setChecked(Config.music);
        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Config.music = b;
            }
        });



        golearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LearnErrorWord.words==null){
                    LearnErrorWord.words=new ArrayList<Word>();
                }else{
                    LearnErrorWord.words.clear();
                }
                for(int i=0;i<words.size();i++){
                    Word word=words.get(i);
                    if(word.checked){
                        LearnErrorWord.words.add(word);
                    }
                }


                if(LearnErrorWord.words.size()>0){
                    Intent i=new Intent(ErrorWord.this,LearnErrorWord.class);
                    startActivity(i);
                }else{
                    Toast.makeText(ErrorWord.this,"请最少勾选一个单词",Toast.LENGTH_SHORT).show();
                    readdb();
                }


            }
        });
        godel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Word word;
                for (int i=0;i<words.size();i++){
                    word=words.get(i);
                    if (word.checked){
                        words.remove(word);
                        db.delete(word.index);
                        i--;

                    }
                }
                if (words.size()<1){
                    isempty.setVisibility(View.VISIBLE);
                    selectall.setEnabled(false);
                    golearn.setVisibility(View.GONE);
                    godel.setVisibility(View.GONE);
                }
                selectall.setChecked(false);
                edp.notifyDataSetChanged();
//                readdb();
                checkednum.setText("已选 "+edp.getchecknum()+" 个/总共"+ErrorWord.words.size()+"个  ");
            }
        });


        selectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                boolean temp=true;
                for (Word word:words){
                    if (isChecked){
                        word.checked=isChecked;
                    }else{
                        if (!word.checked){
                            temp=false;
                        }
                    }
                }
                if (temp&&!isChecked){
                    for (Word word:words){
                        word.checked=isChecked;
                    }
                }
                edp.notifyDataSetChanged();
                if(isChecked){
                    checkednum.setText("已选 " +words.size()+ " 个/总共" + words.size() + "个  ");
                }else {
                    checkednum.setText("已选 " +edp.getchecknum()+ " 个/总共" + words.size() + "个  ");
                }
            }});


        list=(ListView)findViewById(R.id.words);
        if(words==null){
            words=new ArrayList<Word>();
        }else {
            words.clear();
        }


        edp=new ErrorWordAdapter(this);
        list.setAdapter(edp);
//        test();

//        writedb();

        Handler mh=new Handler();
        mh.post(new Runnable() {
            @Override
            public void run() {
               readdb();

            }
        });

//        mh.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                selectall.setChecked(true);
//            }
//        },50);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                if (tt!=null){
                    tt.cancel();
                }

                int location[]=new int[2];
                view.getLocationInWindow(location);

                int y=location[1];

                tt=new Toast(ErrorWord.this);
                tt.setGravity(Gravity.TOP|Gravity.LEFT,750, y-180);


                tt.setView(examp);


                exampcn.setText(words.get(position ).exampcn);
                exampen.setText(words.get(position ).exampen);

                tt.show();
            }
        });

//        list.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                Log.i("LearnWord",scrollState+" scrollState");
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Log.i("LearnWord","onScroll");
//                  list.clearChoices();
//                list.cancelLongPress();
//                list.clearFocus();
////                list
//            }
//        });

        examp=getLayoutInflater().inflate(R.layout.examp, null);
        exampen=(TextView)examp.findViewById(R.id.exampen);
        exampcn=(TextView)examp.findViewById(R.id.exampcn);




    }

    void test(){
        if (db.getCount()<1){
            for(int i=0;i<20;i++){
                DbHelper db=new DbHelper(this);
                ContentValues cv=new ContentValues();
                cv.put("word_name","test"+(int)(Math.random()*100));
                cv.put("word_index",(int)(Math.random()*360)+1);
                cv.put("word_count",0);
                db.insert(cv);
            }
        }
//        db.into("test83",306);
//        db.into("Maofds",294);
    }

    @Override
    protected void onResume() {
        super.onResume();
        music.setChecked(Config.music);
        Resources res=getResources();
        Configuration c=res.getConfiguration();
        c.fontScale=1;
        res.updateConfiguration(c,res.getDisplayMetrics());

//        words.clear();
//        readdb();
//        selectall.setChecked(false);
    }

    void   readdb(){
        Cursor cur=db.query();
        words.clear();

        if (cur.moveToFirst()){
            int index=cur.getInt(3);
            if(SelectWord.data==null){
                SelectWord.data=new Data();
            }
            Word word=SelectWord.data.getword(index);
            words.add(word);
            while (cur.moveToNext()){
                index=cur.getInt(3);
                word=SelectWord.data.getword(index);
                words.add(word);
            }
        }
        cur.close();
//        Log.d("LearnWord","error words has "+words.size());

        edp.notifyDataSetChanged();
        if (words.size()<1){
            isempty.setVisibility(View.VISIBLE);
            godel.setVisibility(View.GONE);
            golearn.setVisibility(View.GONE);
            if(words.size()==0){
                selectall.setEnabled(false);
            }
        }else{
            isempty.setVisibility(View.GONE);
        }

    }

    void writedb(){
        for (int i=1;i<=360;i++){
            Word word=SelectWord.data.getword(i);
            db.into(word);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();

        if(tt!=null){
            tt.cancel();
        }
    }
}
