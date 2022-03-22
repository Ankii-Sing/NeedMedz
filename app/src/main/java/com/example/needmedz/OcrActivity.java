package com.example.needmedz;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

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
    String BlockText;

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
                Toast.makeText(OcrActivity.this, " generating Text  ", Toast.LENGTH_SHORT).show();
                detect();
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
        }
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
        InputImage image = InputImage.fromBitmap(bitmap,0);
        TextRecognizer recognizer  = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
                 StringBuilder result = new StringBuilder();
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
                             result.append(elementText);
                         }

                     }

                 }
                resultTV.setText(BlockText);
                 if(!resultTV.getText().toString().isEmpty())
                 {
                     resultTV.setVisibility(View.VISIBLE);
                     next.setEnabled(true);
                 }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OcrActivity.this, "Fail to Detect text from image", Toast.LENGTH_SHORT).show();
            }
        });

    }



}

