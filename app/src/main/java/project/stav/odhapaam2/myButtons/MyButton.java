package project.stav.odhapaam2.myButtons;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.animation.AnimationUtils;

import project.stav.odhapaam2.R;

/**
 * Created by hackeru on 17/09/2017.
 */

public class MyButton extends AppCompatImageView {

    private boolean poped = false;// Maybe replace with TYPE = -1 ?
    private int TYPE=0;
    private int posX;
    private int posY;

    public MyButton(Context context,int type , int x, int y) {
        super(context);
        setTYPE(type);
        setPosX(x);
        setPosY(y);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }


    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public void setPoped(boolean poped) {
        this.poped = poped;
//        if (poped) {this.setBackgroundColor(Color.BLACK);}
    }

    public boolean isPoped() {
        return poped;
    }

    public void animDown(){
        this.startAnimation(AnimationUtils.loadAnimation(this.getContext() , R.anim.down));
    }
}
