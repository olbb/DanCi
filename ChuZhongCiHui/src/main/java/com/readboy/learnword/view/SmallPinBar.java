package com.readboy.learnword.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Mar on 13-10-8.
 */
public class SmallPinBar extends View {


    public int precent = 75;
    public int angle;

    public SmallPinBar(Context context) {
        super(context);
    }

    public SmallPinBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmallPinBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public void setPrecent(int precent) {
        this.precent = precent;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
//        int r=27;int g=207;int b=221;

        paint.setColor(Color.rgb(255, 180, 0));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);

        angle = 360 * (precent) / 100;
        canvas.drawArc(new RectF(10, 10, 150, 150), -90, angle + 1, false, paint);

        paint.setColor(Color.WHITE);

        canvas.drawArc(new RectF(10, 10, 150, 150), angle - 90, 360 - angle, false, paint);

    }
}
