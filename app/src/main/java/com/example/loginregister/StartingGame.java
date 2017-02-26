package com.example.loginregister;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StartingGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent myIntent = getIntent();
        final String gameToken = myIntent.getStringExtra("gameToken");
        final String EXTRA_MESSAGE = myIntent.getStringExtra("EXTRA_MESSAGE");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_game);

        final TextView counter = (TextView)findViewById(R.id.counterTextView);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                counter.setText("2");
            }
        }, 1500);
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {
                counter.setText("1");
            }
        }, 2500);
        Handler handler3 = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {
                Intent i = new Intent(getApplicationContext(), GameActivity.class);
                i.putExtra("gameToken", gameToken);
                i.putExtra("EXTRA_MESSAGE", EXTRA_MESSAGE);
                startActivity(i);
                finish();
            }
        }, 3500);
    }
}