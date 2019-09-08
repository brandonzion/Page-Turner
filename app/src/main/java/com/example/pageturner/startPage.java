package com.example.pageturner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import java.util.ArrayList;
import java.util.List;

public class startPage extends AppCompatActivity {
    ListView lv;
    ArrayList<Drawable> pages = new ArrayList<Drawable>();
    Drawable[] newPages;
    Drawable[] newPlayList;
    CustomAdapter adapter;
    playList showPlayList;
    Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        File[] files = new File("/data/data/com.example.pageturner/app_songs").listFiles();
        toast = Toast.makeText(getApplicationContext(), "You need to choose a page before you can continue.", Toast.LENGTH_SHORT);
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
        showPlayList = (playList)getApplication();
        newPlayList = new Drawable[showPlayList.playList.size()];
        newPlayList = showPlayList.playList.toArray(newPlayList);
        ListView lv = (ListView)findViewById(R.id.showImages);
        adapter = new CustomAdapter(this,imageNames,pages);
        lv.setAdapter(adapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setItemChecked(2,true);

        configureStartButton();
        configureInstructionButton();
        configurePlayButton();
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
    private void configurePlayButton(){
        Button nextButton = (Button) findViewById(R.id.selectedPages);
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(adapter.getCheckedItems().size()!=0){
                    showPlayList.playList= adapter.getCheckedItems();
                    startActivity(new Intent(startPage.this,Play.class));
                }else{
                    toast.setGravity(Gravity.TOP,0,0);
                    toast.show();
                }
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
