package com.example.medreminder;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medreminder.databinding.DialogAddMedicineBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class AdapterMed extends FirebaseRecyclerAdapter<ModelMed, AdapterMed.Holder> {

    int intQuantity, totalTaken;
    private static final String TAG = "time";
    DialogAddMedicineBinding bindingAddMed, bindingEditMedMed;
    Boolean flag = false;
    AlertDialog dialog;
    Context context;
    DatabaseReference databaseReference;
    MysharedPreferance mysharedPreferance;
    String callForm;
    String message = "";


    public AdapterMed(@NonNull FirebaseRecyclerOptions<ModelMed> options, Context context, String callfrom) {
        super(options);
        this.context = context;
        this.callForm = callfrom;
    }


    @Override
    protected void onBindViewHolder(@NonNull AdapterMed.Holder holder, int position, @NonNull ModelMed model) {
        mysharedPreferance = MysharedPreferance.getPreferences(context);
        databaseReference = FirebaseDatabase.getInstance().getReference("medicines");

        intQuantity = Integer.parseInt(model.quantity);
        totalTaken = model.getTakenQuantity();

        holder.type.setVisibility(View.GONE);
        if (callForm.equals("history")) {

            holder.delete.setImageResource(R.drawable.ic_delete);
            holder.restore.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.INVISIBLE);
            holder.taken.setVisibility(View.INVISIBLE);
            holder.last_taken.setVisibility(View.GONE);
            holder.quantity.setText("" + model.getQuantity());

            holder.layout.setBackgroundColor(Color.parseColor("#edebeb"));
        } else if (callForm.equals("mymed")) {
            holder.delete.setImageResource(R.drawable.ic_close);
            holder.restore.setVisibility(View.INVISIBLE);
            holder.last_taken.setVisibility(View.VISIBLE);
            holder.quantity.setText("" + model.getQuantity());
            if (intQuantity < 4) {
                holder.layout.setBackgroundColor(Color.parseColor("#F79999"));
                holder.quantity.setText("" + model.getQuantity() + "");
            } else {
                holder.layout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            }
        }
        holder.name.setText("  Medicine : " + model.getName());
        holder.time.setText("    " + model.getTime1() + " ," + model.getTime2() + "," + model.getTime3());
        holder.type.setText("  Type: " + model.getType());

        try {
            holder.pres_quantity.setText("" + model.getPres_quantity());
            holder.total_taken.setText("" + model.takenQuantity);
        } catch (Exception e) {}


        bindingAddMed = DialogAddMedicineBinding.inflate(LayoutInflater.from(context));

        holder.expend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag == false) {
                    if (!callForm.equals("history")) {
                        holder.edit.setVisibility(View.VISIBLE);
                    }
                    holder.delete.setVisibility(View.VISIBLE);
                    // holder.taken.setVisibility(View.VISIBLE);
                    flag = true;
                } else {
                    holder.edit.setVisibility(View.INVISIBLE);
                    holder.delete.setVisibility(View.INVISIBLE);
                    // holder.taken.setVisibility(View.INVISIBLE);
                    flag = false;
                }
            }
        });



      /*  String currentTime= DateTimeFormatter.ofPattern("hh").format(LocalTime.now());
        String[] parts = model.getTime1().split(Pattern.quote(":"));*/


        holder.taken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medTaken(model);
            }
        });


        holder.last_taken.setText(" Last taken: " + model.getLastTaken());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                bindingEditMedMed = DialogAddMedicineBinding.inflate(LayoutInflater.from(context));
                builder.setView(bindingEditMedMed.getRoot());
                dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();

                bindingEditMedMed.name.setText(model.name);
                bindingEditMedMed.dailogTitle.setText("Update Medicine");
                bindingEditMedMed.textView6.setText("Remaining Medicine");
                bindingEditMedMed.quantity.setText(model.quantity);
                bindingEditMedMed.spinner.setVisibility(View.GONE);
                bindingEditMedMed.textView12.setVisibility(View.GONE);
                bindingEditMedMed.prescribeQuantity.setVisibility(View.GONE);
                bindingEditMedMed.textView13.setVisibility(View.GONE);
                bindingEditMedMed.time2.setVisibility(View.GONE);
                bindingEditMedMed.time3.setVisibility(View.GONE);
                bindingEditMedMed.time1.setVisibility(View.GONE);
                bindingEditMedMed.typeGroup.setVisibility(View.GONE);
                bindingEditMedMed.ttMedType.setVisibility(View.GONE);
                bindingEditMedMed.textView11.setVisibility(View.GONE);

                bindingEditMedMed.add.setText("Update");

                bindingEditMedMed.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                bindingEditMedMed.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (bindingEditMedMed.name.getText().toString().isEmpty()) {
                            bindingEditMedMed.name.setError("");
                        } else if (bindingEditMedMed.quantity.getText().toString().isEmpty()) {
                            bindingEditMedMed.quantity.setError("");
                        }/*else if (bindingEditMedMed.prescribeQuantity.getText().toString().isEmpty()){
                            bindingEditMedMed.prescribeQuantity.setError("");
                        }*/ else {
                            databaseReference.child(model.id).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().setValue(bindingEditMedMed.name.getText().toString());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                            databaseReference.child(model.id).child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().setValue(bindingEditMedMed.quantity.getText().toString());
                                    Toast.makeText(context, "Successfully updated", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                    }
                });


            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callForm.equals("history")) {
                    deleteMed(model);

                } else if (callForm.equals("mymed")) {
                    updateMedState("_yes", model, 1);
                }


            }
        });

        holder.restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMedState("_no", model, 0);
            }
        });

    }

    @NonNull
    @Override
    public AdapterMed.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine, parent, false);
        return new Holder(view);
    }

    public class Holder extends RecyclerView.ViewHolder {


        ConstraintLayout layout;
        CardView card_layout;
        TextView name, time, quantity, type, last_taken, restore, pres_quantity, total_taken;
        ImageView expend, edit, delete, taken;

        public Holder(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.medName);
            time = itemView.findViewById(R.id.time);
            quantity = itemView.findViewById(R.id.quantity);
            expend = itemView.findViewById(R.id.expend);
            edit = itemView.findViewById(R.id.edit__med);
            delete = itemView.findViewById(R.id.delete);
            type = itemView.findViewById(R.id.med_type);
            layout = itemView.findViewById(R.id.layout);
            card_layout = itemView.findViewById(R.id.card_layout);
            taken = itemView.findViewById(R.id.taken);
            last_taken = itemView.findViewById(R.id.last_taken);
            restore = itemView.findViewById(R.id.restore);
            pres_quantity = itemView.findViewById(R.id.prescribe_quantity);
            total_taken = itemView.findViewById(R.id.total_taken);

        }
    }


    public void medTaken(ModelMed model) {
        new AlertDialog.Builder(context)
                .setTitle(" Medicine Taken")
                .setMessage("Are you sure to take " + model.name + " ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        databaseReference.child(model.id).child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int currentQuantity = Integer.parseInt(model.quantity);
                                dataSnapshot.getRef().setValue(String.valueOf(currentQuantity - 1));
                                Toast.makeText(context, model.name + " is taken", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
                            }
                        });

                        databaseReference.child(model.id).child("takenQuantity").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int currentTotalTaken = model.getTakenQuantity();
                                dataSnapshot.getRef().setValue((currentTotalTaken + 1));

                                //dose complete checking
                                int checkTotalTaken = model.getTakenQuantity() + 1;
                                if (checkTotalTaken == Integer.parseInt(model.getPres_quantity())) {
                                    autoArchiveMed("_yes", model);
                                }
                                notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        databaseReference.child(model.id).child("lastTaken").addListenerForSingleValueEvent(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                SimpleDateFormat sdf = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    sdf = new SimpleDateFormat("hh:mm yyyy-MM-dd", Locale.getDefault());
                                }
                                String currentDateandTime = sdf.format(new Date());
                                dataSnapshot.getRef().setValue(currentDateandTime);
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                })
                .setNegativeButton("No", null)
                .setIcon(R.drawable.taken)
                .show();
    }

    public void updateMedState(String status, ModelMed model, int perform) {


        if (perform == 1) {
            message = "archive";
        } else {
            message = "restore";
        }

        new AlertDialog.Builder(context)
                .setTitle(message)
                .setMessage("Are you sure to " + message + " " + model.name + " ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //for delete
                        //  databaseReference.child(model.getId()).removeValue();
                        databaseReference.child(model.id).child("userId").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //yes for deleteStatus used for multiple condition
                                dataSnapshot.getRef().setValue(mysharedPreferance.getUserID() + "" + status);
                                dialog.dismiss();
                                Toast.makeText(context, model.name + " successfully " + message, Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.ic_history)
                .show();

    }

    public void autoArchiveMed(String status, ModelMed model) {

        databaseReference.child(model.id).child("userId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //yes for deleteStatus used for multiple condition
                dataSnapshot.getRef().setValue(mysharedPreferance.getUserID() + "" + status);
                Toast.makeText(context, model.name + " dose completed ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void deleteMed(ModelMed model) {
        new AlertDialog.Builder(context)
                .setTitle(" Delete")
                .setMessage("Are you sure to delete " + model.name + " ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //for delete
                        databaseReference.child(model.getId()).removeValue();
                        Toast.makeText(context, model.name + " is removed Permanently !", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.ic_delete)
                .show();
    }


}
