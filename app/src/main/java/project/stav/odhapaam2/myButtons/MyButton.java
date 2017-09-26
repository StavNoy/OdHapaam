package project.stav.odhapaam2.myButtons;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.view.animation.AnimationUtils;

import project.stav.odhapaam2.GameScreen;
import project.stav.odhapaam2.R;

/**
 * Created by hackeru on 17/09/2017.
 */

public class MyButton extends AppCompatImageView {

    private boolean poped = false;// Maybe replace with TYPE = -1 ?
    private int TYPE;
    private int posX;
    private int posY;
    private static final int[] altImages = new int[]{R.drawable.triangle, R.drawable.red_circle, R.drawable.yellow_square, R.drawable.green_x};

    public MyButton(final Context context,int type ,final int x,final int y) {
        super(context);
        setTYPE(type);
        this.posX = x;
        this.posY = y;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
        Uri[] images = ((GameScreen)this.getContext()).images;
        if (images[0] != null) {
            this.setImageURI(images[TYPE]);
        } else { //If no images are picked
            this.setBackgroundResource(altImages[TYPE]);
        }
    }

    public void setPopped(boolean poped) {
        this.poped = poped;
    }

    public boolean isPopped() {
        return poped;
    }

    public void animDown(){
        this.startAnimation(AnimationUtils.loadAnimation(this.getContext() , R.anim.down));
    }
}
