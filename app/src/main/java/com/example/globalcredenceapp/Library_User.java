


package com.example.globalcredenceapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.intellij.lang.annotations.JdkConstants;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.globalcredenceapp.BaseUrl;

public class Library_User extends AppCompatActivity {
    public static String token;
    TableRow tbrow;
    ArrayAdapter<String> adapter;
    TableLayout stk;
    public static String l_name, l_profile_pic, l_designation, l_Class, l_roll_number,l_user_id;
    String user_profile = BaseUrl.web_url + "api/library/UserProfile/";
    Map map = new HashMap();
    TextView tv,tv1;
    Pattern p;
    Matcher m;
   public static String l_username,l_userid;
    public static EditText bookid;
    public static  Button issue_book_btn;
    public static AlertDialog alert;


    SearchView searchView;
    public static String scan_code_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", null);
        super.onCreate(savedInstanceState);
        Log.v("user", user_profile);
        setContentView(R.layout.activity_library__user);
        searchView = findViewById(R.id.search_view);
        stk = (TableLayout) findViewById(R.id.user_details);
        getLibraryUser();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(stk, newText);
                return false;
            }
        });

    }


    private void getLibraryUser() {

//        stk.setVisibility(View.GONE);
        final TableRow tbrow0 = new TableRow(this);
//        tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
        final TextView tv0 = new TextView(this);
        tv0.setText("S.No");
        tv0.setPadding(20, 5, 20, 5);
        tv0.setTextColor(getResources().getColor(R.color.table_head));
        tbrow0.addView(tv0);
        final TextView tv1 = new TextView(this);
        tv1.setText("Name");
        tv1.setPadding(20, 5, 20, 5);
        tv1.setTextColor(getResources().getColor(R.color.table_head));
        //tv0.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv1);
//        final TextView tv1 = new TextView(this);
//        tv1.setText("Profile Pic");
//        tv1.setPadding(20, 5, 20, 5);
//        tv1.setTextColor(getResources().getColor(R.color.table_head));
//        //tv1.setGravity(Gravity.CENTER_HORIZONTAL);
//        tbrow0.addView(tv1);
        final TextView tv2 = new TextView(this);
        tv2.setText("designation");
        tv2.setPadding(20, 5, 20, 5);
        tv2.setTextColor(getResources().getColor(R.color.table_head));
        tbrow0.addView(tv2);
        final TextView tv3 = new TextView(this);
        tv3.setText("Class");
        tv3.setPadding(20, 5, 20, 5);
        tv3.setTextColor(getResources().getColor(R.color.table_head));
        //tv3.setGravity(Gravity.CENTER);
        tbrow0.addView(tv3);
        final TextView tv4 = new TextView(this);
        tv4.setText("Roll No");
        tv4.setPadding(20, 5, 20, 5);
        tv4.setTextColor(getResources().getColor(R.color.table_head));
        //tv3.setGravity(Gravity.CENTER);
        tbrow0.addView(tv4);
        final TextView tv5 = new TextView(this);
        tv5.setText("Action");
        tv5.setPadding(20, 5, 20, 5);
        tv5.setTextColor(getResources().getColor(R.color.table_head));
        //tv3.setGravity(Gravity.CENTER);
        tbrow0.addView(tv5);
        stk.addView(tbrow0);

        //        API Call
        RequestQueue requestQueue = Volley.newRequestQueue(Library_User.this);
        StringRequest request = new StringRequest(Request.Method.GET, user_profile, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        int sr = i + 1;
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        l_name = jsonObject.getString("name").toString();
                        l_user_id=jsonObject.getString("user_id").toString();
                        l_profile_pic = jsonObject.getString("profile_pic").toString();
                        l_designation = jsonObject.getString("designation").toString();
                        l_Class = jsonObject.getString("Class").toString();
                        l_roll_number = jsonObject.getString("roll_number");
                        // Table Data Intilization
                        tbrow = new TableRow(Library_User.this);
                        tbrow.setGravity(Gravity.CENTER);
                        tbrow.setId(i);

                        TextView t0v = new TextView(Library_User.this);
                        t0v.setText("" + sr);
                        t0v.setTextColor(Color.parseColor("#212529"));
                        t0v.setPadding(20, 5, 20, 5);
//                         t0v.setGravity(Gravity.CENTER);
                        tbrow.addView(t0v);
                        TextView t1v = new TextView(Library_User.this);
                        t1v.setText(l_name);
                        t1v.setTextColor(Color.parseColor("#212529"));
                        t1v.setPadding(20, 5, 20, 5);
                        //t1v.setGravity(Gravity.CENTER);
                        tbrow.addView(t1v);
//                        TextView t2v = new TextView(Library_User.this);
//                        t2v.setText(l_profile_pic);
//                        t2v.setTextColor(Color.parseColor("#212529"));
//                        //t2v.setGravity(Gravity.CENTER);
//                        tbrow.addView(t2v);
                        TextView t3v = new TextView(Library_User.this);
                        t3v.setText(l_designation);
                        t3v.setTextColor(Color.parseColor("#212529"));
                        t3v.setPadding(20, 5, 20, 5);
//                        t3v.setGravity(Gravity.CENTER);
                        tbrow.addView(t3v);
                        TextView t4v = new TextView(Library_User.this);
                        t4v.setText(l_Class);
                        t4v.setTextColor(Color.parseColor("#212529"));
                        t4v.setPadding(20, 5, 20, 5);
                        //t2v.setGravity(Gravity.CENTER);
                        tbrow.addView(t4v);
                        TextView t5v = new TextView(Library_User.this);
                        t5v.setText(l_roll_number);
                        t5v.setTextColor(Color.parseColor("#212529"));
                        t5v.setPadding(20, 5, 20, 5);
                        //t2v.setGravity(Gravity.CENTER);
                        tbrow.addView(t5v);
                        TextView t6v = new TextView(Library_User.this);
                        t6v.setText(l_user_id);
                        t6v.setVisibility(View.GONE);
                        t6v.setTextColor(Color.parseColor("#212529"));
                        //t2v.setGravity(Gravity.CENTER);
                        tbrow.addView(t6v);


                        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = inflater.inflate(R.layout.library_button, null);
                        Button buttonYes = v.findViewById(R.id.library_but);
//                        if(l_name>0){
//                            buttonYes.setText("Submitted");
//                            buttonYes.setTextSize(10);
//                            buttonYes.setAlpha(.5f);
//                            buttonYes.setEnabled(false);
//                        }
//                        else{
                        buttonYes.setText("");
                        buttonYes.setTextSize(12);
//                        }


                        buttonYes.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                try {
                                    int resourceName = view.getId();

                                    TableRow row = (TableRow) view.getParent().getParent();
                                    // It's index
                                    int index = stk.indexOfChild(row);
                                    TextView firstTextView = (TextView) row.getChildAt(1);
                                    TextView secondTextView = (TextView) row.getChildAt(5);
                                    l_username = firstTextView.getText().toString();
                                   l_userid = secondTextView.getText().toString();
                                    user_data_popup();
//                                    Button btn = (Button) view;
//                                    btn.setText("Submitted");
//                                    btn.setTextSize(10);
//                                    btn.setAlpha(.5f);
//                                    btn.setEnabled(false);
////                                        btn.setTextColor(Color.parseColor("#7cb342"));
////                                btn.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        tbrow.addView(v);

                        stk.addView(tbrow);


                    }
//                    final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View vii = inflater.inflate(R.layout.tripbutton, null);
//                    Button btnOnenew = vii.findViewById(R.id.Trip_button);
//
//                    btnOnenew.setTextSize(12);
//
//                    btnOnenew.setText("Finish");
//
//                    stk.addView(vii);
//                    btnOnenew.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = getIntent();
//                            finish();
//                            startActivity(intent);
//                        }
//                    });
                } catch (Exception exce) {
                    exce.printStackTrace();
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

                }
            }
        }) {
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

    public void filter(TableLayout tl, String regex) {

//        TableRow tr;

        p = Pattern.compile(regex);
        int n = tl.getChildCount();


        for (int i = 0; i < n; i++) {
            tbrow = (TableRow) tl.getChildAt(i);
            tv = (TextView) tbrow.getChildAt(1);
            tv1=(TextView) tbrow.getChildAt(5);

            m = p.matcher(tv.getText());
            if (m.find()) {
                tbrow.setVisibility(View.VISIBLE);
            } else {
                tbrow.setVisibility(View.GONE);
            }
        }
    }


    void user_data_popup(){
        AlertDialog.Builder mydialog=new AlertDialog.Builder(this);
        mydialog.setTitle("Issue Book");
        TextView textView = new TextView(this);
        TextView scan_code = new TextView(this);
        issue_book_btn=new Button(this);

        LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                80
        );
        params.setMargins(70, 50, 50,0);

        textView.setLayoutParams(params);
        textView.setBackground(getDrawable(R.drawable.textview_border));
        textView.setText(l_username);
        textView.setPadding(25, 5, 0, 0);
        ll.addView(textView);

        LinearLayout l_scan=new LinearLayout(this);
        LinearLayout.LayoutParams params_scan = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        l_scan.setLayoutParams(params_scan);
        l_scan.setOrientation(LinearLayout.HORIZONTAL);
//        l_scan.setPadding(35,5,5,5);

        l_scan.setPadding(80,40,20,10);

        bookid = new EditText(this);
        scan_code.setText("Scan Code");
        scan_code.setPadding(25,5,0,5 );
        scan_code.setTextColor(getResources().getColor(R.color.theme_color));
        bookid.setInputType(InputType.TYPE_CLASS_TEXT);
        bookid.setBackground(getDrawable(R.drawable.textview_border));
        bookid.setPadding(20, 5, 0, 5);
        bookid.setWidth(550);
        bookid.setTextSize(15);
        bookid.setTextColor(getResources().getColor(R.color.my_spinner_text_color));

        l_scan.addView(bookid);
        l_scan.addView(scan_code);


        LinearLayout ln=new LinearLayout(this);
        ln.setOrientation(LinearLayout.VERTICAL);

        ln.addView(issue_book_btn);

        ll.addView(l_scan);
        ll.addView(ln);
        LinearLayout.LayoutParams buttonparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonparam.setMargins(90,20,0,0);
        issue_book_btn.setLayoutParams(buttonparam);
        issue_book_btn.setText("Submit");
        issue_book_btn.setTextColor(Color.WHITE);
        issue_book_btn.setBackground(getDrawable(R.drawable.button_selector));
        mydialog.setView(ll);

//        mydialog.setView(textView);
        mydialog.setPositiveButton(
                "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Library_User.this,"Successful", Toast.LENGTH_LONG).show();
            }
        });

        scan_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),scannerView.class));
            }
        });

        alert = mydialog.create();
        alert.show();

    }

    void openDialog(){
        libraryDialog l_dialog=new libraryDialog();
        l_dialog.show(getSupportFragmentManager(),"Issue Book");
    }
}