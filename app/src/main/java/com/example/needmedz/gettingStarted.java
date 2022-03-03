package com.example.needmedz;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

//import androidx.appcompat.app.AppCompatActivity;

public class gettingStarted extends AppCompatActivity {

    FirebaseAuth Mauth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started);

        Mauth = FirebaseAuth.getInstance();
        Button b = findViewById(R.id.getnStarted);

        // if user is loggedin the current user object is not null , it has some value!!
        if(Mauth.getCurrentUser() != null){
            Toast.makeText(gettingStarted.this,"User is already Login ",Toast.LENGTH_SHORT).show();
            redirect("Main");
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirect("Login");
            }
        });

    }

    private void redirect(String name) {
        Intent i = null;
        if(name == "Login")
        {
            i = new Intent(gettingStarted.this , loginActivity.class);
        }
        else if(name == "Main")
        {
            i = new Intent(gettingStarted.this , OcrActivity.class);
        }
        else
        {
            Toast.makeText(gettingStarted.this,"No path exist",Toast.LENGTH_SHORT).show();
        }
        startActivity(i);
        finish();
    }
}