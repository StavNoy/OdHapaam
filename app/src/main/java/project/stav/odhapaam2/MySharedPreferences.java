package project.stav.odhapaam2;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by stav.noy on 19/09/2017.
 */

public class MySharedPreferences {
    //TODO - for production - encrypt all keys
    private static final String PREFS = "RasheyTevot";
    private static final String SCORE_KEY = "score";
    private static final String IMAGES_KEY = "images";


    private static SharedPreferences getPref (Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static synchronized int getScore(Context context){
        return getPref(context).getInt(SCORE_KEY, 0);
    }

    public static synchronized void setScore(Context context, int score){
        getPref(context).edit().putInt(SCORE_KEY, score).apply();
    }

    public static synchronized Uri [] getImages(Context context){
        Uri [] images = new Uri[4];
        Set<String> prefImgs = getPref(context).getStringSet(IMAGES_KEY,new HashSet<String>(0));
        //if not initialized - use default images
        if(prefImgs.isEmpty()){
//            prefImgs.add("android.resource://[project.stav.odhapaam2]/[R.drawable.triangle]");
//            prefImgs.add("android.resource://[project.stav.odhapaam2]/[R.drawable.triangle]");
//            prefImgs.add("android.resource://[project.stav.odhapaam2]/[R.drawable.triangle]");
//            prefImgs.add("android.resource://[project.stav.odhapaam2]/[R.drawable.triangle]");
        } else {
            for(int i =0 ; i<images.length ; i++){
                images[i]=Uri.parse(prefImgs.iterator().next());
            }
        }

        return images;
    }

    public static synchronized void setImages(Context context, Uri[] uris){
        Set<String> toSave=new HashSet<>();
        for (Uri u : uris){
            toSave.add(u.toString());
        }
        getPref(context).edit().putStringSet("images",toSave).apply();

    }

}
