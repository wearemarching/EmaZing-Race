package com.example.loginregister;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JoiningGame extends AppCompatActivity {

    private String uname;
    private String gtoken;

    public class HttpGetter extends AsyncTask<Void,Void,String> {
        private String destURL;
        public HttpGetter(String _URL) {
            destURL = _URL;
        }
        // convert InputStream to String
        private String getStringFromInputStream(InputStream is) {

            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return sb.toString();
        }

        protected void onPreExecute() {
            //display progress dialog.
        }

        protected String doInBackground(Void... params) {
            String output = "";
            try {
                URL url = new URL(destURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    output = getStringFromInputStream(in);
                } finally {
                    urlConnection.disconnect();
                }
            } catch(Exception ex) {
            }
            return output;
        }

        protected void onPostExecute(String result) {
            // dismiss progress dialog and update ui
            System.out.println("LOOL" + result);
            if (result.equals("{}")) {
                Log.d("test","Game is full.");
            } else {
                Intent i = new Intent(getApplicationContext(), WaitingGame.class);
                i.putExtra("participants", result);
                i.putExtra("name", uname);
                i.putExtra("gameToken", gtoken);
                startActivity(i);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent myIntent = getIntent();
        final String name = myIntent.getStringExtra("name");
        uname = name;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining_game);

        final String token = FirebaseInstanceId.getInstance().getToken();

        final EditText t = (EditText)findViewById(R.id.gameCodeEditText);

        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] gameToken = t.getText().toString().split(" ");
                gtoken = t.getText().toString();
                if (gameToken.length >= 2) {
                    System.out.println("TES: " + "https://whispering-lake-62045.herokuapp.com/join?fbToken=" + token + "&name=" + name + "&gameToken=" + gameToken[0] + "%20" + gameToken[1]);
                    new JoiningGame.HttpGetter("https://whispering-lake-62045.herokuapp.com/join?fbToken=" + token + "&name=" + name + "&gameToken=" + gameToken[0] + "%20" + gameToken[1]).execute();
                }
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
