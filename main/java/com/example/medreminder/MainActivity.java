package com.example.medreminder;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medreminder.databinding.ActivityMainBinding;
import com.example.medreminder.fragment.HistoryFragment;
import com.example.medreminder.fragment.HomeFragment;
import com.example.medreminder.fragment.ProfileFragment;
import com.example.medreminder.fragment.SettingsFragment;
import com.example.medreminder.fragment.SoSFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    MysharedPreferance mysharedPreferance;
    public static final int request_call = 1;
    final int PERMISSION_REQUEST_CODE =112;
  HomeFragment homeFragment = new HomeFragment();
  HistoryFragment historyFragment = new HistoryFragment();
  SoSFragment soSFragment = new SoSFragment();
  ProfileFragment profileFragment = new ProfileFragment();
  SettingsFragment settingsFragment = new SettingsFragment();

  androidx.appcompat.app.ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
        backActionBar.setVisibility(View.GONE);
        title.setText("My Medicine");
        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);

        nameActionBar.setText(mysharedPreferance.getName());


        //permission();
       //requestNotificationPermission();
        //getNotificationPermission();


        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        binding.bottomNev.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:

                        title.setText("My Medicine");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;

                    case R.id.history:
                        title.setText("Medicine History");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, historyFragment).commit();
                        return true;

                    case R.id.sos:
                        title.setText("Emergency SOS");
                        if (mysharedPreferance.getSession().equals("none")) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        } else {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, soSFragment).commit();
                        }
                        return true;


                    case R.id.profile:
                        title.setText("Profile");
                        if (mysharedPreferance.getSession().equals("none")) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        } else {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                        }
                        return true;


                    case R.id.settings:
                        title.setText("Settings");
                    /*    if (mysharedPreferance.getSession().equals("none")) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        } else {*/
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();
                        //}
                        return true;


                }



                return false;
            }
        });


        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();

        if (netInfo != null) {
            if (netInfo.isConnected()) {
                // Internet Available
            }else {
                Toast.makeText(this, "Please check the internet connection", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please check the internet connection", Toast.LENGTH_LONG).show();
        }


    }
    @Override
    public void onBackPressed() {

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            new AlertDialog.Builder(this)
                    .setTitle(" App Exit ")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        }

        mBackPressed = System.currentTimeMillis();

    }




    public void permission(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY}, request_call);

        } else {

        }
    }




   /* @RequiresApi(api = 33)
    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {

        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, request_call );
    }*/

   /* @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // Checking the request code of our request
        if (requestCode == request_call ) {

            // If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                // Displaying another toast if permission is not granted

            }
        }}*/

}