package project.stav.odhapaam2;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import project.stav.odhapaam2.myButtons.MyButton;

public class MainActivity extends AppCompatActivity {
    MyButton selected;

    public static Drawable[] imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void SelectedView(MyButton v){
        if (selected==null){
            selected = v;
        }else{
            if (Math.abs((v.x+v.y)-(selected.x+selected.y))==1){
                swap(selected , v);
            }
        }
    }

    public void swap(View selected, View v) {

        selected=null;
    }
}
