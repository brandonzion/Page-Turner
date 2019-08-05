package com.example.pageturner;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class pictures extends AppCompatActivity {
    ArrayList<Drawable> images = MainActivity.getData();
    Context context;
    ListView lv;
    static Drawable[] newImages;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);
        context = this;
        configureBackButton();
        configurePlayButton();
        toast = Toast.makeText(getApplicationContext(), "You need to take a picture before you can continue.", Toast.LENGTH_SHORT);
        String[] imageNames = new String[images.size()];
        for(int i=0;i<images.size();i++){
            imageNames[i] = String.format("page %s",i+1);
        }
        newImages = images.toArray(new Drawable[images.size()]);
        lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(new CustomAdapter(this,imageNames,newImages));
    }
    private void configureBackButton(){
        ImageButton backButton = (ImageButton) findViewById(R.id.home);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void configurePlayButton(){
        ImageButton nextButton = (ImageButton) findViewById(R.id.Play);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newImages.length != 0){
                    startActivity(new Intent(pictures.this,Play.class));
                }else{
                    toast.setGravity(Gravity.TOP,0,0);
                    toast.show();
                }
            }
        });
    }
    public static Drawable[] getData(){
        return newImages;
    }
}

