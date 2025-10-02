package com.example.medreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    MysharedPreferance mysharedPreferance;
    androidx.appcompat.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mysharedPreferance = MysharedPreferance.getPreferences(this);
        //custom action bar
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        TextView nameActionBar = (TextView) mCustomView.findViewById(R.id.name);
        ImageView backActionBar = (ImageView) mCustomView.findViewById(R.id.back);
        backActionBar.setVisibility(View.VISIBLE);
        title.setText("About Us");
        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);

        nameActionBar.setText(mysharedPreferance.getName());
        backActionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });
    }
}