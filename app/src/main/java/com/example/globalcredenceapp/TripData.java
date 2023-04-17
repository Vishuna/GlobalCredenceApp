package com.example.globalcredenceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TripData extends AppCompatActivity {
    String trip_data = "http://trueblueappworks.com/api/vehicle/trip_data";
    String route_data="http://trueblueappworks.com/api/vehicle/route_data/";
    String token;
    String user_designation;
    String p_no;
    String p_name,p_route;
    String get_pno,get_pname;


    TableRow tbrow;
    TextView t0v,t;
    TextView t1v,t2v;
    Button btnOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_data);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", null);
        user_designation = preferences.getString("user_designation", null);
        gettrip();
    }

    private void gettrip() {
        final TableLayout stk = (TableLayout) findViewById(R.id.trip_table);
        final TableRow tbrow0 = new TableRow(this);
        tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
        final TextView tv0 = new TextView(this);
        tv0.setText(" S.No. ");
        tv0.setTextColor(getResources().getColor(R.color.table_head));
        tbrow0.addView(tv0);
        final TextView tv1 = new TextView(this);
        tv1.setText(" Point No. ");
        tv1.setTextColor(getResources().getColor(R.color.table_head));
        tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" Point Name ");
        tv2.setTextColor(getResources().getColor(R.color.table_head));
        tbrow0.addView(tv2);
        final TextView tv3 = new TextView(this);
        tv3.setText(" Action ");
        tv3.setTextColor(getResources().getColor(R.color.table_head));
        tbrow0.addView(tv3);
        stk.addView(tbrow0);
        //        API Call
        RequestQueue requestQueue = Volley.newRequestQueue(TripData.this);
        StringRequest request = new StringRequest(Request.Method.GET, trip_data, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        int sr = i + 1;
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        p_no = jsonObject.getString("point_no").toString();
                        p_name = jsonObject.getString("point_name").toString();
                        p_route=jsonObject.getString("route").toString();


// Table Data Intilization
                        tbrow = new TableRow(TripData.this);
                        tbrow.setGravity(Gravity.CENTER_HORIZONTAL);
                        tbrow.setId(i);
                        t0v = new TextView(TripData.this);
                        t0v.setText("" + sr);
                        t0v.setTextColor(Color.parseColor("#212529"));
                        t0v.setGravity(Gravity.CENTER);
                        tbrow.addView(t0v);
                        t1v = new TextView(TripData.this);
                        t1v.setText(p_no);
                        t1v.setTextColor(Color.parseColor("#212529"));
                        t1v.setGravity(Gravity.CENTER);
                        tbrow.addView(t1v);
                        t2v = new TextView(TripData.this);
                        t2v.setText(p_name);
                        t2v.setTextColor(Color.parseColor("#212529"));
                        t2v.setGravity(Gravity.CENTER);
                        tbrow.addView(t2v);


//                        Table button data
                        btnOne = new Button(TripData.this);
                        btnOne.setText("Yes");
                        btnOne.setId(i);
//                        btnOne.setBackgroundColor(btnOne.getContext().getResources().getColor(R.color.blue));
                        //btnOne.setBackgroundResource(R.color.colorAccent);
//                        btnOne.setBackgroundColor(Color.BLUE);
                        btnOne.setBackgroundColor(Color.parseColor("#1976d2"));


                        btnOne.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    int resourceName = view.getId();

                                    TableRow row = (TableRow) view.getParent();
                                    // It's index
                                    int index = stk.indexOfChild(row);
                                    Log.v("Yeh lo", String.valueOf(index));
                                    TextView firstTextView = (TextView) row.getChildAt(0);
                                    TextView secondTextView = (TextView) row.getChildAt(1);
                                    get_pno = firstTextView.getText().toString();

                                    get_pname = secondTextView.getText().toString();
                                    post_trip_data();



//                                    TableRow r = (TableRow) view;
//                                    TextView firstTextView = (TextView) r.getChildAt(view.getId());
//                                    String firstText = firstTextView.getText().toString();
//                                    Log.d("Heeelllloooo",firstText);
//                                    for (int a = 0, b = stk.getChildCount(); a < b; a++) {
//                                        View view1 = stk.getChildAt(a);
//                                        TableRow r = (TableRow) view1;
//                                        TextView firstTextView = (TextView) r.getChildAt(a);
//                                        String firstText = firstTextView.getText().toString();
//                                        Log.d("Heeelllloooo",firstText);
////                                        for (int c = 0, d = r.getChildCount(); c < d; c++) {
////                                            Log.v("rrtgfd", String.valueOf(r));
////                                            t = (TextView) r.getChildAt(1);
////                                            for (int n = 0; n < t.length(); n++) {
////                                                String text = (String) t.getText();
////                                                Log.v("testdata", text);
////                                            }
////                                        }
//
//                                    }

                                }
                            });
//                            btnOne.setBackgroundColor(Color.BLUE);
//                            btnOne.setHeight(20);
//                            btnOne.setWidth(50);
                       // btnOne.setBackgroundTintList(Button, ContextCompat.getColorStateList(this, android.R.color.white))
                            //btnOne.setTextColor(Color.BLUE);
//                            btnOne.setBackgroundColor(Color.parseColor("#1976d2"));
//                            btnOne.setTextColor(Color.parseColor("#ffffff"));
//                        btnOne.setBackgroundResource(R.color.colorAccent);

                        tbrow.addView(btnOne);
                        stk.addView(tbrow);
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
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Token", token);
                System.out.print(token);
                return params;
            }
        };

        requestQueue.add(request);

    }



    private void post_trip_data(){


        RequestQueue requestQueue = Volley.newRequestQueue(TripData.this);
        StringRequest request=new StringRequest(Request.Method.POST, route_data, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response)

            {

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

                Map<String,String> params = new HashMap<String, String>();
                // Removed this line if you dont need it or Use application/json
                params.put("driver",token);
                params.put("route",p_route);
                params.put("point",p_name);



                params.put("Content-Type", "application/json");
                return params;
            }
        };

        requestQueue.add(request);
    }

}