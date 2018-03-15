package project.stav.odhapaam2;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.view.animation.AnimationUtils;

public class GamePiece extends AppCompatImageView {

    private boolean popped = false;
    private int TYPE;
    private final int row;
    private final int column;
    private static final int[] altImages = new int[]{R.drawable.triangle, R.drawable.red_circle, R.drawable.yellow_square, R.drawable.green_x};

    public GamePiece(Context context, int type, int row, int column) {
        super(context);
        setTYPE(type);
        this.row = row;
        this.column = column;
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }

    int getTYPE() {
        return TYPE;
    }

    void setTYPE(int TYPE) {
        this.TYPE = TYPE;
        Drawable[] images = ((GameActivity) this.getContext()).images;
        if (images[TYPE] != null) {
            this.setImageDrawable(images[TYPE]);
        } else { //If no images are picked
            this.setBackgroundResource(altImages[TYPE]);
        }
    }

    /*Compares GamePiece by their TYPE variables*/
    boolean sameTYPE(GamePiece gamePiece) {
        return this == gamePiece || this.getTYPE() == gamePiece.getTYPE();
    }


/* Adjacent GamePiece are  1 row xor 1 column apart */
    boolean adjacent(GamePiece gamePiece){
        return Math.abs(this.getRow() - gamePiece.getRow()) == 1
                ^ Math.abs(this.getColumn() - gamePiece.getColumn()) == 1;
    }

    void setPopped(boolean popped) {
        this.popped = popped;
    }

    boolean isPopped() {
        return popped;
    }

    void animDown(){
        this.startAnimation(AnimationUtils.loadAnimation(this.getContext() , R.anim.down));
    }
}
