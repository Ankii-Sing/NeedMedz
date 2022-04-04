package com.example.needmedz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;


import java.io.IOException;
import java.util.ArrayList;

public class OcrActivity extends AppCompatActivity {

    private Toolbar toolbar;
    DrawerLayout Drawer;
    NavigationView nav;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Button generateText ;
    Button next;
    ImageView i ;
    EditText resultTV ;
    private Bitmap bitmap;
    String MedData;
    Button skip;
    private ArrayList<MedicineModel> medlist ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        medlist = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.AppBar);
        Drawer = findViewById(R.id.mainDrawer);
        nav = findViewById(R.id.navigation);
        resultTV = findViewById(R.id.editTextTextMultiLine);
        generateText = findViewById(R.id.button);
        next = findViewById(R.id.button3);
        skip = findViewById(R.id.skip);
        i = findViewById(R.id.imageView2);
        i.setImageResource(R.drawable.uploadicon);
        setupDrawerLayout();
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        LoadData();

        //         setting  up log out feature in navigation drawer.
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(OcrActivity.this, loginActivity.class);
                        startActivity(i);

                        Toast.makeText(OcrActivity.this, " logging Out ", Toast.LENGTH_SHORT).show();

                        return true;
                }
                return true;
            }
        });

// setting up name in the header of naviation drawer
       View hnav =  nav.getHeaderView(0);
        TextView t = (TextView) hnav.findViewById(R.id.email);
        String email = getIntent().getStringExtra("keyemail");
        t.setText(email);


//  generarting text from the image :->
        generateText.setOnClickListener(view -> {
            Toast.makeText(OcrActivity.this, " generating Text  ", Toast.LENGTH_SHORT).show();
            detect();
        });

//         added skip button to data activity.
        skip.setOnClickListener(view -> {
            Toast.makeText(OcrActivity.this, " Gathering Data  ", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(OcrActivity.this,DataActivity.class);
            startActivity(i);
        });

    next.setOnClickListener(view -> {

        Intent i = new Intent(OcrActivity.this,DataBaseActivity.class);
        MedData = resultTV.getText().toString();
        i.putExtra("medData",MedData);                  // passing medicine data to next activity.
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

    private void LoadData() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medlist.clear();

                for (DataSnapshot snap : snapshot.getChildren()  ) {
                    medlist.add(snap.getValue(MedicineModel.class));

                }
                if(medlist.size() > 0)
                    DataBaseActivity.medL = medlist;
                else
                    Toast.makeText(OcrActivity.this, "Details cannot be found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void textpasser() {
//        String Meddata = new Gson().toJson(medlist);
//        SharedPreferences.Editor edit = getSharedPreferences("Meddata" , MODE_PRIVATE).edit();
//        edit.putString("list" , Meddata);
//        edit.apply();
//    }


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
            Toast.makeText(this, " Please Edit the Text to Medecine name only ", Toast.LENGTH_LONG).show();
            if(!resultTV.getText().toString().isEmpty()) {
                resultTV.setVisibility(View.VISIBLE);
                next.setEnabled(true);
                next.setAlpha(1);
            }
        }
    }


}

