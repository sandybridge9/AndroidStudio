package com.example.destroyer.lab2mobileapps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Switch;

public class IndicatingView extends View {

    public static final int NOTEXECUTED = 0;
    public static final int SUCCESS = 1;
    public static final int FAILED = 2;
    public static final int EXECUTING = 3;
    public static final int CIRCLE = 4;

    int state = NOTEXECUTED;
    int count = 0;

    public IndicatingView(Context context) { super(context);}

    public IndicatingView(Context context, AttributeSet attrs) { super(context, attrs);}

    public IndicatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getState(){ return state; }

    public void setState(int state) { this.state = state; }

    public void setCount(int count){
        this.count = count;
    }

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        Paint paint;
        switch(state){
            case SUCCESS:
                paint = new Paint();
                paint.setColor(Color.GREEN);
                paint.setStrokeWidth(20f);
                //checkmark
                canvas.drawLine(0,0,width/2, height, paint);
                canvas.drawLine(width/2, height, width, height/2, paint);
                break;

            case FAILED:
                paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStrokeWidth(20f);
                //X
                canvas.drawLine(0,0, width, height, paint);
                canvas.drawLine(0, height, width, 0, paint);
                break;

            case EXECUTING:
                paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setStrokeWidth(20f);
                //TRIANGLE
                canvas.drawLine(0,0, width/2, height, paint);
                canvas.drawLine(width/2, height, width, 0, paint);
                canvas.drawLine(0, 0, width, 0, paint);
                break;

            case CIRCLE:
                paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(20f);
                paint.setTextSize(width/2);
                canvas.drawCircle(width/2, height/2, height/2, paint);
                paint.setColor(Color.WHITE);
                canvas.drawText(Integer.toString(count), width*0.1f, height*0.7f, paint);
                break;

                default:
                    break;
        }
    }
}
