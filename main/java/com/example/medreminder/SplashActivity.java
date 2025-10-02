package com.example.medreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {
    MysharedPreferance mysharedPreferance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mysharedPreferance = MysharedPreferance.getPreferences(getApplicationContext());

        if (mysharedPreferance.getSplashScreen()==1){
            setContentView(R.layout.activity_splash);
            int secondsDelayed = 3;
            new Handler().postDelayed(new Runnable() {
                public void run() {

                    if (!mysharedPreferance.getSession().equals("none")){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }

                }
            }, secondsDelayed * 1000);
        }
        else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }





    }
}