package com.banyan.mss;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dd.CircularProgressButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.android.volley.Request.Method.*;

public class Home_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TextWatcher {

    private static final int MY_SOCKET_TIMEOUT_MS = 0;
    String toid, toname;
    String toStationName;
    String slct_day, slct_month, slct_year, dates, from_id, to_id;
    //Json
    public static String stationId_str;
    public static String stationName_str;

    public static final String KEY_SID = "stationId";
    public static final String KEY_SNAME = "stationName";

    private static final String TAG = "";
    SessionManager session;
    String str_id, str_mobile, str_name;
    /*queue*/
    public static RequestQueue queue;
    /*progress bar*/
    //private ProgressDialog pDialog;
    private AlertDialog dialog;
    TextView date, month, years;
    ImageView place_imgv;
    ArrayList places_al = new ArrayList();
    ArrayList toPlace = new ArrayList();
    ArrayList toplaceList = new ArrayList();
    //HashMap<String , String> places_map = new HashMap<String, String>();
    AutoCompleteTextView autoComplete, auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //session
        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        str_id = user.get(SessionManager.KEY_ID);
        str_mobile = user.get(SessionManager.KEY_MOBILE);
        str_name = user.get(SessionManager.KEY_LOGINID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        date = (TextView) findViewById(R.id.date);
        month = (TextView) findViewById(R.id.month);
        years = (TextView) findViewById(R.id.year);
        place_imgv = (ImageView) findViewById(R.id.place);

        /*try {
            dialog = new SpotsDialog(Home_Activity.this);
            dialog.show();
            dialog.setCancelable(false);
            queue = Volley.newRequestQueue(Home_Activity.this);
            makeJsonObjectRequest();

        } catch (Exception e) {
// TODO: handle exception
        }*/
        final CircularProgressButton circularButton = (CircularProgressButton) findViewById(R.id.circularButton);
        circularButton.setIndeterminateProgressMode(true);
        circularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (autoComplete.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(getApplicationContext(), "Please enter Source", Toast.LENGTH_SHORT).show();

                } else if (auto.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(getApplicationContext(), "Please enter Destination", Toast.LENGTH_SHORT).show();

                } else if (circularButton.getProgress() == 0) {
                    String place = autoComplete.getText().toString();
                    String places = auto.getText().toString();
                    if (place != null) {
                        if (places != null) {
                            if (date != null) {

                                fromMakeJsonObjectRequest(place);
                                finalMakeJsonObjectRequest(places);
                                circularButton.setProgress(50);

                            } else {
                                Toast.makeText(getApplicationContext(), "please Select date", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "please Select to Places", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "please Select from Places", Toast.LENGTH_SHORT).show();
                    }


                } else if (circularButton.getProgress() == -1) {
                    circularButton.setProgress(0);
                } else {
                    circularButton.setProgress(-1);
                }
            }
        });

        place_imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = autoComplete.getText().toString();
                String to = auto.getText().toString();
                autoComplete.setText(to);
                auto.setText(from);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //textViewSelection = (TextView) findViewById(R.id.selection);
        autoComplete = (AutoCompleteTextView) findViewById(R.id.edit);
        autoComplete.setThreshold(0);
        autoComplete.addTextChangedListener(this);
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                String place = autoComplete.getText().toString();
                //Toast.makeText(getApplicationContext(), autoComplete.getText().toString(), Toast.LENGTH_LONG).show();
                toMakeJsonObjectRequest(place);

            }
        });

        autoComplete.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, places_al));
        auto = (AutoCompleteTextView) findViewById(R.id.edit2);
        auto.addTextChangedListener(this);
        auto.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, toplaceList));

    }//OnCreate Complet

    /*public void onTextChanged(CharSequence s, int start, int before, int count) {
        textViewSelection.setText(autoComplete.getText());
    }*/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        boolean handleReturn = super.dispatchTouchEvent(ev);

        View view = getCurrentFocus();

        int x = (int) ev.getX();
        int y = (int) ev.getY();

        if (view instanceof EditText) {
            View innerView = getCurrentFocus();

            if (ev.getAction() == MotionEvent.ACTION_UP &&
                    !getLocationOnScreen((EditText) innerView).contains(x, y)) {

                InputMethodManager input = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }

        return handleReturn;
    }

    protected Rect getLocationOnScreen(EditText mEditText) {
        Rect mRect = new Rect();
        int[] location = new int[2];

        mEditText.getLocationOnScreen(location);

        mRect.left = location[0];
        mRect.top = location[1];
        mRect.right = location[0] + mEditText.getWidth();
        mRect.bottom = location[1] + mEditText.getHeight();

        return mRect;
    }

    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // needed for interface, but not used
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        try {
            dialog = new SpotsDialog(Home_Activity.this);
            dialog.show();
            dialog.setCancelable(false);
            queue = Volley.newRequestQueue(Home_Activity.this);
            makeJsonObjectRequest();

        } catch (Exception e) {
// TODO: handle exception
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.d("CDA", "onBackPressed Called");
            Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        }

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bookticket) {
            Intent inm = new Intent(Home_Activity.this, Home_Activity.class);
            startActivity(inm);

        } else if (id == R.id.nav_cancelticket) {
            Intent inm = new Intent(Home_Activity.this, Cancel_Bookdetail_Activity.class);
            startActivity(inm);

        } else if (id == R.id.nav_bookdetails) {
            Intent in = new Intent(Home_Activity.this, Bookdetail_Activity.class);
            startActivity(in);

        } else if (id == R.id.nav_about) {

            Intent in = new Intent(Home_Activity.this, Aboutus_Activity.class);
            startActivity(in);

        } else if (id == R.id.nav_logout) {

            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // Date Picker
    @SuppressLint("ValidFragment")
    private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog picker = new DatePickerDialog(getActivity(),
                    this, year, month, day);
            picker.getDatePicker().setMinDate(c.getTimeInMillis());
            //picker.getDatePicker().setMinDate(c.getTime().getTime());
            // Create a new instance of DatePickerDialog and return it
            return picker;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            //date.setText(view.getDayOfMonth() + " :" + view.getMonth() + ": " + view.getYear());

            //month.setText(""+view.getMonth());
            month.setText(theMonth(view.getMonth()));
            years.setText("" + view.getYear());
            slct_day = String.valueOf(view.getDayOfMonth());
            if (view.getDayOfMonth() < 10) {
                slct_day = String.valueOf("0" + view.getDayOfMonth());
                date.setText(slct_day);
                //AppConfig.day = slct_day;
            } else {
                date.setText(slct_day);
                //AppConfig.day = slct_day;
            }

            if (view.getMonth() < 10) {
                slct_month = "0" + String.valueOf(view.getMonth() + 1);
                //AppConfig.month = slct_month;
            } else {
                slct_month = String.valueOf(view.getMonth() + 1);
                //AppConfig.month = slct_month;
            }

            slct_year = String.valueOf(view.getYear());
            //AppConfig.year = slct_year;
            dates = slct_day + "/" + slct_month + "/" + slct_year;
            AppConfig.day = slct_day;
            AppConfig.month = slct_month;
            AppConfig.year = slct_year;
            //t.makeText(getApplicationContext(), AppConfig.day + "/" + AppConfig.month + "/" + AppConfig.year, Toast.LENGTH_SHORT).show();

        }

    }

    public static String theMonth(int month) {
        String[] monthNames = {"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month];
    }

    //Json
    private void makeJsonObjectRequest() {


        System.out.println("method inside");

        StringRequest request = new StringRequest(Request.Method.GET,
                AppConfig.url_from_list, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                System.out.println("inside res method");
                Log.d(TAG, response.toString());
                System.out.println("response final " + " : " + response.toString());
                try {
                    System.out.println("inside try");

                    JSONObject obj = new JSONObject(response);
                    JSONArray station_arr = obj.getJSONArray("StationList");
                    for (int i = 0; i < station_arr.length(); i++) {
                        JSONObject station = (JSONObject) station_arr
                                .get(i);
                        JSONObject station_list = station
                                .getJSONObject("station");
                        stationId_str = station_list.getString(KEY_SID);
                        int ids = Integer.parseInt(stationId_str);
                        stationName_str = station_list.getString(KEY_SNAME);
                        places_al.add(i, stationName_str);
                        //Toast.makeText(getApplicationContext(), (CharSequence) stationName_str, Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("gaya1" + obj);

                    String s = obj.getString("success");
                    int success = Integer.parseInt(s);

                    System.out.println("gaya" + success);

                } catch (Exception e) {

// TODO Auto-generated catch block
                    System.out.println("inside catch" + e);
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("success" + error.toString());
                Toast.makeText(Home_Activity.this, "no internet connection...!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Home_Activity.this);
                builder1.setMessage("Please Check Your Internet Connection");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                makeJsonObjectRequest();
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Adding request to request queue
        queue.add(request);
    }

    private void fromMakeJsonObjectRequest(final String place) {


        System.out.println("method inside");

        StringRequest request = new StringRequest(Request.Method.GET,
                AppConfig.url_from_list, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                System.out.println("inside res method");
                Log.d(TAG, response.toString());
                System.out.println("response final " + " : " + response.toString());
                try {
                    System.out.println("inside try");

                    JSONObject obj = new JSONObject(response);
                    JSONArray station_arr = obj.getJSONArray("StationList");

                    for (int i = 0; i < station_arr.length(); i++) {
                        JSONObject station = (JSONObject) station_arr
                                .get(i);
                        JSONObject station_list = station
                                .getJSONObject("station");
                        stationId_str = station_list.getString(KEY_SID);
                        int ids = Integer.parseInt(stationId_str);
                        stationName_str = station_list.getString(KEY_SNAME);
                        if (stationName_str.equalsIgnoreCase(place)) {
                            from_id = String.valueOf(ids);
                            //Toast.makeText(getApplicationContext(),"Place_Id__"+from_id,Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(getApplicationContext(), (CharSequence) stationName_str, Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("gaya1" + obj);

                    String s = obj.getString("success");
                    int success = Integer.parseInt(s);

                    System.out.println("gaya" + success);


                } catch (Exception e) {

// TODO Auto-generated catch block
                    System.out.println("inside catch" + e);
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("kiiikik" + error.toString());
                Toast.makeText(Home_Activity.this, "no internet connection...!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;

            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Adding request to request queue
        queue.add(request);
    }

    private void toMakeJsonObjectRequest(final String place) {
        System.out.println("method inside");
        //Toast.makeText(getApplicationContext(), (CharSequence) place + "This is Tomake", Toast.LENGTH_SHORT).show();

        StringRequest request = new StringRequest(Request.Method.GET,
                AppConfig.url_fromTo_list, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                System.out.println("inside res method");
                Log.d(TAG, response.toString());
                System.out.println("response final " + " : " + response.toString());
                try {
                    System.out.println("inside try");

                    JSONObject obj = new JSONObject(response);
                    JSONArray station_arr = obj.getJSONArray("fromToStationList");

                    for (int i = 0; i < station_arr.length(); i++) {

                        JSONObject jobj = station_arr.getJSONObject(i);

                        toid = jobj.getString("fromStationId");
                        toname = jobj.getString("fromStationName");
                        if (toname.equalsIgnoreCase(place)) {

                            JSONArray listArray = jobj.getJSONArray("toStationList");

                            for (int j = 0; j < listArray.length(); j++) {
                                JSONObject stationId = (JSONObject) listArray
                                        .get(j);
                                String toStationid = stationId.getString("toStationId");
                                int toids = Integer.parseInt(toStationid);
                                //Toast.makeText(getApplicationContext(), (CharSequence) toStationid, Toast.LENGTH_SHORT).show();
                                toStationName = stationId.getString("toStationName");
                                toplaceList.add(j, toStationName);
                                Log.d("moni", toStationName);
                                System.out.println("moni" + toStationName);
                            }
                            toPlace.add(i, toname);
                        }

                        //Toast.makeText(getApplicationContext(), (CharSequence) toStationName, Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("gaya1" + obj);

                    String s = obj.getString("success");
                    int success = Integer.parseInt(s);
                    System.out.println("gaya" + success);
                } catch (Exception e) {

// TODO Auto-generated catch block
                    System.out.println("inside catch" + e);
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("kiiikik" + error.toString());
                Toast.makeText(Home_Activity.this, "no internet connection..!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Home_Activity.this);
                builder1.setMessage("Please Check Your Internet Connection");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String place = autoComplete.getText().toString();
                                //Toast.makeText(getApplicationContext(), autoComplete.getText().toString(), Toast.LENGTH_LONG).show();
                                toMakeJsonObjectRequest(place);
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;

            }
        };
// Adding request to request queue
        queue.add(request);
    }

    private void finalMakeJsonObjectRequest(final String place) {
        System.out.println("method inside");
        //Toast.makeText(getApplicationContext(), (CharSequence) place + "This is Tomake", Toast.LENGTH_SHORT).show();

        StringRequest request = new StringRequest(Request.Method.GET,
                AppConfig.url_fromTo_list, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                System.out.println("inside res method");
                Log.d(TAG, response.toString());
                System.out.println("response final " + " : " + response.toString());
                try {
                    System.out.println("inside try");

                    JSONObject obj = new JSONObject(response);
                    JSONArray station_arr = obj.getJSONArray("fromToStationList");

                    for (int i = 0; i < station_arr.length(); i++) {

                        JSONObject jobj = station_arr.getJSONObject(i);

                        toid = jobj.getString("fromStationId");
                        toname = jobj.getString("fromStationName");

                        JSONArray listArray = jobj.getJSONArray("toStationList");

                        for (int j = 0; j < listArray.length(); j++) {
                            JSONObject stationId = (JSONObject) listArray
                                    .get(j);
                            String toStationid = stationId.getString("toStationId");
                            toStationName = stationId.getString("toStationName");
                            if (toStationName.equalsIgnoreCase(place)) {
                                to_id = toStationid;
                                inten();
                                //Toast.makeText(getApplicationContext(), (CharSequence)"to_Station"+ toStationid, Toast.LENGTH_SHORT).show();
                                Log.d("moni", toStationName);
                                System.out.println("moni" + toStationName);
                            }
                        }


                        //Toast.makeText(getApplicationContext(), (CharSequence) toStationName, Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("object" + obj);

                    /*String s = obj.getString("success");
                    int success = Integer.parseInt(s);
                    System.out.println("gaya" + success);*/
                } catch (Exception e) {

// TODO Auto-generated catch block
                    System.out.println("inside catch" + e);
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("kiiikik" + error.toString());
                Toast.makeText(Home_Activity.this, "no internet connection..!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;

            }
        };
// Adding request to request queue
        queue.add(request);
    }

    public void inten() {

        //old url
        String values = "http://mssbus.com/api/?GetTripList&userId=mssapp&password=india44&" +
                "fromStationId=" + from_id + "&toStationId=" + to_id + "&travelDate=" + dates + "&format=json";

        String value = "http://www.mssbus.com/api/app/?GetTripList&Appkey=HaPpY&fromStationId=" + from_id + "&toStationId=" + to_id + "&travelDate=" + dates + "&format=json";

        AppConfig.from = autoComplete.getText().toString();
        AppConfig.to = auto.getText().toString();

        AppConfig.from_id = Integer.parseInt(from_id);
        AppConfig.to_id = Integer.parseInt(to_id);
        AppConfig.date = dates;

        String from = ((GlobalValues) this.getApplication()).setfrom(autoComplete.getText().toString());

        String to = ((GlobalValues) this.getApplication()).setfrom(auto.getText().toString());

        String traveldate = ((GlobalValues) this.getApplication()).getdate();
        //Pass data to next Intent

        Intent intent = new Intent(Home_Activity.this, Trip_List_Activity.class);
        intent.putExtra("some_key", value);
        intent.putExtra("date", dates);
        intent.putExtra("from_id", from_id);
        intent.putExtra("to_id", to_id);
        intent.putExtra("from", autoComplete.getText().toString());
        intent.putExtra("to", auto.getText().toString());
        startActivity(intent);
    }
}



