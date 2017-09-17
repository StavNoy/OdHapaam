package project.stav.odhapaam2.myButtons;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by hackeru on 17/09/2017.
 */

public class MyButton extends TextView {
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

    public MyButton(Context context) {
        super(context);
        //this.setBackground(*ImageCollection*[TYPE]);
    }


    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
