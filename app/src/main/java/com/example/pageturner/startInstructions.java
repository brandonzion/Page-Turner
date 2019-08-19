package com.example.pageturner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class startInstructions extends AppCompatActivity {
    Boolean checked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startnstructions);
        SharedPreferences isChecked = getSharedPreferences("checkbox",0);
        checked = isChecked.getBoolean("isChecked",false);
        if(checked){
            startActivity(new Intent(startInstructions.this,startPage.class));
        }
        configureCheckBox();
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
        CheckBox chk = (CheckBox) findViewById(R.id.checkBox);
        chk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean checked = ((CheckBox) v).isChecked();
                if(checked){
                    SharedPreferences sharedPreferences = getSharedPreferences("checkbox",0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isChecked",true);
                    editor.commit();
                }

            }
        });

    }
}
