package com.andinazn.sensordetectionv2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InviteCodeActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView t4_code;
    String name,email,password,date,issharing, emergencynumber1, emergencynumber2, emergencynumber3,emergencynumber4,emergencynumber5;
    String code = null;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog dialog;
    TextView t6_done;
    Integer age;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);

        toolbar =(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Invite Code");
        dialog = new ProgressDialog(this);
        t6_done = (TextView)findViewById(R.id.textView6);


        auth = FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference().child("Users");
        t4_code = (TextView)findViewById(R.id.textView4);

        Intent intent = getIntent();
        if (intent!=null)
        {

            name = intent.getStringExtra("name");
            age = intent.getIntExtra("age", 0);
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
            date = intent.getStringExtra("date");
            issharing = intent.getStringExtra("issharing");
            code = intent.getStringExtra("code");
            emergencynumber1 = intent.getStringExtra("emergencynumber1");
            emergencynumber2 = intent.getStringExtra("emergencynumber2");
            emergencynumber3 = intent.getStringExtra("emergencynumber3");
            emergencynumber4 = intent.getStringExtra("emergencynumber4");
            emergencynumber5 = intent.getStringExtra("emergencynumber5");



        }

        if(code == null)
        {
            // check for code in firebase
            t6_done.setVisibility(View.GONE);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = auth.getCurrentUser();
                    String user_code = dataSnapshot.child(user.getUid()).child("circlecode").getValue().toString();
                    t4_code.setText(user_code);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        else
        {
            t4_code.setText(code);
        }

    }

    public void sendCode(View v)
    {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,"Hello, My GPS Tracker Circle code is "+t4_code.getText().toString()+". Please join my circle.");
        startActivity(i.createChooser(i,"Share using:"));
    }

    public void Register(View v)
    {

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Creating new Profile. Please wait");
        dialog.setCancelable(false);
        dialog.show();

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            user = auth.getCurrentUser();
                            CreateUser createUser = new CreateUser(name,age,email,password,code,"false","na","na",user.getUid(), "normal", "normal",emergencynumber1,emergencynumber2, emergencynumber3, emergencynumber4, emergencynumber5);

                            reference.child(user.getUid()).setValue(createUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                sendVerificationEmail();
                                            }


                                        }
                                    });
                        }


                    }
                });

    }

    public void sendVerificationEmail()
    {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Email sent for verification. Please check email.",Toast.LENGTH_SHORT).show();
                            finish();
                            auth.signOut();

                            Intent myIntent = new Intent(InviteCodeActivity.this,PilihanMenuAwal.class);
                            startActivity(myIntent);
                        }
                        else
                        {
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
