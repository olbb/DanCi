package com.readboy.learnword;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

/**
 * Created by mao on 13-11-2.
 */
public class LearnApp extends Application {

    String seralNumber;

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.d("LearnWord","LearnApp onCreate");
//        Intent i =new Intent(this,Rate.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(i);
        Resources res = getResources();
        Configuration c = res.getConfiguration();
        c.fontScale = 1;
        res.updateConfiguration(c, res.getDisplayMetrics());

//        seralNumber= Build.SERIAL;

//        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        String DEVICE_ID = tm.getDeviceId();
//        Log.d("LearnWord","seralNumber:"+seralNumber);
//        Log.d("LearnWord","USER"+Build.USER);
//        Log.d("LearnWord","DEVICE_ID"+DEVICE_ID);

    }


}
