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

public class LoginEmailActivity extends AppCompatActivity {

    EditText e1_email;
    Toolbar toolbar;
    Button b1_emailnext;
    ProgressDialog dialog;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);
        e1_email = (EditText)findViewById(R.id.editTextPass);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Sign In");
        dialog = new ProgressDialog(this);




        auth = FirebaseAuth.getInstance();
        b1_emailnext = (Button)findViewById(R.id.button);


    }

    public void checkEmail(View v)
    {
        dialog.setMessage("Please wait!");
        dialog.show();
       auth.fetchProvidersForEmail(e1_email.getText().toString())
               .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                   @Override
                   public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                       boolean check = !task.getResult().getProviders().isEmpty();
                       if(!check)
                       {
                           dialog.dismiss();
                         Toast.makeText(getApplicationContext(),"This email does not exist. Please create an account first", Toast.LENGTH_SHORT).show();

                       }
                       else
                       {
                           // go to password login
                           dialog.dismiss();
                           Intent myIntent = new Intent(LoginEmailActivity.this,LoginPasswordActivity.class);
                           myIntent.putExtra("email_login",e1_email.getText().toString());
                           startActivity(myIntent);
                           finish();


                       }
                   }
               });

    }

}
