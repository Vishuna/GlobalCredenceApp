package com.example.globalcredenceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {

    EditText ed_forgot_email;
    String user_email="";
    String juseremail="";

    final  String url="http://trueblueappworks.com/users_login_data/";
    String base="http://trueblueappworks.com/";

ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ed_forgot_email = findViewById(R.id.forgot_email);

        findViewById(R.id.reset_link_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( TextUtils.isEmpty(ed_forgot_email.getText())){
                    ed_forgot_email.setError( "Email is required!" );
                }
                else {

                    user_email = ed_forgot_email.getText().toString();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getEmail();
                        }
                    }, 3000);
                    getEmail();
                }
            }
        });



    }

    private void getEmail() {

        String users_api=base+"api/request-reset-email/";

        RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword.this);
        StringRequest request=new StringRequest(Request.Method.POST, users_api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.show(ForgotPassword.this, " ", "Please wait...");
                        Intent intent = new Intent(getApplicationContext(), ForgotPasswordMessage.class);
                        startActivity(intent);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ed_forgot_email.setError( "No Such Email Found!" );
                ed_forgot_email.requestFocus();
                Log.v("test","test");
//                Toast.makeText(ForgotPassword.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }

        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("email", "user_email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String requestBody = jsonBody.toString();
                Map<String, String> params = new HashMap<String, String>();
                Log.v("ttttt",user_email);
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("email",user_email );


                return params;

            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                // Removed this line if you dont need it or Use application/json
                // params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

//    private void emailVerification() {
//        RequestQueue requestQueue= Volley.newRequestQueue(ForgotPassword.this);
//
//        StringRequest request=new StringRequest(Request.Method.GET,url, new com.android.volley.Response.Listener<String>(){
//
//            @Override
//            public void onResponse(String response) {
//
//
//                try{
//                    JSONArray jsonArray=new JSONArray(response);
//                    for(int i=0;i<jsonArray.length();i++){
//                        JSONObject jsonObject=jsonArray.getJSONObject(i);
//                        user_email =  ed_forgot_email.getText().toString();
//                        juseremail =jsonObject.getString("email").toString();
//
//
//                        if( TextUtils.isEmpty(ed_forgot_email.getText())){
//                            ed_forgot_email.setError( "Email is required!" );
//                            ed_forgot_email.requestFocus();
//
//
//
//                        }
//
//                        else if(juseremail.equals(ed_forgot_email.getText().toString())) {
//                            getEmail();
//
//                        }
//                        else {
//                            ed_forgot_email.setError( "No Such Email Found!" );
//                            ed_forgot_email.requestFocus();
//
//                        }
//
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
//            }
//        });
//        requestQueue.add(request);
//    }


}
