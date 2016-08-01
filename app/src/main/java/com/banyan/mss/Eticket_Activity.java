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

public class Eticket_Activity extends Activity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eticket);


        String from = AppConfig.from;//((GlobalValues) this.getApplication()).getfrom();

        String to = AppConfig.to;//((GlobalValues) this.getApplication()).getto();

        String traveldate = ((GlobalValues) this.getApplication()).getdate();

        String address = ((GlobalValues) this.getApplication()).getaddress();

        String boardingname = ((GlobalValues) this.getApplication()).getboardingname();

        fare = AppConfig.fare1; //((GlobalValues) this.getApplication()).getfare();

        stax = AppConfig.tax1; //((GlobalValues) this.getApplication()).gettax();

        ticketcount = AppConfig.ticket_count; //((GlobalValues) this.getApplication()).getcount();

        email = AppConfig.email;

        mobile = AppConfig.mobile;

        name = AppConfig.name;

        age = AppConfig.age;

        gender = AppConfig.gender;

        name2 = AppConfig.name2;

        age2 = AppConfig.age2;

        gender2 = AppConfig.gender2;

        name3 = AppConfig.name3;

        age3 = AppConfig.age3;

        gender3 = AppConfig.gender3;

        name4 = AppConfig.name4;

        age4 = AppConfig.age4;

        gender4 = AppConfig.gender4;

        name5 = AppConfig.name5;

        age5 = AppConfig.age5;

        gender5 = AppConfig.gender5;

        name6 = AppConfig.name6;

        age6 = AppConfig.age6;

        gender6 = AppConfig.gender6;


        //json parsing data

        fromid = AppConfig.from_id;//getIntent().getExtras().getInt("fid");

        toid = AppConfig.to_id;//getIntent().getExtras().getInt("tid");

        scheduleId = AppConfig.schedule_Id; //getIntent().getExtras().getInt("s_id");

        day = AppConfig.day;//getIntent().getExtras().getString("day");

        month = AppConfig.month;//getIntent().getExtras().getString("month");

        year = AppConfig.year;//getIntent().getExtras().getInt("year");

        seats = AppConfig.seats;//getIntent().getStringArrayListExtra("seats");

        boardingid = AppConfig.boardingid;


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


    Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(Eticket_Activity.this, finish_Activity.class);
                startActivity(inte);

            }
        });

    }


}




