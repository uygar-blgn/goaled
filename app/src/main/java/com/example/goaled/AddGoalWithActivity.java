package com.example.goaled;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddGoalWithActivity extends AppCompatActivity {

    private UserLocal userLocal;
    private int aimedPI;
    private int aimedHours;
    private String PIorHours = "";
    private UserGoal.Frequency frequency;
    private UserActivity aktivite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal_with);

        EditText piFieldInit = findViewById(R.id.aimedproductivityindex);
        EditText hourFieldInit = findViewById(R.id.aimedhours);
        piFieldInit.setVisibility(View.VISIBLE);
        hourFieldInit.setVisibility(View.GONE);

        userLocal = (UserLocal) getIntent().getSerializableExtra("USER");
        aktivite = (UserActivity) getIntent().getSerializableExtra("ACTIVITY");

        TextView chosenActivity = findViewById(R.id.chosenactivity);
        chosenActivity.setText(aktivite.getName());


        RadioGroup grub = findViewById(R.id.grub);
        grub.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton r1 = findViewById(R.id.gr2);
                RadioButton r2 = findViewById(R.id.gr1);

                if(r1.isChecked()) {
                    r2.setChecked(false);
                    PIorHours = "PI";
                    piFieldInit.setVisibility(View.VISIBLE);
                    hourFieldInit.setVisibility(View.GONE);
                }
                else if(r2.isChecked()) {
                    r1.setChecked(false);
                    PIorHours = "HOURS";
                    piFieldInit.setVisibility(View.GONE);
                    hourFieldInit.setVisibility(View.VISIBLE);
                }
            }
        });

        RadioGroup basis = findViewById(R.id.basis);
        basis.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton r1 = findViewById(R.id.daily);
                RadioButton r2 = findViewById(R.id.monthly);
                RadioButton r3 = findViewById(R.id.weekly);

                if(r1.isChecked()) {
                    r2.setChecked(false);
                    r3.setChecked(false);
                    frequency = UserGoal.Frequency.DAILY;
                }
                else if(r2.isChecked()) {
                    r1.setChecked(false);
                    r3.setChecked(false);
                    frequency = UserGoal.Frequency.MONTHLY;
                }
                else if(r3.isChecked()) {
                    r1.setChecked(false);
                    r2.setChecked(false);
                    frequency = UserGoal.Frequency.WEEKLY;
                }
            }
        });

        Button buton = findViewById(R.id.button8);
        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText piField = findViewById(R.id.aimedproductivityindex);
                EditText hourField = findViewById(R.id.aimedhours);

                if (PIorHours.equals("")) {
                    Toast.makeText(AddGoalWithActivity.this, "Please enter all details!", Toast.LENGTH_LONG).show();
                }
                else {
                    if (PIorHours.equals("PI")) {
                        if (piField.getText().toString().trim().equals("") || frequency == null) {
                            Toast.makeText(AddGoalWithActivity.this, "Please enter all details!", Toast.LENGTH_LONG).show();
                        } else {
                            aimedPI = Integer.parseInt(piField.getText().toString());
                            userLocal.newGoal(new UserGoal(aktivite, aimedPI, PIorHours, frequency));
                            Intent intent = new Intent(getBaseContext(), MainPage.class);
                            intent.putExtra("USER", userLocal);
                            startActivity(intent);
                        }
                    } else if (PIorHours.equals("HOURS")) {
                        if (hourField.getText().toString().trim().equals("") || frequency == null) {
                            Toast.makeText(AddGoalWithActivity.this, "Please enter all details!", Toast.LENGTH_LONG).show();
                        } else {
                            aimedHours = Integer.parseInt(hourField.getText().toString());
                            userLocal.newGoal(new UserGoal(aktivite, aimedHours, PIorHours, frequency));
                            Intent intent = new Intent(getBaseContext(), MainPage.class);
                            intent.putExtra("USER", userLocal);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }
}