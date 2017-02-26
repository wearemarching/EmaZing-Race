package com.example.loginregister;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorEventListener;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.content.Intent;
import android.widget.Toast;

import java.io.File;

public class Shake extends AppCompatActivity implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    private File imageFile;
    private File folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent myIntent = getIntent();
        final String wl = myIntent.getStringExtra("wl");
        System.out.println("AYODONGKAK " + wl);
        if (wl.equals("1")) {
            setContentView(R.layout.activity_winning);
        } else {
            setContentView(R.layout.activity_losing);
        }

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    process();
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void process (){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"EZRace_"+String.valueOf(System.currentTimeMillis()) + ".jpg");
        folder = new File(Environment.getExternalStorageDirectory().getPath(),"/EZRace");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        imageFile = new File(folder,"EZRace_"+String.valueOf(System.currentTimeMillis()) + ".jpg");

        Uri tempuri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempuri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        if(requestCode==0){
            switch (resultCode){
                case Activity.RESULT_OK:
                    if(imageFile.exists()){
                        Toast.makeText(this,"saved at "+imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
                default:
                    break;
            }
        }
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }}


