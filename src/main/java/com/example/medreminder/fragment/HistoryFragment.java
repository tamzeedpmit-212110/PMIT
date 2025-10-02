package com.example.medreminder.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medreminder.AdapterMed;
import com.example.medreminder.ModelMed;
import com.example.medreminder.MysharedPreferance;
import com.example.medreminder.R;
import com.example.medreminder.databinding.FragmentHistoryBinding;
import com.example.medreminder.databinding.FragmentHomeBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HistoryFragment extends Fragment {

    FragmentHistoryBinding binding;
    MysharedPreferance mysharedPreferance;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    AdapterMed adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mysharedPreferance = MysharedPreferance.getPreferences(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("medicines");

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));

        FirebaseRecyclerOptions<ModelMed> options =
                new FirebaseRecyclerOptions.Builder<ModelMed>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().
                                        child("medicines").
                                        orderByChild("userId").
                                        equalTo(mysharedPreferance.getUserID()+"_yes"),
                                ModelMed.class)
                        .build();





        adapter = new AdapterMed(options, getContext(),"history");
        adapter.startListening();
        binding.recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }
}