package com.example.pageturner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class startPage extends AppCompatActivity {
    ListView lv;
    ArrayList<Drawable> pages = new ArrayList<Drawable>();
    Drawable[] newPages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        File[] files = new File("/data/data/com.example.pageturner/app_songs").listFiles();
        try{
            for(int i=0;i<files.length;i++){
                File file = files[i];
                FileInputStream reader = new FileInputStream(file);
                Bitmap b = BitmapFactory.decodeStream(reader);
                android.graphics.drawable.BitmapDrawable d = new android.graphics.drawable.BitmapDrawable(getResources(), b);
                pages.add(d);
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        String[] imageNames = new String[pages.size()];
        for(int i=0;i<pages.size();i++){
            imageNames[i] = String.format("page %s",i+1);
        }
        newPages = new Drawable[pages.size()];
        newPages = pages.toArray(newPages);
        lv = (ListView)findViewById(R.id.showImages);
        lv.setAdapter(new CustomAdapter(this,imageNames,newPages));
        configureStartButton();
        configureInstructionButton();
    }


    private void configureStartButton(){
        Button nextButton = (Button) findViewById(R.id.create);
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(startPage.this,MainActivity.class));
            }
        });
    }
    private void configureInstructionButton(){
        ImageButton nextButton = (ImageButton) findViewById(R.id.instructions);
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(startPage.this,instructions.class));
            }
        });
    }
}
