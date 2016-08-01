package com.banyan.mss;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.paynimo.android.payment.PaymentActivity;
import com.paynimo.android.payment.PaymentModesActivity;
import com.paynimo.android.payment.model.Checkout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class CheckoutActivity extends AppCompatActivity {

    private static final String TAG = "CheckoutActivity";
    private static final int MY_SOCKET_TIMEOUT_MS = 0;
    /*queue*/
    public static RequestQueue queue;
    String url;
    /*progress bar*/
    //private ProgressDialog pDialog;
    private AlertDialog dialog;
    String email, mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_checkout);

        email = getIntent().getExtras().getString("email");

        mobile = getIntent().getExtras().getString("mobile");

        Checkout checkout = new Checkout();

        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        //Case 1
        // setting Merchant fields values
        checkout.setMerchantIdentifier("T7159");  //where T3239 is the merchant Code and will be provided by TPSL
        checkout.setTransactionIdentifier("TXN001"); //where TXN001 is the Merchant transaction identifier (alphanumeric no special character allowed)
        checkout.setTransactionReference("ORD0001"); //where ORD0001 is the Merchant transaction reference number
        checkout.setTransactionType(""); //Transaction type, default leave it blank
        checkout.setTransactionSubType(""); //Transaction type, default leave it blank
        checkout.setTransactionCurrency("INR"); //CURRENCY
        checkout.setTransactionAmount(AppConfig.total_fare);//Transaction amount
        checkout.setTransactionDateTime(date); //Transaction data
// setting Consumer fields values
        checkout.setConsumerIdentifier(email); //Consumer Identifier
        checkout.setConsumerEmailID(email); //Consumer email id
        checkout.setConsumerMobileNumber(mobile); //Consumer mobile number
        checkout.setConsumerAccountNo(""); //Default value "", leave it blank
// setting Consumer Cart Item
        checkout.addCartItem("TEST", AppConfig.total_fare, "0.0", "", "", "MSS Travels", "");


        System.out.println("email" + email);
        System.out.println("mobile" + mobile);
        System.out.println("date" + date);
        System.out.println("fare" + AppConfig.total_fare);

        Intent authIntent = new Intent(this, PaymentModesActivity.class);
// Checkout Object
        Log.d("Checkout Request Object",
                checkout.getMerchantRequestPayload().toString());
        authIntent.putExtra(PaymentActivity.ARGUMENT_DATA_CHECKOUT,
                checkout);
// Public Key
        authIntent.putExtra(PaymentActivity.EXTRA_PUBLIC_KEY,
                "1234-6666-6789-56");
// Requested Payment Mode
        authIntent.putExtra(PaymentActivity.EXTRA_REQUESTED_PAYMENT_MODE,
                PaymentActivity.PAYMENT_METHOD_DEFAULT);

        startActivityForResult(authIntent, PaymentActivity.REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PaymentActivity.REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == PaymentActivity.RESULT_OK) {
                Log.d(TAG, "Result Code :" + RESULT_OK);
                if (data != null) {

                    try {
                        Checkout checkout_res = (Checkout) data
                                .getSerializableExtra(PaymentActivity.ARGUMENT_DATA_CHECKOUT);
                        Log.d("Checkout Response Object", checkout_res
                                .getMerchantResponsePayload().toString());

                        String transactionType = checkout_res.
                                getMerchantRequestPayload().getTransaction().getType();
                        String transactionSubType = checkout_res.
                                getMerchantRequestPayload().getTransaction().getSubType();
                        if (transactionType != null && transactionType.equalsIgnoreCase(PaymentActivity.TRANSACTION_TYPE_PREAUTH)
                                && transactionSubType != null && transactionSubType
                                .equalsIgnoreCase(PaymentActivity.TRANSACTION_SUBTYPE_RESERVE)) {
                            // Transaction Completed and Got SUCCESS
                            if (checkout_res.getMerchantResponsePayload()
                                    .getPaymentMethod().getPaymentTransaction()
                                    .getStatusCode().equalsIgnoreCase(PaymentActivity.TRANSACTION_STATUS_PREAUTH_RESERVE_SUCCESS)) {
                                Toast.makeText(getApplicationContext(), "Transaction Status - Success", Toast.LENGTH_SHORT).show();
                                Log.v("TRANSACTION STATUS => ", "SUCCESS");

                                /**
                                 * TRANSACTION STATUS - SUCCESS (status code
                                 * 0200 means success), NOW MERCHANT CAN PERFORM
                                 * ANY OPERATION OVER SUCCESS RESULT
                                 */

                                if (checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getInstruction().getStatusCode().equalsIgnoreCase("")) {
                                    /**
                                     * SI TRANSACTION STATUS - SUCCESS (status
                                     * code 0200 means success)
                                     */
                                    /*Intent inte = new Intent(CheckoutActivity.this, finish_Activity.class);
                                    startActivity(inte);*/

                                    last();
                                    Log.v("TRANSACTION SI STATUS=>",
                                            "SI Transaction Not Initiated");
                                }

                            } // Transaction Completed and Got FAILURE

                            else {
                                // some error from bank side
                                Log.v("TRANSACTION STATUS=>", "FAILURE");
                                Toast.makeText(getApplicationContext(),
                                        "Transaction Status - Failure",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            // Transaction Completed and Got SUCCESS
                            if (checkout_res.getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getStatusCode().equalsIgnoreCase(
                                    PaymentActivity.TRANSACTION_STATUS_SALES_DEBIT_SUCCESS)) {
                                Toast.makeText(getApplicationContext(), "Transaction Status - Success", Toast.LENGTH_SHORT).show();
                                Log.v("TRANSACTION STATUS=>", "SUCCESS");

                                /**
                                 * TRANSACTION STATUS - SUCCESS (status code
                                 * 0300 means success), NOW MERCHANT CAN PERFORM
                                 * ANY OPERATION OVER SUCCESS RESULT
                                 */

                                if (checkout_res.getMerchantResponsePayload().
                                        getPaymentMethod().getPaymentTransaction().
                                        getInstruction().getStatusCode()
                                        .equalsIgnoreCase("")) {
                                    /**
                                     * SI TRANSACTION STATUS - SUCCESS (status
                                     * code 0300 means success)
                                     */
                                    /*Intent inte = new Intent(CheckoutActivity.this, finish_Activity.class);
                                    startActivity(inte);*/

                                    last();
                                    Log.v("TRANSACTION SI STATUS=>",
                                            "SI Transaction Not Initiated");
                                } else if (checkout_res.getMerchantResponsePayload()
                                        .getPaymentMethod().getPaymentTransaction()
                                        .getInstruction()
                                        .getStatusCode().equalsIgnoreCase(
                                                PaymentActivity.TRANSACTION_STATUS_SALES_DEBIT_SUCCESS)) {

                                    /**
                                     * SI TRANSACTION STATUS - SUCCESS (status
                                     * code 0300 means success)
                                     */
                                    /*Intent inte = new Intent(CheckoutActivity.this, finish_Activity.class);
                                    startActivity(inte);*/

                                    last();
                                    Log.v("TRANSACTION SI STATUS=>", "SUCCESS");
                                } else {
                                    /**
                                     * SI TRANSACTION STATUS - Failure (status
                                     * code OTHER THAN 0300 means failure)
                                     */
                                    Log.v("TRANSACTION SI STATUS=>", "FAILURE");
                                }

                            } // Transaction Completed and Got FAILURE
                            else {
                                // some error from bank side
                                Log.v("TRANSACTION STATUS=>", "FAILURE");
                                Toast.makeText(getApplicationContext(),
                                        "Transaction Status - Failure",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                        String result = "StatusCode : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getStatusCode()
                                + " \nStatusMessage : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getStatusMessage()
                                + " \nErrorMessage : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getErrorMessage()
                                + " \nAmount : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod().getPaymentTransaction().getAmount()
                                + " \nDateTime : " + checkout_res.
                                getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getDateTime()
                                + "\nMerchantTransactionIdentifier : "
                                + checkout_res.getMerchantResponsePayload()
                                .getMerchantTransactionIdentifier()
                                + "\nIdentifier : " + checkout_res
                                .getMerchantResponsePayload().getPaymentMethod()
                                .getPaymentTransaction().getIdentifier();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            if (resultCode == PaymentActivity.RESULT_ERROR) {
                Log.d(TAG, "got an error");

                if (data.hasExtra(PaymentActivity.RETURN_ERROR_CODE) &&
                        data.hasExtra(PaymentActivity.RETURN_ERROR_DESCRIPTION)) {
                    String error_code = (String) data
                            .getStringExtra(PaymentActivity.RETURN_ERROR_CODE);
                    String error_desc = (String) data
                            .getStringExtra(PaymentActivity.RETURN_ERROR_DESCRIPTION);

                    Toast.makeText(getApplicationContext(), " Got error :"
                            + error_code + "--- " + error_desc, Toast.LENGTH_LONG)
                            .show();
                    Log.d(TAG + " Code=>", error_code);
                    Log.d(TAG + " Desc=>", error_desc);

                }

            }
            if (resultCode == PaymentActivity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Transaction Aborted by User", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "User pressed back button");

            }
        }
    }

    public void last() {
        try {
            dialog = new SpotsDialog(CheckoutActivity.this);
            dialog.show();
            dialog.setCancelable(false);
            queue = Volley.newRequestQueue(CheckoutActivity.this);
            url = "http://www.mssbus.com/api/app/?BookTicket&Appkey=HaPpY&bookingId=" + AppConfig.bookingId + "&format=json";
            makeJsonObjectRequest();

        } catch (Exception e) {
// TODO: handle exception
        }

    }

    private void makeJsonObjectRequest() {


        System.out.println("method inside");

        StringRequest request = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                System.out.println(url);
                Log.d(TAG, response.toString());
                System.out.println("response final " + " : " + response.toString());
                try {
                    System.out.println("inside Checkout Activity");

                    JSONObject obj = new JSONObject(response);
                    JSONArray book_ticket = obj.getJSONArray("BookTicket");

                    JSONObject station_code = (JSONObject) book_ticket
                            .get(book_ticket.length() - 1);
                    JSONObject status_code = station_code
                            .getJSONObject("Status");
                    String statuscode_str = status_code.getString("StatusCode");

                    if (statuscode_str.equals("200")) {

                        JSONObject pnr_obj = (JSONObject) book_ticket
                                .get(0);
                        String pnr = pnr_obj.getString("PNR");

                        JSONObject note_obj = (JSONObject) book_ticket
                                .get(book_ticket.length() - 2);
                        String note = note_obj.getString("note");

                        JSONObject travelsPhoneNbr_obj = (JSONObject) book_ticket
                                .get(book_ticket.length() - 3);
                        String travelsPhoneNbr = travelsPhoneNbr_obj.getString("travelsPhoneNbr");

                        JSONObject cancellationDescList_obj = (JSONObject) book_ticket
                                .get(book_ticket.length() - 4);
                        String cancellationDescList = cancellationDescList_obj.getString("cancellationDescList");

                        AppConfig.pnr = pnr;
                        AppConfig.note = note;
                        AppConfig.travelsPhoneNbr = travelsPhoneNbr;
                        AppConfig.cancellationDescList = cancellationDescList;
                        Intent inte = new Intent(CheckoutActivity.this, Eticket_Activity.class);
                        inte.putExtra("pnr", pnr);
                        inte.putExtra("note", note);
                        inte.putExtra("travelsPhoneNbr", travelsPhoneNbr);
                        inte.putExtra("cancellationDescList", cancellationDescList);
                        startActivity(inte);

                        System.out.println("TAG" +pnr+"/n" +note+ "/n"+ travelsPhoneNbr +"/n"+ cancellationDescList );

                        //Toast.makeText(getApplicationContext(), (CharSequence) stationName_str, Toast.LENGTH_SHORT).show();
                    } else {

                    }

                    System.out.println("gaya1" + obj);


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
                System.out.println("kiiikik" + error.toString());
                Toast.makeText(CheckoutActivity.this, "no internet connection...!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CheckoutActivity.this);
                builder1.setMessage("Please Check Your Internet Connection");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                makeJsonObjectRequest();
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
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
