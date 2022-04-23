package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.TimeUnit;


public class UploadActivity extends AppCompatActivity {

    private final static int PICK_IMAGE_REQUEST = 1;

    private Button buttonChooseImage, buttonUpload;
    private TextView textViewShowUploads;
    private EditText editTextFileName;
    private ImageView imageView;
    private ProgressBar progressBar;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Uri imageUri;

    private StorageReference storageRef = FirebaseStorage.getInstance().getReference("uploads/" + user.getUid() +"/pics/posts" );


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload2);

        buttonChooseImage = findViewById(R.id.button_choose_image);
        buttonUpload = findViewById(R.id.button_upload);
        textViewShowUploads = findViewById(R.id.text_view_show_uploads);
        editTextFileName = findViewById(R.id.edit_test_file_name);
        imageView = findViewById(R.id.image_view);
        progressBar = findViewById(R.id.progress_bar);

        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
                AsyncTaskRunner runner = new AsyncTaskRunner();
                // Create the AsyncTask Runner WITHIN MainActivity
                String sleepTime = "2";
                runner.execute(sleepTime);
            }
        });
        textViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            // Picasso.with(this).load(imageUri).into(imageView);
            imageView.setImageURI(imageUri);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = null;
        cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if (imageUri != null){
            StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //progressBar.setProgress(0);
                                }
                            }, 5000);

                            Toast.makeText(UploadActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                            //Upload upload = new Upload(editTextFileName.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            progressBar.setProgress(100);
                        }
                    });
        }else{
            Toast.makeText(this, "no image", Toast.LENGTH_SHORT).show();
        }
    }



    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(UploadActivity.this, "ProgressDialog",
                    "Please Wait");
        }

        @Override
        protected String doInBackground(String... params) {
            // This method sends updated text to the UI thread
            // using onProgressUpdate
            publishProgress("Sleeping..."); // Calls onProgressUpdate
            try {
                int time = Integer.parseInt(params[0]) * 1000;
                Thread.sleep(time);
                resp = "Slept for " + params[0] + " seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp; // passed to main UI thread, string updated
        }

        @Override
        protected void onPostExecute(String result) {
            // Gives us the result of the Runner
            progressDialog.dismiss();
        }

    }

































    /*private final String TAG = this.getClass().getName();

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
    }*/
}

