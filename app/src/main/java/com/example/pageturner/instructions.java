package com.example.pageturner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

public class instructions extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        configureHomeButton();
    }
    private void configureHomeButton(){
        Button nextButton = (Button) findViewById(R.id.Continue);
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(instructions.this,startPage.class));
            }
        });
    }
    private void configureCheckBox(){
        CheckBox chk = (CheckBox) findViewById(R.id.checkbox);
        chk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean checked = ((CheckBox) v).isChecked();
                if(checked){
                    startActivity(new Intent(instructions.this,startPage.class));
                }
            }
        });

    }
}
