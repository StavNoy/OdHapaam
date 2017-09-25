package project.stav.odhapaam2.LogServer.Server;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    //Keys constants
    public static final String UNAME = "uname", INFO = "info", PASS = "pass";

    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        info = (TextView) findViewById(R.id.info);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String uName = getIntent().getStringExtra(UNAME);
        String info = getIntent().getStringExtra(INFO);
        if(uName != null && info != null){
            this.setTitle(uName);
            this.info.setText(info);
        }else finish();

    }
}
