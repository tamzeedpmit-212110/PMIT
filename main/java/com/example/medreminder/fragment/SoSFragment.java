package com.example.medreminder.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medreminder.MainActivity;
import com.example.medreminder.MysharedPreferance;
import com.example.medreminder.R;
import com.example.medreminder.databinding.FragmentHomeBinding;
import com.example.medreminder.databinding.FragmentSoSBinding;
import com.example.medreminder.databinding.SosContactDialogBinding;
import com.google.android.material.snackbar.Snackbar;


public class SoSFragment extends Fragment {
    private static final int CONTACT_PICKER_RESULT = 1001;
    public static final int request_call = 1;
    FragmentSoSBinding binding;
    MysharedPreferance mysharedPreferance;
    SosContactDialogBinding binding_dailog ;

    AlertDialog dialog_condition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSoSBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding_dailog=SosContactDialogBinding.inflate(LayoutInflater.from(getContext()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(binding_dailog.getRoot());
        dialog_condition = builder.create();


        mysharedPreferance=MysharedPreferance.getPreferences(getContext());
        binding.ttContact.setText(mysharedPreferance.getSosContact());
        binding.sosmessage.setText(mysharedPreferance.getSosText());


        binding.edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sosEditDailog();
               /* mysharedPreferance.setPhone(binding.ttContact.getText().toString());
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();*/
            }
        });

        binding.edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sosEditDailog();
               /* mysharedPreferance.setSosText(binding.sosmessage.getText().toString());
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();*/
            }
        });




        binding.sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.ttContact.getText().equals("empty")){
                    binding.ttContact.setError("required");
                }
                else if (binding.sosmessage.getText().equals("empty")){
                    binding.sosmessage.setError("required");
                }
                else {


                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions((Activity) getContext(), new String[]{
                                Manifest.permission.SEND_SMS}, request_call);

                    } else {

                        new AlertDialog.Builder(getContext())
                                .setTitle(" Emergency SOS ")
                                .setMessage("Are you sure you want need this?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        try {
                                            String sms=mysharedPreferance.getSosText();
                                            SmsManager smsManager=SmsManager.getDefault();
                                            smsManager.sendTextMessage(mysharedPreferance.getSosContact(),null,sms,null,null);
                                            requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                                            Toast.makeText(getContext(), "Successfully send", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(getContext(), MainActivity.class);
                                            startActivity(intent);
                                        }catch (Exception e){
                                            Toast.makeText(getContext(), "Failed to send", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();



                    }


                }


            }
        });




        return view;
    }

    void showSnackbar(View view){
        Snackbar snackbar = Snackbar.make(view.findViewById(android.R.id.content), "Welcome To Main Activity", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1 :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    ContentResolver cr = getContext().getContentResolver();
                    Cursor cur = cr.query(contactData, null, null, null, null);
                    if (cur.getCount() > 0) {// thats mean some resutl has been found
                        if(cur.moveToNext()) {
                            @SuppressLint("Range") String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                            @SuppressLint("Range") String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            Log.e("Names", name);
                            if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                            {
                                // Query phone here. Covered next
                                Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                                while (phones.moveToNext()) {
                                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    Log.e("Number", phoneNumber);
                                    Toast.makeText(getContext(), "Successfully added "+phoneNumber, Toast.LENGTH_SHORT).show();
                                    binding.ttContact.setText(phoneNumber);
                                    binding_dailog.ttContact.setText(phoneNumber);
                                    mysharedPreferance.setSosContact(phoneNumber);
                                }
                                phones.close();
                            }else {
                                Toast.makeText(getContext(), "popo", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }
                    cur.close();
                }
                break;
       }

    }


    public void sosEditDailog(){



        dialog_condition.show();

        binding_dailog.ttContact.setText(mysharedPreferance.getSosContact());
        binding_dailog.sosmessage.setText(mysharedPreferance.getSosText());

        binding_dailog.addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{
                            Manifest.permission.READ_CONTACTS}, request_call);

                } else {

                    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(contactPickerIntent,1);


                }
            }
        });


        binding_dailog.sosSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mysharedPreferance.setSosContact(binding_dailog.ttContact.getText().toString());
                mysharedPreferance.setSosText(binding_dailog.sosmessage.getText().toString());
                binding_dailog.sosmessage.setText(mysharedPreferance.getSosText());
                binding.sosmessage.setText(mysharedPreferance.getSosText());
                binding_dailog.ttContact.setText(mysharedPreferance.getSosContact());
                binding.ttContact.setText(mysharedPreferance.getSosContact());
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                dialog_condition.dismiss();
            }
        });

    }


}