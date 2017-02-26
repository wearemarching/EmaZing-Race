package com.example.loginregister;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WaitingGame extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.loginregister.MESSAGE";
    private int nUser = 0;
    private String g_name;
    private String g_fbtoken;
    private String g_gtoken;
    private Boolean success = false;

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
            } else if (type.contains("Disband")) {
                Intent i = new Intent(getApplicationContext(), JoiningGame.class);
                i.putExtra("name", g_name);
                startActivity(i);
                finish();
            } else if (type.contains("Start")) {
                final TextView user1 = (TextView) findViewById(R.id.user1);
                final TextView user2 = (TextView) findViewById(R.id.user2);
                final TextView user3 = (TextView) findViewById(R.id.user3);
                final TextView user4 = (TextView) findViewById(R.id.user4);
                Intent i = new Intent(getApplicationContext(), StartingGame.class);
                final String[] arrGToken = g_gtoken.split(" ");
                i.putExtra("gameToken", arrGToken[0]+"%20"+arrGToken[1]);
                i.putExtra("EXTRA_MESSAGE", user1.getText().toString()+ "  " + user2.getText().toString()+ "  " + user3.getText().toString()+ "  " + user4.getText().toString());
                success = true;
                startActivity(i);
                finish();
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
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent myIntent = getIntent();

        String participants = myIntent.getStringExtra("participants");
        final String name = myIntent.getStringExtra("name");
        final String gameToken = myIntent.getStringExtra("gameToken");
        final String token = FirebaseInstanceId.getInstance().getToken();

        g_name = name;
        g_gtoken = gameToken;
        g_fbtoken = token;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_game);

        TextView gcTextView = (TextView) findViewById(R.id.gameCodeTextView);
        TextView tvUser1 = (TextView)findViewById(R.id.user1);
        TextView tvUser2 = (TextView)findViewById(R.id.user2);
        TextView tvUser3 = (TextView)findViewById(R.id.user3);
        TextView tvUser4 = (TextView)findViewById(R.id.user4);

        gcTextView.setText(g_gtoken);

        JSONObject json;
        try {
            json = new JSONObject(participants);
            if (json.has("user3")) {
                String user1 = json.getString("user1");
                tvUser1.setText(user1);
                String user2 = json.getString("user2");
                tvUser2.setText(user2);
                String user3 = json.getString("user3");
                tvUser3.setText(user3);
                tvUser4.setText(name);
                nUser = 4;
            } else if (json.has("user2")) {
                String user1 = json.getString("user1");
                tvUser1.setText(user1);
                String user2 = json.getString("user2");
                tvUser2.setText(user2);
                tvUser3.setText(name);
                nUser = 3;
            } else if (json.has("user1")) {
                String user1 = json.getString("user1");
                tvUser1.setText(user1);
                tvUser2.setText(name);
                nUser = 2;
            }
        } catch (Exception ex) {
        }

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] arrGToken = gameToken.split(" ");
                new WaitingGame.HttpGetter("https://whispering-lake-62045.herokuapp.com/leave?fbToken="+token+"&name="+name+"&gameToken="+arrGToken[0]+"%20"+arrGToken[1]).execute();

                Intent i = new Intent(getApplicationContext(), JoiningGame.class);
                i.putExtra("name", name);
                success = true;
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.google.firebase.quickstart.fcm.onMessageReceived");
        WaitingGame.MyBroadcastReceiver receiver = new WaitingGame.MyBroadcastReceiver();
        Intent intent = registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!success) {
            final String[] arrGToken = g_gtoken.split(" ");
            new WaitingGame.HttpGetter("https://whispering-lake-62045.herokuapp.com/leave?fbToken=" + g_fbtoken + "&name=" + g_name + "&gameToken=" + arrGToken[0] + "%20" + arrGToken[1]).execute();
            System.out.println("whispering you just eave!");
        }
    }
}
