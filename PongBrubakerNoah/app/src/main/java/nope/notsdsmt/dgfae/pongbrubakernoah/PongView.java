package nope.notsdsmt.dgfae.pongbrubakernoah;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class PongView extends View {
    public static final String TOUCHX = "touchx";
    public enum Orientation {PORTRAIT, LANDSCAPE}
    public Orientation ORIENTATION;
    public double ASPECT_RATIO = -1;
    public double CANVAS_WID = -1;
    public double CANVAS_HIT = -1;

    int touch1;
    public Model model;

    public static abstract class Shape{
        public Paint paint;
        public double VIEW_ASPECT_RATIO;
        public double wid, hit;
        public double posX, posY, theta;

        public Shape(double width, double height, Paint paint){
            this.paint = paint;
            this.wid = width;
            this.hit = height;
            posX = posY = theta = 0;
        }

        public void reset(double aspect_ratio){
            VIEW_ASPECT_RATIO = aspect_ratio;
        }

        public abstract void onDraw(Canvas canvas, double width, double height);
    }

    public static class Circle extends Shape{
        public double radius;
        public Circle(double radius, Paint paint){
            super(radius, radius, paint);
            this.radius = radius;
        }

        @Override
        public void onDraw(Canvas canvas, double width, double height){
            canvas.drawCircle(0, 0, (float) (radius * width), paint);
        }
    }

    public static class Rectangle extends Shape{
        public Rectangle(double width, double height, Paint paint){
            super(width, height, paint);
        }
        public void onDraw(Canvas canvas, double width, double height){
            float x = (float) (wid * width) / 2,
                    y = (float) (hit * height) / 2;
            canvas.drawRect(-x, -y, x, y, paint);
        }
    }

    public PongView(Context context) {
        super(context);
        init(context);
    }

    public PongView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PongView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context)
    {
        Color x = new Color();
        // Create paint for filling the area the puzzle will
        // be solved in.
        Ball.ballPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Ball.ballPaint.setColor(getResources().getColor(R.color.ball));

        Paddle.paddlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paddle.paddlePaint.setColor(getResources().getColor(R.color.paddle));

        model = new Model();

        // The paddle reacts to a touch event from the view
        Model.ObserverManager.attach(this, model.paddle);

        //make touch point the same size no matter the display resoltuion
        //DisplayMetrics metrics = getResources().getDisplayMetrics();
        //dotSize = TOUCH_SIZE * (metrics.densityDpi/160f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //set bounds the on the first draw
        if(CANVAS_WID < 0) {
            CANVAS_WID = canvas.getWidth();
            CANVAS_HIT = canvas.getHeight();
            ASPECT_RATIO = CANVAS_HIT / CANVAS_WID;
            // Determine the minimum of the two dimensions
            ORIENTATION = CANVAS_WID < CANVAS_HIT ? Orientation.PORTRAIT : Orientation.LANDSCAPE;
            model.ball.shape.reset(ASPECT_RATIO);
            model.paddle.shape.reset(ASPECT_RATIO);
            model.ball.reset();
        }

        //Draw things
        canvas.drawLine(0,0,300,300, Ball.ballPaint);
        model.onDraw(canvas, CANVAS_WID, CANVAS_HIT);

        //animating, so redraw when possible
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event){
        int id = event.getPointerId(event.getActionIndex());

        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                touch1 = id;
                getPositions(event);
                return true;
            case MotionEvent.ACTION_POINTER_UP:
                if( id == touch1 ){
                    touch1 = -1;
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touch1 = -1;
                return true;
            case MotionEvent.ACTION_MOVE:
                getPositions(event);
                return true;
        }

        return super.onTouchEvent(event);
    }

    private void getPositions(MotionEvent event){
        for( int i=0; i < event.getPointerCount(); i++ ){
            int id = event.getPointerId(i);

            if( id == touch1 ){
                double touch1x = event.getX(i) / CANVAS_WID;
                double touch1y = event.getY(i) / CANVAS_WID;
                Log.d("touch", ""+touch1x+", "+touch1y);
                Bundle msg = new Bundle();
                msg.putDouble(TOUCHX, touch1x);
                msg.putString(Model.ObserverManager.TYPE, TOUCHX);
                Model.ObserverManager.updateAll(this, msg);
            }
        }
    }
}
