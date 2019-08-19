package com.example.pageturner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class startPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        configureStartButton();
        configureInstructionButton();
    }


    private void configureStartButton(){
        ImageButton nextButton = (ImageButton) findViewById(R.id.start);
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
