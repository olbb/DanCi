package com.readboy.learnwordx.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.readboy.learnwordx.LearnErrorWord;
import com.readboy.learnwordx.LearnWord;
import com.readboy.learnwordx.R;
import com.readboy.learnwordx.TestWords;
import com.readboy.learnword.util.AiWriteLib;
import com.readboy.learnwordx.util.Util;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mar on 13-9-17.
 */
public class TouchpadView extends View implements View.OnTouchListener {


    public boolean learn = true;
    Context context;


    public TouchpadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        Log.i(tag,"TouchpadView(Context context, AttributeSet attrs, int defStyle)");
        this.context = context;
        if (isInEditMode()) {
            return;
        }
        load();
    }

    public TouchpadView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        Log.i(tag,"TouchpadView(Context context, AttributeSet attrs)");
        this.context = context;
        if (isInEditMode()) {
            return;
        }
        load();


    }


    public TouchpadView(Context context) {
        super(context);
//        Log.i(tag," TouchpadView(Context context)");
        this.context = context;
        if (isInEditMode()) {
            return;
        }
        load();

    }

    void load() {
        aiwrite = new AiWriteLib();
        aiwrite.mainLoad();
        aiwrite.readFromAssets(context.getAssets(), "AiWrite_130401.dat");
        hwpoints = new ArrayList<Point>();
        mpath = new Path();
        paint = new Paint();
        paint.setColor(Color.BLACK);// 设置为黑色
        paint.setStyle(Paint.Style.STROKE);// 设置非填充
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);// 锯齿不显示
        mh = new Handler();

    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        if (mpath != null)
            canvas.drawPath(mpath, paint);
    }

    public List<Point> hwpoints;
    String tag = "LearnWord";
    AiWriteLib aiwrite;
    public Path mpath;
    Paint paint;
    float mx, my;

    public Handler mh;

    public Runnable rr = new Runnable() {
        @Override
        public void run() {

//            System.out.println("post__________"+hwpoints.size());
            if (hwpoints.size() > 10) {
                touchresult();
            } else {
                mpath.reset();
                invalidate();
            }

        }
    };

    boolean flag;

    @Override
    public boolean onTouch(View view, MotionEvent e) {

//        Log.i("onTouch",e.toString());

        float x = e.getX();
        float y = e.getY();
        if (y < 0) {
            if (!flag && (e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_CANCEL)) {
                flag = true;
                hwpoints.add(new Point(-1, -1));
                mh.postDelayed(rr, 500);
            } else {
                mpath.moveTo(x, y);
            }
            return true;


        }


        if (view.getId() == R.id.spell_touchpad) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
//                    mh.postDelayed(rr,1000);
                    flag = false;
                    hwpoints.add(new Point((int) e.getX(), (int) e.getY()));
                    mpath.moveTo(x, y);
                    mx = x;
                    my = y;
                    mh.removeCallbacks(rr);
                    break;

                case MotionEvent.ACTION_MOVE:
                    hwpoints.add(new Point((int) e.getX(), (int) e.getY()));
                    mpath.quadTo(mx, my, (x + mx) / 2, (y + my) / 2);
                    mx = x;
                    my = y;


                    break;

                case MotionEvent.ACTION_UP:
                    hwpoints.add(new Point((int) e.getX(), (int) e.getY()));
                    hwpoints.add(new Point(-1, -1));
                    mh.postDelayed(rr, 500);
                    break;

                case MotionEvent.ACTION_CANCEL:
                    hwpoints.add(new Point((int) e.getX(), (int) e.getY()));
                    hwpoints.add(new Point(-1, -1));
                    mh.postDelayed(rr, 500);
                    break;

            }
            invalidate();
            return true;
        }

        return false;
    }

    public void touchresult() {

        int xx = hwpoints.size() * 2;
        int temp[] = new int[xx];
        for (int i = 0; i < hwpoints.size(); i++) {
            temp[i * 2] = hwpoints.get(i).x;
            temp[i * 2 + 1] = hwpoints.get(i).y;
        }
        byte[] a = aiwrite.getWriteString(temp, 10002);
        String result;

        if (a != null) {
            try {
                result = new String(a, "unicode");
                String tt[] = result.split(" ");
                tt[0] = tt[0].toLowerCase();

                if (learn) {
                    if (Util.curstate.equals("LearnWord")) {
                        if (((LearnWord) context).spell_text.getText().toString().length() < 31)
                            ((LearnWord) context).spell_text.append(tt[0]);

                    } else {
                        if (((LearnErrorWord) context).spell_text.getText().toString().length() < 31)
                            ((LearnErrorWord) context).spell_text.append(tt[0]);
                    }


                } else {
                    if (((TestWords) context).spelled.getText().toString().length() < 31)
                        ((TestWords) context).spelled.append(tt[0]);
                }
//                System.out.println("xx is:"+xx+"     result is:"+result);

                mpath.reset();
                hwpoints.removeAll(hwpoints);
                invalidate();
            } catch (Exception exc) {
            }

        } else {
            mpath.reset();
            hwpoints.removeAll(hwpoints);
            invalidate();
        }
    }
}
