package com.banyan.mss;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences.Editor;
//import static chat.com.mychat.GCMIntentService.regid;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.Map;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class Login_Activity extends Activity {

    private ProgressDialog pDialog;
    int i;
    private static final String TAG = "Login";

    public static RequestQueue queue;

    TextView txt_title, txt_forgot_password, btn_register;
    String mobile;
    EditText edt_username, edt_password;
    EditText edt1;
    Button btn_login;
    String str_username, str_password, str_name, str_mobile, str_user_id, str_user_status, str_city = "";


    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        txt_forgot_password = (TextView) findViewById(R.id.linkForgotpsw);
        edt_username = (EditText) findViewById(R.id.editTextEmail);
        edt_password = (EditText) findViewById(R.id.editTextPassword);
        btn_login = (Button) findViewById(R.id.buttonLogin);
        btn_register = (TextView) findViewById(R.id.linkSignup);


        // Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_username = edt_username.getText().toString();
                str_password = edt_password.getText().toString();

                if (str_username.equals("")) {
                    edt_username.setError("Enter MobileNumber");
                } else if (str_password.equals("")) {
                    edt_password.setError("Enter Password");
                } else {
                    AppConfig.url_login = "http://www.mssbus.com/api/app/?UserAuthentic&Appkey=HaPpY&UserMobile=" + str_username + "&UserPass=" + str_password + "&format=json";
                    try {
                        pDialog = new ProgressDialog(Login_Activity.this);
                        pDialog.setMessage("Please wait...");
                        pDialog.show();
                        pDialog.setCancelable(false);
                        queue = Volley.newRequestQueue(Login_Activity.this);

                        Function_Login();

                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        });

        txt_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Forgotpassword();

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("REG Clickked");
                Intent reg = new Intent(getApplicationContext(), Register_Activity.class);
                startActivity(reg);
                finish();
            }
        });
    }

    public void Forgotpassword() {
        LayoutInflater li = LayoutInflater.from(Login_Activity.this);
        View promptsView = li
                .inflate(R.layout.activity_alert_forgotpassword, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Login_Activity.this);
        alertDialogBuilder.setTitle("Forgot Password?");

        alertDialogBuilder.setIcon(R.mipmap.ic_forgot_password);
        // alertDialogBuilder.setInverseBackgroundForced(#26A65B);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        edt1 = (EditText) promptsView
                .findViewById(R.id.alert_edt_mobile);


        alertDialogBuilder.setCancelable(false)

                .setPositiveButton("Done",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mobile = String.valueOf(edt1.getText());
                                Toast.makeText(getApplicationContext(), mobile, Toast.LENGTH_SHORT).show();

                                if (mobile.length() == 10) {

                                    AppConfig.url_forgot_psw = "http://www.mssbus.com/api/app/?ForgotPass&Appkey=HaPpY&UserMobile=" + mobile + "&format=json";
                                    try {

                                        pDialog = new ProgressDialog(Login_Activity.this);
                                        pDialog.setMessage("Please wait...");
                                        pDialog.show();
                                        pDialog.setCancelable(false);
                                        queue = Volley.newRequestQueue(Login_Activity.this);
                                        ForgotpasswordSendEmail();

                                    } catch (Exception e) {

                                    }
                                    dialog.cancel();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Enter Valid Mobile number", Toast.LENGTH_SHORT).show();
                                }


                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void ForgotpasswordSendEmail() {

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_forgot_psw, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray user = obj.getJSONArray("User");

                    JSONObject status = (JSONObject) user
                            .get(0);
                    String success = status.getString("StatusMessage");

                    if (success.equalsIgnoreCase("Success")) {

                        i = 1;
                        Crouton.makeText(Login_Activity.this,
                                "Password Successfully sent to Your  Mobile",
                                Style.CONFIRM)
                                .show();

                    } else {
                        i = 0;

                        Crouton.makeText(Login_Activity.this,
                                "Failed Please Try Again",
                                Style.ALERT)
                                .show();

                        System.out.println("FAILED");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    // forgot password send email id

    private void Function_Login() {

        String tag_json_obj = "json_obj_req";
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.url_login, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONObject json = new JSONObject(response);
                    JSONObject user = json.getJSONObject("User");
                    str_user_id = user.getString("UserId");
                    str_name = user.getString("UserName");
                    str_mobile = user.getString("UserMobile");
                    str_city = user.getString("UserCity");

                    JSONObject status = user.getJSONObject("0");
                    String success = status.getString("StatusMessage");
                    if (success.equalsIgnoreCase("Success")) {
                        Crouton.makeText(Login_Activity.this,
                                "Login Successfully",
                                Style.CONFIRM)
                                .show();

                        session.createLoginSession(str_user_id, str_name, str_mobile, str_city, str_user_status);
                        AppConfig.user_id = str_user_id;
                        AppConfig.user_phone = str_mobile;

                        Intent i = new Intent(getApplicationContext(), Home_Activity.class);
                        startActivity(i);
                        finish();

                    } else {
                        i = 0;

                        Crouton.makeText(Login_Activity.this,
                                "Login Failed Please Try Again",
                                Style.ALERT)
                                .show();

                        System.out.println("FAILED");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                pDialog.hide();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", str_username);
                params.put("password", str_password);

                System.out.println("username" + str_username);
                System.out.println("password" + str_password);

                return params;
            }

        };

        // Adding request to request queue
        queue.add(request);
    }

    // GET Student ID


    // End forgot pasword email fnction
    @Override
    public void onBackPressed() {
        // your code.
        new AlertDialog.Builder(Login_Activity.this)
                .setTitle("MSS Travels")
                .setMessage("Want to Exit ?")
                .setIcon(R.mipmap.ic_launcher)
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

