package com.example.medreminder.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import com.example.medreminder.AboutActivity;
import com.example.medreminder.MysharedPreferance;
import com.example.medreminder.R;
import com.example.medreminder.databinding.FragmentProfileBinding;
import com.example.medreminder.databinding.FragmentSettingsBinding;

import java.util.Calendar;


public class SettingsFragment extends Fragment {

    MysharedPreferance mysharedPreferance;
    FragmentSettingsBinding binding;
    DatePickerDialog datePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        mysharedPreferance=MysharedPreferance.getPreferences(getContext());

        binding.aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AboutActivity.class));

            }
        });

        binding.calander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                // binding.date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });


        if (mysharedPreferance.getSplashScreen()==1){
            binding.splashCheckBox.setChecked(true);
        }else {
            binding.splashCheckBox.setChecked(false);
        }

        binding.splashCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!binding.splashCheckBox.isChecked()){
                    mysharedPreferance.setSplashScreen(0);
                }
            }
        });

        return view;
    }
}