package com.readboy.learnwordx.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.readboy.learnwordx.Barrier;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mar on 14-1-15.
 */
public class AnimView extends View {

    String path;
    int def_time=200;
    Bitmap bm;
    Context context;
    int index;
    int count;
    Handler mh;

    public AnimView(Context context) {
        super(context);
        this.context=context;
        mh=new Handler();
    }

    public AnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        mh=new Handler();
    }

    public AnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        mh=new Handler();
    }

    public void setPath(String file){
        this.path=file;

        count=0;
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    count=context.getAssets().list(path).length;
//            Log.i("DanCi","count is:"+count);
                }catch (IOException e){
                    e.printStackTrace();
                }
                if(count>0){
                    index=1;
                    mh.post(refresh);
                }else {
                    Log.e("DanCi","Load file failed! File path is: "+path);
                }
            }
        });
        thread.start();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(bm!=null)canvas.drawBitmap(bm,0,0,null);
    }

    Runnable refresh=new Runnable() {
        @Override
        public void run() {
//            if(!Barrier.loop)return;
            if(bm!=null)bm.recycle();
            try {
//                Log.i("DanCi","open file:"+path+"/0"+(10000+index)+".png");
                InputStream is=context.getAssets().open(path+"/0"+(10000+index)+".png");

                bm= BitmapFactory.decodeStream(is);
                is.close();
                postInvalidate();
                if(index<count){
                    index++;
                }else {
                    index=1;
                }
                mh.postDelayed(refresh,def_time);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    };

    public void recycl(){
        if(bm!=null)bm.recycle();
    }

    public void stop(){
        mh.removeCallbacks(refresh);
    }
}
