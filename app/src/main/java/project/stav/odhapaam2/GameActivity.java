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

import project.stav.odhapaam2.LogServer.Server.Upload;

public class GameActivity extends AppCompatActivity {
    private final int gridside = 5;
    private final GamePiece[][] candies = new GamePiece[gridside][gridside];
    //candies[x] - lower row number is higher row on screen
    GridLayout playGrid; //ViewGroup for play area
    int p = 0; //int for points
    Drawable[] images;
    private GamePiece firstClick;//First of 2 clicks
    private boolean muted = true;
    private Animation downImation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        downImation = AnimationUtils.loadAnimation(this, R.anim.down);
        setContentView(R.layout.game_activity);
        playGrid = findViewById(R.id.play_grid);
        images = SharedPrefs.getImages(this);
        if (images[0] == null) {
            images = new Drawable[]{getDrawable(R.drawable.green_x), getDrawable(R.drawable.red_circle), getDrawable(R.drawable.triangle), getDrawable(R.drawable.yellow_square)};
            p = SharedPrefs.getScore(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateScore();
        creatingButtons();
    }

    public void goHome(View v) {//Return to welcome screen
        finish();
    }

    //refresh score
    private void updateScore() {
        SharedPrefs.setScore(this, p);
        final String pointStr = getString(R.string.points) + p;
        Upload.INSTANCE.upLoad(this,SharedPrefs.getName(this),SharedPrefs.getPass(this), p, Upload.PATH_SAVE);
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
                //Creates GamePiece with random TYPE and according image
                candies[row][col] = new GamePiece(this,type , row, col);
               // candies[row][col].setBackground(images[type]);
                 candies[row][col].setOnClickListener(GamePieceListener);
                //Add the new GamePiece to play grid

                playGrid.addView(candies[row][col], (925 / candies[row].length), (1150 / candies.length));// TODO: fix scalability


            }
        }

        //Down animation for entire grid
//        playGrid.startAnimation(new DownerFactory().Downer(DownerFactory.INIT));
        //Listener to check for scoring only after animation
        downImation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                for (GamePiece[] row : candies) {
                    for (GamePiece newGamePiece : row) {
                        whenSwaped(newGamePiece);
                    }
                }
            }
            //irrelevant
            public void onAnimationRepeat(Animation animation) {} public void onAnimationStart(Animation animation) {}
        });

        playGrid.startAnimation(downImation);
    }

    //When a GamePiece is clicked
    public final View.OnClickListener GamePieceListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            //Animation to indicate click
            v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext() , R.anim.clicky));
            //Downcast clicked to GamePiece
            final GamePiece nowClicked = (GamePiece) v;
            // if this is click 1
            if (firstClick == null) {
                firstClick = nowClicked;
                //if both GamePiece clicked have same TYPE and are adjacent
            } else if ((!firstClick.sameTYPE(nowClicked)) && firstClick.adjacent(nowClicked)) {
                swap(firstClick, nowClicked);
                whenSwaped(firstClick);
                whenSwaped(nowClicked);
                firstClick = null;
                //This is click 2. Both clicks are same type and/or not adjacent
            } else {
                firstClick = null;
                final Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (vib!=null && vib.hasVibrator()) {
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
    private void swap(final GamePiece click1, final GamePiece click2) {
        //start animation
        final Animation alphOut = AnimationUtils.loadAnimation(getApplicationContext() , R.anim.alpha_out);
        click1.startAnimation(alphOut);
        click2.startAnimation(alphOut);
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

    private void whenSwaped(final GamePiece swapped) {
        //If Column has 3+
        ArrayList<GamePiece> col3Plus = inAColumn(swapped);
        //If Row has 3+
        ArrayList<GamePiece> row3Plus = inARow(swapped);
        //Collect any repositioned popped
        ArrayList<GamePiece> allPopped = new ArrayList<>(0);
        //IMPORTANT : must be before row is rearranged
        if (col3Plus != null) {
            pop(col3Plus);
            //col3Plus = columnToTop(col3Plus);
            allPopped.addAll(col3Plus);
        }
        if (row3Plus != null) {
            //row3Plus = rowToTop(row3Plus);
            pop(row3Plus);
            allPopped.addAll(row3Plus);
        }
        //Refill any repositioned popped
        if (!allPopped.isEmpty()) {
            reFillPopped(allPopped);
            for(GamePiece newGamePiece : allPopped){
                whenSwaped(newGamePiece);
            }
        }
    }

    //Returns ArrayList of 3+ or null, based on column of given GamePiece
    @Nullable
    private ArrayList<GamePiece> inAColumn(final GamePiece clicked) {
        ArrayList<GamePiece> inALine = new ArrayList<>(0);
        //Every GamePiece in same column has same Y
        final int col = clicked.getColumn();
        for (final GamePiece[] row : candies) {
            //If next is same TYPE
            if (row[col].sameTYPE(clicked)) {
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

    //Returns ArrayList of 3+ or null, based on row of given GamePiece
    @Nullable
    private ArrayList<GamePiece> inARow(final GamePiece clicked) {
        ArrayList<GamePiece> inALine = new ArrayList<>();
        //Every col on current row
        final GamePiece[] row = candies[clicked.getRow()];
        for (final GamePiece gamePiece : row) {
            //If next is same TYPE
            if (gamePiece.sameTYPE(clicked)) {
                inALine.add(gamePiece);
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

    private void pop(final ArrayList<GamePiece> inALine) {
        for (final GamePiece b : inALine) {
            b.setPopped(true);
            p++; //ToDo add pop animation
        }
        //Add pop animation
        p += inALine.size() - 3;//Extra point for each GamePiece over 3;
        updateScore();
        if (!muted) MediaPlayer.create(this, R.raw.pop).start();//SFX
    }

    private ArrayList<GamePiece> columnToTop(final ArrayList<GamePiece> popcol) {
        //Every GamePiece in same column has same col
        final int col = popcol.get(0).getColumn();
        //If top GamePiece in current column is popped, no need for rearranging
        if (candies[0][col].isPopped()) {
            return popcol;
        } else {
            //Collection of popped GamePiece in new positions
            final ArrayList<GamePiece> rePosPop = new ArrayList<>(popcol);
            //Move column to top
            for (int row = 0; row < popcol.size() ; row++) {
                final GamePiece newPosition = candies[row][col];
                final GamePiece pop = popcol.get(row);
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
    private ArrayList<GamePiece> rowToTop(final ArrayList<GamePiece> popRow) {
        //Every GamePiece in same row has same X
        int row = popRow.get(0).getRow();
        //If in top row, no rearrange needed
        if (row == 0) {
            return popRow;
        } else {
            final ArrayList<GamePiece> popsAtTop = new ArrayList<>(popRow);
            for (final GamePiece pop : popRow) {
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

    private void reFillPopped(final ArrayList<GamePiece> allPopped) {
        for (final GamePiece pop : allPopped) {
            Random r = new Random();
            int random = r.nextInt(4);
            while(random==pop.getTYPE())random=r.nextInt(4);
            pop.setTYPE(random);

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
    /* Optional improvement : initializing method that receives predefined 2D array pattern - simplest
            separately write many different patterns for initial playGrid.
                "randomly" choose between them.
            Or: Create 1D Array pattern for all, reassign 2nd Dimension */


