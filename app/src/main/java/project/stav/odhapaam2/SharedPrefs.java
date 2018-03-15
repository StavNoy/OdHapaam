package project.stav.odhapaam2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


class SharedPrefs {
    //for production - encrypt all keys
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

    static synchronized Drawable[] getImages(final Context context){
        final Drawable [] imageFiles = new Drawable[4];
        final Set<String> prefImgs = getPref(context).getStringSet(IMAGES_KEY,new HashSet<String>(4));
        //if not initialized - use default images
        Iterator iterator = prefImgs.iterator();
        if(!prefImgs.isEmpty()){
            for(int i =0 ; i<imageFiles.length ; i++){
                imageFiles[i]=Drawable.createFromPath( (String) iterator.next());
            }
        }
        return imageFiles;
    }

    static synchronized void setImages(final Context context, final HashSet<String> paths){
        getPref(context).edit().putStringSet(IMAGES_KEY,paths).apply();
    }

    static synchronized void setStayLogged(final Context context, final Boolean stayLogged){
        getPref(context).edit().putBoolean(LOG_KEY, stayLogged).apply();
    }
    static synchronized boolean getStayLogged(final Context context){
        return getPref(context).getBoolean(LOG_KEY, false);
    }

    static synchronized void saveName(final Context context, final String uName){
        getPref(context).edit().putString(NAME_KEY,uName).apply();
    }
    static synchronized String getName(final Context context){
        return getPref(context).getString(NAME_KEY, "");
    }
    static synchronized void savePass(final Context context, final String uPass){
        getPref(context).edit().putString(PASS_KEY,uPass).apply();
    }
    static synchronized String getPass(final Context context){
        return getPref(context).getString(PASS_KEY, "");
    }

}
