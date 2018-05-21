package com.andinazn.sensordetectionv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PilihanMenuAwal extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilihan_menu_awal);
        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();


        if(user == null)
        {
            setContentView(R.layout.activity_pilihan_menu_awal);


        }
        else
        {
            Intent myIntent = new Intent(PilihanMenuAwal.this,MainActivity.class);
            startActivity(myIntent);
            finish();

        }
    }


    public void getStarted_click(View v)
    {

        Intent myintent = new Intent(PilihanMenuAwal.this,RegisterEmailActivity.class);
        startActivity(myintent);
        finish();

    }

    public void LoginUser(View v)
    {

        Intent myIntent = new Intent(PilihanMenuAwal.this,LoginEmailActivity.class);
        startActivity(myIntent);
        finish();

    }




}
