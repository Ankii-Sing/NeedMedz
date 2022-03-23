package com.example.needmedz;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

//import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        show splash screen
        Handler h = new Handler();
        h.postDelayed(() -> {
//                #cal next scereen
            Intent i = new Intent(MainActivity.this , gettingStarted.class);
            startActivity(i);
            finish();

        },2500);
    }
}