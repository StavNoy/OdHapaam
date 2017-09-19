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
    private Uri[] images;
    private int[] altImages;//if no images are picked

    private MyButton selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = (GridLayout) findViewById(R.id.main);
        points = (TextView) findViewById(R.id.points);
        images = MySharedPreferences.getImages(this);
        if (images[0] == null) {//if no images are picked
            altImages = new int[]{R.drawable.triangle, R.drawable.red_circle, R.drawable.yellow_square, R.drawable.green_x};
        }
        p = MySharedPreferences.getScore(this);
        String pointStr = getString(R.string.points) + p;
        points.setText(pointStr);

        creatingButtons();
        checkInRow();
    }

    private void creatingButtons() {
        for (int x = 0; x < candies.length; x++) {
            for (int y = 0; y < candies[x].length; y++) {
                if (candies[x][y]==null) {
                    candies[x][y] = randomize();
                    candies[x][y].setxPos(x);
                    candies[x][y].setyPos(y);
                    candies[x][y].setOnClickListener(MyButtonListener);
                    main.addView(candies[x][y], 185, 230);
                }
            }
        }
    }

    private MyButton randomize() {
        MyButton b = new MyButton(this, new Random().nextInt(4));
        if (images[0] == null) { //if no images are picked
            b.setBackgroundResource(altImages[b.getTYPE()]);
        } else {
            b.setImageURI(images[b.getTYPE()]);
        }
        return b;
    }


    public View.OnClickListener MyButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyButton mB = (MyButton) v;
            if (selected == null) {
                selected = mB;
            } else {
                if (Math.abs((mB.getxPos() + mB.getyPos()) - (selected.getxPos() + selected.getyPos())) == 1) {
                    swap(selected, mB);
                    selected = null;
                    checkInRow();
                }
                //ToDo add indication for incorrect move
            }
        }
    };

    private void swap(MyButton selec, MyButton v) {
        int sType = selec.getTYPE();
        selec.setTYPE(v.getTYPE());
        v.setTYPE(sType);
        //ToDo add shrink animation
        if (images[0] == null) { //if no images are picked
            v.setBackgroundResource(altImages[v.getTYPE()]);
            selec.setBackgroundResource(altImages[selec.getTYPE()]);
        } else {
            v.setImageURI(images[v.getTYPE()]);
            selec.setImageURI(images[selec.getTYPE()]);
        }
        //ToDo add expand animation
    }

    //method for checking if 3 or more MyButtons are in a line
    private void checkInRow() {
        for (int x = 0; x < candies.length; x++) {
            for (int y = 0; y < candies[x].length; y++) {
                ArrayList<MyButton> inALine = new ArrayList<>();
                for (int i = 0; y + i < candies[x].length && candies[x][y + i].getTYPE() == candies[x][y].getTYPE(); i++) {
                    inALine.add(candies[x][y + i]);
                }
                checkIfScore(inALine);
                inALine.clear();
                for (int i = 0; x + i < candies[x].length && candies[x + i][y].getTYPE() == candies[x][y].getTYPE(); i++) {
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
                b.setVisibility(View.INVISIBLE);//ToDO add rearrange
                main.removeView(b);
            }
            p+=inALine.size();
            MySharedPreferences.setScore(this,p);
            String pointStr = getString(R.string.points) + p;
            points.setText(pointStr);
//            checkInRow();//recursion FIXME: 19/09/2017
//            reArrange();
        }
    }

    private void reArrange(){//by changing during iteration, the iteration params also change
        for (int x = 0; x < candies.length; x++) {
            for (int y = 0; y+1 < candies[x].length ; y++) {
                MyButton mB = candies[x][y];
                if (mB == null){
                    mB=candies[x][y+1];
                    mB.setxPos(x);
                    mB.setyPos(y);
                    candies[x][y+1] = null;
                }
            }
        }
    }
}