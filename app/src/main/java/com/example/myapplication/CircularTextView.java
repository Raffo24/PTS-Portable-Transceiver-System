package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.List;
import java.util.Random;

public class CircularTextView extends androidx.appcompat.widget.AppCompatTextView
{
    private float strokeWidth;
    int strokeColor,solidColor;
    private static List<Integer> colors = List.of(R.color.blue,R.color.rossochiaro,R.color.purple_700, R.color.red, R.color.green, Color.BLUE, Color.MAGENTA, Color.YELLOW, Color.GRAY, Color.CYAN, R.color.red, R.color.red_light, R.color.greenCall, R.color.verde_nero, R.color.black, R.color.blue);
    private static Random rand = new Random();
    public static Integer getRandColor() {
        return colors.get(rand.ints(0,colors.size()).findFirst().getAsInt());
    }

    public CircularTextView(Context context) {
        super(context);
    }

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {

        Paint circlePaint = new Paint();
        circlePaint.setColor(solidColor);
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        Paint strokePaint = new Paint();
        strokePaint.setColor(strokeColor);
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        int  h = this.getHeight();
        int  w = this.getWidth();

        int diameter = ((h > w) ? h : w);
        int radius = diameter/2;

        this.setHeight(diameter);
        this.setWidth(diameter);

        canvas.drawCircle(diameter / 2 , diameter / 2, radius, strokePaint);

        canvas.drawCircle(diameter / 2, diameter / 2, radius-strokeWidth, circlePaint);

        super.draw(canvas);
    }

    public void setStrokeWidth(int dp)
    {
        float scale = getContext().getResources().getDisplayMetrics().density;
        strokeWidth = dp*scale;

    }

    public void setStrokeColor(String color)
    {
        strokeColor = Color.parseColor(color);
    }

    public void setSolidColor(Integer color)
    {
        solidColor = color;

    }
}