package project.stav.odhapaam2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import project.stav.odhapaam2.LogServer.Server.JSONLoadTask;

/**
 * Created by Noy on 27/9/2017.
 */

public class HighScoreActivity extends AppCompatActivity {
    private static final String serverUrl = "http://127.0.0.1:9999/highscore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RecyclerView highScore = (RecyclerView) findViewById(R.id.highscore);
        highScore.setLayoutManager(new LinearLayouManager(this));
        highScore.setAdapter(new ScoresAdapter(this));
    }

    private void tryGetScores(){
        final JSONObject scores;
        try {
            new JSONLoadTask(){
                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    if (jsonObject != null){
                        Iterator keys = jsonObject.keys();
                        while (keys.hasNext()){
                            String key = keys.next().toString();
                            scores.put(key,jsonObject.getInt(key));
                        }
                    }
                }
            }.execute(serverUrl);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void
}
