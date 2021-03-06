package com.android.yunbumhan.polygoal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import noman.weekcalendar.WeekCalendar;
import noman.weekcalendar.listener.OnDateClickListener;

public class Main2Activity extends AppCompatActivity {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    private Button editBtn;
    private TextView titleView;

    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
    //오늘 날짜와 polygon을 저장
    private String today;
    private String todayPolygon;
    //주간달력에서 선택한 날짜를 저장
    private String currentDate;
    private String currentPolygonNumbers;
    private String recentPolygon;

    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //title edit alert dialog settings
        editBtn = findViewById(R.id.titleEditBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(Main2Activity.this);
                ad.setTitle("목표 설정");
                ad.setMessage("이루고 싶은 것이 있나요?");

                final EditText et = new EditText(Main2Activity.this);
                ad.setView(et);
                //확인 버튼
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = et.getText().toString();

                        dialog.dismiss();
                        saveTitle(title);
                    }
                });
                //취소 버튼
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        editBtn.setVisibility(View.INVISIBLE);
                        titleView.setVisibility(View.VISIBLE);
                    }
                });

                ad.show();
            }
        });

        //titleView Listener
        titleView = findViewById(R.id.titleView);
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBtn.setVisibility(View.VISIBLE);
                titleView.setVisibility(View.INVISIBLE);
            }
        });

        //calendar click listener
        WeekCalendar weekCalendar = findViewById(R.id.weekCalendar);
        weekCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(DateTime dateTime) {
                currentDate = dateTime.toString(fmt);
                calculatePolygon();
            }
        });
        DateTime dateTime = new DateTime();
        currentDate = dateTime.toString(fmt);

        today = dateTime.toString(fmt);

        //toolBar settings
        Toolbar myToolbar = findViewById(R.id.toolBar);
        setSupportActionBar(myToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_trending_flat_black_48dp);
        getSupportActionBar().setTitle("Polygoal");

        //database polygon reading
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        myRef = database.getReference("users").child(user.getUid());
        myRef.child("Recent").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentPolygonNumbers = dataSnapshot.getValue(String.class);
                Log.d("TAG", "read success " + currentPolygonNumbers);
                if(currentPolygonNumbers != null) drawPolygon(currentPolygonNumbers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //database title reading
        myRef.child("Title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);
                if(title != "."){
                    titleView.setText(title);
                    titleView.setVisibility(View.VISIBLE);
                    editBtn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //get recent polygon
        myRef.child("Recent").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recentPolygon = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //오늘 폴리곤 정보가 없으면 최근 데이터 저장, 데이터 있으면 오늘 폴리곤 데이터에 저장
        myRef.child("Polygon").child(today).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    myRef.child("Polygon").child(currentDate).setValue(recentPolygon);
                }else{
                    todayPolygon = dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void mOnClick(View v){
        int type = 0;
        switch(v.getId()){
            case R.id.btnPolygonPhysical: type = 1;
                break;
            case R.id.btnPolygonWork: type = 2;
                break;
            case R.id.btnPolygonSocial: type = 3;
                break;
            case R.id.btnPolygonPlay: type = 4;
                break;
            case R.id.btnPolygonActivity: type = 5;
                break;
            case R.id.btnPolygonMyself: type = 6;
                break;
        }
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        intent.putExtra("type", type);
//        intent.putExtra("date", currentDate);
        intent.putExtra("today", today);
        intent.putExtra("polygon", todayPolygon);
        startActivity(intent);
    }

    //목표 저장
    public void saveTitle(String title){
        Log.d("TAG", "saving title...");

        if(title.length() == 0){
            //no title.
        }else{
            myRef.child("Title").setValue(title);
            Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    //주간달력 클릭했을 때 polygon 데이터가 있으면 읽어온다.
    public void calculatePolygon(){

        myRef.child("Polygon").child(currentDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    currentPolygonNumbers = dataSnapshot.getValue(String.class);
                    Log.d("TAG", "read success " + currentPolygonNumbers);
                    drawPolygon(currentPolygonNumbers);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //선택한 날짜에 맞는 도형 그리기
    public void drawPolygon(String currentPolygonNumbers){
        //draw base polygon
        final Polygon polygon = findViewById(R.id.polygon);
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        float width = dm.widthPixels;
        float height = dm.heightPixels;
        //Log.d("TAG",width + ", " + height);
        polygon.draw(width, height, currentPolygonNumbers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    //툴바 설정버튼 클릭시
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.settings:
                settings();
                return true;
            case R.id.logout:
                signOut();
                return true;
        }
        return true;
    }

    //뒤로가기 어플 종료 기능
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

    //세팅 클릭시
    public void settings(){
        //call intent
    }

    //로그아웃 클릭시
    public void signOut(){
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
        startActivity(intent);
    }

}
