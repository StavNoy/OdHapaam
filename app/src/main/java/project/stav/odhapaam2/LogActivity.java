package project.stav.odhapaam2;

import android.content.Context;
import android.content.Intent;
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

public class LogActivity extends AppCompatActivity {

    private static final String serverUrl = "http://127.0.0.1:9999/login";
    EditText uName, uPass;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        context = this;

        uName = (EditText) findViewById(R.id.uName);
        uPass = (EditText) findViewById(R.id.uPass);
    }

//    public void logIn(View view) {
//        String uName = this.uName.getText().toString();
//        if(uName.equals("game") && uPass.getText().toString().equals("123")){
//            Intent i = new Intent(this , LogActivity.class);
//            startActivity(i);
//            Toast.makeText(this , "Logged in as "+uName ,Toast.LENGTH_LONG).show();
//        }else {
//            Toast.makeText(this , "try again" ,Toast.LENGTH_LONG).show();}
//        }

    public void tryLogIn(View v){
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
                                    MySharedPreferences.saveName(context, NAME);
                                    MySharedPreferences.savePass(context, PASS);
                                    //Save "Stay signed in" state
                                    MySharedPreferences.setStayLogged(context, ((CheckBox)findViewById(R.id.stayLogged)).isChecked());
                                }
                            }else {
                                errAlert("Invalid Request");
                            }
                        }
                    }.execute(serverUrl,uData.toString());
                } else {
                    errAlert("Name must be at least 3 characters long, with no spacing.\nPassword must be at least 8 characters long, with at least 1 uppercase, 1 lowercase, and 1 digit ");
                }
            }catch (JSONException e) {
                    e.printStackTrace();
        }
    }

    private AlertDialog errAlert(final String msg){
        return new AlertDialog.Builder(context).setTitle("Error").setMessage(msg).setNeutralButton("OK",null).show();
    }

    private boolean validPass(final String pswrd){
        return pswrd.matches("(?=.*[A-Z]+)(?=.*[a-z]+)(?=.*\\d+)^.{8,}$");//ToDo add explanation in UI
    }
    private boolean validName(final String name){//username must be at least 3 char long with no spaces
        return name.matches("^[^\\s]{3,}$");//ToDo add explanation in UI; Add "check available"
    }

    public void goHome(View v) {//Return to welcome screen
        finish();
    }

}
