package project.stav.odhapaam2.LogServer.Server;

import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import project.stav.odhapaam2.SharedPrefs;

/**
 * Created by Noy on 27/9/2017.
 */

public enum Upload {
    INSTANCE;
    public static final String SAVE = "/save", SIGNUP = "/signup";
    public void upLoad(final Context context, final int p, final String action) {
        final String url = context.getResources().getString(R.string.server_url)+action;
        try {
            final String NAME = SharedPrefs.getName(context), PASS = SharedPrefs.getPass(context);
            if (valid(NAME) && valid(PASS)) {
                final JSONObject uData = new JSONObject().put("name", NAME).put("pass", PASS).put("points", p);
                new JSONLoadTask().execute(url, uData.toString());
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private Boolean valid(final String txt){
        return txt != null && !txt.isEmpty();
    }

}
