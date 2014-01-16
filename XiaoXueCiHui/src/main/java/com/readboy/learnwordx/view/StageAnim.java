package com.readboy.learnwordx.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mar on 14-1-15.
 */
public class StageAnim extends View{

    int index=1;
    int def_time=200;
    Context context;

    public StageAnim(Context context) {
        super(context);
        this.context=context;
    }

    public StageAnim(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public StageAnim(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!loop)return;
        super.onDraw(canvas);
//        this.canvas=canvas;
//        Log.d("StageAnim", "refresh");
//        canvas.drawColor(Color.BLACK);
        drawStages(canvas);

//        postInvalidateDelayed(def_time);
    }

    public boolean loop=true;



    protected void drawStages(Canvas canvas){

        for(int i=1;i<=8;i++){
            if(i==1||i==8){
                drawitem(canvas,"stageview/l_locked",i);
            }else if(i==2||i==7||i==3||i==6){
                drawitem(canvas,"stageview/m_locked",i);
            }else {
                drawitem(canvas,"stageview/s_locked",i);
            }
        }
        if(index<10){
            index++;
        }else {
            index=1;
        }

    }

    int count;
    boolean locked=true;
    int x[]=new int[]{0,479,483,895,802,1176,1325,1760,1868};
    int y[]=new int[]{0,429,160,160,-84,-84,160,160,429};

    Bitmap bm;

    protected void drawitem(Canvas canvas,String path,int i){
        if(locked){count=10;}else{ count=15;}
//        int a=index%count;
        int a=index;

        if(bm!=null)bm.recycle();
        try {
//                Log.i("DanCi","open file:"+path+"/0"+(10000+a)+".png");
            InputStream is=context.getAssets().open(path+"/0"+(10000+a)+".png");
            bm=BitmapFactory.decodeStream(is);

            canvas.drawBitmap(bm,x[i],y[i],null);
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
