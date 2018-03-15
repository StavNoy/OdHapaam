package project.stav.odhapaam2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import project.stav.odhapaam2.LogServer.Server.HttpRequest;


public class HighScoreActivity extends AppCompatActivity {

    private static final String HIGH_SCORE_URL = WelcomeActivity.SERVER_URL + "/highscore";

    public void goHome(View v) {//Return to welcome screen
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore_activity);
        final RecyclerView highScore = findViewById(R.id.highscore);
        highScore.setLayoutManager(new LinearLayoutManager(HighScoreActivity.this));
        final JSONArray scores = tryGetScores();
        if (scores != null) highScore.setAdapter(new ScoresAdapter(scores,HighScoreActivity.this));
    }

    private JSONArray tryGetScores(){
        final JSONArray[] scores = {new JSONArray()};
        new Thread(){
            public void run() {
                try {
                    scores[0] = new HttpRequest(HIGH_SCORE_URL).prepare(HttpRequest.Method.GET).sendAndReadJSONArray();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return scores[0];
    }
}
