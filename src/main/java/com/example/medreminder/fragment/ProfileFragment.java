package com.example.medreminder.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.medreminder.MysharedPreferance;
import com.example.medreminder.R;
import com.example.medreminder.SplashActivity;
import com.example.medreminder.databinding.EditProfileBinding;
import com.example.medreminder.databinding.FragmentProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    EditProfileBinding editprofileBinding;
    MysharedPreferance mysharedPreferance;
    FragmentProfileBinding binding;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    AlertDialog dialog_edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        mysharedPreferance= MysharedPreferance.getPreferences(getContext());

        editprofileBinding=EditProfileBinding.inflate(LayoutInflater.from(getContext()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(editprofileBinding.getRoot());
        dialog_edit = builder.create();

        binding.name.setText(mysharedPreferance.getName());
        binding.email.setText(mysharedPreferance.getemail());
        binding.phone.setText(mysharedPreferance.getPhone());


        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog_edit.show();

                editprofileBinding.editName.setText(mysharedPreferance.getName());
                editprofileBinding.editPhone.setText(mysharedPreferance.getPhone());

                editprofileBinding.update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      updateName();
                      updatePhone();

                    }
                });

                editprofileBinding.cencel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_edit.dismiss();
                    }
                });


            }
        });


        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Logging out", Toast.LENGTH_LONG).show();
                mysharedPreferance.setSession("none");
                mysharedPreferance.setName("none");
                mysharedPreferance.setUserID("none");
                mysharedPreferance.setPhone("none");
                mysharedPreferance.setEmail("none");
                mysharedPreferance.setlogin_type("none");
                startActivity(new Intent(getContext(), SplashActivity.class));

            }
        });




        return view;
    }

    public  void updateName(){

        reference.child("users").child(mysharedPreferance.getUserID()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().setValue(editprofileBinding.editName.getText().toString());
                mysharedPreferance.setName(editprofileBinding.editName.getText().toString());
                binding.name.setText(editprofileBinding.editName.getText().toString());
                dialog_edit.dismiss();
                Toast.makeText(getContext(), "Successfully updated", Toast.LENGTH_SHORT).show();


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to update", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public  void updatePhone(){

        reference.child("users").child(mysharedPreferance.getUserID()).child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().setValue(editprofileBinding.editPhone.getText().toString());
                mysharedPreferance.setPhone(editprofileBinding.editPhone.getText().toString());
                binding.phone.setText(editprofileBinding.editPhone.getText().toString());
                Toast.makeText(getContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                dialog_edit.dismiss();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to update", Toast.LENGTH_SHORT).show();
            }
        });

    }
}