package com.banyan.mss;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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

import Adapter.Book_Detail_List_Adapter;
import Adapter.Ticket_Detail_List_Adapter;
import Adapter.Trip_List_Adapter;
import dmax.dialog.SpotsDialog;

public class Ticketdetail_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final int MY_SOCKET_TIMEOUT_MS = 0;
    private static final String TAG = "";
    public static final String TAG_BD = "bd";
    public static final String TAG_TIME = "bd_time";
    public static final String TAG_SEATNO = "bd_seatno";
    public static final String TAG_NAME = "bd_name";
    public static final String TAG_SEX = "bd_sex";
    public static final String TAG_AGE = "bd_age";
    public static final String TAG_FAMT = "bd_famt";
    public static final String TAG_SAMT = "bd_samt";
    public static final String TAG_AMT = "bd_amt";
    public static final String TAG_STATUS = "bd_status";

    static ArrayList<HashMap<String, String>> detail_list;

    Ticket_Detail_List_Adapter adapter;
    ListView list;
    JSONArray Tickets;

    SessionManager session;
    String str_id, str_mobile, str_name;

    String pnr, edate, etime, efrom, eto, enos, estatus;
    String bd,bd_time,bd_seatno,bd_name,bd_sex,bd_age,bd_famt,bd_samt,bd_amt,bd_status;
    ImageView back;

    /*queue*/
    public static RequestQueue queue;
    /*progress bar*/
    //private ProgressDialog pDialog;
    private AlertDialog dialog;

    TextView txt_etime,txt_to,txt_from,txt_pnr,txt_date,txt_nos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketdetail);

        txt_etime = (TextView) findViewById(R.id.ticket_txt_etime);
        txt_date = (TextView) findViewById(R.id.ticket_txt_edate);
        txt_to = (TextView) findViewById(R.id.ticket_txt_eto);
        txt_from = (TextView) findViewById(R.id.ticket_txt_efrom);
        txt_pnr = (TextView) findViewById(R.id.ticket_txt_pnr);
        txt_nos = (TextView) findViewById(R.id.ticket_txt_enos);

        back = (ImageView) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Ticketdetail_Activity.this, Bookdetail_Activity.class);
                startActivity(in);
            }
        });


        // Hashmap for ListView
        detail_list = new ArrayList<HashMap<String, String>>();

        list = (ListView) findViewById(R.id.ticket_detail_recycler_view_bus);

        //session
        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        str_id = user.get(SessionManager.KEY_ID);
        str_mobile = user.get(SessionManager.KEY_MOBILE);
        str_name = user.get(SessionManager.KEY_LOGINID);

        try {
            dialog = new SpotsDialog(Ticketdetail_Activity.this);
            dialog.show();
            queue = Volley.newRequestQueue(Ticketdetail_Activity.this);
            makeJsonObjectRequest();

        } catch (Exception e) {
// TODO: handle exception
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ListView Item Click Listener
        /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem

                System.out.println("inside click listener");
                String pnr = ((TextView) view.findViewById(R.id.list_detaillist_txt_pnr))
                        .getText().toString();

                Toast.makeText(getApplicationContext(), pnr, Toast.LENGTH_SHORT).show();

                AppConfig.ticket_detail_url = "ttp://www.mssbus.com/api/app?TicketDetails&Appkey=HaPpY&UserId=" + str_id + "&UserMobile=" + str_mobile + "&pnr=" + pnr + "&format=xml";

                System.out.println("inside click listener2");
                Log.e(TAG, "scheduleId--------------------->" + detail_list.get(position).get("pnr"));

            }
        });*/
    }//oncreate completed

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            Intent in = new Intent(Ticketdetail_Activity.this, Bookdetail_Activity.class);
            startActivity(in);
            finish();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bookticket) {
            Intent ins = new Intent(Ticketdetail_Activity.this,Home_Activity.class);
            startActivity(ins);
        } else if (id == R.id.nav_cancelticket) {

        } else if (id == R.id.nav_bookdetails) {
            Intent in = new Intent(Ticketdetail_Activity.this, Ticketdetail_Activity.class);
            startActivity(in);

        } else if (id == R.id.nav_about) {
            Intent in = new Intent(Ticketdetail_Activity.this, Aboutus_Activity.class);
            startActivity(in);

        } else if (id == R.id.nav_logout) {

            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Json
    private void makeJsonObjectRequest() {

        System.out.println("method inside");

        StringRequest request = new StringRequest(Request.Method.GET,
                AppConfig.ticket_detail_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("inside res method");
                Log.d(TAG, response.toString());
                System.out.println("response final " + " : " + response.toString());
                try {

                    JSONObject obj = new JSONObject(response);
                    System.out.println("OBJECT" + " : " + obj);
                    JSONArray tickets = obj.getJSONArray("Ticket");
                    JSONObject status_obj = (JSONObject) tickets
                            .get(tickets.length() - 1);
                    // status = trips_obj.getJSONObject("Status");

                    String statusMessage = status_obj.getString("StatusMessage");

                    if (statusMessage.equalsIgnoreCase("Success")) {

                        for(int i=0 ; i<1; i++){

                            JSONObject detail = (JSONObject) tickets
                                    .get(i);

                            pnr = detail.getString("pnr");
                            edate = detail.getString("edate");
                            etime = detail.getString("etime");
                            efrom = detail.getString("efrom");
                            eto = detail.getString("eto");
                            enos = detail.getString("enos");
                            estatus = detail.getString("estatus");

                            txt_etime.setText(etime);
                            txt_to.setText(eto);
                            txt_from.setText(efrom);
                            txt_pnr.setText(pnr);
                            txt_date.setText(edate);
                            txt_nos.setText(enos);
                        }
                        JSONObject tdetail = (JSONObject) tickets
                                .get(1);
                        JSONArray ticketDetails = tdetail.getJSONArray("TicketDetails");

                        for (int i = 0; i < ticketDetails.length(); i++) {

                            JSONObject values = (JSONObject) ticketDetails.get(i);

                            System.out.println("OBJECT 2" + " : " + tdetail);
                            bd = values.getString(TAG_BD);
                            bd_time = values.getString(TAG_TIME);
                            bd_seatno = values.getString(TAG_SEATNO);
                            bd_name = values.getString(TAG_NAME);
                            bd_sex = values.getString(TAG_SEX);
                            bd_status = values.getString(TAG_STATUS);
                            bd_age = values.getString(TAG_AGE);
                            bd_famt = values.getString(TAG_FAMT);
                            bd_samt = values.getString(TAG_SAMT);
                            bd_amt = values.getString(TAG_AMT);

                            HashMap<String, String> map = new HashMap<String, String>();


                            // adding each child node to HashMap key => value
                            map.put(TAG_BD, bd);
                            map.put(TAG_TIME, bd_time);
                            map.put(TAG_SEATNO, bd_seatno);
                            map.put(TAG_NAME, bd_name);
                            map.put(TAG_SEX, bd_sex);
                            map.put(TAG_AGE, bd_age);
                            map.put(TAG_FAMT, bd_famt);
                            map.put(TAG_SAMT, bd_samt);
                            map.put(TAG_AMT, bd_amt);
                            map.put(TAG_STATUS, bd_status);

                            detail_list.add(map);

                            System.out.println("HASHMAP ARRAY" + detail_list);

                            System.out.println("pnr" + pnr);
                            System.out.println("date" + edate);
                            System.out.println("from" + efrom);
                            System.out.println("to" + eto);
                            System.out.println("TAG_BD" + TAG_BD);
                            System.out.println("TAG_SEATNO" + TAG_SEATNO);
                            System.out.println("TAG_AMT" + TAG_AMT);
                            System.out.println("TAG_STATUS" + TAG_STATUS);

                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "No Tickets Available", Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("obj" + obj);

                    /*String s = obj.getString("success");
                    int success = Integer.parseInt(s);

                    System.out.println("success" + success);*/
                    adapter = new Ticket_Detail_List_Adapter(Ticketdetail_Activity.this,
                            detail_list);
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
                Toast.makeText(Ticketdetail_Activity.this, "no internet connection...!", Toast.LENGTH_SHORT).show();
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
