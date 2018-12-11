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
        Path path = new Path();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float width = dm.widthPixels/2;
        float height = dm.heightPixels/2 + 150;
        Log.d("TAG", width + ", " + height);
        //mCanvas.drawCircle(width, height, 10, mPaint);
        path.moveTo(width, height - width);
        path.lineTo(width/5, height + width/2);
        path.lineTo(width*9/5, height + width/2);
        path.lineTo(width, height-width);
        mCanvas.drawPath(path, mPaint);
    }

    public void draw(int x, int y, String numbers){
        switch(numbers.length()){
            case 3: //drawTriangle(x,y,numbers);
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

       // mCanvas.drawCircle(x,y,10, mPaint);

//        Point point1_draw = new Point((int) x, (int) y);
//        Point point2_draw = new Point((int) x - side, (int) y + height);
//        Point point3_draw = new Point((int) x + side, (int) y + height);
//        mPath.moveTo(point1_draw.x, point1_draw.y);
//        mPath.lineTo(point2_draw.x, point2_draw.y);
//        mPath.lineTo(point3_draw.x, point3_draw.y);
//        mPath.lineTo(point1_draw.x, point1_draw.y);
//
//        mPath.close();
//        invalidate();

        //user polygon
//        Log.d("TAG", "user polygon");
//        y += height * 2/3;
//        int offset;
//        String[] array = num.split(",");
//        for(int i=0; i<3; i++){
//            offset = (Integer.parseInt(array[i]))*2;
//            switch(i) {
//                case 0:
//                    point1_draw = new Point((int) x, (int) y-offset);
//                    break;
//                case 1:
//                    point2_draw = new Point((int) x - offset, (int) y + offset);
//                    break;
//                case 2:
//                    point3_draw = new Point((int) x + offset, (int) y + offset);
//                    break;
//            }
//        }
//        mPath.moveTo(point1_draw.x, point1_draw.y);
//        mPath.lineTo(point2_draw.x, point2_draw.y);
//        mPath.lineTo(point3_draw.x, point3_draw.y);
//        mPath.lineTo(point1_draw.x, point1_draw.y);
    }

    public void drawRectangle(float x, float y, String num){

    }

    public void drawPentagon(float x, float y, String num){

    }

    public void drawHexagon(float x, float y, String num){

    }

}
