package com.example.loginregister;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.Game;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GameActivity extends AppCompatActivity implements MapsFragment.OnFragmentInteractionListener{

    private String gameToken;
    private static String EXTRA_MESSAGE;
    public final static  String win = "com.example.loginregister";
    private int cFrag = 1;
    private int quest = 1;
    private String uName1;
    private String uName2;
    private String uName3;
    private String uName4;

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


    public class HttpGetter2 extends AsyncTask<Void,Void,String> {
        private String destURL;
        private String gameToken;
        public HttpGetter2(String _URL) {
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
            if (result.equals("1")) {
                Intent i = new Intent(getApplicationContext(), Shake.class);
                i.putExtra("wl", "1");
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(getApplicationContext(), Shake.class);
                i.putExtra("wl", "0");
                startActivity(i);
                finish();
            }
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String msg = extras.getString("msg");
            System.out.println("hehehe");
            if (msg != null) {
                System.out.println(msg);
                try {
                    JSONObject jObject = new JSONObject(msg);
                    double lat = jObject.getDouble("lat");
                    double lng = jObject.getDouble("lng");
                    String name = jObject.getString("name");
                    System.out.println(Double.toString(lat) + " " + Double.toString(lng) + " " + name);
                    MapsFragment articleFrag = (MapsFragment)
                            getSupportFragmentManager().findFragmentById(R.id.container);
                    if (name.equals(uName1)) {
                        articleFrag.createPlayer1(lat, lng);
                    } else if (name.equals(uName2)) {
                        articleFrag.createPlayer2(lat, lng);
                    } else if (name.equals(uName3)) {
                        articleFrag.createPlayer3(lat, lng);
                    } else if (name.equals(uName4)) {
                        articleFrag.createPlayer4(lat, lng);
                    }
                } catch (Exception ex) {
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.google.firebase.quickstart.fcm.onMessageReceived");
        GameActivity.MyBroadcastReceiver receiver = new GameActivity.MyBroadcastReceiver();
        Intent intent = registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent myIntent = getIntent();
        gameToken = myIntent.getStringExtra("gameToken");
        EXTRA_MESSAGE = myIntent.getStringExtra("EXTRA_MESSAGE");

        String[] unames = EXTRA_MESSAGE.split(" ");
        uName1 = unames[0];
        uName2 = unames[1];
        uName3 = unames[2];
        uName4 = unames[3];

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tryhard);

        final String token = FirebaseInstanceId.getInstance().getToken();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                stopService(new Intent(getApplicationContext(), LocationBroadcastService.class));
                new HttpGetter2("https://whispering-lake-62045.herokuapp.com/wl?fbToken=" + token + "&gameToken=" + gameToken).execute();

            }
        }, 300000);

        Intent mServiceIntent = new Intent(getApplicationContext(), LocationBroadcastService.class);
        mServiceIntent.setData(Uri.parse("https://whispering-lake-62045.herokuapp.com/location?gameToken=" + gameToken + "&fbToken=" + token));
        startService(mServiceIntent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Maps",
                        "Quests",
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                if (id == 0) {
                    cFrag = 1;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, MapsFragment.newInstance(gameToken, quest))
                            .commit();
                } else {
                    cFrag = 2;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, PostFinder.newInstance("a","b"))
                            .commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (quest == 1) {
                Toast.makeText(this,"A place where it's people have spirits of warriors.",Toast.LENGTH_LONG).show();
            } else if (quest == 2) {
                Toast.makeText(this,"This post is a post?",Toast.LENGTH_LONG).show();
            } else if (quest == 3) {
                Toast.makeText(this,"denki gakusei dantai!",Toast.LENGTH_LONG).show();
            } else if (quest == 4) {
                Toast.makeText(this,"Neutral, eh?",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,"You've found all posts!",Toast.LENGTH_LONG).show();
            }
            return true;
        } else if (id == R.id.action_settings2) {
            if (cFrag == 1) {
                MapsFragment articleFrag = (MapsFragment)
                        getSupportFragmentManager().findFragmentById(R.id.container);
                if (articleFrag.checkLocationSame()) {
                    Toast.makeText(this, "You are really close to a post!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "You are not next to a post!", Toast.LENGTH_LONG).show();
                }
            } else if (cFrag == 2) {
                PostFinder articleFrag = (PostFinder)
                        getSupportFragmentManager().findFragmentById(R.id.container);
                if (articleFrag.checkQuest(quest)) {
                    Toast.makeText(this, "You found a post!", Toast.LENGTH_LONG).show();
                    quest += 1;
                } else {
                    Toast.makeText(this, "Try again!", Toast.LENGTH_LONG).show();
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tryhard, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(EXTRA_MESSAGE);
            return rootView;
        }

    }
}