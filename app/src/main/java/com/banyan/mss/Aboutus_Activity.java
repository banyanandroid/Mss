package com.banyan.mss;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.HashMap;

public class Aboutus_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SessionManager session;
    String str_id,str_mobile,str_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        //session
        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        str_id = user.get(SessionManager.KEY_ID);
        str_mobile = user.get(SessionManager.KEY_MOBILE);
        str_name = user.get(SessionManager.KEY_LOGINID);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(Aboutus_Activity.this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_bookticket) {
            Intent inm = new Intent(Aboutus_Activity.this, Home_Activity.class);
            startActivity(inm);

        } else if (id == R.id.nav_cancelticket) {
            Intent inm = new Intent(Aboutus_Activity.this, Cancel_Bookdetail_Activity.class);
            startActivity(inm);

        } else if (id == R.id.nav_bookdetails) {
            Intent in = new Intent(Aboutus_Activity.this, Bookdetail_Activity.class);
            startActivity(in);

        }  else if (id == R.id.nav_about) {

            Intent in = new Intent(Aboutus_Activity.this, Aboutus_Activity.class);
            startActivity(in);

        } else if (id == R.id.nav_logout) {

            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
