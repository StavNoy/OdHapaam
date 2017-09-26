package project.stav.odhapaam2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.File;
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

    public static synchronized Drawable[] getImages(Context context){
      Drawable [] imageFiles = new Drawable[4];
        Set<String> prefImgs = getPref(context).getStringSet(IMAGES_KEY,new HashSet<String>(0));// Maybe: make LinkedhHashSet? not good for predefined patterns
        //if not initialized - use default images
        if(!prefImgs.isEmpty()){
            for(int i =0 ; i<imageFiles.length ; i++){
                imageFiles[i]=Drawable.createFromPath(prefImgs.iterator().next());
            }
        }
        return imageFiles;
    }

    public static synchronized void setImages(Context context, HashSet<String> paths){


        getPref(context).edit().putStringSet(IMAGES_KEY,paths).apply();

    }

}
