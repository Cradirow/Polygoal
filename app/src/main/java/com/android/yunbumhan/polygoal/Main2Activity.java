package com.android.yunbumhan.polygoal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import noman.weekcalendar.WeekCalendar;
import noman.weekcalendar.listener.OnDateClickListener;

public class Main2Activity extends AppCompatActivity {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
    private String currentDate;
    private String currentPolygonNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //calendar click listener
        WeekCalendar weekCalendar = findViewById(R.id.weekCalendar);
        weekCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(DateTime dateTime) {
                //Toast.makeText(Main2Activity.this, dateTime.toString(fmt), Toast.LENGTH_SHORT).show();
                currentDate = dateTime.toString(fmt);
            }
        });
        DateTime dateTime = new DateTime();
        currentDate = dateTime.toString(fmt);

        //toolBar settings
        Toolbar myToolbar = findViewById(R.id.toolBar);
        setSupportActionBar(myToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_trending_flat_black_48dp);
        getSupportActionBar().setTitle("Polygoal");

        //database reading
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef = database.getReference("users").child(user.getUid());
        myRef.child("Polygon").child(currentDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentPolygonNumbers = dataSnapshot.getValue(String.class);
                Log.d("TAG", "read success " + currentPolygonNumbers);
                drawPolygon(currentPolygonNumbers);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.d("TAG", "read fail");
            }
        });

    }

    public void drawPolygon(String currentPolygonNumbers){
        //draw base polygon
        final Polygon polygon = findViewById(R.id.polygon);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        polygon.draw(width, height, currentPolygonNumbers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.settings:
                return true;
            case R.id.logout:
                signOut();
                return true;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        }
        else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show();
        }
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
        startActivity(intent);
    }

}
