package com.andinazn.sensordetectionv2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class RegisterEmailActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText e1_email;
    Button b1_emailnext;
    ProgressDialog dialog;
    FirebaseAuth auth;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        e1_email = (EditText)findViewById(R.id.editTextPass);
            auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

        b1_emailnext = (Button)findViewById(R.id.button);
        toolbar.setTitle("Email Address");

    }


    public void checkIfEmailPresent(View v)
    {
        dialog.setMessage("Please wait");
           dialog.show();

        auth.fetchProvidersForEmail(e1_email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        dialog.dismiss();
                        boolean check = !task.getResult().getProviders().isEmpty();

                        if(!check)
                        {
                            Intent myIntent = new Intent(RegisterEmailActivity.this,RegisterPasswordActivity.class);
                            myIntent.putExtra("email",e1_email.getText().toString());
                            startActivity(myIntent);
                            finish();

                        }
                        else
                        {
                          Toast.makeText(getApplicationContext(),"You already have an account. Please login.", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(RegisterEmailActivity.this,LoginEmailActivity.class);
                            startActivity(myIntent);
                            finish();

                        }
                    }
                });


    }



}
