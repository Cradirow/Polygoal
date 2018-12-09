package com.android.yunbumhan.polygoal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
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
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
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
        mCanvas.drawPath(mPath, mPaint);
    }

    public void draw(int x, int y, String numbers){
        switch(numbers.length()){
            case 3: drawTriangle(x,y,numbers); break;
            case 4: drawRectangle(x,y,numbers); break;
            case 5: drawPentagon(x,y,numbers); break;
            case 6: drawHexagon(x,y,numbers); break;
            default: Log.d("TAG", "polygon number errors.");
        }
    }

    //draw base polygon
    public void drawTriangle(float x, float y, String num){
        mode = Mode.TRIANGLE;
        int side = 400;
        int height = 600;
        x /= 2;
        y = y/2 + 350 - height;

        mPath.reset();

        Point point1_draw = new Point((int) x, (int) y);
        Point point2_draw = new Point((int) x - side, (int) y + height);
        Point point3_draw = new Point((int) x + side, (int) y + height);
        mPath.moveTo(point1_draw.x, point1_draw.y);
        mPath.lineTo(point2_draw.x, point2_draw.y);
        mPath.lineTo(point3_draw.x, point3_draw.y);
        mPath.lineTo(point1_draw.x, point1_draw.y);

        //user polygon
        Log.d("TAG", "user polygon");
        y += height/2;
        int offset;
        for(int i=0; i<3; i++){
            offset = (num.charAt(i) - '0')*2;
            switch(i) {
                case 0:
                    point1_draw = new Point((int) x, (int) y-offset);
                    break;
                case 1:
                    point2_draw = new Point((int) x - offset, (int) y + offset);
                    break;
                case 2:
                    point3_draw = new Point((int) x + offset, (int) y + offset);
                    break;
            }
        }
        mPath.moveTo(point1_draw.x, point1_draw.y);
        mPath.lineTo(point2_draw.x, point2_draw.y);
        mPath.lineTo(point3_draw.x, point3_draw.y);
        mPath.lineTo(point1_draw.x, point1_draw.y);
    }

    public void drawRectangle(float x, float y, String num){
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

    public void drawPentagon(float x, float y, String num){
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

    public void drawHexagon(float x, float y, String num){
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
