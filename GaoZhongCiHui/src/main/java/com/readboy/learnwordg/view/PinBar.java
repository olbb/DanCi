package com.readboy.learnwordg.view;

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
public class PinBar extends View {


    public int precent = 75;
    public int angle;

    public PinBar(Context context) {
        super(context);
    }

    public PinBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PinBar(Context context, AttributeSet attrs, int defStyle) {
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
        int r = 27;
        int g = 207;
        int b = 221;
        paint.setColor(Color.rgb(r, g, b));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(25);

        angle = 360 * (precent) / 100;
        canvas.drawArc(new RectF(25, 25, 215, 215), -90, angle + 1, false, paint);

        paint.setColor(Color.rgb(255, 180, 0));

        canvas.drawArc(new RectF(25, 25, 215, 215), angle - 90, 360 - angle, false, paint);

    }
}
