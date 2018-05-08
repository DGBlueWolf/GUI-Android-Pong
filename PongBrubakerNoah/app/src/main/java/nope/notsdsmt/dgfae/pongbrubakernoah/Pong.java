package nope.notsdsmt.dgfae.pongbrubakernoah;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Pong extends AppCompatActivity {
    TextView score;
    private class ScoreObserver implements Model.Observer {
        public TextView score;
        public void update(Bundle msg){
            PongView view = findViewById(R.id.game_view);
            String score = view.getResources().getString(R.string.pong_score) + msg.getInt(Model.Score.SCORE,0);
            this.score.setText(score);
            Log.d("here", "here");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pong);
        ScoreObserver sObserver = new ScoreObserver();
        PongView view = findViewById(R.id.game_view);
        score = findViewById(R.id.pong_score);
        sObserver.score = score;
        Model.ObserverManager.attach(view.model.score, sObserver);
    }
}
