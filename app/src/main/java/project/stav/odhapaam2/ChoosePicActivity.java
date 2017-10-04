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
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

public class ChoosePicActivity extends AppCompatActivity {

    //uri drown from gallery
    //the intent code of the image picking from gallery
    private static final int PICK_IMAGE = 100;
    //the code for requesting the external storage permission
    private final int MEDIA_REQUEST = 102;
    //the last view that was clicked
    private ImageView chosenView;
    String[] FilesPath = new String[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_pic_activity);
        PermissionManager.check(this, Manifest.permission.READ_EXTERNAL_STORAGE, MEDIA_REQUEST);


    }

    //send uName to gallery for picking an image
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    @Override
    //the event after the uName picked an image, saves it in Uri variable "imageUri"
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            setImagesUris(imageUri);

        }


    }

    private void setImagesUris(final Uri imageUri) {

        if (imageUri != null) {
            //connect the view that was clicked with the image uri the image that the uName chose
            final int index = Integer.parseInt(chosenView.getTag().toString());
            chosenView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            chosenView.setImageURI(imageUri);
            final String filesPath = getFilesDir() + "/img" + index + ".jpg";

            new Thread() {
                @Override
                public void run() {
                    try (InputStream fis = getContentResolver().openInputStream(imageUri);
                         FileOutputStream fos = new FileOutputStream(filesPath)) {

                        int b;
                        while ((b = fis.read()) != -1) {
                            fos.write(b);
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(ChoosePicActivity.this, "IOException", Toast.LENGTH_SHORT).show();
                    }
                }
            }.start();
            FilesPath[index] = filesPath;
        }

    }

    public void chooseImage(View view) {
        chosenView = (ImageView) view;
        openGallery();
    }

    public void saveCandies(View view) {
        HashSet<String> pathSet = new HashSet<>();
        for (String path : FilesPath) {
            pathSet.add(path);
        }
        if (pathSet.size() == 4) {
            SharedPrefs.setImages(this, pathSet);
            finish();
        } else {
            AlertDialog.Builder aD = new AlertDialog.Builder(this);
            aD.setMessage("please select four different images").show();
        }

    }


}


