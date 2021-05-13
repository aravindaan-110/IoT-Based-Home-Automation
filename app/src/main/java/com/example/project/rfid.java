package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class rfid extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    int id =0 ;
    int flag =0;
    EditText editText;
    String status = "Door Status";
    TextView textView;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfid);

        textView=findViewById(R.id.textView);
        textView.setText("");
        editText=findViewById(R.id.editText);
        button=findViewById(R.id.button3);
        reference=database.getInstance().getReference().child("Door Status");
        reference.child("Door Status").setValue("Door Locked");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=flag+1;
                String pin=editText.getText().toString();
                String correctPin = "1234";
                Log.d("Pin",pin);
                if(pin.equals(correctPin)){
                    editText.setText("");
                    reference.child("Door Status").setValue("Access Granted");}
                else{
                    if(flag==3){
                        reference.child("Door Status").setValue("Access Denied");
                        editText.setText("");
                        editText.setEnabled(false);
                        textView.setText("Max tries reached ! Try again after sometime");
                        button.setEnabled(false);
                    }
                    else {
                    editText.setText("");
                    textView.setText("Wrong pin ! Try again");}}

            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){

                    status=dataSnapshot.getValue().toString();

                }
                Log.d("Status from Firebase",status);
                textView.setText(status);

                if( status.equals("Access Denied")){
                    Log.d("Function","if() entered");

                    notification();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void notification(){

        Log.d("Status2","Notif() entered");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("n","n", NotificationManager.IMPORTANCE_HIGH);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"n").setContentTitle("Alert from NodeMCU").setSmallIcon(R.drawable.ic_warning)
                .setAutoCancel(true).setContentText("Intruder Detected !");

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(0,builder.build());
    }
}