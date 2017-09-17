package project.stav.odhapaam2.myButtons;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;

import java.util.Random;


public class MainScreen extends AppCompatActivity {
    final int [] buttons= new int[]{R.layout.button1,R.layout.button2,R.layout.button3};
    MyButton[] [] candies=new MyButton[5][5];
    GridLayout main;
    int i;
    MyButton selected;
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
                candies[x][y].setxPos(x);
                candies[x][y].setyPos(y);


            }
        }

    }
    private MyButton randomize(){
        MyButton b =(MyButton) LayoutInflater.from(this).inflate(buttons[new Random().nextInt(3)],null);
        return b;
    }

    public void SelectedView(MyButton v){
        if (selected==null){
            selected = v;
        }else{
            if (Math.abs((v.getxPos() +v.getyPos())-(selected.getxPos() +selected.getyPos()))==1){
                swap(selected , v);
            }
        }
    }

    public void swap(MyButton selected, MyButton v) {
        int x1=selected.getxPos() , y1=selected.getyPos(), x2=v.getxPos(), y2=v.getyPos();
        candies[x1][y1]=v;
        candies[x2][y2]=selected;
        selected.setxPos(x2);
        selected.setyPos(y2);
        v.setxPos(x1);
        v.setyPos(y1);
        selected=null;
    }
}
}

