package com.example.loginregister;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HostingGame extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.loginregister.MESSAGE";
    private Boolean done = false;
    private Boolean success = false;
    private int nUser = 1;
    private String g_gtoken;
    private String g_fbtoken;
    private String g_name;

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String type = extras.getString("type");
            String newuser = extras.getString("name");
            if (type.contains("Join")) {
                nUser++;
                ;// update your textView in the main layout
                switch (nUser) {
                    case 2:
                        TextView user2 = (TextView) findViewById(R.id.user2);
                        user2.setText(newuser);
                        break;
                    case 3:
                        TextView user3 = (TextView) findViewById(R.id.user3);
                        user3.setText(newuser);
                        break;
                    case 4:
                        TextView user4 = (TextView) findViewById(R.id.user4);
                        user4.setText(newuser);
                        break;
                }
            } else if (type.contains("Leave")) {
                ;// update your textView in the main layout
                final TextView user1 = (TextView) findViewById(R.id.user1);
                final TextView user2 = (TextView) findViewById(R.id.user2);
                final TextView user3 = (TextView) findViewById(R.id.user3);
                final TextView user4 = (TextView) findViewById(R.id.user4);
                switch (nUser) {
                    case 2:
                        if (user2.getText().toString().contains(newuser)) {
                            user2.setText("Empty");
                        } else if (user1.getText().toString().contains(newuser)) {
                            user1.setText(user2.getText().toString());
                            user2.setText("Empty");
                        }
                        break;
                    case 3:
                        if (user3.getText().toString().contains(newuser)) {
                            user3.setText("Empty");
                        } else if (user2.getText().toString().contains(newuser)) {
                            user2.setText(user3.getText().toString());
                            user3.setText("Empty");
                        } else if (user1.getText().toString().contains(newuser)) {
                            user1.setText(user3.getText().toString());
                            user3.setText("Empty");
                        }
                        break;
                    case 4:
                        if (user4.getText().toString().contains(newuser)) {
                            user4.setText("Empty");
                        } else if (user3.getText().toString().contains(newuser)) {
                            user3.setText(user4.getText().toString());
                            user4.setText("Empty");
                        } else if (user2.getText().toString().contains(newuser)) {
                            user2.setText(user4.getText().toString());
                            user4.setText("Empty");
                        } else if (user1.getText().toString().contains(newuser)) {
                            user1.setText(user4.getText().toString());
                            user4.setText("Empty");
                        }
                        break;
                }
                nUser--;
            }

        }
    }

    public class HttpGetter extends AsyncTask<Void,Void,String> {
        private String destURL;
        private String gameToken;
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
            final TextView t = (TextView)findViewById(R.id.gameCodeTextView);
            String waiting = "Please wait ";
            t.setText(waiting);
            for (int i = 0; i < 12; i++) {
                final String text = waiting + ".";
                waiting = text;
                if (i % 3 == 2) {
                    waiting = "Please wait ";
                }
                Handler tes = new Handler();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (t.getText().toString().contains("Please wait")) {
                            t.setText(text);
                        } else {
                            t.setText(gameToken);
                        }
                    }
                }, 500*i);
            }
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
            TextView t = (TextView)findViewById(R.id.gameCodeTextView);
            done = true;
            gameToken = result;
            g_gtoken = result;
            t.setText(result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent myIntent = getIntent();
        final String name = myIntent.getStringExtra("name");
        g_name = name;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosting_game);

        final String token = FirebaseInstanceId.getInstance().getToken();
        g_fbtoken = token;

        new HttpGetter("https://whispering-lake-62045.herokuapp.com/host?fbToken="+token+"&name="+name).execute();

        final TextView user1 = (TextView) findViewById(R.id.user1);
        final TextView user2 = (TextView) findViewById(R.id.user2);
        final TextView user3 = (TextView) findViewById(R.id.user3);
        final TextView user4 = (TextView) findViewById(R.id.user4);
        user1.setText(name);

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] arrGToken = g_gtoken.split(" ");
                if (arrGToken.length >= 2) {
                    new HttpGetter("https://whispering-lake-62045.herokuapp.com/leave?fbToken=" + token + "&name=" + name + "&gameToken="+arrGToken[0]+"%20"+arrGToken[1]).execute();
                }
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                success = true;
                if (g_gtoken != null) {
                    String[] arrGToken = g_gtoken.split(" ");
                    new HttpGetter("https://whispering-lake-62045.herokuapp.com/start?gameToken=" + arrGToken[0] + "%20" + arrGToken[1]).execute();
                    Intent i = new Intent(getApplicationContext(), StartingGame.class);
                    i.putExtra("gameToken", arrGToken[0]+"%20"+arrGToken[1]);
                    i.putExtra("EXTRA_MESSAGE", user1.getText().toString()+ "  " + user2.getText().toString()+ "  " + user3.getText().toString()+ "  " + user4.getText().toString());
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.google.firebase.quickstart.fcm.onMessageReceived");
        MyBroadcastReceiver receiver = new MyBroadcastReceiver();
        Intent intent = registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!success) {
            String[] arrGToken = g_gtoken.split(" ");
            if (arrGToken.length >= 2) {
                new HttpGetter("https://whispering-lake-62045.herokuapp.com/leave?fbToken=" + g_fbtoken + "&name=" + g_name + "&gameToken=" + arrGToken[0] + "%20" + arrGToken[1]).execute();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        }
    }
}
