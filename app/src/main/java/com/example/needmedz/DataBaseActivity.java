package com.example.needmedz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class DataBaseActivity extends AppCompatActivity {

    TextView MedData,Data ;
    ImageView back;

    private  String Med;
    private  String s;
    public static ArrayList<MedicineModel> medL ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);
        MedData = findViewById(R.id.MedName);
        Data = findViewById(R.id.textView9);
        back = findViewById(R.id.Backbtn);
        Med = getIntent().getStringExtra("medData");
        MedData.setText(Med);

//        s = Med.substring(0,5);
        ProcessSearch();


        back.setOnClickListener(view -> onBackPressed());

    }

    private void textwatcher() {
        boolean flag = false;
        String Description = "" ;
        for(MedicineModel meds : medL  ) {
           if( meds.Prescribed_medicine.contains(Med.trim()))
           {
               Description = String.format(" Name : %s \n Use for : %s \n Price : %s \n Dosage : %s \n Side effects : %s \n Patient Review : %s \n",
                       meds.Prescribed_medicine , meds.Disease , meds.Price ,meds.Dosage , meds.SideEffects , meds.PatientReview );
               flag = true;
               break;
           }

        }
        if(flag)
         Data.setText(Description);
        else
            Toast.makeText(this, "Data not found", Toast.LENGTH_SHORT).show();

    }


    private void ProcessSearch() {



//        SharedPreferences pref = getSharedPreferences("Meddata", MODE_PRIVATE);
//       String List =  pref.getString("list" ,"");
//       try {
//           medL = (ArrayList<MedicineModel>) new Gson().fromJson(List,new TypeToken<ArrayList<MedicineModel>>().getType() );
//       }
//       catch (Exception e)
//        {}
//

        textwatcher();

    }

}