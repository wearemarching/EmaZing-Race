package com.example.loginregister;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.support.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.Toast;
import android.view.Window;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText getEmail;
    private EditText getPass;
    private Button getLogin;
    private  String result, eMail;

    //Shared Preferences
    SharedPreferences sp;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        getEmail = (EditText) findViewById(R.id.inputEmail);
        sp = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        eMail = sp.getString("email","");
        getEmail.setText(eMail);
        getPass = (EditText) findViewById(R.id.inputPassword);
        getLogin = (Button) findViewById(R.id.loginButton);
        progressDialog = new ProgressDialog(this);
        getLogin.setOnClickListener(this);

    }

    //method for user login
    private void userLogin(){
        String email = getEmail.getText().toString().trim();
        String password  = getPass.getText().toString().trim();


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

        progressDialog.setMessage("Logging In Please Wait...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //save
                            sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("email", getEmail.getText().toString());
                            editor.apply();

                            //get
                            sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                            eMail = sp.getString("email","");
                            System.out.println(eMail);
                            getEmail.setText(eMail);

                            //start the main activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }
                });

    }

    public void goRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onClick(View view) {
        if(view == getLogin){
            userLogin();
        }
    }

}
