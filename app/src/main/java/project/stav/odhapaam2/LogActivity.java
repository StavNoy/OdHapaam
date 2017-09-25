package project.stav.odhapaam2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LogActivity extends AppCompatActivity {

    EditText user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        user = (EditText) findViewById(R.id.uName);
        pass = (EditText) findViewById(R.id.uPass);
    }


    public void LogIn(View view) {
        String uName = user.getText().toString();
        if(uName.equals("game") && pass.getText().toString().equals("123")){ // TODO: change to external DataBase
            Intent i = new Intent(this , LogActivity.class);
            startActivity(i);
            Toast.makeText(this , "Logged in as "+uName ,Toast.LENGTH_LONG).show(); //Maybe SnackBar?
        }else {
            Toast.makeText(this , "try again" ,Toast.LENGTH_LONG).show();}//ToDo change to alert
        }

    private boolean validPass(String pswrd){
        return pswrd.matches("(?=.*[A-Z]+)(?=.*[a-z]+)(?=.*\\d+)^.{8,}$");//ToDo add explanation in UI
    }
    private boolean validName(String name){//username must be at least 3 char long with no spaces
        return name.matches("^[^\\s]{3,}$");//ToDo add explanation in UI; Add "check available"
    }
}
