package nope.notsdsmt.dgfae.pongbrubakernoah;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.HashSet;

public class Model {
    public interface Observer {
        void update(Bundle message);
    }

    public static class Timer{
        public static final String DELTA = "delta";
        private double prevTime;
        public Timer(){
            prevTime = SystemClock.uptimeMillis() * 0.001;
        }
        public void tick(){
            double time = SystemClock.uptimeMillis() * 0.001;
            Bundle msg = new Bundle();
            msg.putDouble(DELTA, time - prevTime);
            msg.putString(ObserverManager.TYPE, DELTA);
            ObserverManager.updateAll(this, msg);
            prevTime = time;
        }
        public void reset(){
            prevTime = SystemClock.uptimeMillis() * 0.001;
        }
    }

    public static class ObserverManager {
        public static final String TYPE = "type";
        private static HashMap<Object, HashSet<Observer>> sub2obs = new HashMap<>();
        public static void attach(Object subject, Observer observer){
            if( !sub2obs.containsKey(subject) ){
                sub2obs.put(subject, new HashSet<Observer>());
            }
            sub2obs.get(subject).add(observer);
        }
        public static void detach(Object subject, Observer observer){
            if( sub2obs.containsKey(subject) ){
                sub2obs.get(subject).remove(observer);
            }
        }
        public static void updateAll(Object subject, Bundle message){
            if( sub2obs.containsKey(subject) ){
                for(Observer observer: sub2obs.get(subject)) {
                    observer.update(message);
                }
            }
        }
    }

    public static class Score implements Observer {
        public static final String SCORE = "score";
        private int score = 0;
        public void reset(){
            score = 0;
        }
        public void update(Bundle msg){
            switch( msg.getString(ObserverManager.TYPE,"")){
                case Ball.SCORED:
                    Log.d("score", ""+score);
                    score += 1;
                    updateScore();
                    break;
            }
        }
        private void updateScore(){
            Bundle msg = new Bundle();
            msg.putInt(SCORE, score);
            ObserverManager.updateAll(this, msg);
        }
    }
    
    public Ball ball;
    public Paddle paddle;
    public Score score;
    public Timer animation_timer;
    public Timer game_timer;

    public Model(){
        ball = new Ball(0.03, 0.6, Math.random() * Math.PI * 2);
        paddle = new Paddle();
        score = new Score();
        animation_timer = new Timer();
        game_timer = new Timer();
        // The score listens is updated by the ball when the ball leaves the game area
        ObserverManager.attach(ball, score);
        // The ball is updated by the timer every on draw.
        ObserverManager.attach(animation_timer, ball);
    }

    public void onDraw(Canvas canvas, double width, double height){
        canvas.save();
        canvas.translate((float)(ball.shape.posX * width), (float)(ball.shape.posY * height));
        ball.shape.onDraw(canvas, width, height);
        canvas.restore();
        canvas.save();
        canvas.translate((float)(paddle.shape.posX * width), (float)(paddle.shape.posY * height));
        paddle.shape.onDraw(canvas, width, height);
        canvas.restore();

        ball.colliding(paddle);
        animation_timer.tick();
    }
}
