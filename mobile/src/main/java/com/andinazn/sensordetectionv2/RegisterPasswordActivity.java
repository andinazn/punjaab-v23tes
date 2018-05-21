package com.andinazn.sensordetectionv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterPasswordActivity extends AppCompatActivity {

    EditText e1_password;
    Toolbar toolbar;
    Button b1_password;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);
        e1_password = (EditText)findViewById(R.id.editTextPassword);
        toolbar = (Toolbar)findViewById(R.id.toolbarPassword);
        b1_password = (Button)findViewById(R.id.buttonPassword);

        Intent intent = getIntent();
        if (intent!=null) {
             email = intent.getStringExtra("email");
        }



    }

    public void goToNameActivity(View v)
    {
        if(e1_password.getText().toString().length()>=6)
        {
            // go to Name Activity
            Intent myIntent = new Intent(RegisterPasswordActivity.this,InputEmergencyNumber.class);
            myIntent.putExtra("email",email);
            myIntent.putExtra("password",e1_password.getText().toString());
            startActivity(myIntent);
            finish();

        }
    }


}
