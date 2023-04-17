package com.example.globalcredenceapp;
import android.app.AlertDialog;
import com.example.globalcredenceapp.Login;
import com.example.globalcredenceapp.BaseUrl;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.globalcredenceapp.ViewPagerAdapter;
import com.example.globalcredenceapp.notice.Notice;
//import com.example.globalcredenceapp.Dashboard;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.provider.Settings.Secure;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.globalcredenceapp.URLs.users;
import static android.provider.Settings.System.AIRPLANE_MODE_ON;

public class MyProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    // start Navigation Drawer
    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    ViewPagerAdapter viewPagerAdapter;
    DrawerLayout drawer_layout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ImageView menu_iv;
    TextView f_name,l_name, desig;
    // End navigation drawer



    String state_id,state_list,login_user_id,login_user_email,login_user_name,mobile_number,facebook_link,org_name,org_address,org_code,i_profile_pic,
            first_name,middle_name,last_name,father_name,mother_name,gender, date_of_birth, marital_status,category, about,qualification, profile_pic,
            aadhar_card_number, address_line_1,address_line_2,city,state,pin_code,status,institute_id,institute_name,
            institute_code,institute_address1,institute_address2,institute_email,institute_phone,institute_facebook_link,designation,image_url,
            institute_district,institute_state,institute_country,institute_pincode;
    ImageView my_profile_pic,nav_profile_pic;
    TextView tv_user_email,tv_user_city,tv_user_phone, tv_user_facebook, tv_user_institute_name, tv_user_institute_address1, tv_user_institute_address2, tv_user_institute_code,tv_org_name, tv_user_address1, tv_user_address2,tv_user_contact, tv_user_role, tv_user_qualification, tv_user_aadhar_card, tv_user_father, tv_user_mother,tv_user_dob,tv_user_marital_status,tv_user_category,tv_user_state,tv_user_pincode,tv_user_institute_city,tv_user_institute_state,tv_user_institute_country,tv_user_institute_pincode;


    String st_user_email,st_user_phone,st_user_facebook, st_user_institute_code,st_user_institute_name,st_user_institute_address,institute_profile_pic;
    String user_profile="http://trueblueappworks.com/api/user_profile_info/";
    String token_url="http://trueblueappworks.com/api/token_data/";
    String base_url="http://trueblueappworks.com";
    String institute_data="http://trueblueappworks.com/api/institute_info/";
    String user_states="http://trueblueappworks.com/api/states/";
    String token,user_designation;
    //  String login_username=getIntent().getStringExtra("USERNAME");
    Map<String, String> params;
    View header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        login_user_name = preferences.getString("username", null);
        login_user_id = preferences.getString("user_id", null);
        institute_id = preferences.getString("institute_id", null);
        login_user_email = preferences.getString("user_email", null);
        user_designation = preferences.getString("user_designation", null);
        token=preferences.getString("token",null);
        i_profile_pic=preferences.getString("i_insti_logo",null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

//        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_home);
//        navigationView.addHeaderView(hView);
//        my_profile_pic = findViewById(R.id.nav_header_pic);




        findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),edit_profile.class);
                startActivity(i);

            }
        });
        getViewId();
        getUserData();



    }


    //    start navigation
    private void getViewId(){
        menu_iv = findViewById(R.id.menu_iv);
        navigationView = findViewById(R.id.navigationView);
        drawer_layout = findViewById(R.id.drawer_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = findViewById(R.id.my_profile_tab_header);
        toggle=new ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.open,R.string.close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);

        header = navigationView.getHeaderView(0);
        nav_profile_pic = header.findViewById(R.id.nav_header_pic);
        Picasso.with(MyProfile.this).load(i_profile_pic).into(nav_profile_pic);
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


    }



    private void setListners(){
        menu_iv.setOnClickListener(this);

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
//            case R.id.library:
//                startActivity(new Intent(getApplicationContext(), IssueBook.class));
//                finish();
//                break;
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


    public void getUserData(){
        RequestQueue requestQueue = Volley.newRequestQueue(MyProfile.this);
        StringRequest request = new StringRequest(Request.Method.GET, user_profile, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("bbbb",response);

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mobile_number = jsonObject.getString("mobile_number").toString().replace("null", "");
                        tv_user_phone=findViewById(R.id.user_phone);
                        tv_user_phone.setText(mobile_number);
                        tv_user_contact=findViewById(R.id.user_contact);
                        tv_user_contact.setText(mobile_number);
                        tv_user_email=findViewById(R.id.user_email);
                        tv_user_email.setText(login_user_email);
                        
                        first_name = jsonObject.getString("first_name").toString().replace("null", "");
                        middle_name = jsonObject.getString("middle_name").toString().replace("null", "");
                        last_name = jsonObject.getString("last_name").toString().replace("null", "");
                        f_name=findViewById(R.id.fname);

                        String full_name =first_name+" "+last_name;
                        f_name.setText(full_name);
                        father_name = jsonObject.getString("father_name").toString().replace("null", "");

                        tv_user_father=findViewById(R.id.user_father);
                        tv_user_father.setText(father_name);

                        mother_name = jsonObject.getString("mother_name").toString().replace("null", "");
                        tv_user_mother=findViewById(R.id.user_mother);
                        tv_user_mother.setText(mother_name);

                        gender = jsonObject.getString("gender").toString().replace("null", "");

                        date_of_birth = jsonObject.getString("date_of_birth").toString().replace("null", "");
                        tv_user_dob=findViewById(R.id.user_dob);
                        tv_user_dob.setText(date_of_birth);

                        marital_status = jsonObject.getString("marital_status").toString().replace("null", "");
                        tv_user_marital_status=findViewById(R.id.user_marital_status);
                        tv_user_marital_status.setText(marital_status);

                        category = jsonObject.getString("category").toString().replace("null", "");
                        tv_user_category=findViewById(R.id.user_category);
                        tv_user_category.setText(category);

                        qualification = jsonObject.getString("qualification").toString().replace("null", "");
                        tv_user_qualification=findViewById(R.id.user_qualification);
                        tv_user_qualification.setText(qualification);

                        aadhar_card_number = jsonObject.getString("aadhar_card_number").toString().replace("null", "");
                        tv_user_aadhar_card=findViewById(R.id.user_aadhar_card);
                        tv_user_aadhar_card.setText(aadhar_card_number);


                        address_line_1 = jsonObject.getString("address_line_1").toString().replace("null", "");

                        tv_user_address1=findViewById(R.id.user_address1);
                        tv_user_address1.setText(address_line_1);

                        address_line_2 = jsonObject.getString("address_line_2").toString().replace("null", "");
                        tv_user_address2=findViewById(R.id.user_address2);
                        tv_user_address2.setText(address_line_2);


                        city = jsonObject.getString("city").toString().replace("null", "");
                        tv_user_city=findViewById(R.id.user_city);
                        tv_user_city.setText(city);

                        state = jsonObject.getString("state").toString().replace("null", "");
                        Log.v("state",state);
                        tv_user_state=findViewById(R.id.user_state);
                        tv_user_state.setText(state);

                        pin_code = jsonObject.getString("addr_pin").toString().replace("null", "");
                        tv_user_pincode=findViewById(R.id.user_pincode);
                        tv_user_pincode.setText(pin_code);



                        facebook_link = jsonObject.getString("facebook_link").toString().replace("null", "");
                        tv_user_facebook=findViewById(R.id.user_facebook);
                        tv_user_facebook.setText(facebook_link);
                        status=jsonObject.getString("status").toString().replace("null", "");
                        designation=jsonObject.getString("designation").toString().replace("null", "");
                        tv_user_role=findViewById(R.id.user_role);
                        tv_user_role.setText(user_designation);
                        profile_pic = jsonObject.getString("profile_pic").toString().replace("null", "");
                        my_profile_pic=findViewById(R.id.profile_pic);
                        String profile_str=profile_pic;
                        image_url=base_url+profile_str;
                        Log.v("lll",image_url);
                        Picasso.with(MyProfile.this).load(image_url).into(my_profile_pic);
                        desig=findViewById(R.id.desig);
                        desig.setText(user_designation);

//                        institute data

                        institute_name = jsonObject.getString("institute_name").toString().replace("null", "");
                        tv_user_institute_name=findViewById(R.id.user_institute_name);
                        tv_user_institute_name.setText(institute_name);
                        tv_org_name=findViewById(R.id.user_org_name);
                        tv_org_name.setText(institute_name);
                        institute_address1 =  jsonObject.getString("institute_address1").toString().replace("null", "");
                        tv_user_institute_address1=findViewById(R.id.user_institute_address1);
                        tv_user_institute_address1.setText(institute_address1);
                        institute_address2 =  jsonObject.getString("institute_address2").toString().replace("null", "");
                        tv_user_institute_address2=findViewById(R.id.user_institute_address2);
                        tv_user_institute_address2.setText(institute_address2);
                        institute_district= jsonObject.getString("institute_district").toString().replace("null", "");
                        tv_user_institute_city=findViewById(R.id.user_institute_city);
                        tv_user_institute_city.setText(institute_district);
                        institute_state=jsonObject.getString("institute_state").toString().replace("null", "");
                        tv_user_institute_state=findViewById(R.id.user_institute_state);
                        tv_user_institute_state.setText(institute_state);
                        institute_country=jsonObject.getString("institute_country").toString().replace("null", "");

                        tv_user_institute_country=findViewById(R.id.user_institute_country);
                        tv_user_institute_country.setText(institute_country);
                        institute_pincode=jsonObject.getString("institute_pin_code").toString().replace("null", "");
                        tv_user_institute_pincode=findViewById(R.id.user_institute_pincode);
                        tv_user_institute_pincode.setText(institute_pincode);
                        institute_code = jsonObject.getString("institute_code").toString().replace("null", "");
                        tv_user_institute_code=findViewById(R.id.user_institute_code);
                        tv_user_institute_code.setText(institute_code);
                        institute_email = jsonObject.getString("institute_email").toString().replace("null", "");
                        institute_phone = jsonObject.getString("institute_contact_number1").toString().replace("null", "");
                        institute_facebook_link = jsonObject.getString("institute_ " +
                                "" +
                                "" +
                                "facebook_link").toString().replace("null", "");;


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
                return params;
            }
        };
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
