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

/**
 * Created by Noy on 27/9/2017.
 */

public class HighScoreActivity extends AppCompatActivity {
    private final String localHostServerUrl = getResources().getString(R.string.server_url)+"/highscore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore_activity);
        final RecyclerView highScore = (RecyclerView) findViewById(R.id.highscore);
        highScore.setLayoutManager(new LinearLayoutManager(this));
        highScore.setAdapter(new ScoresAdapter(tryGetScores(),this));
    }

    public void goHome(View v) {//Return to welcome screen
        finish();
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
