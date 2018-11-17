package com.mou.complex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;


public class Dpad extends android.support.v7.widget.AppCompatButton {


    public static final int CENTER = 0, LEFT =1, UP = 2, RIGHT = 3, DOWN = 4;
    private final String TAG = "Dpad"+this.toString();
    private int selectedButtonIndex;
    private boolean isSelected;
    private Paint fillPaintAlpha70;
    private Point[] buttonsPositions;
    private OnDpadClickListener onDpadClickListener;
    public Dpad(Context context) {
        super(context);
        init();


    }

    public Dpad(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public Dpad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private  void  init(){
        fillPaintAlpha70 = new Paint();
        fillPaintAlpha70.setColor(Color.WHITE);
        fillPaintAlpha70.setAlpha(120);
        fillPaintAlpha70.setStyle(Paint.Style.FILL);
        buttonsPositions = new Point[5];
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                buttonsPositions[CENTER]= new Point(getWidth()/2, getHeight()/2);
                buttonsPositions[LEFT]= new Point(getWidth()/4, getHeight()/2);
                buttonsPositions[UP] = new Point(getWidth()/2, getHeight()/4);
                buttonsPositions[RIGHT] = new Point((int) (0.75f * getWidth()), getHeight()/2);
                buttonsPositions[DOWN] = new Point(getWidth()/2, (int) (0.75f * getHeight()));
            }
        });

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    public void setOnClickListener(View.OnClickListener l) {//disable
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //determine which arrow is selected
        int x, y;
        x = (int) ( event.getX() - getWidth()/2);
        y = (int) ( event.getY() - getHeight()/2);

        if(x==0)x=1;
        if(y==0)y=1;
        if(Math.abs(y)<=0.15f*getHeight() && Math.abs(x)<= 0.15f*getWidth()){//middle button
            selectedButtonIndex = CENTER;
        }else if( y/(float)x <1 && y/(float)x > -1 && x>0){
            selectedButtonIndex = RIGHT;
        }else if(y/(float)x < 1 && y/(float)x > -1 && x < 0){
            selectedButtonIndex = LEFT;
        }else if( (y/(float)x >1 || y/(float)x < -1) && y>0){
            selectedButtonIndex = DOWN;
        }else if((y/(float)x >1 || y/(float)x < -1) && y<0){
            selectedButtonIndex = UP;
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            isSelected = true;
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){

            isSelected = false;
            if(onDpadClickListener != null) onDpadClickListener.onClick(selectedButtonIndex);
        }
        invalidate();
        return  true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = getWidth()/6;
        if(isSelected){
            switch (selectedButtonIndex){
                case LEFT :
                    canvas.drawCircle(buttonsPositions[LEFT].x, buttonsPositions[LEFT].y , radius, fillPaintAlpha70);
                    break;
                case RIGHT :
                    canvas.drawCircle(buttonsPositions[RIGHT].x, buttonsPositions[RIGHT].y , radius, fillPaintAlpha70);
                    break;
                case UP:
                    canvas.drawCircle(buttonsPositions[UP].x, buttonsPositions[UP].y , radius, fillPaintAlpha70);

                    break;
                case DOWN:
                    canvas.drawCircle(buttonsPositions[DOWN].x, buttonsPositions[DOWN].y , radius, fillPaintAlpha70);
                    break;

                case CENTER:
                    canvas.drawCircle(buttonsPositions[CENTER].x, buttonsPositions[CENTER].y , radius, fillPaintAlpha70);
                    break;

            }
        }

    }

    public void setOnDpadClickListener(OnDpadClickListener onDpadClickListener) {
        this.onDpadClickListener = onDpadClickListener;
    }

    public interface OnDpadClickListener{
        void onClick(int selection);
    }
}
