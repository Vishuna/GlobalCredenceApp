package com.example.globalcredenceapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import android.os.Handler;

import kotlin.reflect.KCallable;

public class  AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    ArrayList<String> student_datas = new ArrayList<String>();
    ArrayList<String> student_roll_numbers=new ArrayList<>();
    ArrayList<String> student_attendance_status=new ArrayList<>();
    ArrayList<String> attendance_data = new ArrayList<String>();
    ArrayList<String> attendence_status = new ArrayList<String>();
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
    String std_name,s_name,at_status,Class,institute;
    String student_name_url="http://trueblueappworks.com/api/dashboard/getStudentName/";
    ProgressDialog pd;
//    RadioButton radioButton,pr_value,ab_value,l_value;
    private final Handler handler = new Handler();

    public RadioButton pr_value,ab_value,l_value;

    public  class AttendanceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {

        TextView student_name,student_roll_number,attendance_status;
        RadioGroup radioGroup;
        int radioId;

        String p="present",a="absent",l="leave";
        int p1,a1,l1;
        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            student_name = itemView.findViewById(R.id.student_name);
            student_roll_number = itemView.findViewById(R.id.student_roll_number);
            radioGroup = itemView.findViewById(R.id.radioGroup);

            pr_value= itemView.findViewById(R.id.present);

            ab_value=itemView.findViewById(R.id.absent);
            l_value=itemView.findViewById(R.id.leave);


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
                    attendance_update(institute,Class,attendance_data);

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

    public AttendanceAdapter(ArrayList<String> student_datas, ArrayList<String> student_roll_numbers,ArrayList<String> attendence_status, Button attendance_submit_btn, Context context, String date, String classid,String institute) {
        this.student_datas=student_datas;
        this.student_roll_numbers=student_roll_numbers;
        this.attendence_status=attendence_status;
        this.attendance_submit_btn=attendance_submit_btn;
        this.context=context;
        this.date=date;
        this.Class=classid;
        this.institute=institute;





    }
    public interface VolleyCallback {
        void onSuccess(String result);

    }



    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.student_list_item_layout,parent,false);

        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceViewHolder holder, final int position) {
        String p="present";
        String a="absent";
        String l="leave";
        pos=position;
        std_name= student_datas.get(position);
        holder.student_name.setText(std_name);

        String std_roll_number=student_roll_numbers.get(position);

        holder.student_roll_number.setText(std_roll_number);




        String at=attendence_status.get(position);

            if (at.equals(p)) {
                RadioButton rButton = (RadioButton) holder.radioGroup.getChildAt(0);

                if (rButton.getVisibility() == View.VISIBLE) {
                    rButton.setChecked(true);
                    return;
                }

            }

                if (at.equals(a)) {
                    RadioButton rButton1 = (RadioButton) holder.radioGroup.getChildAt(1);

                    if (rButton1.getVisibility() == View.VISIBLE) {
                        rButton1.setChecked(true);
                        return;
                    }
                }

                    if (at.equals(l)) {
                        RadioButton rButton2 = (RadioButton) holder.radioGroup.getChildAt(2);

                        if (rButton2.getVisibility() == View.VISIBLE) {
                            rButton2.setChecked(true);
                            return;
                        }
                    }
            }
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return student_datas.size();
    }


    private void attendance_update(final String institute, String aClass,  final ArrayList<String> attendance_data){

        String attendance_url="http://trueblueappworks.com/api/attendance_post_update/";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest request=new StringRequest(Request.Method.POST, attendance_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    String responseBody = new String(response);
                    JSONObject data = new JSONObject(responseBody);
                    String message =data.get("message").toString();
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, attendance.class);
                    context.startActivity(intent);
                } catch (JSONException e) {
                }


                }



        }, new Response.ErrorListener() {
            private static final String LOG_TAG ="" ;

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                String errorMsg = "";
                if (response != null && response.data != null) {
                    String errorString = new String(response.data);
                    Log.i("log error", errorString);
                }
            }
//
        })
        {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                params = new HashMap<String, String>();
                String at_data = new Gson().toJson(attendance_data);

                params.put("institute",institute);
                params.put("Class", Class);
                params.put("attendance_status", at_data);
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