package project.stav.odhapaam2;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import project.stav.odhapaam2.LogServer.Server.JSONLoadTask;
import project.stav.odhapaam2.LogServer.Server.Upload;

public class LogActivity extends AppCompatActivity {

    private final String loginUrl = getResources().getString(R.string.server_url)+"/login";
    EditText uName, uPass;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_activity);
        context = this;

        uName = (EditText) findViewById(R.id.uName);
        uPass = (EditText) findViewById(R.id.uPass);
    }


    public void tryLogIn(View v){
        if (WelcomeActivity.checkConnect(this)) {
            try {
                final String NAME = uName.getText().toString(), PASS = uPass.getText().toString();
                if (validName(NAME) && validPass(PASS)){
                    final JSONObject uData = new JSONObject().put("name", NAME).put("pass", PASS);
                    new JSONLoadTask(){
                        @Override
                        protected void onPostExecute(JSONObject jsonObject) {
                            if (jsonObject != null){
                                final String errMsg = jsonObject.optString("error",null);
                                if (errMsg!=null){
                                    //Error dialog
                                    errAlert("Incorrect Name and/or Password");
                                } else {
                                    //Confirmation toast
                                    Toast.makeText(context,"Loged In as "+NAME, Toast.LENGTH_LONG).show();
                                    //Save Credentials
                                    SharedPrefs.saveName(context, NAME);
                                    SharedPrefs.savePass(context, PASS);
                                    //Save "Stay signed in" state
                                    SharedPrefs.setStayLogged(context, ((CheckBox)findViewById(R.id.stayLogged)).isChecked());
                                }
                            }else {
                                errAlert("Invalid Request");
                            }
                        }
                    }.execute(loginUrl,uData.toString());
                } else {
                    info(v);
                }
            }catch (JSONException e) {
                    e.printStackTrace();
        }
        }
    }

    public void trySignUp(View v){
        //Requires Internet Connection
        final String NAME = uName.getText().toString(), PASS = uPass.getText().toString();
        if (validName(NAME) && validPass(PASS)) {
            Upload.INSTANCE.upLoad(this,NAME, PASS, SharedPrefs.getScore(this),Upload.PATH_SIGNUP);
            tryLogIn(v);
        } else {
            info(v);
        }
    }

    private AlertDialog errAlert(final String msg){
        return new AlertDialog.Builder(context).setTitle("Error").setMessage(msg).setNeutralButton("OK",null).show();
    }

    private boolean validPass(final String pswrd){
        return pswrd.matches("(?=.*[A-Z]+)(?=.*[a-z]+)(?=.*\\d+)^.{8,}$");
    }
    private boolean validName(final String name){//username must be at least 3 char long with no spaces
        return name.matches("^[^\\s]{3,}$");
    }

    public void goHome(View v) {//Return to welcome screen
        finish();
    }

    public void info(View view) {
        errAlert("Name must be at least 3 characters long, with no spacing.\nPassword must be at least 8 characters long, with at least 1 uppercase, 1 lowercase, and 1 numeral ");
    }
}
