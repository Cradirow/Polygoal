package com.android.yunbumhan.polygoal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

public class Polygon extends View {
    Paint mPaint;
    Path mPath;
    Canvas mCanvas;

    enum Mode{
        TRIANGLE,
        RECTANGLE,
        PENTAGON,
        HEXAGON
    }

    private Mode mode;

    private int mViewWidth = 0;
    private int mViewHeight = 0;

    public Polygon(Context context){
        super(context);
        initMyView();
    }

    public Polygon(Context context, AttributeSet attrs){
        super(context, attrs);
        initMyView();
    }

    public void initMyView(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(2);

        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        mCanvas = canvas;

        mCanvas.drawPath(mPath, mPaint);
    }

    public void drawTriangle(float x, float y){
        mode = Mode.TRIANGLE;
        int side = 400;
        int height = 600;
        y = y-height;

        mPath.reset();

        Point point1_draw = new Point((int)x, (int)y);
        Point point2_draw = new Point((int)x-side, (int)y+height);
        Point point3_draw = new Point((int)x+side, (int)y+height);
        mPath.moveTo(point1_draw.x, point1_draw.y);
        mPath.lineTo(point2_draw.x, point2_draw.y);
        mPath.lineTo(point3_draw.x, point3_draw.y);
        mPath.lineTo(point1_draw.x, point1_draw.y);

        mPath.close();
        invalidate();
    }
}
