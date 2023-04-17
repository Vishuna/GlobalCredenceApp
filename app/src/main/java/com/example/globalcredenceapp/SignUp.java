package com.example.globalcredenceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class SignUp extends AppCompatActivity {
    EditText ed_user_name, ed_user_email, ed_user_password, ed_user_confirm_password;
    Button register_btn;
    String user_name,user_email,user_password, user_confirm_password="";
    String base="http://trueblueappworks.com/";
    final  String url="http://trueblueappworks.com/users_login_data/";
    ProgressDialog pd;
    private Animation smalltobig, btta, btta2;
    private ImageView imageView, login_img;
    private Button login_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // load this animation
        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        btta = AnimationUtils.loadAnimation(this, R.anim.btta);
        btta2 = AnimationUtils.loadAnimation(this, R.anim.btta2);


        ed_user_name = findViewById(R.id.username);
        ed_user_email = findViewById(R.id.email);
        ed_user_password = findViewById(R.id.password);
        ed_user_confirm_password = findViewById(R.id.password_confirmation);

        ed_user_name.startAnimation(smalltobig);
        ed_user_email.startAnimation(smalltobig);
        ed_user_password.startAnimation(smalltobig);
        ed_user_confirm_password.startAnimation(smalltobig);


        register_btn = findViewById(R.id.register_btn);
        register_btn.startAnimation(smalltobig);
        findViewById(R.id.register_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( TextUtils.isEmpty(ed_user_name.getText())){
                    ed_user_name.requestFocus();
                    ed_user_name.setError( "Username is required!" );

                }
                else if( TextUtils.isEmpty(ed_user_email.getText())){
                    ed_user_email.requestFocus();
                    ed_user_email.setError( "Email is required!" );

                }
                else if( TextUtils.isEmpty(ed_user_password.getText())){
                    ed_user_password.requestFocus();
                    ed_user_password.setError( "Password is required!" );

                }
                else if( TextUtils.isEmpty(ed_user_confirm_password.getText())){
                    ed_user_confirm_password.requestFocus();
                    ed_user_confirm_password.setError( "Confirm Password is required!" );

                }
                else {

                    user_name = ed_user_name.getText().toString();
                    user_email = ed_user_email.getText().toString();
                    user_password = ed_user_password.getText().toString();
                    user_confirm_password = ed_user_confirm_password.getText().toString();
                    if(user_password.equals(user_confirm_password)){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getSignUp();
                            }
                        }, 3000);

                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(),"Passwords must be match!", Toast.LENGTH_SHORT);
                        toast.show();

                    }


                }

                }


        });


        findViewById(R.id.signup_login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }






    private void getSignUp(){

        String users_api=base+"api/registration/";

        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
        StringRequest request=new StringRequest(Request.Method.POST, users_api,  new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.show(SignUp.this, " ", "Please wait...");
                Toast toast = Toast.makeText(getApplicationContext(),"Registration Done Successfully!", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), SignupMessage.class);
                startActivity(intent);
            }
        }, new com.android.volley.Response.ErrorListener() {
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
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();

                params.put("username",user_name );
                params.put("email",user_email );
                params.put("password",user_password );
                params.put("password2",user_confirm_password );
                params.put("Content-Type", "application/json; charset=UTF-8");
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













