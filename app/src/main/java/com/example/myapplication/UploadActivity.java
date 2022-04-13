package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;

public class UploadActivity extends AppCompatActivity {
    //private final String TAG = this.getClass().getName();

    ImageView imageView, galleryView;

    GalleryPhoto galleryPhoto;

    final int GALLERY_REQUEST = 22131;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        galleryPhoto = new GalleryPhoto(getApplicationContext());

        imageView = (ImageView)findViewById(R.id.imageView);
        galleryView = (ImageView)findViewById(R.id.galleryView);

        galleryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                Log.i("hi", "hi");
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                try {
                    Log.i("hi", "just before bitmap");
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(128, 128).getBitmap();
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }else{
                        Log.i("hi", "bitmap didnt work");
                    }
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong while loading the image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

