package project.stav.odhapaam2;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import project.stav.odhapaam2.myButtons.MyButton;


public class MainScreen extends AppCompatActivity {
    private MyButton[][] candies = new MyButton[5][5];
    GridLayout main;
    TextView points;
    int p = 0;
    private Uri [] images;

    private MyButton selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = (GridLayout) findViewById(R.id.main);
        points = (TextView) findViewById(R.id.points);
        images = MySharedPreferences.getImages(this);

        p++;
        points.setText("points: " + p);

        creatingButtons();
        checkInRow();
    }

    private void creatingButtons() {
        for (int x = 0; x < candies.length; x++) {
            for (int y = 0; y < candies[x].length; y++) {
                candies[x][y] = randomize();
                main.addView(candies[x][y], 185, 230);
                candies[x][y].setxPos(x);
                candies[x][y].setyPos(y);
                candies[x][y].setOnClickListener(MyButtonListener);
            }
        }
    }

    private MyButton randomize() {
        MyButton b = new MyButton(this, new Random().nextInt(4));
        b.setImageURI(images[b.getTYPE()]);
        return b;
    }


    public View.OnClickListener MyButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickedMyButton((MyButton) v);
        }
    };

    private void clickedMyButton(MyButton v) {
        if (selected == null) {
            selected = v;
        } else {
            if (Math.abs((v.getxPos() + v.getyPos()) - (selected.getxPos() + selected.getyPos())) == 1) {
                swap(selected, v);
            }
        }
    }

    private void swap(MyButton selected, MyButton v) {
        int sType = selected.getTYPE();
        selected.setTYPE(v.getTYPE());
        v.setTYPE(sType);
        //ToDo add shrink animation
        v.setImageURI(images[v.getTYPE()]);
        selected.setImageURI(images[selected.getTYPE()]);
        //ToDo add expand animation
        selected = null;
        checkInRow();
    }

    //method for checking if 3 or more MyButtons are in a line
    private void checkInRow() {
        for (int x = 0; x < candies.length; x++) {
            for (int y = 0; y < candies[x].length; y++) {
                ArrayList<MyButton> inALine = new ArrayList(0);
                for (int i = 0; candies[x][y + i].getTYPE() == inALine.get(0).getTYPE(); i++) {
                    inALine.add(candies[x][y + i]);
                }
                checkIfScore(inALine);
                inALine.clear();

                for (int i = 0; candies[x + i][y].getTYPE() == inALine.get(0).getTYPE(); i++) {
                    inALine.add(candies[x + i][y]);
                }
                checkIfScore(inALine);
            }
        }
    }

    private void checkIfScore(ArrayList<MyButton> inALine) {
        if (inALine.size() >= 3) {
            for (MyButton b : inALine) {
                //ToDo add animation
                b.setVisibility(View.GONE);
            }
            p+=inALine.size();
            MySharedPreferences.setScore(this,p);
            points.setText("points: " + p);
            checkInRow();
        }
    }
}

