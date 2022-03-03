package com.example.needmedz;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;

public class OcrActivity extends AppCompatActivity {

    Button logout ;
    Button generateText ;
    Button next;
    ImageView i ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        logout = findViewById(R.id.button2);
        generateText = findViewById(R.id.button);
        next = findViewById(R.id.button3);
        i = findViewById(R.id.imageView2);
        i.setImageResource(R.drawable.uploadicon);

    logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

//            logout Functionality
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(OcrActivity.this, loginActivity.class);
            startActivity(i);
        }
    });


    generateText.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(OcrActivity.this, " Backend is Yet to be established ", Toast.LENGTH_SHORT).show();
        }
    });

    next.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(OcrActivity.this,DataBaseActivity.class);
            startActivity(i);
        }
    });

    i.setOnClickListener(new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View view) {
            Toast.makeText(OcrActivity.this, " image is clicked ", Toast.LENGTH_SHORT).show();
            ImagePicker.with(OcrActivity.this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
//            boolean pick = true;
//            if(pick == true ){
//                if(!checkCameraPermission()){
//                    requestCameraPermission();
//
//                }else PickImage();
//            } else {
//                if(!checkStoragePermission()){
//                    requestStoragePermission();
//
//                }else PickImage();
//            }
        }
    });

    }

//    private void PickImage() {
//
////        CropImage.activity()
////                .setGuidelines(CropImageView.Guidelines.ON)
////                .start(this);
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void requestStoragePermission() {
//        requestPermissions(new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, 100);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void requestCameraPermission() {
//        requestPermissions(new String[]{ Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, 100);
//    }
//
//    private boolean checkStoragePermission() {
//        boolean res2 = ContextCompat.checkSelfPermission(this , Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//        return res2;
//    }
//
//    private boolean checkCameraPermission() {
//        boolean res1 = ContextCompat.checkSelfPermission(this , Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
//        boolean res2 = ContextCompat.checkSelfPermission(this , Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
//        return  res1 && res2;
//    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            i.setImageURI(uri);
        }
        else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

}

