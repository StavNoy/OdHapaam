package project.stav.odhapaam2;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import project.stav.odhapaam2.myButtons.MyButton;

public class MainScreen extends AppCompatActivity {
    private MyButton[][] candies = new MyButton[5][5];
    //candies[x] - lower X is higher row
    // TODO: 24/09/2017 change between x and y
    GridLayout main; //ViewGroup for play area
    //TextView points; Made local variable for method updatePoints()
    int p = 0; //int for points
    private Uri[] images;//Images picked by user
    private int[] altImages;//if no images are picked

    private MyButton firstClick;//First of 2 clicks
    private boolean muted=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = (GridLayout) findViewById(R.id.play_grid);
        //points = (TextView) findViewById(R.id.points);
        images = MySharedPreferences.getImages(this);
        if (images[0] == null) {//Default images if non are picked
            altImages = new int[]{R.drawable.triangle, R.drawable.red_circle, R.drawable.yellow_square, R.drawable.green_x};
        }
        p = MySharedPreferences.getScore(this);
        updateScore();

        creatingButtons();
        checkInRow();
    }

    public void goHome(View view) {//Return to welcome screen
        finish();
    }

    //refresh score
    private void updateScore() {
        MySharedPreferences.setScore(this,p);
        String pointStr = getString(R.string.points) + p;
        ((TextView) findViewById(R.id.points)).setText(pointStr);
        // TODO add sound effect
    }

    public void mute(View v) {
        ((ImageView)v).setImageResource((muted = !muted) ? R.mipmap.ic_unmute : R.mipmap.ic_mute);
    }

    private void creatingButtons() {
        for (int x = 0; x < candies.length; x++) {
            for (int y = 0; y < candies[x].length; y++) {
                candies[x][y] = randomize(x, y);
                main.addView(candies [x][y], 185, 230);
            }
        }
    }

    private MyButton randomize(int x, int y) {//Creates MyButton with random TYPE and according image
        MyButton mB = new MyButton(this, new Random().nextInt(4), x, y);
        mB.setOnClickListener(MyButtonListener);
        setImage(mB);
        return mB;
    }

    public View.OnClickListener MyButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyButton clicked2 = (MyButton) v; //downcast click to MyBbutton
            if (firstClick == null) { // if this is click 1
                firstClick = clicked2;
            } else if (firstClick.getTYPE()!=clicked2.getTYPE()
                    && Math.abs((clicked2.getPosX() + clicked2.getPosY()) - (firstClick.getPosX() + firstClick.getPosY())) == 1) { //if views clicked have different TYPE and adjacent
                swap(firstClick, clicked2);
                whenSwaped(firstClick);
                whenSwaped(clicked2);
                firstClick = null;
            } else { //This is click 2. Both clicks are same type and/or not adjacent
                //ToDo add animation for incorrect move + make vibrate optional
                firstClick = null;
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if(vib.hasVibrator()) {
                    if (Build.VERSION.SDK_INT<26) {
                        vib.vibrate(300);
                    } else {
                        vib.vibrate(VibrationEffect.createOneShot(VibrationEffect.DEFAULT_AMPLITUDE,300));
                    }
                }
            }
        }
    };

    private void swap(MyButton selec, MyButton v) {//Change between 2 clicked views
        int sType = selec.getTYPE();
        selec.setTYPE(v.getTYPE()); //Exchange the TYPEs of the 2 views
        v.setTYPE(sType);
        //ToDo add shrink animation
        //Set new Image for views based on new TYPE
        setImage(v);
        setImage(selec);
        //ToDo add expand animation
    }

    private void whenSwaped(MyButton swapped){
        ArrayList<MyButton> inALine = inAColumn(swapped);
        if (inALine != null) {
            pop(inALine);
            reFillPopped(inALine);
            ArrayList<MyButton> newColumn = rePosRefilledColumn(inALine);
            for (MyButton newMB : newColumn) {
                whenSwaped(newMB);
            }
            // TODO: 24/09/2017 add recursion
        } else {
            inALine = inARow(swapped);
            if (inALine != null) {
                pop(inALine);
                reFillPopped(inALine);
                rePosRefilledRow(inALine);
                ArrayList<MyButton> newRow = rePosRefilledRow(inALine);
                for (MyButton newMB : newRow) {
                    whenSwaped(newMB);
                }
                // TODO: 24/09/2017 add recursion
            }
        }
    }

    private ArrayList<MyButton> inAColumn(MyButton clicked) {//returns ArrayList of 3+ or null
        ArrayList<MyButton> inALine = new ArrayList<>();
        int y = clicked.getPosY();
        for (MyButton[] candy : candies) {
            if (candy[y].getTYPE() == clicked.getTYPE()) {
                inALine.add(candy[y]);
            } else if (inALine.size() >= 3) {
                break;
            } else {
                inALine.clear();
            }
        }
            if (inALine.size() >= 3) {
                return inALine;
            }
        return null;
    }
    private ArrayList<MyButton> inARow(MyButton clicked) {//returns ArrayList of 3+ or null
        ArrayList<MyButton> inALine = new ArrayList<>();
        MyButton[] xArr = candies[clicked.getPosX()];
        for (MyButton xY : xArr) {//every y on current x
            if (xY.getTYPE() == clicked.getTYPE()) {
                inALine.add(xY);
            } else if (inALine.size() >= 3) {
                break;
            } else { //when sequence breaks before reaching 3
                inALine.clear();
            }
        }
        if (inALine.size() >= 3) {
            return inALine;
        }
            return null;
    }

    private ArrayList<MyButton> rePosRefilledRow(ArrayList<MyButton> reFilled){//After popping
        ArrayList<MyButton> rePosFilled = new ArrayList<>(reFilled);
            for (MyButton mB : reFilled) {
                int y = mB.getPosY(); //all have same X, different Y
                for (int x = 1; x < candies.length; x++) {
                    swap(mB, candies[x - 1][y]); // move all popped to the top- where X=0
                    candies[x - 1][y].startAnimation(AnimationUtils.loadAnimation(getApplicationContext() , R.anim.down));// start down animation
                    mB.startAnimation(AnimationUtils.loadAnimation(getApplicationContext() , R.anim.down));// start down animation
                }
                rePosFilled.add(candies[0][y]);// each popped is now in candies[0]. Y is unchanged
            }
        return rePosFilled;
    }
    private ArrayList<MyButton> rePosRefilledColumn(ArrayList<MyButton> reFilled){//After popping
        ArrayList<MyButton> rePosFilled = new ArrayList<>(reFilled);
        int y = reFilled.get(0).getPosY(); //all have same Y, different X
        for (int x = 1 ; x <candies.length; x++) {//From top to bottom
            if (!candies[x][y].isPoped()) {
                for (int i = candies.length-2; i < 0; i--) {// move  to the bottom
                    swap(candies[x][y], candies[i + 1][y]);
                    candies[x][y].startAnimation(AnimationUtils.loadAnimation(getApplicationContext() , R.anim.down));// start down animation
                }
            }
        }
        for (int x = 0 ; x < reFilled.size(); x++){
            rePosFilled.add(candies[x][y]);
        }
        return rePosFilled;
    }

    private void pop(ArrayList<MyButton> inALine) {
        for (MyButton b : inALine) {
            b.setPoped(true);
            p++; //ToDo add pop animation
        }
        //add pop animation
        p+=inALine.size()-3;//Extra point for each MyButton over 3;
        updateScore();
        if(!muted) MediaPlayer.create(this,R.raw.pop).start();//sfx
    }
    private  void reFillPopped(ArrayList<MyButton> allPopped){
        for (MyButton pop : allPopped) {
            pop.setTYPE(new Random().nextInt(4));
            pop.setPoped(false);
            setImage(pop);
            //pop.startAnimation(AnimationUtils.loadAnimation(getApplicationContext() , R.anim.down));// start down animation
        }
    }

    private void setImage(MyButton mB) {//Set image from chosen/default // TODO: 21/9/2017 move to MyButton.setTYPE()
        if (images[0] != null) {
            mB.setImageURI(images[mB.getTYPE()]);
        } else { //if no images are picked
            mB.setBackgroundResource(altImages[mB.getTYPE()]);
        }
    }

    //*****************************************O L D E R ------- S T I L L --- F O R --- o n C R E A T E ( ) ****************************************************************************************************
    //method for checking how many MyButtons are in a line
    private void checkInRow() {
        for (int x = 0; x < candies.length; x++) {
            for (int y = 0; y < candies[x].length; y++) {
                ArrayList<MyButton> inALine = new ArrayList<>();
                for (int i = 0; y + i < candies[x].length && candies[x][y + i].getTYPE() == candies[x][y].getTYPE(); i++) { //Within bounds, as long as next has same TYPE
                    inALine.add(candies[x][y + i]);
                }
                checkIfScore(inALine);
                inALine.clear();
                for (int i = 0; x + i < candies.length && candies[x + i][y].getTYPE() == candies[x][y].getTYPE(); i++) {//Same for same position in each array
                    inALine.add(candies[x + i][y]);
                }
                checkIfScore(inALine);
            }
        }
    }

    private void checkIfScore(ArrayList<MyButton> inALine) {
        if (inALine.size() >= 3) { //if 3 or more are in line, add 1 to score for each MyButton
            for (MyButton b : inALine) {
                //ToDo add animation
                b.setPoped(true);
                p++;
            }
            p+=inALine.size()-3;//Extra point for each MyButton over 3;
            if(!muted) MediaPlayer.create(this,R.raw.pop).start();//sfx
            updateScore();
            //checkInRow();//recursion FIXME: 19/09/2017
            reArrange();
            reCreatePoped();
       }
    }

    private void reArrange(){//After popping
        for (int x = 1; x < candies.length; x++) {
            for (int y = 0; y < candies[x].length; y++) {
                MyButton current = candies[x][y], next = candies[x - 1][y];
                if (current.isPoped() && !next.isPoped()) {
                    next.startAnimation(AnimationUtils.loadAnimation(getApplicationContext() , R.anim.down));// start down animation
                    swap(current, next);
                    current.setPoped(false);
                    next.setPoped(true);
                }
            }
        }
    }

    private void reCreatePoped() { // // TODO: 24/09/2017 replace with dependency injection
        for (MyButton[] candyArr : candies) {
            for (MyButton mB : candyArr) {
                if (mB.isPoped()) {
                    mB.setTYPE(new Random().nextInt(4));
                    mB.setPoped(false);
                    // TODO: 24/09/2017 animation
                    //anim
                    setImage(mB);
                    mB.startAnimation(AnimationUtils.loadAnimation(getApplicationContext() , R.anim.down));// start down animation
                }
            }
        }
    }
}



    // TODO: 24/09/2017 add methods searchByX and searchByY, connect to events

    /* TODO: 24/09/2017 init method that gets predefined 2D array pattern - simplest
            Or: Create 1D Array pattern for all, reassign 2nd D */


