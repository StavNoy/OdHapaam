package project.stav.odhapaam2.myButtons;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import project.stav.odhapaam2.MainActivity;

/**
 * Created by hackeru on 17/09/2017.
 */

public class MyButton extends TextView {
    private int x;
    private int y;
    public int TYPE=0;
    public MyButton(Context context) {
        super(context);
        this.setBackground(MainActivity.imgUri[TYPE]);
    }
    public void setX(int x){
        this.x=x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX1() {
        return x;
    }
    public int getY1(){
        return y;
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
