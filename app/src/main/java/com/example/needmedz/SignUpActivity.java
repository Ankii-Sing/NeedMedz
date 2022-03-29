package com.example.needmedz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    EditText Email;
    EditText Pass;
    EditText CnfPass;
    Button SignUp;
    TextView HaveAccnText;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        SignUp = findViewById(R.id.btnsignUp);
        Email = findViewById(R.id.username_input);
        Pass = findViewById(R.id.pass);
        CnfPass = findViewById(R.id.cnfpass);

//        After clicking "Already have account " text redirect to Login activity
        HaveAccnText = findViewById(R.id.haveAccn);
        HaveAccnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this,loginActivity.class);
                startActivity(i);
                finish();
            }
        });


        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp();

            }
        });
    }

    private void SignUp() {
        String email = Email.getText().toString();
        String pass = Pass.getText().toString();
        String cnfPass = CnfPass.getText().toString();

//      <<-----  Checking Validation ----->>
        if (email.isEmpty() || pass.isEmpty() || cnfPass.isEmpty()){
            Toast.makeText(SignUpActivity.this, "Email and Password can't be blank ", Toast.LENGTH_LONG).show();
        return;
        }
        if(!pass.equals(cnfPass)){
            Toast.makeText(SignUpActivity.this, "Password and Confirm Password do not match ", Toast.LENGTH_LONG).show();
            return;
        }
        if (pass.length() < 6) {
            Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
            return;
        }



        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                    AuthResult result = task.getResult();
                    Toast.makeText(SignUpActivity.this,"login Successful ",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpActivity.this , OcrActivity.class);
                    i.putExtra("keyemail",email);
                    startActivity(i);
                    finish();
                    // You can access the new user via result.getUser()
                    // Additional user info profile *not* available via:
                    // result.getAdditionalUserInfo().getProfile() == null
                    // You can check if the user is new or existing:
                    // result.getAdditionalUserInfo().isNewUser()
                } else {

                    Toast.makeText(SignUpActivity.this,"Error Creating User ",Toast.LENGTH_SHORT).show();
                }
            }
        });
     }
}