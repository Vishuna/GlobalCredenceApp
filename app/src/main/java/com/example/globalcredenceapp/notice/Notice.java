package com.example.globalcredenceapp.notice;
import com.example.globalcredenceapp.Dashboard;
import com.example.globalcredenceapp.Login;
import com.example.globalcredenceapp.MyProfile;
import com.example.globalcredenceapp.ViewPagerAdapter;
import com.example.globalcredenceapp.attendance;
import com.example.globalcredenceapp.services.*;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.globalcredenceapp.InstituteProfile;
import com.example.globalcredenceapp.R;
//import com.example.globalcredenceapp.attendance;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.navigation.NavigationView;
//import com.google.android.material.tabs.TabLayout;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class Notice extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    // start Navigation Drawer
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    DrawerLayout drawer_layout;
    NavigationView navigationView;
    ImageView menu_iv,nav_profile_pic;
    TextView inst_name;
//    End navigation drawer

    private RecyclerView recyclerView;
    private List<NoticeModelClass> noticeModelClass = new ArrayList<>();
    String loggedin_token,title,date,body,i_profile_pic,token_data;
    ImageView my_profile_pic;
    String notice_url = "http://trueblueappworks.com/api/all_notice/";
    String user_designation,institute_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_designation = preferences.getString("user_designation", null);
        loggedin_token = preferences.getString("token", null);
        i_profile_pic=preferences.getString("i_insti_logo",null);
        institute_name=preferences.getString("institute_name",null);


//        MyFirebaseInstanceService.getInstance().getToken();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        inst_name=findViewById(R.id.inst_name);
        inst_name.setText(institute_name);

        my_profile_pic=findViewById(R.id.profile_pic);
        Picasso.with(Notice.this).load(i_profile_pic).into(my_profile_pic);
        recyclerView = findViewById(R.id.recycler_view);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        getViewId();
        getNotice();


    }



    private void getNotice() {
        RequestQueue requestQueue = Volley.newRequestQueue(Notice.this);
        StringRequest request = new StringRequest(Request.Method.GET, notice_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        title= jsonObject.getString("subject").toString();
                        date= jsonObject.getString("publish_date").toString();
                        body= jsonObject.getString("content").toString();
                        noticeModelClass.add(new NoticeModelClass(title,date,body));
                    }
                    RecyclerView.Adapter adapter = new NoticeAdapter(noticeModelClass);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Token", loggedin_token);
                return params;
            }
        };

        requestQueue.add(request);
    }
//    start navigation
    private void getViewId(){
        menu_iv = findViewById(R.id.menu_iv);
        navigationView = findViewById(R.id.navigationView);
        drawer_layout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.navigationView);
        View header = navigationView.getHeaderView(0);
        nav_profile_pic = header.findViewById(R.id.nav_header_pic);
        Picasso.with(Notice.this).load(i_profile_pic).into(nav_profile_pic);
        Menu menuNav=navigationView.getMenu();
        MenuItem nav_item2 = menuNav.findItem(R.id.attendance);
        if(user_designation.equals("teacher")){
            nav_item2.setVisible(true);
        }
        else{
            nav_item2.setVisible(false);
        }



        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
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

    private void setListners() {
        menu_iv.setOnClickListener(this);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dashboard:
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                break;
            case R.id.my_profile:

                startActivity(new Intent(getApplicationContext(),MyProfile.class));
                break;
            case R.id.profile_institute:
                startActivity(new Intent(getApplicationContext(),InstituteProfile.class));

                break;
            case R.id.attendance:
                startActivity(new Intent(getApplicationContext(),attendance.class));
                break;

            case R.id.notice:
                startActivity(new Intent(getApplicationContext(), Notice.class));
                break;
            case R.id.logout:
                Intent logout = new Intent(getApplicationContext(), Login.class);
                startActivity(logout);
                finish();
        }
        return false;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu_iv:
                drawer_layout.openDrawer(GravityCompat.START);
                break;
        }

    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

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
