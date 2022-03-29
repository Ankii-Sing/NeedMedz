package com.example.needmedz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataActivity extends AppCompatActivity {

    TextView data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        data = findViewById(R.id.data);

        FirebaseDatabase.getInstance().getReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String s ="";
                        ArrayList<MedicineModel> array = new ArrayList<>();

                        for (DataSnapshot snap : snapshot.getChildren())
                        {
                            MedicineModel med =  snap.getValue(MedicineModel.class);
                            array.add(med);
                            s = s +  "\n" + "Disease: " + snap.child("Disease").getValue().toString() + "\n" +
                                    "Dosage:"  + snap.child("Dosage").getValue().toString() + "\n" +
                                    "Patient Review: " + snap.child("Patient Review").getValue().toString() + "\n" +
                                    "Prescribed medicine: " + snap.child("Prescribed medicine").getValue().toString() + "\n" +
                                    "Price(Rs): " + snap.child("Price(Rs)").getValue().toString() + "\n" +
                                    "Side Effects: " + snap.child("Side Effects").getValue().toString() + "\n";

                        }
                        data.setText(s);
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

    }
}