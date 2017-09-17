package project.stav.odhapaam2.myButtons;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.Random;


public class MainScreen extends AppCompatActivity {
    final int [] buttons= new int[]{R.layout.button1,R.layout.button2,R.layout.button3};
    MyButton[] [] candies=new MyButton[5][5];
    GridLayout main;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = (GridLayout)findViewById(R.id.main);
        creatingButtons();
    }
    private void creatingButtons(){
        for (int x = 0;x<candies.length;x++){
            for (int y = 0;y<candies[x].length;y++){
                candies[x][y]=randomize();
                main.addView(candies[x][y],185,230);


            }
        }

    }
    private MyButton randomize(){
        MyButton b =(MyButton) LayoutInflater.from(this).inflate(buttons[new Random().nextInt(3)],null);
        return b;
    }
}

