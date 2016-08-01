package com.banyan.mss;

        import java.util.ArrayList;
        import java.util.List;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import android.app.ActionBar;
        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.AsyncTask;
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

public class FourContact_Activity extends Activity implements OnItemSelectedListener{

    int fromid,toid,year,boardingid;

    String day,month;

    int scheduleId;

    int count=0;

    ProgressDialog	mProgressDialog;

    ArrayList<String> seats=new ArrayList<String>();

    ArrayList<String> ladies=new ArrayList<String>();

    final String TAG = "FourContact.java";

    String name_s,email_s,phone_s,age_s,gender_s,item_s,name2_s,age2_s,item_s2,name3_s,age3_s,item_s3,name4_s,age4_s,item_s4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_four_contact);

        TextView dates = (TextView) findViewById(R.id.triplistA_txt_date);
        TextView from = (TextView) findViewById(R.id.triplistA_txt_from);
        TextView to = (TextView) findViewById(R.id.triplistA_txt_to);

        dates.setText(AppConfig.date);
        from.setText(AppConfig.from);
        to.setText(AppConfig.to);

        /*String from=((GlobalValues)this.getApplication()).getfrom();

        String to=((GlobalValues)this.getApplication()).getto();

        String traveldate=((GlobalValues)this.getApplication()).getdate();

        ActionBar actionBar = getActionBar();

        actionBar.setTitle(from+" to "+to);

        actionBar.setSubtitle(traveldate);*/

        fromid=getIntent().getExtras().getInt("fid");

        toid=getIntent().getExtras().getInt("tid");

        scheduleId=getIntent().getExtras().getInt("s_id");

        day=getIntent().getExtras().getString("day");

        month=getIntent().getExtras().getString("month");

        year=getIntent().getExtras().getInt("year");

        seats= getIntent().getStringArrayListExtra("seats");

        ladies=getIntent().getStringArrayListExtra("ladies");

        boardingid=getIntent().getExtras().getInt("boarding_id");

        TextView seatno=(TextView)findViewById(R.id.seatno);

        seatno.setText("SeatNo:"+seats.get(0));


        TextView sseatno=(TextView)findViewById(R.id.sseatno);

        sseatno.setText("SeatNo:"+seats.get(1));

        TextView thrseatno=(TextView)findViewById(R.id.thrseatno);

        thrseatno.setText("SeatNo:"+seats.get(2));

        TextView fourseatno=(TextView)findViewById(R.id.fourseatno);

        fourseatno.setText("SeatNo:"+seats.get(3));

        //check this values in logcat
        Log.e(TAG, "fromid"+fromid);

        Log.e(TAG, "toid"+toid);

        Log.e(TAG, "s_id"+scheduleId);

        Log.e(TAG, "day"+day);

        Log.e(TAG, "month"+month);

        Log.e(TAG, "year"+year);

        Log.e(TAG, "seats"+seats);

        Log.e(TAG, "ladies------------------------------>"+ladies);

        Log.e(TAG, "boarding_id"+boardingid);

        Spinner s=(Spinner)findViewById(R.id.spinner1);

        Spinner sec=(Spinner)findViewById(R.id.spinner3);

        Spinner thr=(Spinner)findViewById(R.id.spinner4);

        Spinner four=(Spinner)findViewById(R.id.spinner5);

        s.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();

        for (int i = 0; i < ladies.size(); i++) {


            if(ladies.get(i).equalsIgnoreCase(seats.get(0)))
            {

                categories.add("Female");

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                s.setAdapter(dataAdapter);

                count++;

            }

        }

        if(count==0)
        {
            categories.add("Male");

            categories.add("Female");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            s.setAdapter(dataAdapter);

        }




        //second co passenger gender

        sec.setOnItemSelectedListener(this);

        List<String> categories1 = new ArrayList<String>();

        for (int i = 0; i < ladies.size(); i++) {


            if(ladies.get(i).equalsIgnoreCase(seats.get(1)))
            {

                categories1.add("Female");

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories1);

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                sec.setAdapter(dataAdapter);

                count++;

            }

        }

        if(count==0)
        {
            categories1.add("Male");

            categories1.add("Female");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories1);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            sec.setAdapter(dataAdapter);

        }




        //three co passenger gender

        thr.setOnItemSelectedListener(this);

        List<String> categories3 = new ArrayList<String>();

        for (int i = 0; i < ladies.size(); i++) {


            if(ladies.get(i).equalsIgnoreCase(seats.get(2)))
            {

                categories3.add("Female");

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories3);

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                thr.setAdapter(dataAdapter);

                count++;

            }

        }

        if(count==0)
        {
            categories3.add("Male");

            categories3.add("Female");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories3);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            thr.setAdapter(dataAdapter);

        }

        //four co passenger gender

        four.setOnItemSelectedListener(this);

        List<String> categories4 = new ArrayList<String>();

        for (int i = 0; i < ladies.size(); i++) {


            if(ladies.get(i).equalsIgnoreCase(seats.get(3)))
            {

                categories4.add("Female");

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories4);

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                four.setAdapter(dataAdapter);

                count++;

            }

        }

        if(count==0)
        {
            categories4.add("Male");

            categories4.add("Female");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories4);

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            four.setAdapter(dataAdapter);

        }








        Button contineu=(Button)findViewById(R.id.contineu_booking);

        contineu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub


                EditText email=(EditText)findViewById(R.id.email);

                EditText mobile=(EditText)findViewById(R.id.mobile);

                EditText name=(EditText)findViewById(R.id.name);

                EditText age=(EditText)findViewById(R.id.age);

                EditText name_2=(EditText)findViewById(R.id.secname);

                EditText age_2=(EditText)findViewById(R.id.secage);

                EditText name_3=(EditText)findViewById(R.id.tname);

                EditText age_3=(EditText)findViewById(R.id.tage);

                EditText name_4=(EditText)findViewById(R.id.fname);

                EditText age_4=(EditText)findViewById(R.id.fage);


                Log.e(TAG,email.getText().toString());

                Log.e(TAG,mobile.getText().toString());

                Log.e(TAG,name.getText().toString());

                Log.e(TAG,age.getText().toString());

                Log.e(TAG,name_2.getText().toString());

                Log.e(TAG,age_2.getText().toString());

                Log.e(TAG,name_3.getText().toString());

                Log.e(TAG,age_3.getText().toString());

                Log.e(TAG,name_4.getText().toString());

                Log.e(TAG,age_4.getText().toString());


                Log.e(TAG,item_s);

                Log.e(TAG,seats.get(0));

                Log.e(TAG,seats.get(1));

                Log.e(TAG,seats.get(2));

                Log.e(TAG,seats.get(3));

                email_s=email.getText().toString();

                phone_s=mobile.getText().toString();

                name_s=name.getText().toString();

                age_s=age.getText().toString();

                name2_s=name_2.getText().toString();

                age2_s=age_2.getText().toString();

                name3_s=name_3.getText().toString();

                age3_s=age_3.getText().toString();

                name4_s=name_4.getText().toString();

                age4_s=age_4.getText().toString();



                if(email_s.isEmpty()||phone_s.isEmpty()||name_s.isEmpty()||age_s.isEmpty()||name2_s.isEmpty()||age2_s.isEmpty()||name3_s.isEmpty()||age3_s.isEmpty()||name4_s.isEmpty()||age4_s.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"All the fields are mandatory",Toast.LENGTH_SHORT ).show();
                }
                else
                {
                    Intent in=new Intent(FourContact_Activity.this,Conform_Booking.class);

                    in.putExtra("email",email_s);
                    in.putExtra("mobile", phone_s);
                    in.putExtra("name", name_s);
                    in.putExtra("age",age_s);
                    AppConfig.email = email_s;
                    AppConfig.mobile = phone_s;
                    AppConfig.name = name_s;
                    AppConfig.age = age_s;

                    in.putExtra("name2", name2_s);
                    in.putExtra("age2",age2_s);
                    AppConfig.name2 = name2_s;
                    AppConfig.age2 = age2_s;

                    in.putExtra("name3", name3_s);
                    in.putExtra("age3",age3_s);
                    AppConfig.name3 = name3_s;
                    AppConfig.age3 = age3_s;

                    in.putExtra("name4", name4_s);
                    in.putExtra("age4",age4_s);
                    AppConfig.name4 = name4_s;
                    AppConfig.age4 = age4_s;

                    in.putExtra("gender", item_s);
                    in.putExtra("gender2", item_s2);
                    in.putExtra("gender3", item_s3);
                    in.putExtra("gender4", item_s4);
                    AppConfig.gender = item_s;
                    AppConfig.gender2 = item_s2;
                    AppConfig.gender3 = item_s3;
                    AppConfig.gender4 = item_s4;

                    in.putExtra("s_id", scheduleId);

                    in.putExtra("boarding_id",boardingid);

                    in.putExtra("month",month);

                    in.putExtra("day",day);

                    in.putExtra("year",year);

                    in.putExtra("fid", fromid);

                    in.putExtra("tid",toid);

                    in.putStringArrayListExtra("seats",seats);

                    startActivity(in);
                }


            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // TODO Auto-generated method stub

        switch (parent.getId()) {

            case R.id.spinner1:
                item_s = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinner3:
                item_s2= parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinner4:
                item_s3= parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinner5:
                item_s4= parent.getItemAtPosition(position).toString();
                break;

        }



        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}

