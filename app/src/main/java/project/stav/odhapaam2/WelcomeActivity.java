package project.stav.odhapaam2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    TextView welcome;
    EditText user;

    //Check internet connection
    public static boolean checkConnect(final Context context){
        final ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        if (!isConnected) {
            new AlertDialog.Builder(context).setTitle("No internet connection")
                    .setMessage("Certain features will not work. \nGo to Settings?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                        }
            }).show();
        } return isConnected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        welcome = (TextView) findViewById(R.id.welcome);
        user = (EditText) findViewById(R.id.uName);
        checkConnect(this);
    }

    public void goChoosePic(View view){
        startActivity(new Intent(this, ChoosePicActivity.class));
    }

    public void goLog(View view) {
        startActivity(new Intent(this, LogActivity.class));
    }

    public void goPlay(final View view) {
        //If not logged- Alert
        if (SharedPrefs.getName(this).isEmpty() || SharedPrefs.getPass(this).isEmpty()) {
            new AlertDialog.Builder(this).setTitle("You are not logged in")
                    .setMessage("Go to Log Screen?")
                    .setNegativeButton("No, Just play", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(WelcomeActivity.this, GameActivity.class));
                        }
                     }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                goLog(view);
                            }
            }).show();
        } else { //Logged In
            startActivity(new Intent(this, GameActivity.class));
        }
    }

    public void goHS(View view) {
        //Only works with internet connection
        if (checkConnect(this)) startActivity(new Intent(this, HighScoreActivity.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!SharedPrefs.getStayLogged(this)){
            SharedPrefs.saveName(this,"");
            SharedPrefs.savePass(this,"");
        }
    }
}
