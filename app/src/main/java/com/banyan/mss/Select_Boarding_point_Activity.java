package com.banyan.mss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import Adapter.Trip_List_Adapter;

public class Select_Boarding_point_Activity extends ListActivity {

    final String TAG = "Select_Boarding_point.java";
    private static final int MY_SOCKET_TIMEOUT_MS = 0;
    ListAdapter adapter;
    int fromid,toid;
    String day,month,year;
    int scheduleId;
    ArrayList<String> seats=new ArrayList<String>();
    ArrayList<String> ladies=new ArrayList<String>();
    JSONArray stationpointlist;
    String address;
    /*queue*/
    public static RequestQueue queue;
    /*progress bar*/
    private ProgressDialog pDialog;

    int boarding_id;

    ArrayList<HashMap<String, String>>Boarindglist=new ArrayList<HashMap<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_boarding_point);

        /*String from=((GlobalValues)this.getApplication()).getfrom();

        String to=((GlobalValues)this.getApplication()).getto();

        String traveldate=((GlobalValues)this.getApplication()).getdate();*/

        /*ActionBar actionBar = getActionBar();

        actionBar.setTitle(from+" to "+to);

        actionBar.setSubtitle(traveldate);*/

        TextView dates = (TextView) findViewById(R.id.triplistA_txt_date);
        TextView from = (TextView) findViewById(R.id.triplistA_txt_from);
        TextView to = (TextView) findViewById(R.id.triplistA_txt_to);

        dates.setText(AppConfig.date);
        from.setText(AppConfig.from);
        to.setText(AppConfig.to);

        seats= getIntent().getStringArrayListExtra("seats");

        Log.e(TAG, "seats from boarding point"+seats);

        ladies= getIntent().getStringArrayListExtra("ladies");

        try {
            pDialog = new ProgressDialog(Select_Boarding_point_Activity.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();
            pDialog.setCancelable(false);
            queue = Volley.newRequestQueue(Select_Boarding_point_Activity.this);
            makeJsonObjectRequest();

        } catch (Exception e) {
// TODO: handle exception
        }

        @SuppressWarnings("unchecked")
        ListView lv=getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parnet, android.view.View view,
                                    int position, long id) {


                boarding_id=Integer.parseInt(Boarindglist.get(position).get("stationPointId"));

                address=Boarindglist.get(position).get("address");

                String boardingname=Boarindglist.get(position).get("boardingpointname");



                ((GlobalValues)Select_Boarding_point_Activity.this.getApplication()).setaddress(address);

                ((GlobalValues)Select_Boarding_point_Activity.this.getApplication()).setboardingname(boardingname);


                Log.e(TAG, "address--------------)"+address);

                if(seats.size()==1)
                {
                    Intent in=new Intent(Select_Boarding_point_Activity.this,Passenger_details_Activity.class);

                    in.putExtra("s_id", scheduleId);

                    in.putExtra("boarding_id",boarding_id);

                    in.putExtra("month",month);

                    in.putExtra("day",day);

                    in.putExtra("year",year);

                    in.putExtra("fid", fromid);

                    in.putExtra("tid",toid);

                    in.putStringArrayListExtra("seats",seats);

                    in.putStringArrayListExtra("ladies",ladies);

                    startActivity(in);

                }


                else if(seats.size()==2)
                {
                    Intent in=new Intent(Select_Boarding_point_Activity.this,DoubleContact_Activity.class);

                    in.putExtra("s_id", scheduleId);

                    in.putExtra("boarding_id",boarding_id);

                    in.putExtra("month",month);

                    in.putExtra("day",day);

                    in.putExtra("year",year);

                    in.putExtra("fid", fromid);

                    in.putExtra("tid",toid);

                    in.putStringArrayListExtra("seats",seats);

                    in.putStringArrayListExtra("ladies",ladies);

                    startActivity(in);

                }


                else if(seats.size()==3)
                {
                    Intent in=new Intent(Select_Boarding_point_Activity.this,ThreeContact_Activity.class);

                    in.putExtra("s_id", scheduleId);

                    in.putExtra("boarding_id",boarding_id);

                    in.putExtra("month",month);

                    in.putExtra("day",day);

                    in.putExtra("year",year);

                    in.putExtra("fid", fromid);

                    in.putExtra("tid",toid);

                    in.putStringArrayListExtra("seats",seats);

                    in.putStringArrayListExtra("ladies",ladies);

                    startActivity(in);

                }

                else if(seats.size()==4)
                {
                    Intent in=new Intent(Select_Boarding_point_Activity.this,FourContact_Activity.class);

                    in.putExtra("s_id", scheduleId);

                    in.putExtra("boarding_id",boarding_id);

                    in.putExtra("month",month);

                    in.putExtra("day",day);

                    in.putExtra("year",year);

                    in.putExtra("fid", fromid);

                    in.putExtra("tid",toid);

                    in.putStringArrayListExtra("seats",seats);

                    in.putStringArrayListExtra("ladies",ladies);

                    startActivity(in);

                }

                else if(seats.size()==5)
                {
                    Intent in=new Intent(Select_Boarding_point_Activity.this,FifthContact_Activity.class);

                    in.putExtra("s_id", scheduleId);

                    in.putExtra("boarding_id",boarding_id);

                    in.putExtra("month",month);

                    in.putExtra("day",day);

                    in.putExtra("year",year);

                    in.putExtra("fid", fromid);

                    in.putExtra("tid",toid);

                    in.putStringArrayListExtra("seats",seats);

                    in.putStringArrayListExtra("ladies",ladies);

                    startActivity(in);

                }

                else if(seats.size()==6)
                {
                    Intent in=new Intent(Select_Boarding_point_Activity.this,SixContact_Activity.class);

                    in.putExtra("s_id", scheduleId);

                    in.putExtra("boarding_id",boarding_id);

                    in.putExtra("month",month);

                    in.putExtra("day",day);

                    in.putExtra("year",year);

                    in.putExtra("fid", fromid);

                    in.putExtra("tid",toid);

                    in.putStringArrayListExtra("seats",seats);

                    in.putStringArrayListExtra("ladies",ladies);

                    startActivity(in);

                }



            }

        });


        fromid=getIntent().getExtras().getInt("fid");

        toid=getIntent().getExtras().getInt("tid");

        scheduleId=getIntent().getExtras().getInt("sid");

        day=getIntent().getExtras().getString("day");

        month=getIntent().getExtras().getString("month");

        year=getIntent().getExtras().getString("year");


        Log.e(TAG, fromid+ ""+ toid +""+scheduleId);

        //new AsyncTaskParseJson_boarding().execute();

    }

    private void makeJsonObjectRequest() {

        System.out.println("method inside");

        StringRequest request = new StringRequest(Request.Method.GET,
                AppConfig.url_boarding_list, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("inside res method");
                Log.d(TAG, response.toString());
                System.out.println("response final " + " : " + response.toString());
                try {

                    JSONObject json = new JSONObject(response);
                    System.out.println("OBJECT" + " : " + json);

                    // get the array of users

                    stationpointlist = json.getJSONArray("stationPointList");

                    for (int i = 0; i < stationpointlist.length(); i++)
                    {

                        JSONObject stationpointlist_object=stationpointlist.getJSONObject(i);

                        JSONObject StationsPointDetails=stationpointlist_object.getJSONObject("StationsPointDetails");

                        //Log.e(TAG,StationsPointDetails.getString("stationPointType"));

                        if(StationsPointDetails.getString("stationPointType").equalsIgnoreCase("B"))
                        {

                            HashMap<String, String> b = new HashMap<String, String>();

                            b.put("boardingpointname", StationsPointDetails.getString("stationPointName"));

                            b.put("address", StationsPointDetails.getString("address"));

                            Log.e(TAG,"address"+StationsPointDetails.getString("address"));//print logcat

                            b.put("contactNo", StationsPointDetails.getString("contactNo"));

                            Log.e(TAG,"contactNo"+StationsPointDetails.getString("contactNo"));//print logcat

                            b.put("stationPointId",StationsPointDetails.getString("stationPointId"));

                            Log.e(TAG, "StationId"+StationsPointDetails.get("stationPointId"));//print logcat

                            b.put("time",StationsPointDetails.getString("time"));

                            Boarindglist.add(b);
                        }
                    }
                    System.out.println("Boarindglist"+ Boarindglist);
                    Toast.makeText(getApplicationContext(),"select your boarding point",Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                    adapter = new SimpleAdapter(
                            Select_Boarding_point_Activity.this, Boarindglist,
                            R.layout.boarding_list, new String[] {"boardingpointname","time"},new int[]{
                            R.id.boardingname,R.id.time});
                    setListAdapter(adapter);

                } catch (Exception e) {

// TODO Auto-generated catch block
                    System.out.println("inside catch" + e);
                    e.printStackTrace();
                }
                pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error" + error.toString());
                Toast.makeText(Select_Boarding_point_Activity.this, "no internet connection...!", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
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
