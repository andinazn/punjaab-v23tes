package com.andinazn.sensordetectionv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputEmergencyNumber extends AppCompatActivity {

    EditText ip1, ip2, ip3, ip4, ip5;
    Button buttonNext;
    Toolbar toolbar;
    String email, password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_emergency_number);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Your Profile");
        ip1 = (EditText) findViewById(R.id.inputPhone);
        ip2 = (EditText) findViewById(R.id.inputPhone2);
        ip3 = (EditText) findViewById(R.id.inputPhone3);
        ip4 = (EditText) findViewById(R.id.inputPhone4);
        ip5 = (EditText) findViewById(R.id.inputPhone5);
        buttonNext = (Button) findViewById(R.id.buttonNext);

        Intent intent = getIntent();
        if (intent != null) {
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
        }
    }



    public void goToNameActivity(View v)  {



        if (ip1.getText().toString().length() > 0) {

            Intent myIntent = new Intent(InputEmergencyNumber.this, RegisterNameActivity.class);
            myIntent.putExtra("emergencynumber1", ip1.getText().toString());
            myIntent.putExtra("email", email);
            myIntent.putExtra("password", password);

            if (ip2.getText().toString().length() > 0){
                myIntent.putExtra("emergencynumber2", ip2.getText().toString());
            }
            else {myIntent.putExtra("emergencynumber2","0");}

            if (ip3.getText().toString().length() > 0){
                myIntent.putExtra("emergencynumber3", ip3.getText().toString());
            }
            else {myIntent.putExtra("emergencynumber3","0");}


            if (ip4.getText().toString().length() > 0){
                myIntent.putExtra("emergencynumber4", ip4.getText().toString());
            }
            else {myIntent.putExtra("emergencynumber4","0");}

            if (ip5.getText().toString().length() > 0){
                myIntent.putExtra("emergencynumber5", ip5.getText().toString());
            }
            else {myIntent.putExtra("emergencynumber5","0");}

            startActivity(myIntent);
            finish();
        }

        else
        {
            Toast.makeText(getApplicationContext(),"Must fill at least ONE emergency number!", Toast.LENGTH_SHORT).show();
        }
    }



}
