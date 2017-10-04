package project.stav.odhapaam2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Noy on 3/10/2017.
 */

public class ScoresAdapter extends RecyclerView.Adapter{
    private final Context context;
    private final JSONArray scoreList;
    private int place = 0;

    public ScoresAdapter(final JSONArray scoreList, final Context context){
        this.context=context;
        this.scoreList = scoreList;

    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScoreViewHolder((LinearLayout) LayoutInflater.from(context).inflate(R.layout.highscore_row, null));
    }
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        try {
            final LinearLayout row = ((ScoreViewHolder) holder).row;
            final JSONObject userInfo = scoreList.getJSONObject(i);
            final String name = userInfo.getString("name");
            final String score = (""+userInfo.getInt("points"));
            this.place++;
            final String rowNum = this.place+"";
            ((TextView) row.getChildAt(0)).setText(rowNum);
            ((TextView) row.getChildAt(1)).setText(name);
            ((TextView) row.getChildAt(2)).setText(score);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return scoreList.length();
    }
}
