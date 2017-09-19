package project.stav.odhapaam2;

import android.content.SharedPreferences;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hackeru on 19/09/2017.
 */

public class MySharedPreferences {
    private SharedPreferences prefs;

    public MySharedPreferences(SharedPreferences prefs) {
        this.prefs = prefs;
    }
    public int getScore(){
        return prefs.getInt("score",0);
    }
    public void setScore(int newScore){
        if (newScore>=0) prefs.edit().putInt("score",newScore).apply();
    }
    public Uri [] getImages(){
        Uri [] images=new Uri[4];
        Set<String> prefImgs = prefs.getStringSet("images",new HashSet<String>(4));
        for(int i =0 ; i<images.length ; i++){
            images[i]=Uri.parse(prefImgs.iterator().next());
        }
        return images;
    }
    public void saveImages(Uri[] uris){
        Set<String> toSave=new HashSet<>();
        for (Uri u : uris){
            toSave.add(u.toString());
        }
        prefs.edit().putStringSet("images",toSave).apply();

    }

}
