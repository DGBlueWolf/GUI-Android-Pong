package nope.notsdsmt.dgfae.pongbrubakernoah;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;

public class Ball extends CollisionElement implements Model.Observer {
    public static final String SCORED = "scored";
    public static Paint ballPaint;
    private double base_speed, speed, dir;

    public Ball(double radius, double speed, double heading){
        super( new PongView.Circle(radius, ballPaint) );
        this.base_speed = speed;
        this.dir = heading;
        shape.posX = 0.5;
        shape.posY = 0.5;
    }

    public void onCollide(CollisionElement target){
        if( target instanceof Paddle ){
            double dx = target.shape.posX - shape.posX - target.shape.wid / 2;
            int idx = (int) Math.floor( -(6.0 * dx) / target.shape.wid);
            Log.d("idx", ""+idx);
            try {
                dir = Math.toRadians(-Paddle.ANGLES[idx]);
            } catch ( IndexOutOfBoundsException e ){
                if( idx < 0 ){
                    dir =  Math.toRadians(360-Paddle.ANGLES[0]);
                } else if ( idx > 5 ){
                    dir = Math.toRadians(360-Paddle.ANGLES[5]);
                } else {
                    dir = Math.toRadians(90);
                }
            }
        }
    }

    public void reset(){
        speed = base_speed * Math.sqrt(shape.VIEW_ASPECT_RATIO);
        shape.posX = 0.5;
        shape.posY = 0.5;
    }

    public void update(Bundle msg){
        double radius = ((PongView.Circle) shape).radius;
        if (shape.posX < radius) {
            dir = Math.toRadians(180) - dir;
        } else if (shape.posX > 1 - radius){
            dir = Math.toRadians(180) - dir;
        } else if (shape.posY < radius) {
            dir = Math.toRadians(360) - dir;
        } else if (shape.posY > 1 - radius / shape.VIEW_ASPECT_RATIO){
            Bundle msg2 = new Bundle();
            msg.putString(Model.ObserverManager.TYPE, SCORED);
            Model.ObserverManager.updateAll(this, msg2);
            reset();
        }
        switch (msg.getString(Model.ObserverManager.TYPE,"")){
            case Model.Timer.DELTA:
                double delta = msg.getDouble(Model.Timer.DELTA, 0);
                Log.d("delta", ""+delta);
                shape.posX += speed * delta * Math.cos(dir);
                shape.posY += speed * delta * Math.sin(dir) / shape.VIEW_ASPECT_RATIO;
                Log.d("location",""+shape.posX+", "+shape.posY);
                break;
            default:
                break;
        }
    }
}
