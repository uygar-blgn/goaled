package com.example.goaled;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.HashMap;

public class MainAuthActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register, passwordReset;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private UserLocal userLocal;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_auth);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        passwordReset = (TextView) findViewById(R.id.forgotPassword);
        passwordReset.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(this, com.example.goaled.RegisterUser.class));
                break;

            case R.id.signIn:
                userLogin();
                break;

            case R.id.forgotPassword:
                startActivity(new Intent(this, PasswordReset.class));
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email and try again.");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError("Password length must be grater than six characters!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users");
                    DatabaseReference userRef = rootRef.child(firebaseUser.getUid());


                    if(firebaseUser.isEmailVerified()){
                        rootRef.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    Log.d("sarkik", "samk");
                                    String email;
                                    String fullName;
                                    String age;
                                    String userClass;
                                    long level;
                                    long xp;
                                    long xpForNextLevel;
                                    double totalPI;
                                    HashMap<String, Double> userStats;
                                    HashMap<String, Double> statMultipliers;
                                    ArrayList<HashMap<String, ?>> allAccomplishments;
                                    ArrayList<HashMap<String, ?>> allGoals;
                                    ArrayList<HashMap<String, ?>> allActivities;
                                    String Uid;
                                    boolean firstTime;

                                    email = (String) snapshot.child("email").getValue();
                                    fullName = (String) snapshot.child("fullName").getValue();
                                    age = (String) snapshot.child("age").getValue();
                                    userClass = (String) snapshot.child("userClass").getValue();
                                    level = (Long) snapshot.child("level").getValue();
                                    xp = (Long) snapshot.child("xp").getValue();
                                    xpForNextLevel = (Long) snapshot.child("xpForNextLevel").getValue();
                                    totalPI = (Double) snapshot.child("totalPI").getValue();
                                    Uid = (String) snapshot.child("uid").getValue();
                                    userStats = (HashMap<String, Double>) snapshot.child("userStats").getValue();
                                    statMultipliers = (HashMap<String, Double>) snapshot.child("statMultipliers").getValue();
                                    allAccomplishments = (ArrayList<HashMap<String, ?>>) snapshot.child("allAccomplishments").getValue();
                                    allGoals = (ArrayList<HashMap<String, ?>>) snapshot.child("allGoals").getValue();
                                    allActivities = (ArrayList<HashMap<String, ?>>) snapshot.child("allActivities").getValue();
                                    firstTime = (boolean) snapshot.child("firstTime").getValue();

                                    userLocal = new UserLocal(email, fullName, age, userClass, level, xp, xpForNextLevel, totalPI, Uid, userStats,
                                            statMultipliers, allAccomplishments, allGoals, allActivities, firstTime);
                                    Intent intent;

                                    if (userLocal.getFirstTime()) {
                                        intent = new Intent(MainAuthActivity.this, PickClass.class);
                                        userLocal.setFirstTime(false);
                                    } else {
                                        intent = new Intent(MainAuthActivity.this, MainPage.class);
                                    }
                                    intent.putExtra("USER", userLocal);

                                    startActivity(intent);
                                    progressBar.setVisibility(View.GONE);
                                }
                                else
                                {
                                    Log.d("sarkik", "sa");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        //redirect to user profile





                    }
                    else{
                        firebaseUser.sendEmailVerification();
                        Toast.makeText(MainAuthActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else{
                    Toast.makeText(MainAuthActivity.this, "Failed to login! Check your credentials.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });





    }
}