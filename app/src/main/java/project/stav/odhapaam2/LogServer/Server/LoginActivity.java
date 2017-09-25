//package project.stav.odhapaam2.LogServer.Server;
//
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.EditText;
//
///**
// * A login screen that offers login via email/password.
// */
//public class LoginActivity extends AppCompatActivity {
//
//    // UI references.
//    private EditText uName;
//    private EditText uPass;
//    private View mProgressView;
//    private View mLoginFormView;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_log);
//
//        uName = (EditText) findViewById(R.id.uName);
//        uPass = (EditText) findViewById(R.id.uPass);
//        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);
//
//    }
//
//    private boolean isValid(String txt) {
//        return txt != null && !txt.isEmpty();
//    }
//
//    private void showProgress(final boolean show) {
//         mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//         mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//    }
//
//    //when login button clicked
//    public void tryLogin(View v) {
//        showProgress(true);
//        final String name = uName.getText().toString(), pass = uPass.getText().toString();
//        if(isValid(name) && isValid(pass)){//local validation
//            new LoginTask(){
//                @Override//What to do in UI Main Thread with Result from background task
//                protected void onPostExecute(String info) {
//                    showProgress(false);
//                    if(info != null)validLogin(name, info);
//                    else invalidLogin();
//                }
//            }.execute(name, pass);//pass to background name & pass
//        }else invalidLogin();
//    }
//    //invalid login handler
//    public void invalidLogin(){
//        new AlertDialog.Builder(LoginActivity.this).setTitle("Wrong Credentials").setNeutralButton("OK", null).show();
//    }
//
//    public void validLogin(String name, String info){
//        Intent i = new Intent(this, ProfileActivity.class);
//        i.putExtra(ProfileActivity.UNAME, name);
//        i.putExtra(ProfileActivity.INFO, info);
//        startActivity(i);
//    }
//
//}
//
