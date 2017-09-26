package project.stav.odhapaam2;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import project.stav.odhapaam2.myButtons.MyButton;

public class GameScreen extends AppCompatActivity {
    private final int gridside = 5; // getResources().getInteger(R.integer.grid_side); // FIXME: 25/09/2017
    private final MyButton[][] candies = new MyButton[gridside][gridside];
    //candies[x] - lower X is higher row
    // TODO: 24/09/2017 change between x and y
    GridLayout playGrid; //ViewGroup for play area
    int p = 0; //int for points
    public Uri[] images;//Images picked by user
    private MyButton firstClick;//First of 2 clicks
    private boolean muted = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        playGrid = (GridLayout) findViewById(R.id.play_grid);
        images = MySharedPreferences.getImages(this);
        p = MySharedPreferences.getScore(this);
        updateScore();

        creatingButtons();
    }

    public void goHome(View v) {//Return to welcome screen
        finish();
    }

    //refresh score
    private void updateScore() {
        MySharedPreferences.setScore(this, p);
        String pointStr = getString(R.string.points) + p;
        ((TextView) findViewById(R.id.points)).setText(pointStr);
        // TODO add sound effect
    }

    //Toggle. Button shows other option. Copy/Paste from Nikita Kurtin's WonderWoman/SpiderMan
    public void mute(final View v) {
        ((ImageView) v).setImageResource((muted = !muted) ? R.mipmap.ic_unmute : R.mipmap.ic_mute);
    }

    private void creatingButtons() {
        for (int x = 0; x < candies.length; x++) {
            for (int y = 0; y < candies[x].length; y++) {
                //Creates MyButton with random TYPE and according image
                candies[x][y] = new MyButton(this, new Random().nextInt(4), x, y);
                candies[x][y].setOnClickListener(MyButtonListener);
                //setImage(candies[x][y]);//United with setTYPE
                //Add the new MyButton to play grid
                playGrid.addView(candies[x][y], (925 / candies[x].length), (1150 / candies.length));// TODO: 25/09/2017  fix scalability
            }
        }
        //Down animation for entire grid
        goDown(playGrid);
    }

    private class Downer implements Animation.AnimationListener {
        final int INIT = 0, AFTER_SWAP=1;
        int whatDo;
        private Downer(final int whatDo) {
            this.whatDo = whatDo;
        }
        @Override
        public void onAnimationEnd(Animation animation) {
            switch(whatDo){
                case 0:
                    for (MyButton[] xArr : candies) {
                        for (MyButton newMB : xArr) {
                            whenSwaped(newMB);
                        }
                    }
                    break;
                case 1:

            }
        }
        //Irrelevant
        public void onAnimationRepeat(Animation animation) {} public void onAnimationStart(Animation animation) {}
    }

    private void goDown(View v) {// TODO: make Nested Class
        Animation downImate = AnimationUtils.loadAnimation(GameScreen.this, R.anim.down);
        downImate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                for (MyButton[] xArr : candies) {
                    for (MyButton newMB : xArr) {
                        whenSwaped(newMB);
                    }
                }
            }
            public void onAnimationStart(Animation animation) {}public void onAnimationRepeat(Animation animation) {}
        });
        v.startAnimation(downImate);
    }


    //When a MyButton is clicked
    public final View.OnClickListener MyButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            //Downcast clicked to MyButton
            final MyButton clicked2 = (MyButton) v;
            // if this is click 1
            if (firstClick == null) {
                firstClick = clicked2;
                //if views clicked have different TYPE and are adjacent ((1st x+y)-(2nd x+y)= 1|-1)
            } else if (firstClick.getTYPE() != clicked2.getTYPE()
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
                final Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (vib.hasVibrator()) {
                    if (Build.VERSION.SDK_INT < 26) {
                        vib.vibrate(300);
                    } else {
                        vib.vibrate(VibrationEffect.createOneShot(VibrationEffect.DEFAULT_AMPLITUDE, 300));
                    }
                }
            }
            // TODO: 25/09/2017 Add identical onDrag
        }
    };

    //Change between 2 views
    private void swap(final MyButton click1, final MyButton click2) {
        //Exchange the TYPEs of the 2 views
        final int c1Type = click1.getTYPE();
        click1.setTYPE(click2.getTYPE());
        click2.setTYPE(c1Type);
        // if only one is popped, flip both
        if (click2.isPopped() ^ click1.isPopped()) {
            click2.setPopped(!click2.isPopped());
            click1.setPopped(!click1.isPopped());
        }
    }

    private void whenSwapedV1(final MyButton swapped) {
        //If Row has 3+
        ArrayList<MyButton> moreThan3 = inARow(swapped);
        if (moreThan3 != null) {
            pop(moreThan3);
            final ArrayList<MyButton> newRow = rowToTop(moreThan3);
            reFillPopped(newRow);
            for (MyButton newMB : newRow) {
                whenSwaped(newMB);
            }

        } else {
            //If Column has 3+
            moreThan3 = inAColumn(swapped);
            if (moreThan3 != null) {
                pop(moreThan3);
                final ArrayList<MyButton> newColumn = columnToTop(moreThan3);
                reFillPopped(newColumn);
                for (MyButton newMB : newColumn) {
                    whenSwaped(newMB);
                }
            }
        }
    }

    private void whenSwaped(final MyButton swapped) {
        //If Row has 3+
        final ArrayList<MyButton> oldRow = inARow(swapped);
        final boolean rowHas3 = oldRow != null;
        //If Column has 3+
        final ArrayList<MyButton> oldCol = inAColumn(swapped);
        final boolean colHas3 = oldCol != null;
        //Collect any repositioned popped
        ArrayList<MyButton> allNew = new ArrayList<MyButton>(0);
        //For rearranged column
        //IMPORTANT : must be before row is rearranged
        ArrayList<MyButton> newColumn;
        if (colHas3) {
            if (colHas3) pop(oldCol);
            newColumn = columnToTop(oldCol);
            allNew.addAll(newColumn);
        }
        //For rearranged Row
        ArrayList<MyButton> newRow;
        if (rowHas3) {
            if (rowHas3) pop(oldRow);
            newRow = rowToTop(oldRow);
            allNew.addAll(newRow);
        }
        //Refill any repositioned popped
        if (!allNew.isEmpty()) {
            reFillPopped(allNew);
        }
    }

    //Returns ArrayList of 3+ or null, based on Y of given MyButton
    @Nullable
    private ArrayList<MyButton> inAColumn(final MyButton clicked) {
        ArrayList<MyButton> inALine = new ArrayList<>();
        //Every Y with current Y
        final int y = clicked.getPosY();
        for (MyButton[] xArr : candies) {
            //If next is same TYPE
            if (xArr[y].getTYPE() == clicked.getTYPE()) {
                inALine.add(xArr[y]);
                //If next is different, but already have 3
            } else if (inALine.size() >= 3) {
                return inALine;
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
    @Nullable
    private ArrayList<MyButton> inARow(final MyButton clicked) {
        ArrayList<MyButton> inALine = new ArrayList<>();
        //Every Y on current X
        final MyButton[] xArr = candies[clicked.getPosX()];
        for (MyButton xY : xArr) {
            //If next is same TYPE
            if (xY.getTYPE() == clicked.getTYPE()) {
                inALine.add(xY);
                //If next is different, but already have 3
            } else if (inALine.size() >= 3) {
                return inALine;
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
    //I M P O R T A N T : Must N-O-T be used before cloumnToTop(same parameter);
    private ArrayList<MyButton> rowToTop(final ArrayList<MyButton> popRow) {
        //Every MyButton in same row has same X
        final int rowX = popRow.get(0).getPosX();
        //If in top row, no rearrange needed
        if (rowX == 0) {
            return popRow;
        } else {
            ArrayList<MyButton> popsAtTop = new ArrayList<>(popRow);
            for (MyButton pop : popRow) {
                //In case pop was already repositioned by columnToTop
                if (pop.isPopped()) {
                    int y = pop.getPosY();
                    for (int x = rowX - 1; x > 0; x--) { //candies[index-1] -> one above
                        // Move all popped to the top- where X=0
                        swap(pop, candies[x][y]);
                        pop.animDown(); // pop now is candies[above] before. Start down animation
                    }
                    popsAtTop.add(candies[0][y]);// Each popped is now in candies[0]. Y is unchanged
                }
            }
            return popsAtTop;
        }
    }

    private ArrayList<MyButton> columnToTop(final ArrayList<MyButton> popcol) {
        //Every MyButton in same column has same Y
        final int y = popcol.get(0).getPosY();
        //If top MyButton in current column is popped, no need for rearranging
        if (candies[0][y].isPopped()) {
            return popcol;
        } else {
            //Collect popped MyButtons in new positions
            ArrayList<MyButton> rePosPop = new ArrayList<>(popcol);
            for (int x = popcol.size() - 1; x > 0 && popcol.iterator().hasNext(); x--) {
                MyButton newPosition = candies[x][y];
                MyButton pop = popcol.iterator().next();
                if (!newPosition.isPopped()) {
                    swap(pop, newPosition);
                    pop.animDown(); // pop now is candies[above] before. Start down animation
                }
                rePosPop.add(newPosition);
            }
            return rePosPop;
        }
    }

    private void pop(final ArrayList<MyButton> inALine) {
        for (MyButton b : inALine) {
            b.setPopped(true);
            p++; //ToDo add pop animation
        }
        //Add pop animation
        p += inALine.size() - 3;//Extra point for each MyButton over 3;
        updateScore();
        if (!muted) MediaPlayer.create(this, R.raw.pop).start();//SFX
    }

    private void reFillPopped(final ArrayList<MyButton> allPopped) {
        for (MyButton pop : allPopped) {
            pop.setTYPE(new Random().nextInt(4));
            pop.setPopped(false);
            pop.animDown();
        }
    }
}
    //*****************************************O L D E R ------- S T I L L --- F O R --- o n C R E A T E ( ) ****************************************************************************************************
    //Reposition column top after pop
//    private ArrayList<MyButton> rePosPopColumnV1(ArrayList<MyButton> popped){
//        int y = popped.get(0).getPosY(); //All have same Y, different X
//        //Collect not popped.
//        ArrayList<MyButton> notPopped=new ArrayList<>(candies.length-popped.size());
//        for (int x = candies.length - 1; x >= 0; x--) {//From bottom to top
//            MyButton[] xArr = candies[x];
//            if (!xArr[y].isPopped()) {
//                notPopped.add(xArr[y]);//First added is bottom, last is top
//            }
//        }
//        //Move all not popped to bottom, while maintaining order
//        for (MyButton nPop : notPopped) {
//            int bottomX = candies.length-1; //Index of lowest square on screen
//            int nPopI=notPopped.indexOf(nPop);//Bottom->0, top is biggest
//            MyButton newPos;
//            swap(candies[bottomX-nPopI][y],nPop);
//            nPop.animDown();
//        }
//        // New ArrayList of Popped in new positions
//        ArrayList<MyButton> rePosPop = new ArrayList<>(popped);
//        for (int x = 0 ; x < popped.size(); x++){
//            rePosPop.add(candies[x][y]);
//        }
//        return rePosPop;
//    }
//
//    //Method for checking how many MyButtons are in a line
//    private void checkInLine() {
//        for (int x = 0; x < candies.length; x++) {
//            for (int y = 0; y < candies[x].length; y++) {
//                ArrayList<MyButton> inALine = new ArrayList<>();
//                //Within bounds, as long as next has same TYPE
//                for (int i = 0; y + i < candies[x].length && candies[x][y + i].getTYPE() == candies[x][y].getTYPE(); i++) {
//                    inALine.add(candies[x][y + i]);
//                }
//                checkIfScore(inALine);
//                inALine.clear();
//                //Same for same position in each array
//                for (int i = 0; x + i < candies.length && candies[x + i][y].getTYPE() == candies[x][y].getTYPE(); i++) {
//                    inALine.add(candies[x + i][y]);
//                }
//                checkIfScore(inALine);
//            }
//        }
//    }
//
//    private void checkIfScore(ArrayList<MyButton> inALine) {
//        if (inALine.size() >= 3) { //if 3 or more are in line, add 1 to score for each MyButton
//            for (MyButton b : inALine) {
//                b.setPopped(true);
//                p++;
//            }
//            p+=inALine.size()-3;//Extra point for each MyButton over 3;
//            if(!muted) MediaPlayer.create(this,R.raw.pop).start();//sfx
//            updateScore();
//            //checkInLine();//recursion
//            reArrange();
//            reCreatePoped();
//       }
//    }
//
//    private void reArrange(){//After popping
//        for (int x = 1; x < candies.length; x++) {
//            for (int y = 0; y < candies[x].length; y++) {
//                MyButton current = candies[x][y], next = candies[x - 1][y];
//                if (current.isPopped() && !next.isPopped()) {
//                    // start down animation
//                    next.animDown();// Start down animation
//                    swap(current, next);
//                }
//            }
//        }
//    }
//
//    private void reCreatePoped() {
//        for (MyButton[] candyArr : candies) {
//            for (MyButton mB : candyArr) {
//                if (mB.isPopped()) {
//                    mB.setTYPE(new Random().nextInt(4));
//                    mB.setPopped(false);
//                    mB.animDown();// Start down animation
//                }
//            }
//        }
//    }
//}
//
//    //Set image from chosen/default, by TYPE
//    private void setImage(MyButton mB) {
//        if (images[0] != null) {
//            mB.setImageURI(images[mB.getTYPE()]);
//        } else { //If no images are picked
//            mB.setBackgroundResource(altImages[mB.getTYPE()]);
//        }
//    }

    /* TODO: 24/09/2017 init method that gets predefined 2D array pattern - simplest
            separately write many different patterns for initial playGrid.
                randomly choose between them.
            Or: Create 1D Array pattern for all, reassign 2nd Dimension */


