package project.stav.odhapaam2.myButtons;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by hackeru on 17/09/2017.
 */

public class MyButton1 extends MyButton {
    int TYPE =1;

    public MyButton1(Context context) {
        super(context);
        this.setBackgroundResource(R.drawable.triangle);
    }

    public MyButton1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
