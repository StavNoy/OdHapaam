package project.stav.odhapaam2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Welcome extends AppCompatActivity {

    TextView welcome;
    EditText user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcome = (TextView) findViewById(R.id.welcome);
        user = (EditText) findViewById(R.id.uName);
    }

    public void goStScreen(View view){
        startActivity(new Intent(this, ChoosePicActivity.class));
    }

    public void goLog(View view) {
        startActivity(new Intent(this, LogActivity.class));
    }
    public void goPlay(View view) {
        //ToDo if not logged- Alert
        startActivity(new Intent(this, GameScreen.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!MySharedPreferences.getStayLogged(this)){
            MySharedPreferences.saveName(this,"");
            MySharedPreferences.savePass(this,"");
        }
    }
}
