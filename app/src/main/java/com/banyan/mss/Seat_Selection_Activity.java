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

import Adapter.Trip_List_Adapter;
import dmax.dialog.SpotsDialog;


public class Seat_Selection_Activity extends Activity implements OnItemClickListener {

    GridView gridView;
    ArrayList<Item> gridArray = new ArrayList<Item>();
    ArrayList<String> seat_id = new ArrayList<String>();
    CustomGridViewAdapter customGridAdapter;
    public Bitmap seatIcon, driver;
    public Bitmap seatSelect;
    public Bitmap seatbooked;
    public Bitmap ladiesseat;
    public Bitmap genseat;
    String fare, day, month, seat_count;
    int year, fromid, toid;
    int scheduleId;
    Float temp, temp3, balance = 0f, ticketrate;
    TextView cost, selected_seat;
    Bitmap seatcompare;
    Button done;
    private AlertDialog dialog;
    int p,g;
    int swap = 1;
    Integer count = 0;
    final String TAG = "Seat_Selection.java";
    ProgressDialog mProgressDialog;
    ArrayList<String> seat_status = new ArrayList<String>();
    ArrayList<String> seat_no = new ArrayList<String>();
    ArrayList<String> ladies = new ArrayList<String>();
    ArrayList<String> gents = new ArrayList<String>();

    /*queue*/
    public static RequestQueue queue;
    /*progress bar*/
    //private ProgressDialog pDialog;

    String url;
    private static final int MY_SOCKET_TIMEOUT_MS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        String from = "Coimbatore";//((GlobalValues)this.getApplication()).getfrom();

        String to = "Chennai";//((GlobalValues)this.getApplication()).getto();

        String traveldate = "13/06/2016";//((GlobalValues)this.getApplication()).getdate();

        /*ActionBar actionBar = getActionBar();

        actionBar.setTitle(from + " to " + to);

        actionBar.setSubtitle(traveldate);*/

        //retriving value from the selec_bus class

        fare = getIntent().getExtras().getString("fare");
        day = getIntent().getExtras().getString("day");
        month = getIntent().getExtras().getString("month");
        year = getIntent().getExtras().getInt("year");
        fromid = getIntent().getExtras().getInt("from");
        toid = getIntent().getExtras().getInt("to");
        scheduleId = getIntent().getExtras().getInt("scheduleId");

        ticketrate = Float.valueOf(fare);

        // Log.e(TAG,"scheduleId--------->"+scheduleId);

        //end
        //Toast.makeText(getApplicationContext(), fare,Toast.LENGTH_SHORT).show();//display fare.
        cost = (TextView) findViewById(R.id.amount);

        selected_seat = (TextView) findViewById(R.id.selected_seat);

        seatIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_nor_avl);

        seatbooked = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_nor_bkd);

        ladiesseat = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_nor_lad_avl);

        genseat = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_nor_gen_avl);

        driver = BitmapFactory.decodeResource(this.getResources(), R.drawable.steering_icon);

        seatSelect = BitmapFactory.decodeResource(this.getResources(), R.drawable.seat_layout_screen_nor_std);

        gridView = (GridView) findViewById(R.id.gridView1);

        customGridAdapter = new CustomGridViewAdapter(this, R.layout.seatrow_grid, gridArray);

        gridView.setAdapter(customGridAdapter);

        gridView.setOnItemClickListener(this);

        // new AsyncTaskParseJson_seat_selection().execute();

        totalSeat(50);

        done = (Button) findViewById(R.id.done_s);

        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (seat_id.size() == 0) {

                    Toast.makeText(getApplicationContext(), "Please select a seat", Toast.LENGTH_SHORT).show();
                } else {
                    int ticketcount = seat_id.size();
                    AppConfig.ticket_count = ticketcount;

                    //((GlobalValues) Seat_Selection_Activity.this.getApplication()).setcount(ticketcount);

                    Intent in = new Intent(Seat_Selection_Activity.this, Select_Boarding_point_Activity.class);
                    in.putStringArrayListExtra("seats", seat_id);
                    in.putStringArrayListExtra("ladies", ladies);
                    in.putStringArrayListExtra("gents", gents);
                    in.putExtra("fid", fromid);
                    in.putExtra("tid", toid);
                    in.putExtra("sid", scheduleId);
                    in.putExtra("month", month);
                    in.putExtra("day", day);
                    in.putExtra("year", year);

                    startActivity(in);
                    Log.e(TAG, String.valueOf(seat_id));

                }

            }
        });

        try {
            dialog = new SpotsDialog(Seat_Selection_Activity.this);
            dialog.show();
            queue = Volley.newRequestQueue(Seat_Selection_Activity.this);
            makeJsonObjectRequest();

        } catch (Exception e) {
// TODO: handle exception
        }


    }

    public void totalSeat(int n) {
        for (int i = 1; i <= n; ++i) {
            if (i == 1 || i == 2 || i == 3 || i == 4 || i == 7 || i == 8 || i == 13 || i == 18 || i == 23 || i == 28 || i == 33 || i == 38 || i == 43 || i == 48) {
                gridArray.add(new Item(null, "" + ""));
            } else if (i == 5) {
                gridArray.add(new Item(driver, "" + ""));
            } else {
                gridArray.add(new Item(seatIcon, "" + ""));
            }
        }
    }

    public void seatSelected(int pos) {
        // Log.e(TAG,"seat position-------------------------------->"+String.valueOf(pos));

        gridArray.remove(pos);
        gridArray.add(pos, new Item(seatSelect, ""));
        String c = cost.getText().toString();
        String[] split = c.split("₹");
        balance = Float.valueOf(split[1]) + ticketrate;
        cost.setText("₹" + String.valueOf(balance));
        //Log.e(TAG,"balance------------------------------------->"+split[1] );
        balance = 0f;
        //Log.e(TAG,"--------------------------------------");
        count++;

        seat_count = selected_seat.getText().toString();
        //selected seat value

        if (seat_count.isEmpty()) {
            if (pos == 9) {
                seat_id.add(String.valueOf(1));
                selected_seat.setText(String.valueOf(1));
            }
            if (pos == 8) {
                seat_id.add(String.valueOf(2));
                selected_seat.setText(String.valueOf(2));
            }
            if (pos == 5) {
                seat_id.add(String.valueOf(4));
                selected_seat.setText(String.valueOf(4));
            }
            if (pos == 10) {
                seat_id.add(String.valueOf(5));
                selected_seat.setText(String.valueOf(5));
            }
            if (pos == 11) {
                seat_id.add(String.valueOf(6));
                selected_seat.setText(String.valueOf(6));
            }
            if (pos == 13) {
                seat_id.add(String.valueOf(7));
                selected_seat.setText(String.valueOf(7));
            }
            if (pos == 14) {
                seat_id.add(String.valueOf(8));
                selected_seat.setText(String.valueOf(8));
            }
            if (pos == 19) {
                seat_id.add(String.valueOf(9));
                selected_seat.setText(String.valueOf(9));
            }
            if (pos == 18) {
                seat_id.add(String.valueOf(10));
                selected_seat.setText(String.valueOf(10));
            }
            if (pos == 16) {
                seat_id.add(String.valueOf(11));
                selected_seat.setText(String.valueOf(11));
            }
            if (pos == 15) {
                seat_id.add(String.valueOf(12));
                selected_seat.setText(String.valueOf(12));
            }
            if (pos == 20) {
                seat_id.add(String.valueOf(13));
                selected_seat.setText(String.valueOf(13));
            }
            if (pos == 21) {
                seat_id.add(String.valueOf(14));
                selected_seat.setText(String.valueOf(14));
            }
            if (pos == 23) {
                seat_id.add(String.valueOf(15));
                selected_seat.setText(String.valueOf(15));
            }
            if (pos == 24) {
                seat_id.add(String.valueOf(16));
                selected_seat.setText(String.valueOf(16));
            }
            if (pos == 29) {
                seat_id.add(String.valueOf(17));
                selected_seat.setText(String.valueOf(17));
            }
            if (pos == 28) {
                seat_id.add(String.valueOf(18));
                selected_seat.setText(String.valueOf(18));
            }
            if (pos == 26) {
                seat_id.add(String.valueOf(19));
                selected_seat.setText(String.valueOf(19));
            }
            if (pos == 25) {
                seat_id.add(String.valueOf(20));
                selected_seat.setText(String.valueOf(20));
            }
            if (pos == 30) {
                seat_id.add(String.valueOf(21));
                selected_seat.setText(String.valueOf(21));
            }
            if (pos == 31) {
                seat_id.add(String.valueOf(22));
                selected_seat.setText(String.valueOf(22));
            }
            if (pos == 33) {
                seat_id.add(String.valueOf(23));
                selected_seat.setText(String.valueOf(23));
            }
            if (pos == 34) {
                seat_id.add(String.valueOf(24));
                selected_seat.setText(String.valueOf(24));
            }
            if (pos == 39) {
                seat_id.add(String.valueOf(25));
                selected_seat.setText(String.valueOf(25));
            }
            if (pos == 38) {
                seat_id.add(String.valueOf(26));
                selected_seat.setText(String.valueOf(26));
            }
            if (pos == 36) {
                seat_id.add(String.valueOf(27));
                selected_seat.setText(String.valueOf(27));
            }
            if (pos == 35) {
                seat_id.add(String.valueOf(28));
                selected_seat.setText(String.valueOf(28));
            }
            if (pos == 40) {
                seat_id.add(String.valueOf(29));
                selected_seat.setText(String.valueOf(29));
            }
            if (pos == 41) {
                seat_id.add(String.valueOf(30));
                selected_seat.setText(String.valueOf(30));
            }
            if (pos == 43) {
                seat_id.add(String.valueOf(31));
                selected_seat.setText(String.valueOf(31));
            }
            if (pos == 44) {
                seat_id.add(String.valueOf(32));
                selected_seat.setText(String.valueOf(32));
            }
            if (pos == 49) {
                seat_id.add(String.valueOf(33));
                selected_seat.setText(String.valueOf(33));
            }
            if (pos == 48) {
                seat_id.add(String.valueOf(34));
                selected_seat.setText(String.valueOf(34));
            }
            if (pos == 46) {
                seat_id.add(String.valueOf(35));
                selected_seat.setText(String.valueOf(35));
            }
            if (pos == 45) {
                seat_id.add(String.valueOf(36));
                selected_seat.setText(String.valueOf(36));
            }

        } else {

            if (pos == 9) {
                seat_id.add(String.valueOf(1));
                selected_seat.setText(seat_count + "," + 1);
            }
            if (pos == 8) {
                seat_id.add(String.valueOf(2));
                selected_seat.setText(seat_count + "," + 2);
            }
            if (pos == 5) {
                seat_id.add(String.valueOf(4));
                selected_seat.setText(seat_count + "," + 4);
            }
            if (pos == 10) {
                seat_id.add(String.valueOf(5));
                selected_seat.setText(seat_count + "," + 5);
            }
            if (pos == 11) {
                seat_id.add(String.valueOf(6));
                selected_seat.setText(seat_count + "," + 6);
            }
            if (pos == 13) {
                seat_id.add(String.valueOf(7));
                selected_seat.setText(seat_count + "," + 7);
            }
            if (pos == 14) {
                seat_id.add(String.valueOf(8));
                selected_seat.setText(seat_count + "," + 8);
            }
            if (pos == 19) {
                seat_id.add(String.valueOf(9));
                selected_seat.setText(seat_count + "," + 9);
            }
            if (pos == 18) {
                seat_id.add(String.valueOf(10));
                selected_seat.setText(seat_count + "," + 10);
            }
            if (pos == 16) {
                seat_id.add(String.valueOf(11));
                selected_seat.setText(seat_count + "," + 11);
            }
            if (pos == 15) {
                seat_id.add(String.valueOf(12));
                selected_seat.setText(seat_count + "," + 12);
            }
            if (pos == 20) {
                seat_id.add(String.valueOf(13));
                selected_seat.setText(seat_count + "," + 13);
            }
            if (pos == 21) {
                seat_id.add(String.valueOf(14));
                selected_seat.setText(seat_count + "," + 14);
            }
            if (pos == 23) {
                seat_id.add(String.valueOf(15));
                selected_seat.setText(seat_count + "," + 15);
            }
            if (pos == 24) {
                seat_id.add(String.valueOf(16));
                selected_seat.setText(seat_count + "," + 16);
            }
            if (pos == 29) {
                seat_id.add(String.valueOf(17));
                selected_seat.setText(seat_count + "," + 17);
            }
            if (pos == 28) {
                seat_id.add(String.valueOf(18));
                selected_seat.setText(seat_count + "," + 18);
            }
            if (pos == 26) {
                seat_id.add(String.valueOf(19));
                selected_seat.setText(seat_count + "," + 19);
            }
            if (pos == 25) {
                seat_id.add(String.valueOf(20));
                selected_seat.setText(seat_count + "," + 20);
            }
            if (pos == 30) {
                seat_id.add(String.valueOf(21));
                selected_seat.setText(seat_count + "," + 21);
            }
            if (pos == 31) {
                seat_id.add(String.valueOf(22));
                selected_seat.setText(seat_count + "," + 22);
            }
            if (pos == 33) {
                seat_id.add(String.valueOf(23));
                selected_seat.setText(seat_count + "," + 23);
            }
            if (pos == 34) {
                seat_id.add(String.valueOf(24));
                selected_seat.setText(seat_count + "," + 24);
            }
            if (pos == 39) {
                seat_id.add(String.valueOf(25));
                selected_seat.setText(seat_count + "," + 25);
            }
            if (pos == 38) {
                seat_id.add(String.valueOf(26));
                selected_seat.setText(seat_count + "," + 26);
            }
            if (pos == 36) {
                seat_id.add(String.valueOf(27));
                selected_seat.setText(seat_count + "," + 27);
            }
            if (pos == 35) {
                seat_id.add(String.valueOf(28));
                selected_seat.setText(seat_count + "," + 28);
            }
            if (pos == 40) {
                seat_id.add(String.valueOf(29));
                selected_seat.setText(seat_count + "," + 29);
            }
            if (pos == 41) {
                seat_id.add(String.valueOf(30));
                selected_seat.setText(seat_count + "," + 30);
            }
            if (pos == 43) {
                seat_id.add(String.valueOf(31));
                selected_seat.setText(seat_count + "," + 31);
            }
            if (pos == 44) {
                seat_id.add(String.valueOf(32));
                selected_seat.setText(seat_count + "," + 32);
            }
            if (pos == 49) {
                seat_id.add(String.valueOf(33));
                selected_seat.setText(seat_count + "," + 33);
            }
            if (pos == 48) {
                seat_id.add(String.valueOf(34));
                selected_seat.setText(seat_count + "," + 34);
            }
            if (pos == 46) {
                seat_id.add(String.valueOf(35));
                selected_seat.setText(seat_count + "," + 35);
            }
            if (pos == 45) {
                seat_id.add(String.valueOf(36));
                selected_seat.setText(seat_count + "," + 36);
            }

        }
        customGridAdapter.notifyDataSetChanged();
    }

    public void seatDeselcted(int pos) {

        gridArray.remove(pos);
        gridArray.add(pos, new Item(seatIcon, "" + ""));
        String c = cost.getText().toString();
        String[] split = c.split("₹");
        balance = Float.valueOf(split[1]) - ticketrate;
        cost.setText("₹" + String.valueOf(balance));
        //Log.e(TAG,"balance------------------------------------->"+balance );
        balance = 0f;
        //Log.e(TAG,"------------------------------------------------------");
        count = count - 1;

        for (int k = 0; k < 50; k++) {

            if (pos == 9) {
                swap = 1;
            }
            if (pos == 8) {
                swap = 2;
            }
            if (pos == 5) {
                swap = 4;
            }
            if (pos == 10) {
                swap = 5;
            }
            if (pos == 11) {
                swap = 6;
            }
            if (pos == 13) {
                swap = 7;
            }
            if (pos == 14) {
                swap = 8;
            }
            if (pos == 19) {
                swap = 9;
            }
            if (pos == 18) {
                swap = 10;
            }
            if (pos == 16) {
                swap = 11;
            }
            if (pos == 15) {
                swap = 12;
            }
            if (pos == 20) {
                swap = 13;
            }
            if (pos == 21) {
                swap = 14;
            }
            if (pos == 23) {
                swap = 15;
            }
            if (pos == 24) {
                swap = 16;
            }
            if (pos == 29) {
                swap = 17;
            }
            if (pos == 28) {
                swap = 18;
            }
            if (pos == 26) {
                swap = 19;
            }
            if (pos == 25) {
                swap = 20;
            }
            if (pos == 30) {
                swap = 21;
            }
            if (pos == 31) {
                swap = 22;
            }
            if (pos == 33) {
                swap = 23;
            }
            if (pos == 34) {
                swap = 24;
            }
            if (pos == 39) {
                swap = 25;
            }
            if (pos == 38) {
                swap = 26;
            }
            if (pos == 36) {
                swap = 27;
            }
            if (pos == 35) {
                swap = 28;
            }
            if (pos == 40) {
                swap = 29;
            }
            if (pos == 41) {
                swap = 30;
            }
            if (pos == 43) {
                swap = 31;
            }
            if (pos == 44) {
                swap = 32;
            }
            if (pos == 49) {
                swap = 33;
            }
            if (pos == 48) {
                swap = 34;
            }
            if (pos == 46) {
                swap = 35;
            }
            if (pos == 45) {
                swap = 36;
            }
            if (pos == k) {
                selected_seat.setText("");
                for (int i = 0; i < seat_id.size(); i++) {


                    // Log.e(TAG,"true");

                    seat_id.remove(String.valueOf(swap));

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

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Item item = gridArray.get(position);
        seatcompare = item.getImage();
        if (seatcompare == seatIcon) {
            if (count < 6) {
                seatSelected(position);
            } else {
                Toast.makeText(getApplicationContext(), "Maximum 6 seats", Toast.LENGTH_SHORT).show();
            }

        } else if (seatcompare == seatbooked) {

            seatbooked(position);


        } else if (seatcompare == ladiesseat) {

            seatSelected(position);

            p = position;

        } else if (seatcompare == genseat) {

            seatSelected(position);

            g = position;

        } else {
            if (p == position) {

                ladiesseat(p);

            }else if (g == position) {

                gentsseat(g);

            } else {
                seatDeselcted(position);
            }
        }

    }

    private void ladiesseat(int p2) {
        // TODO Auto-generated method stub

        String c = cost.getText().toString();
        String[] split = c.split("₹");
        Item item = gridArray.get(p2);
        seatcompare = item.getImage();
        if (seatcompare == seatSelect) {
            balance = Float.valueOf(split[1]) - ticketrate;
            gridArray.remove(p2);
            gridArray.add(p2, new Item(ladiesseat, "" + ""));
            count = count - 1;

            for (int k = 0; k < 50; k++) {

                if (p2 == 9) {
                    swap = 1;
                }
                if (p2 == 8) {
                    swap = 2;
                }
                if (p2 == 5) {
                    swap = 4;
                }
                if (p2 == 10) {
                    swap = 5;
                }
                if (p2 == 11) {
                    swap = 6;
                }
                if (p2 == 13) {
                    swap = 7;
                }
                if (p2 == 14) {
                    swap = 8;
                }
                if (p2 == 19) {
                    swap = 9;
                }
                if (p2 == 18) {
                    swap = 10;
                }
                if (p2 == 16) {
                    swap = 11;
                }
                if (p2 == 15) {
                    swap = 12;
                }
                if (p2 == 20) {
                    swap = 13;
                }
                if (p2 == 21) {
                    swap = 14;
                }
                if (p2 == 23) {
                    swap = 15;
                }
                if (p2 == 24) {
                    swap = 16;
                }
                if (p2 == 29) {
                    swap = 17;
                }
                if (p2 == 28) {
                    swap = 18;
                }
                if (p2 == 26) {
                    swap = 19;
                }
                if (p2 == 25) {
                    swap = 20;
                }
                if (p2 == 30) {
                    swap = 21;
                }
                if (p2 == 31) {
                    swap = 22;
                }
                if (p2 == 33) {
                    swap = 23;
                }
                if (p2 == 34) {
                    swap = 24;
                }
                if (p2 == 39) {
                    swap = 25;
                }
                if (p2 == 38) {
                    swap = 26;
                }
                if (p2 == 36) {
                    swap = 27;
                }
                if (p2 == 35) {
                    swap = 28;
                }
                if (p2 == 40) {
                    swap = 29;
                }
                if (p2 == 41) {
                    swap = 30;
                }
                if (p2 == 43) {
                    swap = 31;
                }
                if (p2 == 44) {
                    swap = 32;
                }
                if (p2 == 49) {
                    swap = 33;
                }
                if (p2 == 48) {
                    swap = 34;
                }
                if (p2 == 46) {
                    swap = 35;
                }
                if (p2 == 45) {
                    swap = 36;
                }
                if (p2 == k) {
                    selected_seat.setText("");
                    for (int i = 0; i < seat_id.size(); i++) {


                        // Log.e(TAG,"true");

                        seat_id.remove(String.valueOf(swap));

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
        } else {
            balance = Float.valueOf(split[1]) + ticketrate;
            count++;

            seat_count = selected_seat.getText().toString();
            //selected seat value

            if (seat_count.isEmpty()) {
                if (p2 == 9) {
                    seat_id.add(String.valueOf(1));
                    selected_seat.setText(String.valueOf(1));
                }
                if (p2 == 8) {
                    seat_id.add(String.valueOf(2));
                    selected_seat.setText(String.valueOf(2));
                }
                if (p2 == 5) {
                    seat_id.add(String.valueOf(4));
                    selected_seat.setText(String.valueOf(4));
                }
                if (p2 == 10) {
                    seat_id.add(String.valueOf(5));
                    selected_seat.setText(String.valueOf(5));
                }
                if (p2 == 11) {
                    seat_id.add(String.valueOf(6));
                    selected_seat.setText(String.valueOf(6));
                }
                if (p2 == 13) {
                    seat_id.add(String.valueOf(7));
                    selected_seat.setText(String.valueOf(7));
                }
                if (p2 == 14) {
                    seat_id.add(String.valueOf(8));
                    selected_seat.setText(String.valueOf(8));
                }
                if (p2 == 19) {
                    seat_id.add(String.valueOf(9));
                    selected_seat.setText(String.valueOf(9));
                }
                if (p2 == 18) {
                    seat_id.add(String.valueOf(10));
                    selected_seat.setText(String.valueOf(10));
                }
                if (p2 == 16) {
                    seat_id.add(String.valueOf(11));
                    selected_seat.setText(String.valueOf(11));
                }
                if (p2 == 15) {
                    seat_id.add(String.valueOf(12));
                    selected_seat.setText(String.valueOf(12));
                }
                if (p2 == 20) {
                    seat_id.add(String.valueOf(13));
                    selected_seat.setText(String.valueOf(13));
                }
                if (p2 == 21) {
                    seat_id.add(String.valueOf(14));
                    selected_seat.setText(String.valueOf(14));
                }
                if (p2 == 23) {
                    seat_id.add(String.valueOf(15));
                    selected_seat.setText(String.valueOf(15));
                }
                if (p2 == 24) {
                    seat_id.add(String.valueOf(16));
                    selected_seat.setText(String.valueOf(16));
                }
                if (p2 == 29) {
                    seat_id.add(String.valueOf(17));
                    selected_seat.setText(String.valueOf(17));
                }
                if (p2 == 28) {
                    seat_id.add(String.valueOf(18));
                    selected_seat.setText(String.valueOf(18));
                }
                if (p2 == 26) {
                    seat_id.add(String.valueOf(19));
                    selected_seat.setText(String.valueOf(19));
                }
                if (p2 == 25) {
                    seat_id.add(String.valueOf(20));
                    selected_seat.setText(String.valueOf(20));
                }
                if (p2 == 30) {
                    seat_id.add(String.valueOf(21));
                    selected_seat.setText(String.valueOf(21));
                }
                if (p2 == 31) {
                    seat_id.add(String.valueOf(22));
                    selected_seat.setText(String.valueOf(22));
                }
                if (p2 == 33) {
                    seat_id.add(String.valueOf(23));
                    selected_seat.setText(String.valueOf(23));
                }
                if (p2 == 34) {
                    seat_id.add(String.valueOf(24));
                    selected_seat.setText(String.valueOf(24));
                }
                if (p2 == 39) {
                    seat_id.add(String.valueOf(25));
                    selected_seat.setText(String.valueOf(25));
                }
                if (p2 == 38) {
                    seat_id.add(String.valueOf(26));
                    selected_seat.setText(String.valueOf(26));
                }
                if (p2 == 36) {
                    seat_id.add(String.valueOf(27));
                    selected_seat.setText(String.valueOf(27));
                }
                if (p2 == 35) {
                    seat_id.add(String.valueOf(28));
                    selected_seat.setText(String.valueOf(28));
                }
                if (p2 == 40) {
                    seat_id.add(String.valueOf(29));
                    selected_seat.setText(String.valueOf(29));
                }
                if (p2 == 41) {
                    seat_id.add(String.valueOf(30));
                    selected_seat.setText(String.valueOf(30));
                }
                if (p2 == 43) {
                    seat_id.add(String.valueOf(31));
                    selected_seat.setText(String.valueOf(31));
                }
                if (p2 == 44) {
                    seat_id.add(String.valueOf(32));
                    selected_seat.setText(String.valueOf(32));
                }
                if (p2 == 49) {
                    seat_id.add(String.valueOf(33));
                    selected_seat.setText(String.valueOf(33));
                }
                if (p2 == 48) {
                    seat_id.add(String.valueOf(34));
                    selected_seat.setText(String.valueOf(34));
                }
                if (p2 == 46) {
                    seat_id.add(String.valueOf(35));
                    selected_seat.setText(String.valueOf(35));
                }
                if (p2 == 45) {
                    seat_id.add(String.valueOf(36));
                    selected_seat.setText(String.valueOf(36));
                }

            } else {

                if (p2 == 9) {
                    seat_id.add(String.valueOf(1));
                    selected_seat.setText(seat_count + "," + 1);
                }
                if (p2 == 8) {
                    seat_id.add(String.valueOf(2));
                    selected_seat.setText(seat_count + "," + 2);
                }
                if (p2 == 5) {
                    seat_id.add(String.valueOf(4));
                    selected_seat.setText(seat_count + "," + 4);
                }
                if (p2 == 10) {
                    seat_id.add(String.valueOf(5));
                    selected_seat.setText(seat_count + "," + 5);
                }
                if (p2 == 11) {
                    seat_id.add(String.valueOf(6));
                    selected_seat.setText(seat_count + "," + 6);
                }
                if (p2 == 13) {
                    seat_id.add(String.valueOf(7));
                    selected_seat.setText(seat_count + "," + 7);
                }
                if (p2 == 14) {
                    seat_id.add(String.valueOf(8));
                    selected_seat.setText(seat_count + "," + 8);
                }
                if (p2 == 19) {
                    seat_id.add(String.valueOf(9));
                    selected_seat.setText(seat_count + "," + 9);
                }
                if (p2 == 18) {
                    seat_id.add(String.valueOf(10));
                    selected_seat.setText(seat_count + "," + 10);
                }
                if (p2 == 16) {
                    seat_id.add(String.valueOf(11));
                    selected_seat.setText(seat_count + "," + 11);
                }
                if (p2 == 15) {
                    seat_id.add(String.valueOf(12));
                    selected_seat.setText(seat_count + "," + 12);
                }
                if (p2 == 20) {
                    seat_id.add(String.valueOf(13));
                    selected_seat.setText(seat_count + "," + 13);
                }
                if (p2 == 21) {
                    seat_id.add(String.valueOf(14));
                    selected_seat.setText(seat_count + "," + 14);
                }
                if (p2 == 23) {
                    seat_id.add(String.valueOf(15));
                    selected_seat.setText(seat_count + "," + 15);
                }
                if (p2 == 24) {
                    seat_id.add(String.valueOf(16));
                    selected_seat.setText(seat_count + "," + 16);
                }
                if (p2 == 29) {
                    seat_id.add(String.valueOf(17));
                    selected_seat.setText(seat_count + "," + 17);
                }
                if (p2 == 28) {
                    seat_id.add(String.valueOf(18));
                    selected_seat.setText(seat_count + "," + 18);
                }
                if (p2 == 26) {
                    seat_id.add(String.valueOf(19));
                    selected_seat.setText(seat_count + "," + 19);
                }
                if (p2 == 25) {
                    seat_id.add(String.valueOf(20));
                    selected_seat.setText(seat_count + "," + 20);
                }
                if (p2 == 30) {
                    seat_id.add(String.valueOf(21));
                    selected_seat.setText(seat_count + "," + 21);
                }
                if (p2 == 31) {
                    seat_id.add(String.valueOf(22));
                    selected_seat.setText(seat_count + "," + 22);
                }
                if (p2 == 33) {
                    seat_id.add(String.valueOf(23));
                    selected_seat.setText(seat_count + "," + 23);
                }
                if (p2 == 34) {
                    seat_id.add(String.valueOf(24));
                    selected_seat.setText(seat_count + "," + 24);
                }
                if (p2 == 39) {
                    seat_id.add(String.valueOf(25));
                    selected_seat.setText(seat_count + "," + 25);
                }
                if (p2 == 38) {
                    seat_id.add(String.valueOf(26));
                    selected_seat.setText(seat_count + "," + 26);
                }
                if (p2 == 36) {
                    seat_id.add(String.valueOf(27));
                    selected_seat.setText(seat_count + "," + 27);
                }
                if (p2 == 35) {
                    seat_id.add(String.valueOf(28));
                    selected_seat.setText(seat_count + "," + 28);
                }
                if (p2 == 40) {
                    seat_id.add(String.valueOf(29));
                    selected_seat.setText(seat_count + "," + 29);
                }
                if (p2 == 41) {
                    seat_id.add(String.valueOf(30));
                    selected_seat.setText(seat_count + "," + 30);
                }
                if (p2 == 43) {
                    seat_id.add(String.valueOf(31));
                    selected_seat.setText(seat_count + "," + 31);
                }
                if (p2 == 44) {
                    seat_id.add(String.valueOf(32));
                    selected_seat.setText(seat_count + "," + 32);
                }
                if (p2 == 49) {
                    seat_id.add(String.valueOf(33));
                    selected_seat.setText(seat_count + "," + 33);
                }
                if (p2 == 48) {
                    seat_id.add(String.valueOf(34));
                    selected_seat.setText(seat_count + "," + 34);
                }
                if (p2 == 46) {
                    seat_id.add(String.valueOf(35));
                    selected_seat.setText(seat_count + "," + 35);
                }
                if (p2 == 45) {
                    seat_id.add(String.valueOf(36));
                    selected_seat.setText(seat_count + "," + 36);
                }

            }


        }

        cost.setText("₹" + String.valueOf(balance));
        //Log.e(TAG,"--------------------ladiesseat------------------------");
        //Log.e(TAG,"balance------------------------------------->"+balance );
        balance = 0f;
        //Log.e(TAG,"--------------------------------------------------------------------------------");
        customGridAdapter.notifyDataSetChanged();

    }

    private void gentsseat(int p2) {
        // TODO Auto-generated method stub

        String c = cost.getText().toString();
        String[] split = c.split("₹");
        Item item = gridArray.get(p2);
        seatcompare = item.getImage();
        if (seatcompare == seatSelect) {
            balance = Float.valueOf(split[1]) - ticketrate;
            gridArray.remove(p2);
            gridArray.add(p2, new Item(genseat, "" + ""));
            count = count - 1;

            for (int k = 0; k < 50; k++) {

                if (p2 == 9) {
                    swap = 1;
                }
                if (p2 == 8) {
                    swap = 2;
                }
                if (p2 == 5) {
                    swap = 4;
                }
                if (p2 == 10) {
                    swap = 5;
                }
                if (p2 == 11) {
                    swap = 6;
                }
                if (p2 == 13) {
                    swap = 7;
                }
                if (p2 == 14) {
                    swap = 8;
                }
                if (p2 == 19) {
                    swap = 9;
                }
                if (p2 == 18) {
                    swap = 10;
                }
                if (p2 == 16) {
                    swap = 11;
                }
                if (p2 == 15) {
                    swap = 12;
                }
                if (p2 == 20) {
                    swap = 13;
                }
                if (p2 == 21) {
                    swap = 14;
                }
                if (p2 == 23) {
                    swap = 15;
                }
                if (p2 == 24) {
                    swap = 16;
                }
                if (p2 == 29) {
                    swap = 17;
                }
                if (p2 == 28) {
                    swap = 18;
                }
                if (p2 == 26) {
                    swap = 19;
                }
                if (p2 == 25) {
                    swap = 20;
                }
                if (p2 == 30) {
                    swap = 21;
                }
                if (p2 == 31) {
                    swap = 22;
                }
                if (p2 == 33) {
                    swap = 23;
                }
                if (p2 == 34) {
                    swap = 24;
                }
                if (p2 == 39) {
                    swap = 25;
                }
                if (p2 == 38) {
                    swap = 26;
                }
                if (p2 == 36) {
                    swap = 27;
                }
                if (p2 == 35) {
                    swap = 28;
                }
                if (p2 == 40) {
                    swap = 29;
                }
                if (p2 == 41) {
                    swap = 30;
                }
                if (p2 == 43) {
                    swap = 31;
                }
                if (p2 == 44) {
                    swap = 32;
                }
                if (p2 == 49) {
                    swap = 33;
                }
                if (p2 == 48) {
                    swap = 34;
                }
                if (p2 == 46) {
                    swap = 35;
                }
                if (p2 == 45) {
                    swap = 36;
                }
                if (p2 == k) {
                    selected_seat.setText("");
                    for (int i = 0; i < seat_id.size(); i++) {


                        // Log.e(TAG,"true");

                        seat_id.remove(String.valueOf(swap));

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
        } else {
            balance = Float.valueOf(split[1]) + ticketrate;
            count++;

            seat_count = selected_seat.getText().toString();
            //selected seat value

            if (seat_count.isEmpty()) {
                if (p2 == 9) {
                    seat_id.add(String.valueOf(1));
                    selected_seat.setText(String.valueOf(1));
                }
                if (p2 == 8) {
                    seat_id.add(String.valueOf(2));
                    selected_seat.setText(String.valueOf(2));
                }
                if (p2 == 5) {
                    seat_id.add(String.valueOf(4));
                    selected_seat.setText(String.valueOf(4));
                }
                if (p2 == 10) {
                    seat_id.add(String.valueOf(5));
                    selected_seat.setText(String.valueOf(5));
                }
                if (p2 == 11) {
                    seat_id.add(String.valueOf(6));
                    selected_seat.setText(String.valueOf(6));
                }
                if (p2 == 13) {
                    seat_id.add(String.valueOf(7));
                    selected_seat.setText(String.valueOf(7));
                }
                if (p2 == 14) {
                    seat_id.add(String.valueOf(8));
                    selected_seat.setText(String.valueOf(8));
                }
                if (p2 == 19) {
                    seat_id.add(String.valueOf(9));
                    selected_seat.setText(String.valueOf(9));
                }
                if (p2 == 18) {
                    seat_id.add(String.valueOf(10));
                    selected_seat.setText(String.valueOf(10));
                }
                if (p2 == 16) {
                    seat_id.add(String.valueOf(11));
                    selected_seat.setText(String.valueOf(11));
                }
                if (p2 == 15) {
                    seat_id.add(String.valueOf(12));
                    selected_seat.setText(String.valueOf(12));
                }
                if (p2 == 20) {
                    seat_id.add(String.valueOf(13));
                    selected_seat.setText(String.valueOf(13));
                }
                if (p2 == 21) {
                    seat_id.add(String.valueOf(14));
                    selected_seat.setText(String.valueOf(14));
                }
                if (p2 == 23) {
                    seat_id.add(String.valueOf(15));
                    selected_seat.setText(String.valueOf(15));
                }
                if (p2 == 24) {
                    seat_id.add(String.valueOf(16));
                    selected_seat.setText(String.valueOf(16));
                }
                if (p2 == 29) {
                    seat_id.add(String.valueOf(17));
                    selected_seat.setText(String.valueOf(17));
                }
                if (p2 == 28) {
                    seat_id.add(String.valueOf(18));
                    selected_seat.setText(String.valueOf(18));
                }
                if (p2 == 26) {
                    seat_id.add(String.valueOf(19));
                    selected_seat.setText(String.valueOf(19));
                }
                if (p2 == 25) {
                    seat_id.add(String.valueOf(20));
                    selected_seat.setText(String.valueOf(20));
                }
                if (p2 == 30) {
                    seat_id.add(String.valueOf(21));
                    selected_seat.setText(String.valueOf(21));
                }
                if (p2 == 31) {
                    seat_id.add(String.valueOf(22));
                    selected_seat.setText(String.valueOf(22));
                }
                if (p2 == 33) {
                    seat_id.add(String.valueOf(23));
                    selected_seat.setText(String.valueOf(23));
                }
                if (p2 == 34) {
                    seat_id.add(String.valueOf(24));
                    selected_seat.setText(String.valueOf(24));
                }
                if (p2 == 39) {
                    seat_id.add(String.valueOf(25));
                    selected_seat.setText(String.valueOf(25));
                }
                if (p2 == 38) {
                    seat_id.add(String.valueOf(26));
                    selected_seat.setText(String.valueOf(26));
                }
                if (p2 == 36) {
                    seat_id.add(String.valueOf(27));
                    selected_seat.setText(String.valueOf(27));
                }
                if (p2 == 35) {
                    seat_id.add(String.valueOf(28));
                    selected_seat.setText(String.valueOf(28));
                }
                if (p2 == 40) {
                    seat_id.add(String.valueOf(29));
                    selected_seat.setText(String.valueOf(29));
                }
                if (p2 == 41) {
                    seat_id.add(String.valueOf(30));
                    selected_seat.setText(String.valueOf(30));
                }
                if (p2 == 43) {
                    seat_id.add(String.valueOf(31));
                    selected_seat.setText(String.valueOf(31));
                }
                if (p2 == 44) {
                    seat_id.add(String.valueOf(32));
                    selected_seat.setText(String.valueOf(32));
                }
                if (p2 == 49) {
                    seat_id.add(String.valueOf(33));
                    selected_seat.setText(String.valueOf(33));
                }
                if (p2 == 48) {
                    seat_id.add(String.valueOf(34));
                    selected_seat.setText(String.valueOf(34));
                }
                if (p2 == 46) {
                    seat_id.add(String.valueOf(35));
                    selected_seat.setText(String.valueOf(35));
                }
                if (p2 == 45) {
                    seat_id.add(String.valueOf(36));
                    selected_seat.setText(String.valueOf(36));
                }

            } else {

                if (p2 == 9) {
                    seat_id.add(String.valueOf(1));
                    selected_seat.setText(seat_count + "," + 1);
                }
                if (p2 == 8) {
                    seat_id.add(String.valueOf(2));
                    selected_seat.setText(seat_count + "," + 2);
                }
                if (p2 == 5) {
                    seat_id.add(String.valueOf(4));
                    selected_seat.setText(seat_count + "," + 4);
                }
                if (p2 == 10) {
                    seat_id.add(String.valueOf(5));
                    selected_seat.setText(seat_count + "," + 5);
                }
                if (p2 == 11) {
                    seat_id.add(String.valueOf(6));
                    selected_seat.setText(seat_count + "," + 6);
                }
                if (p2 == 13) {
                    seat_id.add(String.valueOf(7));
                    selected_seat.setText(seat_count + "," + 7);
                }
                if (p2 == 14) {
                    seat_id.add(String.valueOf(8));
                    selected_seat.setText(seat_count + "," + 8);
                }
                if (p2 == 19) {
                    seat_id.add(String.valueOf(9));
                    selected_seat.setText(seat_count + "," + 9);
                }
                if (p2 == 18) {
                    seat_id.add(String.valueOf(10));
                    selected_seat.setText(seat_count + "," + 10);
                }
                if (p2 == 16) {
                    seat_id.add(String.valueOf(11));
                    selected_seat.setText(seat_count + "," + 11);
                }
                if (p2 == 15) {
                    seat_id.add(String.valueOf(12));
                    selected_seat.setText(seat_count + "," + 12);
                }
                if (p2 == 20) {
                    seat_id.add(String.valueOf(13));
                    selected_seat.setText(seat_count + "," + 13);
                }
                if (p2 == 21) {
                    seat_id.add(String.valueOf(14));
                    selected_seat.setText(seat_count + "," + 14);
                }
                if (p2 == 23) {
                    seat_id.add(String.valueOf(15));
                    selected_seat.setText(seat_count + "," + 15);
                }
                if (p2 == 24) {
                    seat_id.add(String.valueOf(16));
                    selected_seat.setText(seat_count + "," + 16);
                }
                if (p2 == 29) {
                    seat_id.add(String.valueOf(17));
                    selected_seat.setText(seat_count + "," + 17);
                }
                if (p2 == 28) {
                    seat_id.add(String.valueOf(18));
                    selected_seat.setText(seat_count + "," + 18);
                }
                if (p2 == 26) {
                    seat_id.add(String.valueOf(19));
                    selected_seat.setText(seat_count + "," + 19);
                }
                if (p2 == 25) {
                    seat_id.add(String.valueOf(20));
                    selected_seat.setText(seat_count + "," + 20);
                }
                if (p2 == 30) {
                    seat_id.add(String.valueOf(21));
                    selected_seat.setText(seat_count + "," + 21);
                }
                if (p2 == 31) {
                    seat_id.add(String.valueOf(22));
                    selected_seat.setText(seat_count + "," + 22);
                }
                if (p2 == 33) {
                    seat_id.add(String.valueOf(23));
                    selected_seat.setText(seat_count + "," + 23);
                }
                if (p2 == 34) {
                    seat_id.add(String.valueOf(24));
                    selected_seat.setText(seat_count + "," + 24);
                }
                if (p2 == 39) {
                    seat_id.add(String.valueOf(25));
                    selected_seat.setText(seat_count + "," + 25);
                }
                if (p2 == 38) {
                    seat_id.add(String.valueOf(26));
                    selected_seat.setText(seat_count + "," + 26);
                }
                if (p2 == 36) {
                    seat_id.add(String.valueOf(27));
                    selected_seat.setText(seat_count + "," + 27);
                }
                if (p2 == 35) {
                    seat_id.add(String.valueOf(28));
                    selected_seat.setText(seat_count + "," + 28);
                }
                if (p2 == 40) {
                    seat_id.add(String.valueOf(29));
                    selected_seat.setText(seat_count + "," + 29);
                }
                if (p2 == 41) {
                    seat_id.add(String.valueOf(30));
                    selected_seat.setText(seat_count + "," + 30);
                }
                if (p2 == 43) {
                    seat_id.add(String.valueOf(31));
                    selected_seat.setText(seat_count + "," + 31);
                }
                if (p2 == 44) {
                    seat_id.add(String.valueOf(32));
                    selected_seat.setText(seat_count + "," + 32);
                }
                if (p2 == 49) {
                    seat_id.add(String.valueOf(33));
                    selected_seat.setText(seat_count + "," + 33);
                }
                if (p2 == 48) {
                    seat_id.add(String.valueOf(34));
                    selected_seat.setText(seat_count + "," + 34);
                }
                if (p2 == 46) {
                    seat_id.add(String.valueOf(35));
                    selected_seat.setText(seat_count + "," + 35);
                }
                if (p2 == 45) {
                    seat_id.add(String.valueOf(36));
                    selected_seat.setText(seat_count + "," + 36);
                }

            }


        }

        cost.setText("₹" + String.valueOf(balance));
        //Log.e(TAG,"--------------------ladiesseat------------------------");
        //Log.e(TAG,"balance------------------------------------->"+balance );
        balance = 0f;
        //Log.e(TAG,"--------------------------------------------------------------------------------");
        customGridAdapter.notifyDataSetChanged();

    }

    private void seatbooked(int position) {
        // TODO Auto-generated method stub
    }

    //Json
    private void makeJsonObjectRequest() {


        System.out.println("method inside");

        StringRequest request = new StringRequest(Request.Method.GET,
                AppConfig.url_trip_details, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                System.out.println("inside res method");
                System.out.println("url S36\t"+ AppConfig.url_trip_details);
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


                            for (int j = 0; j <= 34; j++) {

                                String temp = seat_no.get(j);

                                Integer booked = Integer.parseInt(temp);

                                if (booked == 1) {

                                    String seatstatus = seat_status.get(j);

                                    if (seatstatus.equalsIgnoreCase("A")) {

                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(9);
                                        gridArray.add(9, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(9);
                                        gridArray.add(9, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(9);
                                        gridArray.add(9, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));
                                    }
                                }

                                if (booked == 2) {

                                    String seatstatus = seat_status.get(j);

                                    //Log.e(TAG,"seatstatus"+seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(8);
                                        gridArray.add(8, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(8);
                                        gridArray.add(8, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));


                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(8);
                                        gridArray.add(8, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));


                                    }
                                }

                                if (booked == 4) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(5);
                                        gridArray.add(5, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(5);
                                        gridArray.add(5, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(5);
                                        gridArray.add(5, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));
                                    }
                                }

                                if (booked == 5) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(10);
                                        gridArray.add(10, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(10);
                                        gridArray.add(10, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));

                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(10);
                                        gridArray.add(10, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }

                                if (booked == 6) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(11);
                                        gridArray.add(11, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(11);
                                        gridArray.add(11, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));

                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(11);
                                        gridArray.add(11, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 7) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(13);
                                        gridArray.add(13, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(13);
                                        gridArray.add(13, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));

                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(13);
                                        gridArray.add(13, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }

                                if (booked == 8) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(14);
                                        gridArray.add(14, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(14);
                                        gridArray.add(14, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));

                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(14);
                                        gridArray.add(14, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 9) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(19);
                                        gridArray.add(19, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(19);
                                        gridArray.add(19, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));

                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(19);
                                        gridArray.add(19, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }

                                if (booked == 10) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(18);
                                        gridArray.add(18, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(18);
                                        gridArray.add(18, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));

                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(18);
                                        gridArray.add(18, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }

                                if (booked == 11) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(16);
                                        gridArray.add(16, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(16);
                                        gridArray.add(16, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));

                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(16);
                                        gridArray.add(16, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 12) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(15);
                                        gridArray.add(15, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(15);
                                        gridArray.add(15, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(15);
                                        gridArray.add(15, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 13) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(20);
                                        gridArray.add(20, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(20);
                                        gridArray.add(20, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(20);
                                        gridArray.add(20, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 14) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(21);
                                        gridArray.add(21, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(21);
                                        gridArray.add(21, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(21);
                                        gridArray.add(21, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 15) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(23);
                                        gridArray.add(23, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(23);
                                        gridArray.add(23, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(23);
                                        gridArray.add(23, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 16) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(24);
                                        gridArray.add(24, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(24);
                                        gridArray.add(24, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(24);
                                        gridArray.add(24, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 17) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(29);
                                        gridArray.add(29, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(29);
                                        gridArray.add(29, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(29);
                                        gridArray.add(29, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 18) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(28);
                                        gridArray.add(28, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(28);
                                        gridArray.add(28, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(28);
                                        gridArray.add(28, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 19) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(26);
                                        gridArray.add(26, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(26);
                                        gridArray.add(26, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(26);
                                        gridArray.add(26, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 20) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(25);
                                        gridArray.add(25, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(25);
                                        gridArray.add(25, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(25);
                                        gridArray.add(25, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 21) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(30);
                                        gridArray.add(30, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(30);
                                        gridArray.add(30, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(30);
                                        gridArray.add(30, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 22) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(31);
                                        gridArray.add(31, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(31);
                                        gridArray.add(31, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(31);
                                        gridArray.add(31, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 23) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(33);
                                        gridArray.add(33, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(33);
                                        gridArray.add(33, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(33);
                                        gridArray.add(33, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 24) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(34);
                                        gridArray.add(34, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(34);
                                        gridArray.add(34, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(34);
                                        gridArray.add(34, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 25) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(39);
                                        gridArray.add(39, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(39);
                                        gridArray.add(39, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(390);
                                        gridArray.add(39, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 26) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(38);
                                        gridArray.add(38, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(38);
                                        gridArray.add(38, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(38);
                                        gridArray.add(38, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 27) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(36);
                                        gridArray.add(36, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(36);
                                        gridArray.add(36, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(36);
                                        gridArray.add(36, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 28) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(35);
                                        gridArray.add(35, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(35);
                                        gridArray.add(35, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(35);
                                        gridArray.add(35, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 29) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(40);
                                        gridArray.add(40, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(40);
                                        gridArray.add(40, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(40);
                                        gridArray.add(40, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 30) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(41);
                                        gridArray.add(41, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(41);
                                        gridArray.add(41, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(41);
                                        gridArray.add(41, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 31) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(43);
                                        gridArray.add(43, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(43);
                                        gridArray.add(43, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(43);
                                        gridArray.add(43, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 32) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(44);
                                        gridArray.add(44, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(44);
                                        gridArray.add(44, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(44);
                                        gridArray.add(44, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 33) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(49);
                                        gridArray.add(49, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(49);
                                        gridArray.add(49, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(49);
                                        gridArray.add(49, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 34) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(48);
                                        gridArray.add(48, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(48);
                                        gridArray.add(48, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(48);
                                        gridArray.add(48, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }

                                }
                                if (booked == 35) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(46);
                                        gridArray.add(46, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(46);
                                        gridArray.add(46, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(46);
                                        gridArray.add(46, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

                                    }
                                }
                                if (booked == 36) {

                                    String seatstatus = seat_status.get(j);

                                    Log.e(TAG, "seatstatus" + seatstatus);

                                    if (seatstatus.equalsIgnoreCase("A")) {


                                    } else if (seatstatus.equalsIgnoreCase("B")) {

                                        gridArray.remove(45);
                                        gridArray.add(45, new Item(seatbooked, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();

                                    } else if (seatstatus.equalsIgnoreCase("F")) {

                                        gridArray.remove(45);
                                        gridArray.add(45, new Item(ladiesseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        ladies.add(String.valueOf(booked));
                                    } else if (seatstatus.equalsIgnoreCase("M")) {

                                        gridArray.remove(45);
                                        gridArray.add(45, new Item(genseat, "" + ""));
                                        customGridAdapter.notifyDataSetChanged();
                                        gents.add(String.valueOf(booked));

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
                Toast.makeText(Seat_Selection_Activity.this, "no internet connection...!", Toast.LENGTH_SHORT).show();
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


} //Class close