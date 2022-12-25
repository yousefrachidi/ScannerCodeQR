package com.example.canva.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawLine extends View {
    Paint paint = new Paint();
    private float x,y;

    public DrawLine(Context context, float x, float y) {
        super(context);
        this.x = x;
        this.y = y;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.TRANSPARENT);
        paint.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas canvas) {

        int redium = 20;
        canvas.drawPaint(paint);
        paint.setColor(Color.parseColor("#FF03DAC5"));
        canvas.drawCircle(x  , y  , redium, paint);

    }


}
