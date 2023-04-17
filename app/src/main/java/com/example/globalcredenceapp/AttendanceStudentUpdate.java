package com.example.globalcredenceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;

public class AttendanceStudentUpdate extends AppCompatActivity {
    RecyclerView attendance_data;
    Intent intent;
    String institute_id,selected_class,u_name,student_name;
    String student_data ;

    Button attendance_submit_btn;
    TextView back_btn;
    ImageView back_arrow;

    ArrayList<String> student_datas = new ArrayList<String>();
    ArrayList<String> student_roll_numbers=new ArrayList<>();
    ArrayList<String> attendance_datas=new ArrayList<>();
    String date;
    BroadcastReceiver mMessageReceiver = null;
    RecyclerView recyclerView;
    Context context=AttendanceStudentUpdate.this;

    String text,attendance_d;
    String student_url="http://trueblueappworks.com/api/student_data/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_student);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        attendance_submit_btn=findViewById(R.id.attendance_submit_btn);
        back_btn= findViewById(R.id.attendance_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(AttendanceStudentUpdate.this, attendance.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                finish();
            }
        });
        back_arrow= findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(AttendanceStudentUpdate.this, attendance.class);
                startActivity(i);
            }
        });



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        institute_id = preferences.getString("institute_id", null);

//get the value from attendance.java
        intent = getIntent();
        student_datas=intent.getStringArrayListExtra("student_datas");
        Log.v("mera data h", String.valueOf(student_datas));
//        for(int j=0;j<student_datas.size();j++){
//            Log.v("mystudentdata", student_datas.get(j));
//        }
        student_roll_numbers=intent.getStringArrayListExtra("student_roll_numbers");
        date=intent.getStringExtra("date");
        System.out.println(student_datas);
        attendance_data=findViewById(R.id.attendance_data);
        attendance_submit_btn=findViewById(R.id.attendance_submit_btn);
        attendance_datas=intent.getStringArrayListExtra("attendance_data");
        attendance_data.setLayoutManager(new LinearLayoutManager(this));
        attendance_data.setAdapter(new AttendanceUpdateAdapter(student_datas,student_roll_numbers,attendance_submit_btn,context,date));







    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


}