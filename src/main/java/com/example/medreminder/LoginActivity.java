package com.example.medreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.medreminder.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    MysharedPreferance mysharedPreferance;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    ActivityLoginBinding binding;
    String[] parts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Login");
        mysharedPreferance=MysharedPreferance.getPreferences(LoginActivity.this);


        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailPass;

                if (binding.email.getText().toString().isEmpty()) {
                    binding.email.setError("required");
                } else if (binding.password.getText().toString().isEmpty()) {
                    binding.password.setError("required");
                } else if(!isValidEmail(binding.email.getText().toString())){
                    binding.email.setError("invalid email");
                }

                else {
                    //emailPass=binding.email.getText().toString()+""+binding.password.getText().toString();
                     parts = binding.email.getText().toString().split(Pattern.quote("."));
                    ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "Loading", "Please wait...", true);
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    databaseReference=firebaseDatabase.getReference("users/"
                            +parts[0]+"/password");

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //value match for login

                            if(binding.password.getText().toString().equals(dataSnapshot.getValue(String.class)))
                            {
                                dialog.dismiss();

                                Toast.makeText(LoginActivity.this, "Successfully login", Toast.LENGTH_SHORT).show();
                                mysharedPreferance.setSession("login");

                                getName();
                                getEmail();
                                getPhone();
                                get_Id();
                            }
                            else {

                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {


                            Toast.makeText(LoginActivity.this, "Something Wrong !", Toast.LENGTH_SHORT).show();

                        }
                    });


                }
            }
        });


        binding.createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

    }
    public static boolean isValidEmail(CharSequence email  ) {


        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public void getName(){
        databaseReference=firebaseDatabase.getReference("users/"
                +parts[0]+"/name");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mysharedPreferance.setName(snapshot.getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void getEmail(){
        databaseReference=firebaseDatabase.getReference("users/"
                +parts[0]+"/email");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mysharedPreferance.setEmail(snapshot.getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}

    public void getPhone(){
        databaseReference=firebaseDatabase.getReference("users/"
                +parts[0]+"/phone");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mysharedPreferance.setPhone(snapshot.getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}


    public void get_Id(){
        databaseReference=firebaseDatabase.getReference("users/"
                +parts[0]+"/id");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mysharedPreferance.setUserID(snapshot.getValue(String.class));
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}

}