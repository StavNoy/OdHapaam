package project.stav.odhapaam2;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.animation.AnimationUtils;

/**
 * Created by hackeru on 17/09/2017.
 */

public class MyButton extends AppCompatImageView {

    private boolean popped = false;
    private int TYPE;
    private int row;
    private int column;
    private static final int[] altImages = new int[]{R.drawable.triangle, R.drawable.red_circle, R.drawable.yellow_square, R.drawable.green_x};

    public MyButton(final Context context,int type ,final int row,final int col) {
        super(context);
        setTYPE(type);
        this.row = row;
        this.column = col;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
      /*  Drawable[] images = ((GameScreen)this.getContext()).images;
        if (images[0] != null) {
            this.setImageDrawable(images[TYPE]);
        } else { //If no images are picked
            this.setBackgroundResource(altImages[TYPE]);
        }*/
    }

    public void setPopped(boolean popped) {
        this.popped = popped;
    }

    public boolean isPopped() {
        return popped;
    }

    public void animDown(){
        this.startAnimation(AnimationUtils.loadAnimation(this.getContext() , R.anim.down));
    }
}
