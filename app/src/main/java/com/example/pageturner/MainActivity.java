package com.example.pageturner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
    static ArrayList<Drawable> images = new ArrayList<>();
    ImageButton buttonClick;
    int CAMERA_PIC_REQUEST = 1337;
    static ArrayList<Bitmap> files = new ArrayList<>();
    int numberOfSongs;
    Bitmap[] newFiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonClick=(ImageButton)findViewById(R.id.startCamera);
        buttonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_PIC_REQUEST);
            }
        });
        configureNextButton();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode==CAMERA_PIC_REQUEST){
            Bitmap thumbnail = (Bitmap)data.getExtras().get("data");
            files.add(thumbnail);
            Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
            ImageView image = (ImageView)findViewById(R.id.imageView);
            image.setImageDrawable(drawable);
            images.add(drawable);

        }
        else{
            Toast.makeText(MainActivity.this,"Picture not taken", LENGTH_LONG);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
    private void configureNextButton(){
        ImageButton nextButton = (ImageButton) findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                newFiles = files.toArray(new Bitmap[files.size()]);
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("songs", Context.MODE_PRIVATE);
                SharedPreferences load = getSharedPreferences("numberSongs",0);
                int savedNumberOfSongs = load.getInt("numberOfSongs",1);
                numberOfSongs = savedNumberOfSongs;

                for(int i=0;i<newFiles.length;i++){
                    File file = new File(directory,"page_"+savedNumberOfSongs+"-"+(i+1)+".jpg");
                    if(!file.exists()){
                        Log.d("path", file.toString());
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                            newFiles[i].compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.flush();
                            fos.close();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                numberOfSongs+=1;
                SharedPreferences save = getSharedPreferences("numberSongs",0);
                save.edit().putInt("numberOfSongs",numberOfSongs).commit();
                startActivity(new Intent(MainActivity.this,pictures.class));
            }
        });
    }
    public static ArrayList getData(){
        return images;
    }
}