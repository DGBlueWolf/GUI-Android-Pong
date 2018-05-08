package nope.notsdsmt.dgfae.pongbrubakernoah;

import android.graphics.Paint;
import android.os.Bundle;

public class Paddle extends CollisionElement implements Model.Observer {
    public static final double ANGLES[] = {165, 135, 105, 75, 45, 15};
    public static Paint paddlePaint;
    public PongView view;

    public Paddle(){
        super( new PongView.Rectangle(0.2, 0.03, paddlePaint));
        shape.posX = 0.5;
        shape.posY = 0.95;
    }
    @Override
    public boolean colliding(CollisionElement target){
        return false;
    }

    public void onCollide(CollisionElement target){}

    public void update(Bundle msg){
        switch (msg.getString(Model.ObserverManager.TYPE,"")){
            case PongView.TOUCHX:
                shape.posX = msg.getDouble(PongView.TOUCHX, 0.5);
            default:
                break;
        }
    }
}
