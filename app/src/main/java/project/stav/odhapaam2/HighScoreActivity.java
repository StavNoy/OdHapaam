package project.stav.odhapaam2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import project.stav.odhapaam2.LogServer.Server.JSONLoadTask;

/**
 * Created by Noy on 27/9/2017.
 */

public class HighScoreActivity extends AppCompatActivity {
    private static final String serverUrl = "http://127.0.0.1:9999/highscore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore_activity);
        final RecyclerView highScore = (RecyclerView) findViewById(R.id.highscore);
        highScore.setLayoutManager(new LinearLayoutManager(this));
        highScore.setAdapter(new ScoresAdapter(arrangeScores(tryGetScores()),this));
    }

    private Map<String, Integer> tryGetScores(){
    final Map<String,Integer> scores = new HashMap<>();
        new JSONLoadTask(){
            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                if (jsonObject != null){
                    Iterator keys = jsonObject.keys();
                    while (keys.hasNext()){
                        String key = keys.next().toString();
                        try {
                            scores.put(key,jsonObject.getInt(key));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.execute(serverUrl);
    return scores;
    }
    private Map.Entry<String,Integer>[] arrangeScores(final Map<String,Integer> scores){
        SortedSet<Map.Entry<String,Integer>> sortedEntries = new TreeSet<Map.Entry<String,Integer>>(
                new Comparator<Map.Entry<String,Integer>>() {
                    @Override public int compare(Map.Entry<String,Integer> e1, Map.Entry<String,Integer> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(scores.entrySet());
//        TreeMap<String,Integer> newMap = new TreeMap<>();
//        for (Map.Entry<String,Integer> e: sortedEntries){
//            newMap.put(e.getKey(),e.getValue());
//        }
//        return newMap;
        Map.Entry<String,Integer>[] arr = new Map.Entry[sortedEntries.size()];
        Iterator<Map.Entry<String,Integer>> iter = sortedEntries.iterator();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = iter.next();
        }
        return arr;
    }
}
