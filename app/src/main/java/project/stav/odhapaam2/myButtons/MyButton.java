package project.stav.odhapaam2.myButtons;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hackeru on 17/09/2017.
 */

public class MyButton extends ImageView {
    public MyButton(Context context,int type) {
        super(context);
        TYPE=type;
    }


    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    private int xPos;

    public void setxPos(int xPos) {
        this.xPos = xPos;

    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    private int yPos;

    public int getTYPE() {
        return TYPE;
    }

    private int TYPE=0;
}
