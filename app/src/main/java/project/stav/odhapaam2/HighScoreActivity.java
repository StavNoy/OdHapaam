package project.stav.odhapaam2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import project.stav.odhapaam2.LogServer.Server.HttpRequest;

/**
 * Created by Noy on 27/9/2017.
 */

public class HighScoreActivity extends AppCompatActivity {

    private final String highScoreUrl = getResources().getString(R.string.server_url)+"/highscore";

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
        final JSONArray[] scores = {null};
        try {
            scores[0] = new HttpRequest(highScoreUrl).prepare(HttpRequest.Method.GET).sendAndReadJSONArray();
            new AsyncTask<String, Void, JSONArray>(){
                @Override
                protected JSONArray doInBackground(String... strings) {
                    try {
                        final String URL = strings[0];
                            return new HttpRequest(URL).prepare(HttpRequest.Method.GET).sendAndReadJSONArray();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(JSONArray jsonArray) {
                    scores[0] = jsonArray;
                }
            };
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scores[0];
    }
}
