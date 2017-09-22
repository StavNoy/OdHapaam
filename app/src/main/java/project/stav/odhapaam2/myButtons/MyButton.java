package project.stav.odhapaam2.myButtons;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;

/**
 * Created by hackeru on 17/09/2017.
 */

public class MyButton extends AppCompatImageView {

    private boolean poped = false;// Maybe replace with TYPE = -1 ?
    private int TYPE=0;
    private int xPos;
    private int yPos;

    public MyButton(Context context,int type , int x, int y) {
        super(context);
        setTYPE(type);
        setxPos(x);
        setyPos(y);
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }


    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
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
}
