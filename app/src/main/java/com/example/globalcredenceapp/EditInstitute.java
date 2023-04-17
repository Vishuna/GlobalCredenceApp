package com.example.globalcredenceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.text.TextUtils;
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
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditInstitute extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private final String url = "http://trueblueappworks.com/";
    String user_institute = "http://trueblueappworks.com/api/institute_info/";
    String base_url = "http://trueblueappworks.com";
    String user_states = "http://trueblueappworks.com/api/states/";
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    DrawerLayout drawer_layout;
    NavigationView navigationView;
    ImageView menu_iv, my_profile_pic, nav_profile_pic;
    EditText get_code, get_name, get_inst_profile_pic, get_institute_logo,
            get_principal, get_contact_number1, get_contact_number2, get_contact_number3,
            get_about, get_address1, get_address2, get_district, get_country, get_pin_code, get_email,
            get_facebook_link, get_website_link;
    String institute_name, institute_establish_date, institute_profile_pic, inst_logo,
            institute_principal, institute_session_start_date, institute_contact_number1, institute_contact_number2, institute_contact_number3,
            institute_address1, institute_address2, institute_district, institute_country, institute_pin_code,
            institute_email, institute_about, institute_website_link, institute_facebook_link, date, date1,
            inst_profile_file, imageToServer, token, u_name,
            user_name, user_id, institute_id, institute_code,
            code, name, establish_date, inst_profile_pic, institute_logo, principal, session_start_date,
            contact_number1, contact_number2, contact_number3,
            about, address1, address2, district, country, pin_code, email, facebook_link, website_link, user_designation, i_profile_pic,p,p1,img ,encodeImage,image_url,image_logo;
    Map<String, String> params;
    Button btn_inst_profile_pic, btn_institute_logo;
    Bitmap bitmap, bitmap1;
    Bitmap bitmaps, bitmaps1;
    private static Bitmap myBitmap;
    private static Bitmap myBitmaps;
    DatePickerDialog.OnDateSetListener setListener;
    Calendar calendar;
    TextView dateformat, dateformat1, get_session_start_date, get_establish_date, inst_name;
    final int IMG_REQUEST = 0,IMG_REQUEST1=1;

    ImageView imageView;
    Uri path,path1;
    String i_get_code,i_get_name,i_dateformat,i_get_contact_number1,i_get_inst_profile_pic,i_get_institute_logo,i_get_principal,
            i_dateformat1,i_get_contact_number2,i_get_contact_number3,i_get_address1,i_get_address2,i_get_district,
            i_get_country,i_get_pin_code,i_get_email,i_get_facebook_link,i_get_website_link,i_get_about,state_i,i_get_state,state_list,state_id;

    Spinner state_spinner;
    ArrayAdapter<String> arrayAdapter3;
    List<String> stateList = new ArrayList<String>();
    String state_ids,state_lists,state_listss,state_idss,s_id;
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
        i_profile_pic = preferences.getString("i_profile_pic", i_profile_pic);
        institute_name = preferences.getString("institute_name", null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_institute);
//        fetch ids
        get_code = (EditText) findViewById(R.id.institute_code);
        get_name = (EditText) findViewById(R.id.institute_name);
        get_establish_date = findViewById(R.id.establish_date);
        get_inst_profile_pic = findViewById(R.id.profile_pic);
        get_institute_logo =  findViewById(R.id.institute_logo);
        get_principal = (EditText) findViewById(R.id.principal);
        get_session_start_date = findViewById(R.id.session_start_date);
        get_contact_number1 = (EditText) findViewById(R.id.contact_number1);
        get_contact_number2 = (EditText) findViewById(R.id.contact_number2);
        get_contact_number3 = (EditText) findViewById(R.id.contact_number3);
        get_about = (EditText) findViewById(R.id.about);
        get_address1 = (EditText) findViewById(R.id.address1);
        get_address2 = (EditText) findViewById(R.id.address2);
        get_district = (EditText) findViewById(R.id.district);
        get_country = (EditText) findViewById(R.id.country);
        get_pin_code = (EditText) findViewById(R.id.pin_code);
        get_email = (EditText) findViewById(R.id.email);
        get_facebook_link = (EditText) findViewById(R.id.facebook_link);
        get_website_link = (EditText) findViewById(R.id.website_link);
        my_profile_pic = findViewById(R.id.edit_institute_profile_pic);
        inst_name = findViewById(R.id.inst_name);

        btn_inst_profile_pic =  findViewById(R.id.btn_inst_profile_pic);
        btn_institute_logo =  findViewById(R.id.btn_institute_logo);
        btn_inst_profile_pic.setOnClickListener(this);
        btn_institute_logo.setOnClickListener(this);
        Picasso.with(EditInstitute.this).load(i_profile_pic).into(my_profile_pic);


        state_spinner = (Spinner)findViewById(R.id.state_spinner);
        //to get states value
        arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,stateList);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,stateList);
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
        getstates();
        getViewId();
        edit_user_institutes();
        getCalendarValue();
        getCalendarValue1();

        findViewById(R.id.institute_update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_institute_validation();

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
        Picasso.with(EditInstitute.this).load(i_profile_pic).into(nav_profile_pic);
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
        switch (v.getId()) {
            case R.id.menu_iv:
                drawer_layout.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_inst_profile_pic:
                selectImage();

            case R.id.btn_institute_logo:

                selectImage1();

                break;
        }
    }



    public void edit_user_institutes(){
        RequestQueue requestQueue = Volley.newRequestQueue(EditInstitute.this);
        StringRequest request = new StringRequest(Request.Method.GET, user_institute, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.v("response",response);
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        i_get_code=jsonObject.getString("code");
                        get_code.setText(i_get_code);
                        i_get_name=jsonObject.getString("name").replace("null", "");;

                            get_name.setText(i_get_name);


                        u_name=get_name.getText().toString();
                        i_dateformat=jsonObject.getString("establish_date");
                        dateformat.setText(i_dateformat);

                        i_get_inst_profile_pic=jsonObject.getString("profile_pic");

                        get_inst_profile_pic.setText(i_get_inst_profile_pic);
                        i_get_institute_logo=jsonObject.getString("institute_logo");
                        get_institute_logo.setText(i_get_institute_logo);
                        i_get_principal=jsonObject.getString("principal");


                            get_principal.setText(i_get_principal);


                        i_dateformat1=jsonObject.getString("session_start_date");
                        dateformat1.setText(i_dateformat1);
                        i_get_contact_number1=jsonObject.getString("contact_number1").replace("null", "");;

                            get_contact_number1.setText(i_get_contact_number1);


                        i_get_contact_number2=jsonObject.getString("contact_number2").replace("null", "");;

                        get_contact_number2.setText(i_get_contact_number2);
                        i_get_contact_number3=jsonObject.getString("contact_number3").replace("null", "");;
                        get_contact_number3.setText(i_get_contact_number3);
                        i_get_address1=jsonObject.getString("address1");
                        get_address1.setText(i_get_address1);
                        i_get_address2=jsonObject.getString("address2").replace("null", "");;
                        get_address2.setText(i_get_address2);
                        i_get_district=jsonObject.getString("district");
                        get_district.setText(i_get_district);
                        i_get_country=jsonObject.getString("country");
                        get_country.setText(i_get_country);
                        i_get_state=jsonObject.getString("state");
                        i_get_pin_code=jsonObject.getString("pin_code");
                        get_pin_code.setText(i_get_pin_code);
                        i_get_email=jsonObject.getString("email").replace("null", "");;

                        get_email.setText(i_get_email);
                        i_get_facebook_link=jsonObject.getString("facebook_link").replace("null", "");

                        get_facebook_link.setText(i_get_facebook_link);
                        i_get_website_link=jsonObject.getString("website_link").replace("null", "");;

                        get_website_link.setText(i_get_website_link);

                        i_get_about=jsonObject.getString("about");
                        get_about.setText(i_get_about);
                        inst_name.setText(institute_name);
                        String profile_str=i_get_inst_profile_pic;
                        String institute_logo_str=i_get_inst_profile_pic;
                        image_url=base_url+profile_str;

                        image_logo=base_url+institute_logo_str;
                        Picasso.with(EditInstitute.this).load(image_url).into(my_profile_pic);
                        Picasso.with(EditInstitute.this).load(image_url).into((getTarget(image_url)));
                        File file = new File(new URL("image_url").toURI());

                        System.out.println(file);
                        getBitmapFromURL(image_url);


                        Picasso.with(EditInstitute.this).load(image_logo).into((getTarget(image_logo)));
                        File files = new File(new URL("image_logo").toURI());

                        System.out.println(file);
                        getBitmapFromURL(image_logo);

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
                params.put("Token", token);
                params.put("Content-Type", "application/json; charset=UTF-8");
                return params;
            }
        };

        requestQueue.add(request);
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

    public void getCalendarValue () {
        Calendar calendar = Calendar.getInstance();
        dateformat = findViewById(R.id.establish_date);

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int mon = month + 1;
        date = year + "-" + month + "-" + day;

        dateformat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditInstitute.this, new DatePickerDialog.OnDateSetListener() {
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

    public void getCalendarValue1 () {
        Calendar calendar = Calendar.getInstance();

        dateformat1 = findViewById(R.id.session_start_date);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int mon = month + 1;
        date1 = year + "-" + month + "-" + day;

        dateformat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditInstitute.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;

                        date1 = year + "-" + month + "-" + day;
                        dateformat1.setText(date1);


                    }
                }, year, month, day);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }


        });
    }


    private void selectImage() {

        Intent intent = new Intent();
        Log.v("hhh", String.valueOf(intent));
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }
    private void selectImage1() {

        Intent intent = new Intent();
        Log.v("hhh", String.valueOf(intent));
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            path = data.getData();
            p=path.getPath();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (requestCode == IMG_REQUEST1 && resultCode == RESULT_OK && data != null) {
            path1 = data.getData();
            p1=path1.getPath();

            try {
              bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), path1);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private byte[] imageToString(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if(bitmap!=null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imgBytes=byteArrayOutputStream.toByteArray();
            return imgBytes;
        }
        else{

            if(image_url!=null) {
                URL imageurl = new URL(image_url);

                bitmap = BitmapFactory.decodeStream(imageurl.openConnection().getInputStream());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imgBytes=byteArrayOutputStream.toByteArray();
                return imgBytes;
            }


        }


        return new byte[0];
    }
    private byte[] imageToString1(Bitmap bitmap1) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
        if(bitmap1!=null) {
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream1);
            byte[] imgBytes1=byteArrayOutputStream1.toByteArray();
            return imgBytes1;
        }
        if(image_logo!=null) {

            URL imageurls = new URL(image_logo);
            bitmap1 = BitmapFactory.decodeStream(imageurls.openConnection().getInputStream());
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream1);
            byte[] imgBytes1=byteArrayOutputStream1.toByteArray();
            return imgBytes1;

        }

        return new byte[0];
    }



    private void institute_profile_update(){
        code=get_code.getText().toString();
        name=get_name.getText().toString();
        establish_date=dateformat.getText().toString();
        inst_profile_file=get_inst_profile_pic.getText().toString();
        inst_logo=get_institute_logo.getText().toString();
        principal=get_principal.getText().toString();
        session_start_date=dateformat1.getText().toString();
        contact_number1=get_contact_number1.getText().toString();
        contact_number2=get_contact_number2.getText().toString();
        contact_number3=get_contact_number3.getText().toString();
        address1=get_address1.getText().toString();
        address2=get_address2.getText().toString();
        district=get_district.getText().toString();
        country=get_country.getText().toString();
        email=get_email.getText().toString();
        facebook_link=get_facebook_link.getText().toString();
        website_link=get_website_link.getText().toString();
        about=get_about.getText().toString();
        pin_code=get_pin_code.getText().toString();
        String institute_url="http://trueblueappworks.com/api/instituteprofile_update/"+institute_id;
        RequestQueue requestQueue = Volley.newRequestQueue(EditInstitute.this);
        VolleyMultipartRequest volleyMultipartRequest  = new VolleyMultipartRequest(Request.Method.PUT, institute_url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse  response) {
                Log.v("response", String.valueOf(response));
                Toast toast = Toast.makeText(getApplicationContext(),"Institute Profile Updated Successfully!", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), InstituteProfile.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
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
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();
                final String requestBody = jsonBody.toString();
                Map<String, String> params = new HashMap<String, String>();
                params.put("code",code);
                params.put("name",name);
                params.put("establish_date",establish_date);
                params.put("principal",principal);
                params.put("session_start_date",session_start_date);
                params.put("contact_number1",contact_number1);
                params.put("contact_number2",contact_number2);
                params.put("contact_number3",contact_number3);
                params.put("address1",address1);
                params.put("address2",address2);
                params.put("district",district);
                params.put("state",state_idss);
                params.put("country",country);
                params.put("pin_code",pin_code);
                params.put("email",email);
                params.put("about",about);
                params.put("facebook_link",facebook_link);
                params.put("website_link",website_link);
                params.put("created_by",user_id);
                params.put("Content-Type", "application/json");
                return params;

            }
            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() throws IOException {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                String imgName = String.valueOf(System.currentTimeMillis() + ".jpeg");
                params.put("profile_pic", new VolleyMultipartRequest.DataPart(imgName, imageToString(bitmap)));
                params.put("institute_logo", new VolleyMultipartRequest.DataPart(imgName, imageToString1(bitmap1)));
//                if (inst_profile_file.equals(i_get_inst_profile_pic)){
//                }
//                else {
//
//
//                    params.put("profile_pic", new VolleyMultipartRequest.DataPart(imgName, imageToString(bitmap)));
//                }
                return params;
            }

        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }



    public void edit_institute_validation(){


        TextView errorStateText = (TextView)state_spinner.getSelectedView();
        if(TextUtils.isEmpty(get_code.getText())){
            get_code.requestFocus();
            get_code.setError( "School Code is required!" );

        }


        else if (!TextUtils.isEmpty(get_principal.getText()) &&isStringOnlyAlphabet(get_principal.getText().toString())==false) {
            get_principal.requestFocus();
            get_principal.setError("Only characters are allowed!");

        }
        else if (!TextUtils.isEmpty(get_district.getText()) && isStringOnlyAlphabet(get_district.getText().toString())==false) {
            get_district.requestFocus();
            get_district.setError("Only characters are allowed!");

        }

        else if (!TextUtils.isEmpty(get_contact_number1.getText()) && isStringOnlyDigit(get_contact_number1.getText().toString())==false) {

            get_contact_number1.requestFocus();
            get_contact_number1.setError("Only digits are allowed!");

        }
        else if (!TextUtils.isEmpty(get_contact_number2.getText()) && isStringOnlyDigit(get_contact_number2.getText().toString())==false) {


            get_contact_number2.requestFocus();
            get_contact_number2.setError("Only digits are allowed!");

        }
        else if (!TextUtils.isEmpty(get_contact_number3.getText()) && isStringOnlyDigit(get_contact_number3.getText().toString())==false) {


            get_contact_number3.requestFocus();
            get_contact_number3.setError("Only digits are allowed!");

        }

        else if (!TextUtils.isEmpty(get_pin_code.getText()) && isStringOnlyDigit(get_pin_code.getText().toString())==false) {


            get_pin_code.requestFocus();
            get_pin_code.setError("Only digits are allowed!");

        }
        else if( TextUtils.isEmpty(get_session_start_date.getText())){
            get_session_start_date.requestFocus();
            get_session_start_date.setError( "Session Start Date is required!" );

        }
        else if( TextUtils.isEmpty(get_principal.getText())){
            get_principal.requestFocus();
            get_principal.setError( "Principal is required!" );

        }
        else if( TextUtils.isEmpty(get_address1.getText())){
            get_address1.requestFocus();
            get_address1.setError( "Address Line 1 is required!" );

        }
        else if( TextUtils.isEmpty(get_district.getText())){
            get_district.requestFocus();
            get_district.setError( "City is required!" );

        }
        else if( TextUtils.isEmpty(get_pin_code.getText())){
            get_pin_code.requestFocus();
            get_pin_code.setError( "Pin Code is required!" );

        }
        else if (state_spinner.getSelectedItemPosition()==0){
            state_spinner.requestFocus();
            state_spinner.setFocusable(true);
            errorStateText.setText("State is Required!");
            Toast toast = Toast.makeText(getApplicationContext(),"State is Required!", Toast.LENGTH_SHORT);
            toast.show();
            errorStateText.setTextColor(Color.RED);
            errorStateText.setError("State is Required!");
        }


        else{
            institute_profile_update();

        }
    }



    public void getstates() {
        RequestQueue requestQueue = Volley.newRequestQueue(EditInstitute.this);
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

    public void get_state(){
        RequestQueue requestQueue = Volley.newRequestQueue(EditInstitute.this);
        StringRequest request = new StringRequest(Request.Method.GET, user_states, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        state_ids= jsonObject.getString("id").toString();

                        if(state_ids.equals(i_get_state)){
                            state_lists = jsonObject.getString("name").toString();
//                    state_spinner.setSelection(Integer.parseInt(state_lists));
                        }
                    }
                    for (int n = 0; n < stateList.size();n++) {

                        if(stateList.get(n).equalsIgnoreCase(state_lists))
                        {
                            Log.v("state2",state_lists);
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

    public void get_state_id(){
        RequestQueue requestQueue = Volley.newRequestQueue(EditInstitute.this);
        StringRequest request = new StringRequest(Request.Method.GET, user_states, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        state_listss = jsonObject.getString("name").toString();
                        if(state_listss.equals(state_i)){
                            state_idss= jsonObject.getString("id").toString();
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
