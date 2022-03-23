package com.example.needmedz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.dhaval2404.imagepicker.ImagePicker;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;


import java.io.IOException;

public class OcrActivity extends AppCompatActivity {

    private Toolbar toolbar;
    DrawerLayout Drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Button logout ;
    Button generateText ;
    Button next;
    ImageView i ;
    EditText resultTV ;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        toolbar = (Toolbar) findViewById(R.id.AppBar);
        Drawer = findViewById(R.id.mainDrawer);

        logout = findViewById(R.id.button2);
        resultTV = findViewById(R.id.editTextTextMultiLine);
        generateText = findViewById(R.id.button);
        next = findViewById(R.id.button3);
        i = findViewById(R.id.imageView2);
        i.setImageResource(R.drawable.uploadicon);
        setupDrawerLayout();
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
    logout.setOnClickListener(view -> {

//            logout Functionality
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(OcrActivity.this, loginActivity.class);
        startActivity(i);
    });

        generateText.setOnClickListener(view -> {
            Toast.makeText(OcrActivity.this, " generating Text  ", Toast.LENGTH_SHORT).show();
            detect();
        });

    next.setOnClickListener(view -> {

        Intent i = new Intent(OcrActivity.this,DataBaseActivity.class);
        startActivity(i);
    });

    i.setOnClickListener(view -> {
        Toast.makeText(OcrActivity.this, " image is clicked ", Toast.LENGTH_SHORT).show();
        ImagePicker.with(OcrActivity.this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    });

    }

    private void setupDrawerLayout() {
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, Drawer ,R.string.app_name,R.string.app_name);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            i.setImageURI(uri);
            generateText.setEnabled(true);
            generateText.setAlpha(1);
            try {
                 bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                Toast.makeText(OcrActivity.this, " error occured ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();


            }
        }
        else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void detect() {

        TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if(!recognizer.isOperational()){
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();

        }
        else{
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = recognizer.detect(frame);
            StringBuilder sb = new StringBuilder();

            for(int i = 0;i<items.size();i++){
                TextBlock myItems = items.valueAt(i);
                sb.append(myItems.getValue());

            }

            resultTV.setText(sb.toString());
            Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
            if(!resultTV.getText().toString().isEmpty())
            {
                resultTV.setVisibility(View.VISIBLE);
                next.setEnabled(true);
                next.setAlpha(1);
            }
          //  Result = sb.toString().trim().toLowerCase().split("");

        }

      /*  InputImage image = InputImage.fromBitmap(bitmap,0);
        TextRecognizer recognizer  = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result = recognizer.process(image).addOnSuccessListener(text -> {
             StringBuilder result1 = new StringBuilder();
             for(Text.TextBlock block : text.getTextBlocks()){
                 BlockText = block.getText();
//                     Point[]  blockCornerPoint = block.getCornerPoints();
//                     Rect blockframe = block.getBoundingBox();
                 for (Text.Line line : block.getLines()){
//                         String lineTExt = line.getText();
//                         Point[] lineCornerPoint = line.getCornerPoints();
//                         Rect linerect = line.getBoundingBox();
                     for (Text.Element element: line.getElements()){
                         String elementText = element.getText();
                         result1.append(elementText);
                     }

                 }

             }
            resultTV.setText(BlockText);*/


    }



}

