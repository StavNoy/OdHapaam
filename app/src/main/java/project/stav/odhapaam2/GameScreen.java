package project.stav.odhapaam2;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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

import project.stav.odhapaam2.LogServer.Server.PointUploader;

public class GameScreen extends AppCompatActivity {
    private final int gridside = 5; // getResources().getInteger(R.integer.grid_side); // FIXME: 25/09/2017
    private final MyButton[][] candies = new MyButton[gridside][gridside];
    //candies[x] - lower row number is higher row on screen
    GridLayout playGrid; //ViewGroup for play area
    int p = 0; //int for points
    public Drawable[] images;
    private MyButton firstClick;//First of 2 clicks
    private boolean muted = true;
    Animation downImation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        downImation = AnimationUtils.loadAnimation(this, R.anim.down);
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
        final String pointStr = getString(R.string.points) + p;
        PointUploader.INSTANCE.upLoad(this, p);
        ((TextView) findViewById(R.id.points)).setText(pointStr);
    }

    //Toggle. Button shows other option. Copy/Paste from Nikita Kurtin's WonderWoman/SpiderMan
    public void mute(final View v) {
        ((ImageView) v).setImageResource((muted = !muted) ? R.mipmap.ic_unmute : R.mipmap.ic_mute);
    }

    private void creatingButtons() {
        for (int row = 0; row < candies.length; row++) {
            for (int col = 0; col < candies[row].length; col++) {
                int type = new Random().nextInt(4);
                //Creates MyButton with random TYPE and according image
                candies[row][col] = new MyButton(this,type , row, col);
                candies[row][col].setBackground(images[type]);
                candies[row][col].setOnClickListener(MyButtonListener);
                //setImage(candies[x][y]);//United with setTYPE
                //Add the new MyButton to play grid

                playGrid.addView(candies[row][col], (925 / candies[row].length), (1150 / candies.length));// TODO: 25/09/2017  fix scalability


            }
        }

        //Down animation for entire grid
//        playGrid.startAnimation(new DownerFactory().Downer(DownerFactory.INIT));
        //Listener to check for scoring only after animation
        downImation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                for (final MyButton[] row : candies) {
                    for (final MyButton newMB : row) {
                        whenSwaped(newMB);
                    }
                }
            }
            //irrelevant
            public void onAnimationRepeat(Animation animation) {} public void onAnimationStart(Animation animation) {}
        });
        playGrid.startAnimation(downImation);
    }

    //When a MyButton is clicked
    public final View.OnClickListener MyButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            //Animation to indicate click
            v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext() , R.anim.clicky));
            //Downcast clicked to MyButton
            final MyButton clicked2 = (MyButton) v;
            // if this is click 1
            if (firstClick == null) {
                firstClick = clicked2;
                //if views clicked have different TYPE and are adjacent ((1st x+y)-(2nd x+y)= 1|-1)
            } else if (firstClick.getTYPE() != clicked2.getTYPE()
                    && Math.abs((firstClick.getRow() + firstClick.getColumn()) - (clicked2.getRow() + clicked2.getColumn())) == 1) {
                //ToDo add shrink animation
                firstClick.startAnimation(AnimationUtils.loadAnimation(getApplicationContext() , R.anim.alpha_out));
                clicked2.startAnimation(AnimationUtils.loadAnimation(getApplicationContext() , R.anim.alpha_out));
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

    private void whenSwaped(final MyButton swapped) {
        //If Column has 3+
        final ArrayList<MyButton> oldCol = inAColumn(swapped);
        final boolean colHas3 = oldCol != null;
        //If Row has 3+
        final ArrayList<MyButton> oldRow = inARow(swapped);
        final boolean rowHas3 = oldRow != null;
        //Collect any repositioned popped
        ArrayList<MyButton> allNew = new ArrayList<MyButton>(0);
        //For rearranged column
        ArrayList<MyButton> newColumn;
        //IMPORTANT : must be before row is rearranged
        if (colHas3) {
            pop(oldCol);
            newColumn = columnToTop(oldCol);
            allNew.addAll(newColumn);
        }
        //For rearranged Row
        ArrayList<MyButton> newRow;
        if (rowHas3) {
            pop(oldRow);
            newRow = rowToTop(oldRow);
            allNew.addAll(newRow);
        }
        //Refill any repositioned popped
        if (!allNew.isEmpty()) {
            reFillPopped(allNew);
        }
    }

    //Returns ArrayList of 3+ or null, based on column of given MyButton
    @Nullable
    private ArrayList<MyButton> inAColumn(final MyButton clicked) {
        ArrayList<MyButton> inALine = new ArrayList<>();
        //Every MyButton in same column has same Y
        final int col = clicked.getColumn();
        for (final MyButton[] row : candies) {
            //If next is same TYPE
            if (row[col].getTYPE() == clicked.getTYPE()) {
                inALine.add(row[col]);
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

    //Returns ArrayList of 3+ or null, based on row of given MyButon
    @Nullable
    private ArrayList<MyButton> inARow(final MyButton clicked) {
        ArrayList<MyButton> inALine = new ArrayList<>();
        //Every col on current row
        final MyButton[] row = candies[clicked.getRow()];
        for (final MyButton mB : row) {
            //If next is same TYPE
            if (mB.getTYPE() == clicked.getTYPE()) {
                inALine.add(mB);
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

    private void pop(final ArrayList<MyButton> inALine) {
        for (final MyButton b : inALine) {
            b.setPopped(true);
            p++; //ToDo add pop animation
        }
        //Add pop animation
        p += inALine.size() - 3;//Extra point for each MyButton over 3;
        updateScore();
        if (!muted) MediaPlayer.create(this, R.raw.pop).start();//SFX
    }

    private ArrayList<MyButton> columnToTop(final ArrayList<MyButton> popcol) {
        //Every MyButton in same column has same col
        final int col = popcol.get(0).getColumn();
        //If top MyButton in current column is popped, no need for rearranging
        if (candies[0][col].isPopped()) {
            return popcol;
        } else {
            //Collection of popped MyButtons in new positions
            final ArrayList<MyButton> rePosPop = new ArrayList<>(popcol);
            //Move column to top
            for (int row = 0; row < popcol.size() ; row++) {
                final MyButton newPosition = candies[row][col];
                final MyButton pop = popcol.get(row);
                if (!newPosition.isPopped()) {
                    swap(pop, newPosition);
                    pop.animDown(); // pop now is candies[above] before. Start down animation
                }
                rePosPop.add(newPosition);
            }
            return rePosPop;
        }
    }

    //Reposition row top after pop
    //I M P O R T A N T : Must N-O-T be used before cloumnToTop(same parameter);
    private ArrayList<MyButton> rowToTop(final ArrayList<MyButton> popRow) {
        //Every MyButton in same row has same X
        int row = popRow.get(0).getRow();
        //If in top row, no rearrange needed
        if (row == 0) {
            return popRow;
        } else {
            final ArrayList<MyButton> popsAtTop = new ArrayList<>(popRow);
            for (final MyButton pop : popRow) {
                //In case pop was already repositioned by columnToTop
                if (pop.isPopped()) {
                    final int col = pop.getColumn();
                    for (; row > 0; row--) {
                        // Move all popped to the top- where X=0
                        swap(candies[row][col], candies[row-1][col]); //candies[index-1] -> one above
                        candies[row][col].startAnimation(downImation); // candies[x][y] now is candies[above pop] before. Start down animation
                    }
                    popsAtTop.add(candies[0][col]);// Each popped is now in candies[0]. column is unchanged
                }
            }
            return popsAtTop;
        }
    }

    private void reFillPopped(final ArrayList<MyButton> allPopped) {
        for (final MyButton pop : allPopped) {
            pop.setTYPE(new Random().nextInt(4));
            pop.setPopped(false);

            //Animation for ascending pop. When finished, check for score
            downImation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    whenSwaped(pop);
                }
                //Irrelevant
                public void onAnimationRepeat(Animation animation) {}  public void onAnimationStart(Animation animation) {}
            });
        }
    }

}
    /* TODO: 24/09/2017 init method that gets predefined 2D array pattern - simplest
            separately write many different patterns for initial playGrid.
                randomly choose between them.
            Or: Create 1D Array pattern for all, reassign 2nd Dimension */


