package com.banyan.mss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import Adapter.Sleeper32_GridviewAdapter;
import dmax.dialog.SpotsDialog;

public class sleeper32_Activity extends Activity implements OnItemClickListener {
    GridView gridView;
    ArrayList<Item> gridArray = new ArrayList<Item>();
    ArrayList<String> seat_id = new ArrayList<String>();
    ArrayList<String> ladies = new ArrayList<String>();
    Sleeper32_GridviewAdapter customGridAdapter;
    public Bitmap seatIcon, driver, long_available, long_booked, long_ladiesavailable, long_std;
    public Bitmap seatSelect, Booked, ladies_seat, lower, upper;
    String fare, day, month, seat_count;
    int year, fromid, toid, p, lad;
    int scheduleId;
    Float ticketrate, balance = 0f;
    Integer n, count = 0;
    final String TAG = "Lower.java";
    ArrayList<String> seat_status = new ArrayList<String>();
    ArrayList<String> seat_no = new ArrayList<String>();
    //ProgressDialog mProgressDialog;
    private AlertDialog dialog;
    TextView cost, selected_seat;

    /*queue*/
    public static RequestQueue queue;
    /*progress bar*/
    //private ProgressDialog pDialog;

    String url;
    private static final int MY_SOCKET_TIMEOUT_MS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleeper32);
        /*String from=((GlobalValues)this.getApplication()).getfrom();

        String to=((GlobalValues)this.getApplication()).getto();

        String traveldate=((GlobalValues)this.getApplication()).getdate();

        ActionBar actionBar = getActionBar();

        actionBar.setTitle(from+" to "+to);

        actionBar.setSubtitle(traveldate);*/


        //retriving value from the select_bus class

        TextView dates = (TextView) findViewById(R.id.triplistA_txt_date);
        TextView from = (TextView) findViewById(R.id.triplistA_txt_from);
        TextView to = (TextView) findViewById(R.id.triplistA_txt_to);

        dates.setText(AppConfig.date);
        from.setText(AppConfig.from);
        to.setText(AppConfig.to);

        fare = getIntent().getExtras().getString("fare");
        scheduleId = getIntent().getExtras().getInt("scheduleId");
        ticketrate = Float.valueOf(fare);

        Log.e(TAG, "fare--------------------------------------------------->" + fare);

        selected_seat = (TextView) findViewById(R.id.selected_seat_l);
        cost = (TextView) findViewById(R.id.amount_l);

        /**************************************************************************************************/
        seatIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_long_avl);
        seatSelect = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_long_std);
        Booked = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_long_bkd);
        ladies_seat = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_long_lad_avl);


        long_available = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_tall_avl);
        long_booked = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_tall_bkd);
        long_ladiesavailable = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_tall_lad_avl);
        long_std = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_tall_std);


        driver = BitmapFactory.decodeResource(this.getResources(), R.drawable.steering_icon);
        lower = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_l);
        upper = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_u);

        totalSeat(56);
        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setColumnWidth(1);
        customGridAdapter = new Sleeper32_GridviewAdapter(this, R.layout.seatrow_grid, gridArray);
        gridView.setAdapter(customGridAdapter);
        gridView.setOnItemClickListener(this);

        try {
            dialog = new SpotsDialog(sleeper32_Activity.this);
            dialog.show();
            queue = Volley.newRequestQueue(sleeper32_Activity.this);
            makeJsonObjectRequest();

        } catch (Exception e) {
// TODO: handle exception
        }
        Button done = (Button) findViewById(R.id.done_s);

        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (seat_id.size() == 0) {

                    Toast.makeText(getApplicationContext(), "Please select a seat", Toast.LENGTH_SHORT).show();
                } else {
                    int ticketcount = seat_id.size();
                    AppConfig.ticket_count = ticketcount;

                    //((GlobalValues)sleeper32_Activity.this.getApplicationContext()).setcount(ticketcount);

                    Intent in = new Intent(sleeper32_Activity.this, Select_Boarding_point_Activity.class);
                    in.putStringArrayListExtra("seats", seat_id);
                    in.putStringArrayListExtra("ladies", ladies);
                    in.putExtra("fid", AppConfig.from_id);
                    in.putExtra("tid", AppConfig.to_id);
                    in.putExtra("month", AppConfig.month);
                    in.putExtra("day", AppConfig.day);
                    in.putExtra("year", AppConfig.year);
                    in.putExtra("sid", scheduleId);
                    startActivity(in);
                    Log.e(TAG, String.valueOf(seat_id));

                }

            }
        });

    }

    public void totalSeat(int n) {
        for (int i = 1; i <= n; ++i) {
            if (i == 1 || i == 3 || i == 6 || i == 10 || i == 14 || i == 18 || i == 22 || i == 25 || i == 27 || i == 28 || i == 29 || i == 31 || i == 32 || i == 34 || i == 38 || i == 42 || i == 46 || i == 50 || i == 53 || i == 55 || i == 56) {
                gridArray.add(new Item(null, "" + ""));

            } else if (i == 30) {
                gridArray.add(new Item(upper, ""));
            } else if (i == 2) {
                gridArray.add(new Item(lower, ""));

            } else if (i == 4) {
                gridArray.add(new Item(driver, "" + ""));
            } else if (i == 26 || i == 54) {
                gridArray.add(new Item(long_available, "" + ""));
            } else {
                gridArray.add(new Item(seatIcon, "" + ""));
            }


        }
    }

    public void seatSelected(int pos) {

        Log.e(TAG, "seatselected position------------------------------->" + String.valueOf(pos));

        gridArray.remove(pos);
        gridArray.add(pos, new Item(seatSelect, ""));

        String c = cost.getText().toString();
        String[] split = c.split("₹");
        balance = Float.valueOf(split[1]) + ticketrate;
        cost.setText("₹" + String.valueOf(balance));
        //Log.e(TAG,"balance------------------------------------->"+split[1] );
        balance = 0f;
        //Log.e(TAG,"--------------------------------------------------------------------------------");
        count++;


        seat_count = selected_seat.getText().toString();

        if (pos == 7) {
            n = 1;
        }
        if (pos == 6) {
            n = 2;
        }
        if (pos == 4) {
            n = 3;
        }
        if (pos == 11) {
            n = 7;
        }
        if (pos == 10) {
            n = 8;
        }
        if (pos == 8) {
            n = 9;
        }
        if (pos == 15) {
            n = 13;
        }
        if (pos == 14) {
            n = 14;
        }
        if (pos == 12) {
            n = 15;
        }
        if (pos == 19) {
            n = 19;
        }
        if (pos == 18) {
            n = 20;
        }
        if (pos == 16) {
            n = 21;
        }
        if (pos == 23) {
            n = 25;
        }
        if (pos == 22) {
            n = 26;
        }
        if (pos == 20) {
            n = 27;
        }
        if (pos == 32) {
            n = 4;
        }
        if (pos == 34) {
            n = 5;
        }
        if (pos == 35) {
            n = 6;
        }
        if (pos == 36) {
            n = 10;
        }
        if (pos == 38) {
            n = 11;
        }
        if (pos == 39) {
            n = 12;
        }
        if (pos == 40) {
            n = 16;
        }
        if (pos == 42) {
            n = 17;
        }
        if (pos == 43) {
            n = 18;
        }
        if (pos == 44) {
            n = 22;
        }
        if (pos == 46) {
            n = 23;
        }
        if (pos == 47) {
            n = 24;
        }
        if (pos == 48) {
            n = 28;
        }
        if (pos == 50) {
            n = 29;
        }
        if (pos == 51) {
            n = 30;
        }
        if (seat_count.isEmpty()) {


            seat_id.add(String.valueOf(n));

            for (int i = 0; i < seat_id.size(); i++) {

                selected_seat.setText(String.valueOf(n));

            }
        } else {


            seat_id.add(String.valueOf(n));

            Log.e(TAG, "value of n----------------->" + n);

            selected_seat.setText(selected_seat.getText().toString() + "," + n);

        }

        customGridAdapter.notifyDataSetChanged();


    }

    public void seatDeselcted(int pos) {

        if (pos == lad) {
            gridArray.remove(pos);
            gridArray.add(pos, new Item(ladies_seat, "" + ""));
        } else {
            gridArray.remove(pos);
            gridArray.add(pos, new Item(seatIcon, "" + ""));
        }

        String c = cost.getText().toString();
        String[] split = c.split("₹");
        balance = Float.valueOf(split[1]) - ticketrate;
        cost.setText("₹" + String.valueOf(balance));
        //Log.e(TAG,"balance------------------------------------->"+balance );
        balance = 0f;
        //Log.e(TAG,"--------------------------------------------------------------------------------");
        count = count - 1;
        if (pos == 7) {
            n = 1;
        }
        if (pos == 6) {
            n = 2;
        }
        if (pos == 4) {
            n = 3;
        }
        if (pos == 11) {
            n = 7;
        }
        if (pos == 10) {
            n = 8;
        }
        if (pos == 8) {
            n = 9;
        }
        if (pos == 15) {
            n = 13;
        }
        if (pos == 14) {
            n = 14;
        }
        if (pos == 12) {
            n = 15;
        }
        if (pos == 19) {
            n = 19;
        }
        if (pos == 18) {
            n = 20;
        }
        if (pos == 16) {
            n = 21;
        }
        if (pos == 23) {
            n = 25;
        }
        if (pos == 22) {
            n = 26;
        }
        if (pos == 20) {
            n = 27;
        }
        if (pos == 32) {
            n = 4;
        }
        if (pos == 34) {
            n = 5;
        }
        if (pos == 35) {
            n = 6;
        }
        if (pos == 36) {
            n = 10;
        }
        if (pos == 38) {
            n = 11;
        }
        if (pos == 39) {
            n = 12;
        }
        if (pos == 40) {
            n = 16;
        }
        if (pos == 42) {
            n = 17;
        }
        if (pos == 43) {
            n = 18;
        }
        if (pos == 44) {
            n = 22;
        }
        if (pos == 46) {
            n = 23;
        }
        if (pos == 47) {
            n = 24;
        }
        if (pos == 48) {
            n = 28;
        }
        if (pos == 50) {
            n = 29;
        }
        if (pos == 51) {
            n = 30;
        }

        for (int k = 0; k < 55; k++) {

            if (pos == k) {
                selected_seat.setText("");
                for (int i = 0; i < seat_id.size(); i++) {


                    // Log.e(TAG,"true");

                    seat_id.remove(String.valueOf(n));

                    if (seat_id.size() == 0) {

                    } else {
                        if (selected_seat.getText().toString().isEmpty()) {

                            selected_seat.setText(selected_seat.getText().toString() + seat_id.get(i));
                        } else {
                            selected_seat.setText(selected_seat.getText().toString() + "," + seat_id.get(i));
                        }
                    }

                }

            }

        }


        customGridAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Item item = gridArray.get(position);
        Bitmap seatcompare = item.getImage();
        if (seatcompare == seatIcon) {
            if (count < 6) {
                seatSelected(position);
            } else {
                Toast.makeText(getApplicationContext(), "Maximum 6 seats", Toast.LENGTH_SHORT).show();
            }
        } else if (seatcompare == Booked) {

        } else if (seatcompare == ladies_seat) {
            if (count < 6) {
                seatSelected(position);
                lad = position;

            } else {
                Toast.makeText(getApplicationContext(), "Maximum 6 seats", Toast.LENGTH_SHORT).show();
            }

        } else if (seatcompare == long_available) {

            if (count < 6) {
                lastseat(position);
            } else {
                Toast.makeText(getApplicationContext(), "Maximum 6 seats", Toast.LENGTH_SHORT).show();
            }

        } else {
            if (position == 25 || position == 53) {
                lastseat_Deselected(position);
            } else {
                seatDeselcted(position);
            }

        }

    }

    private void lastseat_Deselected(int pos) {
        // TODO Auto-generated method stub

        gridArray.remove(pos);
        gridArray.add(pos, new Item(long_available, "" + ""));
        String c = cost.getText().toString();
        String[] split = c.split("₹");
        balance = Float.valueOf(split[1]) - ticketrate;
        cost.setText("₹" + String.valueOf(balance));
        //Log.e(TAG,"balance------------------------------------->"+balance );
        balance = 0f;
        //Log.e(TAG,"--------------------------------------------------------------------------------");
        count = count - 1;
        if (pos == 25) {
            n = 31;
        }
        if (pos == 53) {
            n = 32;
        }
        for (int k = 0; k < 55; k++) {

            if (pos == k) {
                selected_seat.setText("");
                for (int i = 0; i < seat_id.size(); i++) {


                    // Log.e(TAG,"true");

                    seat_id.remove(String.valueOf(n));

                    if (seat_id.size() == 0) {

                    } else {
                        if (selected_seat.getText().toString().isEmpty()) {

                            selected_seat.setText(selected_seat.getText().toString() + seat_id.get(i));
                        } else {
                            selected_seat.setText(selected_seat.getText().toString() + "," + seat_id.get(i));
                        }
                    }
                }

            }
        }

        customGridAdapter.notifyDataSetChanged();
    }

    private void lastseat(int position) {
        // TODO Auto-generated method stub

        gridArray.remove(position);
        gridArray.add(position, new Item(long_std, ""));
        String c = cost.getText().toString();
        String[] split = c.split("₹");
        balance = Float.valueOf(split[1]) + ticketrate;
        cost.setText("₹" + String.valueOf(balance));
        //Log.e(TAG,"balance------------------------------------->"+split[1] );
        balance = 0f;
        //Log.e(TAG,"--------------------------------------------------------------------------------");
        count++;

        seat_count = selected_seat.getText().toString();

        if (position == 25) {
            n = 31;
        }
        if (position == 53) {
            n = 32;
        }

        if (seat_count.isEmpty()) {

            //     Log.e(TAG, "seat_count........................|"+seat_count);


            seat_id.add(String.valueOf(n));

            for (int i = 0; i < seat_id.size(); i++) {

                selected_seat.setText(String.valueOf(n));
            }

        } else {


            seat_id.add(String.valueOf(n));

            Log.e(TAG, "value of n----------------->" + n);

            selected_seat.setText(selected_seat.getText().toString() + "," + n);

        }


        //selected seat value

        customGridAdapter.notifyDataSetChanged();


        customGridAdapter.notifyDataSetChanged();
    }

    private void makeJsonObjectRequest() {


        System.out.println("method inside");

        StringRequest request = new StringRequest(Request.Method.GET,
                AppConfig.url_trip_details, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                System.out.println("inside res method");
                System.out.println("url Sleeper 32\t"+ AppConfig.url_trip_details);
                Log.d(TAG, response.toString());
                System.out.println("response final " + " : " + response.toString());
                try {


                    JSONObject json = new JSONObject(response);

                    JSONArray tripdetails = json.getJSONArray("TripDetails");

                    for (int i = 0; i < tripdetails.length(); i++) {

                        try {

                            JSONObject trip_object = (JSONObject) tripdetails.getJSONObject(i);

                            JSONArray seatdetail = trip_object.getJSONArray("SeatDetail");
                            System.out.println("Seatdetail" + seatdetail.length());

                            for (int j = 0; j < seatdetail.length(); j++) {

                                JSONObject seatdetail_object = seatdetail.getJSONObject(j);

                                String seatNbr = seatdetail_object.getString("seatNbr");

                                String seatStatus = seatdetail_object.getString("seatStatus");

                                int fare1 = seatdetail_object.getInt("fare");

                                int stax1 = seatdetail_object.getInt("stax");

                                AppConfig.fare1 = fare1;
                                AppConfig.tax1 = stax1;

                                System.out.println("seatNbr" + seatNbr);
                                System.out.println("seatStatus" + seatStatus);
                                System.out.println("fare1" + fare1);
                                System.out.println("stax1" + stax1);

                                /*((GlobalValues) Seat_Selection_Activity.this.getApplication()).setfare(fare1);

                                ((GlobalValues) Seat_Selection_Activity.this.getApplication()).settax(stax1);*/

                                Log.e(TAG, "Fare------------------------------------------->" + fare1);

                                Log.e(TAG, "stax------------------------------------------->" + stax1);


                                seat_no.add(seatNbr);
                                seat_status.add(seatStatus);
                                System.out.println("SeatStatus " + seat_status);
                            }
                            //for Loop Completed


                            //For loop selection

                            for (int j = 0; j < 32; j++){

                                String temp=seat_no.get(j);

                                Integer booked=Integer.parseInt(temp);

                                String seatstatus=seat_status.get(j);

                                Log.e(TAG,"seat_no------------->"+temp);

                                Log.e(TAG,"seat_status------------->"+seatstatus);


                                if(booked==1)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(7);
                                        gridArray.add(7, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(7);
                                        gridArray.add(7, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==2)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(6);
                                        gridArray.add(6, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(6);
                                        gridArray.add(6, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==3)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(4);
                                        gridArray.add(4, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(4);
                                        gridArray.add(4, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==7)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(11);
                                        gridArray.add(11, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(11);
                                        gridArray.add(11, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==8)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(10);
                                        gridArray.add(10, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(10);
                                        gridArray.add(10, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==9)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(8);
                                        gridArray.add(8, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(8);
                                        gridArray.add(8, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }

                                }
                                if(booked==13)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(15);
                                        gridArray.add(15, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(15);
                                        gridArray.add(15, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==14)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(14);
                                        gridArray.add(14, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(14);
                                        gridArray.add(14, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==15)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(12);
                                        gridArray.add(12, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(12);
                                        gridArray.add(12, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==19)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(19);
                                        gridArray.add(19, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(19);
                                        gridArray.add(19, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==20)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(18);
                                        gridArray.add(18, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(18);
                                        gridArray.add(18, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==21)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(16);
                                        gridArray.add(16, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(16);
                                        gridArray.add(16, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==25)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(23);
                                        gridArray.add(23, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(23);
                                        gridArray.add(23, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==26)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(22);
                                        gridArray.add(22, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(22);
                                        gridArray.add(22, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==27)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(20);
                                        gridArray.add(20, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(20);
                                        gridArray.add(20, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==31)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(25);
                                        gridArray.add(25, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(25);
                                        gridArray.add(25, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==4)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(32);
                                        gridArray.add(32, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(32);
                                        gridArray.add(32, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));

                                    }
                                }
                                if(booked==5)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(34);
                                        gridArray.add(34, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(34);
                                        gridArray.add(34, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==6)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(35);
                                        gridArray.add(35, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(35);
                                        gridArray.add(35, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==10)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(36);
                                        gridArray.add(36, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(36);
                                        gridArray.add(36, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==11)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(38);
                                        gridArray.add(38, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(38);
                                        gridArray.add(38, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==12)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(39);
                                        gridArray.add(39, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(39);
                                        gridArray.add(39, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }

                                if(booked==16)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(40);
                                        gridArray.add(40, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(40);
                                        gridArray.add(40, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }

                                if(booked==17)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(42);
                                        gridArray.add(42, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(42);
                                        gridArray.add(42, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==18)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(43);
                                        gridArray.add(43, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(43);
                                        gridArray.add(43, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==22)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(44);
                                        gridArray.add(44, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(44);
                                        gridArray.add(44, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }

                                if(booked==23)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(46);
                                        gridArray.add(46, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(46);
                                        gridArray.add(46, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }

                                if(booked==24)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(47);
                                        gridArray.add(47, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(47);
                                        gridArray.add(47, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==28)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(48);
                                        gridArray.add(48, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(48);
                                        gridArray.add(48, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==29)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(50);
                                        gridArray.add(50, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(50);
                                        gridArray.add(50, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==30)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(51);
                                        gridArray.add(51, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(51);
                                        gridArray.add(51, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }
                                if(booked==32)
                                {

                                    if(seatstatus.equalsIgnoreCase("A"))
                                    {

                                    }
                                    else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(53);
                                        gridArray.add(53, new Item(Booked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    }
                                    else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(53);
                                        gridArray.add(53, new Item(ladies_seat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    }
                                }

                            }


                        } catch (Exception e) {
                            // TODO: handle exception

                            Log.e(TAG, e.toString());
                        }

                    }
                    System.out.println("obj" + json);


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
                System.out.println("error" + error.toString());
                Toast.makeText(sleeper32_Activity.this, "no internet connection...!", Toast.LENGTH_SHORT).show();
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

}


