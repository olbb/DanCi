package com.readboy.learnword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Mar on 13-11-22.
 */
public class WarnZhuCe extends Activity{

    Button gozhuce;
    public static WarnZhuCe instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warnzhuce);
        gozhuce=(Button)findViewById(R.id.warn_zhuce);
        instance=this;
        gozhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setClassName("com.readboy.personalsetting", "com.readboy.personalsetting.activity.LandingActivity");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
                finish();
            }
        });
    }


}
