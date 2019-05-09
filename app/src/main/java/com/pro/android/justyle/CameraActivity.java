package com.pro.android.justyle;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.pro.android.justyle.FrontPageActivity.bpt;
import static com.pro.android.justyle.WardrobeFragment.mImageView;
import static java.text.DateFormat.getDateTimeInstance;

public class CameraActivity extends AppCompatActivity {


     public static   String pathToFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_camera);
        if(Build.VERSION.SDK_INT>=23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }



        if (bpt > 10) { //batter level
            dispatchPictureTakenAction();
        }
        else {
            Toast.makeText(CameraActivity.this, "Your battery should be more than 10%", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == 1) {
              //  Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                //cameraImageView.setImageBitmap(bitmap);

              /*  Picasso.with(CameraActivity.this)
                         .load(pathToFile)
                         .into(WardrobeFragment.mImageView);*/

//                Intent i = new Intent();

                // Throw in some identifier
  //              i.putExtra (pathToFile, pathToFile);

                // Set the result with this data, and finish the activity
    //            setResult(RESULT_OK, i);


                finish();


            }
        }

    }


    private void dispatchPictureTakenAction() {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getPackageManager()) != null) {
            File photoFile ;
            photoFile = createPhotoFile();

            if(photoFile != null){
                pathToFile = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(CameraActivity.this, "com.pro.android.justyle.fileprovider", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePic, 1);



            }


        }

    }
    File image = null;

    private File createPhotoFile() {
       String name = new SimpleDateFormat("yyyyMMdd_MMmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("myLog", "Excep : " + e.toString());
        }
        return image;
    }
}

