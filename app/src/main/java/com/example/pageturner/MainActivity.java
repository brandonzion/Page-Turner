package com.example.pageturner;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
    static ArrayList<Drawable> images = new ArrayList<>();
    ImageButton buttonClick;
    int CAMERA_PIC_REQUEST = 1337;
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
                startActivity(new Intent(MainActivity.this,pictures.class));
            }
        });
    }
    public static ArrayList getData(){
        return images;
    }
}