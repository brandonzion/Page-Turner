package com.example.pageturner;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class pictures extends AppCompatActivity {
    ArrayList<Drawable> images = MainActivity.getData();
    Context context;
    ListView lv;
    static Drawable[] newImages;
    Toast toast;
    ArrayList<Drawable> pages = new ArrayList<>();
    Drawable[] newPages;
    ArrayList<String> fileNames = new ArrayList<>();
    Drawable[] newPlayList;
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
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("songs", Context.MODE_PRIVATE);
        try {
            File[] files = new File("/data/data/com.example.pageturner/app_songs").listFiles();
            for(int i=0;i<files.length;i++){
                //if(files[i].getName().substring(0,6).equals("page_1")){
                    //fileNames.add(files[i].getName());
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(files[i]));
                pages.add(new BitmapDrawable(getResources(),b));
                //}
            }
            String[] newFileNames = new String[fileNames.size()];
            newFileNames = fileNames.toArray(newFileNames);
            /*
            for(int i=0;i<fileNames.size();i++){

            }*/
            newPages = new Drawable[pages.size()];
            newPages = pages.toArray(newPages);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        lv = (ListView)findViewById(R.id.listView);

        //lv.setAdapter(new CustomAdapter(this,imageNames,newPlayList));
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

