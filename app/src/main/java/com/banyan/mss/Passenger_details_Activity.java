package com.banyan.mss;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Passenger_details_Activity extends Activity implements OnItemSelectedListener {

    int fromid, toid, boardingid;

    String day, month, year;

    int scheduleId;

    int count = 0;

    ProgressDialog mProgressDialog;

    ArrayList<String> seats = new ArrayList<String>();

    ArrayList<String> ladies = new ArrayList<String>();

    final String TAG = "Passenger_details.java";

    String name_s, email_s, phone_s, age_s, gender_s, item_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_passenger_details);


        /*String from=((GlobalValues)this.getApplication()).getfrom();

        String to=((GlobalValues)this.getApplication()).getto();

        String traveldate=((GlobalValues)this.getApplication()).getdate();*/
        TextView dates = (TextView) findViewById(R.id.triplistA_txt_date);
        TextView from = (TextView) findViewById(R.id.triplistA_txt_from);
        TextView to = (TextView) findViewById(R.id.triplistA_txt_to);

        dates.setText(AppConfig.date);
        from.setText(AppConfig.from);
        to.setText(AppConfig.to);

        /*ActionBar actionBar = getActionBar();

        actionBar.setTitle(from+" to "+to);

        actionBar.setSubtitle(traveldate);*/

        fromid = getIntent().getExtras().getInt("fid");

        toid = getIntent().getExtras().getInt("tid");

        scheduleId = getIntent().getExtras().getInt("s_id");

        day = AppConfig.day;//getIntent().getExtras().getString("day");

        month = AppConfig.month; //getIntent().getExtras().getString("month");

        year = AppConfig.year; //getIntent().getExtras().getInt("year");

        seats = getIntent().getStringArrayListExtra("seats");

        ladies = getIntent().getStringArrayListExtra("ladies");

        boardingid = getIntent().getExtras().getInt("boarding_id");


        TextView seatno = (TextView) findViewById(R.id.seatno);

        seatno.setText("SeatNo:" + seats.get(0));

        //check this values in logcat
        Log.e(TAG, "fromid" + fromid);

        Log.e(TAG, "toid" + toid);

        Log.e(TAG, "s_id" + scheduleId);

        Log.e(TAG, "day" + day);

        Log.e(TAG, "month" + month);

        Log.e(TAG, "year" + year);

        Log.e(TAG, "seats" + seats);

        Log.e(TAG, "ladies------------------------------>" + ladies);


        Log.e(TAG, "boarding_id" + boardingid);

        Spinner s = (Spinner) findViewById(R.id.spinner1);

        s.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();

        for (int i = 0; i < ladies.size(); i++) {


            if (ladies.get(i).equalsIgnoreCase(seats.get(0))) {

                categories.add("Female");

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                s.setAdapter(dataAdapter);

                count++;

            }

        }

        if (count == 0) {
            categories.add("Male");

            categories.add("Female");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            s.setAdapter(dataAdapter);

        }

        Button contineu = (Button) findViewById(R.id.contineu_booking);

        contineu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                EditText email = (EditText) findViewById(R.id.email);

                EditText mobile = (EditText) findViewById(R.id.mobile);

                EditText name = (EditText) findViewById(R.id.name);

                EditText age = (EditText) findViewById(R.id.age);


                Log.e(TAG, email.getText().toString());

                Log.e(TAG, mobile.getText().toString());

                Log.e(TAG, name.getText().toString());

                Log.e(TAG, age.getText().toString());

                Log.e(TAG, item_s);

                Log.e(TAG, seats.get(0));


                email_s = email.getText().toString();

                phone_s = mobile.getText().toString();

                name_s = name.getText().toString();

                age_s = age.getText().toString();


                if (email_s.isEmpty() || phone_s.isEmpty() || name_s.isEmpty() || age_s.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All the fields are mandatory", Toast.LENGTH_SHORT).show();

                } else if (phone_s.length() != 10) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                } else {
                    AppConfig.email = email_s;
                    AppConfig.mobile = phone_s;
                    AppConfig.name = name_s;
                    AppConfig.age = age_s;
                    AppConfig.gender = item_s;
                    Intent in = new Intent(Passenger_details_Activity.this, Conform_Booking.class);
                    in.putExtra("email", email_s);
                    in.putExtra("mobile", phone_s);
                    in.putExtra("name", name_s);
                    in.putExtra("age", age_s);
                    in.putExtra("gender", item_s);
                    in.putExtra("s_id", scheduleId);
                    in.putExtra("boarding_id", boardingid);
                    in.putExtra("month", month);
                    in.putExtra("day", day);
                    in.putExtra("year", year);
                    in.putExtra("fid", fromid);
                    in.putExtra("tid", toid);
                    in.putStringArrayListExtra("seats", seats);

                    startActivity(in);
                    finish();

                }
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // TODO Auto-generated method stub

        item_s = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}

