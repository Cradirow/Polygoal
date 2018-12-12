package com.android.yunbumhan.polygoal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class Polygon extends View {
    Paint mPaint;
    Canvas mCanvas;
    Path mPath;

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
        mPaint.setARGB(255,255,210,80);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);

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
        Path path = new Path();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        float width = dm.widthPixels/2;
        float height = dm.heightPixels/2 + 150;
        //Log.d("TAG", width + ", " + height);

        //mCanvas.drawCircle(width, height, 10, mPaint);
        path.moveTo(width, height - width);
        path.lineTo(width/5, height + width/2);
        path.lineTo(width*9/5, height + width/2);
        path.lineTo(width, height-width);

        mCanvas.drawPath(path, paint);
        mCanvas.drawPath(mPath, mPaint);
    }

    public void draw(float x, float y, String numbers){
        String array[] = numbers.split(",");
        switch(array.length){
            case 3: drawTriangle(x,y,numbers);
                break;
            case 4: //drawRectangle(x,y,numbers); break;
            case 5: //drawPentagon(x,y,numbers); break;
            case 6: //drawHexagon(x,y,numbers); break;
            default: Log.d("TAG", "polygon number errors.");
        }
    }

    //draw base polygon
    public void drawTriangle(float x, float y, String num){
        //mode = Mode.TRIANGLE;

        float width = x/2;
        float height = y/2 + 125;

        //user polygon
        String[] array = num.split(",");
        float a,b,c, max, offset;
        a = Float.parseFloat(array[0]);
        b = Float.parseFloat(array[1]);
        c = Float.parseFloat(array[2]);
        if(a>=b){
            if(a>=c) max = a;
            else max = c;
        }else{
            if(b>=c) max = b;
            else max = c;
        }
        max += 5;
        offset = width/max;

        mPath.reset();

        mPath.moveTo(width, height - a*offset);
        mPath.lineTo(width - b*offset, height + b*offset);
        mPath.lineTo(width + c*offset, height + c*offset);
        mPath.lineTo(width, height- a*offset);
//        mPath.moveTo(width, height - width * a/max);
//        mPath.lineTo(width - 4/5*width * b/max, height + width/2 * b/max);
//        mPath.lineTo(width + 4/5*width * c/max, height + width/2 * c/max);
//        mPath.lineTo(width, height- width * a/max);
        mPath.close();
        invalidate();
        Log.d("TAG", "draw end");
    }

    public void drawRectangle(float x, float y, String num){

    }

    public void drawPentagon(float x, float y, String num){

    }

    public void drawHexagon(float x, float y, String num){

    }

}
