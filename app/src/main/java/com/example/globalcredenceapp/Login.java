package com.example.globalcredenceapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Trace;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.textclassifier.TextClassifier;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.globalcredenceapp.services.MyFirebaseInstanceService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownServiceException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class Login extends AppCompatActivity {
    EditText ed_user_email, ed_user_password;

    String institute_name,android_id,first_name,last_name,institutes_profile_pic,i_profile_pic,st_user_password="",username="", password="", user_email="", user_id="", is_active="",
            user_designation="",user_designation_id,institute_id="",token,user_is_active,i_logo,i_insti_logo;
    Map<String, String> params;
    String firebase_token,status;
    String url="http://trueblueappworks.com/api/user_info/";
    String url_data="http://trueblueappworks.com/api/user_profile_info/";
    String token_url="http://trueblueappworks.com/api/token_data/";
    String designation_url="http://trueblueappworks.com/api/institute/institute_levels/";
    String institute_data="http://trueblueappworks.com/api/institute_info/";
    String base_url="http://trueblueappworks.com";
    String notice_url="http://trueblueappworks.com/api/notice/device_data/";
    private VolleySingleton AppController;
    ProgressDialog pd,mDialog;
    private Animation smalltobig, btta, btta2;
    private ImageView imageView, login_img;
    private Button login_btn;
    private LinearLayout loginlayout;
    private TextView login_signup_btn, login_forgot_password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // load this animation
        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        btta = AnimationUtils.loadAnimation(this, R.anim.btta);
        btta2 = AnimationUtils.loadAnimation(this, R.anim.btta2);

        imageView = findViewById(R.id.imageView);
        loginlayout = findViewById(R.id.login_layout);
        login_btn = findViewById(R.id.login_btn);
        login_img = findViewById(R.id.logo_img);

        ed_user_email = findViewById(R.id.user_email);  //get the id of username
        ed_user_password = findViewById(R.id.user_password); //get the id of password
        pd = new ProgressDialog(Login.this, ProgressDialog.STYLE_HORIZONTAL);

        imageView.startAnimation(smalltobig);
        loginlayout.startAnimation(btta2);
        login_img.startAnimation(btta2);
        login_btn.startAnimation(btta2);
        ed_user_email.startAnimation(smalltobig);
        ed_user_password.startAnimation(smalltobig);



        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        //Login Button Click Function
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( TextUtils.isEmpty(ed_user_email.getText())){
                    ed_user_email.requestFocus();
                    ed_user_email.setError( "Email is required!" );
                }
                else if( TextUtils.isEmpty(ed_user_password.getText())){
                    ed_user_password.requestFocus();
                    ed_user_password.setError( "Password is required!" );

                }
                else{

                    user_email =  ed_user_email.getText().toString();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getusers();
                        }
                    }, 3000);

                }

            }
        });
        login_forgot_password = findViewById(R.id.login_forgot_password);
        login_forgot_password.startAnimation(btta2);
        findViewById(R.id.login_forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
            }
        });
        login_signup_btn = findViewById(R.id.login_signup_btn);
        login_signup_btn.startAnimation(btta2);
        login_signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                firebase_token = instanceIdResult.getToken();
            }
        });
    }

    private void login() {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(LoginApi.DJANGO_SITE)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        LoginApi loginApi = retrofit.create(LoginApi.class);

        password  =  ed_user_password.getText().toString(); //get the value of password
        // method that provides the ids

        LoginData login = new LoginData(username, password);  //send data to LoginData Class

        Log.v("u",username);
        Log.v("p",password);
        Call<User> call = loginApi.login(login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    pd.show(Login.this, " ", "Please wait...");
                    if (response.body() != null) {
                        getnotice();
                        if(username.equals(username) && password.equals(password))
                        {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("username", username);
                            editor.putString("user_id", user_id);
                            editor.putString("institute_id", institute_id);
                            editor.putString("user_email", user_email);
                            editor.putString("user_designation", user_designation);
                            editor.putString("user_designation_id", user_designation_id);
                            editor.putString("token", token);
                            editor.putString("i_profile_pic", i_profile_pic);
                            editor.putString("first_name", first_name);
                            editor.putString("last_name", last_name);
                            editor.putString("institute_name", institute_name);
                            editor.putString("i_insti_logo",i_insti_logo);

                            editor.commit();

                            if (status.equals("approve")) {
//                                    pd.dismiss();

                                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                                intent.putExtra("USER_ID", user_id);
                                intent.putExtra("USER_EMAIL", user_email);
                                intent.putExtra("USERNAME", username);
                                startActivity(intent);
                            } else {

                                Intent intent = new Intent(getApplicationContext(), NewUser.class);
                                startActivity(intent);
                            }

                        }

                    }
                }else {
                    if(user_is_active.equals("False")){
                        pd.dismiss();
                        Intent intent = new Intent(getApplicationContext(), Email_Activation.class);
                        startActivity(intent);

                    }
                    else{
                        try {

                            Toast.makeText(getApplicationContext(), "Entered Email or Password Wrong", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

                    }

                }


            }


            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }
    //to get the users data


    private void getusers(){

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        StringRequest request=new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {

                    if(response!=" ") {


                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            username = jsonObject.getString("username");
                            user_id=jsonObject.getString("id");
                            user_is_active=jsonObject.getString("is_active");


                        }
                        getToken();
                    }

                    else{
                        ed_user_email.setError( "Already registered Email!" );
                        ed_user_email.requestFocus();
                        Toast.makeText(getApplicationContext(), "Entered Email is not found", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception exce) {
                    Toast.makeText(getApplicationContext(), "Entered Email is not found", Toast.LENGTH_SHORT).show();
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
                    jsonBody.put("email", user_email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String requestBody = jsonBody.toString();

                Map<String,String> params = new HashMap<String, String>();
                // Removed this line if you dont need it or Use application/json
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("email", user_email);
                return params;

            }
        };

        requestQueue.add(request);
    }



    private void getinstituteid() {
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        StringRequest request = new StringRequest(Request.Method.GET, url_data, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        first_name=jsonObject.getString("first_name");
                        last_name=jsonObject.getString("last_name");

                        status=jsonObject.getString("status").toString();
                        institute_id= jsonObject.getString("institute").toString();
                        getDesignation();
                        login();
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

    private void getToken(){
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        StringRequest request=new StringRequest(Request.Method.POST, token_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    token = jsonObject.getString("key");
                    Log.v("myyo",token);
                    getinstituteid();
                    getinstitutedata();
                } catch (Exception exce) {
                    exce.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            private static final String LOG_TAG ="" ;

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Entered Email is not found", Toast.LENGTH_SHORT).show();
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
                    jsonBody.put("user", username);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String requestBody = jsonBody.toString();

                Map<String,String> params = new HashMap<String, String>();
                // Removed this line if you dont need it or Use application/json
                params.put("user",username);
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        requestQueue.add(request);
    }




    private void getDesignation() {
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        StringRequest request = new StringRequest(Request.Method.GET, designation_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        user_designation= jsonObject.getString("level_name").toString();
                        user_designation_id=jsonObject.getString("id").toString();
                        Log.v("my des id",user_designation_id);
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



    private void getinstitutedata() {
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        StringRequest request = new StringRequest(Request.Method.GET, institute_data, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        institute_name=jsonObject.getString("name");
                        institutes_profile_pic=jsonObject.getString("profile_pic");
                        i_logo=jsonObject.getString("institute_logo");

                        i_profile_pic=base_url+institutes_profile_pic;
                        i_insti_logo=base_url+i_logo;

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



    private void getnotice(){
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        StringRequest request=new StringRequest(Request.Method.POST, notice_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        username = jsonObject.getString("username");
//                        getToken();
                    }

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
                    jsonBody.put("email", user_email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String requestBody = jsonBody.toString();

                Map<String,String> params = new HashMap<String, String>();
                // Removed this line if you dont need it or Use application/json


                params.put("user", user_id);
                Log.v("user",user_id);
                params.put("key", firebase_token);
                params.put("is_active", "true");
                params.put("Content-Type", "application/json; charset=UTF-8");
                return params;

            }
        };

        requestQueue.add(request);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            Log.v("back","back");
            pd.dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }





    private class WaitTime extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }
        protected void onPostExecute() {
            mDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            mDialog.dismiss();
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... params) {
            long delayInMillis = 3000;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mDialog.dismiss();
                }
            }, delayInMillis);
            return null;
        }
    }



}


