package com.banyan.mss;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class finish_Activity extends AppCompatActivity {

    String note,cancel,travel_ph,pnr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        note = AppConfig.note;
        travel_ph = AppConfig.travelsPhoneNbr;
        cancel = AppConfig.cancellationDescList;
        pnr = AppConfig.pnr;

        TextView note_txt = (TextView) findViewById(R.id.note_txt);
        note_txt.setText(note);

        TextView ph = (TextView) findViewById(R.id.ph_no_txt);
        ph.setText(travel_ph);

        TextView cancel_txt = (TextView) findViewById(R.id.cancel_txt);
        cancel_txt.setText(cancel);

        TextView pnr_txt = (TextView) findViewById(R.id.pnr_txt);
        pnr_txt.setText(pnr);

        Button ok = (Button) findViewById(R.id.ok_btn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(finish_Activity.this, Home_Activity.class);
                startActivity(intent);
            }
        });

    }

}
