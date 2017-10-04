package project.stav.odhapaam2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import project.stav.odhapaam2.LogServer.Server.HttpRequest;

/**
 * Created by Noy on 27/9/2017.
 */

public class HighScoreActivity extends AppCompatActivity {
    private static final String localHostServerUrl = "http://127.0.0.1:9999/highscore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore_activity);
        final RecyclerView highScore = (RecyclerView) findViewById(R.id.highscore);
        highScore.setLayoutManager(new LinearLayoutManager(this));
        highScore.setAdapter(new ScoresAdapter(tryGetScores(),this));
    }

    private JSONArray tryGetScores(){
        JSONArray scores = null;
        try {
            scores = new HttpRequest(localHostServerUrl).prepare(HttpRequest.Method.GET).sendAndReadJSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scores;
    }
}
