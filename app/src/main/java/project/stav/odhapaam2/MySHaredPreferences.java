package project.stav.odhapaam2;

import android.content.SharedPreferences;
import android.net.Uri;

/**
 * Created by hackeru on 19/09/2017.
 */

public class MySharedPreferences {
    SharedPreferences prefs;

    public MySharedPreferences(SharedPreferences prefs) {
        this.prefs = prefs;
    }
    public int getScore(){
        return prefs.getInt("score",0);
    }
    public void setScore(int newScore){
        if (newScore>=0) prefs.edit().putInt("score",newScore);
    }
    public Uri [] getImages(){
        return prefs.getString("images","android.resource://[project.stav.odhapaam2]/[R.drawable.triangle]")
    }

}
