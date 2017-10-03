package project.stav.odhapaam2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Noy on 3/10/2017.
 */

public class ScoresAdapter extends RecyclerView.Adapter{
    final Context context;
    final JSONObject scoreList;
    public ScoresAdapter(final JSONObject scoreList, final Context context){
        this.context=context;
        this.scoreList = scoreList;
    }
//    public int getItemCount() {
//        return scores.length();
//    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScoreViewHolder((TextView) LayoutInflater.from(context).inflate(R.layout.highscore_item, null));
    }
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
//        final String name = scoreList.names().getString(i);
//        ((ScoreViewHolder)holder).nameScore.setText(scoreList.getInt(name).toString()+" "+name);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private void arrangeByScore(JSONObject list) throws JSONException {
        Map<String, Integer> names= new HashMap();
        Iterator iter = list.keys();
        while (iter.hasNext()){
            String key = iter.next().toString();
            names.put(key, list.getInt(key));
        }
//        Collections.sort(names, new Comparator<Object>() {
//
//        });
    }
}
