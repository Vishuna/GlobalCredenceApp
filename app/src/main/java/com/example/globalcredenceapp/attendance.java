package com.example.globalcredenceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.globalcredenceapp.notice.Notice;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

public class attendance extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    // Api's
    String class_url="http://trueblueappworks.com/api/class_data/";
    String des_url="http://trueblueappworks.com/api/designation_data/";
    String user_profile="http://trueblueappworks.com/api/userprofile/";
    String student_url="http://trueblueappworks.com/api/student_data/";
    String token_url="http://trueblueappworks.com/api/token_data/";
    String class_id_url="http://trueblueappworks.com/api/getClassId/";

    // start Navigation Drawer
    TabLayout tabLayout;
    DrawerLayout drawer_layout;
    NavigationView navigationView;
    ImageView menu_iv,my_profile_pic,nav_profile_pic;
    // End navigation drawer
    TextView dateformat,inst_name;
    TextView student_name,student_roll_number;
    Spinner class_spinner;
    RadioGroup radioGroup;

    RadioButton radioButton;
    Button attendance_submit_btn,view_btn,attendance_update_btn;

    DatePickerDialog.OnDateSetListener setListener;
    List<String> classesList = new ArrayList<String>();


    ArrayAdapter<String> arrayAdapter3;
    Map<String, String> params;
    ArrayList<String> student_datas = new ArrayList<String>();
    ArrayList<String> student_roll_numbers = new ArrayList<String>();
    ArrayList<String> attendence_status = new ArrayList<String>();
    Calendar calendar;
    String inst_id;
    String level_name;
    String level_id;
    String designation;
    String std_name;
    String class_id;
    String s_class;
    String login_user_name;
    String login_user_id;
    String institute_id;
    String user_designation;
    String u_name;
    String u_roll_no;
    String student_data;
    String student_class_id;
    String at_status;
    String name;
    String date;
    String selected_class;
    String std_inst_id;
    String student_class;
    String token;
    String attendance_status;
    String i_profile_pic;
    String institute_name;
    String classid;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
//        get Shared Preference Values
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        login_user_name = preferences.getString("username", null);
        institute_id = preferences.getString("institute_id", null);
        user_designation = preferences.getString("user_designation", null);
        i_profile_pic=preferences.getString("i_insti_logo",null);
        institute_name=preferences.getString("institute_name",null);
        student_name=findViewById(R.id.student_name);
        student_roll_number=findViewById(R.id.student_roll_number);
        class_spinner = (Spinner)findViewById(R.id.class_spinner);
        view_btn = findViewById(R.id.attendance_view_btn);
        inst_name=findViewById(R.id.inst_name);

        inst_name.setText(institute_name);
        my_profile_pic=findViewById(R.id.profile_pic);
        Picasso.with(attendance.this).load(i_profile_pic).into(my_profile_pic);
        getViewId();
        getToken();
        getCalendarValue();
        //to get class value
        arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,classesList);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,classesList);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        class_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                class_spinner.setSelection(position);
                if (parent.getItemAtPosition(position).equals("-- Select Class --")) {
                } else {
                    selected_class = parent.getItemAtPosition(position).toString();
                    getClassId(institute_id, selected_class);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                class_spinner_validate();
            }
        });

    }


        private void getViewId(){
            menu_iv = findViewById(R.id.menu_iv);
            navigationView = findViewById(R.id.navigationView);
            drawer_layout = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.navigationView);
            View header = navigationView.getHeaderView(0);
            nav_profile_pic = header.findViewById(R.id.nav_header_pic);
            Picasso.with(attendance.this).load(i_profile_pic).into(nav_profile_pic);
            Menu menuNav=navigationView.getMenu();
            MenuItem nav_item2 = menuNav.findItem(R.id.attendance);
            if(user_designation.equals("teacher")){
                nav_item2.setVisible(true);
            }
            else{
                nav_item2.setVisible(false);
            }
            navigationView.setNavigationItemSelectedListener(this);
            setListners();
            drawer_layout.addDrawerListener(
                    new DrawerLayout.DrawerListener() {
                        @Override
                        public void onDrawerSlide(View drawerView, float slideOffset) {
                            // Respond when the drawer's position changes
                            Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
                            drawerView.startAnimation(animFadeIn);
                        }

                        @Override
                        public void onDrawerOpened(View drawerView) {
                            // Respond when the drawer is opened
                        }

                        @Override
                        public void onDrawerClosed(View drawerView) {
                            // Respond when the drawer is closed
                        }

                        @Override
                        public void onDrawerStateChanged(int newState) {
                            // Respond when the drawer motion state changes
                        }
                    }
            );
        }
        private void setListners(){
            menu_iv.setOnClickListener(this);
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.dashboard:
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    break;
                case R.id.my_profile:
                    startActivity(new Intent(getApplicationContext(),MyProfile.class));
                    finish();
                    break;
                case R.id.profile_institute:
                    startActivity(new Intent(getApplicationContext(),InstituteProfile.class));
                    finish();

                    break;
                case R.id.attendance:
                    startActivity(new Intent(getApplicationContext(),attendance.class));
                    finish();
                    break;

                case R.id.notice:
                    startActivity(new Intent(getApplicationContext(), Notice.class));
                    finish();
                    break;
                case R.id.logout:
                    Intent logout = new Intent(getApplicationContext(), Login.class);
                    startActivity(logout);
                    finish();
            }
            return false;
        }


    @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.menu_iv:
                    drawer_layout.openDrawer(GravityCompat.START);
                    break;
            }
    }
    private void getClasses() {
        RequestQueue requestQueue = Volley.newRequestQueue(attendance.this);
        StringRequest request = new StringRequest(Request.Method.GET, class_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    classesList.add("-- Select Class --");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        name= jsonObject.getString("name").toString();
                        classesList.add(name);
                    }
                    class_spinner.setAdapter(arrayAdapter3);
                } catch (Exception exce) {
                }
            }
        }, new Response.ErrorListener() {
            private static final String LOG_TAG = "";

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                String errorMsg = "";
                if (response != null && response.data != null) {
                    String errorString = new String(response.data);
                    Log.i("log error", errorString);
                }
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                params = new HashMap<String, String>();
                params.put("Token", token);
                params.put("Content-Type", "application/json; charset=UTF-8");

                return params;
            }
        };

        requestQueue.add(request);
    }

        public void getCalendarValue () {
            final String[] fm = new String[1];
            final String[] fd = new String[1];
            Calendar calendar = Calendar.getInstance();
            dateformat = findViewById(R.id.attendance_date);
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            int mon= month + 1;
            if(mon<10){
                fm[0] ="0"+mon;
            }
            else{
                fm[0]=String.valueOf(mon);
            }
            if (day<10){
                fd[0] ="0"+day;
            }
            else{
                fd[0] =String.valueOf(day);
            }
            date = year + "-" +fm[0] + "-" +fd[0];

            dateformat.setText(date, null);
            dateformat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(attendance.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            month = month + 1;
                            if(month<10){
                                fm[0] ="0"+month;
                            }
                            else{
                                fm[0]=String.valueOf(month);
                            }
                            if (day<10){
                               fd[0] ="0"+day;
                            }
                            else{
                                fd[0] =String.valueOf(day);
                            }
                            date = year + "-" + fm[0] + "-" + fd[0];
                            Log.v("my_new date",date);
                            dateformat.setText(date);
                        }
                    }, year, month, day);
                    datePickerDialog.show();
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                }


            });
        }

//        get the all the students list from the ap
    private void getStudent(){

        RequestQueue requestQueue = Volley.newRequestQueue(attendance.this);
        StringRequest request=new StringRequest(Request.Method.POST, student_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)

            {

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        u_name= jsonObject.getString("student_name").toString();

                        u_roll_no=jsonObject.getString("roll_no").toString();
                        at_status=jsonObject.getString("attendance_status").toString();
                        student_datas.add(u_name);
                        student_roll_numbers.add(u_roll_no);
                        attendence_status.add(at_status);


                        Intent intent=new Intent(getApplicationContext(),AttendanceStudent.class);
                        pd.show(attendance.this, " ", "Please wait....");
                        intent.putExtra("student_datas",student_datas);
                        intent.putExtra("student_roll_numbers",student_roll_numbers);
                        intent.putExtra("attendence_status",attendence_status);
                        intent.putExtra("date",date);
                        Log.v("nehaaaaa", String.valueOf(classid));
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(attendance.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("Class",classid);
                        editor.commit();

                        intent.putExtra("Class",classid);
                        intent.putExtra("institute",institute_id);
                        startActivity(intent);
                    }


                } catch (Exception exce) {

                }
            }
        }, new Response.ErrorListener() {
            private static final String LOG_TAG ="" ;

            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    String message =data.get("errors").toString();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } catch (JSONException | UnsupportedEncodingException e) {
                }
            }
             })
        {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();

                final String requestBody = jsonBody.toString();
                Map<String,String> params = new HashMap<String, String>();
                // Removed this line if you don't need it or Use application/json
                Log.v("rr", String.valueOf(classid));
                Log.v("rr1",institute_id);
                Log.v("date",date);
                String s=String.valueOf(classid);
                params.put("Class",String.valueOf(classid));
                params.put("institute",institute_id);
                params.put("date",date);
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        requestQueue.add(request);
    }


    private void getToken(){

        RequestQueue requestQueue = Volley.newRequestQueue(attendance.this);
        StringRequest request=new StringRequest(Request.Method.POST, token_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    token = jsonObject.getString("key");
                    getClasses();
                } catch (Exception exce) {
                    exce.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            private static final String LOG_TAG ="" ;

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                String errorMsg = "";
                if (response != null && response.data != null) {
                    String errorString = new String(response.data);
                    Log.i("log error", errorString);
                }
            }  })
        {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("user", login_user_name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String requestBody = jsonBody.toString();
                Map<String,String> params = new HashMap<String, String>();
                params.put("user",login_user_name);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        requestQueue.add(request);
    }

    private void getClassId(final String s1, final String s2){

        RequestQueue requestQueue = Volley.newRequestQueue(attendance.this);
        StringRequest request=new StringRequest(Request.Method.POST, class_id_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)

            {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        classid= jsonObject.getString("class_id");

                    }


                } catch (Exception exce) {

                }
            }
        }, new Response.ErrorListener() {
            private static final String LOG_TAG ="" ;

            @Override
            public void onErrorResponse(VolleyError error) {


            } })
        {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();

                final String requestBody = jsonBody.toString();
                Map<String,String> params = new HashMap<String, String>();
                // Removed this line if you dont need it or Use application/json
                Log.v("kjl",selected_class);
                Log.v("kjl",institute_id);
                params.put("Class",s2);
                params.put("institute",s1);
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        requestQueue.add(request);
    }

    public void class_spinner_validate(){
        TextView errorText = (TextView)class_spinner.getSelectedView();
        if (class_spinner.getSelectedItemPosition()==0){
            class_spinner.requestFocus();
            class_spinner.setFocusable(true);
            errorText.setText("Select Class is Required!");
            Toast toast = Toast.makeText(getApplicationContext(),"Select Class is Required!", Toast.LENGTH_SHORT);
            toast.show();
            errorText.setTextColor(Color.RED);
            errorText.setError("Select Class is Required!");
        }
        else{


            getStudent();
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Do you want to log out?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                finish();
                Intent i = new Intent(getApplicationContext(),Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

//                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


}