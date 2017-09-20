package project.stav.odhapaam2.myButtons;
import android.content.Context;
import android.widget.ImageView;

/**
 * Created by hackeru on 17/09/2017.
 */

public class MyButton extends ImageView {

    private boolean poped = false;
    private int TYPE=0;
    private int xPos;
    private int yPos;

    public MyButton(Context context,int type) {
        super(context);
        setTYPE(type);
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
    }

    public boolean getPoped() {
        return poped;
    }
}
