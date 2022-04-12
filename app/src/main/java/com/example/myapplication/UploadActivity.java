package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class UploadActivity extends AppCompatActivity {

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView wv = new WebView(this);
        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient() {
            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                UploadActivity.this.showAttachmentDialog(uploadMsg);
            }

            // For Android > 3.x
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                UploadActivity.this.showAttachmentDialog(uploadMsg);
            }

            // For Android > 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                UploadActivity.this.showAttachmentDialog(uploadMsg);
            }
        });

        this.setContentView(wv);

        wv.loadUrl("https://www.google.com/");

    }

    private void showAttachmentDialog(ValueCallback<Uri> uploadMsg) {
        this.mUploadMessage = uploadMsg;

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");

        this.startActivityForResult(Intent.createChooser(i, "Choose type of attachment"), FILECHOOSER_RESULTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == this.mUploadMessage) {
                return;
            }
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            this.mUploadMessage.onReceiveValue(result);
            this.mUploadMessage = null;
        }
    }
}