package com.example.taxiprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class chooseactivity extends AppCompatActivity {

    Button uber,lyft,dev;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseactivity);
        uber=findViewById(R.id.button);
        lyft=findViewById(R.id.button2);
        dev=findViewById(R.id.button_leader);



        uber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String type="uber";
                i = new Intent(chooseactivity.this, dataactivity.class);
                i.putExtra("type",type);
                startActivity(i);
            }
        });

        lyft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String type="lyft";
                i = new Intent(chooseactivity.this, dataactivity2.class);
                i.putExtra("type",type);
                startActivity(i);
            }
        });

        dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(chooseactivity.this,leadership.class);
                startActivity(i);
            }
        });



    }
}