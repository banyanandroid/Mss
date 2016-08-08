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
import Adapter.Trip_List_Adapter;
import dmax.dialog.SpotsDialog;

public class Bookdetail_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String url;
    private static final int MY_SOCKET_TIMEOUT_MS = 0;
    private static final String TAG = "";
    public static final String TAG_PNR = "pnr";
    public static final String TAG_EDATE = "edate";
    public static final String TAG_ETIME = "etime";
    public static final String TAG_EFROM = "efrom";
    public static final String TAG_ETO = "eto";
    public static final String TAG_ENOS = "enos";
    public static final String TAG_ESTATUS = "estatus";

    static ArrayList<HashMap<String, String>> detail_list;

    Book_Detail_List_Adapter adapter;
    ListView list;
    JSONArray Tickets;

    SessionManager session;
    String str_id, str_mobile, str_name;

    String pnr, edate, etime, efrom, eto, enos, estatus;

    /*queue*/
    public static RequestQueue queue;
    /*progress bar*/
    //private ProgressDialog pDialog;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookdetail);

        // Hashmap for ListView
        detail_list = new ArrayList<HashMap<String, String>>();

        list = (ListView) findViewById(R.id.book_detail_recycler_view_bus);

        //session
        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        str_id = user.get(SessionManager.KEY_ID);
        str_mobile = user.get(SessionManager.KEY_MOBILE);
        str_name = user.get(SessionManager.KEY_LOGINID);

        url = "http://www.mssbus.com/api/app?GetTickets&Appkey=HaPpY&UserId=" + str_id + "&UserMobile=" + str_mobile;
        System.out.printf(url);


        try {
            dialog = new SpotsDialog(Bookdetail_Activity.this);
            dialog.show();
            queue = Volley.newRequestQueue(Bookdetail_Activity.this);
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
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
        });
    }//oncreate completed

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            Intent in = new Intent(Bookdetail_Activity.this, Home_Activity.class);
            startActivity(in);
            finish();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bookticket) {
            Intent ins = new Intent(Bookdetail_Activity.this,Home_Activity.class);
            startActivity(ins);
        } else if (id == R.id.nav_cancelticket) {

        } else if (id == R.id.nav_bookdetails) {
            Intent in = new Intent(Bookdetail_Activity.this, Bookdetail_Activity.class);
            startActivity(in);

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_about) {

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
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("inside res method");
                Log.d(TAG, response.toString());
                System.out.println("response final " + " : " + response.toString());
                try {

                    JSONObject obj = new JSONObject(response);
                    System.out.println("OBJECT" + " : " + obj);
                    JSONArray tickets = obj.getJSONArray("Tickets");
                    JSONObject status_obj = (JSONObject) tickets
                            .get(tickets.length() - 1);
                    // status = trips_obj.getJSONObject("Status");

                    String statusMessage = status_obj.getString("StatusMessage");

                    if (statusMessage.equalsIgnoreCase("Success")) {

                        for (int i = 0; i < tickets.length() - 1; i++) {

                            JSONObject detail = (JSONObject) tickets
                                    .get(i);

                            System.out.println("OBJECT 2" + " : " + detail);

                            pnr = detail.getString(TAG_PNR);
                            edate = detail.getString(TAG_EDATE);
                            etime = detail.getString(TAG_ETIME);
                            efrom = detail.getString(TAG_EFROM);
                            eto = detail.getString(TAG_ETO);
                            enos = detail.getString(TAG_ENOS);
                            estatus = detail.getString(TAG_ESTATUS);


                            HashMap<String, String> map = new HashMap<String, String>();


                            // adding each child node to HashMap key => value
                            map.put(TAG_PNR, pnr);
                            map.put(TAG_EDATE, edate);
                            map.put(TAG_ETIME, etime);
                            map.put(TAG_EFROM, efrom);
                            map.put(TAG_ETO, eto);
                            map.put(TAG_ENOS, enos);
                            map.put(TAG_ESTATUS, estatus);

                            detail_list.add(map);

                            System.out.println("HASHMAP ARRAY" + detail_list);

                            System.out.println("pnr" + pnr);
                            System.out.println("date" + edate);
                            System.out.println("from" + efrom);
                            System.out.println("to" + eto);

                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "No Tickets Available", Toast.LENGTH_SHORT).show();
                    }
                    System.out.println("obj" + obj);

                    /*String s = obj.getString("success");
                    int success = Integer.parseInt(s);

                    System.out.println("success" + success);*/
                    adapter = new Book_Detail_List_Adapter(Bookdetail_Activity.this,
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
                Toast.makeText(Bookdetail_Activity.this, "no internet connection...!", Toast.LENGTH_SHORT).show();
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
