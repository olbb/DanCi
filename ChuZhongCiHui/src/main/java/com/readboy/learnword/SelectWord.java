package com.readboy.learnword;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.readboy.learnword.adapter.SelWordsAdapter;
import com.readboy.learnword.util.BackMusic;
import com.readboy.learnword.util.Data;
import com.readboy.learnword.util.Util;
import com.readboy.learnword.util.Word;

public class SelectWord extends Activity implements OnClickListener{

	String TAG="LearnWord";
    public static SelectWord instance;
	int stage;
	ListView wordlist;
	public SelWordsAdapter sd;
	public static List<Word>words;
	static Data data;
	TextView t1,exampen,exampcn;
	public TextView checkednum;
    public CheckBox selectall;
    Button home,close,config;
	ToggleButton music;
	Button golearn,gotest;
	
	int y;

	
    BackMusic bm;


	Toast tt;
	
	View examp;
	RelativeLayout exampbg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectword);

		instance=this;
        if(data==null){
            data=new Data();
        }
        try{
            Rate.instance.finish();
        }catch (Exception e){

        }

        bm=new BackMusic(this);
        bm.play("selectword");

//		Intent i =getIntent();
//		stage=i.getIntExtra("stage", -1);

        stage=Util.stage;
        if(stage==-1){
            return;
        }
//        Util.stage=stage;
        update();
//		words=new ArrayList<Word>();
//		int start=stage*18;
//		for (int a=1;a<= Util.stage;a++){
//			Word word =data.getword(start+a);
//			words.add(word);
//		}

        selectall=(CheckBox)findViewById(R.id.selectall);
		wordlist=(ListView)findViewById(R.id.words);
        checkednum=(TextView)findViewById(R.id.checkednums);
		sd=new SelWordsAdapter(this);
		wordlist.setAdapter(sd);


		
		wordlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				sd.getItem(position).
				

				if (tt!=null){

					tt.cancel();
				}

                int location[]=new int[2];
                view.getLocationInWindow(location);
//                Log.d("LearnWord","View's X"+location[0]+" View's Y"+location[1]);

                y=location[1];

				tt=new Toast(SelectWord.this);
                    tt.setGravity(Gravity.TOP|Gravity.LEFT,750, y-180);
//
                tt.setView(examp);
                exampcn.setText(words.get(position ).exampcn);
                exampen.setText(words.get(position ).exampen);

				tt.show();

			}
		});


        examp=getLayoutInflater().inflate(R.layout.examp, null);
        exampen=(TextView)examp.findViewById(R.id.exampen);
        exampcn=(TextView)examp.findViewById(R.id.exampcn);
		exampbg=(RelativeLayout)examp.findViewById(R.id.exampbg);

		
		wordlist.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
                if(tt!=null){
                    int tmp[]=new int[2];
                    View ttv=tt.getView();
                    ttv.getLocationOnScreen(tmp);
                    int x= (int)event.getX();
                    int y= (int)event.getY()+190;
                    if(x>tmp[0]&&x<(tmp[0]+450)&&y>tmp[1]&&y<(tmp[1]+280)){
                        return true;
                    }
                }

				return false;
			}
		});
		
		
		
		t1=(TextView)findViewById(R.id.t1);
		t1.setVisibility(View.VISIBLE);


		home =(Button)findViewById(R.id.home);
		close = (Button)findViewById(R.id.close);
		config =(Button)findViewById(R.id.config);
		golearn=(Button)findViewById(R.id.golearn);
		gotest=(Button)findViewById(R.id.gotest);
		home.setOnClickListener(this);
        close.setOnClickListener(this);
        config.setOnClickListener(this);
        music=(ToggleButton)findViewById(R.id.music);
        music.setChecked(Config.music);
        music.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Config.music=b;
            }
        });
		config.setOnClickListener(this);
		golearn.setOnClickListener(this);
		gotest.setOnClickListener(this);
		
		selectall.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
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
				sd.notifyDataSetChanged();

                if(isChecked){
                    checkednum.setText("已选 " +words.size()+ " 个/总共" + words.size() + "个  ");
                }else {
                    checkednum.setText("已选 " +sd.getchecknum()+ " 个/总共" + words.size() + "个  ");
                }
			}});

        selectall.setChecked(false);
//        Handler mh=new Handler();
//        mh.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                selectall.setChecked(true);
//            }
//        },50);

	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
            case R.id.home:
                Util.finish();
                break;

            case R.id.config:
                Intent ii=new Intent(SelectWord.this,Config.class);
                startActivity(ii);
                break;

            case R.id.close:
                finish();
                break;

            //开始学习
            case R.id.golearn:
                Intent i=new Intent(this, LearnWord.class);
                startActivity(i);
                break;

            //测试
            case R.id.gotest:
                Intent b=new Intent(this,TestWords.class);
                TestWords.basetime=0;
                startActivity(b);
                break;

		default:
			break;
		}
		
	};


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		Log.i(TAG, "SelectWord onPause");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		Log.i(TAG, "SelectWord onDestroy");

        if(tt!=null){
            tt.cancel();
        }
        if(bm!=null){
            bm.stop();
        }
        System.gc();

	}

    void update(){
        if(words!=null){
            words.removeAll(words);
        }else{
            words=new ArrayList<Word>();
        }
        stage= Util.stage;
        int start=(stage-1)*10;
        for (int a=1;a<=10;a++){
            Word word =data.getword(start+a);
            words.add(word);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        music.setChecked(Config.music);
        Resources res=getResources();
        Configuration c=res.getConfiguration();
        c.fontScale=1;
        res.updateConfiguration(c,res.getDisplayMetrics());
    }
}
