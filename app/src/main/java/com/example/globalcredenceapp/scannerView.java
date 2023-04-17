

package com.example.globalcredenceapp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

    public class scannerView extends AppCompatActivity implements ZXingScannerView.ResultHandler
    {
        String issue_book_id,issue_student_id,issue_by_token;
        String issue_book="http://trueblueappworks.com/api/library/issue_book_action/";
        ZXingScannerView scannerView;
        Map<String,String> params = new HashMap<String, String>();
        List<String> errorList = new ArrayList<String>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            scannerView=new ZXingScannerView(this);
            setContentView(scannerView);

            Dexter.withContext(getApplicationContext())
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            scannerView.startCamera();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        }

        @Override
        public void handleResult(Result rawResult) {

            issue_book_id=rawResult.getText();
            issue_student_id=Library_User.l_userid;
            issue_by_token=Library_User.token;
            Library_User.bookid.setText(rawResult.getText());
            Library_User.issue_book_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    issue_book();
                }
            });
            onBackPressed();
        }

        @Override
        protected void onPause() {
            super.onPause();
            scannerView.stopCamera();
        }

        @Override
        protected void onResume() {
            super.onResume();
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        }




        private void issue_book(){

            RequestQueue requestQueue = Volley.newRequestQueue(scannerView.this);
            StringRequest request=new StringRequest(Request.Method.POST, issue_book, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response)

                {
                    JSONObject data = null;
                    try {
                        data = new JSONObject(response);
                        String message =data.get("message").toString();
                        errorList.add(message);

                        String success_message = errorList.get(errorList.size() - 1);
                        Toast toast = Toast.makeText(getApplicationContext(), success_message, Toast.LENGTH_SHORT);
                        toast.show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                private static final String LOG_TAG ="" ;

                @Override
                public void onErrorResponse(VolleyError error) {
                    String last_message;
                    try {
                        String responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject data = new JSONObject(responseBody);
                        String message =data.get("errors").toString();
                        errorList.add(message);
                        last_message = errorList.get(errorList.size() - 1);
                        Toast toast = Toast.makeText(getApplicationContext(), last_message, Toast.LENGTH_SHORT);
                        toast.show();
                        if(last_message.equals("Book issued successfully !")){
                        Library_User.alert.dismiss();}

                    } catch (JSONException | UnsupportedEncodingException e) {
                    }

                }
            })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    params.put("Token", issue_by_token);
                    return params;}
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    JSONObject jsonBody = new JSONObject();

                    final String requestBody = jsonBody.toString();

                    params.put("book_id",issue_book_id);
                    Log.v("s1",issue_book_id);
                    params.put("user",issue_student_id);

                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            requestQueue.add(request);
        }

    }
