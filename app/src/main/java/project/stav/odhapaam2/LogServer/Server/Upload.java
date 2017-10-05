package project.stav.odhapaam2.LogServer.Server;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

import project.stav.odhapaam2.R;
import project.stav.odhapaam2.SharedPrefs;
import project.stav.odhapaam2.WelcomeActivity;

/**
 * Created by Noy on 27/9/2017.
 */

public enum Upload {
    INSTANCE;
    public static final String PATH_SAVE = "/save", PATH_SIGNUP = "/signup";
    public void upLoad(final Context context,final String NAME, final String PASS, final int p, final String path) {
        if (WelcomeActivity.checkConnect(context)) {
            final String url = WelcomeActivity.SERVER_URL+path;
            try {
                if (valid(NAME) && valid(PASS)) {
                    final JSONObject uData = new JSONObject().put("name", NAME).put("pass", PASS).put("points", p);
                    new JSONLoadTask().execute(url, uData.toString());
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private Boolean valid(final String txt){
        return txt != null && !txt.isEmpty();
    }

}
