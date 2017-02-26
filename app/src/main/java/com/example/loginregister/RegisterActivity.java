package com.example.loginregister;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import com.google.firebase.auth.FirebaseAuth;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    private FirebaseAuth firebaseAuth;
    private SensorManager sManager;

    private EditText Email;
    private EditText Username;
    private EditText Password;
    private Button buttonRegister;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Intent intent = getIntent();
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        Email = (EditText) findViewById(R.id.inputEmail);

        Password = (EditText) findViewById(R.id.inputPassword);

        buttonRegister = (Button) findViewById(R.id.registerButton);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonRegister.setOnClickListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        /*register the sensor listener to listen to the gyroscope sensor, use the
        callbacks defined in this class, and gather the sensor information as quick
        as possible*/
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_FASTEST);
    }

    //When this Activity isn't visible anymore
    @Override
    protected void onStop()
    {
        //unregister the sensor listener
        sManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        //Do nothing.
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        //if sensor is unreliable, return void
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        {
            return;
        }


        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.argb(255,157,195,(200+3*((int)(event.values[2])))));


    }

    private void registerUser(){

        String email = Email.getText().toString().trim();
        String password  = Password.getText().toString().trim();
        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here
                            Toast.makeText(RegisterActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
                        }
                        else{
                            //display some message here
                            Toast.makeText(RegisterActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        //calling register method on click
        registerUser();
    }
}
