package com.example.pageturner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

public class startInstructions extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startnstructions);
        configureCheckBox();
        SharedPreferences prefs = getSharedPreferences("skipIntroCheckbox",0);
        boolean checked = prefs.getBoolean("skipIntro", false);
        if(checked){
            startActivity(new Intent(startInstructions.this,startPage.class));
        }
        configureHomeButton();
    }
    private void configureHomeButton(){
        Button nextButton = (Button) findViewById(R.id.Continue);
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(startInstructions.this,startPage.class));
            }
        });
    }
    private void configureCheckBox(){
        CheckBox chk = (CheckBox) findViewById(R.id.checkbox);
        chk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean checked = ((CheckBox) v).isChecked();
                SharedPreferences sharedPreferences = getSharedPreferences("skipIntroCheckbox",0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("skipIntro", checked);
                editor.commit();

            }
        });

    }
}
