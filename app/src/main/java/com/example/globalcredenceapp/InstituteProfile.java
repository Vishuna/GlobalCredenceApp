package com.example.globalcredenceapp;

import com.example.globalcredenceapp.BaseUrl;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.globalcredenceapp.notice.Notice;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InstituteProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    String institute_code,institute_name,institute_establish_date,institute_profile_pic,institute_logo,institute_principal,
    institute_session_start_date,institute_contact_number1,institute_contact_number2, institute_contact_number3,institute_address1,institute_address2,
    institute_district,institute_country,institute_pin_code,institute_email,institute_about,institute_website_link, token,institute_facebook_link="",user_designation,i_profile_pic,
            institutes_profile_pic,institute_phone,institute_state,state_id,state_list,image_url,institute_image_logo;
    TextView tv_institute_code,tv_institute_name,tv_institute_establish_date,tv_institute_session_start_date, tv_institute_profile_pic,
            tv_institute_logo, tv_institute_principal, tv_institute_about, tv_institute_contact1,tv_institute_address_line1,tv_institute_address_line2,
            tv_institute_city, tv_institute_state, tv_institute_country, tv_institute_pincode, tv_institute_email,tv_institute_website_link,tv_institute_facebook_link;

    TextView  d_org_name,d_org_establish_date,d_org_address1,d_org_address2,d_org_city,d_org_state,d_org_pincode,
            d_org_contact,d_org_session_start_date,d_org_principal,d_org_website;
    String username="";
    String user_id="";
    String institute_id="", android_id;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewInstituteAdapter viewPagerAdapter;
    DrawerLayout drawer_layout;
    NavigationView navigationView;
    ImageView institute_menu,my_profile_pic,nav_profile_pic;
    Map<String, String> params;
    TextView insti_name;
    ImageView edit_btn;
    String institute_data="http://trueblueappworks.com/api/institute_info/";
    String user_states="http://trueblueappworks.com/api/states/";
    String base_url="http://trueblueappworks.com";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = preferences.getString("username", null);
        user_id = preferences.getString("user_id", null);
        institute_id = preferences.getString("institute_id", null);
        user_designation = preferences.getString("user_designation", null);
        token=preferences.getString("token",null);
//        i_profile_pic=preferences.getString("i_profile_pic",null);
        institute_name=preferences.getString("institute_name",null);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_profile);
        my_profile_pic=findViewById(R.id.inst_profile_pic);



        edit_btn=findViewById(R.id.edit_institute_button);
        if(user_designation.equalsIgnoreCase("admin")){
            edit_btn.setVisibility(View.VISIBLE);
        }
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),EditInstitute.class);
                startActivity(i);
            }
        });
        getViewId();
        edit_institute();
        getstates();


    }

    private void getViewId(){
        institute_menu = findViewById(R.id.institute_menu);
        navigationView = findViewById(R.id.navigationView);
        drawer_layout = findViewById(R.id.drawer_layout);

        viewPager = (ViewPager) findViewById(R.id.institute_pager);
        viewPagerAdapter = new ViewInstituteAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.institute_profile_tab_header);
        tabLayout.setupWithViewPager(viewPager);
        NavigationView navigationView = findViewById(R.id.navigationView);
        View header = navigationView.getHeaderView(0);
        nav_profile_pic = header.findViewById(R.id.nav_header_pic);

        Menu menuNav=navigationView.getMenu();
        MenuItem nav_item2 = menuNav.findItem(R.id.attendance);
        if(user_designation.equals("teacher")){
            nav_item2.setVisible(true);
        }
        else{
            nav_item2.setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(this);
        setListeners();
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

    private void setListeners(){
        institute_menu.setOnClickListener(this);
    }

    @Override
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
            case R.id.institute_menu:
                drawer_layout.openDrawer(GravityCompat.START);
                break;
        }

    }



    private void edit_institute() {
        RequestQueue requestQueue = Volley.newRequestQueue(InstituteProfile.this);
        StringRequest request = new StringRequest(Request.Method.GET, institute_data, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        get values form json

                        institute_name = jsonObject.getString("name").toString();

                        institute_establish_date=jsonObject.getString("establish_date").toString().replace("null", "");
                        institute_code = jsonObject.getString("code").toString().replace("null", "");
                        institute_principal=jsonObject.getString("principal").toString().replace("null", "");
                        institute_session_start_date=jsonObject.getString("session_start_date").toString().replace("null", "");
                        institute_contact_number1=jsonObject.getString("contact_number1").toString().replace("null", "");
                        institute_contact_number2=jsonObject.getString("contact_number2").toString().replace("null", "");
                        institute_contact_number3 = jsonObject.getString("contact_number3").toString().replace("null", "");
                        institute_address1 =  jsonObject.getString("address1").toString().replace("null", "");
                        institute_address2 =  jsonObject.getString("address2").toString().replace("null", "");
                        institute_district = jsonObject.getString("district").toString().replace("null", "");
                        institute_country=jsonObject.getString("country").toString().replace("null", "");
                        institute_pin_code=jsonObject.getString("pin_code").toString().replace("null", "");
                        institute_email = jsonObject.getString("email").toString().replace("null", "");
                        institutes_profile_pic=jsonObject.getString("profile_pic").toString().replace("null", "");
                        institute_image_logo=jsonObject.getString("institute_logo").toString().replace("null", "");
                        institute_about=jsonObject.getString("about").toString().replace("null", "");
                        institute_facebook_link = jsonObject.getString("facebook_link").toString().replace("null", "");
                        institute_website_link=jsonObject.getString("website_link").toString().replace("null", "");
                        institute_state=jsonObject.getString("state").toString().replace("null", "");
                        String profile_str=institutes_profile_pic;

                        image_url=base_url+profile_str;
                        String i_ilogo=base_url+institute_image_logo;
                        Picasso.with(InstituteProfile.this).load(image_url).into(my_profile_pic);
                        Picasso.with(InstituteProfile.this).load(i_ilogo).into(nav_profile_pic);
                        tv_institute_email =  findViewById(R.id.institute_email);
                        tv_institute_email.setText(institute_email);
                        tv_institute_contact1 = findViewById(R.id.institute_phone);
                        tv_institute_contact1.setText(institute_contact_number1);
                        tv_institute_facebook_link =  findViewById(R.id.institute_facebook_link);
                        tv_institute_facebook_link.setText(institute_facebook_link);

//                        About Section
                        //get ids from UI
                        insti_name=findViewById(R.id.inst_name);
                        insti_name.setText(institute_name);
                        tv_institute_name = findViewById(R.id.institute_name);
                        tv_institute_address_line1 = findViewById(R.id.inst_address1);
                        tv_institute_address_line2 = findViewById(R.id.inst_address2);
                        tv_institute_city=findViewById(R.id.inst_city);
                        tv_institute_state=findViewById(R.id.inst_state);
                        tv_institute_country=findViewById(R.id.country);
                        tv_institute_pincode=findViewById(R.id.inst_pincode);
                        tv_institute_code = findViewById(R.id.institute_code);





//                       Details Section

                        d_org_name=findViewById(R.id.org_name);
                        d_org_name.setText(institute_name);
                        d_org_establish_date=findViewById(R.id.org_establish_date);
                        d_org_establish_date.setText(institute_establish_date);
                        d_org_address1=findViewById(R.id.org_address1);
                        d_org_address1.setText(institute_address1);
                        d_org_address2=findViewById(R.id.org_address2);
                        d_org_address2.setText(institute_address2);
                        d_org_city=findViewById(R.id.org_city);
                        d_org_city.setText(institute_district);
                        d_org_state=findViewById(R.id.org_state);
                        d_org_pincode=findViewById(R.id.org_pincode);
                        d_org_pincode.setText(institute_pin_code);
                        d_org_contact=findViewById(R.id.org_contact);
                        d_org_contact.setText(institute_contact_number1);
                        d_org_session_start_date=findViewById(R.id.org_session_start_date);
                        d_org_session_start_date.setText(institute_session_start_date);
                        d_org_principal=findViewById(R.id.org_principal);
                        d_org_principal.setText(institute_principal);
                        d_org_website=findViewById(R.id.org_website);
                        d_org_website.setText(institute_website_link);


//                        set the values to textviews
                        tv_institute_name.setText(institute_name);
                        tv_institute_code.setText(institute_code);
                        tv_institute_address_line1.setText(institute_address1);
                        tv_institute_address_line2.setText(institute_address2);
                        tv_institute_city.setText(institute_district);


                        tv_institute_country.setText(institute_country);
                        tv_institute_pincode.setText(institute_pin_code);


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
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Token", token);
                System.out.print(token);
                return params;
            }
        };

        requestQueue.add(request);
    }





    public void getstates() {
        RequestQueue requestQueue = Volley.newRequestQueue(InstituteProfile.this);
        StringRequest request = new StringRequest(Request.Method.GET, user_states, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
//                    stateList.add("-- Select State --");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        state_id = jsonObject.getString("id").toString();


                        if(state_id.equals(institute_state)){
                            state_list = jsonObject.getString("name").toString();
                            d_org_state.setText(state_list);
                            tv_institute_state.setText(state_list);


                        }
                    }

                } catch (Exception exce) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);
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
