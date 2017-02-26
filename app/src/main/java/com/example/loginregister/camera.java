package com.example.loginregister;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
//referensi: https://www.youtube.com/watch?v=6Z6k7X2vfhk
public class camera extends Activity{

    ImageView imageView;
    private File imageFile;
    private File folder;
    float[] hsv;
    int runColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);
    }

    public void process (View view){
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

}

