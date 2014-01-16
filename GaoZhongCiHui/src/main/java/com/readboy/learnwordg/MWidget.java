package com.readboy.learnwordg;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.readboy.learnwordg.util.Util;

/**
 * Created by mao on 13-11-2.
 */
public class MWidget extends AppWidgetProvider {

    SharedPreferences sh;
    SharedPreferences.Editor ed;
    int stage;
    TextView t;

    RemoteViews rv;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);


//        Log.d("LearnWord","LearnWord Widget update");
        if (rv == null) {
            rv = new RemoteViews(context.getPackageName(), R.layout.cihuiwidget);
        }


//        Intent startAct=new Intent(context,Entrance.class);
//        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,startAct,0);
//
//        rv.setOnClickPendingIntent(R.id.widget_stage,pendingIntent);

//            this.rv = new RemoteViews(context.getPackageName(), R.layout.cihuiwidget);
//            Intent localIntent2 = new Intent("goto");
//            localIntent2.setClass(context, Entrance.class);
//            PendingIntent localPendingIntent1 = PendingIntent.getActivity(context, 0, localIntent2, 0);
//            this.rv.setOnClickPendingIntent(R.id.widget_goto, localPendingIntent1);

        rv = new RemoteViews(context.getPackageName(), R.layout.cihuiwidget);
        Intent i = new Intent(context, Entrance.class);
        PendingIntent localPending = PendingIntent.getActivity(context, 0, i, 0);
        rv.setOnClickPendingIntent(R.id.widget_goto, localPending);


        appWidgetManager.updateAppWidget(appWidgetIds, rv);


    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

//        Log.d("LearnWord","LearnWord Widget onReceive"+intent.getAction());

        //        if(sh==null){
//            sh=context.getSharedPreferences("barrier",Context.MODE_PRIVATE);
//        }
//        stage=sh.getInt("curstage", 1);
//        if(rv==null){
        rv = new RemoteViews(context.getPackageName(), R.layout.cihuiwidget);
//        }

//        if(stage==0){
        sh = context.getSharedPreferences("barrier", Context.MODE_PRIVATE);
        stage = sh.getInt("curstage", 0);
//        }
        if (Util.curstage > stage) {
            stage = Util.curstage;
        }
        int paiming;
        String shijian;
        shijian = sh.getString("zongyongshi", "--");
        paiming = sh.getInt("zongpaiming", 0);

        rv.setTextViewText(R.id.widget_zongyongshi, shijian);

        if (paiming == 0) {
            rv.setTextViewText(R.id.widget_paiming, "-");
            rv.setTextViewText(R.id.widget_ming, "");
        } else {
            rv.setTextViewText(R.id.widget_paiming, paiming + "");
            rv.setTextViewText(R.id.widget_ming, "名");
        }


//        if(stage<1){
//            stage=1;
//        }

        if (stage > 46) {
            stage = 46;
        }
        if (stage < 10) {
            rv.setTextViewText(R.id.widget_stage, "0" + stage);
        } else {
            rv.setTextViewText(R.id.widget_stage, stage + "");
        }
        rv.setTextViewText(R.id.widget_yiwanchen, stage * 100 / 46 + "%");
        rv.setTextViewText(R.id.widget_weiwanchen, (100 - stage * 100 / 46) + "%");


//        Log.i("LearnWord",stage * 100 / 36+" ____");
        rv.setInt(R.id.widget_jindu, "setStep", 6);

        rv.setInt(R.id.widget_jindu, "setColor", Color.argb(255, 52, 212, 2));
        rv.setInt(R.id.widget_jindu, "setStrokeWidth", 20);
        rv.setInt(R.id.widget_jindu, "setBackColor", Color.argb(255, 1, 169, 254));
        rv.setFloat(R.id.widget_jindu, "setTextSize", 40);
        rv.setInt(R.id.widget_jindu, "setRate", stage * 100 / 46);


        Intent i = new Intent(context, Entrance.class);
        PendingIntent localPending = PendingIntent.getActivity(context, 0, i, 0);
        rv.setOnClickPendingIntent(R.id.widget_goto, localPending);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appids = appWidgetManager.getAppWidgetIds(new ComponentName(context, MWidget.class));
        appWidgetManager.updateAppWidget(appids, rv);
//        Log.i("LearnWord","Widget update curstage="+stage);


//        views.setInt(COURSE_RING_IDS[i], "setRate", i * 10 == 0 ? 1 : i * 10);
//        views.setInt(COURSE_RING_IDS[i], "setColor", COLORS[i]);
//        views.setInt(COURSE_RING_IDS[i], "setStrokeWidth", 7);


    }


}
