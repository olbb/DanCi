package com.readboy.learnword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.readboy.learnword.util.Util;

/**
 * Created by Mar on 13-11-19.
 */
public class Warn extends Activity {

    Button exit;
    public static boolean flag=false;
    public static Warn instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.setfont(this);
        setContentView(R.layout.warn);
        instance=this;
        flag=true;
        exit=(Button)findViewById(R.id.warn_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
            }
        });
    }
}
