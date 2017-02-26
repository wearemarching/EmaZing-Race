package com.example.loginregister;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String token = FirebaseInstanceId.getInstance().getToken();
        System.out.println(token);

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]

        Button hostButton = (Button) findViewById(R.id.hostButton);
        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eT = (EditText) findViewById(R.id.inputText);

                Intent i = new Intent(getApplicationContext(), HostingGame.class);
                i.putExtra("name", eT.getText().toString());
                startActivity(i);
                finish();
            }
        });

        Button joinButton = (Button) findViewById(R.id.joinButton);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eT = (EditText) findViewById(R.id.inputText);

                Intent i = new Intent(getApplicationContext(), JoiningGame.class);
                i.putExtra("name", eT.getText().toString());
                startActivity(i);
                finish();
            }
        });
    }
}
