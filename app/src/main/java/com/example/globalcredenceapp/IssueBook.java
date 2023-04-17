package com.example.globalcredenceapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.globalcredenceapp.notice.Notice;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IssueBook extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    LinearLayout l_issue_table;
    Map<String, String> params;
    String token,user_designation;
    String issued_book_data = "http://trueblueappworks.com/api/library/issue_books/";
    DrawerLayout drawer_layout;
    NavigationView navigationView;

    ImageView menu_iv,my_profile_pic,nav_profile_pic,issue_book_upward;
    TextView issue_book_btn;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_book);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", null);
        user_designation = preferences.getString("user_designation", null);
        l_issue_table=findViewById(R.id.linear_issue_table);
        issue_book_btn=findViewById(R.id.issue_book_btn);
//        issue_book_upward=findViewById(R.id.issue_book_upward);
        issue_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(IssueBook.this, Library_User.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();
            }
        });

        getViewId();
        getIssuedBook();
    }


    private void getViewId(){
        menu_iv = findViewById(R.id.menu_iv);
        navigationView = findViewById(R.id.navigationView);
        drawer_layout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        View header = navigationView.getHeaderView(0);
        nav_profile_pic = header.findViewById(R.id.nav_header_pic);
//        Picasso.with(attendance.this).load(i_profile_pic).into(nav_profile_pic);
        Menu menuNav=navigationView.getMenu();
        MenuItem nav_item2 = menuNav.findItem(R.id.library);
//        if(user_designation.equals("admin") ||user_designation.equals("librarian") ){
//            nav_item2.setVisible(true);
//        }
//        else{
//            nav_item2.setVisible(false);
//        }
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
            case R.id.library:
                startActivity(new Intent(getApplicationContext(), IssueBook.class));
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

    private void getIssuedBook() {
            final TableLayout stk = (TableLayout) findViewById(R.id.issue_book_table);
            TableRow tbrow0 = new TableRow(this);
            tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
            TextView tv0 = new TextView(this);
            tv0.setText("S.No.");
            tv0.setPadding(20, 5, 20, 5);
            tv0.setTextColor(getResources().getColor(R.color.table_head));
            // tv0.setGravity(Gravity.CENTER);
            tbrow0.addView(tv0);
            TextView tv1 = new TextView(this);
            tv1.setText("Book Code");
            tv1.setPadding(20, 5, 20, 5);
            tv1.setTextColor(getResources().getColor(R.color.table_head));
            //tv1.setGravity(Gravity.CENTER_HORIZONTAL);
            tbrow0.addView(tv1);
            TextView tv2 = new TextView(this);
            tv2.setText("Book ID");
            tv2.setPadding(20, 5, 20, 5);
            tv2.setTextColor(getResources().getColor(R.color.table_head));
            //tv2.setGravity(Gravity.CENTER_HORIZONTAL);
            tbrow0.addView(tv2);
            TextView tv3 = new TextView(this);
            tv3.setText("Book Name");
            tv3.setPadding(20, 5, 20, 5);
            tv3.setTextColor(getResources().getColor(R.color.table_head));
            //tv2.setGravity(Gravity.CENTER_HORIZONTAL);
            tbrow0.addView(tv3);
            TextView tv4 = new TextView(this);
            tv4.setText("Issued To");
            tv4.setPadding(20, 5, 20, 5);
            tv4.setTextColor(getResources().getColor(R.color.table_head));
            //tv2.setGravity(Gravity.CENTER_HORIZONTAL);
            tbrow0.addView(tv4);
            TextView tv5 = new TextView(this);
            tv5.setText("Designation");
            tv5.setPadding(20, 5, 20, 5);
            tv5.setTextColor(getResources().getColor(R.color.table_head));
            //tv2.setGravity(Gravity.CENTER_HORIZONTAL);
            tbrow0.addView(tv5);
            TextView tv6 = new TextView(this);
            tv6.setText("Issue Date");
            tv6.setPadding(20, 5, 20, 5);
            tv6.setTextColor(getResources().getColor(R.color.table_head));
            //tv2.setGravity(Gravity.CENTER_HORIZONTAL);
            tbrow0.addView(tv6);
            stk.addView(tbrow0);
//        API Call
            RequestQueue requestQueue = Volley.newRequestQueue(IssueBook.this);
            StringRequest request = new StringRequest(Request.Method.GET, issued_book_data, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String book_name = jsonObject.getString("book_name").toString();
                            String book_code = jsonObject.getString("book_code").toString();
                            String book_id = jsonObject.getString("book_id").toString();
                            String issue_to = jsonObject.getString("issue_to").toString();
                            String designation = jsonObject.getString("designation").toString();
                            String issue_date = jsonObject.getString("issue_date").toString();
                            int sr = i+1;


// Table Data Intilization
                            TableRow tbrow = new TableRow(IssueBook.this);
                            tbrow.setGravity(Gravity.CENTER_HORIZONTAL);
                            TextView t0v = new TextView(IssueBook.this);
                            t0v.setText(String.valueOf(sr));
                            t0v.setTextColor(Color.BLACK);
                            t0v.setPadding(20, 5, 20, 5);
                            //t0v.setGravity(Gravity.CENTER);
                            tbrow.addView(t0v);
                            TextView t1v = new TextView(IssueBook.this);
                            t1v.setText(book_code);
                            t1v.setTextColor(Color.BLACK);
                            t1v.setPadding(20, 5, 20, 5);
                            // t1v.setGravity(Gravity.CENTER);
                            tbrow.addView(t1v);
                            TextView t2v = new TextView(IssueBook.this);
                            t2v.setText(book_id);
                            t2v.setTextColor(Color.BLACK);
                            t2v.setPadding(20, 5, 20, 5);
                            //t2v.setGravity(Gravity.CENTER);
                            tbrow.addView(t2v);
                            TextView t3v = new TextView(IssueBook.this);
                            t3v.setText(book_name);
                            t3v.setTextColor(Color.BLACK);
                            t3v.setPadding(20, 5, 20, 5);
                            //t2v.setGravity(Gravity.CENTER);
                            tbrow.addView(t3v);
                            TextView t4v = new TextView(IssueBook.this);
                            t4v.setText(issue_to);
                            t4v.setTextColor(Color.BLACK);
                            t4v.setPadding(20, 5, 20, 5);
                            //t2v.setGravity(Gravity.CENTER);
                            tbrow.addView(t4v);
                            TextView t5v = new TextView(IssueBook.this);
                            t5v.setText(designation);
                            t5v.setTextColor(Color.BLACK);
                            t5v.setPadding(20, 5, 20, 5);
                            //t2v.setGravity(Gravity.CENTER);
                            tbrow.addView(t5v);
                            TextView t6v = new TextView(IssueBook.this);
                            t6v.setText(issue_date);
                            t6v.setTextColor(Color.BLACK);
                            t6v.setPadding(20, 5, 20, 5);
                            //t2v.setGravity(Gravity.CENTER);
                            tbrow.addView(t6v);
                            stk.addView(tbrow);
                        }
                    } catch (Exception exce) {
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
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
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    params.put("Token", token);

                    return params;
                }
            };

            requestQueue.add(request);


        }


}