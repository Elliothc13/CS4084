package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.kosalgeek.android.photoutil.GalleryPhoto;

public class UploadActivity extends AppCompatActivity {
    //private final String TAG = this.getClass().getName();

    ImageView imageView, galleryView;
    TextView textView;

    GalleryPhoto galleryPhoto;

    final int GALLERY_REQUEST = 22131;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        galleryPhoto = new GalleryPhoto(getApplicationContext());

        imageView = (ImageView)findViewById(R.id.imageView);
        galleryView = (ImageView)findViewById(R.id.galleryView);
        textView = (TextView)findViewById(R.id.textView);

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
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                textView.setText(uri.toString());
                //Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(128, 128).getBitmap();
            }
        }
    }
}

