package project.stav.odhapaam2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Welcome extends AppCompatActivity {

    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcome = (TextView) findViewById(R.id.welcome);
    }
}
