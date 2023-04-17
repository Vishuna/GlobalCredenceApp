package com.example.globalcredenceapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.reflect.KCallable;

public class AttendanceUpdateAdapter extends RecyclerView.Adapter<AttendanceUpdateAdapter.AttendanceViewHolder> {

    ArrayList<String> student_datas = new ArrayList<String>();
    ArrayList<String> student_roll_numbers=new ArrayList<>();
    ArrayList<String> attendance_data = new ArrayList<String>();
    ArrayList<String> attendance_datas=new ArrayList<>();
    List<String> errorList = new ArrayList<String>();
    Context context;
    RadioButton presentId,absentId,leaveId;
    Button attendance_submit_btn;
    String n;
    int pos;
    int j;
    String radioButtonText,date;
    String attendance_value="present";
    String ad;
    String attendance_index_value;
    int p,a;
    Map<String, String> params;
    Map<String, Map<String, String>> params_data;
    String present,absent,leave;
    String[] arr;
    Map<String, String> att_val;
    String gsonString;
    ArrayList<Map<String, String>> mylist = new ArrayList<Map<String, String>>();
    String std_name,s_name;
    String student_name_url="http://trueblueappworks.com/api/dashboard/getStudentName/";
    ProgressDialog pd;


    public  class AttendanceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        TextView student_name,student_roll_number;
        RadioGroup radioGroup;
        int radioId;
        RadioButton radioButton;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            student_name =itemView.findViewById(R.id.student_name);
            student_roll_number=itemView.findViewById(R.id.student_roll_number);
            radioGroup=itemView.findViewById(R.id.radioGroup);
            attendance_data.add(attendance_value);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    Context mcontext = null;
                    try {
                        radioId = radioGroup.getCheckedRadioButtonId();
                        View radioButton = radioGroup.findViewById(radioId);
                        int idx = radioGroup.indexOfChild(radioButton);
                        if (idx != -1) {

                            radioButtonText = ((RadioButton) radioButton).getText().toString();
                            if(radioButtonText.equals("P")){
                                ad="present";
                            }
                            else if(radioButtonText.equals("A")){
                                ad="absent";
                            }
                            else{
                                ad="leave";
                            }


                        }
                        j=getAdapterPosition();
                        attendance_data.set(j,ad);

                    } catch (Exception e) {
                    }
                }

            });
            attendance_submit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (a = 0; a < student_datas.size()-1; a++) {

                        attendance_update(student_datas.get(a),attendance_data.get(a));
                    }
                    Toast toast = Toast.makeText(context,"Attendance Submitted Successfully!", Toast.LENGTH_SHORT);
                    toast.show();

                }
            });
        }

        @Override
        public void onClick(View v) {
            v.setOnClickListener(this);

        }
        public void checkButton(){


//            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

    }

    public ArrayList<String> getArrayList(){
        return attendance_data;
    }

    public AttendanceUpdateAdapter(ArrayList<String> student_datas, ArrayList<String> student_roll_numbers, Button attendance_submit_btn, Context context, String date) {
        this.student_datas=student_datas;
        this.student_roll_numbers=student_roll_numbers;
        this.attendance_submit_btn=attendance_submit_btn;
        this.context=context;
        this.date=date;




    }
    public interface VolleyCallback {
        void onSuccess(String result);
        void onError(String result);
    }
    private void getStudentName(final VolleyCallback callback){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request=new StringRequest(Request.Method.POST, student_name_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Log.v("res",response);
                s_name=response.replace("\"", "");

                callback.onSuccess(s_name);

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
                    jsonBody.put("username", std_name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String requestBody = jsonBody.toString();

                Map<String,String> params = new HashMap<String, String>();
                // Removed this line if you dont need it or Use application/json
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("username", std_name);
                return params;

            }
        };

        requestQueue.add(request);
    }


    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.student_update_list_item_layout,parent,false);

        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceUpdateAdapter.AttendanceViewHolder holder, int position) {
        pos=position;
        std_name= student_datas.get(position);
        getStudentName(new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                s_name.toString().replaceAll("\" ", "");
                holder.student_name.setText(s_name);
            }
            @Override
            public void onError(String result) {

            }
        });



        String std_roll_number=student_roll_numbers.get(position);
        holder.student_roll_number.setText(std_roll_number);
    }



    @Override
    public int getItemCount() {
        return student_datas.size();
    }


    private void attendance_update(final String s, final String s1){

        String attendance_url="http://trueblueappworks.com/api/attendance_post_update/";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request=new StringRequest(Request.Method.POST, attendance_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                final String result = response.toString();
//                Log.d("response", "result : " + result);

                try{

                    Intent intent = new Intent(context, attendance.class);
                    context.startActivity(intent);

                }
                catch(Exception e){
//                    Log.v("e", String.valueOf(e));

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
                    Log.i("log errorsss", message);
                    errorList.add(message);
                    last_message = errorList.get(errorList.size() - 1);
                    Intent intent = new Intent(context, attendance.class);
                    context.startActivity(intent);

                    Toast toast = Toast.makeText(context, last_message, Toast.LENGTH_SHORT);
                    toast.show();

                } catch (JSONException | UnsupportedEncodingException e) {
                }

            }
        })
        {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                params = new HashMap<String, String>();
                String at_data = new Gson().toJson(attendance_data);

                params.put("username", s);
                params.put("attendance_status", s1);
                params.put("date", date);
                params.put("Content-Type", "application/json");

                return params;

            }



        };

        requestQueue.add(request);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }


}