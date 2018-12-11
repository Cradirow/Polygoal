package com.android.yunbumhan.polygoal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CalendarActivity extends AppCompatActivity {

    private TextView textView;
    private int day;

    private String polygonType;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        textView = findViewById(R.id.textView);

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //Toast.makeText(getApplicationContext(), ""+dayOfMonth, Toast.LENGTH_SHORT).show();
                day = dayOfMonth;
                
            }
        });

        //toolbar settings
        Toolbar myToolbar = findViewById(R.id.calendarToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Polygoal");

        if(getSupportActionBar()!=null){
            Drawable drawable= getResources().getDrawable(R.drawable.ic_trending_flat_black_48dp);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Drawable newDrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 80, 100, true));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(newDrawable);
        }

        Intent intent = getIntent();
        int type = intent.getExtras().getInt("type");
        date = intent.getExtras().getString("date");

        polygonType = "";
        switch(type){
            case 1: polygonType = "Physical"; break;
            case 2: polygonType = "Work"; break;
            case 3: polygonType = "Social"; break;
            case 4: polygonType = "Play"; break;
            case 5: polygonType = "Activity"; break;
            case 6: polygonType = "Myself"; break;
        }
        Log.d("TAG", date + ", " + polygonType);
        readData(polygonType, date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.calendar_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add:
                addItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addItem(){
        AlertDialog.Builder ad = new AlertDialog.Builder(CalendarActivity.this);
        ad.setTitle("일정 추가");
        ad.setMessage("오늘 한 일을 적어주세요");

        final EditText et = new EditText(CalendarActivity.this);
        ad.setView(et);
        //확인 버튼
        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String msg = et.getText().toString();
                textView.setText(msg);
                saveData(msg);
                dialog.dismiss();
            }
        });
        //취소 버튼
        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        ad.show();
    }

    public void saveData(String msg){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef = database.getReference("users").child(user.getUid());
        myRef.child(polygonType).child(date).setValue(msg);

    }

    public void readData(String type, String date){
        //데이터를 불러와 세팅
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef = database.getReference("users").child(user.getUid());
        myRef.child(type).child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String msg = dataSnapshot.getValue(String.class);
                    textView.setText("  " + msg);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
