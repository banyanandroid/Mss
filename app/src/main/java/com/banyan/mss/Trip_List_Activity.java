package com.banyan.mss;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.Trip_List_Adapter;
import dmax.dialog.SpotsDialog;

public class Trip_List_Activity extends AppCompatActivity {

    String url;
    private static final int MY_SOCKET_TIMEOUT_MS = 0;
    private static final String TAG = "";
    public static final String TAG_TYPE = "type";
    public static final String TAG_FARE = "fare";
    public static final String TAG_PROVIDER = "provider";
    public static final String TAG_DEP_TIME = "departureTime";
    public static final String TAG_ARR_TIME = "arrivalTime";
    public static final String TAG_SEATS = "availableSeats";
    public static final String TAG_SHEDULEID = "scheduleId";
    public static final String TAG_BTYPE = "btype";
    public static final String TAG_OFFERAMOUNT = "offeramt";
    public static final String TAG_OFFERNAME = "offername";
    public static final String TAG_DROP_POINT = "DropingPointName";

    static ArrayList<HashMap<String, String>> trip_list;
    public static ArrayList<String> drop_list = null;

    Trip_List_Adapter adapter;
    ListView list;
    JSONArray trip2_arr;

    String type, departureTime, arrivalTime, availableSeats, fare, provider, scheduleId, btype, bustype, dropingPoint, description, offeramt, offername;
    int s_id;
    /*queue*/
    public static RequestQueue queue;
    /*progress bar*/
    //private ProgressDialog pDialog;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        // Hashmap for ListView
        trip_list = new ArrayList<HashMap<String, String>>();
        drop_list = new ArrayList<>();

        TextView dates = (TextView) findViewById(R.id.triplistA_txt_date);
        TextView from = (TextView) findViewById(R.id.triplistA_txt_from);
        TextView to = (TextView) findViewById(R.id.triplistA_txt_to);
        list = (ListView) findViewById(R.id.trip_list_recycler_view_bus);

        Bundle bundle = getIntent().getExtras();
        String value_str = bundle.getString("some_key");
        final String date_str = bundle.getString("date");
        final String from_str = bundle.getString("from");
        final String to_str = bundle.getString("to");
        final int from_id = Integer.parseInt(bundle.getString("from_id"));
        final int to_id = Integer.parseInt(bundle.getString("to_id"));
        dates.setText(date_str);
        from.setText(from_str);
        to.setText(to_str);
        url = value_str;
        System.out.printf(url);
        AppConfig.url_trip_list = value_str;

        try {
            dialog = new SpotsDialog(Trip_List_Activity.this);
            dialog.show();
            queue = Volley.newRequestQueue(Trip_List_Activity.this);
            makeJsonObjectRequest();

        } catch (Exception e) {
// TODO: handle exception
        }

        // ListView Item Click Listener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem

                System.out.println("inside click listener");
                String cost = ((TextView) view.findViewById(R.id.list_triplist_txt_offer_rate))
                        .getText().toString();

                String type = ((TextView) view.findViewById(R.id.list_triplist_txt_btype))
                        .getText().toString();

                System.out.println("inside click listener2");
                Log.e(TAG, "scheduleId--------------------->" + trip_list.get(position).get("scheduleId"));
                System.out.println("inside click listener3");

                bustype = trip_list.get(position).get("btype");

                System.out.println("inside click listener4" +bustype);
                System.out.println("inside click listener"+ trip_list);
                s_id = Integer.parseInt(trip_list.get(position).get("scheduleId"));

                AppConfig.schedule_Id = s_id;
                //Old Json
                //AppConfig.url_trip_details = "http://mssbus.com/api/?GetTripDetails&userId=mssapp&password=india44&fromStationId=" + from_id + "&toStationId=" + to_id + "&travelDate=" + date_str + "&scheduleId=" + s_id + "&format=json";
                AppConfig.url_trip_details = "http://www.mssbus.com/api/app/?GetTripDetails&Appkey=HaPpY&fromStationId="+from_id+"&toStationId="+to_id+"&travelDate="+date_str+"&scheduleId="+s_id+"&format=json";
                //old Json
                //AppConfig.url_boarding_list = "http://mssbus.com/api/?getStationPointDetails&userId=mssapp&password=india44&fromStationId=" + from_id + "&toStationId=" + to_id + "&scheduleId=" + s_id + "&format=json";
                AppConfig.url_boarding_list = "http://www.mssbus.com/api/app/?getStationPointDetails&Appkey=HaPpY&fromStationId="+from_id+"&toStationId="+to_id+"&scheduleId="+s_id+"&format=json";
                // Starting single temp activity

                //					if(type.contains("Semi Sleeper"))

                try {

                    if (type.contains("s36")) {
                        Intent in = new Intent(getApplicationContext(), Seat_Selection_Activity.class);
                        in.putExtra("from", AppConfig.from);
                        in.putExtra("to", AppConfig.to);
                        in.putExtra("fare", cost);
                        in.putExtra("month", AppConfig.month);
                        in.putExtra("day", AppConfig.day);
                        in.putExtra("year", AppConfig.year);
                        in.putExtra("scheduleId", s_id);
                        startActivity(in);
                        Log.e(TAG, String.valueOf(from_id));
                        Log.e(TAG, String.valueOf(to_id));
                        Log.e(TAG, String.valueOf(cost));
                        Log.e(TAG, String.valueOf(AppConfig.month));
                        Log.e(TAG, String.valueOf(AppConfig.day));
                        Log.e(TAG, String.valueOf(AppConfig.year));
                        Log.e(TAG, String.valueOf(s_id));
                        finish();

                        //Toast.makeText(getApplicationContext(),String.valueOf(scheduleId),Toast.LENGTH_SHORT).show();

                    } else if (type.contains("sl32")) {

                        Intent sleeper = new Intent(getApplicationContext(), sleeper32_Activity.class);
                        sleeper.putExtra("from", AppConfig.from);
                        sleeper.putExtra("to", AppConfig.to);
                        sleeper.putExtra("fare", cost);
                        sleeper.putExtra("month", AppConfig.month);
                        sleeper.putExtra("day", AppConfig.day);
                        sleeper.putExtra("year", AppConfig.year);
                        sleeper.putExtra("scheduleId", s_id);
                        startActivity(sleeper);
                        Log.e(TAG, String.valueOf(from_id));
                        Log.e(TAG, String.valueOf(to_id));
                        Log.e(TAG, String.valueOf(cost));
                        Log.e(TAG, String.valueOf(AppConfig.month));
                        Log.e(TAG, String.valueOf(AppConfig.day));
                        Log.e(TAG, String.valueOf(AppConfig.year));
                        Log.e(TAG, String.valueOf(s_id));
                        finish();
                        //Log.e(TAG,String.valueOf( fromid));
                        //Log.e(TAG,String.valueOf( toid));


                        //Toast.makeText(getApplicationContext(), type+"Seat design under development",Toast.LENGTH_SHORT).show();
                    } else if (type.contains("sl36")) {

                        Intent sleeper = new Intent(getApplicationContext(), Sleeper36_Activity.class);
                        sleeper.putExtra("from", AppConfig.from);
                        sleeper.putExtra("to", AppConfig.to);
                        sleeper.putExtra("fare", cost);
                        sleeper.putExtra("month", AppConfig.month);
                        sleeper.putExtra("day", AppConfig.day);
                        sleeper.putExtra("year", AppConfig.year);
                        sleeper.putExtra("scheduleId", s_id);
                        startActivity(sleeper);
                        Log.e(TAG, String.valueOf(from_id));
                        Log.e(TAG, String.valueOf(to_id));
                        Log.e(TAG, String.valueOf(cost));
                        Log.e(TAG, String.valueOf(AppConfig.month));
                        Log.e(TAG, String.valueOf(AppConfig.day));
                        Log.e(TAG, String.valueOf(AppConfig.year));
                        Log.e(TAG, String.valueOf(s_id));
                        finish();
                        //Log.e(TAG,String.valueOf( fromid));
                        //Log.e(TAG,String.valueOf( toid));


                        //Toast.makeText(getApplicationContext(), type+"Seat design under development",Toast.LENGTH_SHORT).show();
                    } else if (type.contains("sl30")) {

                        Intent sleeper = new Intent(getApplicationContext(), Sleeper30_Activity.class);
                        sleeper.putExtra("from", from_id);
                        sleeper.putExtra("to", to_id);
                        sleeper.putExtra("fare", cost);
                        sleeper.putExtra("month", AppConfig.month);
                        sleeper.putExtra("day", AppConfig.day);
                        sleeper.putExtra("year", AppConfig.year);
                        sleeper.putExtra("scheduleId", s_id);
                        startActivity(sleeper);
                        finish();
                        Log.e(TAG, String.valueOf(from_id));
                        Log.e(TAG, String.valueOf(to_id));
                        Log.e(TAG, String.valueOf(cost));
                        Log.e(TAG, String.valueOf(AppConfig.month));
                        Log.e(TAG, String.valueOf(AppConfig.day));
                        Log.e(TAG, String.valueOf(AppConfig.year));
                        Log.e(TAG, String.valueOf(s_id));


                        //Toast.makeText(getApplicationContext(), type+"Seat design under development",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }
        });
    }//oncreate completed

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent in = new Intent(Trip_List_Activity.this, Home_Activity.class);
        startActivity(in);
        finish();
    }

    //Json
    private void makeJsonObjectRequest() {

        System.out.println("method inside");

        StringRequest request = new StringRequest(Request.Method.GET,
                AppConfig.url_trip_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("inside res method");
                Log.d(TAG, response.toString());
                System.out.println("response final " + " : " + response.toString());
                try {

                    JSONObject obj = new JSONObject(response);
                    System.out.println("OBJECT" + " : " + obj);
                    JSONArray triplist_arr = obj.getJSONArray("tripList");
                    JSONObject trips_obj = (JSONObject) triplist_arr
                            .get(1);
                    // status = trips_obj.getJSONObject("Status");

                    String statusMessage = trips_obj.getString("StatusMessage");

                    if (statusMessage.equalsIgnoreCase("Success")) {

                        for (int i = 0; i < triplist_arr.length() - 1; i++) {

                            JSONObject trip1_obj = (JSONObject) triplist_arr
                                    .get(i);

                            System.out.println("OBJECT 2" + " : " + trip1_obj);

                            trip2_arr = trip1_obj.getJSONArray("trips");

                            System.out.println("OBJECT 3" + " : " + trip2_arr);

                            for (int j = 0; j < trip2_arr.length(); j++) {
                                JSONObject trip3_obj = (JSONObject) trip2_arr
                                        .get(j);
                                JSONObject trip = trip3_obj
                                        .getJSONObject("trip");

                                type = trip.getString(TAG_TYPE);
                                btype = trip.getString(TAG_BTYPE);
                                fare = trip.getString(TAG_FARE);
                                scheduleId = trip.getString(TAG_SHEDULEID);
                                provider = trip.getString(TAG_PROVIDER);
                                availableSeats = trip.getString(TAG_SEATS);
                                arrivalTime = trip.getString(TAG_ARR_TIME);
                                departureTime = trip.getString(TAG_DEP_TIME);
                                offeramt = trip.getString(TAG_OFFERAMOUNT);
                                offername = trip.getString(TAG_OFFERNAME);

                                HashMap<String, String> map = new HashMap<String, String>();


                                JSONArray DropingPoint_arr = trip.getJSONArray("DropingPoint");

                                for(int k=0; k<DropingPoint_arr.length(); k++){

                                    JSONObject trip5_obj = (JSONObject) DropingPoint_arr
                                            .get(k);
                                    dropingPoint = trip5_obj.getString(TAG_DROP_POINT);

                                    drop_list.add(dropingPoint);

                                }

                                System.out.println("Droplist" + drop_list);

                                // adding each child node to HashMap key => value
                                map.put(TAG_BTYPE, btype);
                                map.put(TAG_TYPE, type);
                                map.put(TAG_FARE, fare);
                                map.put(TAG_PROVIDER, provider);
                                map.put(TAG_SEATS, availableSeats);
                                map.put(TAG_ARR_TIME, arrivalTime);
                                map.put(TAG_DEP_TIME, departureTime);
                                map.put(TAG_SHEDULEID, scheduleId);
                                map.put(TAG_OFFERAMOUNT, offeramt);
                                map.put(TAG_OFFERNAME, offername);

                                trip_list.add(map);

                                System.out.println("HASHMAP ARRAY" + trip_list);

                                System.out.println("btype" + btype);
                                System.out.println("fare" + fare);
                                System.out.println("availableSeats" + availableSeats);
                                System.out.println("provider" + provider);

                            }

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "No Bus facility on that date", Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("obj" + obj);

                    /*String s = obj.getString("success");
                    int success = Integer.parseInt(s);

                    System.out.println("success" + success);*/
                    adapter = new Trip_List_Adapter(Trip_List_Activity.this,
                            trip_list,drop_list);
                    list.setAdapter(adapter);


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
                Toast.makeText(Trip_List_Activity.this, "no internet connection...!", Toast.LENGTH_SHORT).show();
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
