package com.example.medreminder.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.provider.AlarmClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.medreminder.AdapterMed;
import com.example.medreminder.AlermReceiver;
import com.example.medreminder.ModelMed;
import com.example.medreminder.MysharedPreferance;
import com.example.medreminder.R;
import com.example.medreminder.databinding.DialogAddMedicineBinding;
import com.example.medreminder.databinding.FragmentHomeBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {


    AlarmManager alarmManager1, alarmManager2, alarmManager3;
    PendingIntent pendingIntent;
    Calendar calendar1, calendar2, calendar3;
    int hour1 = 0, hour2 = 0, hour3 = 0;
    int min1 = 0, min2 = 0, min3 = 0;
    MysharedPreferance mysharedPreferance;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DialogAddMedicineBinding bindingAddMed;
    AlertDialog dialog;
    FragmentHomeBinding binding;
    int count;
    public static final int request_call = 1;
    AdapterMed adapter;


    private static final int REQ_CODE_SPEECH_INPUT = 100;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


         SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
       // requestPermission();

        mysharedPreferance = MysharedPreferance.getPreferences(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("medicines");


        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));

        FirebaseRecyclerOptions<ModelMed> options =
                new FirebaseRecyclerOptions.Builder<ModelMed>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().
                                        child("medicines").
                                        orderByChild("userId").
                                        equalTo(mysharedPreferance.getUserID() + "_no"),
                                ModelMed.class)
                        .build();


        adapter = new AdapterMed(options, getContext(), "mymed");
        adapter.startListening();
        binding.recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        binding.progressBar.setVisibility(View.GONE);

        binding.recyclerView.setItemAnimator(null);




        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                count = 0;
                bindingAddMed = DialogAddMedicineBinding.inflate(LayoutInflater.from(getContext()));
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(bindingAddMed.getRoot());
                dialog = builder.create();
                dialog.show();



                suggest_med_name();


                bindingAddMed.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                bindingAddMed.editAlarm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       Intent intent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                       startActivity(intent);
                    }
                });

                bindingAddMed.time1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        getTime(bindingAddMed.time1, 1);
                    }
                });

                bindingAddMed.time2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getTime(bindingAddMed.time2, 2);
                    }
                });

                bindingAddMed.time3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getTime(bindingAddMed.time3, 3);
                    }
                });




                //speech to text
/*
                final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
                final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());


                mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                    @Override
                    public void onReadyForSpeech(Bundle bundle) {

                    }

                    @Override
                    public void onBeginningOfSpeech() {

                    }

                    @Override
                    public void onRmsChanged(float v) {

                    }

                    @Override
                    public void onBufferReceived(byte[] bytes) {

                    }

                    @Override
                    public void onEndOfSpeech() {

                    }

                    @Override
                    public void onError(int i) {

                    }

                    @Override
                    public void onResults(Bundle bundle) {
                        //getting all the matches
                        ArrayList<String> matches = bundle
                                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                        //displaying the first match
                        if (matches != null)
                            bindingAddMed.name.setText(matches.get(0));
                    }

                    @Override
                    public void onPartialResults(Bundle bundle) {

                    }

                    @Override
                    public void onEvent(int i, Bundle bundle) {

                    }
                });



                bindingAddMed.mic.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_UP:
                                mSpeechRecognizer.stopListening();
                                break;

                            case MotionEvent.ACTION_DOWN:
                                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                                bindingAddMed.name.setText("");
                                bindingAddMed.name.setHint("Listening...");
                                break;
                        }
                        return false;
                    }
                });*/

                bindingAddMed.mic.setOnClickListener(new View.OnClickListener() {
                   @Override public void onClick(View view) {

                       startVoiceInput();
                    }
               });


                ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(getContext(), R.array.number, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                bindingAddMed.spinner.setAdapter(adapter);
                bindingAddMed.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (bindingAddMed.spinner.getSelectedItem().equals("1")){
                            bindingAddMed.time1.setVisibility(View.VISIBLE);
                            bindingAddMed.time2.setVisibility(View.GONE);
                            bindingAddMed.time3.setVisibility(View.GONE);
                        }
                        else if (bindingAddMed.spinner.getSelectedItem().equals("2")){
                            bindingAddMed.time1.setVisibility(View.VISIBLE);
                            bindingAddMed.time2.setVisibility(View.VISIBLE);
                            bindingAddMed.time3.setVisibility(View.GONE);
                        }else {
                            bindingAddMed.time1.setVisibility(View.VISIBLE);
                            bindingAddMed.time2.setVisibility(View.VISIBLE);
                            bindingAddMed.time3.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });




                bindingAddMed.weekly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (bindingAddMed.weekly.isChecked()) {
                            bindingAddMed.weekName.setVisibility(View.VISIBLE);
                        } else {
                            bindingAddMed.weekName.setVisibility(View.GONE);
                        }

                    }
                });


                bindingAddMed.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (bindingAddMed.name.getText().toString().isEmpty()) {
                            bindingAddMed.name.setError("");
                        } else if (bindingAddMed.quantity.getText().toString().isEmpty()) {
                            bindingAddMed.quantity.setError("");
                        } else {
                            ProgressDialog dialog2 = ProgressDialog.show(getContext(), "Loading", "Please wait...", true);
                            dialog2.show();
                            Random rnd = new Random();
                            int number = rnd.nextInt(999999);

                            String[] MedNameParts = bindingAddMed.name.getText().toString().split(Pattern.quote(" "));
                            String id = MedNameParts[0] + "" + number;

                            String type;
                            if (bindingAddMed.daily.isChecked()) {
                                type = "Daily";
                            } else {
                                type = "Weekly" + " " + bindingAddMed.weekName.getText().toString();
                            }


                            ModelMed modelMed = new ModelMed(
                                    bindingAddMed.name.getText().toString(), id
                                    , bindingAddMed.time1.getText().toString(),
                                    bindingAddMed.time2.getText().toString(),
                                    bindingAddMed.time3.getText().toString(),
                                    bindingAddMed.prescribeQuantity.getText().toString(),
                                    bindingAddMed.quantity.getText().toString(),
                                    mysharedPreferance.getUserID() + "_no",
                                    type, "not taken yet", 0);

                            databaseReference.child(id).setValue(modelMed);


                            //set alarm push notification
                            setAlarm();

                            dialog2.dismiss();
                            dialog.dismiss();
                            Toast.makeText(getContext(), bindingAddMed.name.getText().toString() + " added successful", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });


        return view;

    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void getTime(TextView timeText, int callFrom) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        //alarm intent initizied
        Intent alarm_intent1,alarm_intent2,alarm_intent3;
        alarm_intent1 = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarm_intent2 = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarm_intent3 = new Intent(AlarmClock.ACTION_SET_ALARM);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                if (callFrom == 1) {
                    hour1 = selectedHour;
                    min1 = selectedMinute;
                    //set alarm
                    alarm_intent1.putExtra(AlarmClock.EXTRA_HOUR, hour1);
                    alarm_intent1.putExtra(AlarmClock.EXTRA_MINUTES, min1);
                    alarm_intent1.putExtra(AlarmClock.EXTRA_ALARM_SNOOZE_DURATION, 5);
                    alarm_intent1.putExtra(AlarmClock.EXTRA_MESSAGE,
                            bindingAddMed.name.getText().toString()+" medicine taken reminder alarm");
                    startActivity(alarm_intent1);

                } else if (callFrom == 2) {
                    hour2 = selectedHour;
                    min2 = selectedMinute;
                    //set alarm
                    alarm_intent2.putExtra(AlarmClock.EXTRA_HOUR, hour2);
                    alarm_intent2.putExtra(AlarmClock.EXTRA_MINUTES, min2);
                    alarm_intent2.putExtra(AlarmClock.EXTRA_ALARM_SNOOZE_DURATION, 5);
                    alarm_intent2.putExtra(AlarmClock.EXTRA_MESSAGE,
                            bindingAddMed.name.getText().toString()+" medicine taken reminder alarm");
                    startActivity(alarm_intent2);

                } else if (callFrom == 3) {
                    hour3 = selectedHour;
                    min3 = selectedMinute;
                    //set alarm
                    alarm_intent3.putExtra(AlarmClock.EXTRA_HOUR, hour3);
                    alarm_intent3.putExtra(AlarmClock.EXTRA_MINUTES, min3);
                    alarm_intent3.putExtra(AlarmClock.EXTRA_ALARM_SNOOZE_DURATION, 5);
                    alarm_intent3.putExtra(AlarmClock.EXTRA_MESSAGE,
                            bindingAddMed.name.getText().toString()+" medicine taken reminder alarm");
                    startActivity(alarm_intent3);

                }
                boolean isPM = (selectedHour >= 12);
                timeText.setText(String.format("%02d:%02d %s", (selectedHour == 12 || selectedHour == 0) ? 12 : selectedHour % 12, selectedMinute, isPM ? "PM" : "AM"));
                bindingAddMed.editAlarm.setVisibility(View.VISIBLE);

            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    public void setAlarm() {
        calendar1 = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
        calendar3 = Calendar.getInstance();

        if ((hour1 != 0)) {

            //set time for push notification
            calendar1.set(Calendar.HOUR_OF_DAY, hour1);
            calendar1.set(Calendar.MINUTE, min1);
            calendar1.set(Calendar.SECOND, 0);
            calendar1.set(Calendar.MILLISECOND, 0);

            //set time for push notification
            alarmManager1 = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            Random rnd = new Random();
            int id = rnd.nextInt(999999);
            Intent intent = new Intent(getContext(), AlermReceiver.class);
            intent.putExtra("name", bindingAddMed.name.getText().toString());

           // PendingIntent appIntent = PendingIntent.getBroadcast(getContext(), id, intent, PendingIntent.FLAG_ONE_SHOT);
            PendingIntent appIntent = PendingIntent.getBroadcast(getContext(), id, intent, PendingIntent.FLAG_IMMUTABLE);

            alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, appIntent);

        }

        if ((hour2 != 0)) {

            //set time for push notification
            calendar2.set(Calendar.HOUR_OF_DAY, hour2);
            calendar2.set(Calendar.MINUTE, min2);
            calendar2.set(Calendar.SECOND, 0);
            calendar2.set(Calendar.MILLISECOND, 0);

            alarmManager2 = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), AlermReceiver.class);
            intent.putExtra("name", bindingAddMed.name.getText().toString());
            Random rnd = new Random();
            int id = rnd.nextInt(99999);

            PendingIntent appIntent = PendingIntent.getBroadcast(getContext(), id, intent, PendingIntent.FLAG_IMMUTABLE);

            alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, appIntent);

        }

        if ((hour3 != 0)) {

            //set time for push notification
            calendar3.set(Calendar.HOUR_OF_DAY, hour3);
            calendar3.set(Calendar.MINUTE, min3);
            calendar3.set(Calendar.SECOND, 0);
            calendar3.set(Calendar.MILLISECOND, 0);

            alarmManager3 = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), AlermReceiver.class);
            intent.putExtra("name", bindingAddMed.name.getText().toString());
            Random rnd = new Random();
            int id = rnd.nextInt(999);

            PendingIntent appIntent = PendingIntent.getBroadcast(getContext(), id, intent, PendingIntent.FLAG_IMMUTABLE);

            alarmManager3.setRepeating(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, appIntent);

        }



    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SET_ALARM) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Perm check:SET_ALARM", "Permission Denied");
            requestPermissions(new String[]{Manifest.permission.SET_ALARM}, 1);
        } else {
            Log.d("Perm check:SET_ALARM", "Permission Exists");
        }
    }

    private void speakNow(){




        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());

        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null)
                    bindingAddMed.name.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });




    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions((Activity) getContext(),new String[]{Manifest.permission.RECORD_AUDIO},1);
        }
    }


    public void suggest_med_name(){

                String[] array=getResources().getStringArray(R.array.med_name);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item, array);

                bindingAddMed.name.setThreshold(2);
                bindingAddMed.name.setAdapter(adapter);


    }


    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Listening...");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

            Toast.makeText(getContext(), ""+a.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    bindingAddMed.name.setText(result.get(0));
                }
                break;
            }

        }
    }

}