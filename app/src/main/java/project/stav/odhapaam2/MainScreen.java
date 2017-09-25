package project.stav.odhapaam2;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import project.stav.odhapaam2.myButtons.MyButton;

public class MainScreen extends AppCompatActivity {
//    private int gridside = R.integer.grid_side; // FIXME: 25/09/2017
    private MyButton[][] candies = new MyButton[5][5];
    //candies[x] - lower X is higher row
    // TODO: 24/09/2017 change between x and y
    GridLayout playGrid; //ViewGroup for play area
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
        playGrid = (GridLayout) findViewById(R.id.play_grid);
        images = MySharedPreferences.getImages(this);
        //Default images if non are picked
        if (images[0] == null) {
            altImages = new int[]{R.drawable.triangle, R.drawable.red_circle, R.drawable.yellow_square, R.drawable.green_x};
        }
        p = MySharedPreferences.getScore(this);
        updateScore();

        creatingButtons();
        checkInLine();
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

    //Toggle. Button shows other option. Copy/Paste from Nikita Kurtin's WonderWoman/SpiderMan
    public void mute(View v) {
        ((ImageView)v).setImageResource((muted = !muted) ? R.mipmap.ic_unmute : R.mipmap.ic_mute);
    }

    private void creatingButtons() {
        for (int x = 0; x < candies.length; x++) {
            for (int y = 0; y < candies[x].length; y++) {
                //Creates MyButton with random TYPE and according image
                candies[x][y] = new MyButton(this, new Random().nextInt(4), x, y);
                candies[x][y].setOnClickListener(MyButtonListener);
                setImage(candies[x][y]);
                //Add the new MyButton to play grid
                playGrid.addView(candies [x][y], (925/candies[x].length),(1150/candies.length));// TODO: 25/09/2017  fix scalability
            }
        }
    }

    //When a MyButton is clicked
    public View.OnClickListener MyButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Downcast clicked to MyButton
            MyButton clicked2 = (MyButton) v;
            // if this is click 1
            if (firstClick == null) {
                firstClick = clicked2;
            //if views clicked have different TYPE and are adjacent ((1st x+y)-(2nd x+y)= 1|-1)
            } else if (firstClick.getTYPE()!=clicked2.getTYPE()
                    && Math.abs((firstClick.getPosX() + firstClick.getPosY()) - (clicked2.getPosX() + clicked2.getPosY())) == 1) {
                //ToDo add shrink animation
                swap(firstClick, clicked2);
                //ToDo add expand animation
                whenSwaped(firstClick);
                whenSwaped(clicked2);
                firstClick = null;
            //This is click 2. Both clicks are same type and/or not adjacent
            } else {
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
            // TODO: 25/09/2017 Add identical onDrag
        }
    };

    //Change between 2 views
    private void swap(MyButton selec, MyButton v) {
        //Exchange the TYPEs of the 2 views
        int sType = selec.getTYPE();
        selec.setTYPE(v.getTYPE());
        v.setTYPE(sType);
        //Set new Image for views based on new TYPE
        setImage(v);
        setImage(selec);
        // if only one is popped, flip both
        if (v.isPoped() ^ selec.isPoped()){
            v.setPoped(!v.isPoped());
            selec.setPoped(!selec.isPoped());
        }
    }

    private void whenSwaped(MyButton swapped){
        //If column has 3+
        ArrayList<MyButton> moreThan3 = inAColumn(swapped);
        if (moreThan3 != null) {
            pop(moreThan3);
            ArrayList<MyButton> newColumn = columnToTop(moreThan3);
            reFillPopped(newColumn);
            for (MyButton newMB : newColumn) {
                whenSwaped(newMB);
            }

        } else {
            //If row has 3+
            moreThan3 = inARow(swapped);
            if (moreThan3 != null) {
                pop(moreThan3);
                rowToTop(moreThan3);
                ArrayList<MyButton> newRow = rowToTop(moreThan3);
                reFillPopped(newRow);
                for (MyButton newMB : newRow) {
                    whenSwaped(newMB);
                }
            }
        }
    }

    //Returns ArrayList of 3+ or null, based on Y of given MyButton
    private ArrayList<MyButton> inAColumn(MyButton clicked) {
        ArrayList<MyButton> inALine = new ArrayList<>();
        //Every Y with current Y
        int y = clicked.getPosY();
        for (MyButton[] xArr : candies) {
            if (xArr[y].getTYPE() == clicked.getTYPE()) {
                inALine.add(xArr[y]);
            //If next is different, but already have 3
            } else if (inALine.size() >= 3) {
                break;
            //When next is different before reaching 3
            } else {
                inALine.clear();
            }
        }
            if (inALine.size() >= 3) {
                return inALine;
            }
        return null;
    }

    //Returns ArrayList of 3+ or null, based on X of given MyButon
    private ArrayList<MyButton> inARow(MyButton clicked) {
        ArrayList<MyButton> inALine = new ArrayList<>();
        //Every Y on current X
        MyButton[] xArr = candies[clicked.getPosX()];
        for (MyButton xY : xArr) {
            if (xY.getTYPE() == clicked.getTYPE()) {
                inALine.add(xY);
            //If next is different, but already have 3
            } else if (inALine.size() >= 3) {
                break;
            //When next is different before reaching 3
            } else {
                inALine.clear();
            }
        }
        if (inALine.size() >= 3) {
            return inALine;
        }
            return null;
    }

    //Reposition row top after pop
    private ArrayList<MyButton> rowToTop(ArrayList<MyButton> popped){
        //Every MyButton in same row has same X
        int rowX = popped.get(0).getPosX();
        //If in top row, no rearrange needed
        if (rowX == 0) {
            return popped;
        } else {
            ArrayList<MyButton> popsAtTop = new ArrayList<>(popped);
            for (MyButton pop : popped) {
                int y = pop.getPosY();
                for (int x = rowX-1; x > 0; x--) { //candies[index-1] -> one above
                    // Move all popped to the top- where X=0
                    swap(pop, candies[x][y]);
                    pop.animDown(); // pop now is candies[above] before. Start down animation
                }
                popsAtTop.add(candies[0][y]);// Each popped is now in candies[0]. Y is unchanged
            }
            return popsAtTop;
        }
    }

    private ArrayList<MyButton> columnToTop(ArrayList<MyButton> popped){
        //Every MyButton in same column has same Y
        int y = popped.get(0).getPosY();
        //If top MyButton in current column is popped, no need for rearranging
        if (candies[0][y].isPoped()) {
            return popped;
//        } else {
//            ArrayList<MyButton> rePosPop = new ArrayList<>(popped);
//            for (MyButton pop : popped){
//                int newX= popped.indexOf(pop);
//                MyButton newPosition = candies[newX][pop.getPosY()];
//                if(newPosition != pop) {
//                    newPosition.animDown();
//                    swap(pop, newPosition);
//                }
//                rePosPop.add(newPosition);
//            }
//            return rePosPop;
        } else {
            ArrayList<MyButton> rePosPop = new ArrayList<>(popped);
            for (int x=popped.size()-1 ; x>0 && popped.iterator().hasNext(); x--){
                MyButton newPosition = candies[x][y];
                MyButton pop = popped.iterator().next();
                if(!newPosition.isPoped()) {
                    swap(pop, newPosition);
                    pop.animDown(); // pop now is candies[above] before. Start down animation
                }
                rePosPop.add(newPosition);
            }
            return rePosPop;
        }
    }

    //Reposition column top after pop
    private ArrayList<MyButton> rePosPopColumnV1(ArrayList<MyButton> popped){
        int y = popped.get(0).getPosY(); //All have same Y, different X
        //Collect not popped.
        ArrayList<MyButton> notPopped=new ArrayList<>(candies.length-popped.size());
        for (int x = candies.length - 1; x >= 0; x--) {//From bottom to top
            MyButton[] xArr = candies[x];
            if (!xArr[y].isPoped()) {
                notPopped.add(xArr[y]);//First added is bottom, last is top
            }
        }
        //Move all not popped to bottom, while maintaining order
        for (MyButton nPop : notPopped) {
            int bottomX = candies.length-1; //Index of lowest square on screen
            int nPopI=notPopped.indexOf(nPop);//Bottom->0, top is biggest
            MyButton newPos;
            swap(candies[bottomX-nPopI][y],nPop);
            nPop.animDown();
        }
        // New ArrayList of Popped in new positions
        ArrayList<MyButton> rePosPop = new ArrayList<>(popped);
        for (int x = 0 ; x < popped.size(); x++){
            rePosPop.add(candies[x][y]);
        }
        return rePosPop;
    }

    private void pop(ArrayList<MyButton> inALine) {
        for (MyButton b : inALine) {
            b.setPoped(true);
            p++; //ToDo add pop animation
        }
        //Add pop animation
        p+=inALine.size()-3;//Extra point for each MyButton over 3;
        updateScore();
        if(!muted) MediaPlayer.create(this,R.raw.pop).start();//SFX
    }
    private  void reFillPopped(ArrayList<MyButton> allPopped){
        for (MyButton pop : allPopped) {
            pop.setTYPE(new Random().nextInt(4));
            pop.setPoped(false);
            setImage(pop);
            pop.animDown();
        }
    }

    //Set image from chosen/default, by TYPE
    private void setImage(MyButton mB) { // TODO: 21/9/2017 move to MyButton.setTYPE()
        if (images[0] != null) {
            mB.setImageURI(images[mB.getTYPE()]);
        } else { //If no images are picked
            mB.setBackgroundResource(altImages[mB.getTYPE()]);
        }
    }

    //*****************************************O L D E R ------- S T I L L --- F O R --- o n C R E A T E ( ) ****************************************************************************************************
    //Method for checking how many MyButtons are in a line
    private void checkInLine() {
        for (int x = 0; x < candies.length; x++) {
            for (int y = 0; y < candies[x].length; y++) {
                ArrayList<MyButton> inALine = new ArrayList<>();
                //Within bounds, as long as next has same TYPE
                for (int i = 0; y + i < candies[x].length && candies[x][y + i].getTYPE() == candies[x][y].getTYPE(); i++) {
                    inALine.add(candies[x][y + i]);
                }
                checkIfScore(inALine);
                inALine.clear();
                //Same for same position in each array
                for (int i = 0; x + i < candies.length && candies[x + i][y].getTYPE() == candies[x][y].getTYPE(); i++) {
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
            //checkInLine();//recursion FIXME: 19/09/2017
            reArrange();
            reCreatePoped();
       }
    }

    private void reArrange(){//After popping
        for (int x = 1; x < candies.length; x++) {
            for (int y = 0; y < candies[x].length; y++) {
                MyButton current = candies[x][y], next = candies[x - 1][y];
                if (current.isPoped() && !next.isPoped()) {
                    // start down animation
                    next.animDown();// Start down animation
                    swap(current, next);
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
                    mB.animDown();// Start down animation
                }
            }
        }
    }
}



    // TODO: 24/09/2017 add methods searchByX and searchByY, connect to events

    /* TODO: 24/09/2017 init method that gets predefined 2D array pattern - simplest
            Or: Create 1D Array pattern for all, reassign 2nd D */


