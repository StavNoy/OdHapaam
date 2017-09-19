package project.stav.odhapaam2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class StartScreen extends AppCompatActivity {

    private boolean check = false;
    public static Uri imageUri;
    private static final int PICK_IMAGE = 100;
    private final int MEDIA_REQUEST =102;
    ImageView chosenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      PermissionManager.check(this,Manifest.permission.WRITE_EXTERNAL_STORAGE,MEDIA_REQUEST);


    }


    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            if (imageUri != null) {
                chosenView.setImageURI(imageUri);
            }
            imageUri=null;
        }


    }

    public void chooseImage(View view) {
        chosenView =(ImageView)view;
        openGallery();


    }
}
