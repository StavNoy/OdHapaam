package project.stav.odhapaam2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.HashSet;

public class StartScreen extends AppCompatActivity {

    //uri drown from gallery
    //the intent code of the image picking from gallery
    private static final int PICK_IMAGE = 100;
    //the code for requesting the external storage permission
    private final int MEDIA_REQUEST =102;
    //the last view that was clicked
    private ImageView chosenView;
    //the map that connects the uri and the views
    Uri [] imagesUris = new Uri[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen1);
        PermissionManager.check(this,Manifest.permission.WRITE_EXTERNAL_STORAGE,MEDIA_REQUEST);

    }

    //send user to gallery for picking an image
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    @Override
    //the event after the user picked an image, saves it in Uri variable "imageUri"
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            setImagesUris(data.getData());
        }


    }

    private void setImagesUris(Uri imageUri){
        if (imageUri != null) {
            //connect the view that was clicked with the image uri the image that the user chose
            int index = Integer.parseInt(chosenView.getTag().toString());
            imagesUris[index]= imageUri;
            chosenView.setImageURI(imageUri);
        }
    }

    public void chooseImage(View view) {
        chosenView =(ImageView)view;
        openGallery();
    }

    public void saveCandies(View view) {
        HashSet<Uri> uriSet =new HashSet<>(Arrays.asList(imagesUris));
//        for (Uri uri : imagesUris){
//            uriSet.add(uri);
//        }
        if (uriSet.size()==4) {
            MySharedPreferences.setImages(this, (Uri[]) uriSet.toArray());
           finish();
        }else{
          AlertDialog.Builder aD = new AlertDialog.Builder(this);
            aD.setMessage("please select four different images").show();
        }

        }

        }

