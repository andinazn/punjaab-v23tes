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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginPasswordActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText e1_pass;
    Button b1_password;
    FirebaseAuth auth;
    String email;
    ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_password);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Password");
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);



        e1_pass = (EditText)findViewById(R.id.editTextPass);
        b1_password = (Button)findViewById(R.id.button);

        Intent intent = getIntent();
        if (intent!=null)
        {
            email = intent.getStringExtra("email_login");
        }


    }

    public void Login(View v)
    {
        dialog.setMessage("Please wait. Logging in.");
        dialog.show();
        if(e1_pass.getText().toString().length()>=6)
        {
            auth.signInWithEmailAndPassword(email,e1_pass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        FirebaseUser user = auth.getCurrentUser();

                                        if(user.isEmailVerified())
                                        {
                                            dialog.dismiss();
                                            finish();
                                            Intent myIntent = new Intent(LoginPasswordActivity.this,MainActivity.class);
                                            startActivity(myIntent);
                                        }
                                        else
                                        {
                                            dialog.dismiss();
                                            finish();
                                            FirebaseAuth.getInstance().signOut();
                                            Toast.makeText(getApplicationContext(),"This email is not verified yet. Please check your email", Toast.LENGTH_SHORT).show();
                                            Intent myIntent = new Intent(LoginPasswordActivity.this,PilihanMenuAwal.class);
                                            startActivity(myIntent);
                                        }
                                    }
                                    else
                                    {
                                        dialog.dismiss();
                                       Toast.makeText(getApplicationContext(),"Wrong username/password", Toast.LENGTH_SHORT).show();
                                    }
                        }
                    });





        }
    }
}
