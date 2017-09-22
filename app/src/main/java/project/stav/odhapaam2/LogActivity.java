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

        user = (EditText) findViewById(R.id.input1);
        pass = (EditText) findViewById(R.id.input2);

    }


    public void LogIn(View view) {

        if(user.getText().toString().equals("game") && pass.getText().toString().equals("123")){
            Intent i = new Intent(this , LogActivity.class);
            startActivity(i);
        }else {
            Toast.makeText(this , "try again" ,Toast.LENGTH_LONG).show();}
    }
}
