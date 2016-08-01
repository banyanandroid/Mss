package com.banyan.mss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import dmax.dialog.SpotsDialog;

public class Conform_Booking extends Activity {

    final String TAG = "Conform_Booking.java";

    TextView fromto, date, address_, boardingpoint, email_, phone_, pname, fare_, stax_, totalfare;

    String email, mobile, name, age, gender, name2, age2, gender2, name3, age3, gender3, name4, age4, gender4, name5, age5, gender5, name6, age6, gender6;

    int stax, fare, ticketcount;

    Button makepayment;

    int fromid, toid, boardingid;

    String day, month, year;

    int scheduleId;
    private static final int MY_SOCKET_TIMEOUT_MS = 0;
    public static RequestQueue queue;
    private AlertDialog dialog;

    ArrayList<String> seats = new ArrayList<String>();
    SessionManager session;
    String str_id,str_mobile,str_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conform__booking);

        //session
        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        str_id = user.get(SessionManager.KEY_ID);
        str_mobile = user.get(SessionManager.KEY_MOBILE);
        str_name = user.get(SessionManager.KEY_LOGINID);

        String from = AppConfig.from;//((GlobalValues) this.getApplication()).getfrom();

        String to = AppConfig.to;//((GlobalValues) this.getApplication()).getto();

        String traveldate = ((GlobalValues) this.getApplication()).getdate();

        String address = ((GlobalValues) this.getApplication()).getaddress();

        String boardingname = ((GlobalValues) this.getApplication()).getboardingname();

        fare = AppConfig.fare1; //((GlobalValues) this.getApplication()).getfare();

        stax = AppConfig.tax1; //((GlobalValues) this.getApplication()).gettax();

        ticketcount = AppConfig.ticket_count; //((GlobalValues) this.getApplication()).getcount();

        email = getIntent().getExtras().getString("email");

        mobile = getIntent().getExtras().getString("mobile");

        name = getIntent().getExtras().getString("name");

        age = getIntent().getExtras().getString("age");

        gender = getIntent().getExtras().getString("gender");

        name2 = getIntent().getExtras().getString("name2");

        age2 = getIntent().getExtras().getString("age2");

        gender2 = getIntent().getExtras().getString("gender2");

        name3 = getIntent().getExtras().getString("name3");

        age3 = getIntent().getExtras().getString("age3");

        gender3 = getIntent().getExtras().getString("gender3");

        name4 = getIntent().getExtras().getString("name4");

        age4 = getIntent().getExtras().getString("age4");

        gender4 = getIntent().getExtras().getString("gender4");

        name5 = getIntent().getExtras().getString("name5");

        age5 = getIntent().getExtras().getString("age5");

        gender5 = getIntent().getExtras().getString("gender5");

        name6 = getIntent().getExtras().getString("name6");

        age6 = getIntent().getExtras().getString("age6");

        gender6 = getIntent().getExtras().getString("gender6");


        //json parsing data

        fromid = AppConfig.from_id;//getIntent().getExtras().getInt("fid");

        toid = AppConfig.to_id;//getIntent().getExtras().getInt("tid");

        scheduleId = AppConfig.schedule_Id; //getIntent().getExtras().getInt("s_id");

        day = AppConfig.day;//getIntent().getExtras().getString("day");

        month = AppConfig.month;//getIntent().getExtras().getString("month");

        year = AppConfig.year;//getIntent().getExtras().getInt("year");

        seats = getIntent().getStringArrayListExtra("seats");
        AppConfig.seats = seats;

        boardingid = getIntent().getExtras().getInt("boarding_id");
        AppConfig.boardingid = boardingid;

        Log.e(TAG, "sample))))))))))))))))))))))))))))))))))" + from);

        Log.e(TAG, "to))))))))))))))))))))))))))))))))))))))" + to);

        Log.e(TAG, "ticketcount))))))))))))))))))))))))))))))))))))))" + ticketcount);


        Log.e(TAG, fromid + "\n" + toid + "\n" + scheduleId + "\n" + day + "\n" + month + "\n" + year + "\n" + seats + "\n" + boardingid);


        fromto = (TextView) findViewById(R.id.fromto);

        date = (TextView) findViewById(R.id.traveldate);

        address_ = (TextView) findViewById(R.id.address);

        boardingpoint = (TextView) findViewById(R.id.Boardingpoint);

        email_ = (TextView) findViewById(R.id.email_);

        phone_ = (TextView) findViewById(R.id.phone);

        pname = (TextView) findViewById(R.id.passengername);

        stax_ = (TextView) findViewById(R.id.tax);

        fare_ = (TextView) findViewById(R.id.fare_);

        totalfare = (TextView) findViewById(R.id.totalfare);

        int tx = stax * ticketcount;

        stax_.setText(String.valueOf(tx));

        int f = fare * ticketcount;

        fare_.setText(String.valueOf(f));

        totalfare.setText("₹" + String.valueOf(tx + f));

        Log.e(TAG, "tx))))))))))))))))))))))))))))))))))" + tx);

        Log.e(TAG, "fare))))))))))))))))))))))))))))))))))))))" + f);

        Log.e(TAG, "totalcount))))))))))))))))))))))))))))))))))))))" + String.valueOf(tx + f));

        AppConfig.total_fare = String.valueOf(tx + f);

        fromto.setText(AppConfig.from + " to " + AppConfig.to);

        date.setText(traveldate);

        address_.setText(address);

        boardingpoint.setText(boardingname);

        email_.setText(email);

        phone_.setText(mobile);


        if (name2 == null) {
            pname.setText(name + " " + gender + " " + age + "years");
        } else if (name3 == null) {
            pname.setText(name + "  " + gender + " " + age + "years" + "\n" + name2 + "  " + gender2 + " " + age2 + "years");
        } else if (name4 == null) {
            pname.setText(name + "  " + gender + " " + age + "years" + "\n" + name2 + "  " + gender2 + " " + age2 + "years" + "\n" + name3 + "  " + gender3 + " " + age3 + "years");
        } else if (name5 == null) {
            pname.setText(name + "  " + gender + " " + age + "years" + "\n" + name2 + "  " + gender2 + " " + age2 + "years" + "\n" + name3 + "  " + gender3 + " " + age3 + "years" + "\n" + name4 + "  " + gender4 + " " + age4 + "years");
        } else if (name6 == null) {
            pname.setText(name + "  " + gender + " " + age + "years" + "\n" + name2 + "  " + gender2 + " " + age2 + "years" + "\n" + name3 + "  " + gender3 + " " + age3 + "years" + "\n" + name4 + "  " + gender4 + " " + age4 + "years" + "\n" + name5 + "  " + gender5 + " " + age5 + "years");
        } else {
            pname.setText(name + "  " + gender + " " + age + "years" + "\n" + name2 + "  " + gender2 + " " + age2 + "years" + "\n" + name3 + "  " + gender3 + " " + age3 + "years" + "\n" + name4 + "  " + gender4 + " " + age4 + "years" + "\n" + name5 + "  " + gender5 + " " + age5 + "years" + "\n" + name6 + "  " + gender6 + " " + age6 + "years");
        }
        //pname.setText(name+"  "+gender+" "+age+"years"+"\n"+name2+"  "+gender2+" "+age2+"years"+"\n"+name3+"  "+gender3+" "+age3+"years"+"\n"+name4+"  "+gender4+" "+age4+"years"+"\n"+name5+"  "+gender5+" "+age5+"years"+"\n"+name6+"  "+gender6+" "+age6+"years");

        makepayment = (Button) findViewById(R.id.makepayment);

        makepayment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                try {
                    dialog = new SpotsDialog(Conform_Booking.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Conform_Booking.this);
                    makeJsonObjectRequest();
                    AppConfig.email = email;
                    AppConfig.mobile = mobile;
                    Intent inte = new Intent(Conform_Booking.this, CheckoutActivity.class);
                    inte.putExtra("email",email);
                    inte.putExtra("mobile", mobile);
                    startActivity(inte);

                } catch (Exception e) {
// TODO: handle exception
                }
            }
        });

    }

    private void makeJsonObjectRequest() {

        System.out.println("method inside");

        if (seats.size() == 1) {
            AppConfig.jsonurl = "http://www.mssbus.com/api/app/?BlockSeatsForBooking&Appkey=HaPpY&UserId="+str_id+"&UserMobile="+str_mobile+"&scheduleId=" + AppConfig.schedule_Id + "&travelDate=" + AppConfig.date + "&fromStationId=" + AppConfig.from_id + "&toStationId=" + AppConfig.to_id + "&boardingPointId=" + boardingid + "&emailId=" + email + "&mobileNbr=" + mobile + "&passengerDetails[0][seatNbr]=" + seats.get(0) + "&passengerDetails[0][name]=" + name + "&passengerDetails[0][age]=" + age + "&passengerDetails[0][sex]=" + gender + "&format=json";

            Log.e(TAG, "\n\n\n\n\n\n\n\ninside single passenger");
        }
        if (seats.size() == 2) {
            AppConfig.jsonurl = "http://www.mssbus.com/api/app/?BlockSeatsForBooking&Appkey=HaPpY&UserId="+str_id+"&UserMobile="+str_mobile+"&scheduleId=" + scheduleId + "&travelDate=" + day + "/" + month + "/" + year + "&fromStationId=" + fromid + "&toStationId=" + toid + "&boardingPointId=" + boardingid + "&emailId=" + email + "&mobileNbr=" + mobile + "&passengerDetails[0][seatNbr]=" + seats.get(0) + "&passengerDetails[0][name]=" + name + "&passengerDetails[0][age]=" + age + "&passengerDetails[0][sex]=" + gender + "&passengerDetails[1][seatNbr]=" + seats.get(1) + "&passengerDetails[1][name]=" + name2 + "&passengerDetails[1][age]=" + age2 + "&passengerDetails[1][sex]=" + gender2 + "&format=json";
            Log.e(TAG, "\n\n\n\n\n\n\n\ninside double passenger");

        }
        if (seats.size() == 3) {
            AppConfig.jsonurl = "http://www.mssbus.com/api/app/?BlockSeatsForBooking&Appkey=HaPpY&UserId="+str_id+"&UserMobile="+str_mobile+"&scheduleId=" + scheduleId + "&travelDate=" + day + "/" + month + "/" + year + "&fromStationId=" + fromid + "&toStationId=" + toid + "&boardingPointId=" + boardingid + "&emailId=" + email + "&mobileNbr=" + mobile + "&passengerDetails[0][seatNbr]=" + seats.get(0) + "&passengerDetails[0][name]=" + name + "&passengerDetails[0][age]=" + age + "&passengerDetails[0][sex]=" + gender + "&passengerDetails[1][seatNbr]=" + seats.get(1) + "&passengerDetails[1][name]=" + name2 + "&passengerDetails[1][age]=" + age2 + "&passengerDetails[1][sex]=" + gender2 + "&passengerDetails[2][seatNbr]=" + seats.get(2) + "&passengerDetails[2][name]=" + name3 + "&passengerDetails[2][age]=" + age3 + "&passengerDetails[2][sex]=" + gender3 + "&format=json";
            Log.e(TAG, "\n\n\n\n\n\n\n\ninside triple passenger");

        }
        if (seats.size() == 4) {
            AppConfig.jsonurl = "http://www.mssbus.com/api/app/?BlockSeatsForBooking&Appkey=HaPpY&UserId="+str_id+"&UserMobile="+str_mobile+"&scheduleId=" + scheduleId + "&travelDate=" + day + "/" + month + "/" + year + "&fromStationId=" + fromid + "&toStationId=" + toid + "&boardingPointId=" + boardingid + "&emailId=" + email + "&mobileNbr=" + mobile + "&passengerDetails[0][seatNbr]=" + seats.get(0) + "&passengerDetails[0][name]=" + name + "&passengerDetails[0][age]=" + age + "&passengerDetails[0][sex]=" + gender + "&passengerDetails[1][seatNbr]=" + seats.get(1) + "&passengerDetails[1][name]=" + name2 + "&passengerDetails[1][age]=" + age2 + "&passengerDetails[1][sex]=" + gender2 + "&passengerDetails[2][seatNbr]=" + seats.get(2) + "&passengerDetails[2][name]=" + name3 + "&passengerDetails[2][age]=" + age3 + "&passengerDetails[2][sex]=" + gender3 + "&passengerDetails[3][seatNbr]=" + seats.get(3) + "&passengerDetails[3][name]=" + name4 + "&passengerDetails[3][age]=" + age4 + "&passengerDetails[3][sex]=" + gender4 + "&format=json";
            Log.e(TAG, "\n\n\n\n\n\n\n\ninside four passenger");

        }
        if (seats.size() == 5) {
            AppConfig.jsonurl = "http://www.mssbus.com/api/app/?BlockSeatsForBooking&Appkey=HaPpY&UserId="+str_id+"&UserMobile="+str_mobile+"&scheduleId=" + scheduleId + "&travelDate=" + day + "/" + month + "/" + year + "&fromStationId=" + fromid + "&toStationId=" + toid + "&boardingPointId=" + boardingid + "&emailId=" + email + "&mobileNbr=" + mobile + "&passengerDetails[0][seatNbr]=" + seats.get(0) + "&passengerDetails[0][name]=" + name + "&passengerDetails[0][age]=" + age + "&passengerDetails[0][sex]=" + gender + "&passengerDetails[1][seatNbr]=" + seats.get(1) + "&passengerDetails[1][name]=" + name2 + "&passengerDetails[1][age]=" + age2 + "&passengerDetails[1][sex]=" + gender2 + "&passengerDetails[2][seatNbr]=" + seats.get(2) + "&passengerDetails[2][name]=" + name3 + "&passengerDetails[2][age]=" + age3 + "&passengerDetails[2][sex]=" + gender3 + "&passengerDetails[3][seatNbr]=" + seats.get(3) + "&passengerDetails[3][name]=" + name4 + "&passengerDetails[3][age]=" + age4 + "&passengerDetails[3][sex]=" + gender4 + "&passengerDetails[4][seatNbr]=" + seats.get(4) + "&passengerDetails[4][name]=" + name5 + "&passengerDetails[4][age]=" + age5 + "&passengerDetails[4][sex]=" + gender5 + "&format=json";
            Log.e(TAG, "\n\n\n\n\n\n\n\ninside fix passenger");
        }
        if (seats.size() == 6) {
            AppConfig.jsonurl = "http://www.mssbus.com/api/app/?BlockSeatsForBooking&Appkey=HaPpY&UserId="+str_id+"&UserMobile="+str_mobile+"&scheduleId=" + scheduleId + "&travelDate=" + day + "/" + month + "/" + year + "&fromStationId=" + fromid + "&toStationId=" + toid + "&boardingPointId=" + boardingid + "&emailId=" + email + "&mobileNbr=" + mobile + "&passengerDetails[0][seatNbr]=" + seats.get(0) + "&passengerDetails[0][name]=" + name + "&passengerDetails[0][age]=" + age + "&passengerDetails[0][sex]=" + gender + "&passengerDetails[1][seatNbr]=" + seats.get(1) + "&passengerDetails[1][name]=" + name2 + "&passengerDetails[1][age]=" + age2 + "&passengerDetails[1][sex]=" + gender2 + "&passengerDetails[2][seatNbr]=" + seats.get(2) + "&passengerDetails[2][name]=" + name3 + "&passengerDetails[2][age]=" + age3 + "&passengerDetails[2][sex]=" + gender3 + "&passengerDetails[3][seatNbr]=" + seats.get(3) + "&passengerDetails[3][name]=" + name4 + "&passengerDetails[3][age]=" + age4 + "&passengerDetails[3][sex]=" + gender4 + "&passengerDetails[4][seatNbr]=" + seats.get(4) + "&passengerDetails[4][name]=" + name5 + "&passengerDetails[4][age]=" + age5 + "&passengerDetails[4][sex]=" + gender5 + "&passengerDetails[5][seatNbr]=" + seats.get(5) + "&passengerDetails[5][name]=" + name6 + "&passengerDetails[5][age]=" + age6 + "&passengerDetails[5][sex]=" + gender6 + "&format=json";
            Log.e(TAG, "\n\n\n\n\n\n\n\ninside six passenger");

        }

        StringRequest request = new StringRequest(Request.Method.GET,
                AppConfig.jsonurl, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                System.out.println("inside ConformBooking"+AppConfig.jsonurl);
                Log.d("booking id", AppConfig.jsonurl);
                Log.d(TAG, response.toString());
                System.out.println("response final " + " : " + response.toString());
                try {

                    JSONObject json = new JSONObject(response);

                    JSONArray Blockseats = json.getJSONArray("BlockSeats");

                    for (int i = 0; i < Blockseats.length(); i++) {

                        JSONObject id = Blockseats.getJSONObject(i);

                        Log.e(TAG, "bookingId:---->" + id.getString("bookingId"));

                        AppConfig.bookingId = id.getString("bookingId");

                    }


                } catch (Exception e) {
                    // TODO: handle exception

                    Log.e(TAG, e.toString());

                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error" + error.toString());
                Toast.makeText(Conform_Booking.this, "no internet connection...!", Toast.LENGTH_SHORT).show();
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




