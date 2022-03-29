package com.example.needmedz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

public class DataBaseActivity extends AppCompatActivity {

    TextView MedData ;
    ImageView back;
    private  String Med;
    private  String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);
        MedData = findViewById(R.id.MedName);
        back = findViewById(R.id.Backbtn);

        Med = getIntent().getStringExtra("medData");
        MedData.setText(Med);

        s = Med.substring(0,5);
        ProcessSearch(s);

//       --------- Back Button implementation---
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DataBaseActivity.this, OcrActivity.class);
                startActivity(i);
            }
        });





    }

    private void ProcessSearch(String s) {
        FirebaseDatabase.getInstance().getReference().orderByChild("Prescribed medicine").startAt(s).endAt(s + "\uf8ff");

    }
}