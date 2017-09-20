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
    GridLayout main; //ViewGroup for play area
    TextView points;
    int p = 0; //int for points
    private Uri[] images;//Images picked by user
    private int[] altImages;//if no images are picked

    private MyButton selected;//First of 2 clicks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = (GridLayout) findViewById(R.id.main);
        points = (TextView) findViewById(R.id.points);
        images = MySharedPreferences.getImages(this);
        if (images[0] == null) {//Default images if non are picked
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
                    //candies[x][y].setOnClickListener(MyButtonListener); //Moved to method randomize()
                    main.addView(candies[x][y], 185, 230);
                }
            }
        }
    }

    private MyButton randomize() {//Creates MyButton with random TYPE and according image
        MyButton b = new MyButton(this, new Random().nextInt(4));
        if (images[0] == null) { //if no images are picked
            b.setBackgroundResource(altImages[b.getTYPE()]);
        } else {
            b.setImageURI(images[b.getTYPE()]);
        }
        b.setOnClickListener(MyButtonListener);
        return b;
    }


    public View.OnClickListener MyButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyButton mB = (MyButton) v; //downcast click to MyBbutton
            if (selected == null) { // if this is click 1
                selected = mB;
            } else if (selected.getTYPE()!=mB.getTYPE()
                    && Math.abs((mB.getxPos() + mB.getyPos()) - (selected.getxPos() + selected.getyPos())) == 1) { //if views clicked are different and adjacent
                swap(selected, mB);
                selected = null;
                checkInRow();
            } else { //This is click 2. Both clicks are same type and/or not adjacent
                //ToDo add indication for incorrect move
                selected = null;
            }
        }
    };

    private void swap(MyButton selec, MyButton v) {//Change between 2 clicked views
        int sType = selec.getTYPE();
        selec.setTYPE(v.getTYPE()); //Exchange the TYPEs of the 2 views
        v.setTYPE(sType);
        //ToDo add shrink animation
        //Set new Image for views based on new TYPE
        if (images[0] == null) { //if no images are picked
            v.setBackgroundResource(altImages[v.getTYPE()]);
            selec.setBackgroundResource(altImages[selec.getTYPE()]);
        } else {
            v.setImageURI(images[v.getTYPE()]);
            selec.setImageURI(images[selec.getTYPE()]);
        }
        //ToDo add expand animation
    }

    //method for checking how many MyButtons are in a line
    private void checkInRow() {
        for (int x = 0; x < candies.length; x++) {
            for (int y = 0; y < candies[x].length; y++) {
                ArrayList<MyButton> inALine = new ArrayList<>();
                for (int i = 0; y + i < candies[x].length && candies[x][y + i].getTYPE() == candies[x][y].getTYPE(); i++) { //Within bounds, as long as next has same TYPE
                    inALine.add(candies[x][y + i]);
                }
                checkIfScore(inALine);
                System.out.println(inALine.size());
                inALine.clear();
                System.out.println(inALine.size());
                for (int i = 0; x + i < candies.length && candies[x + i][y].getTYPE() == candies[x][y].getTYPE(); i++) {//Same for same position in each array
                    inALine.add(candies[x + i][y]);
                }
                checkIfScore(inALine);
                System.out.println(inALine.size());
            }
        }
    }

    private void checkIfScore(ArrayList<MyButton> inALine) {
        if (inALine.size() >= 3) { //if 3 or more are in line, add 1 to score for each element
            for (MyButton b : inALine) {
                //ToDo add animation
                //b.setVisibility(View.INVISIBLE);//ToDO add rearrange
                //main.removeView(b);
                b.setPoped(true);
                p++;
            }
            //refresh score
            MySharedPreferences.setScore(this,p);
            String pointStr = getString(R.string.points) + p;
            points.setText(pointStr);
//            checkInRow();//recursion FIXME: 19/09/2017
//            reArrange();
        }
    }

    private void reArrange(){//by changing during iteration, the iteration params also change
//        for (int x = 0; x < candies.length; x++) {
//            for (int y = 0; y+1 < candies[x].length ; y++) {
//                MyButton mB = candies[x][y];
//                if (mB == null){
//                    mB=candies[x][y+1];
//                    mB.setxPos(x);
//                    mB.setyPos(y);
//                    candies[x][y+1] = null;
//                }
//            }
//        }
        for (int i = 0 ; i<main.getChildCount() ; i++){//For each child of main ViewGroup, swap() with invisible under it
            MyButton mB = (MyButton) main.getChildAt(i);
            if (mB.getyPos()-1>=0) {
                MyButton under = candies[mB.getxPos()][mB.getyPos()-1];
                if (under.getPoped()){
                    swap(mB,under);
                    //recursion
                }
            }
        }
    }
}