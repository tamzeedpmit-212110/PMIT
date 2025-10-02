package com.example.medreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.medreminder.databinding.ActivitySignUpBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    DatabaseReference database_user;
    MysharedPreferance mysharedPreferance;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mysharedPreferance = MysharedPreferance.getPreferences(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        database_user = FirebaseDatabase.getInstance().getReference("users");


        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });


        binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.name.getText().toString().isEmpty()) {
                    binding.name.setError("required");
                } else if (binding.phone.getText().toString().isEmpty()) {
                    binding.name.setError("required");
                } else if (binding.email.getText().toString().isEmpty()) {
                    binding.email.setError("required");
                } else if (!isValidEmail(binding.email.getText().toString())) {
                    binding.email.setError("invalid email");
                } else if (binding.password.getText().toString().isEmpty()) {
                    binding.password.setError("required");
                } else {

                    String[] parts = binding.email.getText().toString().split(Pattern.quote("."));


                    ProgressDialog dialog = ProgressDialog.show(SignUpActivity.this, "Loading", "Please wait...", true);
                    ModelUser modelUser = new
                            ModelUser(binding.name.getText().toString()
                            , binding.email.getText().toString(),
                            binding.phone.getText().toString(), binding.password.getText().toString()
                            , "" + parts[0]);

                    database_user.child(parts[0]).setValue(modelUser);

                    dialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    finish();


                }
            }
        });

    }

    public static boolean isValidEmail(CharSequence email) {


        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}