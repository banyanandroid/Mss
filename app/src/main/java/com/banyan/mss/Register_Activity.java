package com.banyan.mss;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Sangavi on 1/12/2016.
 */
public class Register_Activity extends Activity {

    EditText edt_name, edt_mobilenumber, edt_email, edt_city, edt_password, edt_retypepassword;
    String str_name, str_mobile, str_email, str_city, str_password, str_retype_password;


    Button btn_clear, btn_register,reg_btn_login;

    ProgressDialog pDialog;

    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edt_name = (EditText) findViewById(R.id.reg_edt_name);
        edt_mobilenumber = (EditText) findViewById(R.id.reg_edt_mobilenumber);
        edt_email = (EditText) findViewById(R.id.reg_edt_email);
        edt_city = (EditText) findViewById(R.id.reg_edt_city);
        edt_password = (EditText) findViewById(R.id.reg_edt_password);
        edt_retypepassword = (EditText) findViewById(R.id.reg_edt_re_password);

        btn_clear = (Button) findViewById(R.id.reg_btn_clear);
        btn_register = (Button) findViewById(R.id.reg_btn_register);
        reg_btn_login = (Button) findViewById(R.id.reg_btn_login);

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_name.setText("");
                edt_mobilenumber.setText("");
                edt_email.setText("");
                edt_city.setText("");
                edt_password.setText("");
                edt_retypepassword.setText("");
               edt_email.setText("");

            }
        });

        reg_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_Activity.this, Login_Activity.class);
                startActivity(intent);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_name = edt_name.getText().toString();
                str_mobile = edt_mobilenumber.getText().toString();
                str_email = edt_email.getText().toString();
                str_city = edt_city.getText().toString();
                str_password = edt_password.getText().toString();
                str_retype_password = edt_retypepassword.getText().toString();
                String email = edt_email.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (str_name.equals("")) {
                    edt_name.setError("Enter User Name");
                } else if (str_mobile.equals("")) {
                    edt_mobilenumber.setError("Enter Mobile Number");
                } else if (str_mobile.length() != 10) {
                    edt_mobilenumber.setError("Enter valid Mobile Number");
                } else if (str_email.equals("")) {
                    edt_email.setError("Enter Email");
                } else if (!email.matches(emailPattern)) {
                    edt_email.setError("Enter Valid email address");
                } else if (str_city.equals("")) {
                    edt_city.setError("Enter City");
                } else if (str_password.equals("")) {
                    edt_password.setError("Enter Password");
                } else if (str_retype_password.equals("")) {
                    edt_retypepassword.setError("Enter Re-Password");
                } else if ((str_password.length() < 4) || (str_retype_password.length() < 4)) {
                    Crouton.makeText(Register_Activity.this,
                            "Enter Minimum 4 digit password",
                            Style.INFO)
                            .show();
                } else if (!(str_password.equals(str_retype_password))) {
                    Crouton.makeText(Register_Activity.this,
                            "Password not matched",
                            Style.INFO)
                            .show();
                } else {
                    AppConfig.url_register = "http://www.mssbus.com/api/app/?UserRegistration&Appkey=HaPpY&UserMobile="+str_mobile+"&UserPass="+str_password+"&UserName="+str_name+"&UserEmail="+str_email+"&&UserCity="+str_city+"&format=json";
                    upload();
                }
            }
        });


    }


    private void upload() {

        Alertshow();
        String tag_str = "Uploading";
        System.out.println("FUNCTION CALLED");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.url_register, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Register_activity",
                            "Response: " + response.toString());
                    JSONObject json = new JSONObject(response);
                    hidePDialog();
                    JSONObject user = json.getJSONObject("User");
                    JSONObject status = user.getJSONObject("0");
                    String success = status.getString("StatusMessage");
                    if (success.equalsIgnoreCase("Success")) {
                        String user_name = user.getString("UserName");
                        String user_mobile = user.getString("UserMobile");
                        String user_id = user.getString("UserId");
                        String user_city = user.getString("UserCity");

                        System.out.println("user_name " +user_name);
                        System.out.println("user_mobile " +user_mobile);
                        System.out.println("user_id " +user_id);
                        System.out.println("user_city " +user_city);

                        Crouton.makeText(Register_Activity.this,
                                "Registered Successfully",
                                Style.CONFIRM)
                                .show();

                        Intent i = new Intent(getApplicationContext(), Login_Activity.class);
                        startActivity(i);
                        finish();

                    } else {

                        Crouton.makeText(Register_Activity.this,
                                success,
                                Style.INFO)
                                .show();
                    }

                } catch (JSONException e) {
                    Log.d("JSON Exception", e.toString());
                    Crouton.makeText(Register_Activity.this,
                            "Error While Loading Data",
                            Style.INFO)
                            .show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidePDialog();
                Log.d("ERROR", "Error [" + error + "]");
                Crouton.makeText(Register_Activity.this,
                        "Cannot connect to Server",
                        Style.ALERT)
                        .show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

               /* params.put("name", str_name);
                params.put("mobile", str_mobile);
                params.put("email", str_email);
                params.put("address", str_city);
                params.put("usertype", str_usertype);
                params.put("password", str_password);

                System.out.println("name" + str_name);
                System.out.println("mobile" + str_mobile);
                System.out.println("email" + str_email);
                System.out.println("address" + str_city);
                System.out.println("usertype" + str_usertype);
                System.out.println("password" + str_password);*/

                return params;

            }
        };
        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    public void Alertshow() {
        pDialog = new ProgressDialog(Register_Activity.this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onBackPressed() {
        // your code.
        new AlertDialog.Builder(Register_Activity.this)
                .setTitle("MSS")
                .setMessage("Want to Exit ?")
                .setIcon(R.drawable.ic_logo)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                })
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                // finish();
                                finishAffinity();
                            }
                        }).show();
    }
}