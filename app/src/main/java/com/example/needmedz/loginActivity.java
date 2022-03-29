package com.example.needmedz;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//import androidx.appcompat.app.AppCompatActivity;

public class loginActivity extends AppCompatActivity {

    EditText Email;
    EditText Pass;
    Button LogIn ;
    TextView SignupText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.username_input);
        Pass = findViewById(R.id.password);
        LogIn = findViewById(R.id.btnlogin);
        mAuth = FirebaseAuth.getInstance();

//        After clicking "Dont have account " text redirect to signup activity
        SignupText = findViewById(R.id.tSignUp);
        SignupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(loginActivity.this,SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });


        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

    private void Login() {
        String email = Email.getText().toString();
        String pass = Pass.getText().toString();
//         <<-----  Checking Validation ----->>

        if (email.isEmpty() || pass.isEmpty()){
            Toast.makeText(loginActivity.this, "Email and Password can't be blank ", Toast.LENGTH_LONG).show();
            return;
        }
        if (pass.length() < 6) {
            Toast.makeText(loginActivity.this, "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
            return;
        }

//      <<-------- Login Functionality ---------->>
    mAuth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                Toast.makeText(loginActivity.this,"Success",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(loginActivity.this , OcrActivity.class);
                i.putExtra("keyemail",email);
                startActivity(i);
                finish();
            }
            else
            {
                Toast.makeText(loginActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
            }
        }
    });

    }
}