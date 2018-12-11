package com.android.yunbumhan.polygoal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CalendarActivity extends AppCompatActivity {

    TextView text1 = findViewById(R.id.text1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //toolbar settings
        Toolbar myToolbar = findViewById(R.id.calendarToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Polygoal");

        Intent intent = getIntent();
        int type = intent.getExtras().getInt("type");
        String date = intent.getExtras().getString("date");

        String polygonType = "";
        switch(type){
            case 1: polygonType = "Physical"; break;
            case 2: polygonType = "Work"; break;
            case 3: polygonType = "Social"; break;
            case 4: polygonType = "Play"; break;
            case 5: polygonType = "Activity"; break;
            case 6: polygonType = "Myself"; break;
        }
        readData(polygonType, date);

    }

    public void readData(String type, String date){
        //데이터를 불러와 세팅
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef = database.getReference("users").child(user.getUid());
        myRef.child("Type").child(type).child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String msg = dataSnapshot.getValue(String.class);
                if(msg != null) {
                    text1.setText(msg);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void mOnClick(View v){
        switch(v.getId()){
            case R.id.addBtn:
                //addNewMsg();
                break;
        }
    }

}
