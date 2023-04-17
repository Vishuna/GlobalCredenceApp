package com.example.globalcredenceapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.usage.UsageEvents;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.text.Layout;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.EventLog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.utils.ColorTemplate;

//import com.alamkanak.weekview.MonthLoader;
//import com.alamkanak.weekview.WeekView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.globalcredenceapp.notice.Notice;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;


public class Dashboard extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    // start Navigation Drawer
    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    ViewPagerAdapter viewPagerAdapter;
    DrawerLayout drawer_layout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ImageView menu_iv;
    ImageView my_profile_pic,nav_profile_pic;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    TextView total_std, total_tch, total_cls, act_users, std_ins, std_name, std_cls, std_fee, bus_no, route_no, trip_start,cal_text;
    String small_cards = "http://trueblueappworks.com/api/dashboard/small_cards";
    String daily_att = "http://trueblueappworks.com/api/dashboard/daily_attendence";
    String trip_status = "http://trueblueappworks.com/api/dashboard/trips";
    String trip_data = "http://trueblueappworks.com/api/vehicle/trip_data";
    String route_data = "http://trueblueappworks.com/api/vehicle/route_data/";
    String approval_data = "http://trueblueappworks.com/api/dashboard/approvals";
    String assigned_tch = "http://trueblueappworks.com/api/dashboard/all_assigned_teachers";
    String assigned_cls = "http://trueblueappworks.com/api/dashboard/all_assigned_classes";
    String cls_att = "http://trueblueappworks.com/api/dashboard/class_attendance_status";
    String cls_fee = "http://trueblueappworks.com/api/dashboard/class_unpaid_students";
    String lib_std = "http://trueblueappworks.com/api/fees/studentLibraryData";
    String due_std = "http://trueblueappworks.com/api/fees/studentMyDues";
    String p_std = "http://trueblueappworks.com/api/dashboard/parent_child";
    String temp_url = "http://trueblueappworks.com/api/dashboard/princiapl_fees_sts1";
    String cal_url = "http://trueblueappworks.com/api/dashboard/calendar";
    String map_url = "http://trueblueappworks.com/api/dashboard/bus_location";
    String p_no,Classes;
    String base_url="http://trueblueappworks.com";
    String p_name, p_route, get_pno, get_pname,i_profile_pic,i_insti_logo;
    TableRow tbrow;
    TextView t0v, t;
    TextView t1v, t2v, firstTextView, secondTextView, tv;
    Button btnOne;
    Map<String, String> params;
    String token, user_designation;
    LinearLayout l_total_std, l_total_tch, l_total_cls, l_act_usr, l_ins, l_name, l_cls, l_fee, l_daily_att;
    CalendarView calendarView;
    TextView dateDisplay;
    Context mContext = this;
    LinearLayout l_fee_p, l_bus_no, l_route, l_trip_start, l_trip_card, l_bar_chart, l_all_tch, l_att_sts, l_fee_sts, l_libbook, l_cls_mts, l_dues, l_res, l_lp_book, l_all_cls, l_cls_att, l_cls_fee, l_act_fee,temp_card,l_map_bus;
    Button button;
    public OnLongClickListener longClickListner;
    LinearLayout panel1, panel2, panel3;
    TextView text1, text2, text3;
    View openLayout;
    int index, approve, disapprove, pending;
    BarChart Bchart;
    ArrayList<BarEntry> approved = new ArrayList<>();
    JSONArray p_fees_data = new JSONArray();
    ArrayList<BarEntry> pendings = new ArrayList<>();
    ArrayList<BarEntry> disapproved = new ArrayList<>();
    ConstraintLayout expandableView;
    Button arrowBtn;
    CardView cardView;
    LinearLayout fees_class;
    TextView f_class;
    int i;
    ScrollView principal_fee_scroll,class_fee_status_scroll,assigned_classes_scroll,assigned_teacher_scroll,attendance_status_scroll,parentScrollView;

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- YYYY", Locale.ENGLISH);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", null);

        user_designation = preferences.getString("user_designation", null);
        i_profile_pic=preferences.getString("i_profile_pic",null);
        i_insti_logo=preferences.getString("i_insti_logo",null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        attendance_status_scroll=findViewById(R.id.attendance_status_scroll);
        assigned_teacher_scroll=findViewById(R.id.assigned_teacher_scroll);
        assigned_classes_scroll=findViewById(R.id.assigned_classes_scroll);
        class_fee_status_scroll=findViewById(R.id.class_fee_status_scroll);
        principal_fee_scroll=findViewById(R.id.principal_fee_scroll);
        parentScrollView=findViewById(R.id.parentScrollView);
        NewCal();
        //Approvals Barchart
        Bchart = (BarChart) findViewById(R.id.barcharts);



        //ac
        panel1 = (LinearLayout) findViewById(R.id.panel1);

        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);




        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
        //end accordion


        button = findViewById(R.id.button_trip);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final TableLayout stk = (TableLayout) findViewById(R.id.new_trip);
                stk.setVisibility(View.VISIBLE);
                final TableLayout stk1 = (TableLayout) findViewById(R.id.trip_table);
                stk1.setVisibility(View.GONE);
            }
        });


        int permissionCheck =
                ContextCompat.checkSelfPermission(Dashboard.this,
                        Manifest.permission.WRITE_CALENDAR);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Calendar cal = Calendar.getInstance();
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", cal.getTimeInMillis() + 60 * 60 * 1000);
            intent.putExtra("allDay", true);
            intent.putExtra("rrule", "FREQ=YEARLY");
            intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
            intent.putExtra("title", "A Test Event from android app");
            startActivity(intent);

        } else {

        }
//        Small Cards for all users
        total_std = findViewById(R.id.total_student);
        total_tch = findViewById(R.id.total_teacher);
        total_cls = findViewById(R.id.total_classes);
        act_users = findViewById(R.id.active_users);
//        Small Cards for student
        std_ins = findViewById(R.id.ins);
        std_name = findViewById(R.id.name);
        std_cls = findViewById(R.id.cls);
        std_fee = findViewById(R.id.fee);

        //        Small Cards for Driver
        bus_no = findViewById(R.id.bus_no);
        route_no = findViewById(R.id.route);
        trip_start = findViewById(R.id.trip_start);

//        Linear Layouts
        l_total_std = findViewById(R.id.linear_total_student);
        l_total_tch = findViewById(R.id.linear_total_tch);
        l_total_cls = findViewById(R.id.linear_total_cls);
        l_act_usr = findViewById(R.id.linear_total_act);
        l_ins = findViewById(R.id.linear_ins);
        l_name = findViewById(R.id.linear_name);
        l_cls = findViewById(R.id.linear_cls);
        l_fee = findViewById(R.id.linear_fee);
        l_daily_att = findViewById(R.id.linear_daily_att);
        l_bus_no = findViewById(R.id.linear_bus_no);
        l_route = findViewById(R.id.linear_route);
        l_trip_start = findViewById(R.id.linear_trip_start);
        l_trip_card = findViewById(R.id.linear_driver_trip_card);
        l_bar_chart = findViewById(R.id.l_BarChart);
        l_all_tch = findViewById(R.id.l_AllTeachers);
        l_all_cls = findViewById(R.id.l_AllClasses);
        l_att_sts = findViewById(R.id.l_attstatus);
        l_cls_fee = findViewById(R.id.l_Class_Fee);
        l_fee_sts = findViewById(R.id.l_FeeStatus);
        l_libbook = findViewById(R.id.l_LibBook);
//        l_cls_mts = findViewById(R.id.l_My_Classmates);
        l_dues = findViewById(R.id.l_My_Dues);
        l_lp_book = findViewById(R.id.l_P_LibBook);
        l_cls_att = findViewById(R.id.l_Class_Att);
        l_act_fee = findViewById(R.id.accFee);
        temp_card= findViewById(R.id.l_FeeStatusprincipal);
        temp_card.setVisibility(View.GONE);
        l_map_bus = findViewById(R.id.l_Map);
        l_map_bus.setVisibility(View.GONE);

        if(user_designation.equals("parent"))
        {
            l_act_fee.setVisibility(View.GONE);
            l_fee_sts.setVisibility(View.GONE);
            l_dues.setVisibility(View.GONE);
            l_lp_book.setVisibility(View.GONE);
            l_libbook.setVisibility(View.GONE);
            l_bar_chart.setVisibility(View.GONE);
            l_att_sts.setVisibility(View.GONE);
            getChildCard();
        }

        if (user_designation.equals("teacher")) {
            l_all_tch.setVisibility(View.VISIBLE);
            l_all_cls.setVisibility(View.VISIBLE);
            l_cls_att.setVisibility(View.VISIBLE);
            l_cls_fee.setVisibility(View.VISIBLE);
            l_fee_sts.setVisibility(View.GONE);
            l_act_fee.setVisibility(View.GONE);
            l_dues.setVisibility(View.GONE);
            l_libbook.setVisibility(View.GONE);
            l_lp_book.setVisibility(View.GONE);
            l_att_sts.setVisibility(View.GONE);
            l_bar_chart.setVisibility(View.GONE);
            getAssignedTch();
            getAssignedCls();
            getClassAttendance();
            getFeeStatus();
        } else {
            l_all_tch.setVisibility(View.GONE);
            l_cls_fee.setVisibility(View.GONE);
            l_all_cls.setVisibility(View.GONE);
            l_cls_att.setVisibility(View.GONE);
        }

        if (user_designation.equals("admin") || user_designation.equals("principal")) {
            l_daily_att.setVisibility(View.VISIBLE);
            if (user_designation.equals("principal")){
                temp_card.setVisibility(View.VISIBLE);
            }
            l_act_fee.setVisibility(View.GONE);
            l_fee_sts.setVisibility(View.GONE);
            l_dues.setVisibility(View.GONE);
            l_lp_book.setVisibility(View.GONE);
            l_libbook.setVisibility(View.GONE);
            l_att_sts.setVisibility(View.GONE);
            l_total_std.setVisibility(View.VISIBLE);
            l_total_tch.setVisibility(View.VISIBLE);
            l_total_cls.setVisibility(View.VISIBLE);
            l_act_usr.setVisibility(View.VISIBLE);
            getArrayData();
            gettempcard();

        } else {
            l_daily_att.setVisibility(View.GONE);
            l_total_std.setVisibility(View.GONE);
            l_total_tch.setVisibility(View.GONE);
            l_total_cls.setVisibility(View.GONE);
            l_act_usr.setVisibility(View.GONE);
        }
        if (user_designation.equals("driver")) {
            l_bus_no.setVisibility(View.VISIBLE);
            l_route.setVisibility(View.VISIBLE);
            l_trip_start.setVisibility(View.VISIBLE);
            l_trip_card.setVisibility(View.VISIBLE);
            l_act_fee.setVisibility(View.GONE);
            l_all_tch.setVisibility(View.GONE);
            l_att_sts.setVisibility(View.GONE);
            l_fee_sts.setVisibility(View.GONE);
            l_libbook.setVisibility(View.GONE);
            l_dues.setVisibility(View.GONE);
            l_lp_book.setVisibility(View.GONE);
            l_bar_chart.setVisibility(View.GONE);
//             l_fee_p.setVisibility(View.GONE);
            getTrip();
        } else {
            l_bus_no.setVisibility(View.GONE);
            l_route.setVisibility(View.GONE);
            l_trip_start.setVisibility(View.GONE);
            l_trip_card.setVisibility(View.GONE);

        }

        if (user_designation.equals("student")) {
            l_bar_chart.setVisibility(View.GONE);
            l_att_sts.setVisibility(View.GONE);
            l_lp_book.setVisibility(View.GONE);
            l_total_std.setVisibility(View.GONE);
            l_total_tch.setVisibility(View.GONE);
            l_total_cls.setVisibility(View.GONE);
            l_act_usr.setVisibility(View.GONE);
            l_act_fee.setVisibility(View.GONE);
            l_fee_sts.setVisibility(View.GONE);
            getLibrayData();
            getMyDues();
        } else {
            l_ins.setVisibility(View.GONE);
            l_name.setVisibility(View.GONE);
            l_cls.setVisibility(View.GONE);
            l_fee.setVisibility(View.GONE);
        }
        getSmallCards();
        init();
        gettrip();
       // calendarView = (CalendarView) findViewById(R.id.calendarView);
        getViewId();

        //Maps
        myMap();
    }

    private void myMap() {
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_maps);
        client = LocationServices.getFusedLocationProviderClient(this);
        if(ActivityCompat.checkSelfPermission(Dashboard.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        }else{
            ActivityCompat.requestPermissions(Dashboard.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
    }

    private void getCurrentLocation() {
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.GET, map_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final String locc = jsonObject.getString("location");
                        final Double latt = jsonObject.getDouble("lat");
                        final Double lngg = jsonObject.getDouble("lng");
                        l_map_bus.setVisibility(View.VISIBLE);
                        Task<Location> task = client.getLastLocation();
                        task.addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(final Location location) {
//                if(location != null){
                                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(GoogleMap googleMap) {
                                        LatLng latLng = new LatLng(latt,lngg);
                                        MarkerOptions options;
                                        if(locc.equals("Your Bus Is Here...")){
                                            options = new MarkerOptions().position(latLng).title(locc).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_directions_bus_2422));
                                        }
                                        else{
                                            options = new MarkerOptions().position(latLng).title(locc);

                                        }
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
                                        googleMap.addMarker(options);

                                    }
                                    private BitmapDescriptor bitmapDescriptorFromVector(Context context,int vectorResId){
                                        Drawable vectorDrawable=ContextCompat.getDrawable(context,vectorResId);
                                        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
                                        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
                                        Canvas canvas=new Canvas(bitmap);
                                        vectorDrawable.draw(canvas);
                                        return BitmapDescriptorFactory.fromBitmap(bitmap);
                                    }
                                });
                            }
//            }
                        });



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
        }) {
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

    private void NewCal() {
//        final ActionBar actionBar = getSupportActionBar();
////        actionBar.setTitle(null);
//        actionBar.setDisplayHomeAsUpEnabled(false);
        cal_text = (TextView)findViewById(R.id.calendar_textview);
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.GET, cal_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                if (response.isEmpty()) {
//                    l_dues.setVisibility(View.GONE);
//                    Log.d("Inside","if");
//                }
                try {
                    JSONArray jsonArray = new JSONArray(response);
//                    if(jsonArray.length()<1){
//                        l_dues.setVisibility(View.GONE);
//                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String data1 = jsonObject.getString("holiday").toString();
                        Long date = jsonObject.getLong("date");
                        Long d = date *10;
                        Log.d("data",data1);
                        Log.d("date", String.valueOf(d));
                        Event ev1 = new Event(Color.BLUE,d,data1);
                        compactCalendar.addEvent(ev1);


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
        }) {
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


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                try {
                    List<Event> events = compactCalendar.getEvents(dateClicked);

                    String tempString = "";
                    for (int x = 0; x < events.size(); x++) {
                        tempString = tempString + ", " + events.get(x).getData();
                    }
                    tempString = tempString.substring(2);
                    System.out.println(events);
                    Toast.makeText(getApplicationContext(), tempString, Toast.LENGTH_SHORT).show();
                } catch (Exception exce) {
                }
            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

                cal_text.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

    }

    //    start navigation
    private void getViewId(){
        menu_iv = findViewById(R.id.menu_iv);
        navigationView = findViewById(R.id.navigationView);
        drawer_layout = findViewById(R.id.drawer_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toggle=new ActionBarDrawerToggle(this,drawer_layout,toolbar,R.string.open,R.string.close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.navigationView);
        View header = navigationView.getHeaderView(0);
        nav_profile_pic = header.findViewById(R.id.nav_header_pic);
        Picasso.with(Dashboard.this).load(i_insti_logo).into(nav_profile_pic);
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
                drawer_layout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(getApplicationContext(),MyProfile.class));
                finish();
                break;
            case R.id.profile_institute:
                startActivity(new Intent(getApplicationContext(),InstituteProfile.class));
                finish();
                break;
            case R.id.attendance:
                startActivity(new Intent(getApplicationContext(),attendance.class));
                finish();
                break;
            case R.id.library:
                startActivity(new Intent(getApplicationContext(), IssueBook.class));
                finish();
                break;
            case R.id.notice:
                startActivity(new Intent(getApplicationContext(), Notice.class));
                finish();
                break;
//            ;
            case R.id.logout:
                Intent logout = new Intent(getApplicationContext(), Login.class);
                startActivity(logout);
                finish();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_iv:
                drawer_layout.openDrawer(GravityCompat.START);
                break;
        }

    }

    private void gettempcard() {
        final TableLayout stk = (TableLayout) findViewById(R.id.Fee_table_temp);
        parentScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                findViewById(R.id.principal_fee_scroll).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        principal_fee_scroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        TableRow tbrow0 = new TableRow(this);
        tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView tv0 = new TextView(this);
        tv0.setText("Student Name");
        tv0.setTextColor(getResources().getColor(R.color.table_head));
        //tv0.setGravity(Gravity.CENTER);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("Date");
        tv1.setTextColor(getResources().getColor(R.color.table_head));
        //tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("Due");
        tv2.setTextColor(getResources().getColor(R.color.table_head));
       // tv2.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText("Class");
        tv3.setTextColor(getResources().getColor(R.color.table_head));
       // tv3.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);
//        API Call
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.GET, temp_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                if (response.isEmpty()) {
//                    l_dues.setVisibility(View.GONE);
//                    Log.d("Inside","if");
//                }
                try {
                    JSONArray jsonArray = new JSONArray(response);
//                    if(jsonArray.length()<1){
//                        l_dues.setVisibility(View.GONE);
//                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.d("Yaha H Hum", String.valueOf(jsonObject));
                        String std = jsonObject.getString("student").toString();
                        String date = jsonObject.getString("dues").toString();
                        String due = jsonObject.getString("date").toString();
                        String clsee = jsonObject.getString("class").toString();
                        // Table Data Intilization
                        TableRow tbrow = new TableRow(Dashboard.this);
                        tbrow.setGravity(Gravity.CENTER_HORIZONTAL);
                        TextView t0v = new TextView(Dashboard.this);
                        t0v.setText(std);
                        t0v.setTextColor(Color.BLACK);
                        //t0v.setGravity(Gravity.CENTER);
                        tbrow.addView(t0v);
                        TextView t1v = new TextView(Dashboard.this);
                        t1v.setText(date);
                        t1v.setTextColor(Color.BLACK);
                       // t1v.setGravity(Gravity.CENTER);
                        tbrow.addView(t1v);
                        TextView t2v = new TextView(Dashboard.this);
                        t2v.setText(due);
                        t2v.setTextColor(Color.BLACK);
                        //t2v.setGravity(Gravity.CENTER);
                        tbrow.addView(t2v);
                        TextView t3v = new TextView(Dashboard.this);
                        t3v.setText(clsee);
                        t3v.setTextColor(Color.BLACK);
                        //t3v.setGravity(Gravity.CENTER);
                        tbrow.addView(t3v);
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
        }) {
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

    private void getChildCard() {
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.GET, p_std, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                if (response.isEmpty()) {
//                    l_dues.setVisibility(View.GONE);
//                    Log.d("Inside","if");
//                }
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name").toString();
                        String ins = jsonObject.getString("institute").toString();
                        String img = jsonObject.getString("profile_pic").toString();
                        String cls = jsonObject.getString("class").toString();
                        String abt = jsonObject.getString("about").toString();
                        String con = jsonObject.getString("phone").toString();
                        String rno = jsonObject.getString("roll_no").toString();
                        Double att = jsonObject.getDouble("attendance");
                        String feee = jsonObject.getString("fees").toString();
                        String holi = jsonObject.getString("holiday").toString();
                        String p_img = base_url + img;
                        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.child_linear_layout);
                        LayoutInflater inflater = getLayoutInflater();
                        View myLayout = inflater.inflate(R.layout.activity_parent_child, mainLayout, false);
                        expandableView = (ConstraintLayout) myLayout.findViewById(R.id.expandableView);
                        expandableView.setBackgroundColor(getResources().getColor(R.color.white));
// arrowBtn = (Button)findViewById(R.id.arrowBtn);
                        arrowBtn = (Button) myLayout.findViewById(R.id.arrowBtn);

                        cardView = (CardView) myLayout.findViewById(R.id.cardView);

//                        arrowBtn.setOnClickListener(new View.OnClickListener() {
//                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//                            @Override
//                            public void onClick(View v) {
//
//                                if (expandableView.getVisibility()==View.GONE){
//                                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
//                                    expandableView.setVisibility(View.VISIBLE);
//                                    expandableView.setBackgroundColor(getResources().getColor(R.color.white));
//                                    arrowBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
//                                } else {
//                                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
//                                    expandableView.setVisibility(View.GONE);
//                                    expandableView.setBackgroundColor(getResources().getColor(R.color.white));
//                                    arrowBtn.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
//                                }
//                            }
//
//                        });
//                        mainLayout.addView(arrowBtn);
                        TextView nam = (TextView) myLayout.findViewById(R.id.name);
                        nam.setText(name);
                        TextView inst = (TextView) myLayout.findViewById(R.id.desc);
                        inst.setText(ins);
                        ImageView imgr = (ImageView) myLayout.findViewById(R.id.circleImage);
                        Picasso.with(Dashboard.this).load(p_img).into(imgr);

                        TextView clse = (TextView) myLayout.findViewById(R.id.desc3);
                        clse.setText(cls);
                        TextView abte = (TextView) myLayout.findViewById(R.id.childabout);
                        abte.setText(con);
                        TextView rnoe = (TextView) myLayout.findViewById(R.id.RollNumber);
                        rnoe.setText(rno);
                        TextView attee = (TextView) myLayout.findViewById(R.id.AttendanceName);
                        attee.setText(att.toString());
                        TextView feesee = (TextView) myLayout.findViewById(R.id.FeeName);
                        feesee.setText(feee);
                        TextView h = (TextView) myLayout.findViewById(R.id.HolidayName);
                        h.setText(holi);

                        mainLayout.addView(myLayout);

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
        }) {
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

    private void getMyDues() {
        final TableLayout stk = (TableLayout) findViewById(R.id.Fee_Dues);
        TableRow tbrow0 = new TableRow(this);
        tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView tv0 = new TextView(this);
        tv0.setText("S.No.");
        tv0.setTextColor(getResources().getColor(R.color.table_head));
        // tv0.setGravity(Gravity.CENTER);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("Dues");
        tv1.setTextColor(getResources().getColor(R.color.table_head));
        //tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("Amount");
        tv2.setTextColor(getResources().getColor(R.color.table_head));
        //tv2.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv2);
        stk.addView(tbrow0);
//        API Call
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.GET, due_std, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                if (response.isEmpty()) {
//                    l_dues.setVisibility(View.GONE);
//                    Log.d("Inside","if");
//                }
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length()<1){
                        l_dues.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String due_date = jsonObject.getString("due_date").toString();
                        String amount = jsonObject.getString("amount").toString();
                        int sr = i+1;


// Table Data Intilization
                        TableRow tbrow = new TableRow(Dashboard.this);
                        tbrow.setGravity(Gravity.CENTER_HORIZONTAL);
                        TextView t0v = new TextView(Dashboard.this);
                        t0v.setText(String.valueOf(sr));
                        t0v.setTextColor(Color.BLACK);
                        //t0v.setGravity(Gravity.CENTER);
                        tbrow.addView(t0v);
                        TextView t1v = new TextView(Dashboard.this);
                        t1v.setText(due_date);
                        t1v.setTextColor(Color.BLACK);
                        // t1v.setGravity(Gravity.CENTER);
                        tbrow.addView(t1v);
                        TextView t2v = new TextView(Dashboard.this);
                        t2v.setText(amount);
                        t2v.setTextColor(Color.BLACK);
                        //t2v.setGravity(Gravity.CENTER);
                        tbrow.addView(t2v);
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
        }) {
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

    private void getLibrayData() {
        final TableLayout stk = (TableLayout) findViewById(R.id.LibraryTable);
        TableRow tbrow0 = new TableRow(this);
        tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView tv0 = new TextView(this);
        tv0.setText("Book Name");
        tv0.setTextColor(getResources().getColor(R.color.table_head));
        //tv0.setGravity(Gravity.CENTER);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("Due Date");
        tv1.setTextColor(getResources().getColor(R.color.table_head));
        // tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv1);
        stk.addView(tbrow0);
//        API Call
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.GET, lib_std, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                if (response != null) {
//                    l_libbook.setVisibility(View.GONE);
//                }
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length()<1){
                        l_libbook.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String book = jsonObject.getString("book_name").toString();
                        String due_date = jsonObject.getString("due_date").toString();


// Table Data Intilization
                        TableRow tbrow = new TableRow(Dashboard.this);
                        tbrow.setGravity(Gravity.CENTER_HORIZONTAL);
                        TextView t0v = new TextView(Dashboard.this);
                        t0v.setText(book);
                        t0v.setTextColor(getResources().getColor(R.color.class_table));
                        //t0v.setGravity(Gravity.CENTER);
                        tbrow.addView(t0v);
                        TextView t1v = new TextView(Dashboard.this);
                        t1v.setText(due_date);
                        t1v.setTextColor(Color.BLACK);
                        //t1v.setGravity(Gravity.CENTER);
                        tbrow.addView(t1v);
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
        }) {
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

    private void getAssignedTch () {
        //        Table Header
        final TableLayout stk = (TableLayout) findViewById(R.id.allAssignedTeachers);
        parentScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                findViewById(R.id.assigned_teacher_scroll).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        assigned_teacher_scroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        TableRow tbrow0 = new TableRow(this);
        tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView tv0 = new TextView(this);
        tv0.setText("Teachers Name");
        tv0.setTextColor(getResources().getColor(R.color.table_head));
        //tv0.setGravity(Gravity.CENTER);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("Subjects");
        tv1.setTextColor(getResources().getColor(R.color.table_head));
        //tv1.setGravity(Gravity.CENTER);
        tbrow0.addView(tv1);
        stk.addView(tbrow0);
//        API Call
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.GET, assigned_tch, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length()<1){
                        l_all_tch.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String tch = jsonObject.getString("teachers").toString();
                        String sub = jsonObject.getString("subjects").toString();


// Table Data Intilization
                        TableRow tbrow = new TableRow(Dashboard.this);
                        tbrow.setGravity(Gravity.CENTER_HORIZONTAL);
                        TextView t0v = new TextView(Dashboard.this);
                        t0v.setText(tch);
                        t0v.setTextColor(getResources().getColor(R.color.class_table));
                        t0v.setGravity(Gravity.LEFT);
                        tbrow.addView(t0v);
                        TextView t1v = new TextView(Dashboard.this);
                        t1v.setText(sub);
                        t1v.setTextColor(Color.BLACK);
                        t1v.setGravity(Gravity.LEFT);
                        tbrow.addView(t1v);
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
                    Log.i("log error hai", errorString);
                }
            }
        }) {
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

    private void getAssignedCls () {
        //        Table Header
        final TableLayout stk = (TableLayout) findViewById(R.id.allAssignedClasses);
        parentScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                findViewById(R.id.assigned_classes_scroll).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        assigned_classes_scroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        TableRow tbrow0 = new TableRow(this);
        tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView tv0 = new TextView(this);
        tv0.setText("Assigned Classes");
        tv0.setTextColor(getResources().getColor(R.color.table_head));
        //tv0.setGravity(Gravity.CENTER);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("Assigned Subjects");
        tv1.setTextColor(getResources().getColor(R.color.table_head));
        //tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv1);
        stk.addView(tbrow0);
//        API Call
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.GET, assigned_cls, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String acls = jsonObject.getString("classes").toString();
                        String sub = jsonObject.getString("subjects").toString();


// Table Data Intilization
                        TableRow tbrow = new TableRow(Dashboard.this);
                        tbrow.setGravity(Gravity.CENTER_HORIZONTAL);
                        TextView t0v = new TextView(Dashboard.this);
                        t0v.setText(acls);
                        t0v.setTextColor(getResources().getColor(R.color.class_table));
                        //t0v.setGravity(Gravity.CENTER);
                        tbrow.addView(t0v);
                        TextView t1v = new TextView(Dashboard.this);
                        t1v.setText(sub);
                        t1v.setTextColor(Color.BLACK);
                        //t1v.setGravity(Gravity.CENTER);
                        tbrow.addView(t1v);
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
        }) {
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


    public void getClassAttendance () {
//        Table Header
        final TableLayout stk = (TableLayout) findViewById(R.id.classAttendanceStatus);
        TableRow tbrow0 = new TableRow(this);
        tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView tv0 = new TextView(this);
        tv0.setText("Date");
        //tv0.setGravity(Gravity.CENTER);
        tv0.setTextColor(getResources().getColor(R.color.table_head));
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("Present");
        //tv1.setGravity(Gravity.CENTER);
        tv1.setTextColor(getResources().getColor(R.color.table_head));
        //tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("Absent");
        //tv2.setGravity(Gravity.CENTER);
        tv2.setTextColor(getResources().getColor(R.color.table_head));
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText("Leave");
        //tv3.setGravity(Gravity.CENTER);
        tv3.setTextColor(getResources().getColor(R.color.table_head));
        tbrow0.addView(tv3);
        stk.addView(tbrow0);
//        API Call
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.GET, cls_att, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String date = jsonObject.getString("date").toString();
                        String pre = jsonObject.getString("present").toString();
                        String abs = jsonObject.getString("absent").toString();
                        String lea = jsonObject.getString("leave").toString();
// Table Data Intilization
                        TableRow tbrow = new TableRow(Dashboard.this);
                        tbrow.setGravity(Gravity.CENTER_HORIZONTAL);
                        TextView t0v = new TextView(Dashboard.this);
                        t0v.setText(date);
                        t0v.setTextColor(getResources().getColor(R.color.class_table));
                        //t0v.setGravity(Gravity.CENTER);
                        tbrow.addView(t0v);
                        TextView t1v = new TextView(Dashboard.this);
                        t1v.setText(pre);
                        t1v.setTextColor(Color.BLACK);
                        //t1v.setGravity(Gravity.CENTER);
                        tbrow.addView(t1v);
                        TextView t2v = new TextView(Dashboard.this);
                        t2v.setText(abs);
                        t2v.setTextColor(Color.BLACK);
                        //t2v.setGravity(Gravity.CENTER);
                        tbrow.addView(t2v);
                        TextView t3v = new TextView(Dashboard.this);
                        t3v.setText(lea);
                        t3v.setTextColor(Color.BLACK);
                        //t3v.setGravity(Gravity.CENTER);
                        tbrow.addView(t3v);
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
        }) {
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

    public void getFeeStatus () {
//        Table Header
        final TableLayout stk = (TableLayout) findViewById(R.id.classFeeStatus);
        parentScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                findViewById(R.id.class_fee_status_scroll).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        class_fee_status_scroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        TableRow tbrow0 = new TableRow(this);
        tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView tv0 = new TextView(this);
        tv0.setText("Students");
        //tv0.setGravity(Gravity.CENTER);
        tv0.setTextColor(getResources().getColor(R.color.table_head));
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("Due Date");
        //tv1.setGravity(Gravity.CENTER);
        tv1.setTextColor(getResources().getColor(R.color.table_head));
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("Fee Status");
        //tv2.setGravity(Gravity.CENTER);
        tv2.setTextColor(getResources().getColor(R.color.table_head));
        tbrow0.addView(tv2);
        stk.addView(tbrow0);
//        API Call
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.GET, cls_fee, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                if (response == null) {
//                    l_cls_fee.setVisibility(View.GONE);
//                }
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length()<1){
                        l_cls_fee.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String std = jsonObject.getString("students").toString();
                        String due = jsonObject.getString("duedate").toString();
                        String sts = jsonObject.getString("status").toString();
                        // Table Data Intilization
                        TableRow tbrow = new TableRow(Dashboard.this);
                        tbrow.setGravity(Gravity.CENTER_HORIZONTAL);
                        TextView t0v = new TextView(Dashboard.this);
                        t0v.setText(std);
                        t0v.setTextColor(getResources().getColor(R.color.class_table));
                        //t0v.setGravity(Gravity.CENTER);
                        tbrow.addView(t0v);
                        TextView t1v = new TextView(Dashboard.this);
                        t1v.setText(due);
                        t1v.setTextColor(Color.BLACK);
                        //t1v.setGravity(Gravity.CENTER);
                        tbrow.addView(t1v);
                        TextView t2v = new TextView(Dashboard.this);
                        t2v.setText(sts);
                        t2v.setTextColor(Color.RED);
                        //t2v.setGravity(Gravity.CENTER);
                        tbrow.addView(t2v);
                        stk.addView(tbrow);
                    }
                } catch (Exception exce) {
                    Log.d("Catch Block", "Running...");
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
        }) {
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

    private void getArrayData () {
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.GET, approval_data, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String week = jsonObject.getString("week").toString();
                        approve = jsonObject.getInt("approve");
                        disapprove = jsonObject.getInt("disapprove");
                        pending = jsonObject.getInt("pending");
                        approved.add(new BarEntry(i + 1, approve));
                        disapproved.add(new BarEntry(i + 1, disapprove));
                        pendings.add(new BarEntry(i + 1, pending));
                    }

                    Log.v("Approvaal", String.valueOf(approved));
                    BarDataSet barDataSet = new BarDataSet(pendings, "Pending");
                    barDataSet.setColor(Color.parseColor("#ff7f0e"));

                    BarDataSet barDataSet1 = new BarDataSet(approved, "Approved");
                    barDataSet1.setColors(Color.parseColor("#2ca02c"));

                    BarDataSet barDataSet2 = new BarDataSet(disapproved, "Disapproved");
                    barDataSet2.setColor(Color.parseColor("#d62728"));

                    BarData barData = new BarData(barDataSet, barDataSet1, barDataSet2);
                    barData.setBarWidth(0.10f);
                    Bchart.setData(barData);
                    String[] des = new String[]{"2nd Week", "This Week", "Total"};
                    XAxis xAxis = Bchart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(des));
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1);
                    xAxis.setGranularityEnabled(true);

                    Bchart.setDragEnabled(true);
                    Bchart.setVisibleXRangeMaximum(3);

                    float barSpace = 0.08f;
                    float groupSpace = 0.44f;
                    Bchart.getXAxis().setAxisMinimum(0);
                    Bchart.getXAxis().setAxisMaximum(0 + Bchart.getBarData().getGroupWidth(groupSpace, barSpace) * 3);
                    Bchart.getAxisLeft().setAxisMinimum(0);
                    Bchart.groupBars(0, groupSpace, barSpace);

                    Bchart.invalidate();
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Token", token);
                return params;
            }
        };

        requestQueue.add(request);

    }


    private void getTrip () {
        //        Table Header
        final TableLayout stk = (TableLayout) findViewById(R.id.trip_table);
        TableRow tbrow0 = new TableRow(this);
        tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView tv0 = new TextView(this);
        tv0.setText("Date");
        tv0.setTextColor(getResources().getColor(R.color.table_head));
        //tv0.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("Route Name");
        tv1.setTextColor(getResources().getColor(R.color.table_head));
        //tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("Status");
        tv2.setTextColor(getResources().getColor(R.color.table_head));
        //tv2.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv2);
        stk.addView(tbrow0);
//        API Call
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.GET, trip_status, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String date = jsonObject.getString("date").toString();
                        String route_n = jsonObject.getString("route").toString();
                        String status_n = jsonObject.getString("status").toString();

// Table Data Intilization
                        TableRow tbrow = new TableRow(Dashboard.this);
                        tbrow.setGravity(Gravity.CENTER_HORIZONTAL);
                        TextView t0v = new TextView(Dashboard.this);
                        t0v.setText(date);
                        t0v.setTextColor(getResources().getColor(R.color.class_table));
                        //t0v.setGravity(Gravity.CENTER);
                        tbrow.addView(t0v);
                        TextView t1v = new TextView(Dashboard.this);
                        t1v.setText(route_n);
                        t1v.setTextColor(Color.BLACK);
                        //t1v.setGravity(Gravity.CENTER);
                        t1v.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                        tbrow.addView(t1v);
                        TextView t2v = new TextView(Dashboard.this);
                        t2v.setText(status_n);
                        if (status_n.equals("Incomplete")) {
                            t2v.setTextColor(Color.RED);
                        } else {
                            t2v.setTextColor(Color.parseColor("#7cb342"));
                        }
                       // t2v.setGravity(Gravity.CENTER);
                        tbrow.addView(t2v);
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
        }) {
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

    private void getSmallCards () {
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.GET, small_cards, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String total_student = json.getString("total_student");
                    String total_teacher = json.getString("total_teacher");
                    String total_classes = json.getString("total_classes");
                    String active_users = json.getString("active_users");

                    total_std.setText(total_student);
                    total_tch.setText(total_teacher);
                    total_cls.setText(total_classes);
                    act_users.setText(active_users);


                } catch (Exception exce) {
                    try {
                        JSONObject json = new JSONObject(response);
                        String ins = json.getString("institute");
                        String name = json.getString("name");
                        String cls = json.getString("class");
                        String fee = json.getString("total_due");
                        std_ins.setText(ins);
                        std_name.setText(name);
                        std_cls.setText(cls);
                        std_fee.setText(fee);
                        if (fee.equals("Dues Pending")) {
                            std_fee.setTextColor(Color.RED);
                        }
                    } catch (JSONException e) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String d_name = json.getString("driver_name");
                            String b_name = json.getString("bus_number");
                            String route = json.getString("route");
                            String d = json.getString("date");
                            int total = json.getInt("total");
                            int total_trip = json.getInt("total_trip");
                            bus_no.setText(b_name);
                            route_no.setText(route);
                            trip_start.setText(d);
//                            route.setText(route);
                            if(total==total_trip){
                                Button new_btn = findViewById(R.id.button_trip);
                                new_btn.setEnabled(false);
                                new_btn.setAlpha(.5f);
                                new_btn.setClickable(false);
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }

                }

            }
        }, new Response.ErrorListener() {
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Token", token);
                return params;
            }
        };
        requestQueue.add(request);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void init () {
//        Table Header
        final TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        parentScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                findViewById(R.id.attendance_status_scroll).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        attendance_status_scroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        TableRow tbrow0 = new TableRow(this);
        tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView tv0 = new TextView(this);
        tv0.setText("Class");
        tv0.setTextColor(getResources().getColor(R.color.table_head));
        //tv0.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("Students");
        tv1.setTextColor(getResources().getColor(R.color.table_head));
        //tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("Present");
        tv2.setTextColor(getResources().getColor(R.color.table_head));
        //tv2.setGravity(Gravity.CENTER);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText("Absent");
        tv3.setTextColor(getResources().getColor(R.color.table_head));
        //tv1.setGravity(Gravity.CENTER);
        tbrow0.addView(tv3);
        TextView tv4 = new TextView(this);
        tv4.setText("Leave");
        tv4.setTextColor(getResources().getColor(R.color.table_head));
        //tv1.setGravity(Gravity.CENTER);
        tbrow0.addView(tv4);
        stk.addView(tbrow0);
//        API Call
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.GET, daily_att, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String classes = jsonObject.getString("Classes").toString();
                        String total_std = jsonObject.getString("total_std").toString();
                        String total_pre = jsonObject.getString("total_pre").toString();
                        String total_abs = jsonObject.getString("total_abs").toString();
                        String total_lea = jsonObject.getString("total_lea").toString();

// Table Data Intilization
                        TableRow tbrow = new TableRow(Dashboard.this);
                       // tbrow.setGravity(Gravity.CENTER_HORIZONTAL);
                        TextView t0v = new TextView(Dashboard.this);
                        t0v.setText(classes);
                        t0v.setTextColor(getResources().getColor(R.color.class_table));
                        //t0v.setGravity(Gravity.CENTER);
                        tbrow.addView(t0v);
                        TextView t1v = new TextView(Dashboard.this);
                        t1v.setText(total_std);
                        t1v.setTextColor(Color.BLACK);
                        //t1v.setGravity(Gravity.CENTER);
                        tbrow.addView(t1v);
                        TextView t2v = new TextView(Dashboard.this);
                        t2v.setText(total_pre);
                        t2v.setTextColor(Color.BLACK);
                       // t2v.setGravity(Gravity.CENTER);
                        tbrow.addView(t2v);
                        TextView t3v = new TextView(Dashboard.this);
                        t3v.setText(total_abs);
                        t3v.setTextColor(Color.BLACK);
                       // t3v.setGravity(Gravity.CENTER);
                        tbrow.addView(t3v);
                        TextView t4v = new TextView(Dashboard.this);
                        t4v.setText(total_lea);
                        t4v.setTextColor(Color.BLACK);
                       // t4v.setGravity(Gravity.CENTER);
                        tbrow.addView(t4v);
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
        }) {
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

    private void gettrip () {
        final TableLayout stk = (TableLayout) findViewById(R.id.new_trip);
        stk.setVisibility(View.GONE);
        final TableRow tbrow0 = new TableRow(this);
        tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
        final TextView tv0 = new TextView(this);
        tv0.setText("S.No.");
        tv0.setTextColor(getResources().getColor(R.color.table_head));
        //tv0.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv0);
        final TextView tv1 = new TextView(this);
        tv1.setText("Point No.");
        tv1.setTextColor(getResources().getColor(R.color.table_head));
        //tv1.setGravity(Gravity.CENTER_HORIZONTAL);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText("Point Name");
        tv2.setTextColor(getResources().getColor(R.color.table_head));
        tbrow0.addView(tv2);
        final TextView tv3 = new TextView(this);
        tv3.setText(" Action");
        tv3.setTextColor(getResources().getColor(R.color.table_head));
        //tv3.setGravity(Gravity.CENTER);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);
        //        API Call
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
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
                        p_route = jsonObject.getString("route").toString();
                        int p_st = jsonObject.getInt("status");
                        Log.d("trip", "jsonObject : " + jsonObject.toString());

// Table Data Intilization
                        tbrow = new TableRow(Dashboard.this);
                        tbrow.setGravity(Gravity.CENTER);
                        tbrow.setId(i);
//                        tbrow.setPadding(5, 5, 5, 5);
                        t0v = new TextView(Dashboard.this);
                        t0v.setText("" + sr);
//                        t0v.getLayoutParams().width = 300;
                        t0v.setTextColor(Color.parseColor("#212529"));
//                        t0v.setGravity(Gravity.CENTER);
                        tbrow.addView(t0v);
                        t1v = new TextView(Dashboard.this);
                        t1v.setText(p_no);

//                        t1v.getLayoutParams().width = 80;
                        t1v.setTextColor(Color.parseColor("#212529"));
                        //t1v.setGravity(Gravity.CENTER);
                        tbrow.addView(t1v);
                        t2v = new TextView(Dashboard.this);
                        t2v.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//                        t2v.getLayoutParams().width = 300;
                        t2v.setText(p_name);

                        t2v.setTextColor(Color.parseColor("#212529"));
                        //t2v.setGravity(Gravity.CENTER);
                        tbrow.addView(t2v);

                        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v = inflater.inflate(R.layout.buttons, null);
                        Button buttonYes = v.findViewById(R.id.button_yes);
                        if(p_st>0){
                            buttonYes.setText("Submitted");
                            buttonYes.setTextSize(10);
                            buttonYes.setAlpha(.5f);
                            buttonYes.setEnabled(false);
                        }
                        else{
                            buttonYes.setText("Yes");
                            buttonYes.setTextSize(12);
                        }


                        buttonYes.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                try {
                                    int resourceName = view.getId();

                                    TableRow row = (TableRow) view.getParent().getParent();
                                    // It's index
                                    index = stk.indexOfChild(row);

                                    firstTextView = (TextView) row.getChildAt(0);
                                    secondTextView = (TextView) row.getChildAt(1);

                                    get_pno = firstTextView.getText().toString();
                                    get_pname = secondTextView.getText().toString();
                                    post_trip_data();
                                    Button btn = (Button) view;
                                    btn.setText("Submitted");
                                    btn.setTextSize(10);
                                    btn.setAlpha(.5f);
                                    btn.setEnabled(false);
//                                        btn.setTextColor(Color.parseColor("#7cb342"));
//                                btn.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        tbrow.addView(v);
                        stk.addView(tbrow);
                    }
                    final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View vii = inflater.inflate(R.layout.tripbutton, null);
                    Button btnOnenew = vii.findViewById(R.id.Trip_button);

                    btnOnenew.setTextSize(12);

                    btnOnenew.setText("Finish");

                    stk.addView(vii);
                    btnOnenew.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    });
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
                    Log.i("log error", errorString);
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

    private void post_trip_data () {
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.POST, route_data, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
        }) {


            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();
                Map<String, String> params = new HashMap<String, String>();
                params.put("route", p_route);
                params.put("point", get_pname);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                JSONObject jsonBody = new JSONObject();
                Map<String, String> header = new HashMap<String, String>();
                header.put("token", token);
                return header;
            }
        };

        requestQueue.add(request);
    }



    private void principalFeesData() {
        String princiapl_fees_sts=base_url+"/api/dashboard/princiapl_fees_sts";
        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);
        StringRequest request = new StringRequest(Request.Method.GET, princiapl_fees_sts, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Classes= jsonObject.getString("Classes").toString();
                        p_fees_data= jsonObject.getJSONArray("d");
                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.accFee);

                        LinearLayout lv=new LinearLayout(Dashboard.this);
                        TextView tv=new TextView(Dashboard.this);
                        tv.setText(Classes);
                        lv.addView(tv);


                        LinearLayout linearLayout1= new LinearLayout(Dashboard.this);
                        TableLayout tableLayout=new TableLayout(Dashboard.this);
                        TableRow tbrow0 = new TableRow(Dashboard.this);
                        tbrow0.setGravity(Gravity.CENTER_HORIZONTAL);
                        TextView tv0 = new TextView(Dashboard.this);
                        tv0.setText("Student Name");
                        tv0.setTextColor(getResources().getColor(R.color.table_head));
                        //tv0.setGravity(Gravity.CENTER);
                        tbrow0.addView(tv0);
                        TextView tv1 = new TextView(Dashboard.this);
                        tv1.setText("Due Date");
                        tv1.setTextColor(getResources().getColor(R.color.table_head));
                        //tv1.setGravity(Gravity.CENTER_HORIZONTAL);
                        tbrow0.addView(tv1);
                        TextView tv2 = new TextView(Dashboard.this);
                        tv2.setText("Status");
                        tv2.setTextColor(getResources().getColor(R.color.table_head));
                        //tv2.setGravity(Gravity.CENTER_HORIZONTAL);
                        tbrow0.addView(tv2);
                        tableLayout.addView(tbrow0);
//                        linearLayout1.addView(tableLayout);
                        for (int j = 0; j < p_fees_data.length(); j++) {
                            JSONObject jsonObject1 = p_fees_data.getJSONObject(j);
                            String std_name = jsonObject1.getString("student_name").toString();
                            String due_date = jsonObject1.getString("due_date").toString();
                            String status = jsonObject1.getString("status").toString();
                            Log.v("test", std_name);
                            TableRow tbrow = new TableRow(Dashboard.this);
                            tbrow.setGravity(Gravity.CENTER_HORIZONTAL);
                            TextView t0v = new TextView(Dashboard.this);
                            t0v.setText(std_name);
                            t0v.setTextColor(getResources().getColor(R.color.class_table));
                            //t0v.setGravity(Gravity.CENTER);
                            tbrow.addView(t0v);
                            TextView t1v = new TextView(Dashboard.this);
                            t1v.setText(due_date);
                            t1v.setTextColor(Color.BLACK);
                            //t1v.setGravity(Gravity.CENTER);
                            tbrow.addView(t1v);
                            TextView t2v = new TextView(Dashboard.this);
                            t2v.setText(status);
                            t2v.setTextColor(Color.BLACK);
                           // t2v.setGravity(Gravity.CENTER);
                            tbrow.addView(t2v);
                            tableLayout.addView(tbrow);
                        }
                        linearLayout1.addView(tableLayout);
                        lv.addView(linearLayout1);
//                            tableLayout.addView(tbrow);
//
//                        }
//                        linearLayout1.addView(tableLayout);
//                        lv.addView(linearLayout1);
                        linearLayout.addView(lv);
                    }

                } catch (Exception exce) {
                }
            }
        }, new Response.ErrorListener() {
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
                System.out.print(token);
                return params;
            }
        };

        requestQueue.add(request);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK)  return super.onKeyDown(keyCode, event);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        finish();
                        startActivity(new Intent(Dashboard.this,Login.class));
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();





        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }
}
