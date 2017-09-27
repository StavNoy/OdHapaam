package project.stav.odhapaam2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

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
    private static final String LOG_KEY = "stayLogged";
    private static final String NAME_KEY = "uName";
    private static final String PASS_KEY = "uPass";


    private static SharedPreferences getPref (final Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    static synchronized int getScore(final Context context){
        return getPref(context).getInt(SCORE_KEY, 0);
    }

    static synchronized void setScore(final Context context,final int score){
        getPref(context).edit().putInt(SCORE_KEY, score).apply();
    }

    public static synchronized Drawable[] getImages(final Context context){
        final Drawable [] imageFiles = new Drawable[4];
        final Set<String> prefImgs = getPref(context).getStringSet(IMAGES_KEY,new HashSet<String>(0));
        //if not initialized - use default images
        if(!prefImgs.isEmpty()){
            for(int i =0 ; i<imageFiles.length ; i++){
                imageFiles[i]=Drawable.createFromPath(prefImgs.iterator().next());
            }
        }
        return imageFiles;
    }

    public static synchronized void setImages(final Context context,final HashSet<String> paths){
        getPref(context).edit().putStringSet(IMAGES_KEY,paths).apply();
    }

    public static synchronized void setStayLogged(final Context context,final Boolean stayLogged){
        getPref(context).edit().putBoolean(LOG_KEY, stayLogged).apply();
    }
    public static synchronized boolean getStayLogged(final Context context){
        return getPref(context).getBoolean(LOG_KEY, false);
    }

    public static synchronized void saveName(final Context context, final String uName){
        getPref(context).edit().putString(NAME_KEY,uName).apply();
    }
    public static synchronized String getName(final Context context){
        return getPref(context).getString(NAME_KEY, null);
    }
    public static synchronized void savePass(final Context context, final String uPass){
        getPref(context).edit().putString(PASS_KEY,uPass).apply();
    }
    public static synchronized String getPass(final Context context){
        return getPref(context).getString(PASS_KEY, null);
    }

}
