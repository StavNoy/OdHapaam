package project.stav.odhapaam2.LogServer.Server;


import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Noy on 27/9/2017.
 */

public class JSONLoadTask extends AsyncTask<String, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(String... strings) {
            try {
                final String URL = strings[0];
                final String jsnStr = strings[1];
                return new HttpRequest(URL).prepare(HttpRequest.Method.POST).withData(jsnStr).sendAndReadJSON();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
    }
}
