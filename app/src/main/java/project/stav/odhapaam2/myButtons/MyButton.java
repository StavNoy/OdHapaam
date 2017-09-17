package project.stav.odhapaam2.myButtons;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import project.stav.odhapaam2.MainActivity;

/**
 * Created by hackeru on 17/09/2017.
 */

public class MyButton extends TextView {
    public int x;
    public int y;
    public int TYPE=0;
    public MyButton(Context context) {
        super(context);
        this.setBackground(MainActivity.imgUri[TYPE]);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
