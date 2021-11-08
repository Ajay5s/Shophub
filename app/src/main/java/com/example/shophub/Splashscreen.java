package com.example.shophub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        getSupportActionBar().hide();  //Here your ActionBar is hiding
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i= new Intent(Splashscreen.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },3000);//This is Visible time
    }
}