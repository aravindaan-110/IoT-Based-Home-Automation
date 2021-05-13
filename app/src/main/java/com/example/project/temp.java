package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.net.IpSecManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class temp extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference,reference1,reference2,reference3;
    Switch ac,heater,manual;
    Handler handler = new Handler();
    Runnable runnableCode;

    double f1=89.00;
    int m;
    String f,c,h,a;
    TextView Farenheit,humidity,celcius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

/*
        Farenheit=findViewById(R.id.textView5);
        celcius=findViewById(R.id.textView6);
        humidity=findViewById(R.id.textView7);
        ac=findViewById(R.id.switch1);
        heater=findViewById(R.id.switch2);
        manual=findViewById(R.id.switch3);

        Farenheit.setText("");
        celcius.setText("");
        humidity.setText("");

        reference3=database.getInstance().getReference().child("manual");

        reference=database.getInstance().getReference().child("Fahrenheit");

        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){

                    setm(Integer.parseInt(dataSnapshot.getValue().toString()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){

                    f=dataSnapshot.getValue().toString();
                    f1=Float.parseFloat(f);
                }
                Log.d("Status from Firebase inside loop",f);
                Farenheit.setText(f);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference=database.getInstance().getReference().child("Celsius");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){

                    c=dataSnapshot.getValue().toString();

                }
                Log.d("Status from Firebase",c);
                celcius.setText(c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference=database.getInstance().getReference().child("Humidity");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){

                    h=dataSnapshot.getValue().toString();

                }
                Log.d("Status from Firebase",h);
                humidity.setText(h);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference1=database.getInstance().getReference().child("greenLed");
        reference2=database.getInstance().getReference().child("redLed");
        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status1,status2;
                if(ac.isChecked()){
                    reference1.child("greenLed").setValue("on");
                    reference2.child("redLed").setValue("off");
                    heater.setEnabled(false);
                    status1=ac.getTextOn().toString();
                }
                else{
                    heater.setEnabled(true);
                    reference1.child("greenLed").setValue("off");
                    reference2.child("redLed").setValue("off");
                    status1=ac.getTextOff().toString();}

            }
        });

        heater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(heater.isChecked()){

                    ac.setEnabled(false);
                    reference2.child("redLed").setValue("on");
                    reference1.child("greenLed").setValue("off");
                }
                else{
                    ac.setEnabled(true);
                    reference1.child("greenLed").setValue("off");
                    reference2.child("redLed").setValue("off");
                }
            }
        });

        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(manual.isChecked()){
                    reference3.child("manual").setValue("1");
                }
                else
                    reference3.child("manual").setValue("0");
            }
        });


        Log.d("Status from Firebase outside loop", String.valueOf(f1));

        runnableCode = new Runnable() {
            @Override
            public void run() {
                if(  f1 >= 90){
                    Log.d("ac status","ac on");
                    ac.setChecked(true);
                }
                else if( f1<=80){
                    heater.setChecked(true);
                }
                else{
                    Log.d("ac status","ac off");
                    heater.setChecked(false);
                    ac.setChecked(false);}
                handler.postDelayed(this,2000);
            }
        };
*/
    } // oncreate()

    public void setm(int x){
        m=x;
        if(m==0){
            handler.post(runnableCode);
        }
        else
            handler.removeCallbacks(runnableCode);
    }

    private void notification(){

        Log.d("Status2","Notif() entered");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("n","n", NotificationManager.IMPORTANCE_HIGH);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"n").setContentTitle("Alert from NodeMCU").setSmallIcon(R.drawable.ic_warning)
                .setAutoCancel(true).setContentText("Please check room temperature/humidity");

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(0,builder.build());
    }
}