
package com.example.globalcredenceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.service.autofill.FieldClassification;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class edit_profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private final String url = "http://trueblueappworks.com/";
    String user_profile = "http://trueblueappworks.com/api/user_profile_info/";
    String base_url = "http://trueblueappworks.com";
    String user_states = "http://trueblueappworks.com/api/states/";

    //Edittext defined
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    DrawerLayout drawer_layout;
    NavigationView navigationView;
    ImageView menu_iv, nav_profile_pic;
    Spinner gender_spinner, marital_status_spinner, category_spinner, state_spinner;
    Button file_chooser;
    Bitmap bitmap, bitmap1;
    private static Bitmap myBitmap;

    EditText getFirst_name, get_middle_name, get_last_name, get_father_name, get_mother_name,
            get_gender, get_date_of_birth, get_marital_status, get_category, get_about, get_qualification,
            get_aadhar_card_number, get_address_line_1, get_address_line_2, get_city, get_pin_code, get_facebook_link,
            get_profile_pic, get_mobile_number;
    TextView get_designation, dateformat, student_first_name, student_last_name, student_designation;
    Map<String, String> params;
    String mobile_number, facebook_link, s, f_name, l_name, desig,
            first_name, middle_name,full_name, last_name, father_name, mother_name, gender, date_of_birth, marital_status, category, i_profile_pic,
            about, qualification, user_name, user_id, institute_id, aadhar_card_number, address_line_1, address_line_2,
            city, state, pin_code, status, designation, image_url, profile_pic, class_promotion_status = "Promoted", fname, institute_name,
            state_id, state_name, state_list, contact_number,
            encodeImage, file, imageToServer, selected_state,
            u_middle_name, u_last_name, u_father_name, u_mother_name, u_dob, u_marital_status, u_qualification, u_aadhar_card, u_about, u_address1, date,
            u_address2, u_city, u_facebook_link, u_pin_code, u_gender, u_status, u_designation, u_profile_pic, token, user_designation,user_designation_id, p, state_i;
    ImageView my_profile_pic;
    DatePickerDialog.OnDateSetListener setListener;
    List<String> classesList = new ArrayList<String>();
    Calendar calendar;
    ArrayAdapter<String> arrayAdapter3;
    List<String> list, list1, list2;
    List<String> stateList = new ArrayList<String>();
    private final int IMG_REQUEST = 1;
    String state_ids, state_lists, state_listss, state_idss, s_id;
    Uri path;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get the values from shared preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_name = preferences.getString("username", null);
        user_id = preferences.getString("user_id", null);
        institute_id = preferences.getString("institute_id", null);
        token = preferences.getString("token", null);
        user_designation = preferences.getString("user_designation", null);
        user_designation_id = preferences.getString("user_designation_id", null);
        Log.v("my_des",user_designation);
        i_profile_pic = preferences.getString("i_profile_pic", null);
        f_name = preferences.getString("first_name", null);
        l_name = preferences.getString("last_name", null);
        institute_name = preferences.getString("institute_name", null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        findViewById(R.id.profile_update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                edit_validation();


            }
        });

        file_chooser = (Button) findViewById(R.id.file_chooser);
        file_chooser.setOnClickListener(this);


//      to get gender value
        gender_spinner = (Spinner) findViewById(R.id.gender_spinner);
        list = new ArrayList<String>();
        list.add("--Select Gender--");
        list.add("Male");
        list.add("Female");
        list.add("Other");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(arrayAdapter);
        gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender_spinner.setSelection(position);
                gender = gender_spinner.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//      to get marital status
        marital_status_spinner = (Spinner) findViewById(R.id.marital_status_spinner);
        list1 = new ArrayList<String>();
        list1.add("--Select Marital Status--");
        list1.add("Unmarried");
        list1.add("Married");
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marital_status_spinner.setAdapter(arrayAdapter1);
        marital_status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                marital_status_spinner.setSelection(position);
                marital_status = marital_status_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


//      to get category value
        category_spinner = (Spinner) findViewById(R.id.category_spinner);
        list2 = new ArrayList<String>();
        list2.add("--Select Category--");
        list2.add("Unreserved");
        list2.add("Sc/St");
        list2.add("OBC");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list2);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(arrayAdapter2);
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category_spinner.setSelection(position);
                category = category_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        state_spinner = (Spinner) findViewById(R.id.state_spinner);
        //to get states value
        arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stateList);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stateList);
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state_spinner.setSelection(position);
                state_i = state_spinner.getSelectedItem().toString();

                get_state_id();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        student_first_name = findViewById(R.id.fname);
        student_last_name = findViewById(R.id.lname);
        student_designation = findViewById(R.id.desig);



        getUserData();
        getstates();
        getViewId();
    }


    private void getViewId() {
        menu_iv = findViewById(R.id.menu_iv);
        navigationView = findViewById(R.id.navigationView);
        drawer_layout = findViewById(R.id.drawer_layout);

        Menu menuNav = navigationView.getMenu();
        MenuItem nav_item2 = menuNav.findItem(R.id.attendance);
        NavigationView navigationView = findViewById(R.id.navigationView);
        View header = navigationView.getHeaderView(0);
        nav_profile_pic = header.findViewById(R.id.nav_header_pic);
        Picasso.with(edit_profile.this).load(i_profile_pic).into(nav_profile_pic);
        if (user_designation.equals("teacher")) {
            nav_item2.setVisible(true);
        } else {
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

    private void setListners() {
        menu_iv.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dashboard:
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                break;
            case R.id.my_profile:

                startActivity(new Intent(getApplicationContext(), MyProfile.class));
                break;
            case R.id.profile_institute:
                startActivity(new Intent(getApplicationContext(), InstituteProfile.class));

                break;
            case R.id.attendance:
                startActivity(new Intent(getApplicationContext(), attendance.class));
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
        switch (v.getId()) {
            case R.id.menu_iv:
                drawer_layout.openDrawer(GravityCompat.START);
                break;
            case R.id.file_chooser:


                selectImage();

        }
    }
    public static boolean isStringOnlyAlphabet(String str)
    {
        return ((!str.equals(""))
                && (str != null)
                && (str.matches("^[a-zA-Z ]*$")));
    }
    public static boolean isStringOnlyDigit(String str1)
    {
//        ^[1-9]\d*(\.\d+)?$
        return ((!str1.equals(""))
                && (str1 != null)
                && (str1.matches("((\\d+)((\\.\\d{1,2})?))$")));
    }

    public void getUserData() {
        RequestQueue requestQueue = Volley.newRequestQueue(edit_profile.this);
        StringRequest request = new StringRequest(Request.Method.GET, user_profile, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        getFirst_name = findViewById(R.id.first_name);
                        get_middle_name = findViewById(R.id.middle_name);
                        get_last_name = findViewById(R.id.last_name);
                        get_father_name = findViewById(R.id.father_name);
                        get_mother_name = findViewById(R.id.mother_name);
                        gender_spinner = findViewById(R.id.gender_spinner);
                        marital_status_spinner = findViewById(R.id.marital_status_spinner);
                        get_qualification = (EditText) findViewById(R.id.qualification);
                        get_aadhar_card_number = (EditText) findViewById(R.id.aadhar_card_number);
                        get_about = (EditText) findViewById(R.id.about);
                        get_profile_pic = (EditText) findViewById(R.id.profile_pic);
                        get_address_line_1 = (EditText) findViewById(R.id.address_line_1);
                        get_address_line_2 = (EditText) findViewById(R.id.address_line_2);
                        get_city = (EditText) findViewById(R.id.city);
                        get_pin_code = (EditText) findViewById(R.id.pin_code);
                        get_facebook_link = (EditText) findViewById(R.id.facebook_link);
                        get_designation = findViewById(R.id.user_role);
                        get_mobile_number = findViewById(R.id.mobile_number);
                        mobile_number = jsonObject.getString("mobile_number").toString();
                        get_mobile_number.setText(mobile_number);
                        contact_number = get_mobile_number.getText().toString();
                        aadhar_card_number = jsonObject.getString("aadhar_card_number").toString().replace("null", "");
                        get_aadhar_card_number.setText(aadhar_card_number);
                        u_aadhar_card = get_aadhar_card_number.getText().toString();
                        address_line_1 = jsonObject.getString("address_line_1").toString();
                        get_address_line_1.setText(address_line_1);
                        u_address1 = get_address_line_1.getText().toString();
                        address_line_2 = jsonObject.getString("address_line_2").toString().replace("null", "");
                        get_address_line_2.setText(address_line_2);
                        u_address2 = get_address_line_2.getText().toString();
                        city = jsonObject.getString("city").toString();
                        get_city.setText(city);
                        u_city = get_city.getText().toString();
                        pin_code = jsonObject.getString("addr_pin");
                        get_pin_code.setText(pin_code);
                        u_pin_code = get_pin_code.getText().toString();
                        facebook_link = jsonObject.getString("facebook_link").replace("null", "");
                        get_facebook_link.setText(facebook_link);
                        u_facebook_link = get_facebook_link.getText().toString();
                        u_gender = jsonObject.getString("gender").toString();
                        for (int j = 0; j < list.size(); j++) {
                            if (list.get(j).equalsIgnoreCase(u_gender)) {
                                gender_spinner.setSelection(j);
                            }
                        }

                        marital_status = jsonObject.getString("marital_status").toString();
                        for (int k = 0; k < list1.size(); k++) {
                            if (list1.get(k).equalsIgnoreCase(marital_status)) {
                                marital_status_spinner.setSelection(k);
                            }
                        }
                        category = jsonObject.getString("category").toString();
                        Log.v("my_category", category);
                        for (int l = 0; l < list2.size(); l++) {
                            if (list2.get(l).equalsIgnoreCase(category)) {
                                category_spinner.setSelection(l);
                            }
                        }
                        state = jsonObject.getString("state").toString();

                        for (int n = 0; n < stateList.size(); n++) {

                            if (stateList.get(n).equalsIgnoreCase(state)) {

                                state_spinner.setSelection(n);
                            }
                        }

//
//                        user_id = jsonObject.getString("user").toString();
                        first_name = jsonObject.getString("first_name").toString();


                        getFirst_name.setText(first_name);
                        middle_name = jsonObject.getString("middle_name").toString().replace("null", "");

                        get_middle_name.setText(middle_name);
                        last_name = jsonObject.getString("last_name").toString().replace("null", "");
                        get_last_name.setText(last_name);
                        full_name = first_name+" "+last_name;
                        father_name = jsonObject.getString("father_name").toString();
                        get_father_name.setText(father_name);
                        mother_name = jsonObject.getString("mother_name").toString().replace("null", "");
                        get_mother_name.setText(mother_name);
                        getCalendarValue();
                        date_of_birth = jsonObject.getString("date_of_birth").toString();

                        dateformat.setText(date_of_birth);

                        qualification = jsonObject.getString("qualification").toString().replace("null", "");
                        get_qualification.setText(qualification);
                        profile_pic = jsonObject.getString("profile_pic").toString();

                        get_profile_pic.setText(profile_pic);
                        about = jsonObject.getString("about").toString().replace("null", "");

                        get_about.setText(about);
                        institute_id = jsonObject.getString("institute");
                        status = jsonObject.get("status").toString();
                        designation = jsonObject.getString("designation").toString();
                        student_first_name.setText(full_name);
                        student_designation.setText(designation);

                        student_designation.setText(designation);
                        my_profile_pic = findViewById(R.id.edit_profile_pic);
                        String profile_str = profile_pic;
                        image_url = base_url + profile_str;

                        Picasso.with(edit_profile.this).load(image_url).into(my_profile_pic);
                        Picasso.with(edit_profile.this).load(image_url).into((getTarget(image_url)));
                        File file = new File(new URL("image_url").toURI());
                        getBitmapFromURL(image_url);

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
                params.put("Token", token);
                params.put("Content-Type", "application/json; charset=UTF-8");
                return params;
            }
        };

        requestQueue.add(request);
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            path = data.getData();
            p = path.getPath();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private byte[] imageToString(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imgBytes = byteArrayOutputStream.toByteArray();
            return imgBytes;
        } else {


            URL imageurl = new URL(image_url);
            bitmap = BitmapFactory.decodeStream(imageurl.openConnection().getInputStream());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imgBytes = byteArrayOutputStream.toByteArray();
            return imgBytes;
        }


    }


    public void getCalendarValue() {
        Calendar calendar = Calendar.getInstance();
        dateformat = findViewById(R.id.date_of_birth);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int mon = month + 1;
        date = day + "-" + month + "-" + year;
        dateformat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(edit_profile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        date = year + "-" + month + "-" + day;
                        dateformat.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }


        });
    }

    private void profile_update() {


        fname = getFirst_name.getText().toString();
        u_middle_name = get_middle_name.getText().toString();

        u_last_name = get_last_name.getText().toString();
        u_father_name = get_father_name.getText().toString();
        u_mother_name = get_mother_name.getText().toString();
        u_about = get_about.getText().toString();
        u_profile_pic = get_profile_pic.getText().toString();
        u_qualification = get_qualification.getText().toString();
        u_dob = dateformat.getText().toString();
        u_aadhar_card = get_aadhar_card_number.getText().toString();
        String final_url = "http://trueblueappworks.com/api/userprofile_update/" + user_id;
        Log.v("fsj",final_url);
        RequestQueue requestQueue = Volley.newRequestQueue(edit_profile.this);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.PUT, final_url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                final String result = response.toString();
                try {
                    Toast toast = Toast.makeText(getApplicationContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(getApplicationContext(), MyProfile.class);
                    startActivity(intent);
                } catch (Exception e) {

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
        }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("first_name", fname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String requestBody = jsonBody.toString();
                Map<String, String> params = new HashMap<String, String>();
                // Removed this line if you dont need it or Use application/json
                params.put("user", user_id);
                params.put("institute", institute_id);
                params.put("first_name", fname);
                params.put("middle_name", u_middle_name);
                params.put("last_name", u_last_name);
                params.put("father_name", u_father_name);
                params.put("mother_name", u_mother_name);
                params.put("gender", gender);
                params.put("date_of_birth", u_dob);
                params.put("marital_status", marital_status);
                params.put("category", category);
                params.put("qualification", qualification);
                params.put("aadhar_card_number", u_aadhar_card);
                params.put("about", u_about);
                params.put("mobile_number", mobile_number);
                params.put("address_line_1", address_line_1);
                params.put("address_line_2", address_line_2);
                params.put("city", u_city);
                params.put("pin_code", u_pin_code);
                params.put("facebook_link", u_facebook_link);
                params.put("state", state_idss);
                params.put("status", status);
                params.put("designation", user_designation_id);
                params.put("class_promotion_status", class_promotion_status);
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() throws IOException {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                String imgName = String.valueOf(System.currentTimeMillis() + ".jpeg");
                params.put("profile_pic", new VolleyMultipartRequest.DataPart(imgName, imageToString(bitmap)));
                if (u_profile_pic.equals(profile_pic)) {
//                    Log.v("test", String.valueOf(myBitmap));
//
//                    params.put("profile_pic", myBitmap);
                } else {

                    params.put("profile_pic", new VolleyMultipartRequest.DataPart(imgName, imageToString(bitmap)));
                }

                return params;
            }
        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    public void getstates() {
        RequestQueue requestQueue = Volley.newRequestQueue(edit_profile.this);
        StringRequest request = new StringRequest(Request.Method.GET, user_states, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    stateList.add("-- Select State --");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        state_list = jsonObject.getString("name").toString();
                        state_id = jsonObject.getString("id").toString();
                        stateList.add(state_list);
                    }

                    state_spinner.setAdapter(arrayAdapter3);
                } catch (Exception exce) {
                }
                get_state();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);
    }

    public void get_state() {
        RequestQueue requestQueue = Volley.newRequestQueue(edit_profile.this);
        StringRequest request = new StringRequest(Request.Method.GET, user_states, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        state_ids = jsonObject.getString("id").toString();

                        if (state_ids.equals(state)) {
                            state_lists = jsonObject.getString("name").toString();
                        }


                    }
                    for (int n = 0; n < stateList.size(); n++) {

                        if (stateList.get(n).equalsIgnoreCase(state_lists)) {
                            Log.v("state2", state_lists);
                            state_spinner.setSelection(n);
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

    public void get_state_id() {
        RequestQueue requestQueue = Volley.newRequestQueue(edit_profile.this);
        StringRequest request = new StringRequest(Request.Method.GET, user_states, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        state_listss = jsonObject.getString("name").toString();


                        if (state_listss.equals(state_i)) {
                            state_idss = jsonObject.getString("id").toString();


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

    public void edit_validation() {
        TextView errorText = (TextView) gender_spinner.getSelectedView();
        TextView errorStateText = (TextView) state_spinner.getSelectedView();


        if (TextUtils.isEmpty(getFirst_name.getText())) {
            getFirst_name.requestFocus();
            getFirst_name.setError("First Name is required!");

        }
        else if (!TextUtils.isEmpty(getFirst_name.getText()) && isStringOnlyAlphabet(getFirst_name.getText().toString())==false) {

                getFirst_name.requestFocus();
                getFirst_name.setError("Only characters are allowed!");
        }
        else if (!TextUtils.isEmpty(get_middle_name.getText()) &&isStringOnlyAlphabet(get_middle_name.getText().toString())==false) {
                get_middle_name.requestFocus();
                get_middle_name.setError("Only characters are allowed!");

        }
        else if (!TextUtils.isEmpty(get_last_name.getText()) && isStringOnlyAlphabet(get_last_name.getText().toString())==false) {
             get_last_name.requestFocus();
            get_last_name.setError("Only characters are allowed!");

        }
        else if (!TextUtils.isEmpty(get_father_name.getText()) && isStringOnlyAlphabet(get_father_name.getText().toString())==false) {


            get_father_name.requestFocus();
            get_father_name.setError("Only characters are allowed!");

        }
        else if (!TextUtils.isEmpty(get_mother_name.getText()) && isStringOnlyAlphabet(get_mother_name.getText().toString())==false) {


            get_mother_name.requestFocus();
            get_mother_name.setError("Only characters are allowed!");

        }
        else if (!TextUtils.isEmpty(get_city.getText()) && isStringOnlyAlphabet(get_city.getText().toString())==false) {


            get_city.requestFocus();
            get_city.setError("Only characters are allowed!");

        }
        else if (!TextUtils.isEmpty(get_mobile_number.getText()) && isStringOnlyDigit(get_mobile_number.getText().toString())==false) {
            Boolean b= isStringOnlyDigit(get_mobile_number.getText().toString());
            Log.v("trd", String.valueOf(b));

            get_mobile_number.requestFocus();
            get_mobile_number.setError("Only digits are allowed!");

        }
        else if (!TextUtils.isEmpty(get_aadhar_card_number.getText()) && isStringOnlyDigit(get_aadhar_card_number.getText().toString())==false) {


            get_aadhar_card_number.requestFocus();
            get_aadhar_card_number.setError("Only digits are allowed!");

        }
        else if (!TextUtils.isEmpty(get_pin_code.getText()) && isStringOnlyDigit(get_pin_code.getText().toString())==false) {


            get_pin_code.requestFocus();
            get_pin_code.setError("Only digits are allowed!");

        }
        else if (TextUtils.isEmpty(get_address_line_1.getText())) {
            get_address_line_1.requestFocus();
            get_address_line_1.setError("Address Line 1 is required!");

        } else if (TextUtils.isEmpty(get_city.getText())) {
            get_city.requestFocus();
            get_city.setError("City is required!");

        } else if (TextUtils.isEmpty(get_father_name.getText())) {
            get_father_name.requestFocus();
            get_father_name.setError("Father's Name is required!");

        } else if (TextUtils.isEmpty(get_pin_code.getText())) {
            get_pin_code.requestFocus();
            get_pin_code.setError("Pin Code is required!");

        } else if (gender_spinner.getSelectedItemPosition() == 0) {
            gender_spinner.requestFocus();
            gender_spinner.setFocusable(true);
            errorText.setText("Gender is Required!");
            Toast toast = Toast.makeText(getApplicationContext(), "Gender is Required!", Toast.LENGTH_SHORT);
            toast.show();
            errorText.setTextColor(Color.RED);
            errorText.setError("Gender is Required!");
        } else if (state_spinner.getSelectedItemPosition() == 0) {
            state_spinner.requestFocus();
            state_spinner.setFocusable(true);
            errorStateText.setText("State is Required!");
            Toast toast = Toast.makeText(getApplicationContext(), "State is Required!", Toast.LENGTH_SHORT);
            toast.show();
            errorStateText.setTextColor(Color.RED);
            errorStateText.setError("State is Required!");

        }
        else {
            profile_update();
        }



    }


    private static Target getTarget(final String url){
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + url);

                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }


}
