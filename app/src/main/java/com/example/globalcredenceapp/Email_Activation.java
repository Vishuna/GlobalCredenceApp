package com.example.globalcredenceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Email_Activation extends AppCompatActivity {
    String user_email;
    String email_reactivation_url="http://trueblueappworks.com/api/send_reactivation_email/";
    Button reset_link_btn;
    EditText u_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) { SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        user_email = preferences.getString("user_email", null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email__activation);
        u_email=findViewById(R.id.reactivation_email);


        findViewById(R.id.reset_link_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_email=u_email.getText().toString();
                reactivationEmail();
            }
        });


    }



    private void reactivationEmail(){
        RequestQueue requestQueue = Volley.newRequestQueue(Email_Activation.this);
        StringRequest request=new StringRequest(Request.Method.POST, email_reactivation_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {Log.v("kkkk",response);
                try {


                    JSONObject data = new JSONObject(response);
                    String message =data.get("message").toString();
                        Log.v("kkkk",message);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);


                } catch (Exception exce) {
                    exce.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            private static final String LOG_TAG ="" ;

            @Override
            public void onErrorResponse(VolleyError error) {
                try {

                    String responseBody = new String(error.networkResponse.data, "utf-8");

                    JSONObject data = new JSONObject(responseBody);
                    String message =data.get("message").toString();

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } catch (JSONException | UnsupportedEncodingException e) {
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
                Log.v("vvv",user_email);
                params.put("email",user_email);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }
}